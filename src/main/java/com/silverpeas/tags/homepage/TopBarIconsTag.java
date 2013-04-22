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

import com.stratelia.silverpeas.peasCore.MainSessionController;
import com.stratelia.silverpeas.peasCore.URLManager;
import com.stratelia.webactiv.util.ResourceLocator;

public class TopBarIconsTag extends TagSupport {

  protected ResourceLocator messages;
  protected Hashtable icons;
  protected String webContext;
  protected MainSessionController mainSessionController;

  public void setMessage(ResourceLocator message) {
    this.messages = message;
  }

  public void setIcons(Hashtable icons) {
    this.icons = icons;
  }

  public void setSContext(String sContext) {
    this.webContext = sContext;
  }

  public void setMainSessionCtrl(MainSessionController mainSessionCtrl) {
    this.mainSessionController = mainSessionCtrl;
  }

  @Override
  public int doStartTag() throws JspException {
    try {
      pageContext.getOut().println("&nbsp;<span class='txtpetitblanc'>"
          + messages.getString("Tools") + " :&nbsp;</span>");
      pageContext.getOut().println("<img src='" + icons.get("arrowRightIcon")
          + "' align='absmiddle'>&nbsp;");
      pageContext.getOut().println("<a href=javascript:notifyPopup('" + webContext
          + "','','Administrators','')><img src='" + icons.get("mailIcon")
          + "' align='absmiddle' alt='" + messages.getString("Feedback")
          + "' border='0' onFocus='self.blur()' title='" + messages.getString("Feedback")
          + "'></a>&nbsp;");
      pageContext.getOut().println(
          "<a href='" + webContext + "/admin/jsp/Map.jsp' target='MyMain'><img src='" + icons.get(
          "mapIcon") + "' align='absmiddle' border='0' alt='" + messages.getString("MyMap")
          + "' onFocus='self.blur()' title='" + messages.getString("MyMap") + "'></a>&nbsp;");
      pageContext.getOut().println(
          "<a href='" + webContext + "/LogoutServlet' target='_top'><img src='" + icons.get(
          "logIcon") + "' align='absmiddle' border='0' alt='" + messages.getString("Exit")
          + "' onFocus='self.blur()' title='" + messages.getString("Exit") + "'></a>&nbsp;");
      pageContext.getOut().println(
          "<a href='" + webContext + URLManager.getURL(URLManager.CMP_PERSONALIZATION, null, null)
          + "Main.jsp' target='MyMain'><img src='" + icons.get("customIcon")
          + "' align='absmiddle' border='0' alt='" + messages.getString("Personalization")
          + "' onFocus='self.blur()' title='" + messages.getString("Personalization")
          + "'></a>&nbsp;");
      pageContext.getOut().println(
          "<a href='/help_fr/Silverpeas.htm' target='_blank'><img src='" + icons.get("helpIcon")
          + "' align='absmiddle' border='0' alt='" + messages.getString("Help")
          + "' onFocus='self.blur()' title='" + messages.getString("Help") + "'></a>&nbsp;");
      pageContext.getOut().println(
          "<a href='" + webContext + URLManager.getURL(URLManager.CMP_CLIPBOARD, null, null)
          + "Idle.jsp?message=SHOWCLIPBOARD' target='IdleFrame'><img src='" + icons.get(
          "clipboardIcon") + "' align='absmiddle' border='0' alt='" + messages.getString(
          "Clipboard") + "' onFocus='self.blur()' title='" + messages.getString("Clipboard")
          + "'></a>&nbsp;&nbsp;");

      if ("A".equals(mainSessionController.getUserAccessLevel()) || (mainSessionController.
          getUserManageableSpaceIds() != null && mainSessionController
          .getUserManageableSpaceIds().length > 0)) {
        pageContext.getOut().println(
            "<a href='" + webContext + URLManager.getURL(URLManager.CMP_JOBMANAGERPEAS, null, null)
            + "Main' target='_top'><img src='" + icons.get("adminConsol")
            + "' align='absmiddle' border='0' alt='" + messages.getString("adminConsol")
            + "' onFocus='self.blur()' title='" + messages.getString("adminConsol")
            + "'></a>&nbsp;");
      }

      pageContext.getOut()
          .println(
          "<a href=javascript:onClick=openPdc()><img src='" + icons.get("glossary")
          + "' align='absmiddle' border='0' alt='" + messages.getString("glossaire")
          + "' onFocus='self.blur()' title='" + messages.getString("glossaire") + "'></a>&nbsp;");
    } catch (IOException ioe) {
      // User probably disconnected ...
      throw new JspTagException("IO_ERROR");
    }

    return SKIP_BODY;
  }
}