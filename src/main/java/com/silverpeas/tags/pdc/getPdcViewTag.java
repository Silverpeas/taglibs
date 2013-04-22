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

package com.silverpeas.tags.pdc;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class getPdcViewTag extends TagSupport {

  public static final String PAGE_ID = "page";
  public static final String REQUEST_ID = "request";
  public static final String SESSION_ID = "session";
  public static final String APPLICATION_ID = "application";

  private String scope = REQUEST_ID;
  private String name;
  private String axisId;
  private String valueId;
  private int depth = -1;
  private String spaceId;
  private String componentId;
  private boolean skipSpaceId = false;

  public boolean isSpaceIdSkipped() {
    return skipSpaceId;
  }

  public void setSkipSpaceId(boolean skipSpaceId) {
    this.skipSpaceId = skipSpaceId;
  }

  public String getComponentId() {
    return componentId;
  }

  public void setComponentId(String componentId) {
    this.componentId = componentId;
  }

  public getPdcViewTag() {
    super();
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setAxisId(String axisId) {
    this.axisId = axisId;
  }

  public String getAxisId() {
    return axisId;
  }

  public void setValueId(String valueId) {
    this.valueId = valueId;
  }

  public String getValueId() {
    return valueId;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  public int getDepth() {
    return depth;
  }

  public String getSpaceId() {
    return spaceId;
  }

  public void setSpaceId(String spaceId) {
    this.spaceId = spaceId;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public int doStartTag() throws JspTagException {
    // create a new object which have a reference on a pdc ejb
    PdcTagUtil ptu = new PdcTagUtil(getAxisId(), getValueId(), getDepth(), getSpaceId());
    ptu.setComponentId(getComponentId());
    pageContext.setAttribute(getName(), ptu, translateScope(scope));
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
    } else
      return PageContext.REQUEST_SCOPE;
  }
}