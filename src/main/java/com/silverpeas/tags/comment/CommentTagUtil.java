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

package com.silverpeas.tags.comment;

import com.silverpeas.comment.service.CommentService;
import com.silverpeas.comment.service.CommentServiceFactory;
import com.silverpeas.tags.ComponentTagUtil;
import com.silverpeas.tags.util.VisibilityException;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;
import com.stratelia.webactiv.util.publication.model.PublicationPK;
import java.rmi.RemoteException;

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
    int commentsCount;
    PublicationPK publicationKey = new PublicationPK(this.getElementId(), this.getComponentId());
    commentsCount = getCommentService().getCommentsCountOnPublication(PublicationDetail.
        getResourceType(), publicationKey);
    return commentsCount;
  }
}
