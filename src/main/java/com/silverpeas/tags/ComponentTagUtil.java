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

/*
 * Created on 7 juin 2005
 *
 */
package com.silverpeas.tags;

import com.silverpeas.admin.ejb.AdminBmRuntimeException;
import com.silverpeas.tags.util.Admin;
import com.silverpeas.tags.util.AuthorizationException;
import com.silverpeas.tags.util.SiteTagUtil;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

/**
 * @author neysseri
 */
public class ComponentTagUtil {

  private Admin admin = null;
  private String userId = null;

  public ComponentTagUtil(String componentId, String userId) {
    setUserId(userId);
    init(componentId, true);
  }

  public ComponentTagUtil(String componentId, String userId, boolean check) {
    setUserId(userId);
    init(componentId, check);
  }

  private void init(String componentId, boolean check) {
    if (check)
      checkAuthorization(componentId);
  }

  private void checkAuthorization(String componentId) {
    // Check webUser rights
    if (!isUserAllowed(componentId)) {
      String errorMessage =
          "Warning ! User identified by id '" + getUserId() +
              "' is not allowed to access to component identified by componentId '" + componentId +
              "'";
      throw new AuthorizationException(errorMessage);
    }
  }

  private boolean isUserAllowed(String componentId) {
    try {
      return getAdmin().isUserAllowed(getUserId(), componentId);
    } catch (Exception e) {
      throw new AdminBmRuntimeException("ComponentTagUtil.isUserAllowed()",
          SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
    }
  }

  public Admin getAdmin() {
    if (admin == null) {
      admin = new Admin();
    }
    return admin;
  }

  public String getUserId() {
    if (userId == null)
      return SiteTagUtil.getUserId();
    else
      return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

}