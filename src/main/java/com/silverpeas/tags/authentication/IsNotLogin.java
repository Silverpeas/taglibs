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

package com.silverpeas.tags.authentication;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class IsNotLogin extends BodyTagSupport {
  private static final long serialVersionUID = 1L;

  BodyContent bodyContent;

  @Override
  public int doStartTag() throws JspTagException {
    return EVAL_BODY_BUFFERED;
  }

  @Override
  public void setBodyContent(BodyContent bodyContent) {
    this.bodyContent = bodyContent;
  }

  @Override
  public int doEndTag() throws JspTagException {
    try {
      HttpSession session = pageContext.getSession();

      String userId = (String) session.getAttribute("UserId");
      if (userId == null || "-1".equals(userId)) {
        if (bodyContent != null)
          bodyContent.writeOut(bodyContent.getEnclosingWriter());
      }
    } catch (java.io.IOException e) {
      throw new JspTagException("IO Error : " + e.getMessage());
    }
    return EVAL_PAGE;
  }
}