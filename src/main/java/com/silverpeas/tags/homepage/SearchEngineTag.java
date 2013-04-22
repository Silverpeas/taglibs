/**
 * Copyright (C) 2000 - 2012 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.tags.homepage;

import com.stratelia.webactiv.util.ResourceLocator;
import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class SearchEngineTag extends TagSupport {

  protected ResourceLocator m_message;
  protected Hashtable m_icons;

  public void setMessage(ResourceLocator message) {
    this.m_message = message;
  }

  public void setIcons(Hashtable icons) {
    this.m_icons = icons;
  }

  public int doStartTag() throws JspException {
    try {
      pageContext.getOut().println(
          "		&nbsp;<a href='javascript:advancedSearchEngine()' class='Titre'>" +
              m_message.getString("SearchAdvanced") + "</a><br>");
      pageContext.getOut().println("		<script language=javascript>");
      pageContext.getOut().println("			<!--");
      pageContext.getOut().println("		    if (navigator.appName == 'Netscape')");
      pageContext.getOut().println(
          "				document.write('&nbsp;<input type=text name=query size=8 value=>');");
      pageContext.getOut().println("		    else");
      pageContext.getOut().println(
          "				document.write('&nbsp;<input type=text name=query size=12 value=>');");
      pageContext.getOut().println("		    //-->");
      pageContext.getOut().println("		</script>");
      pageContext.getOut().println(
          "		<a href='javascript:searchEngine()'><img border='0' src='" + m_icons.get("okIcon") +
              "' align='absmiddle'></a>");
    } catch (IOException ioe) {
      // User probably disconnected ...
      throw new JspTagException("IO_ERROR");
    }

    return SKIP_BODY;
  }

}