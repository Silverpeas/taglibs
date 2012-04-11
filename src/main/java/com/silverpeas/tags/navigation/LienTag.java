package com.silverpeas.tags.navigation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.silverpeas.tags.kmelia.KmeliaTagUtil;
import com.silverpeas.tags.navigation.links.LinkGeneratorFactory;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.node.model.NodePK;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;

/**
 * Tag permettant de générer un lien à partir de l'arborescence du site.
 * @author svuillet
 */
public class LienTag extends TagSupport {

	private static final long serialVersionUID = -2611520269052008032L;
	private String idTopic = null;
	private String idsRootsTopics = null;
	private String idPub = null;
	private String prefixId = null;
	private KmeliaTagUtil themetracker = null;
	private String excludeTopicNamed = null;
	private String usePageNumber = "true";
	
	/**
	 * Si true : ajoute le numéro de page à la fin de l'url.
	 * Si false : ajoute l'id de la publication à la fin de l'url.
	 * @param usePageNumber
	 */
	public void setUsePageNumber(String usePageNumber) {
		this.usePageNumber = usePageNumber;
	}
	
	/**
	 * Exclusion d'un chemin (cas du multi-emplacement avec un emplacement technique).
	 * @param excludeTopicNamed
	 */
	public void setExcludeTopicNamed(String excludeTopicNamed) {
		this.excludeTopicNamed = excludeTopicNamed;
	}
	
	/**
	 * Racines des arborescences.
	 * @param idsRootsTopics
	 */
	public void setIdsRootsTopics(String idsRootsTopics) {
		this.idsRootsTopics = idsRootsTopics;
	}
	
	/**
	 * Emplacement du contenu.
	 * @param idTopic
	 */
	public void setIdPub(String idPub) {
		this.idPub = idPub;
	}
	
	/**
	 * Emplacement du contenu.
	 * @param idTopic
	 */
	public void setIdTopic(String idTopic) {
		this.idTopic = idTopic;
	}
	
	/**
	 * Source de données.
	 * @param tt
	 */
	public void setThemetracker(String tt) {
		int scope = pageContext.getAttributesScope(tt);	
		themetracker = (KmeliaTagUtil) pageContext.getAttribute(tt, scope);
	}
	
	/**
	 * Prefix id.
	 * @param prefixId
	 */
	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	
	/**
	 * Only for scriptlet.
	 */
	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}
	
	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();									
			out.print(getUrl());			
		} catch (Exception e) {
			e.printStackTrace();
		}
								
        return SKIP_BODY;
	}
	
	/**
	 * Construction de l'url de l'item.
	 * @param node
	 * @return
	 * @throws RemoteException
	 */
	private String generateFullSemanticPath(NodeDetail node, PublicationDetail pub) throws RemoteException {
		try {
			return LinkGeneratorFactory.getInstance().newLinkGenerator().generateFullSemanticPath(pageContext, themetracker, node, idsRootsTopics, pub, prefixId);
		} catch (Exception e) {
			throw new RemoteException("", e);
		}
	}
	
	/**
	 * Selection des éléments nécessaires à la construction de l'url de l'item.
	 * @return
	 * @throws Exception
	 */
	public String getUrl() throws Exception {
		PublicationDetail pub = null;				
		NodeDetail node = null;
		String page = "";
		if (idTopic != null) {
			node = themetracker.getTopic(idTopic);	
		} else if (idPub != null) {
			pub = themetracker.getPublicationDetail(idPub);
			Collection<NodePK>  fathers = pub.getPublicationBm().getAllFatherPK(pub.getPK());
			Iterator<NodePK> it = fathers.iterator();
			while (it.hasNext()) {
				NodePK pk = it.next();			
				node = themetracker.getTopic(pk.getId());
				if (excludeTopicNamed != null) {
					StringTokenizer st = new StringTokenizer(node.getFullPath(), "/");
					boolean found = false;
					while(st.hasMoreTokens()) {
						if (themetracker.getTopic(st.nextToken()).getName().equals(excludeTopicNamed)) {							
							found = true;
							break;
						}
					}
					if (found == false) break;
				} else {
					break;
				}
			}
			
			@SuppressWarnings("unchecked")
			Collection<PublicationDetail> pubs = themetracker.getPublicationsByTopic(node.getId()+",order,asc");	
			Iterator<PublicationDetail> iPubs = pubs.iterator();
			int order = 1;
			while (iPubs.hasNext()) {
				PublicationDetail p = iPubs.next();	
				if (p.getId().equals(pub.getId())) break;
				order++;
			}
			if (order > 1) page = "-" + order;
		} else {
			throw new Exception("Pas de publication ou de theme");
		}
		
		if (Boolean.parseBoolean(usePageNumber)) {
			return generateFullSemanticPath(node, pub) + page;
		} else {
			return generateFullSemanticPath(node, pub) + "-" + idPub;
		}
	}
}
