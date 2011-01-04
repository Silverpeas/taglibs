package com.silverpeas.tags.forum;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.StringTokenizer;

import com.silverpeas.tagcloud.ejb.TagCloudRuntimeException;
import com.silverpeas.tags.ComponentTagUtil;
import com.stratelia.webactiv.forums.forumsManager.ejb.ForumsBM;
import com.stratelia.webactiv.forums.forumsManager.ejb.ForumsBMHome;
import com.stratelia.webactiv.forums.models.Forum;
import com.stratelia.webactiv.forums.models.ForumPK;
import com.stratelia.webactiv.forums.models.Message;
import com.stratelia.webactiv.forums.models.MessagePK;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

public class ForumTagUtil
    extends ComponentTagUtil {

  private ForumsBM forumsBm = null;
  private String componentId = null;
  private String elementId = null;

  public ForumTagUtil(String componentId, String elementId, String userId) {
    super(componentId, userId, (componentId != null));
    this.componentId = componentId;
    this.elementId = elementId;
  }

  public Collection getForums() throws RemoteException {
    return getForumsBm().getForums(getForumPK(0));
  }

  public Forum getForumElement() throws RemoteException {
    return getForumsBm().getForum(getForumPK());
  }

  public Message getMessageElement() throws RemoteException {
    return getForumsBm().getMessage(getMessagePK());
  }

  public Collection getLastThreads(String forumIdAndCount) throws RemoteException {
    StringTokenizer st = new StringTokenizer(forumIdAndCount, ",");
    int forumId = Integer.parseInt(st.nextToken());
    int count = Integer.parseInt(st.nextToken());
    return getForumsBm().getLastThreads(getForumPK(forumId), count);
  }

  public Collection getNotAnsweredLastThreads(String forumIdAndCount) throws RemoteException {
    StringTokenizer st = new StringTokenizer(forumIdAndCount, ",");
    int forumId = Integer.parseInt(st.nextToken());
    int count = Integer.parseInt(st.nextToken());
    return getForumsBm().getNotAnsweredLastThreads(getForumPK(forumId), count);
  }

  private ForumsBM getForumsBm() {
    if (forumsBm == null) {
      try {
        forumsBm = ((ForumsBMHome) EJBUtilitaire.getEJBObjectRef(
            JNDINames.FORUMSBM_EJBHOME, ForumsBMHome.class)).create();
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
