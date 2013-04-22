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

package com.silverpeas.tags.util;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.stratelia.silverpeas.silvertrace.SilverTrace;

public class SiteTag extends TagSupport {
  private static final long serialVersionUID = 4514691251508452414L;

  int mode = 0;
  String userId = "undefined";
  String serverName = "undefined";
  String serverPort = null;
  String context = null;
  String fileServerName = "undefined";
  String userAuthenticationClass = "com.silverpeas.tags.authentication.BasicAuthentication";
  String language = null;
  String httpMode = null;

  public void setMode(int mode) {
    this.mode = mode;
  }

  public void setUserId(String userId) {
    SilverTrace.info("peasUtil", "SiteTag.setUserId", "root.MSG_GEN_ENTER_METHOD", "userId = " +
        userId);
    this.userId = userId;
  }

  public void setServer(String sName) {
    this.serverName = sName;
  }

  public void setPort(String sPort) {
    this.serverPort = sPort;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public void setFileServer(String fileServerName) {
    this.fileServerName = fileServerName;
  }

  public void setUserAuthenticationClass(String userAuthenticationClass) {
    this.userAuthenticationClass = userAuthenticationClass;
  }

  /**
   * Set language for the site (Ex: en, fr)
   * @param language
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Set httpMode for the site (Ex: http:// our https://)
   * @param mode
   */
  public void setHttpMode(String httpMode) {
    this.httpMode = httpMode;
  }

  public int doStartTag() throws JspTagException {
    SiteTagUtil.setMode(this.mode);
    SiteTagUtil.setUserId(this.userId);
    SiteTagUtil.setServerName(this.serverName);
    SiteTagUtil.setServerPort(this.serverPort);
    SiteTagUtil.setServerContext(this.context);
    SiteTagUtil.setFileServerName(this.fileServerName);
    SiteTagUtil.setUserAuthenticationClass(userAuthenticationClass);
    SiteTagUtil.setLanguage(this.language);
    SiteTagUtil.setHttpMode(this.httpMode);
    return EVAL_PAGE;
  }

  protected int translateScope(String scope) {
    return PageContext.SESSION_SCOPE;
  }
}