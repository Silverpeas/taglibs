package com.silverpeas.tags.gallery;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.silverpeas.gallery.control.ejb.GalleryBm;
import com.silverpeas.gallery.model.AlbumDetail;
import com.silverpeas.gallery.model.PhotoDetail;
import com.silverpeas.gallery.model.PhotoPK;
import com.silverpeas.tags.ComponentTagUtil;
import com.silverpeas.tags.util.EJBDynaProxy;
import com.stratelia.webactiv.kmelia.model.KmeliaRuntimeException;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;
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
        galleryBm =
            (GalleryBm) EJBDynaProxy.createProxy(JNDINames.GALLERYBM_EJBHOME, GalleryBm.class);
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
   * @return a PhotoDetail object
   * @throws RemoteException
   */
  public PhotoDetail getPhotoDetail(String photoId) throws RemoteException {
    PhotoPK photoPk = new PhotoPK(photoId, this.getSpaceId(), this.getComponentId());
    return getGalleryBm().getPhoto(photoPk);
  }

  /**
   * Get the detail of photo with given id.
   * @param photoId the photo id
   * @return a PhotoDetail object
   * @throws RemoteException
   */
  public AlbumDetail getAlbumsDetail(String albumId) throws RemoteException {
    return getGalleryBm().getAlbum(new NodePK(albumId, this.getSpaceId(), this.getComponentId()),
        true);
  }

  /**
   * Get all photos available in current component instance
   * @return A Collection of PhotoDetail objects
   * @throws RemoteException
   */
  public Collection<PhotoDetail> getAllPhotos() throws RemoteException {
    return getGalleryBm().getAllPhotos(this.getComponentId());
  }

  /**
   * Get all photos available in album with given album id
   * @param albumIdAndNameAndSort album id and sort options (field and order. Ex.: "12,name,ASC" )
   * @return A Collection of PhotoDetail objects
   * @throws RemoteException
   */
  public Collection<PhotoDetail> getPhotosByAlbum(String albumIdAndNameAndSort)
      throws RemoteException {
    ArrayList<String> listParameters = new ArrayList<String>();
    listParameters.add("albumId");
    listParameters.add("fieldName");
    listParameters.add("sortType");
    HashMap<String, String> parsedParameters = parseSort(listParameters, albumIdAndNameAndSort);
    Collection<PhotoDetail> photos = new ArrayList<PhotoDetail>();
    if (parsedParameters.get("albumId") != null) {
      String albumId = parsedParameters.get("albumId");
      NodePK nodePK = new NodePK(albumId, this.getSpaceId(), this.getComponentId());
      if (parsedParameters.get("sortType") != null && parsedParameters.get("fieldName") != null) {
        photos =
            getGalleryBm().getAllPhotosSorted(nodePK, parsedParameters, true);
      } else {
        photos =
            getGalleryBm().getAllPhoto(nodePK, true);
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
        false);
    Collection<AlbumDetail> albums = new ArrayList<AlbumDetail>();
    ArrayList<Integer> fatherPathIds = parsePath(currentAlbum.getPath());
    AlbumDetail fatherAlbum = null;
    for (Iterator iterator = fatherPathIds.iterator(); iterator.hasNext();) {
      Integer fatherId = (Integer) iterator.next();
      fatherAlbum =
          getGalleryBm()
          .getAlbum(
          new NodePK(String.valueOf(fatherId), this.getSpaceId(), this.getComponentId()),
          false);
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
    ArrayList<String> pathList =
        (ArrayList<String>) getGalleryBm().getPathList(this.getComponentId(), photoId);
    return getAlbumPath(pathList.get(0));
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