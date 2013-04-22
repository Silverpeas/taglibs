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
package com.silverpeas.tags.forum;

import java.util.Collection;
import java.util.StringTokenizer;

import com.silverpeas.tagcloud.ejb.TagCloudRuntimeException;
import com.silverpeas.tags.ComponentTagUtil;

import com.stratelia.webactiv.forums.forumsManager.ejb.ForumsBM;
import com.stratelia.webactiv.forums.models.Forum;
import com.stratelia.webactiv.forums.models.ForumPK;
import com.stratelia.webactiv.forums.models.Message;
import com.stratelia.webactiv.forums.models.MessagePK;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

public class ForumTagUtil extends ComponentTagUtil {

  private ForumsBM forumsBm = null;
  private String componentId = null;
  private String elementId = null;

  public ForumTagUtil(String componentId, String elementId, String userId) {
    super(componentId, userId, (componentId != null));
    this.componentId = componentId;
    this.elementId = elementId;
  }

  public Collection<Forum> getForums()  {
    return getForumsBm().getForums(getForumPK(0));
  }

  public Forum getForumElement()  {
    return getForumsBm().getForum(getForumPK());
  }

  public Message getMessageElement()  {
    return getForumsBm().getMessage(getMessagePK());
  }

  public Collection<Message> getLastThreads(String forumIdAndCount)  {
    StringTokenizer st = new StringTokenizer(forumIdAndCount, ",");
    int forumId = Integer.parseInt(st.nextToken());
    int count = Integer.parseInt(st.nextToken());
    return getForumsBm().getLastThreads(getForumPK(forumId), count);
  }

  public Collection<Message> getNotAnsweredLastThreads(String forumIdAndCount) {
    StringTokenizer st = new StringTokenizer(forumIdAndCount, ",");
    int forumId = Integer.parseInt(st.nextToken());
    int count = Integer.parseInt(st.nextToken());
    return getForumsBm().getNotAnsweredLastThreads(getForumPK(forumId), count);
  }

  private ForumsBM getForumsBm() {
    if (forumsBm == null) {
      try {
        forumsBm = EJBUtilitaire.getEJBObjectRef(JNDINames.FORUMSBM_EJBHOME, ForumsBM.class);
      } catch (Exception e) {
        throw new TagCloudRuntimeException("ForumTagUtil.getForumsBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return forumsBm;
  }

  private ForumPK getForumPK() {
    return new ForumPK(componentId, elementId);
  }

  private ForumPK getForumPK(int forumId) {
    return new ForumPK(componentId, String.valueOf(forumId));
  }

  private MessagePK getMessagePK() {
    return new MessagePK(componentId, elementId);
  }
}
