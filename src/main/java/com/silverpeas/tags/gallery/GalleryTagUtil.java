/**
 * Copyright (C) 2000 - 2015 Silverpeas
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

package com.silverpeas.tags.gallery;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.silverpeas.gallery.control.ejb.GalleryBm;
import com.silverpeas.gallery.control.ejb.MediaServiceFactory;
import com.silverpeas.gallery.model.AlbumDetail;
import com.silverpeas.gallery.model.Media;
import com.silverpeas.gallery.model.MediaCriteria.VISIBILITY;
import com.silverpeas.gallery.model.MediaPK;
import com.silverpeas.gallery.model.Photo;
import com.silverpeas.tags.ComponentTagUtil;
import com.stratelia.webactiv.kmelia.model.KmeliaRuntimeException;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.node.model.NodePK;

/**
 * This class provided tag library to access a Silverpeas Gallery Component instance
 * @author Ludovic Bertin
 */
public class GalleryTagUtil extends ComponentTagUtil {

  private String spaceId;
  private String componentId;
  private String elementId;
  private GalleryBm galleryBm;

  /**
   * Constructor.
   * @param spaceId space id where component has been instanciated
   * @param componentId instance id
   * @param elementId element id
   * @param userId user id which access to the instance
   */
  public GalleryTagUtil(String spaceId, String componentId, String elementId, String userId) {
    super(componentId, userId);
    this.setSpaceId(spaceId);
    this.setComponentId(componentId);
    this.setElementId(elementId);
  }

  /**
   * Constructor.
   * @param componentId instance id
   * @param elementId element id
   * @param userId user id which access to the instance
   */
  public GalleryTagUtil(String componentId, String elementId, String userId) {
    super(componentId, userId);
    this.setComponentId(componentId);
    this.setElementId(elementId);
  }

  public String getSpaceId() {
    return spaceId;
  }

  public void setSpaceId(String spaceId) {
    this.spaceId = spaceId;
  }

  public String getComponentId() {
    return componentId;
  }

  public void setComponentId(String componentId) {
    this.componentId = componentId;
  }

  public String getElementId() {
    return elementId;
  }

  public void setElementId(String elementId) {
    this.elementId = elementId;
  }

