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
package com.silverpeas.tags.almanach;

import java.util.Collection;

import com.silverpeas.tags.ComponentTagUtil;

import com.stratelia.webactiv.almanach.control.ejb.AlmanachBm;
import com.stratelia.webactiv.almanach.control.ejb.AlmanachRuntimeException;
import com.stratelia.webactiv.almanach.model.EventDetail;
import com.stratelia.webactiv.almanach.model.EventPK;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

public class AlmanachTagUtil extends ComponentTagUtil {

  private AlmanachBm almanachBm = null;
  private String componentId = null;
  private String spaceId = "useless";

  public AlmanachTagUtil(String componentId, String userId) {
    super(componentId, userId);

    this.componentId = componentId;
  }

  public Collection<EventDetail> getAllEvents() {
    return getAlmanachBm().getAllEvents(new EventPK("", spaceId, componentId));
  }

  public EventDetail getEventDetail(String id) {
    return getAlmanachBm().getEventDetail(new EventPK(id, spaceId, componentId));
  }

  private AlmanachBm getAlmanachBm() {
    if (almanachBm == null) {
      try {
        almanachBm = EJBUtilitaire.getEJBObjectRef(JNDINames.ALMANACHBM_EJBHOME,
            AlmanachBm.class);
      } catch (Exception e) {
        throw new AlmanachRuntimeException("AlmanachTagUtil.getAlmanachBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return almanachBm;
  }
}