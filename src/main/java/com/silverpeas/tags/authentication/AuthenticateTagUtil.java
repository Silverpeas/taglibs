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
package com.silverpeas.tags.authentication;

import java.io.Serializable;

import com.silverpeas.admin.ejb.AdminBmRuntimeException;
import com.silverpeas.admin.ejb.AdminBusiness;
import com.silverpeas.authentication.ejb.AuthenticationBm;

import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

public class AuthenticateTagUtil implements Serializable {
  private static final long serialVersionUID = 1L;

  private AdminBusiness adminBm = null;
  private AuthenticationBm authentication = null;

  public AuthenticateTagUtil() {
  }

  public String authenticate(String login, String password, String domainId, String sessionId) {
    try {
      // Authenticate the user (through Silverpeas or LDAP or...)
      String key = getAuthenticationBm().authenticate(login, password, domainId);

      String userId = getAdminBm().authenticate(key, sessionId);

      return userId;
    } catch (Exception e) {
      return null;
    }
  }

  private AdminBusiness getAdminBm() {
    if (adminBm == null) {
      try {
        adminBm = EJBUtilitaire.getEJBObjectRef(JNDINames.ADMINBM_EJBHOME, AdminBusiness.class);
      } catch (Exception e) {
        throw new AdminBmRuntimeException("AuthenticateTag.getAdminBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return adminBm;
  }

  private AuthenticationBm getAuthenticationBm() {
    if (authentication == null) {
      try {
        authentication = EJBUtilitaire.getEJBObjectRef(JNDINames.AUTHENTICATIONBM_EJBHOME,
            AuthenticationBm.class);
      } catch (Exception e) {
        throw new AdminBmRuntimeException("AuthenticateTag.getAuthenticationBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return authentication;
  }
}
