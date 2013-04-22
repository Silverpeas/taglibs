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

import com.stratelia.silverpeas.peasCore.MainSessionController;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.SpaceInst;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class SpaceChoiceTag extends TagSupport {

  protected String m_sContext;

  protected String m_sPrivateDomain;

  protected String[] m_asPrivateDomainsIds;

  protected OrganizationController m_organizationCtrl;

  protected MainSessionController m_mainSessionCtrl;

  protected boolean m_currentSpaceInMaintenance;

  protected int m_nMax;

  protected String m_homeSpaceIcon;

  protected String m_messageBackToMainPage;

  public void setSContext(String sContext) {
    this.m_sContext = sContext;
  }

  public void setSPrivateDomain(String sPrivateDomain) {
    this.m_sPrivateDomain = sPrivateDomain;
  }

  public void setAsPrivateDomainsIds(String[] asPrivateDomainsIds) {
    this.m_asPrivateDomainsIds = asPrivateDomainsIds;
  }

  public void setOrganizationCtrl(OrganizationController organizationCtrl) {
    this.m_organizationCtrl = organizationCtrl;
  }

  public void setMainSessionCtrl(MainSessionController mainSessionCtrl) {
    this.m_mainSessionCtrl = mainSessionCtrl;
  }

  public boolean getCurrentSpaceInMaintenance() {
    return m_currentSpaceInMaintenance;
  }

  public void setCurrentSpaceInMaintenance(boolean currentSpaceInMaintenance) {
    this.m_currentSpaceInMaintenance = currentSpaceInMaintenance;
  }

  public void setNMax(int nMax) {
    this.m_nMax = nMax;
  }

  public void setHomeSpaceIcon(String homeSpaceIcon) {
    this.m_homeSpaceIcon = homeSpaceIcon;
  }

  public void setMessageBackToMainPage(String messageBackToMainPage) {
    this.m_messageBackToMainPage = messageBackToMainPage;
  }

  public int doStartTag() throws JspException {
    try {
      pageContext.getOut().println("	<td width='100%' nowrap valign='top'>");
      pageContext.getOut().println(
          "		<img src='icons/1px.gif' height='20' width='0' align='absmiddle'>");
      pageContext.getOut().println("		<span class='selectNS'>");
      pageContext.getOut().println(
          "			<select name='privateDomain' size=1 onChange=changeSpace('" + m_sContext +
              "/admin/jsp/Main.jsp')>");

      // Other space selected
      String _sPrivateDomainName = null;
      String[] _asPrivateDomainsNames = m_organizationCtrl.getSpaceNames(m_asPrivateDomainsIds);

      for (int nI = 0; nI < m_asPrivateDomainsIds.length; nI++) {
        SpaceInst spaceInst = m_organizationCtrl.getSpaceInstById(m_asPrivateDomainsIds[nI]);
        if (spaceInst.getDomainFatherId().equals("0")) {
          _sPrivateDomainName = _asPrivateDomainsNames[nI];

          // Spaces in Maintenance with (M)
          if (!m_asPrivateDomainsIds[nI].equals("") && m_asPrivateDomainsIds[nI] != null) {
            if (m_mainSessionCtrl.isSpaceInMaintenance(m_asPrivateDomainsIds[nI].substring(2))) {
              _sPrivateDomainName += " (M)";
            }
            // Current space in maintenance ?
            if (m_mainSessionCtrl.isSpaceInMaintenance(m_sPrivateDomain.substring(2)))
              m_currentSpaceInMaintenance = true;
          }

          if (m_asPrivateDomainsIds[nI].equals(m_sPrivateDomain)) {
            pageContext.getOut().println(
                "				<option selected value=" + m_sPrivateDomain + ">" +
                    shortDomain(_sPrivateDomainName, m_nMax) + "</option>");
          } else
            pageContext.getOut().println(
                "				<option value=" + m_asPrivateDomainsIds[nI] + ">" +
                    shortDomain(_sPrivateDomainName, m_nMax) + "</option>");
        }
      }

      pageContext.getOut().println("			</select>");
      pageContext.getOut().println("		</span>");
      pageContext
          .getOut()
          .println(
              "		<a href='#' onclick=changeSpace('" +
                  m_sContext +
                  "/admin/jsp/Main.jsp')><img src='icons/1px.gif' width='2' height='1' border='0'><img src='" +
                  m_homeSpaceIcon + "' border='0' align='absmiddle' alt='" +
                  m_messageBackToMainPage + "' title='" + m_messageBackToMainPage + "'></a>");
      pageContext.getOut().println("	</td>");
    } catch (IOException ioe) {
      // User probably disconnected ...
      throw new JspTagException("IO_ERROR");
    }

    return SKIP_BODY;
  }

  private String shortDomain(String domainName, int nMax) {
    String _shortName;
    int _nbChar = nMax;

    _shortName = domainName;
    if (_shortName.length() > _nbChar)
      _shortName = domainName.substring(0, _nbChar - 3) + "...";

    return _shortName;
  }

}