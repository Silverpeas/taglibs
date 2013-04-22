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
import java.util.Iterator;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.publication.control.PublicationBm;
import com.stratelia.webactiv.util.publication.model.PublicationPK;

public class AllPublicationsTag extends BodyTagSupport {
  private static final long serialVersionUID = 1L;

  private Iterator iterator;
  private String name = "publication";
  private String componentId;
  private int iterations = 1;
  private boolean iterationsUsed = false;
  private PublicationBm publicationBm;

  public AllPublicationsTag() {
    super();
  }

  public void setIterations(int iterations) {
    this.iterations = iterations;
    iterationsUsed = true;
  }

  /*
   * public void setName(String name) { this.name = name; }
   */
  public void setComponentId(String componentId) {
    this.componentId = componentId;
  }

  @Override
  public int doStartTag() throws JspTagException {
    Collection allPublications = getAllPublications();
    if (allPublications.size() > 0) {
      iterator = allPublications.iterator();
    }
    if (iterator == null) {
      return SKIP_BODY;
    }
    return addNext(iterator);
  }

  @Override
  public int doAfterBody() throws JspTagException {
    return addNext(iterator);
  }

  @Override
  public int doEndTag() throws JspTagException {
    try {
      if (bodyContent != null) {
        bodyContent.writeOut(bodyContent.getEnclosingWriter());
      }
    } catch (java.io.IOException e) {
      throw new JspTagException("IO Error : " + e.getMessage());
    }
    return EVAL_PAGE;
  }

  protected int addNext(Iterator iterator) throws JspTagException {
    if (iterations > 0 && iterator.hasNext()) {
      pageContext.setAttribute(name, iterator.next(), PageContext.PAGE_SCOPE);
      if (iterationsUsed) {
        iterations--;
      }
      return EVAL_BODY_BUFFERED;
    }
    return SKIP_BODY;
  }

  private PublicationBm getPublicationBm() throws JspTagException {
    try {
      publicationBm = EJBUtilitaire.getEJBObjectRef(JNDINames.PUBLICATIONBM_EJBHOME,
          PublicationBm.class);
      return publicationBm;
    } catch (Exception e) {
      throw new JspTagException("NamingException : " + e.getMessage());
    }
  }

  private Collection getAllPublications() throws JspTagException {
    try {
      Collection allPublications =
          getPublicationBm().getAllPublications(
          new PublicationPK("useless", "useless", componentId), "P.pubCreationDate desc");
      return allPublications;
    } catch (Exception e) {
      throw new JspTagException("Getting info failed : " + e.getMessage());
    }
  }
}