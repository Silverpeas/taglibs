package com.silverpeas.tags.navigation;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringEscapeUtils;

import com.silverpeas.tags.kmelia.KmeliaTagUtil;
import com.silverpeas.tags.navigation.config.Configurateur;
import com.silverpeas.tags.navigation.links.LinkGeneratorFactory;
import com.silverpeas.tags.pdc.PdcTagUtil;
import com.stratelia.silverpeas.pdc.model.Value;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;

/**
 * Tag permettant de générer la liste des publication d'un thème.
 * @author svuillet
 */
public class PageListeTag extends TagSupport {

	private static final long serialVersionUID = 7316128024807549206L;	
	private static final String TOPIC_ID_PREFIX = "topicId-";

	private KmeliaTagUtil themetracker = null;
	private PdcTagUtil pdc = new PdcTagUtil(null, null, 0, null);
	private String idTopicRoot;
	private String currentNumber;
	private String idAxisFiltering = null;
	private String axisValueFilter = null;
	private String id;
	private String classNamesFiltered;
	
	
	/**
	 * Point de démarrage du parcours de l'arborescence.
	 * @param idTopicRoot
	 */
	public void setIdTopicRoot(String idTopicRoot) {
		this.idTopicRoot = idTopicRoot;
	}
	
	/**
	 * Classes appliquées si la publication est positionnée sur idAxisFiltering avec la valeurs "axisValueFilter". 
	 * @param classNamesFiltered
	 */
	public void setClassNamesFiltered(String classNamesFiltered) {
		this.classNamesFiltered = classNamesFiltered;
	}
	
	/**
	 * Id de l'axe qui va servir pour filtrer l'arborescence.
	 * @param idAxisFiltering
	 */
	public void setIdAxisFiltering(String idAxisFiltering) {
		this.idAxisFiltering = idAxisFiltering;
	}
	
	/**
	 * Valeur du filtre de l'arborescence.
	 * @param axisValueFilter
	 */
	public void setAxisValueFilter(String axisValueFilter) {
		this.axisValueFilter = axisValueFilter;
	}
	
	/**
	 * Id de l'élément HTML contenant le menu (UL).
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Page courante.
	 * @param currentNumber
	 */
	public void setCurrentNumber(String currentNumber) {
		this.currentNumber = currentNumber;
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
	 * Construction d'un id html pour un theme.
	 * @param prefix
	 * @param theme
	 * @param number
	 * @return
	 */
	private String buildId(String prefix, NodeDetail theme, int number) {	
		String genericId = prefix + theme.getId() + "-" + number;
		String specificId = Configurateur.getConfigValue(genericId);
		if (specificId != null) {
			return specificId;
		}
		
		return genericId;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();	
			NodeDetail root = themetracker.getTopic(idTopicRoot);			
			browse(out, root);
		} catch (Exception e) {
			e.printStackTrace();
		}
								
        return SKIP_BODY;
	}
	
	/**
	 * Construction de la liste.
	 * @param out
	 * @param rootTopic
	 */
	private void browse(JspWriter out, NodeDetail rootTopic) {
		try {
			Collection pubs = themetracker.getPublicationsByTopic(idTopicRoot+",order,asc");
			Iterator iPubs = pubs.iterator();
			print(out, "<ul id='"+id+"'>", true);
			StringBuffer html = new StringBuffer();
			int number = 1;
			while (iPubs.hasNext()) {
				PublicationDetail pub = (PublicationDetail) iPubs.next();				
				html.setLength(0);
				html.append("<li id='");
				html.append(buildId(TOPIC_ID_PREFIX, rootTopic, number));
								
				html.append("' class='");
				html.append(getClassNameByPublication(pub, number));
				html.append("'");
				
				html.append(">");			
				html.append("<a href='");
				html.append(generateFullSemanticPath(rootTopic, pub, number));
				html.append("' title='");
				html.append(StringEscapeUtils.escapeHtml(rootTopic.getDescription()));								
				html.append("'><span>");
				html.append(pub.getName());				
				html.append("</span></a>");				
				print(out, html.toString(), true);
				number++;				
			}
			print(out, "</ul>", true);			
		} catch (Exception e) {
            e.printStackTrace();
        }		
	}
	
	/**
	 * Retourne le nom de la classe css à appliquer pour lien donné. 
	 * @param pub
	 * @return
	 * @throws Exception 
	 */
	private String getClassNameByPublication(PublicationDetail pub, int number) throws Exception {
		StringBuffer className = new StringBuffer("");
		if (Integer.parseInt(currentNumber)==number) {
			className.append("item-selected");
		}		
		
		if (idAxisFiltering != null && axisValueFilter != null && !idAxisFiltering.isEmpty() && !axisValueFilter.isEmpty()) {
			@SuppressWarnings("unchecked")
			Collection<Value> values = pdc.getValuesOnAxis(pub.getId()+","+themetracker.getComponentInst().getId()+","+idAxisFiltering);
			Iterator<Value> iValues = values.iterator();
			while (iValues.hasNext()) {
				Value v = (Value) iValues.next();
				if (v.getName().equals(axisValueFilter) && classNamesFiltered != null) {
					className.append(" ");
					className.append(classNamesFiltered);					
					break;
				}
			}
		}		
		return className.toString().trim();
	}
	
	/**
	 * Print html.
	 * @param out
	 * @param html
	 * @param display
	 */
	private void print(JspWriter out, String html, boolean display) {
		if (display) {
			try {
				out.println(html);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Construction de l'url de l'item.
	 * @param node
	 * @param number
	 * @return
	 * @throws RemoteException
	 */
	private String generateFullSemanticPath(NodeDetail node, PublicationDetail pub, int number) throws RemoteException {
		try {
			return LinkGeneratorFactory.getInstance().newLinkGenerator().generateFullSemanticPath(pageContext, themetracker, node, idTopicRoot, pub, null) + "-" + number;
		} catch (Exception e) {
			throw new RemoteException("", e);
		}
	}
}
