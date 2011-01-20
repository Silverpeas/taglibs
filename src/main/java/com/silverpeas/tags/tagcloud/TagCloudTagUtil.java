package com.silverpeas.tags.tagcloud;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.silverpeas.tagcloud.ejb.TagCloudBm;
import com.silverpeas.tagcloud.ejb.TagCloudBmHome;
import com.silverpeas.tagcloud.ejb.TagCloudRuntimeException;
import com.silverpeas.tagcloud.model.TagCloud;
import com.silverpeas.tagcloud.model.TagCloudPK;
import com.silverpeas.tags.ComponentTagUtil;
import com.silverpeas.tags.util.EJBDynaProxy;
import com.stratelia.webactiv.forums.forumsManager.ejb.ForumsBM;
import com.stratelia.webactiv.forums.models.ForumPK;
import com.stratelia.webactiv.forums.models.MessagePK;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;
import com.stratelia.webactiv.util.publication.control.PublicationBm;
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

  public Collection getInstanceTagClouds(String maxCount) throws RemoteException {
    if (maxCount == null || maxCount.length() == 0) {
      return getTagCloudBm().getInstanceTagClouds(componentId);
    } else {
      return getTagCloudBm().getInstanceTagClouds(componentId, Integer.parseInt(maxCount));
    }
  }

  public Collection getPublicationTagClouds() throws RemoteException {
    return getElementTagClouds(TagCloud.TYPE_PUBLICATION);
  }

  public Collection getForumTagClouds() throws RemoteException {
    return getElementTagClouds(TagCloud.TYPE_FORUM);
  }

  public Collection getMessageTagClouds() throws RemoteException {
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

  public Collection getPublicationsByTags(String tags) throws RemoteException {
    Collection tagClouds = getTagCloudBm().getTagCloudsByTags(
        tags, componentId, TagCloud.TYPE_PUBLICATION);

    return getPublications(tagClouds);
  }

  public Collection getPublicationsByElement() throws RemoteException {
    Collection tagClouds = getTagCloudBm().getTagCloudsByElement(
        componentId, elementId, TagCloud.TYPE_PUBLICATION);

    Collection linkedTagClouds = new ArrayList();
    Iterator iter = tagClouds.iterator();
    TagCloud tagCloud;
    while (iter.hasNext()) {
      tagCloud = (TagCloud) iter.next();
      linkedTagClouds.addAll(getTagCloudBm().getTagCloudsByTags(
          tagCloud.getTag(), componentId, TagCloud.TYPE_PUBLICATION));
    }

    return getPublications(linkedTagClouds);
  }

  public Collection getForumsByTags(String tags) throws RemoteException {
    Collection tagClouds = getTagCloudBm().getTagCloudsByTags(
        tags, componentId, TagCloud.TYPE_FORUM);

    Iterator iter = tagClouds.iterator();
    TagCloud tagCloud;
    if (!tagClouds.isEmpty()) {
      ArrayList forumPKs = new ArrayList();
      while (iter.hasNext()) {
        tagCloud = (TagCloud) iter.next();
        ForumPK forumPK = new ForumPK(
            tagCloud.getInstanceId(), tagCloud.getExternalId());
        if (!forumPKs.contains(forumPK)) {
          forumPKs.add(forumPK);
        }
      }
      return getForumsBm().getForumsList(forumPKs);
    }
    return new ArrayList();
  }

  public Collection getThreadsByTags(String tags) throws RemoteException {
    Collection tagClouds = getTagCloudBm().getTagCloudsByTags(
        tags, componentId, TagCloud.TYPE_MESSAGE);

    ArrayList messagesList = new ArrayList();
    Iterator iter = tagClouds.iterator();
    TagCloud tagCloud;
    if (!tagClouds.isEmpty()) {
      ArrayList messagePKs = new ArrayList();
      while (iter.hasNext()) {
        tagCloud = (TagCloud) iter.next();
        MessagePK messagePK = new MessagePK(
            tagCloud.getInstanceId(), tagCloud.getExternalId());
        if (!messagePKs.contains(messagePK)) {
          messagePKs.add(messagePK);
        }
      }
      return getForumsBm().getThreadsList(messagePKs);
    }
    return messagesList;
  }

  private Collection getElementTagClouds(int type) throws RemoteException {
    return getTagCloudBm().getElementTagClouds(new TagCloudPK(elementId, componentId, type));
  }

  private String getElementTags(int type) throws RemoteException {
    return getTagCloudBm().getTagsByElement(new TagCloudPK(elementId, componentId, type));
  }

  private Collection getPublications(Collection tagClouds) throws RemoteException {
    if (!tagClouds.isEmpty()) {
      ArrayList publicationPKs = new ArrayList();
      Iterator iter = tagClouds.iterator();
      TagCloud tagCloud;
      while (iter.hasNext()) {
        tagCloud = (TagCloud) iter.next();
        PublicationPK publicationPK = new PublicationPK(
            tagCloud.getExternalId(), tagCloud.getInstanceId());
        if (!publicationPKs.contains(publicationPK)) {
          publicationPKs.add(publicationPK);
        }
      }
      return getPublicationBm().getPublications(publicationPKs);
    } else {
      return new ArrayList();
    }
  }

  private TagCloudBm getTagCloudBm() {
    if (tagCloudBm == null) {
      try {
        tagCloudBm = ((TagCloudBmHome) EJBUtilitaire.getEJBObjectRef(
            JNDINames.TAGCLOUDBM_EJBHOME, TagCloudBmHome.class)).create();
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
        publicationBm = (PublicationBm) EJBDynaProxy.createProxy(
            JNDINames.PUBLICATIONBM_EJBHOME, PublicationBm.class);
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
        forumsBm = (ForumsBM) EJBDynaProxy.createProxy(
            JNDINames.FORUMSBM_EJBHOME, ForumsBM.class);
      } catch (Exception e) {
        throw new TagCloudRuntimeException("TagCloudTagUtil.getForumsBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return forumsBm;
  }
}