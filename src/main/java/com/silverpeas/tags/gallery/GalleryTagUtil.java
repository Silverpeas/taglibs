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

public class GalleryTagUtil extends ComponentTagUtil {

  private String spaceId;
  private String componentId;
  private String elementId;
  private GalleryBm galleryBm;

  public GalleryTagUtil(String spaceId, String componentId, String elementId, String userId) {
    super(componentId, userId);
    this.setSpaceId(spaceId);
    this.setComponentId(componentId);
    this.setElementId(elementId);
  }
  
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

  public PhotoDetail getPhotoDetail(String photoId) throws RemoteException {
    PhotoPK photoPk = new PhotoPK(photoId, this.getSpaceId(), this.getComponentId());
    return getGalleryBm().getPhoto(photoPk);
  }

  public AlbumDetail getAlbumsDetail(String albumId) throws RemoteException {
    return getGalleryBm().getAlbum(new NodePK(albumId, this.getSpaceId(), this.getComponentId()), true);
  }
  
  public Collection<PhotoDetail> getAllPhotos() throws RemoteException{
    return getGalleryBm().getAllPhotos(this.getComponentId());
  }
  
  public Collection<PhotoDetail> getPhotosByAlbum(String albumIdAndNameAndSort) throws RemoteException{
    ArrayList<String> listParameters = new ArrayList<String>();
    listParameters.add("albumId");
    listParameters.add("fieldName");
    listParameters.add("sortType");
    HashMap<String, String> parsedParameters = parseSort(listParameters, albumIdAndNameAndSort);
    Collection<PhotoDetail> photos = new ArrayList<PhotoDetail>();
    if(parsedParameters.get("albumId") != null){
      String albumId = parsedParameters.get("albumId");
      if(parsedParameters.get("sortType") != null && parsedParameters.get("fieldName") != null){
        photos = getGalleryBm().getAlbumByIdAndField(new NodePK(albumId, this.getSpaceId(), this.getComponentId()), parsedParameters, true).getPhotos();
      }
      else{
        photos = getGalleryBm().getAlbum(new NodePK(albumId, this.getSpaceId(), this.getComponentId()),true).getPhotos();
      }
    }
    return photos;
  }
  
  public Collection<AlbumDetail> getAlbumPath(String albumId) throws RemoteException{
    AlbumDetail currentAlbum = getGalleryBm().getAlbum(new NodePK(albumId, this.getSpaceId(), this.getComponentId()), false);
    Collection<AlbumDetail> albums = new ArrayList<AlbumDetail>();
    ArrayList<Integer> fatherPathIds = parsePath(currentAlbum.getPath());
    AlbumDetail fatherAlbum = null;
    for (Iterator iterator = fatherPathIds.iterator(); iterator.hasNext();) {
      Integer fatherId = (Integer) iterator.next();
      fatherAlbum = getGalleryBm().getAlbum(new NodePK(albumId, this.getSpaceId(), this.getComponentId()), false);
      albums.add(fatherAlbum);
    }
    albums.add(currentAlbum);
    return albums;
  }
  
  public Collection<AlbumDetail> getPhotoPath(String photoId) throws RemoteException{
    ArrayList<String> pathList = (ArrayList<String>) getGalleryBm().getPathList(this.getComponentId(), photoId);
    return getAlbumPath(pathList.get(0));
  }
  
  private HashMap<String, String> parseSort(ArrayList<String> listParameters, String parsingRequested){
    StringTokenizer tokenizer = new StringTokenizer(parsingRequested, ",");
    int parameterIndex = 0;
    String parameterizedField = "";
    String parameterizedValue = "";
    HashMap<String, String> parsedParameters = new HashMap<String, String>();
    while (tokenizer.hasMoreTokens()) {
      parameterizedValue = tokenizer.nextToken();
      if("".equals(parameterizedValue)){
        parameterizedValue=null;
      }
      parameterizedField = listParameters.get(parameterIndex);
      parsedParameters.put(parameterizedField, parameterizedValue);
      parameterIndex++;
    }
    return parsedParameters;
  }
  
  private ArrayList<Integer> parsePath(String path){
    ArrayList<Integer> fatherIds = new ArrayList<Integer>();
    StringTokenizer tokenizer = new StringTokenizer(path, "/");
    while (tokenizer.hasMoreTokens()) {
      fatherIds.add(new Integer(tokenizer.nextToken()));
    }
    return fatherIds;
  }

}
