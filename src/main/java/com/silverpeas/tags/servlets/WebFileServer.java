package com.silverpeas.tags.servlets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.rmi.RemoteException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.silverpeas.gallery.control.ejb.GalleryBm;
import com.silverpeas.gallery.model.GalleryRuntimeException;
import com.silverpeas.gallery.model.PhotoDetail;
import com.silverpeas.gallery.model.PhotoPK;
import com.silverpeas.tags.authentication.AuthenticationManager;
import com.silverpeas.tags.util.Admin;
import com.silverpeas.tags.util.EJBDynaProxy;
import com.silverpeas.util.StringUtil;
import com.silverpeas.util.web.servlet.RestRequest;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.util.FileRepositoryManager;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.ResourceLocator;
import com.stratelia.webactiv.util.attachment.control.AttachmentController;
import com.stratelia.webactiv.util.attachment.ejb.AttachmentPK;
import com.stratelia.webactiv.util.attachment.model.AttachmentDetail;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

public class WebFileServer extends HttpServlet
{
    PrintWriter out;

	private Admin	admin	= null;

    public void init(ServletConfig config)
    {
        try
        {
            super.init(config);
        }
        catch (ServletException se)
        {
            SilverTrace.fatal("peasUtil", "WebFileServer.init", "peasUtil.CANNOT_ACCESS_SUPERCLASS");
        }
    }
    
    public void service(HttpServletRequest req, HttpServletResponse res)
	    throws ServletException, IOException {
    	SilverTrace.info("peasUtil", "OnlineFileServer.doPost", "root.MSG_GEN_ENTER_METHOD");
    	RestRequest restRequest = new RestRequest(req, "");
	  
    	String userId = AuthenticationManager.getUserId(req);
    	String componentId = restRequest.getElementValue("componentId");
    	if (!StringUtil.isDefined(componentId))
    		componentId = restRequest.getElementValue("ComponentId");	//forward compatibility
    	if (getAdmin().isUserAllowed(userId,componentId))
    	{
    		OnlineFile file = getWantedFile(restRequest);
    		if (file != null) {
    			display(res, file);
    			return;
    		}
    		displayWarningHtmlCode(res);
    	}
    	else
    	{
    		displayError(res, userId, componentId);
    	}
	}

    public OnlineFile getFileFromOldURL(RestRequest restRequest) throws RemoteException
    {
        SilverTrace.info("peasUtil", "WebFileServer.doPost", "root.MSG_GEN_ENTER_METHOD");
        OnlineFile file = null;
        
        String mimeType		= restRequest.getElementValue("MimeType");
        String sourceFile	= restRequest.getElementValue("SourceFile");
        String directory	= restRequest.getElementValue("Directory");
        String componentId	= restRequest.getElementValue("ComponentId");
        String imageId		= restRequest.getElementValue("ImageId");

    	String attachmentId = restRequest.getWebRequest().getParameter("attachmentId");
        AttachmentDetail attachment = null;
        if(StringUtil.isDefined(attachmentId))
        {
            attachment = AttachmentController.searchAttachmentByPK(new AttachmentPK(attachmentId));
            if(attachment != null)
            {
                mimeType = attachment.getType();
                sourceFile = attachment.getPhysicalName();
                directory = FileRepositoryManager.getRelativePath(FileRepositoryManager.getAttachmentContext(attachment.getContext()));
                file = new OnlineFile(mimeType, sourceFile, directory);
                file.setComponentId(componentId);
            }
        } 
        else if (StringUtil.isDefined(imageId))
    	{
    		PhotoDetail image = getGalleryBm().getPhoto(new PhotoPK(imageId, componentId));
    		mimeType = "image/jpg";
        boolean useOriginal = new Boolean(restRequest.getElementValue("UseOriginal"));
        if (useOriginal)
          sourceFile = image.getImageName();
        else
          sourceFile = image.getId()+"_preview.jpg";
    		directory = "image"+image.getId();
    		file = new OnlineFile(mimeType, sourceFile, directory);
            file.setComponentId(componentId);
    	}
    	else
    	{
    		file = new OnlineFile(mimeType, sourceFile, directory);
    		file.setComponentId(componentId);
    	}
        return file;
    }

