package com.silverpeas.tags.navigation;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.silverpeas.tags.kmelia.KmeliaTagUtil;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;

/**
 * Tag permettant d'affiche un libellé à partir d'un contenu wysiwyg d'une publication
 * au format clef/valeur.
 * @author svuillet
 */
public class LabelTag extends TagSupport {

	private static final long serialVersionUID = -7428587170229279713L;
	private String key = null;
	private String idPub = null;
	private KmeliaTagUtil themetracker = null;

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getIdPub() {
		return idPub;
	}

	public void setIdPub(String idPub) {
		this.idPub = idPub;
	}	
	
	/**
	 * Source de données.
	 * @param tt
	 */
	public void setThemetracker(String tt) {
		int scope = pageContext.getAttributesScope(tt);	
		themetracker = (KmeliaTagUtil) pageContext.getAttribute(tt, scope);
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();									
			out.print(getLabel());			
		} catch (Exception e) {
			e.printStackTrace();
		}								
        return SKIP_BODY;
	}

	/**
	 * Retourne le libellé correspondant à la clef.
	 * @return
	 * @throws Exception
	 */
	private String getLabel() throws Exception {
		Properties properties = loadLabels() ;		
		return properties.getProperty(key, "");	
	}
	
	/**
	 * Chargement des libéllés.
	 * @return
	 * @throws Exception
	 */
	private Properties loadLabels() throws Exception {		
		
		PublicationDetail pub = PublicationCache.getInstance(themetracker).getPublication(idPub);		
		
		String content = pub.getWysiwyg();	
		// suppression des commentaires
		content = content.replaceAll("(?s)<!--.*?-->", "");
		content = content.replaceAll("<p>","");
		content = content.replaceAll("</p>","\n");			
				
		Properties properties = new Properties();
		properties.load(new ByteArrayInputStream(content.getBytes()));
		return properties;
	}
}
