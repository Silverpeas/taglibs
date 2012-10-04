package com.silverpeas.tags.comment;

import java.rmi.RemoteException;

import com.silverpeas.tags.ComponentTagUtil;
import com.silverpeas.tags.util.VisibilityException;
import com.silverpeas.comment.service.CommentServiceFactory;
import com.silverpeas.comment.service.CommentService;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;
import com.stratelia.webactiv.util.publication.model.PublicationPK;

public class CommentTagUtil extends ComponentTagUtil {

  private String componentId;
  private String elementId;
  private CommentService commentService;

  public CommentTagUtil(String componentId, String elementId, String userId) {
    super(componentId, userId);
    this.componentId = componentId;
    this.elementId = elementId;
  }

  public String getComponentId() {
    return componentId;
  }

  public String getElementId() {
    return elementId;
  }

  private CommentService getCommentService() {
    if (commentService == null) {
      CommentServiceFactory serviceFactory = CommentServiceFactory.getFactory();
      commentService = serviceFactory.getCommentService();
    }
    return commentService;
  }

  public Integer getPublicationCommentsCount() throws RemoteException, VisibilityException {
    int commentsCount = 0;
    PublicationPK publicationKey = new PublicationPK(this.getElementId(), this.getComponentId());
    commentsCount = getCommentService().getCommentsCountOnPublication(PublicationDetail.
        getResourceType(), publicationKey);
    return commentsCount;
  }
}
