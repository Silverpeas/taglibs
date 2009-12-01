package com.silverpeas.tags.searchEngine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.silverpeas.admin.ejb.AdminBm;
import com.silverpeas.admin.ejb.AdminBmRuntimeException;
import com.silverpeas.pdc.ejb.PdcBm;
import com.silverpeas.pdc.ejb.PdcBmRuntimeException;
import com.silverpeas.tags.publication.PublicationTagUtil;
import com.silverpeas.tags.util.EJBDynaProxy;
import com.silverpeas.tags.util.SiteTagUtil;
import com.silverpeas.util.StringUtil;
import com.stratelia.silverpeas.contentManager.GlobalSilverContent;
import com.stratelia.silverpeas.contentManager.GlobalSilverContentI18N;
import com.stratelia.silverpeas.pdc.model.SearchContext;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.searchEngine.control.ejb.SearchEngineBm;
import com.stratelia.webactiv.searchEngine.model.MatchingIndexEntry;
import com.stratelia.webactiv.searchEngine.model.QueryDescription;
import com.stratelia.webactiv.searchEngine.model.ScoreComparator;
import com.stratelia.webactiv.util.DateUtil;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;
import com.stratelia.webactiv.util.indexEngine.model.SpaceComponentPair;
import com.stratelia.webactiv.util.publication.model.PublicationPK;

public class SearchEngineTagUtil implements java.io.Serializable
{

	private static final String COMPONENT_PUBLICATION_PREFIX = "kmelia";
	private static final String COMPONENT_FORUM_PREFIX = "forums";

	private String			query;
	private String			spaceId;
	private String			componentId;
	private String			authorId;
	private String			afterDate;
	private String			beforeDate;
	private SearchContext	pdcContext;
	private String			userId;
	private Hashtable		xmlQuery;
	private String			xmlTemplate;
	private String			xmlTitle;
	private boolean			publicationEnabled;
	private boolean			forumEnabled;

	private PdcBm				pdcBm				= null;
	private AdminBm				adminBm				= null;
	private SearchEngineBm		searchEngineBm		= null;
	private PublicationTagUtil	publicationTagUtil	= null;

	private final String ALL_SPACES		= "*";
	private final String ALL_AUTHORS	= "*";
	private final String ALL_COMPONENTS = "*";

    public SearchEngineTagUtil(String query, String spaceId, String componentId, String authorId,
    	String afterDate, String beforeDate, String publicationEnabled, String forumEnabled)
    {
		this.query = query;
		this.spaceId = spaceId;
		this.componentId = componentId;
		this.authorId = authorId;
		this.afterDate = afterDate;
		this.beforeDate = beforeDate;
		this.publicationEnabled = "true".equals(publicationEnabled);
		this.forumEnabled = "true".equals(forumEnabled);
    }

	public void setPdcContext(SearchContext pdcContext)
	{
		this.pdcContext = pdcContext;
	}

	public void setXmlQuery(Hashtable xmlQuery)
	{
		this.xmlQuery = xmlQuery;
	}

	public void setXmlTemplate(String xmlTemplate)
	{
		this.xmlTemplate = xmlTemplate;
	}

	public Collection getResults() throws Exception
	{
		//build the search
		QueryDescription query = new QueryDescription(getQuery());

		//Set the identity of the user who processing the search
		query.setSearchingUser(getUserId());

		//Set the list of all components which are available for the user
		buildSpaceComponentAvailableForUser(query, getSpaceId(), getComponentId());

		//Set the filter according dates
		String afterDateSQL = getAfterDate();
		query.setRequestedCreatedAfter(afterDateSQL);
		SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "After date set !");

