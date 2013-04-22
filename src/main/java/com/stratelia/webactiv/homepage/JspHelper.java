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
package com.stratelia.webactiv.homepage;

import java.util.ArrayList;
import java.util.Vector;

import com.silverpeas.util.StringUtil;

import com.stratelia.silverpeas.peasCore.MainSessionController;
import com.stratelia.webactiv.beans.admin.ComponentInst;
import com.stratelia.webactiv.util.ResourceLocator;

public class JspHelper {

  public static ArrayList sortComponentList(Vector vAllowedComponents, ArrayList alCompoInst) {
    ArrayList sortedComponents = new ArrayList();
    ArrayList labels = new ArrayList(vAllowedComponents.size());
    ArrayList indexes = new ArrayList();
    for (int idx = 0; idx < vAllowedComponents.size(); idx++) {
      int nK = ((Integer) vAllowedComponents.get(idx)).intValue();
      String label = ((ComponentInst) alCompoInst.get(nK)).getLabel();
      labels.add(label);
      indexes.add(idx);
    }
    int lastIndex = -1;
    while (labels.size() != 0) {
      int maxIdx = getMaxString(labels, lastIndex);
      if (maxIdx < 0) {
        break;
      }
      sortedComponents.add(vAllowedComponents.get(((Integer) indexes.get(maxIdx)).intValue()));
      lastIndex = maxIdx;
    }

    return sortedComponents;
  }

  /**
   * @return max String index
   */
  public static int getMaxString(ArrayList labels, int currindx) {
    if (labels == null || labels.isEmpty()) {
      return -1;
    }

    String currMaxString = null;
    String lastFoundedString = null;
    int currIdx = -1;

    if (currindx != -1) {
      lastFoundedString = (String) labels.get(currindx);
    }

    for (int i = 0; i < labels.size(); i++) {
      String str = (String) labels.get(i);

      if (currMaxString == null) {
        if (currindx == -1 || (str.compareToIgnoreCase(lastFoundedString) > 0)
            || (str.compareToIgnoreCase(lastFoundedString) == 0 && currindx < i)) {
          currIdx = i;
          currMaxString = str;
        }
        continue;
      }

      if ((str.compareToIgnoreCase(currMaxString) < 0 && (lastFoundedString == null || str
          .compareToIgnoreCase(lastFoundedString) > 0))
          || (str.compareToIgnoreCase(currMaxString) == 0 && currindx < i)) {
        currIdx = i;
        currMaxString = str;
      }

    }

    return currIdx;
  }

  /**
   * @return formatted caption string for axes in DomainsBar.jsp
   */
  // public static String formatAxesCaption(String component_id, String domain, String subDomain,
  // ResourceLocator message, MainSessionController m_MainSessionCtrl) {
  public static String formatAxesCaption(String componentId, String askingDomain,
      ResourceLocator message, MainSessionController mainSessionController) {
    String additionalMessage = "";
    if (StringUtil.isNotDefined(componentId)) {

      // String askingDomain = (subDomain == null || "".equals(subDomain) || "".equals(subDomain))?
      // domain: subDomain;

      if (askingDomain != null && askingDomain.length() > 0) {
        additionalMessage =
            mainSessionController.getOrganisationController().getSpaceInstById(askingDomain).getName();
      }

    } else {
      ComponentInst inst =
          mainSessionController.getOrganisationController().getComponentInst(componentId);
      if (inst != null) {
        additionalMessage = inst.getLabel();
      } else {
        additionalMessage = "";
      }
    }
    return message.getString("AxisCollaboration") + " " + additionalMessage;
  }
}