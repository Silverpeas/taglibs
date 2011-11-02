package com.silverpeas.tags.navigation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

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
		themetracker = (KmeliaTagUtil) pageContext.getAttribute(tt);
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
	
	public String getUrl() throws Exception {
		PublicationDetail pub = null;				
		NodeDetail node = null;
		if (idTopic != null) {
			node = themetracker.getTopic(idTopic);	
		} else if (idPub != null) {
			pub = themetracker.getPublicationDetail(idPub);
			Collection<NodePK>  fathers = pub.getPublicationBm().getAllFatherPK(pub.getPK());
			Iterator<NodePK> it = fathers.iterator();
			NodePK pk = it.next();
			node = themetracker.getTopic(pk.getId());				
		} else {
			throw new Exception("Pas de publication ou de theme");
		}
		return generateFullSemanticPath(node, pub);
	}
}