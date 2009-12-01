package com.silverpeas.tags.comment;

import java.rmi.RemoteException;

import com.silverpeas.tags.ComponentTagUtil;
import com.silverpeas.tags.util.EJBDynaProxy;
import com.silverpeas.tags.util.VisibilityException;
import com.stratelia.silverpeas.comment.ejb.CommentBm;
import com.stratelia.webactiv.kmelia.model.KmeliaRuntimeException;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;
import com.stratelia.webactiv.util.publication.model.PublicationPK;

public class CommentTagUtil extends ComponentTagUtil{
	
	private String componentId;
	private String elementId;
	private CommentBm commentBm;

	public CommentTagUtil(String componentId, String elementId, String userId) {
		super(componentId, userId);
		this.componentId	= componentId;
		this.elementId= elementId;
	}
	
	public String getComponentId() {
		return componentId;
	}
	
	public String getElementId() {
		return elementId;
	}

	private CommentBm getCommentBm() 
	{
		if (commentBm == null) {
			try
			{
				commentBm = (CommentBm)EJBDynaProxy.createProxy(JNDINames.COMMENT_EJBHOME, CommentBm.class);
			}
			catch (Exception e)
			{
				throw new KmeliaRuntimeException("KmeliaTagUtil.getCommentBm", SilverpeasRuntimeException.ERROR,"root.EX_CANT_GET_REMOTE_OBJECT",e);
			}
		}
		return commentBm;
    }
	
	public Integer getPublicationCommentsCount()throws RemoteException, VisibilityException
    {
    	Integer commentsCount = new Integer(0);
    	PublicationPK publicationKey = new PublicationPK(this.getElementId(),this.getComponentId());
    	int count = getCommentBm().getCommentsCount(publicationKey);
    	if(count>0){
    		commentsCount = new Integer(count);
    	}
    	return commentsCount;
    }

}
