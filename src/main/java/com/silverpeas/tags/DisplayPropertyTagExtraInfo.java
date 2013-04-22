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

package com.silverpeas.tags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;

public class DisplayPropertyTagExtraInfo extends TagExtraInfo {

  public boolean isValid(TagData data) {
    Object o = data.getAttribute("object");
    if ((o != null) && (o != TagData.REQUEST_TIME_VALUE)) {
      return false;
    }
    String name = data.getAttributeString("name");
    String scope = data.getAttributeString("scope");

    /*
     * If an object was provided, reject name and scope attributes. Else verify that at least the
     * name attribute is available.
     */
    if (o != null) {
      if (null != name || null != scope) {
        return false;
      }
    } else {
      if (null == name) {
        return false;
      }

      if (null != scope &&
          !scope.equals(DisplayPropertyTag.PAGE_ID) &&
          !scope.equals(DisplayPropertyTag.REQUEST_ID) &&
          !scope.equals(DisplayPropertyTag.SESSION_ID) &&
          !scope.equals(DisplayPropertyTag.APPLICATION_ID)) {
        return false;
      }
    }

    /*
     * Verify that if an index was provided so was the property name.
     */
    if ((null != data.getAttribute("index")) &&
        (null == data.getAttribute("property"))) {
      return false;
    }
    return true;
  }
}