		String beforeDateSQL = getBeforeDate();
		query.setRequestedCreatedBefore(beforeDateSQL);
		SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "Before date set !");

		//Set the filter on a particular author
		query.setRequestedAuthor(getAuthorId());
		SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "authorId set !");

		List alSilverContentIds	= new ArrayList();
		ArrayList silverContents = new ArrayList();

		if (getPdcContext() != null && !getPdcContext().isEmpty()) {
			//the search context is not empty. We have to search all silvercontentIds according to query settings
			SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "pdc context search is not empty !");

			//get all componentIds
			ArrayList	alComponentIds		= new ArrayList();
			Set			spaceComponentPairs = query.getSpaceComponentPairSet();
			if (spaceComponentPairs != null)
			{
				Iterator			iterator			= spaceComponentPairs.iterator();
				SpaceComponentPair	spaceComponentPair	= null;
				while (iterator.hasNext())
				{
					spaceComponentPair = (SpaceComponentPair) iterator.next();
					alComponentIds.add(spaceComponentPair.getComponent());
				}
			}

			SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "component id list built !");

			List alSilverContents = null;

			boolean visibilitySensitive = true;
			if (SiteTagUtil.isDevMode() || SiteTagUtil.isRecetteMode())
			{
				//Le site n'est pas en mode production. Une recherche est faite sur le PDC sans tenir compte de la colonne isVisible.
				visibilitySensitive = false;
			}
			alSilverContents = getPdcBm().findGlobalSilverContents(getPdcContext(), alComponentIds, getAuthorId(), afterDateSQL, beforeDateSQL, true, visibilitySensitive);

			GlobalSilverContent	silverContent = null;
			if (getQuery() != null && getQuery().length() > 0)
			{
				//extract the silvercontent ids
				for (int sc=0; sc<alSilverContents.size(); sc++)
				{
					silverContent = (GlobalSilverContent) alSilverContents.get(sc);
					alSilverContentIds.add(silverContent.getId());
				}
			}
			else
			{
				for (int sc=0; sc<alSilverContents.size(); sc++)
				{
					silverContent = (GlobalSilverContent) alSilverContents.get(sc);
					if (isSilverContentVisible(silverContent))
						silverContents.add(getTranslatedGlobalSilveContent(silverContent, null));
				}
				return silverContents;
			}

			SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "silverContentId list returned !");
		}

		if (xmlQuery != null && !xmlQuery.isEmpty() && xmlTemplate != null)
		{
			Hashtable newXmlQuery = new Hashtable();

			Set keys = xmlQuery.keySet();
			Iterator i = keys.iterator();
			String key = null;
			String value = null;
			while (i.hasNext())
			{
				key = (String) i.next();
				value = (String) xmlQuery.get(key);
				value = value.trim().replaceAll("##", " AND ");
				newXmlQuery.put(xmlTemplate+"$$"+key, value);

				SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "newXmlQuery.put("+xmlTemplate+"$$"+key+","+value+")");
			}

			query.setXmlQuery(newXmlQuery);
		}

		if (xmlTitle != null && xmlTitle.length() > 0 && !"null".equals(xmlTitle))
		{
			query.setXmlTitle(xmlTitle);
			if (query.getXmlQuery() == null)
				query.setXmlQuery(new Hashtable()); //Mandatory to launch xml search
		}

		MatchingIndexEntry[] result = null;
		if (StringUtil.isDefined(query.getQuery()) || query.getXmlQuery() != null || StringUtil.isDefined(query.getXmlTitle()))
		{
			//launch the full text search
			try
			{
				getSearchEngineBm().search(query);
			}
			catch (Exception e)
			{
				SilverTrace.warn("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "search method failed with query = "+query+" probably due to a parse exception !");
				return silverContents;
			}

			SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "search processed !");

			//retrieve results
			MatchingIndexEntry[] fullTextResult = getSearchEngineBm().getRange(0, getSearchEngineBm().getResultLength());
			SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "results retrieved !");

			if (getPdcContext() != null && !getPdcContext().isEmpty()) {
				// We retain only objects which are presents in the both search result list
				result = mixedSearch(fullTextResult, alSilverContentIds);
				SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "both searches have been mixed !");
			} else {
				result = fullTextResult;
				SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "results are full text results !");
			}
		}

		if (result != null)
		{
			//get each result according to result's list
			MatchingIndexEntry	mie				= null;
			GlobalSilverContent silverContent	= null;
			LinkedList			returnedObjects = new LinkedList();
			for (int r=0; r<result.length; r++)
			{
				mie = result[r];
				SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "mie.getTitle() = "+mie.getTitle()+", mie.getObjectType() = "+mie.getObjectType());
				if (mie.getTitle().endsWith("wysiwyg.txt"))
				{
					//we don't show it as result.
				}
				else
				{
					//Added by NEY - 22/01/2004
					//Some explanations to lines below
					//If a publication have got the word "truck" in its title and an associated wysiwyg which content the same word
					//The search engine will return 2 same lines (One for the publication and the other for the wysiwyg)
					//Following lines filters one and only one line. The choice between both lines is not important.
					if ("Wysiwyg".equals(mie.getObjectType()))
					{
						//We must search if the eventual associated Publication have not been already added to the result
						String objectIdAndObjectType = mie.getObjectId()+"&&Publication&&"+mie.getComponent();
						if (returnedObjects.contains(objectIdAndObjectType))
						{
							//the Publication have already been added
							continue;
						} else {
							objectIdAndObjectType = mie.getObjectId()+"&&Wysiwyg&&"+mie.getComponent();
							returnedObjects.add(objectIdAndObjectType);
						}
					}
					else if ("Publication".equals(mie.getObjectType()))
					{
						//We must search if the eventual associated Wysiwyg have not been already added to the result
						String objectIdAndObjectType = mie.getObjectId()+"&&Wysiwyg&&"+mie.getComponent();
						if (returnedObjects.contains(objectIdAndObjectType))
						{
							//the Wysiwyg have already been added
							continue;
						} else {
							objectIdAndObjectType = mie.getObjectId()+"&&Publication&&"+mie.getComponent();
							returnedObjects.add(objectIdAndObjectType);
						}
					}

					silverContent = matchingIndexEntry2SilverContent(mie);
					if (silverContent != null)
						silverContents.add(silverContent);
				}
			}
			SilverTrace.info("searchEngine", "SearchEngineTagUtil.getResults()", "root.MSG_GEN_PARAM_VALUE", "results transformed !");
		}

		return silverContents;
	}

	private String getUserId()
	{
		if (userId == null)
			return SiteTagUtil.getUserId();
		else
			return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	private String getSiteLanguage()
	{
		return SiteTagUtil.getLanguage();
	}

	private void buildSpaceComponentAvailableForUser(QueryDescription queryDescription, String spaceId, String componentId) throws Exception
	{
		SilverTrace.info("searchEngine", "SearchEngineTagUtil.buildSpaceComponentAvailableForUser()", "root.MSG_GEN_PARAM_VALUE", "spaceId = "+spaceId+", componentId = "+componentId);
		if (spaceId == null || spaceId.length() == 0)
			spaceId = ALL_SPACES;
		if (componentId == null || componentId.length() == 0)
			componentId = ALL_COMPONENTS;

		if (spaceId.equals(ALL_SPACES))
		{
			//No restriction on spaces.
			List allowedSpaceIds = getAdminBm().getAvailableSpaceIds(getUserId());

			for (int i = 0; i < allowedSpaceIds.size(); i++)
			{
				buildSpaceComponentAvailableForUser(queryDescription, (String) allowedSpaceIds.get(i), ALL_COMPONENTS);
			}
		}
		else
		{
			//The search is restricted to one given space
			if (componentId.equals(ALL_COMPONENTS))
			{
				//No restriction on components of the selected space
				//First, we get all available components on this space
				List allowedComponentIds = getAdminBm().getAvailCompoIds(spaceId, getUserId());
				for (int i = 0; i < allowedComponentIds.size(); i++)
					buildSpaceComponentAvailableForUser(queryDescription, spaceId, (String) allowedComponentIds.get(i));

				//Second, we recurse on each sub space of this space
				List subSpaceIds = getAdminBm().getAvailableSubSpaceIds(spaceId, getUserId());
				if (subSpaceIds != null)
				{
					for (int i = 0; i < subSpaceIds.size(); i++)
						buildSpaceComponentAvailableForUser(queryDescription, (String) subSpaceIds.get(i), ALL_COMPONENTS);
				}
			}
			else
			{
				if ((publicationEnabled && componentId.startsWith(COMPONENT_PUBLICATION_PREFIX))
					|| (forumEnabled && componentId.startsWith(COMPONENT_FORUM_PREFIX)))
				{
					queryDescription.addSpaceComponentPair(spaceId, componentId);
				}
			}
		}
	}

	private MatchingIndexEntry[] mixedSearch(MatchingIndexEntry[] ie, List objectIds) throws Exception {
		SilverTrace.info("searchEngine", "SearchEngineTagUtil.mixedSearch()", "root.MSG_GEN_PARAM_VALUE", "objectIds = "+objectIds.toString());

        // la liste basicSearchList ne contient maintenant que les silverContentIds des documents trouvés
        // mais ces documents sont également dans le tableau résultat de la recherche classique
        // il faut donc créer un tableau de MatchingIndexEntry pour afficher le resultat
        ArrayList result = new ArrayList();

		String				objectId	= null;
		MatchingIndexEntry	mie			= null;
		for (int i=0; i<objectIds.size(); i++)
		{
			objectId = (String) objectIds.get(i);
			mie = getMatchingIndexEntry(ie, objectId);
			if (mie != null) {
				result.add(mie);
				SilverTrace.info("searchEngine", "SearchEngineTagUtil.mixedSearch()", "root.MSG_GEN_PARAM_VALUE", "common objectId = "+mie.getObjectId());
			}
		}

		Collections.sort(result, ScoreComparator.comparator);
        return (MatchingIndexEntry[]) result.toArray(new MatchingIndexEntry[0]);
    }

	/**
     * Dans un tableau de MatchingIndexEntry, on recherche l'objet MatchingIndexEntry
     * qui a comme objectId l'internalContentId
     */
    private MatchingIndexEntry getMatchingIndexEntry(MatchingIndexEntry[] ie, String internalContentId) throws Exception {
        MatchingIndexEntry res = null;
        for (int i = 0; i < ie.length; i++) {
            // on parcourt le tableau résultats de la recherche classique
            // et on retourne le MatchingIndexEntry correspondant à l'internalContentId
            if ((ie[i].getObjectId()).equals(internalContentId)) {
                res = ie[i];
                break;
            }
        }
        return res;
    }

	private GlobalSilverContent matchingIndexEntry2SilverContent(MatchingIndexEntry mie) throws Exception
	{
		SilverTrace.info("searchEngine", "SearchEngineTagUtil.matchingIndexEntry2SilverContent()", "root.MSG_GEN_PARAM_VALUE", "mie = "+mie.toString());
		GlobalSilverContent silverContent = null;
		if (mie != null && isMatchingIndexEntryVisible(mie))
		{
			silverContent = new GlobalSilverContent(mie.getTitle(getSiteLanguage()), mie.getPreview(getSiteLanguage()), mie.getObjectId(), null, mie.getComponent(), mie.getCreationDate(), mie.getCreationUser());
			silverContent.setScore(mie.getScore());
			silverContent.setType(mie.getObjectType());
		}
		return silverContent;
	}

	private boolean isMatchingIndexEntryVisible(MatchingIndexEntry mie) throws Exception
	{
		String objectType 	= mie.getObjectType();
		String componentId 	= mie.getComponent();
		if ("Publication".equals(objectType) || "Wysiwyg".equals(objectType))
		{
			return getPublicationTagUtil().isPublicationVisible(new PublicationPK(mie.getObjectId(), "useless", componentId));
		}
		else if (objectType != null && objectType.startsWith("Attachment"))
		{
			if (componentId.startsWith(COMPONENT_FORUM_PREFIX))
			{
				return getPublicationTagUtil().isPublicationVisible(new PublicationPK(mie.getObjectId(), "useless", componentId));
			}
			else
			{
				return true;
			}
		}
		else if ("Node".equals(objectType))
		{
			return true;
		}
		else if ("Forum".equals(objectType))
		{
			return true;
		}
		else if ("Message".equals(objectType))
		{
			return true;
		}
		return false;
	}

	private boolean isSilverContentVisible(GlobalSilverContent silverContent) throws Exception
	{
		//Actually, only Publication are indexed
		if (silverContent.getInstanceId().startsWith("kmelia"))
		{
			return getPublicationTagUtil().isPublicationVisible(new PublicationPK(silverContent.getId(), "useless", silverContent.getInstanceId()));
		}
		else
			return true;
	}

	private String getQuery()
	{
		return query;
	}

	private String getSpaceId()
	{
		if (spaceId==null)
			spaceId = "*";
		return spaceId;
	}

	private String getComponentId()
	{
		if (componentId==null)
			componentId = "*";
		return componentId;
	}

	private String getAuthorId()
	{
		if (authorId != null && !authorId.equals(ALL_AUTHORS))
			return authorId;
		else
			return null;
	}

	private String getAfterDate() throws Exception
	{
		return date2SQLDate(afterDate);
	}

	private String getBeforeDate() throws Exception
	{
		return date2SQLDate(beforeDate);
	}

	private SearchContext getPdcContext()
	{
		return pdcContext;
	}

	/**
	 * Get translated Publication in current site lang or lang as parameter
	 * @param gsc
	 * @param language
	 * @return GlobalSilverContent
	 */
	public GlobalSilverContent getTranslatedGlobalSilveContent(GlobalSilverContent gsc, String language)
	{
		String lang = null;
		if (StringUtil.isDefined(language))
		{
			lang = language;
		}
		else if (StringUtil.isDefined(getSiteLanguage()))
		{
			lang = getSiteLanguage();
		}
		if (StringUtil.isDefined(lang))
		{
			GlobalSilverContentI18N gsci18n = (GlobalSilverContentI18N) gsc.getTranslation(getSiteLanguage());
			if (gsci18n != null)
				gsc.setTitle(gsci18n.getName());
		}
		return gsc;
	}

	private String date2SQLDate(String date) throws Exception
	{
		SilverTrace.info("searchEngine", "SearchEngineTagUtil.date2SQLDate()", "root.MSG_GEN_PARAM_VALUE", "date = "+date);
		if (date != null && !date.equals("")) {
			Date parsedDate = DateUtil.parse(date, "dd/MM/yyyy");
			SilverTrace.info("searchEngine", "SearchEngineTagUtil.date2SQLDate()", "root.MSG_GEN_PARAM_VALUE", "parsedDate = "+parsedDate.toString());
			String sqlDate = DateUtil.date2SQLDate(parsedDate);
			SilverTrace.info("searchEngine", "SearchEngineTagUtil.date2SQLDate()", "root.MSG_GEN_PARAM_VALUE", "sqlDate = "+sqlDate);
			return sqlDate;
		} else {
			return null;
		}
	}

	private PdcBm getPdcBm()
	{
		if (pdcBm == null)
		{
			try
			{
				pdcBm = (PdcBm)EJBDynaProxy.createProxy(JNDINames.PDCBM_EJBHOME, PdcBm.class);
			}
			catch (Exception e)
			{
				throw new PdcBmRuntimeException("SearchEngineTagUtil.getPdcBm", SilverpeasRuntimeException.ERROR,"root.EX_CANT_GET_REMOTE_OBJECT",e);
			}
		}
		return pdcBm;
	}

	private AdminBm getAdminBm()
	{
		if (adminBm == null) {
			try
			{
				adminBm = (AdminBm)EJBDynaProxy.createProxy(JNDINames.ADMINBM_EJBHOME, AdminBm.class);
			}
			catch (Exception e)
			{
				throw new AdminBmRuntimeException("SearchEngineTagUtil.getAdminBm", SilverpeasRuntimeException.ERROR,"root.EX_CANT_GET_REMOTE_OBJECT",e);
			}
		}
		return adminBm;
    }

	private SearchEngineBm getSearchEngineBm()
	{
		if (searchEngineBm == null) {
			try
			{
				searchEngineBm = (SearchEngineBm)EJBDynaProxy.createProxy(JNDINames.SEARCHBM_EJBHOME, SearchEngineBm.class);
			}
			catch (Exception e)
			{
				throw new PdcBmRuntimeException("SearchEngineTagUtil.getPdcBm", SilverpeasRuntimeException.ERROR,"root.EX_CANT_GET_REMOTE_OBJECT",e);
			}
		}
		return searchEngineBm;
    }

	private PublicationTagUtil getPublicationTagUtil()
	{
		if (publicationTagUtil==null)
		{
			publicationTagUtil = new PublicationTagUtil();
		}
		return publicationTagUtil;
	}

	public void setXmlTitle(String xmlTitle) {
		this.xmlTitle = xmlTitle;
	}
}