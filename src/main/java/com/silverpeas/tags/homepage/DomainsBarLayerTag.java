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

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class DomainsBarLayerTag extends TagSupport {

  public int doStartTag() throws JspException {
    try {
      pageContext
          .getOut()
          .println(
              "<div id='Layer1' style='position:absolute; left:0px; top:70px; width:135px; height:50px; z-index:10; visibility: hidden'>");
      pageContext.getOut().println(
          "	<table width='100%' cellspacing='0' cellpadding='0' border='0'>");
      pageContext.getOut().println("		<tr class='intfdcolor13'>");
      pageContext.getOut().println("			<td width='100%'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("			<td><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("			<td class='intfdcolor4'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("		</tr>");
      pageContext.getOut().println("		<tr class='intfdcolor4'>");
      pageContext.getOut().println("			<td width='100%'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("			<td class='intfdcolor4'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("			<td class='intfdcolor13'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("		</tr>");
      pageContext.getOut().println("		<tr class='intfdcolor51'>");
      pageContext.getOut().println(
          "			<td width='100%'><img src='icons/1px.gif' width='1' height='10'></td>");
      pageContext.getOut().println("			<td class='intfdcolor51'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("			<td class='intfdcolor13'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("		</tr>");
      pageContext.getOut().println("		<tr class='intfdcolor51'>");
      pageContext.getOut().println("			<td width='100%'>");
      /****
       * &nbsp;<select name="navigation' onchange="top.scriptFrame.jumpDomainsbar()'> <option
       * value=''><%=message.getString("Choose')%></option> <option
       * value=''>--------------------</option> <option
       * value="DomainsBar.jsp'><%=message.getString("bySpace')%></option> <option
       * value="DomainsBarComponent.jsp'><%=message.getString("byComponent')%></option> </select>
       *****/
      pageContext.getOut().println("			</td>");
      pageContext.getOut().println("			<td><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("			<td class='intfdcolor'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("		</tr>");
      pageContext.getOut().println("		<tr class='intfdcolor51'>");
      pageContext.getOut().println(
          "			<td width='100%'><img src='icons/1px.gif' width='1' height='10'></td>");
      pageContext.getOut().println("			<td class='intfdcolor51'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("			<td class='intfdcolor13'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("		</tr>");
      pageContext.getOut().println("		<tr class='intfdcolor4'>");
      pageContext.getOut().println("			<td width='100%'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("			<td class='intfdcolor4'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("			<td class='intfdcolor13'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("		</tr>");
      pageContext.getOut().println("		<tr class='intfdcolor13'>");
      pageContext.getOut().println("			<td width='100%'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("			<td><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("			<td class='intfdcolor4'><img src='icons/1px.gif'></td>");
      pageContext.getOut().println("		</tr>");
      pageContext.getOut().println("	</table>");
      pageContext.getOut().println("</div>");
    } catch (IOException ioe) {
      // User probably disconnected ...
      throw new JspTagException("IO_ERROR");
    }

    return SKIP_BODY;
  }

}