package com.silverpeas.tags.homepage;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class DomainsBarHeadingTag extends TagSupport 
{

    protected String m_iconAngleHaut;

	public void setIconAngleHaut(String iconAngleHaut)
	{
		this.m_iconAngleHaut = iconAngleHaut;
	}

	public int doStartTag() throws JspException
    {
        try
		{
			pageContext.getOut().println("<table width='100%' cellspacing='0' cellpadding='0' border='0'>");
			pageContext.getOut().println("<tr>");
			pageContext.getOut().println("	<td width='100%' class='intfdcolor13'><img src='icons/1px.gif' width='1' height='1'></td>");
			pageContext.getOut().println("	<td rowspan='3' colspan='2' class='intfdcolor51'><img src='" + m_iconAngleHaut + "'></td>");
			pageContext.getOut().println("</tr>");
			pageContext.getOut().println("<tr>");
			pageContext.getOut().println("	<td width='100%' class='intfdcolor4'><img src='icons/1px.gif' width='1' height='1'></td>");
			pageContext.getOut().println("</tr>");
			pageContext.getOut().println("<tr class='intfdcolor51'>");
			pageContext.getOut().println("	<td width='100%'><img src='icons/1px.gif' width='2' align='middle'>");
			pageContext.getOut().println("	</td>");
			pageContext.getOut().println("</tr>");
			pageContext.getOut().println("</table>");
		} 
		catch(IOException ioe) 
		{
            // User probably disconnected ...
            throw new JspTagException("IO_ERROR");
        }

        return SKIP_BODY;
    }

}