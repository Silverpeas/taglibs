package com.silverpeas.tags.homepage;

import com.stratelia.silverpeas.peasCore.URLManager;
import com.stratelia.webactiv.util.ResourceLocator;
import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class PersonnalSpaceChoiceTag extends TagSupport 
{
	protected ResourceLocator m_message;
	protected Hashtable m_icons;
	protected String m_sContext;

	public void setMessage(ResourceLocator message)
	{
		this.m_message = message;
	}
	public void setIcons(Hashtable icons)
	{
		this.m_icons = icons;
	}
	public void setSContext(String sContext)
	{
		this.m_sContext = sContext;
	}

	public int doStartTag() throws JspException
    {
        try
		{
			pageContext.getOut().println("			<table cellspacing='0' cellpadding='0' border='0' height='1%'>");
			pageContext.getOut().println("				<tr valign='top'>");
			pageContext.getOut().println("					<td align='left' valign='top'>");
			pageContext.getOut().println("						<img src='" + m_icons.get("personalSpaceIcon") + "' valign='top'>");
			pageContext.getOut().println("					</td>");
			pageContext.getOut().println("					<td valign='top'><span class='Titre'>" + m_message.getString("SpacePersonal") + "</span>");
			pageContext.getOut().println("					</td>");
			pageContext.getOut().println("				</tr>");
			pageContext.getOut().println("				<tr valign='top'>");
			pageContext.getOut().println("					<td align='left' colspan='2' valign='top'>");
			pageContext.getOut().println("						<span class='selectNS'>");
			pageContext.getOut().println("					    <select name='selection' onChange='top.scriptFrame.jumpTopbar()'>");
			pageContext.getOut().println("							<option value='' selected>" + m_message.getString("Choose") + "</option>");
			pageContext.getOut().println("						    <option value=''>----------------</option>");
			pageContext.getOut().println("						    <option value='" + m_sContext + URLManager.getURL(URLManager.CMP_AGENDA) + "agenda.jsp'>" + m_message.getString("Diary") + "</option>");
			pageContext.getOut().println("						    <option value='" + m_sContext + URLManager.getURL(URLManager.CMP_TODO) + "todo.jsp'>" + m_message.getString("ToDo") +"</option>");
			pageContext.getOut().println("						    <option value='" + m_sContext + URLManager.getURL(URLManager.CMP_SILVERMAIL) + "Main'>" + m_message.getString("Mail") + "</option>");
			pageContext.getOut().println("						    <option value='" + m_sContext + URLManager.getURL(URLManager.CMP_PDCSUBSCRIPTION) + "subscriptionList.jsp'>" + m_message.getString("MyInterestCenters") + "</option>");
			pageContext.getOut().println("						    <option value='" + m_sContext + URLManager.getURL(URLManager.CMP_INTERESTCENTERPEAS)+ "iCenterList.jsp'>" + m_message.getString("FavRequests") + "</option>");
			pageContext.getOut().println("					    </select>");
			pageContext.getOut().println("			            </span>");
			pageContext.getOut().println("						<a href=javascript:onClick=viewPersonalHomePage() border=0><img src='" + m_icons.get("homeSpaceIcon") + "' border='0' align='absmiddle' alt='" + m_message.getString("BackToPersonalMainPage") + "' title='" + m_message.getString("BackToMainPage") + "'></a>");
			pageContext.getOut().println("					</td>");
			pageContext.getOut().println("				</tr>");
			pageContext.getOut().println("			</table>");
		} 
		catch(IOException ioe) 
		{
            // User probably disconnected ...
            throw new JspTagException("IO_ERROR");
        }

        return SKIP_BODY;
    }

}