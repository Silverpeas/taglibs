/**
 * Copyright (C) 2000 - 2015 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of the GPL, you may
 * redistribute this Program in connection with Free/Libre Open Source Software ("FLOSS")
 * applications as described in Silverpeas's FLOSS exception. You should have received a copy of the
 * text describing the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.silverpeas.tags.quickinfo;

import com.silverpeas.tags.ComponentTagUtil;
import com.silverpeas.tags.util.SiteTagUtil;
import com.silverpeas.tags.util.VisibilityException;
import com.silverpeas.util.StringUtil;
import com.silverpeas.util.i18n.I18NHelper;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.kmelia.model.KmeliaRuntimeException;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;
import com.stratelia.webactiv.util.publication.control.PublicationBm;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;
import com.stratelia.webactiv.util.publication.model.PublicationI18N;
import com.stratelia.webactiv.util.publication.model.PublicationPK;
import com.stratelia.webactiv.util.publication.model.PublicationRuntimeException;
import org.silverpeas.wysiwyg.control.WysiwygController;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class QuickInfoTagUtil extends ComponentTagUtil {

  private PublicationBm publicationBm = null;
  private String componentId = null;
  private String spaceId = "useless";

  public QuickInfoTagUtil(String componentId, String userId) {
    super(componentId, userId);
    this.componentId = componentId;
  }

  public QuickInfoTagUtil(String componentId, String userId, String language) {
    super(componentId, userId);
    this.componentId = componentId;
  }

  private String getSiteLanguage() {
    return SiteTagUtil.getLanguage();
  }

  /**
   * Get QuickinfoDetail
   *
   * @param id
   * @return PublicationDetail
   * @throws RemoteException
   * @throws VisibilityException
   */
  public PublicationDetail getQuickInfoDetail(String id) throws RemoteException,
      VisibilityException {
    SilverTrace.info("quickinfo", "QuickInfoTagUtil.getQuickInfoDetail()",
        "root.MSG_GEN_ENTER_METHOD", "id = " + id);
    PublicationDetail result =
        getPublicationBm().getDetail(new PublicationPK(id, spaceId, componentId));
    result = getTranslatedPublication(result, null);

    if (!StringUtil.isDefined(result.getDescription())) {
      result.setDescription(getPublicationHTMLContent(id));
    }
    SilverTrace.info("quickinfo", "QuickInfoTagUtil.getQuickInfoDetail()",
        "root.MSG_GEN_ENTER_METHOD", "result = " + result);
    return result;
  }

  /**
   * Get html wysiwyg content
   *
   * @param pubId
   * @return parsed htmlCode
   * @throws RemoteException
   * @throws VisibilityException
   */
  public String getPublicationHTMLContent(String pubId) throws RemoteException, VisibilityException {
    SilverTrace.info("quickinfo", "QuickInfoTagUtil.getPublicationHTMLContent()",
        "root.MSG_GEN_ENTER_METHOD", "pubId = " + pubId);
    String wysiwygContent = getWysiwyg(pubId);
    SilverTrace.info("quickinfo", "QuickInfoTagUtil.getPublicationHTMLContent()",
        "root.MSG_GEN_ENTER_METHOD", "wysiwygContent = " + wysiwygContent);
    return parseHtmlContent(wysiwygContent);
  }

  /**
   * Get wysiwyg content file
   *
   * @param pubPK
   * @return htmlCode
   */
  public String getWysiwyg(PublicationPK pubPK) {
    String wysiwygContent = null;
    try {
      wysiwygContent = WysiwygController.load(pubPK.getInstanceId(), pubPK.getId(), I18NHelper.defaultLanguage);    		  
    } catch (Exception e) {
      throw new KmeliaRuntimeException("quickinfo.getWysiwyg()", SilverpeasRuntimeException.ERROR,
          "quickinfo.EX_IMPOSSIBLE_DOBTENIR_LE_WYSIWYG", e);
    }
    return wysiwygContent;
  }

  /**
   * Get wysiwyg content file
   *
   * @param pubId
   * @return htmlCode
   */
  public String getWysiwyg(String pubId) throws RemoteException, VisibilityException {
    SilverTrace.info("quickinfo", "QuickInfoTagUtil.getWysiwyg()", "root.MSG_GEN_ENTER_METHOD",
        "pubId = " + pubId);
    String wysiwyg = getWysiwyg(getPublicationPK(pubId));
    return parseHtmlContent(wysiwyg);
  }

  /**
   * There's a problem with images. Links to images are like
   * http://server_name:port/silverpeas/FileServer/image.jpg?... The servlet FileServer is used.
   * This works in the traditional context of silverpeas. But, it does not works in the taglibs
   * context because the FileServer is securised. In taglibs context, the servlet WebFileServer must
   * be used. The string http://server_name:port/silverpeas/FileServer must be replaced by
   * http://webServer_name:port/webContext/WebFileServer The web context is provided by the tag
   * Site.
   */
  private String parseHtmlContent(String htmlContent) {
    if (htmlContent != null && htmlContent.length() > 0) {
      String webContext = SiteTagUtil.getFileServerLocation();

      int place = -1;
      String debut = null;
      int srcPlace = -1;
      String suite = null;
      while (htmlContent.indexOf("/FileServer/") > -1) {
        place = htmlContent.indexOf("/FileServer/");
        debut = htmlContent.substring(0, place);
        srcPlace = debut.lastIndexOf("\"");
        debut = debut.substring(0, srcPlace + 1);

        suite =
            htmlContent.substring(place + (new String("/FileServer/")).length(), htmlContent
            .length());
        htmlContent = debut + webContext + suite;
      }

      place = -1;
      debut = null;
      srcPlace = -1;
      suite = null;
      while (htmlContent.indexOf("/GalleryInWysiwyg/") > -1) {
        place = htmlContent.indexOf("/GalleryInWysiwyg/");
        debut = htmlContent.substring(0, place);
        srcPlace = debut.lastIndexOf("\"");
        debut = debut.substring(0, srcPlace + 1);

        suite =
            htmlContent.substring(place + (new String("/GalleryInWysiwyg/")).length(), htmlContent
            .length());
        htmlContent = debut + webContext + suite;
      }

      int finPath = 0;
      int debutPath = 0;
      StringBuffer newWysiwygText = new StringBuffer();
      String link = null;
      while (htmlContent.indexOf("href=\"", finPath) > -1) {
        debutPath = htmlContent.indexOf("href=\"", finPath);
        debutPath += 6;

        newWysiwygText.append(htmlContent.substring(finPath, debutPath));

        finPath = htmlContent.indexOf("\"", debutPath);
        link = htmlContent.substring(debutPath, finPath);

        int d = link.indexOf("../../");
        if (d != -1) {
          // C'est un lien relatif
          d += 6;
          newWysiwygText.append("/silverpeas/");
          newWysiwygText.append(link.substring(d, link.length()));
        } else {
          newWysiwygText.append(link);
        }
      }
      newWysiwygText.append(htmlContent.substring(finPath, htmlContent.length()));

      return newWysiwygText.toString();
    }
    return htmlContent;
  }

  private PublicationPK getPublicationPK(String pubId) {
    return new PublicationPK(pubId, componentId);
  }

  /**
   * Get All quickinfos (with wysiwyg in description)
   *
   * @return a collection of infos
   * @throws RemoteException
   * @throws VisibilityException
   */
  public Collection<PublicationDetail> getAllQuickInfos() throws RemoteException,
      VisibilityException {
    Collection<PublicationDetail> pubsDetail;
    if (SiteTagUtil.isProdMode()) {
      pubsDetail = getPublicationBm().getDetailsByBeginDateDesc(new PublicationPK("useless",
          spaceId, componentId), 1000);
    } else {
      pubsDetail = getPublicationBm().getOrphanPublications(new PublicationPK("useless",
          spaceId, componentId));
    }
    if (pubsDetail != null) {
      Collection<PublicationDetail> newPubsDetail = new ArrayList<PublicationDetail>();
      for (PublicationDetail pubDetail : pubsDetail) {
        pubDetail = getTranslatedPublication(pubDetail, null);
        if (!StringUtil.isDefined(pubDetail.getDescription())) {
          pubDetail.setDescription(getWysiwyg(pubDetail.getId()));
        }
        newPubsDetail.add(pubDetail);
      }
      return newPubsDetail;
    }
    return Collections.<PublicationDetail>emptyList();
  }

  /**
   * Get translated Publication in current site lang or lang as parameter
   *
   * @param pubDetail
   * @return PublicationDetail
   */
  private PublicationDetail getTranslatedPublication(PublicationDetail pubDetail, String language) {
    String lang = null;
    if (StringUtil.isDefined(language)) {
      lang = language;
    } else if (StringUtil.isDefined(getSiteLanguage())) {
      lang = getSiteLanguage();
    }
    if (StringUtil.isDefined(lang)) {
      PublicationI18N pubDetaili18n = (PublicationI18N) pubDetail.getTranslation(getSiteLanguage());
      if (pubDetaili18n != null) {
        pubDetail.setName(pubDetaili18n.getName());
      }
    }
    return pubDetail;
  }

  private PublicationBm getPublicationBm() {
    if (publicationBm == null) {
      try {
        publicationBm = EJBUtilitaire.getEJBObjectRef(JNDINames.PUBLICATIONBM_EJBHOME,
            PublicationBm.class);
      } catch (Exception e) {
        throw new PublicationRuntimeException("QuickInfoTagUtil.getPublicationBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return publicationBm;
  }
}
