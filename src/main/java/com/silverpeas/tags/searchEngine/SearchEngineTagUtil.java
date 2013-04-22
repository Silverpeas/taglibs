/**
 * Copyright (C) 2000 - 2012 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.tags.searchEngine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.silverpeas.search.SearchEngineFactory;
import org.silverpeas.search.indexEngine.model.SpaceComponentPair;
import org.silverpeas.search.searchEngine.model.MatchingIndexEntry;
import org.silverpeas.search.searchEngine.model.QueryDescription;
import org.silverpeas.search.searchEngine.model.ScoreComparator;

import com.silverpeas.admin.ejb.AdminBmRuntimeException;
import com.silverpeas.admin.ejb.AdminBusiness;
import com.silverpeas.pdc.ejb.PdcBm;
import com.silverpeas.pdc.ejb.PdcBmRuntimeException;
import com.silverpeas.tags.publication.PublicationTagUtil;
import com.silverpeas.tags.util.SiteTagUtil;
import com.silverpeas.util.StringUtil;

import com.stratelia.silverpeas.contentManager.GlobalSilverContent;
import com.stratelia.silverpeas.contentManager.GlobalSilverContentI18N;
import com.stratelia.silverpeas.pdc.model.SearchContext;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.util.DateUtil;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;
import com.stratelia.webactiv.util.publication.model.PublicationPK;

public class SearchEngineTagUtil implements java.io.Serializable {

  private static final String COMPONENT_PUBLICATION_PREFIX = "kmelia";
  private static final String COMPONENT_FORUM_PREFIX = "forums";
  private static final long serialVersionUID = 1L;
  private String query;
  private String spaceId;
  private String componentId;
  private String authorId;
  private String afterDate;
  private String beforeDate;
  private SearchContext pdcContext;
  private String userId;
  private Map<String, String> xmlQuery;
  private String xmlTemplate;
  private String xmlTitle;
  private boolean publicationEnabled;
  private boolean forumEnabled;
  private PdcBm pdcBm = null;
  private AdminBusiness adminBm = null;
  private PublicationTagUtil publicationTagUtil = null;
  private final String ALL_SPACES = "*";
  private final String ALL_AUTHORS = "*";
  private final String ALL_COMPONENTS = "*";

  public SearchEngineTagUtil(String query, String spaceId, String componentId, String authorId,
      String afterDate, String beforeDate, String publicationEnabled, String forumEnabled) {
    this.query = query;
    this.spaceId = spaceId;
    this.componentId = componentId;
    this.authorId = authorId;
    this.afterDate = afterDate;
    this.beforeDate = beforeDate;
    this.publicationEnabled = "true".equals(publicationEnabled);
    this.forumEnabled = "true".equals(forumEnabled);
  }

  public void setPdcContext(SearchContext pdcContext) {
    this.pdcContext = pdcContext;
  }

  public void setXmlQuery(Map<String, String> xmlQuery) {
    this.xmlQuery = xmlQuery;
  }

  public void setXmlTemplate(String xmlTemplate) {
    this.xmlTemplate = xmlTemplate;
  }

  public Collection getResults() throws Exception {
    // build the search
    QueryDescription theQuery = new QueryDescription(getQuery());

    // Set the identity of the user who processing the search
    theQuery.setSearchingUser(getUserId());

    // Set the list of all components which are available for the user
    buildSpaceComponentAvailableForUser(theQuery, getSpaceId(), getComponentId());

    // Set the filter according dates
    String afterDateSQL = getAfterDate();
    theQuery.setRequestedCreatedAfter(afterDateSQL);
    SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()",
        "root.MSG_GEN_PARAM_VALUE",
        "After date set !");

    String beforeDateSQL = getBeforeDate();
    theQuery.setRequestedCreatedBefore(beforeDateSQL);
    SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()",
        "root.MSG_GEN_PARAM_VALUE",
        "Before date set !");

    // Set the filter on a particular author
    theQuery.setRequestedAuthor(getAuthorId());
    SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()",
        "root.MSG_GEN_PARAM_VALUE",
        "authorId set !");

    List<String> alSilverContentIds = new ArrayList<String>();
    List<GlobalSilverContent> silverContents = new ArrayList<GlobalSilverContent>();

    if (getPdcContext() != null && !getPdcContext().isEmpty()) {
      // the search context is not empty. We have to search all silvercontentIds according to query
      // settings
      SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()",
          "root.MSG_GEN_PARAM_VALUE", "pdc context search is not empty !");

      // get all componentIds
      List<String> alComponentIds = new ArrayList<String>();
      Set<SpaceComponentPair> spaceComponentPairs = theQuery.getSpaceComponentPairSet();
      if (spaceComponentPairs != null) {
        for (SpaceComponentPair spaceComponentPair : spaceComponentPairs) {
          alComponentIds.add(spaceComponentPair.getComponent());
        }
      }

      SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()",
          "root.MSG_GEN_PARAM_VALUE", "component id list built !");

      List alSilverContents;

      boolean visibilitySensitive = true;
      if (SiteTagUtil.isDevMode() || SiteTagUtil.isRecetteMode()) {
        // Le site n'est pas en mode production. Une recherche est faite sur le PDC sans tenir
        // compte de la colonne isVisible.
        visibilitySensitive = false;
      }
      alSilverContents = getPdcBm().findGlobalSilverContents(getPdcContext(), alComponentIds,
          getAuthorId(), afterDateSQL, beforeDateSQL, true, visibilitySensitive);

      GlobalSilverContent silverContent;
      if (getQuery() != null && getQuery().length() > 0) {
        // extract the silvercontent ids
        for (int sc = 0; sc < alSilverContents.size(); sc++) {
          silverContent = (GlobalSilverContent) alSilverContents.get(sc);
          alSilverContentIds.add(silverContent.getId());
        }
      } else {
        for (int sc = 0; sc < alSilverContents.size(); sc++) {
          silverContent = (GlobalSilverContent) alSilverContents.get(sc);
          if (isSilverContentVisible(silverContent)) {
            silverContents.add(getTranslatedGlobalSilveContent(silverContent, null));
          }
        }
        return silverContents;
      }

      SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()",
          "root.MSG_GEN_PARAM_VALUE", "silverContentId list returned !");
    }

    if (xmlQuery != null && !xmlQuery.isEmpty() && xmlTemplate != null) {
      Map<String, String> newXmlQuery = new HashMap<String, String>();

      Set<String> keys = xmlQuery.keySet();
      Iterator<String> i = keys.iterator();
      while (i.hasNext()) {
        String key = i.next();
        String value = xmlQuery.get(key);
        value = value.trim().replaceAll("##", " AND ");
        newXmlQuery.put(xmlTemplate + "$$" + key, value);

        SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()",
            "root.MSG_GEN_PARAM_VALUE",
            "newXmlQuery.put(" + xmlTemplate + "$$" + key + "," + value + ")");
      }

      theQuery.setXmlQuery(newXmlQuery);
    }

    if (xmlTitle != null && !xmlTitle.isEmpty() && !"null".equals(xmlTitle)) {
      theQuery.setXmlTitle(xmlTitle);
      if (theQuery.getXmlQuery() == null) {
        theQuery.setXmlQuery(new HashMap<String, String>()); // Mandatory to launch xml search
      }
    }

    List<MatchingIndexEntry> result = null;
    if (StringUtil.isDefined(theQuery.getQuery()) || theQuery.getXmlQuery() != null
        || StringUtil.isDefined(theQuery.getXmlTitle())) {
      // launch the full text search

      SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()",
          "root.MSG_GEN_PARAM_VALUE", "search processed !");

      // retrieve results
      List<MatchingIndexEntry> fullTextResult =
          SearchEngineFactory.getSearchEngine().search(theQuery)
              .getEntries();
      SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()",
          "root.MSG_GEN_PARAM_VALUE", "results retrieved !");

      if (getPdcContext() != null && !getPdcContext().isEmpty()) {
        // We retain only objects which are presents in the both search result list
        result = mixedSearch(fullTextResult, alSilverContentIds);
        SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()",
            "root.MSG_GEN_PARAM_VALUE", "both searches have been mixed !");
      } else {
        result = fullTextResult;
        SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()",
            "root.MSG_GEN_PARAM_VALUE", "results are full text results !");
      }
    }

    if (result != null) {
      // get each result according to result's list
      GlobalSilverContent silverContent;
      List<String> returnedObjects = new LinkedList<String>();
      for (MatchingIndexEntry mie : result) {
        SilverTrace
            .info("searchEngine", "SearchEngineTagUtil.getResults()",
                "root.MSG_GEN_PARAM_VALUE",
                "mie.getTitle() = " + mie.getTitle() + ", mie.getObjectType() = " +
                mie.getObjectType());
        if (mie.getTitle().endsWith("wysiwyg.txt")) {
          // we don't show it as result.
        } else {
          // Added by NEY - 22/01/2004
          // Some explanations to lines below
          // If a publication have got the word "truck" in its title and an associated wysiwyg which
          // content the same word
          // The search engine will return 2 same lines (One for the publication and the other for
          // the wysiwyg)
          // Following lines filters one and only one line. The choice between both lines is not
          // important.
          if ("Wysiwyg".equals(mie.getObjectType())) {
            // We must search if the eventual associated Publication have not been already added to
            // the result
            String objectIdAndObjectType =
                mie.getObjectId() + "&&Publication&&" + mie.getComponent();
            if (returnedObjects.contains(objectIdAndObjectType)) {
              // the Publication have already been added
              continue;
            } else {
              objectIdAndObjectType = mie.getObjectId() + "&&Wysiwyg&&" + mie.getComponent();
              returnedObjects.add(objectIdAndObjectType);
            }
          } else if ("Publication".equals(mie.getObjectType())) {
            // We must search if the eventual associated Wysiwyg have not been already added to the
            // result
            String objectIdAndObjectType = mie.getObjectId() + "&&Wysiwyg&&" + mie.getComponent();
            if (returnedObjects.contains(objectIdAndObjectType)) {
              // the Wysiwyg have already been added
              continue;
            } else {
              objectIdAndObjectType = mie.getObjectId() + "&&Publication&&" + mie.getComponent();
              returnedObjects.add(objectIdAndObjectType);
            }
          }

          silverContent = matchingIndexEntry2SilverContent(mie);
          if (silverContent != null) {
            silverContents.add(silverContent);
          }
        }
      }
      SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()",
          "root.MSG_GEN_PARAM_VALUE", "results transformed !");
    }

    return silverContents;
  }

  private String getUserId() {
    if (userId == null) {
      return SiteTagUtil.getUserId();
    } else {
      return userId;
    }
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  private String getSiteLanguage() {
    return SiteTagUtil.getLanguage();
  }

  private void buildSpaceComponentAvailableForUser(QueryDescription queryDescription,
      String spaceId,
      String componentId) throws Exception {
    SilverTrace.info("searchEngine", "SearchEngineTagUtil.buildSpaceComponentAvailableForUser()",
        "root.MSG_GEN_PARAM_VALUE", "spaceId = " + spaceId + ", componentId = " + componentId);
    if (spaceId == null || spaceId.length() == 0) {
      spaceId = ALL_SPACES;
    }
    if (componentId == null || componentId.length() == 0) {
      componentId = ALL_COMPONENTS;
    }

    if (spaceId.equals(ALL_SPACES)) {
      // No restriction on spaces.
      List allowedSpaceIds = getAdminBm().getAvailableSpaceIds(getUserId());

      for (int i = 0; i < allowedSpaceIds.size(); i++) {
        buildSpaceComponentAvailableForUser(queryDescription, (String) allowedSpaceIds.get(i),
            ALL_COMPONENTS);
      }
    } else {
      // The search is restricted to one given space
      if (componentId.equals(ALL_COMPONENTS)) {
        // No restriction on components of the selected space
        // First, we get all available components on this space
        List allowedComponentIds = getAdminBm().getAvailCompoIds(spaceId, getUserId());
        for (int i = 0; i < allowedComponentIds.size(); i++) {
          buildSpaceComponentAvailableForUser(queryDescription, spaceId,
              (String) allowedComponentIds.get(i));
        }

        // Second, we recurse on each sub space of this space
        List subSpaceIds = getAdminBm().getAvailableSubSpaceIds(spaceId, getUserId());
        if (subSpaceIds != null) {
          for (int i = 0; i < subSpaceIds.size(); i++) {
            buildSpaceComponentAvailableForUser(queryDescription, (String) subSpaceIds.get(i),
                ALL_COMPONENTS);
          }
        }
      } else {
        if ((publicationEnabled && componentId.startsWith(COMPONENT_PUBLICATION_PREFIX))
            || (forumEnabled && componentId.startsWith(COMPONENT_FORUM_PREFIX))) {
          queryDescription.addSpaceComponentPair(spaceId, componentId);
        }
      }
    }
  }

  private List<MatchingIndexEntry> mixedSearch(List<MatchingIndexEntry> ie, List<String> objectIds)
      throws Exception {
    SilverTrace.info("searchEngine", "SearchEngineTagUtil.mixedSearch()",
        "root.MSG_GEN_PARAM_VALUE", "objectIds = " + objectIds.toString());
    // la liste basicSearchList ne contient maintenant que les silverContentIds des documents
    // trouvés
    // mais ces documents sont également dans le tableau résultat de la recherche classique
    // il faut donc créer un tableau de MatchingIndexEntry pour afficher le resultat
    List<MatchingIndexEntry> result = new ArrayList<MatchingIndexEntry>(ie.size());
    for (String objectId : objectIds) {
      MatchingIndexEntry mie = getMatchingIndexEntry(ie, objectId);
      if (mie != null) {
        result.add(mie);
        SilverTrace.info("searchEngine", "SearchEngineTagUtil.mixedSearch()",
            "root.MSG_GEN_PARAM_VALUE", "common objectId = " + mie.getObjectId());
      }
    }
    Collections.sort(result, ScoreComparator.comparator);
    return result;
  }

  /**
   * Dans un tableau de MatchingIndexEntry, on recherche l'objet MatchingIndexEntry qui a comme
   * objectId l'internalContentId
   */
  private MatchingIndexEntry getMatchingIndexEntry(List<MatchingIndexEntry> ie,
      String internalContentId) throws Exception {
    for (MatchingIndexEntry entry : ie) {
      if (entry.getObjectId().equals(internalContentId)) {
        return entry;
      }
    }
    return null;
  }

  private GlobalSilverContent matchingIndexEntry2SilverContent(MatchingIndexEntry mie) throws
      Exception {
    SilverTrace.info("searchEngine", "SearchEngineTagUtil.matchingIndexEntry2SilverContent()",
        "root.MSG_GEN_PARAM_VALUE", "mie = " + mie.toString());
    GlobalSilverContent silverContent = null;
    if (mie != null && isMatchingIndexEntryVisible(mie)) {
      silverContent = new GlobalSilverContent(mie.getTitle(getSiteLanguage()), mie
          .getPreview(getSiteLanguage()), mie.getObjectId(), null, mie.getComponent(), mie
          .getCreationDate(), mie.getCreationUser());
      silverContent.setScore(mie.getScore());
      silverContent.setType(mie.getObjectType());
    }
    return silverContent;
  }

  private boolean isMatchingIndexEntryVisible(MatchingIndexEntry mie) throws Exception {
    String objectType = mie.getObjectType();
    String theComponentId = mie.getComponent();
    if ("Publication".equals(objectType) || "Wysiwyg".equals(objectType)) {
      return getPublicationTagUtil().isPublicationVisible(new PublicationPK(mie.getObjectId(),
          "useless", theComponentId));
    } else if (objectType != null && objectType.startsWith("Attachment")) {
      if (theComponentId.startsWith(COMPONENT_FORUM_PREFIX)) {
        return getPublicationTagUtil().isPublicationVisible(new PublicationPK(mie.getObjectId(),
            "useless", theComponentId));
      } else {
        return true;
      }
    } else if ("Node".equals(objectType)) {
      return true;
    } else if ("Forum".equals(objectType)) {
      return true;
    } else if ("Message".equals(objectType)) {
      return true;
    }
    return false;
  }

  private boolean isSilverContentVisible(GlobalSilverContent silverContent) throws Exception {
    // Actually, only Publication are indexed
    if (silverContent.getInstanceId().startsWith("kmelia")) {
      return getPublicationTagUtil().isPublicationVisible(new PublicationPK(silverContent.getId(),
          "useless", silverContent.getInstanceId()));
    } else {
      return true;
    }
  }

  private String getQuery() {
    return query;
  }

  private String getSpaceId() {
    if (spaceId == null) {
      spaceId = "*";
    }
    return spaceId;
  }

  private String getComponentId() {
    if (componentId == null) {
      componentId = "*";
    }
    return componentId;
  }

  private String getAuthorId() {
    if (authorId != null && !authorId.equals(ALL_AUTHORS)) {
      return authorId;
    } else {
      return null;
    }
  }

  private String getAfterDate() throws Exception {
    return date2SQLDate(afterDate);
  }

  private String getBeforeDate() throws Exception {
    return date2SQLDate(beforeDate);
  }

  private SearchContext getPdcContext() {
    return pdcContext;
  }

  /**
   * Get translated Publication in current site lang or lang as parameter
   * @param gsc
   * @param language
   * @return GlobalSilverContent
   */
  public GlobalSilverContent getTranslatedGlobalSilveContent(GlobalSilverContent gsc,
      String language) {
    String lang = null;
    if (StringUtil.isDefined(language)) {
      lang = language;
    } else if (StringUtil.isDefined(getSiteLanguage())) {
      lang = getSiteLanguage();
    }
    if (StringUtil.isDefined(lang)) {
      GlobalSilverContentI18N gsci18n = (GlobalSilverContentI18N) gsc
          .getTranslation(getSiteLanguage());
      if (gsci18n != null) {
        gsc.setTitle(gsci18n.getName());
      }
    }
    return gsc;
  }

  private String date2SQLDate(String date) throws Exception {
    SilverTrace.info("searchEngine", "SearchEngineTagUtil.date2SQLDate()",
        "root.MSG_GEN_PARAM_VALUE", "date = " + date);
    if (date != null && !date.isEmpty()) {
      Date parsedDate = DateUtil.parse(date, "dd/MM/yyyy");
      SilverTrace.info("searchEngine", "SearchEngineTagUtil.date2SQLDate()",
          "root.MSG_GEN_PARAM_VALUE", "parsedDate = " + parsedDate.toString());
      String sqlDate = DateUtil.date2SQLDate(parsedDate);
      SilverTrace.info("searchEngine", "SearchEngineTagUtil.date2SQLDate()",
          "root.MSG_GEN_PARAM_VALUE", "sqlDate = " + sqlDate);
      return sqlDate;
    } else {
      return null;
    }
  }

  private PdcBm getPdcBm() {
    if (pdcBm == null) {
      try {
        pdcBm = EJBUtilitaire.getEJBObjectRef(JNDINames.PDCBM_EJBHOME, PdcBm.class);
      } catch (Exception e) {
        throw new PdcBmRuntimeException("SearchEngineTagUtil.getPdcBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return pdcBm;
  }

  private AdminBusiness getAdminBm() {
    if (adminBm == null) {
      try {
        adminBm = EJBUtilitaire.getEJBObjectRef(JNDINames.ADMINBM_EJBHOME, AdminBusiness.class);
      } catch (Exception e) {
        throw new AdminBmRuntimeException("SearchEngineTagUtil.getAdminBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return adminBm;
  }

  private PublicationTagUtil getPublicationTagUtil() {
    if (publicationTagUtil == null) {
      publicationTagUtil = new PublicationTagUtil();
    }
    return publicationTagUtil;
  }

  public void setXmlTitle(String xmlTitle) {
    this.xmlTitle = xmlTitle;
  }
}