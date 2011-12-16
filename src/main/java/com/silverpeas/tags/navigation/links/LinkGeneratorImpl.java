package com.silverpeas.tags.navigation.links;

import java.rmi.RemoteException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringEscapeUtils;

import com.silverpeas.tags.kmelia.KmeliaTagUtil;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;

public class LinkGeneratorImpl implements LinkGenerator {	
	/**
	 * Construction de l'url de l'item.
	 * @param pageContext
	 * @param themetracker
	 * @param node
	 * @param idsTopicsRoots
	 * @param pub
	 * @param prefixId
	 * @return
	 * @throws RemoteException
	 */
	public String generateFullSemanticPath(PageContext pageContext, KmeliaTagUtil themetracker, NodeDetail node, String idsTopicsRoots, PublicationDetail pub, String prefixId) throws RemoteException {
		StringBuffer path = new StringBuffer();		
		path.append(((HttpServletRequest) pageContext.getRequest()).getContextPath());
		path.append("/");
		StringTokenizer nodes = new StringTokenizer(node.getPath(), "/");
		boolean beginPath = false;
		while (nodes.hasMoreTokens()) {
			String nodeId = nodes.nextToken();
			if (beginPath) {
				NodeDetail n = themetracker.getTopic(nodeId);
				path.append(tranformName(n.getName()));
				path.append("/");
			} else {
				beginPath = isRootTopic(nodeId, idsTopicsRoots);
			}
		}
		path.append(tranformName(node.getName()));
		path.append("/");
		if (pub != null) {
			path.append(tranformName(pub.getName()));
			path.append("/");
		}		
		if (prefixId != null) {
			path.append(prefixId);
		}	
		path.append(node.getId());
		return path.toString();
	}
	
	/**
	 * Changement des caractères "spéciaux" du nom d'un élément.
	 * @param name
	 * @return
	 */
	private String tranformName(String name) {		
		name = name.toLowerCase();
		name = stripAccents(name);
		name = name.replace("'","_");
		name = name.replace(" ","_");
		name = name.replace("?","");
		name = name.replaceAll("\\<.*?>","");
		name = StringEscapeUtils.escapeHtml(name);
		return name;
	}

	/**
	 * Suppresion des accents d'une chaine de caractères.
	 * @param s
	 * @return
	 */
	public String stripAccents(String s) {
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		s = s.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		return s;	
	}
	
	/**
	 * Vérifie que un theme est un theme racine.
	 * @param nodeId
	 * @param idsTopicsRoots
	 * @return
	 */
	private boolean isRootTopic(String nodeId, String idsTopicsRoots) {
		StringTokenizer rootNodes = new StringTokenizer(idsTopicsRoots, ",");
		while (rootNodes.hasMoreTokens()) {
			String rootNodeId = rootNodes.nextToken();
			if (rootNodeId.equals(nodeId)) {
				return true;
			}
		}
		return false;
	}
}