    protected OnlineFile getWantedFile(RestRequest restRequest)
	    throws RemoteException {
	  OnlineFile file = getWantedAttachment(restRequest);
	  if(file == null) {
		  file = getFileFromOldURL(restRequest);
	  }
	  /*if(file == null) {
	    file = getWantedVersionnedDocument(restRequest);
	  }*/
	  return file;
	}
	
	protected OnlineFile getWantedAttachment(RestRequest restRequest) {
	  String componentId = restRequest.getElementValue("componentId");
	  if (!StringUtil.isDefined(componentId))
		  componentId = restRequest.getElementValue("ComponentId");
	  OnlineFile file = null;
	  String attachmentId = restRequest.getElementValue("attachmentId");
	  String language = restRequest.getElementValue("lang");
	  if (StringUtil.isDefined(attachmentId)) {
	    AttachmentDetail attachment = AttachmentController
	        .searchAttachmentByPK(new AttachmentPK(attachmentId));
	    if (attachment != null) {
	      file = new OnlineFile(attachment.getType(language), attachment
	          .getPhysicalName(language), FileRepositoryManager
	          .getRelativePath(FileRepositoryManager
	              .getAttachmentContext(attachment.getContext())));
	      file.setComponentId(componentId);
	    }
	  }
	  return file;
	}
	
	/*protected OnlineFile getWantedVersionnedDocument(RestRequest restRequest)
	    throws RemoteException {
	  String componentId = restRequest.getElementValue("componentId");
	  OnlineFile file = null;
	  String documentId = restRequest.getElementValue("documentId");
	  if (StringUtil.isDefined(documentId)) {
	    String versionId = restRequest.getElementValue("versionId");
	    VersioningUtil versioning = new VersioningUtil();
	    DocumentVersionPK versionPK = new DocumentVersionPK(Integer
	        .parseInt(versionId), "useless", componentId);
	    DocumentVersion version = versioning.getDocumentVersion(versionPK);
	    if (version != null) {
	      String[] path = new String[1];
	      path[0] = "Versioning";
	      file = new OnlineFile(version.getMimeType(), version.getPhysicalName(),
	          FileRepositoryManager.getRelativePath(path));
	      file.setComponentId(componentId);
	    }
	  }
	  return file;
	}*/
	
	/**
	 * This method writes the result of the preview action.
	 * 
	 * @param res
	 *          - The HttpServletResponse where the html code is write
	 * @param htmlFilePath
	 *          - the canonical path of the html document generated by the parser
	 *          tools. if this String is null that an exception had been catched
	 *          the html document generated is empty !! also, we display a warning
	 *          html page
	 */
	private void display(HttpServletResponse res, OnlineFile file)
	    throws IOException {
	  String filePath = FileRepositoryManager.getAbsolutePath(file
	      .getComponentId())
	      + file.getDirectory() + File.separator + file.getSourceFile();
	
	  File realFile = new File(filePath);
	  if (!realFile.exists() && !realFile.isFile()) {
	    displayWarningHtmlCode(res);
	    return;
	  }
	  OutputStream out2 = res.getOutputStream();
	  BufferedInputStream input = null; // for the html document generated
	  SilverTrace.info("peasUtil", "OnlineFileServer.display()",
	      "root.MSG_GEN_ENTER_METHOD", " htmlFilePath " + filePath);
	  try {
	    res.setContentType(file.getMimeType());
	    input = new BufferedInputStream(new FileInputStream(realFile));
	    byte[] buffer = new byte[8];
	    int read = 0;
	    SilverTrace.info("peasUtil", "OnlineFileServer.display()",
	        "root.MSG_GEN_ENTER_METHOD", " BufferedInputStream read " + read);
	    while ((read = input.read(buffer)) != -1) {
	      out2.write(buffer, 0, read);
	    }
	  } catch (Exception e) {
	    SilverTrace.warn("peasUtil", "OnlineFileServer.doPost",
	        "root.EX_CANT_READ_FILE", "file name=" + filePath);
	    displayWarningHtmlCode(res);
	  } finally {
	    SilverTrace.info("peasUtil", "OnlineFileServer.display()", "",
	        " finally ");
	    // we must close the in and out streams
	    try {
	      if (input != null) {
	        input.close();
	      }
	      out2.close();
	    } catch (Exception e) {
	      SilverTrace.warn("peasUtil", "OnlineFileServer.display",
	          "root.EX_CANT_READ_FILE", "close failed");
	    }
	  }
	}
	
