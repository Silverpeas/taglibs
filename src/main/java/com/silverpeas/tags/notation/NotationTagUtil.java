/**
 * Copyright (C) 2000 - 2015 Silverpeas
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.silverpeas.rating.ContributionRating;
import org.silverpeas.rating.RaterRatingPK;

import com.silverpeas.notation.ejb.RatingBm;
import com.silverpeas.notation.ejb.RatingRuntimeException;
import com.silverpeas.tags.ComponentTagUtil;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

public class NotationTagUtil extends ComponentTagUtil {

  private RatingBm notationBm = null;
  private String componentId = null;
  private String elementId = null;
  private String authorId = null;

  public NotationTagUtil(String componentId, String elementId, String userId, String authorId) {
    super(componentId, userId, false);
    this.componentId = componentId;
    this.elementId = elementId;
    this.authorId = authorId;
  }

  public ContributionRating getPublicationNotation() {    	  
	  return getNotationBm().getRating(getPublicationNotationPK());
  }

  public ContributionRating getPublicationUpdatedNotation(String note) {
	RaterRatingPK pk = getPublicationNotationPK();
    getNotationBm().updateRating(pk, Integer.parseInt(note));
    return getNotationBm().getRating(pk);
  }

  public ContributionRating getForumNotation() {
    return getNotationBm().getRating(getForumNotationPK());
  }

  public ContributionRating getForumUpdatedNotation(String note) {
	RaterRatingPK pk = getForumNotationPK();
    getNotationBm().updateRating(pk, Integer.parseInt(note));
    return getNotationBm().getRating(pk);
  }

  public ContributionRating getMessageNotation() {
    return getNotationBm().getRating(getMessageNotationPK());
  }

  public ContributionRating getMessageUpdatedNotation(String note) {
	RaterRatingPK pk = getMessageNotationPK();
    getNotationBm().updateRating(pk, Integer.parseInt(note));
    return getNotationBm().getRating(pk);
  }

  public Collection<Integer> getPublicationsBestNotations(String notationsCount) {
	  ContributionRating r = getNotationBm().getRating(getPublicationNotationPK());	  
	  Collection<Integer> bestRates = new ArrayList<Integer>();
	  List<Integer> ratings = new ArrayList<Integer>(r.getRaterRatings().values());	  
	  Collections.sort(ratings);
	  Collections.reverse(ratings);
	  int max= Integer.parseInt(notationsCount);
	  int i = 0;
	  for (Integer rate : ratings) {
		  if (i > max) return bestRates;
		  bestRates.add(rate);
		  i++;
	  }
	  return bestRates;
  }

  private RatingBm getNotationBm() {
    if (notationBm == null) {
      try {
        notationBm = EJBUtilitaire.getEJBObjectRef(JNDINames.RATINGBM_EJBHOME, RatingBm.class);
      } catch (Exception e) {
        throw new RatingRuntimeException("NotationTagUtil.getNotationBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return notationBm;
  }

  private RaterRatingPK getPublicationNotationPK() {	  
	  return new RaterRatingPK(elementId, componentId, "Publication", UserDetail.getById(authorId));    
  }

  private RaterRatingPK getForumNotationPK() {
    return new RaterRatingPK(elementId, componentId, "Forum", UserDetail.getById(authorId));
  }

  private RaterRatingPK getMessageNotationPK() {
    return new RaterRatingPK(elementId, componentId, "Message", UserDetail.getById(authorId));
  }
}