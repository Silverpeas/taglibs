/**
 * Copyright (C) 2000 - 2012 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of the GPL, you may
 * redistribute this Program in connection with Free/Libre Open Source Software ("FLOSS")
 * applications as described in Silverpeas's FLOSS exception. You should have received a copy of the
 * text describing the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.silverpeas.tags.homepage;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.stratelia.silverpeas.peasCore.URLManager;
import com.stratelia.webactiv.util.ResourceLocator;

public class MapPersonnalSpaceTag extends TagSupport {

  protected ResourceLocator m_message;
  protected String m_sContext;
  protected String m_iconPersonnalSpace;

  public void setMessage(ResourceLocator message) {
    this.m_message = message;
  }

  public void setSContext(String sContext) {
    this.m_sContext = sContext;
  }

  public void setIconPersonnalSpace(String iconPersonnalSpace) {
    this.m_iconPersonnalSpace = iconPersonnalSpace;
  }

  public int doStartTag() throws JspException {
    try {
      pageContext.getOut().println("<center>");
      pageContext.getOut().println(
          "<table width='98%' border='0' cellspacing='0' cellpadding='0' class='intfdcolor4'>");
      pageContext.getOut().println("	<tr>");
      pageContext.getOut().println("		<td nowrap>");
      pageContext
          .getOut()
          .println(
          "			<table border='0' cellspacing='0' cellpadding='5' class='contourintfdcolor' width='100%'>");
      pageContext.getOut().println("				<tr align=left>");
      pageContext.getOut().println("					<td nowrap>");
      pageContext.getOut().println(
          "						<img src='" + m_iconPersonnalSpace + "' align='absmiddle'>&nbsp;&nbsp;");
      pageContext.getOut().println(
          "						<span class='txtnav' nowrap>" + m_message.getString("SpacePersonal") + "</span>");
      pageContext.getOut().println("					</td>");
      pageContext.getOut().println("				</tr>");
      pageContext.getOut().println("				<tr>");
      pageContext.getOut().println("					<td nowrap>");
      pageContext.getOut().println("						&nbsp;&nbsp;");
      pageContext.getOut().println(
          "						<img src='" + m_sContext
          + "/util/icons/component/agendaSmall.gif' border=0 width=15 align=absmiddle>");
      pageContext.getOut().println("						&nbsp;");
      pageContext.getOut().println("						<span class='txtnote' nowrap>");
      pageContext.getOut().println(
          "							<a href='" + m_sContext + URLManager.getURL(URLManager.CMP_AGENDA, null, null)
          + "agenda.jsp' target='MyMain'>" + m_message.getString("Diary") + "</a>");
      pageContext.getOut().println("						</span>");
      pageContext.getOut().println("						&nbsp;&nbsp;&nbsp;");
      pageContext.getOut().println(
          "						<img src='" + m_sContext
          + "/util/icons/component/todoSmall.gif' border=0 width=15 align=absmiddle>");
      pageContext.getOut().println("						&nbsp;");
      pageContext.getOut().println("						<span class='txtnote' nowrap>");
      pageContext.getOut().println(
          "							<a href='" + m_sContext + URLManager.getURL(URLManager.CMP_TODO, null, null)
          + "todo.jsp' target='MyMain'>" + m_message.getString("ToDo") + "</a>");
      pageContext.getOut().println("						</span>");
      pageContext.getOut().println("					</td>");
      pageContext.getOut().println("				</tr>");
      pageContext.getOut().println("				<tr>");
      pageContext.getOut().println("					<td nowrap>");
      pageContext.getOut().println(
          "						<img src='" + m_sContext
          + "/util/icons/component/mailserviceSmall.gif' border=0 width=15 align=absmiddle>");
      pageContext.getOut().println("						&nbsp;");
      pageContext.getOut().println("						<span class='txtnote' nowrap>");
      pageContext.getOut().println(
          "							<a href='" + m_sContext + URLManager.getURL(URLManager.CMP_SILVERMAIL, null, null)
          + "Main.jsp' target='MyMain'>" + m_message.getString("Mail") + "</a>");
      pageContext.getOut().println("						</span>");
      pageContext.getOut().println("					</td>");
      pageContext.getOut().println("				</tr>");
      pageContext.getOut().println("				<tr>");
      pageContext.getOut().println("					<td nowrap>");
      pageContext.getOut().println(
          "						<img src='" + m_sContext
          + "/util/icons/component/notificationUser.gif' border=0 width=15 align=absmiddle>");
      pageContext.getOut().println("						&nbsp;");
      pageContext.getOut().println("						<span class='txtnote' nowrap>");
      pageContext.getOut().println(
          "							<a href='" + m_sContext + URLManager.getURL(URLManager.CMP_NOTIFICATIONUSER, null,
          null) + "Main.jsp' target='MyMain'>" + m_message.getString("NotifyUser") + "</a>");
      pageContext.getOut().println("						</span>");
      pageContext.getOut().println("					</td>");
      pageContext.getOut().println("				</tr>");
      pageContext.getOut().println("			</table>");
      pageContext.getOut().println("		</td>");
      pageContext.getOut().println("	</tr>");
      pageContext.getOut().println("</table>");
      pageContext.getOut().println("</center>");
    } catch (IOException ioe) {
      // User probably disconnected ...
      throw new JspTagException("IO_ERROR");
    }

    return SKIP_BODY;
  }
}