package com.silverpeas.tags.homepage;

import com.stratelia.webactiv.util.ResourceLocator;
import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class SearchEngineTag extends TagSupport 
{

    protected ResourceLocator m_message;
	protected Hashtable m_icons;

	public void setMessage(ResourceLocator message)
	{
		this.m_message = message;
	}
	public void setIcons(Hashtable icons)
	{
		this.m_icons = icons;
	}

	public int doStartTag() throws JspException
    {
        try
		{
			pageContext.getOut().println("		&nbsp;<a href='javascript:advancedSearchEngine()' class='Titre'>" + m_message.getString("SearchAdvanced") + "</a><br>");
			pageContext.getOut().println("		<script language=javascript>");
			pageContext.getOut().println("			<!--");
			pageContext.getOut().println("		    if (navigator.appName == 'Netscape')");
			pageContext.getOut().println("				document.write('&nbsp;<input type=text name=query size=8 value=>');");
			pageContext.getOut().println("		    else");
			pageContext.getOut().println("				document.write('&nbsp;<input type=text name=query size=12 value=>');");
			pageContext.getOut().println("		    //-->");
			pageContext.getOut().println("		</script>");
			pageContext.getOut().println("		<a href='javascript:searchEngine()'><img border='0' src='" + m_icons.get("okIcon") + "' align='absmiddle'></a>");
		} 
		catch(IOException ioe) 
		{
            // User probably disconnected ...
            throw new JspTagException("IO_ERROR");
        }

        return SKIP_BODY;
    }

}