	// Add By Mohammed Hguig
	
	private void displayWarningHtmlCode(HttpServletResponse res)
	    throws IOException {
	  StringReader sr = null;
	  OutputStream out2 = res.getOutputStream();
	  int read;
	  ResourceLocator resourceLocator = new ResourceLocator(
	      "com.stratelia.webactiv.util.peasUtil.multiLang.fileServerBundle", "");
	
	  sr = new StringReader(resourceLocator.getString("warning"));
	  try {
	    read = sr.read();
	    while (read != -1) {
	      SilverTrace.info("peasUtil", "OnlineFileServer.displayHtmlCode()",
	          "root.MSG_GEN_ENTER_METHOD", " StringReader read " + read);
	      out2.write(read); // writes bytes into the response
	      read = sr.read();
	    }
	  } catch (Exception e) {
	    SilverTrace.warn("peasUtil", "OnlineFileServer.displayWarningHtmlCode",
	        "root.EX_CANT_READ_FILE", "warning properties");
	  } finally {
	    try {
	      if (sr != null)
	        sr.close();
	      out2.close();
	    } catch (Exception e) {
	      SilverTrace.warn("peasUtil", "OnlineFileServer.displayHtmlCode",
	          "root.EX_CANT_READ_FILE", "close failed");
	    }
	  }
	}
    
	private void displayError(HttpServletResponse res, String userId, String componentId) throws ServletException, IOException
	{
		SilverTrace.info("peasUtil", "WebFileServer.displayError()", "root.MSG_GEN_ENTER_METHOD");
		
		res.setContentType("text/html");
		OutputStream        out2 = res.getOutputStream();
		int                 read;

		StringBuffer message = new StringBuffer(255);
		message.append("<HTML>");
		message.append("<BODY>");
		message.append("Warning ! User identified by id '<b>").append(userId).append("</b>'");
		message.append(" is not allowed to access to component identified by componentId '<b>");
		message.append(componentId).append("</b>' !");
		message.append("</BODY>");
		message.append("</HTML>");
		
		StringReader reader = new StringReader(message.toString());
		
		try
		{
			read = reader.read();
			while (read != -1){
				out2.write(read); // writes bytes into the response
				read = reader.read();
			}
		}
		catch (Exception e)
		{
			SilverTrace.warn("peasUtil", "WebFileServer.displayError", "root.EX_CANT_READ_FILE");
		}
		finally
		{
			// we must close the in and out streams
			try
			{
				out2.close();
			}
			catch (Exception e)
			{
				SilverTrace.warn("peasUtil", "WebFileServer.displayError", "root.EX_CANT_READ_FILE", "close failed");
			}
		}
	}
	
	private Admin getAdmin() 
	{
		if (admin == null) {
			admin = new Admin();
		}
		return admin;
    }
	
	private GalleryBm getGalleryBm()
	{
		try 
		{
			return (GalleryBm)EJBDynaProxy.createProxy(JNDINames.GALLERYBM_EJBHOME, GalleryBm.class);
		}
		catch (Exception e)
		{
			throw new GalleryRuntimeException("WebFileServer.getGalleryBm", SilverpeasRuntimeException.ERROR,"root.EX_CANT_GET_REMOTE_OBJECT",e);
		}
	}
}