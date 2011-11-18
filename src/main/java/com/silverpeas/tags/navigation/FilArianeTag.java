package com.silverpeas.tags.navigation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringEscapeUtils;

import com.silverpeas.tags.kmelia.KmeliaTagUtil;
import com.silverpeas.tags.navigation.links.LinkGeneratorFactory;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;

/**
 * Tag permettant de générer un fil d'ariane à partir d'une position donnée.
 * @author svuillet
 */
public class FilArianeTag extends TagSupport {

	private static final long serialVersionUID = -8729790317427902342L;
	private KmeliaTagUtil themetracker = null;
	private String idsTopicsRoots;
	private String idCurrentTopic;
	private String separator;
	private String pageNumber;
	private boolean displayPubName = false;
	private String prefixIdHierarchyByIdTopicRoot = null;
	private String prefixIdHierarchyByIdTopicRoot = null;

	/**
	 * Noms des classes CSS (séparés par des virgules) à appliquer à chaque niveau de l'arborescence.
	 * Si le nombre de classes CSS est inférieur à la profondeur de l'arborescence, 
	 * alors la dernières classes CSS est appliquée au niveau inférieurs.
	 * @param classNamesHierarchy
	 */
	public void setClassNamesHierarchy(String classNamesHierarchy) {
		this.classNamesHierarchy = classNamesHierarchy;
	}
	
	/**
	 * Syntaxe : rootID1[prefix1,prefix2];rootID2[prefix3, prefix4]
	 * @param prefixIdHierarchyByIdTopicRoot
	 */
	public void setPrefixIdHierarchyByIdTopicRoot(String prefixIdHierarchyByIdTopicRoot) {
		this.prefixIdHierarchyByIdTopicRoot = prefixIdHierarchyByIdTopicRoot;
	}
	
	/**
	 * Source de données.
	 * @param tt
	 */
	public void setThemetracker(String tt) {
		themetracker = (KmeliaTagUtil) pageContext.getAttribute(tt);
	}
	
	/**
	 * Point d'arrêt du parcours de l'arborescence (plusieurs alternatives sont possibles, les séparer pas ",").
	 * @param idsTopicsRoots
	 */
	public void setIdsTopicsRoots(String idsTopicsRoots) {
		this.idsTopicsRoots = idsTopicsRoots;
	}
	
	/**
	 * Point de démarrage du parcours de l'arborescence.
	 * @param idCurrentTopic
	 */
	public void setIdCurrentTopic(String idCurrentTopic) {
		this.idCurrentTopic = idCurrentTopic;
	}
	
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public void setDisplayPubName(String displayPubName) {
		this.displayPubName = Boolean.parseBoolean(displayPubName);
	}
	
	@Override
	public int doStartTag() throws JspException {

		try {
			JspWriter out = pageContext.getOut();	
			NodeDetail current = themetracker.getTopic(idCurrentTopic);
			String path = generateBreadcrumb(current);
			out.println(path);			
		} catch (Exception e) {
			e.printStackTrace();
		}
								
        return SKIP_BODY;
	}
	

	/**
	 * Construction du fil d'ariane.
	 * @param node
	 * @return
	 * @throws RemoteException
	 */
	private String generateBreadcrumb(NodeDetail node) throws RemoteException {
		StringBuffer path = new StringBuffer();		
		
		StringTokenizer nodes = new StringTokenizer(node.getPath(), "/");
		boolean beginPath = false;
		int level=0;
		while (nodes.hasMoreTokens()) {
			String nodeId = nodes.nextToken();
			if (beginPath) {
				NodeDetail n = themetracker.getTopic(nodeId);
				path.append("<a href='");
				path.append(generateFullSemanticPath(n));
				path.append("' title='");
				path.append(StringEscapeUtils.escapeHtml(n.getDescription()));
				path.append("' class='");
				path.append(getCssClass(level));
				path.append("'>");
				path.append(transformLabel(n.getName()));
				path.append("</a>");
				path.append(separator);
				level++;
			} else {
				beginPath = isRootTopic(nodeId);
			}			
		}
		if (beginPath) path.append(transformLabel(node.getName()));
		
		if (displayPubName) {
			@SuppressWarnings("rawtypes")
			Collection pubs = themetracker.getPublicationsByTopic(String.valueOf(node.getId()+",order,asc"));
			@SuppressWarnings("rawtypes")
			Iterator iPubs = pubs.iterator();
			int i = 1;
			while (iPubs.hasNext()) {
				PublicationDetail pub = (PublicationDetail) iPubs.next();
				if (pageNumber == null || (pageNumber !=null && Integer.parseInt(pageNumber) == i)) {
					if (beginPath) path.append(separator);					
					path.append(transformLabel(pub.getName()));
				}			
				i++;
			}
		}
				
		return path.toString();
	}

