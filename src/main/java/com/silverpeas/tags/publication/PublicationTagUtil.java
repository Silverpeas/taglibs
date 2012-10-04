/*
 * Created on 24 juin 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.silverpeas.tags.publication;

import com.silverpeas.tags.util.EJBDynaProxy;
import com.silverpeas.tags.util.SiteTagUtil;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;
import com.stratelia.webactiv.util.node.control.NodeBm;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.node.model.NodePK;
import com.stratelia.webactiv.util.publication.control.PublicationBm;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;
import com.stratelia.webactiv.util.publication.model.PublicationPK;
import com.stratelia.webactiv.util.publication.model.PublicationRuntimeException;
import java.util.List;

/**
 * @author neysseri
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PublicationTagUtil {

		private PublicationBm 	publicationBm 	= null;
		private NodeBm 			nodeBm 			= null;

		public PublicationTagUtil()
		{
		}

		public boolean isPublicationVisible(PublicationPK pubPK) throws Exception
		{
			PublicationDetail pubDetail = null;
			try
			{
				pubDetail = getPublicationBm().getDetail(pubPK);
			}
			catch (Exception e)
			{
				SilverTrace.info("searchEngine", "SearchEngineTagUtil.isMatchingIndexEntryVisible()", "root.MSG_GEN_PARAM_VALUE", "pubDetail mie.getObjectId() not found ! ");
			}

			if (pubDetail != null)
			{
				String pubStatus = pubDetail.getStatus();
				SilverTrace.info("searchEngine", "PublicationTagUtil.isPublicationVisible()", "root.MSG_GEN_PARAM_VALUE", "pubDetail = "+pubDetail.getName());
				SilverTrace.info("searchEngine", "PublicationTagUtil.isPublicationVisible()", "root.MSG_GEN_PARAM_VALUE", "pubStatus = "+pubStatus);
				if (SiteTagUtil.isDevMode())
				{
					if ("Valid".equals(pubStatus) || "ToValidate".equals(pubStatus) || "Draft".equals(pubStatus)) {
            return true;
          }
				}
				else if (SiteTagUtil.isRecetteMode())
				{
					if (("Valid".equals(pubStatus) || "ToValidate".equals(pubStatus)) && isPublicationInVisiblePath(pubDetail)) {
            return true;
          }
				}
				else
				{
					if ("Valid".equals(pubStatus) && isPublicationInVisiblePath(pubDetail)) {
            return true;
          }
				}
			}
			return false;
		}

		/**
		 * @param pub la publication à vérifier
		 * @return true si la publication est dans un thème "visible sur le web" (de la racine au thème)
		 * @throws Exception
		 */
		private boolean isPublicationInVisiblePath(PublicationDetail pub) throws Exception
		{
			//Recupere tous les thèmes dans lesquels la publication est classée
			List 	allFathers 			= (List) getPublicationBm().getAllFatherPK(pub.getPK());
			NodePK 	fatherPK;
			for (int f=0; f<allFathers.size();f++)
			{
				fatherPK = (NodePK) allFathers.get(f);
				//Recupere le chemin de la racine jusqu'au père
				List path = (List) getNodeBm().getAnotherPath(fatherPK);
				if (isAVisiblePath(path))
				{
					return true;
				}
			}
			return false;
		}

		private boolean isAVisiblePath(List path)
		{
			boolean 	isVisible 	= true;
			NodeDetail 	node;
			for (int n=0; n<path.size();n++)
			{
				node = (NodeDetail) path.get(n);
				if ("Invisible".equals(node.getStatus()))
				{
					return false;
				}
			}
			return isVisible;
		}

		private PublicationBm getPublicationBm()
		{
			if (publicationBm == null) {
				try
				{
					publicationBm = (PublicationBm)EJBDynaProxy.createProxy(JNDINames.PUBLICATIONBM_EJBHOME, PublicationBm.class);
				}
				catch (Exception e)
				{
					throw new PublicationRuntimeException("PublicationTagUtil.getPublicationBm", SilverpeasRuntimeException.ERROR,"root.EX_CANT_GET_REMOTE_OBJECT",e);
				}
			}
			return publicationBm;
		}

		private NodeBm getNodeBm()
		{
			if (nodeBm == null) {
				try
				{
					nodeBm = (NodeBm)EJBDynaProxy.createProxy(JNDINames.NODEBM_EJBHOME, NodeBm.class);
				}
				catch (Exception e)
				{
					throw new PublicationRuntimeException("PublicationTagUtil.getNodeBm", SilverpeasRuntimeException.ERROR,"root.EX_CANT_GET_REMOTE_OBJECT",e);
				}
			}
			return nodeBm;
		}
}
