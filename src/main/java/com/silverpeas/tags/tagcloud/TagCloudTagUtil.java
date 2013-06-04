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
package com.silverpeas.tags.tagcloud;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.silverpeas.tagcloud.ejb.TagCloudBm;
import com.silverpeas.tagcloud.ejb.TagCloudRuntimeException;
import com.silverpeas.tagcloud.model.TagCloud;
import com.silverpeas.tagcloud.model.TagCloudPK;
import com.silverpeas.tags.ComponentTagUtil;
import com.silverpeas.util.StringUtil;

import com.stratelia.webactiv.forums.forumsManager.ejb.ForumsBM;
import com.stratelia.webactiv.forums.models.Forum;
import com.stratelia.webactiv.forums.models.ForumPK;
import com.stratelia.webactiv.forums.models.Message;
import com.stratelia.webactiv.forums.models.MessagePK;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;
import com.stratelia.webactiv.util.publication.control.PublicationBm;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;
import com.stratelia.webactiv.util.publication.model.PublicationPK;

public class TagCloudTagUtil extends ComponentTagUtil {

  private TagCloudBm tagCloudBm = null;
  private PublicationBm publicationBm = null;
  private ForumsBM forumsBm = null;
  private String componentId = null;
  private String elementId = null;

  public TagCloudTagUtil(String componentId, String elementId, String userId) {
    super(componentId, userId, (componentId != null));
    this.componentId = componentId;
    this.elementId = elementId;
  }

  public Collection<TagCloud> getInstanceTagClouds(String maxCount) {
    if (StringUtil.isNotDefined(maxCount)) {
      return getTagCloudBm().getInstanceTagClouds(componentId);
    }
    return getTagCloudBm().getInstanceTagClouds(componentId, Integer.parseInt(maxCount));
  }

  public Collection<TagCloud> getPublicationTagClouds() throws RemoteException {
    return getElementTagClouds(TagCloud.TYPE_PUBLICATION);
  }

  public Collection<TagCloud> getForumTagClouds() throws RemoteException {
    return getElementTagClouds(TagCloud.TYPE_FORUM);
  }

  public Collection<TagCloud> getMessageTagClouds() throws RemoteException {
    return getElementTagClouds(TagCloud.TYPE_MESSAGE);
  }

  public String getPublicationTags() throws RemoteException {
    return getElementTags(TagCloud.TYPE_PUBLICATION);
  }

  public String getForumTags() throws RemoteException {
    return getElementTags(TagCloud.TYPE_FORUM);
  }

  public String getMessageTags() throws RemoteException {
    return getElementTags(TagCloud.TYPE_MESSAGE);
  }

  public Collection<PublicationDetail> getPublicationsByTags(String tags) throws RemoteException {
    Collection<TagCloud> tagClouds = getTagCloudBm().getTagCloudsByTags(tags, componentId,
        TagCloud.TYPE_PUBLICATION);
    return getPublications(tagClouds);
  }

  public Collection<PublicationDetail> getPublicationsByElement() throws RemoteException {
    Collection<TagCloud> tagClouds = getTagCloudBm().getTagCloudsByElement(componentId, elementId,
        TagCloud.TYPE_PUBLICATION);
    Collection<TagCloud> linkedTagClouds = new ArrayList<TagCloud>();
    for (TagCloud tagCloud : tagClouds) {
      linkedTagClouds.addAll(getTagCloudBm().getTagCloudsByTags(tagCloud.getTag(), componentId,
          TagCloud.TYPE_PUBLICATION));
    }
    return getPublications(linkedTagClouds);
  }

  public Collection<Forum> getForumsByTags(String tags) throws RemoteException {
    Collection<TagCloud> tagClouds = getTagCloudBm().getTagCloudsByTags(tags, componentId,
        TagCloud.TYPE_FORUM);
    if (!tagClouds.isEmpty()) {
      List<ForumPK> forumPKs = new ArrayList<ForumPK>();
      for (TagCloud tagCloud : tagClouds) {
        ForumPK forumPK = new ForumPK(tagCloud.getInstanceId(), tagCloud.getExternalId());
        if (!forumPKs.contains(forumPK)) {
          forumPKs.add(forumPK);
        }
      }
      return getForumsBm().getForumsList(forumPKs);
    }
    return Collections.<Forum>emptyList();
  }

  public Collection<Message> getThreadsByTags(String tags) throws RemoteException {
    Collection<TagCloud> tagClouds = getTagCloudBm().getTagCloudsByTags(tags, componentId,
        TagCloud.TYPE_MESSAGE);
    if (!tagClouds.isEmpty()) {
      List<MessagePK> messagePKs = new ArrayList<MessagePK>();
      for (TagCloud tagCloud : tagClouds) {
        MessagePK messagePK = new MessagePK(tagCloud.getInstanceId(), tagCloud.getExternalId());
        if (!messagePKs.contains(messagePK)) {
          messagePKs.add(messagePK);
        }
      }
      return getForumsBm().getThreadsList(messagePKs);
    }
    return Collections.<Message>emptyList();
  }

  private Collection<TagCloud> getElementTagClouds(int type) {
    return getTagCloudBm().getElementTagClouds(new TagCloudPK(elementId, componentId, type));
  }

  private String getElementTags(int type) {
    return getTagCloudBm().getTagsByElement(new TagCloudPK(elementId, componentId, type));
  }

  private Collection<PublicationDetail> getPublications(Collection<TagCloud> tagClouds) {
    if (!tagClouds.isEmpty()) {
      List<PublicationPK> publicationPKs = new ArrayList<PublicationPK>(tagClouds.size());
      for (TagCloud tagCloud : tagClouds) {
        PublicationPK publicationPK = new PublicationPK(tagCloud.getExternalId(), tagCloud.
            getInstanceId());
        if (!publicationPKs.contains(publicationPK)) {
          publicationPKs.add(publicationPK);
        }
      }
      return getPublicationBm().getPublications(publicationPKs);
    }
    return Collections.<PublicationDetail>emptyList();
  }

  private TagCloudBm getTagCloudBm() {
    if (tagCloudBm == null) {
      try {
        tagCloudBm = EJBUtilitaire.getEJBObjectRef(JNDINames.TAGCLOUDBM_EJBHOME, TagCloudBm.class);
      } catch (Exception e) {
        throw new TagCloudRuntimeException("TagCloudTagUtil.getTagCloudBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return tagCloudBm;
  }

  private PublicationBm getPublicationBm() {
    if (publicationBm == null) {
      try {
        publicationBm = EJBUtilitaire.getEJBObjectRef(JNDINames.PUBLICATIONBM_EJBHOME,
            PublicationBm.class);
      } catch (Exception e) {
        throw new TagCloudRuntimeException("TagCloudTagUtil.getPublicationBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return publicationBm;
  }

  private ForumsBM getForumsBm() {
    if (forumsBm == null) {
      try {
        forumsBm = EJBUtilitaire.getEJBObjectRef(JNDINames.FORUMSBM_EJBHOME, ForumsBM.class);
      } catch (Exception e) {
        throw new TagCloudRuntimeException("TagCloudTagUtil.getForumsBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return forumsBm;
  }
}