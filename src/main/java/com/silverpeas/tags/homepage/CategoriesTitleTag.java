package com.silverpeas.tags.homepage;

import com.stratelia.silverpeas.peasCore.MainSessionController;
import com.stratelia.webactiv.homepage.JspHelper;
import com.stratelia.webactiv.util.ResourceLocator;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class CategoriesTitleTag extends TagSupport 
{

	protected String m_component_id;

	protected String m_spaceOrSubSpace;

	protected ResourceLocator m_message;

	protected MainSessionController m_mainSessionCtrl;


	public void setComponent_id(String component_id)
	{
		this.m_component_id = component_id;
	}

	public void setSpaceOrSubSpace(String spaceOrSubSpace)
	{
		this.m_spaceOrSubSpace = spaceOrSubSpace;
	}

	public void setMessage(ResourceLocator message)
	{
		this.m_message = message;
	}

	public void setMainSessionCtrl(MainSessionController mainSessionCtrl)
	{
		this.m_mainSessionCtrl = mainSessionCtrl;
	}

	public int doStartTag() throws JspException
    {
        try
		{
			pageContext.getOut().println("<tr class='intfdcolor'>");
			pageContext.getOut().println("	<td width='100%'>");
			pageContext.getOut().println("		<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
			pageContext.getOut().println("			<tr>");
			pageContext.getOut().println("				<td>&nbsp;</td>");
			pageContext.getOut().println("				<td width='100%'><span class='txtpetitblanc'>" + JspHelper.formatAxesCaption(m_component_id, m_spaceOrSubSpace, m_message, m_mainSessionCtrl) + "</span></td>");
			pageContext.getOut().println("			</tr>");
			pageContext.getOut().println("		</table>");
			pageContext.getOut().println("	</td>");
			pageContext.getOut().println("	<td><img src='icons/1px.gif'></td>");
			pageContext.getOut().println("	<td class='intfdcolor'><img src='icons/1px.gif'></td>");
			pageContext.getOut().println("</tr>");
		} 
		catch(IOException ioe) 
		{
            // User probably disconnected ...
            throw new JspTagException("IO_ERROR");
        }

        return SKIP_BODY;
    }

}