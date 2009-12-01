package com.silverpeas.tags.homepage;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class CollaborativeSpaceTitleTag extends TagSupport 
{

    protected String m_iconAngleHaut;

	protected String m_iconCollaborative;

	protected String m_messageSpaceCollaboration;

	public void setIconAngleHaut(String iconAngleHaut)
	{
		this.m_iconAngleHaut = iconAngleHaut;
	}

	public void setIconCollaborative(String iconCollaborative)
	{
		this.m_iconCollaborative = iconCollaborative;
	}

	public void setMessageSpaceCollaboration(String messageSpaceCollaboration)
	{
		this.m_messageSpaceCollaboration = messageSpaceCollaboration;
	}

	public int doStartTag() throws JspException
    {
        try
		{
			pageContext.getOut().println("<tr>");
			pageContext.getOut().println("	<td width='100%' class='intfdcolor13'><img src='icons/1px.gif' width='1' height='1'></td>");
			pageContext.getOut().println("	<td rowspan='3' colspan='2' class='intfdcolor'><img src='" + m_iconAngleHaut + "' width='8' height='8'></td>");
			pageContext.getOut().println("</tr>");
			pageContext.getOut().println("<tr>");
			pageContext.getOut().println("	<td width='100%' class='intfdcolor4'><img src='icons/1px.gif' width='1' height='1'></td>");
			pageContext.getOut().println("</tr>");
			pageContext.getOut().println("<tr class='intfdcolor'>");
			pageContext.getOut().println("	<td width='100%'><img src='icons/1px.gif' width='1' height='6'></td>");
			pageContext.getOut().println("</tr>");
			pageContext.getOut().println("<tr class='intfdcolor'>");
			pageContext.getOut().println("	<td width='100%'><img src='icons/1px.gif' width='1' height='1'></td>");
			pageContext.getOut().println("	<td><img src='icons/1px.gif' width='7' height='1'></td>");
			pageContext.getOut().println("	<td class='intfdcolor'><img src='icons/1px.gif' width='1' height='1'></td>");
			pageContext.getOut().println("</tr>");
			pageContext.getOut().println("<tr class='intfdcolor'>");
			pageContext.getOut().println("	<td width='100%'>");
			pageContext.getOut().println("		<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
			pageContext.getOut().println("			<tr>");
			pageContext.getOut().println("				<td><img src='" + m_iconCollaborative + "'></td>");
			pageContext.getOut().println("				<td width='100%'><span class='txtpetitblanc'>" + m_messageSpaceCollaboration + "</span></td>");
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