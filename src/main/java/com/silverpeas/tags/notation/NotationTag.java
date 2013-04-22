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

package com.silverpeas.tags.notation;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.silverpeas.tags.authentication.AuthenticationManager;

public class NotationTag extends TagSupport {

  public static final String PAGE_ID = "page";
  public static final String REQUEST_ID = "request";
  public static final String SESSION_ID = "session";
  public static final String APPLICATION_ID = "application";
  private static final long serialVersionUID = 1L;

  private String name;
  private String scope = REQUEST_ID;
  private String componentId;
  private String elementId;
  private String authorId;
  private String forceReload = "false";

  public NotationTag() {
    super();
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    if (name == null || name.equals("null")) {
      name = componentId;
    }
    return name;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getScope() {
    return scope;
  }

  public void setComponentId(String componentId) {
    this.componentId = componentId;
  }

  public String getComponentId() {
    return componentId;
  }

  public void setElementId(String elementId) {
    this.elementId = elementId;
  }

  public String getElementId() {
    return elementId;
  }

  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  public String getAuthorId() {
    return authorId;
  }

  public String getForceReload() {
    return forceReload;
  }

  public void setForceReload(String forceReload) {
    this.forceReload = forceReload;
  }

  @Override
  public int doStartTag() throws JspTagException {
    int iScope = translateScope(scope);

    NotationTagUtil tagCloudTagUtil = (NotationTagUtil) pageContext.getAttribute(
        getName(), iScope);
    if (tagCloudTagUtil == null || "true".equals(forceReload)) {
      String userId = AuthenticationManager.getUserId(pageContext.getRequest());

      // create a new object which has a reference on Notation EJB
      tagCloudTagUtil = new NotationTagUtil(
          getComponentId(), getElementId(), userId, getAuthorId());
      pageContext.setAttribute(getName(), tagCloudTagUtil, iScope);
    }
    return EVAL_PAGE;
  }

  protected int translateScope(String scope) {
    if (scope.equalsIgnoreCase(PAGE_ID)) {
      return PageContext.PAGE_SCOPE;
    } else if (scope.equalsIgnoreCase(REQUEST_ID)) {
      return PageContext.REQUEST_SCOPE;
    } else if (scope.equalsIgnoreCase(SESSION_ID)) {
      return PageContext.SESSION_SCOPE;
    } else if (scope.equalsIgnoreCase(APPLICATION_ID)) {
      return PageContext.APPLICATION_SCOPE;
    } else {
      return PageContext.REQUEST_SCOPE;
    }
  }

}