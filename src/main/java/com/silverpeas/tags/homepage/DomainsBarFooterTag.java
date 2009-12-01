package com.silverpeas.tags.homepage;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class DomainsBarFooterTag extends TagSupport 
{

    protected String m_iconAngleBas;

	public void setIconAngleBas(String iconAngleBas)
	{
		this.m_iconAngleBas = iconAngleBas;
	}

	public int doStartTag() throws JspException
    {
        try
		{
			pageContext.getOut().println("<tr>");
			pageContext.getOut().println("	<td width='100%' class='intfdcolor51'><img src='icons/1px.gif' width='1' height='6'></td>");
			pageContext.getOut().println("	<td rowspan='3' colspan='2' class='intfdcolor51'><img src='icons/angleBasDomainsBar.gif' width='8' height='8'></td>");
			pageContext.getOut().println("</tr>");
			pageContext.getOut().println("<tr>");
			pageContext.getOut().println("	<td width='100%' class='intfdcolor4'><img src='icons/1px.gif' width='1' height='1'></td>");
			pageContext.getOut().println("</tr>");
			pageContext.getOut().println("<tr class='intfdcolor13'>");
			pageContext.getOut().println("	<td width='100%'><img src='icons/1px.gif' width='1' height='1'></td>");
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