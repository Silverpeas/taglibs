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

package com.silverpeas.tags.organization;

public class MenuItem extends Object {
  public static final int TYPE_SPACE = 0;
  public static final int TYPE_COMPONENT = 1;
  public static final int TYPE_COMPONENT_ORGA = 2;
  public static final int TYPE_COMPONENT_CONTENT = 3;
  public static final int TYPE_PDC_VALUE = 4;
  public static final int TYPE_UNKNOWN = 99;

  public String name = "";
  public String description = "";
  public int level = 0;
  public int type = TYPE_UNKNOWN;
  public String id = "";
  public String fatherId = ""; // useful ??

  public String spaceId = "";
  public String componentId = "";

  public String iconPath;
  public String hrefPath;
  public String hrefTitle;

  public MenuItem(String name, int type) {
    this.name = name;
    this.type = type;
  }

  public MenuItem(String name, String description, int level, int type, String id, String fatherId) {
    this.name = name;
    this.description = description;
    this.level = level;
    this.type = type;
    this.id = id;
    this.fatherId = fatherId;
    if (type == TYPE_COMPONENT)
      this.spaceId = fatherId;
  }

  public MenuItem(String spaceId, String componentId, String id) {
    this.spaceId = spaceId;
    this.componentId = componentId;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public int getLevel() {
    return level;
  }

  public int getType() {
    return type;
  }

  public String getId() {
    return id;
  }

  public String getFatherId() {
    return fatherId;
  }

  public void setSpaceId(String spaceId) {
    this.spaceId = spaceId;
  }

  public String getSpaceId() {
    return spaceId;
  }

  public void setComponentId(String componentId) {
    this.componentId = componentId;
  }

  public String getComponentId() {
    return componentId;
  }

  public void setIconPath(String iconPath) {
    this.iconPath = iconPath;
  }

  public String getIconPath() {
    return iconPath;
  }

  public void setHrefPath(String hrefPath) {
    this.hrefPath = hrefPath;
  }

  public String getHrefPath() {
    return hrefPath;
  }

  public void setHrefTitle(String hrefTitle) {
    this.hrefTitle = hrefTitle;
  }

  public String getHrefTitle() {
    return hrefTitle;
  }
};