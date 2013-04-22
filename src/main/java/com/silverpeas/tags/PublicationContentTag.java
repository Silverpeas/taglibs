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
package com.silverpeas.tags;

import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.publication.control.PublicationBm;
import com.stratelia.webactiv.util.publication.info.model.InfoTextDetail;
import com.stratelia.webactiv.util.publication.model.CompletePublication;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;

public class PublicationContentTag extends TagSupport {

  public static final String PAGE_ID = "page";
  public static final String REQUEST_ID = "request";
  public static final String SESSION_ID = "session";
  public static final String APPLICATION_ID = "application";
  private static final long serialVersionUID = 1L;

  /* the object that we are going to show */
  protected Object obj = null;

  /* the name of the object that we are going to show */
  protected String objName = null;

  /* the scope of the object that we are going to show */
  protected String objScope = null;

  public void setObject(Object o) {
    this.obj = o;
  }

  public void setName(String name) {
    this.objName = name;
  }

  public void setScope(String scope) {
    this.objScope = scope;
  }

  @Override
  public int doStartTag() throws JspException {
    PublicationDetail pubDetail = (PublicationDetail) getPointedObject(objName, objScope);
    if (pubDetail != null) {
      try {
        CompletePublication publication = getPublicationBm().getCompletePublication(pubDetail.
            getPK());
        processObject(publication);
      } catch (Exception e) {
        throw new JspTagException("getCompletePublication failed ! : " + objName);
      }
    }
    return SKIP_BODY;
  }

  protected Object getPointedObject(String name, String scope) throws JspException {
    Object rc;
    if (null != scope) {
      rc = pageContext.getAttribute(name, translateScope(scope));
    } else {
      rc = pageContext.findAttribute(name);
    }
    if (null == rc) {
      throw new JspTagException("No object : " + name);
    }

    return rc;
  }

  protected int translateScope(String scope) throws JspException {
    if (PAGE_ID.equalsIgnoreCase(scope)) {
      return PageContext.PAGE_SCOPE;
    } else if (REQUEST_ID.equalsIgnoreCase(scope)) {
      return PageContext.REQUEST_SCOPE;
    } else if (SESSION_ID.equalsIgnoreCase(scope)) {
      return PageContext.SESSION_SCOPE;
    } else if (APPLICATION_ID.equalsIgnoreCase(scope)) {
      return PageContext.APPLICATION_SCOPE;
    }

    // No such scope, this is probably an error maybe the
    // TagExtraInfo associated with thit tag was not configured
    // signal that by throwing a JspException
    throw new JspTagException("No such scope : " + scope);
  }

  protected void processObject(CompletePublication cp) throws JspException {
    try {
      if (null != cp) {
        Collection<InfoTextDetail> contentList = cp.getInfoDetail().getInfoTextList();
        StringBuilder content = new StringBuilder();
        if (contentList != null) {
          for (InfoTextDetail textDetail : contentList) {
            content.append(textDetail.getContent());
          }
        }
        pageContext.getOut().println(content.toString());
      } else {
        pageContext.getOut().println("INSTEAD_NULL");
      }
    } catch (java.io.IOException ioe) {
      // User probably disconnected ...
      throw new JspTagException("IO_ERROR");
    }
  }

  protected void clearProperties() {
    obj = null;
    objName = null;
    objScope = null;
  }

  private PublicationBm getPublicationBm() throws JspTagException {
    try {
      return EJBUtilitaire.getEJBObjectRef(JNDINames.PUBLICATIONBM_EJBHOME, PublicationBm.class);
    } catch (Exception e) {
      throw new JspTagException("NamingException : " + e.getMessage());
    }
  }
}