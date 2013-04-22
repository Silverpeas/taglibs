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
import java.util.Hashtable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.stratelia.silverpeas.peasCore.URLManager;
import com.stratelia.webactiv.util.ResourceLocator;

public class PersonnalSpaceChoiceTag extends TagSupport {

  protected ResourceLocator messages;
  protected Hashtable icons;
  protected String webContext;

  public void setMessage(ResourceLocator message) {
    this.messages = message;
  }

  public void setIcons(Hashtable icons) {
    this.icons = icons;
  }

  public void setSContext(String sContext) {
    this.webContext = sContext;
  }

  @Override
  public int doStartTag() throws JspException {
    try {
      pageContext.getOut().println(
          "			<table cellspacing='0' cellpadding='0' border='0' height='1%'>");
      pageContext.getOut().println("				<tr valign='top'>");
      pageContext.getOut().println("					<td align='left' valign='top'>");
      pageContext.getOut().println(
          "						<img src='" + icons.get("personalSpaceIcon") + "' valign='top'>");
      pageContext.getOut().println("					</td>");
      pageContext.getOut().println("					<td valign='top'><span class='Titre'>" + messages.
          getString("SpacePersonal") + "</span>");
      pageContext.getOut().println("					</td>");
      pageContext.getOut().println("				</tr>");
      pageContext.getOut().println("				<tr valign='top'>");
      pageContext.getOut().println("					<td align='left' colspan='2' valign='top'>");
      pageContext.getOut().println("						<span class='selectNS'>");
      pageContext.getOut().println(
          "					    <select name='selection' onChange='top.scriptFrame.jumpTopbar()'>");
      pageContext.getOut().println(
          "							<option value='' selected>" + messages.getString("Choose") + "</option>");
      pageContext.getOut().println("						    <option value=''>----------------</option>");
      pageContext.getOut().println("						    <option value='" + webContext + URLManager.getURL(
          URLManager.CMP_AGENDA, null, null) + "agenda.jsp'>" + messages.getString("Diary")
          + "</option>");
      pageContext.getOut().println("						    <option value='" + webContext + URLManager.getURL(
          URLManager.CMP_TODO, null, null) + "todo.jsp'>" + messages.getString("ToDo") + "</option>");
      pageContext.getOut().println("						    <option value='" + webContext + URLManager.getURL(
          URLManager.CMP_SILVERMAIL, null, null) + "Main'>" + messages.getString("Mail")
          + "</option>");
      pageContext.getOut().println("						    <option value='" + webContext + URLManager.getURL(
          URLManager.CMP_PDCSUBSCRIPTION, null, null) + "subscriptionList.jsp'>" + messages.
          getString("MyInterestCenters") + "</option>");
      pageContext.getOut().println("						    <option value='" + webContext + URLManager.getURL(
          URLManager.CMP_INTERESTCENTERPEAS, null, null) + "iCenterList.jsp'>" + messages.getString(
          "FavRequests") + "</option>");
      pageContext.getOut().println("					    </select>");
      pageContext.getOut().println("			            </span>");
      pageContext.getOut().println(
          "						<a href=javascript:onClick=viewPersonalHomePage() border=0><img src='" + icons.get(
          "homeSpaceIcon") + "' border='0' align='absmiddle' alt='" + messages.getString(
          "BackToPersonalMainPage") + "' title='" + messages.getString("BackToMainPage") + "'></a>");
      pageContext.getOut().println("					</td>");
      pageContext.getOut().println("				</tr>");
      pageContext.getOut().println("			</table>");
    } catch (IOException ioe) {
      // User probably disconnected ...
      throw new JspTagException("IO_ERROR");
    }
    return SKIP_BODY;
  }
}