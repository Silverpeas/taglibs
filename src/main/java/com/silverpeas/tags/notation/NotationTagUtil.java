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
package com.silverpeas.tags.notation;

import java.util.Collection;

import com.silverpeas.notation.ejb.NotationBm;
import com.silverpeas.notation.ejb.NotationRuntimeException;
import com.silverpeas.notation.model.Notation;
import com.silverpeas.notation.model.NotationDetail;
import com.silverpeas.notation.model.NotationPK;
import com.silverpeas.tags.ComponentTagUtil;

import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

public class NotationTagUtil extends ComponentTagUtil {

  private NotationBm notationBm = null;
  private String componentId = null;
  private String elementId = null;
  private String authorId = null;

  public NotationTagUtil(String componentId, String elementId, String userId, String authorId) {
    super(componentId, userId, false);
    this.componentId = componentId;
    this.elementId = elementId;
    this.authorId = authorId;
  }

  public NotationDetail getPublicationNotation() {
    return getNotationBm().getNotation(getPublicationNotationPK());
  }

  public NotationDetail getPublicationUpdatedNotation(String note) {
    NotationPK pk = getPublicationNotationPK();
    getNotationBm().updateNotation(pk, Integer.parseInt(note));
    return getNotationBm().getNotation(pk);
  }

  public NotationDetail getForumNotation() {
    return getNotationBm().getNotation(getForumNotationPK());
  }

  public NotationDetail getForumUpdatedNotation(String note) {
    NotationPK pk = getForumNotationPK();
    getNotationBm().updateNotation(pk, Integer.parseInt(note));
    return getNotationBm().getNotation(pk);
  }

  public NotationDetail getMessageNotation() {
    return getNotationBm().getNotation(getMessageNotationPK());
  }

  public NotationDetail getMessageUpdatedNotation(String note) {
    NotationPK pk = getMessageNotationPK();
    getNotationBm().updateNotation(pk, Integer.parseInt(note));
    return getNotationBm().getNotation(pk);
  }

  public Collection<NotationDetail> getPublicationsBestNotations(String notationsCount) {
    return getNotationBm().getBestNotations(getPublicationNotationPK(), Integer.parseInt(
        notationsCount));
  }

  private NotationBm getNotationBm() {
    if (notationBm == null) {
      try {
        notationBm = EJBUtilitaire.getEJBObjectRef(JNDINames.NOTATIONBM_EJBHOME, NotationBm.class);
      } catch (Exception e) {
        throw new NotationRuntimeException("NotationTagUtil.getNotationBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return notationBm;
  }

  private NotationPK getPublicationNotationPK() {
    return new NotationPK(elementId, componentId, Notation.TYPE_PUBLICATION, authorId);
  }

  private NotationPK getForumNotationPK() {
    return new NotationPK(elementId, componentId, Notation.TYPE_FORUM, authorId);
  }

  private NotationPK getMessageNotationPK() {
    return new NotationPK(elementId, componentId, Notation.TYPE_MESSAGE, authorId);
  }
}