  private GalleryBm getGalleryBm() {
    if (galleryBm == null) {
      try {    	  
    	galleryBm = MediaServiceFactory.getMediaService();
        //galleryBm = EJBUtilitaire.getEJBObjectRef(JNDINames.GALLERYBM_EJBHOME, GalleryBm.class);
      } catch (Exception e) {
        throw new KmeliaRuntimeException("KmeliaTagUtil.getGalleryBm",
            SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
      }
    }
    return galleryBm;
  }

  /**
   * Get the detail of photo with given id.
   * @param photoId the photo id
   * @return a Photo object
   * @throws RemoteException
   */
  public Photo getPhotoDetail(String photoId) throws RemoteException {
	MediaPK photoPk = new MediaPK(photoId, this.getSpaceId(), this.getComponentId());
    return getGalleryBm().getPhoto(photoPk);
  }

  /**
   * Get the detail of photo with given id.
   * @param photoId the photo id
   * @return a PhotoDetail object
   * @throws RemoteException
   */
  public AlbumDetail getAlbumsDetail(String albumId) throws RemoteException {
    return getGalleryBm().getAlbum( new NodePK(albumId, this.getSpaceId(), this.getComponentId()),
        VISIBILITY.FORCE_GET_ALL);
  }

  /**
   * Get all photos available in current component instance
   * @return A Collection of PhotoDetail objects
   * @throws RemoteException
   */
  public Collection<Photo> getAllPhotos() throws RemoteException {
    Collection<Media> medias = getGalleryBm().getAllMedia(this.getComponentId());
    Collection<Photo> photos = new ArrayList<Photo>();
    for (Media media : medias) {
		if (media.getType().isPhoto()) {
			photos.add(media.getPhoto());
		}
	}
    return photos;    		
  }

  /**
   * Get all photos available in album with given album id
   * @param albumIdAndNameAndSort album id and sort options (field and order. Ex.: "12,name,ASC" )
   * @return A Collection of PhotoDetail objects
   * @throws RemoteException
   */
  public Collection<Photo> getPhotosByAlbum(String albumIdAndNameAndSort)
      throws RemoteException {
    ArrayList<String> listParameters = new ArrayList<String>();
    listParameters.add("albumId");
    listParameters.add("fieldName");
    listParameters.add("sortType");
    HashMap<String, String> parsedParameters = parseSort(listParameters, albumIdAndNameAndSort);
    Collection<Photo> photos = new ArrayList<Photo>();
    if (parsedParameters.get("albumId") != null) {
      String albumId = parsedParameters.get("albumId");
      NodePK nodePK = new NodePK(albumId, this.getSpaceId(), this.getComponentId());
      if (parsedParameters.get("sortType") != null && parsedParameters.get("fieldName") != null) {
        //photos = getGalleryBm().getAllPhotosSorted(nodePK, parsedParameters, true);
    	photos = getGalleryBm().getAllPhotos(nodePK, VISIBILITY.FORCE_GET_ALL);
      } else {
        photos = getGalleryBm().getAllPhotos(nodePK, VISIBILITY.FORCE_GET_ALL);
      }
    }
    return photos;
  }

  /**
   * Get path of album with given album id
   * @param albumId album id
   * @return A Collection of AlbumDetail objects
   * @throws RemoteException
   */
  public Collection<AlbumDetail> getAlbumPath(String albumId) throws RemoteException {
    AlbumDetail currentAlbum =
        getGalleryBm().getAlbum(new NodePK(albumId, this.getSpaceId(), this.getComponentId()),
        VISIBILITY.VISIBLE_ONLY);
    Collection<AlbumDetail> albums = new ArrayList<AlbumDetail>();
    ArrayList<Integer> fatherPathIds = parsePath(currentAlbum.getPath());
    AlbumDetail fatherAlbum = null;
    for (Iterator iterator = fatherPathIds.iterator(); iterator.hasNext();) {
      Integer fatherId = (Integer) iterator.next();
      fatherAlbum =
          getGalleryBm()
          .getAlbum(
          new NodePK(String.valueOf(fatherId), this.getSpaceId(), this.getComponentId()),
          VISIBILITY.VISIBLE_ONLY);
      albums.add(fatherAlbum);
    }
    albums.add(currentAlbum);
    return albums;
  }

  /**
   * Get path of photo with given photo id
   * @param photoId photo id
   * @return A Collection of AlbumDetail objects
   * @throws RemoteException
   */
  public Collection<AlbumDetail> getPhotoPath(String photoId) throws RemoteException {
	ArrayList<NodeDetail> pathList = new ArrayList<NodeDetail>(getGalleryBm().getPath(new NodePK(photoId, this.getComponentId())));
    return getAlbumPath(String.valueOf(pathList.get(0).getId()));
  }

  private HashMap<String, String> parseSort(ArrayList<String> listParameters,
      String parsingRequested) {
    StringTokenizer tokenizer = new StringTokenizer(parsingRequested, ",");
    int parameterIndex = 0;
    String parameterizedField = "";
    String parameterizedValue = "";
    HashMap<String, String> parsedParameters = new HashMap<String, String>();
    while (tokenizer.hasMoreTokens()) {
      parameterizedValue = tokenizer.nextToken();
      if ("".equals(parameterizedValue)) {
        parameterizedValue = null;
      }
      parameterizedField = listParameters.get(parameterIndex);
      parsedParameters.put(parameterizedField, parameterizedValue);
      parameterIndex++;
    }
    return parsedParameters;
  }

  private ArrayList<Integer> parsePath(String path) {
    ArrayList<Integer> fatherIds = new ArrayList<Integer>();
    StringTokenizer tokenizer = new StringTokenizer(path, "/");
    while (tokenizer.hasMoreTokens()) {
      fatherIds.add(new Integer(tokenizer.nextToken()));
    }
    return fatherIds;
  }

}