	/**
	 * Récupère la classe css du niveau hierarchique.
	 * @param level
	 * @return
	 */
	private String getCssClass(int level) {
		if (classNamesHierarchy != null) {
			StringTokenizer classes = new StringTokenizer(classNamesHierarchy, ",");
			int i = 0;
			while (classes.hasMoreTokens()) {
				String classe = classes.nextToken();
				if (level == i) return classe;
				i++;
			}
		}		
		return "";
	}
	
	/**
	 * Construction de l'url de l'item.
	 * @param node
	 * @return
	 * @throws RemoteException
	 */
	private String generateFullSemanticPath(NodeDetail node) throws RemoteException {
		try {
			//TODO use prefix id
			return LinkGeneratorFactory.getInstance().newLinkGenerator().generateFullSemanticPath(pageContext, themetracker, node, idsTopicsRoots, null, getPrefixIdByLevelAndRoot(node));
		} catch (Exception e) {
			throw new RemoteException("", e);
		}
	}
	
	private String getPrefixIdByLevelAndRoot(NodeDetail node) throws Exception {
		if (prefixIdHierarchyByIdTopicRoot != null) {			
			// recherche de la racine du topic
			StringTokenizer nodes = new StringTokenizer(node.getPath(), "/");
			String rootId = null;
			while (nodes.hasMoreTokens()) {
				String nodeId = nodes.nextToken();
				if (isRootTopic(nodeId)) {
					rootId = nodeId;
				}
			}
			if (rootId != null) {				
				// selection des prefixes du root
				StringTokenizer tokenizer = new StringTokenizer(prefixIdHierarchyByIdTopicRoot, ";");			
				String token = null;
				while (tokenizer.hasMoreTokens()) {
					token = tokenizer.nextToken();
					if (token.startsWith(rootId+"[")) {
						int rootLevel = themetracker.getTopic(String.valueOf(rootId)).getLevel();
						int level = node.getLevel() - rootLevel;
						// recherche du prefixe
						StringTokenizer tokenizerPrefix = new StringTokenizer(token.substring(token.indexOf("[")+1, token.indexOf("]")), ",");						
						int l = 1;
						String prefix = null;
						while (tokenizerPrefix.hasMoreTokens()) {
							prefix = tokenizerPrefix.nextToken();
							if (level == l) {				
								return prefix;		
							}
							l++;
						}
					}			
				}			
			} else {
				return null;
			}		
		}		
		return null;		
	}
	
	/**
	 * Vérifie que un theme est un theme racine.
	 * @param nodeId
	 * @return
	 */
	private boolean isRootTopic(String nodeId) {
		StringTokenizer rootNodes = new StringTokenizer(idsTopicsRoots, ",");
		while (rootNodes.hasMoreTokens()) {
			String rootNodeId = rootNodes.nextToken();
			if (rootNodeId.equals(nodeId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Suppression des caractères à ne pas afficher.
	 * @param label
	 * @return
	 */
	private String transformLabel(String label) {
		label = label.replaceAll("\\<.*?>","");
		label = StringEscapeUtils.escapeHtml(label);
		return label;
	}
}
