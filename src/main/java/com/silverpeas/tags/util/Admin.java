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

/*
 * Created on 7 juin 2005
 *
 */
package com.silverpeas.tags.util;

import java.rmi.RemoteException;

import com.silverpeas.admin.ejb.AdminBmRuntimeException;
import com.silverpeas.admin.ejb.AdminBusiness;

import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.ComponentInst;
import com.stratelia.webactiv.beans.admin.SpaceInst;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

/**
 * @author neysseri
 */
public class Admin {

  private AdminBusiness adminBm = null;

  public Admin() {
  }

  // check if the user is allowed to access the required component
  public boolean isUserAllowed(String userId, String componentId) throws RemoteException {
    SilverTrace.info("peasUtil", "Admin.isUserAllowed()", "root.MSG_GEN_ENTER_METHOD", "userId = "
        + userId + ", componentId = " + componentId);
    boolean isAllowed;
    if (componentId == null) {
      isAllowed = false;
    } else {
      isAllowed = getAdminBm().isComponentAvailable("useless", componentId, userId);
    }
    return isAllowed;
  }

  public ComponentInst getComponentInst(String componentId) throws RemoteException {
    return getAdminBm().getComponentInst(componentId);
  }

  public SpaceInst getSpaceInst(String spaceId) throws RemoteException {
    return getAdminBm().getSpaceInstById(spaceId);
  }

  private AdminBusiness getAdminBm() {
    if (adminBm == null) {
      try {
        adminBm = EJBUtilitaire.getEJBObjectRef(JNDINames.ADMINBM_EJBHOME, AdminBusiness.class);
      } catch (Exception e) {
        throw new AdminBmRuntimeException("Admin.getAdminBm", SilverpeasRuntimeException.ERROR,
            "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return adminBm;
  }
}
