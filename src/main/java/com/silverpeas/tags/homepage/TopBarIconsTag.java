package com.silverpeas.tags.homepage;

import com.stratelia.silverpeas.peasCore.MainSessionController;
import com.stratelia.silverpeas.peasCore.URLManager;
import com.stratelia.webactiv.util.ResourceLocator;
import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class TopBarIconsTag extends TagSupport 
{
	protected ResourceLocator m_message;
	protected Hashtable m_icons;
	protected String m_sContext;
	protected MainSessionController m_mainSessionCtrl;

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
	public void setMainSessionCtrl(MainSessionController mainSessionCtrl)
	{
		this.m_mainSessionCtrl = mainSessionCtrl;
	}

	public int doStartTag() throws JspException
    {
        try
		{
			pageContext.getOut().println("&nbsp;<span class='txtpetitblanc'>" + m_message.getString("Tools") + " :&nbsp;</span>");
			pageContext.getOut().println("<img src='" + m_icons.get("arrowRightIcon") + "' align='absmiddle'>&nbsp;");
			pageContext.getOut().println("<a href=javascript:notifyPopup('" + m_sContext + "','','Administrators','')><img src='" + m_icons.get("mailIcon") + "' align='absmiddle' alt='" + m_message.getString("Feedback") + "' border='0' onFocus='self.blur()' title='" + m_message.getString("Feedback") + "'></a>&nbsp;");
			pageContext.getOut().println("<a href='" + m_sContext + "/admin/jsp/Map.jsp' target='MyMain'><img src='" + m_icons.get("mapIcon") + "' align='absmiddle' border='0' alt='" + m_message.getString("MyMap") + "' onFocus='self.blur()' title='" + m_message.getString("MyMap") + "'></a>&nbsp;");
			pageContext.getOut().println("<a href='" + m_sContext + "/LogoutServlet' target='_top'><img src='" + m_icons.get("logIcon") + "' align='absmiddle' border='0' alt='" + m_message.getString("Exit") + "' onFocus='self.blur()' title='" + m_message.getString("Exit") + "'></a>&nbsp;");
			pageContext.getOut().println("<a href='" + m_sContext + URLManager.getURL(URLManager.CMP_PERSONALIZATION) + "Main.jsp' target='MyMain'><img src='" + m_icons.get("customIcon") + "' align='absmiddle' border='0' alt='" + m_message.getString("Personalization") + "' onFocus='self.blur()' title='" + m_message.getString("Personalization") + "'></a>&nbsp;");
			pageContext.getOut().println("<a href='/help_fr/Silverpeas.htm' target='_blank'><img src='" + m_icons.get("helpIcon") + "' align='absmiddle' border='0' alt='" + m_message.getString("Help") + "' onFocus='self.blur()' title='" + m_message.getString("Help") + "'></a>&nbsp;");
			pageContext.getOut().println("<a href='" + m_sContext + URLManager.getURL(URLManager.CMP_CLIPBOARD) + "Idle.jsp?message=SHOWCLIPBOARD' target='IdleFrame'><img src='" + m_icons.get("clipboardIcon") + "' align='absmiddle' border='0' alt='" + m_message.getString("Clipboard") + "' onFocus='self.blur()' title='" + m_message.getString("Clipboard") + "'></a>&nbsp;&nbsp;");

			if(m_mainSessionCtrl.getUserAccessLevel().equals("A") || (m_mainSessionCtrl.getUserManageableSpaceIds() != null && m_mainSessionCtrl.getUserManageableSpaceIds().length>0))
				pageContext.getOut().println("<a href='" + m_sContext + URLManager.getURL(URLManager.CMP_JOBMANAGERPEAS) + "Main' target='_top'><img src='" + m_icons.get("adminConsol") + "' align='absmiddle' border='0' alt='" + m_message.getString("adminConsol") + "' onFocus='self.blur()' title='" + m_message.getString("adminConsol") + "'></a>&nbsp;");

			pageContext.getOut().println("<a href=javascript:onClick=openPdc()><img src='" + m_icons.get("glossary") + "' align='absmiddle' border='0' alt='" + m_message.getString("glossaire") + "' onFocus='self.blur()' title='" + m_message.getString("glossaire") + "'></a>&nbsp;");
		} 
		catch(IOException ioe) 
		{
            // User probably disconnected ...
            throw new JspTagException("IO_ERROR");
        }

        return SKIP_BODY;
    }

}