package com.silverpeas.tags.notation;

import java.rmi.RemoteException;
import java.util.Collection;

import com.silverpeas.notation.ejb.NotationBm;
import com.silverpeas.notation.ejb.NotationBmHome;
import com.silverpeas.notation.ejb.NotationRuntimeException;
import com.silverpeas.notation.model.Notation;
import com.silverpeas.notation.model.NotationDetail;
import com.silverpeas.notation.model.NotationPK;
import com.silverpeas.tags.ComponentTagUtil;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

public class NotationTagUtil
	extends ComponentTagUtil
{
	
	private NotationBm notationBm = null;
	
	private String componentId = null;
	private String elementId = null;
	private String authorId = null;
	
	public NotationTagUtil(String componentId, String elementId, String userId, String authorId)
	{
		super(componentId, userId, false);
		this.componentId = componentId;
		this.elementId = elementId;
		this.authorId = authorId;
	}
	
	public NotationDetail getPublicationNotation() throws RemoteException
	{
		return getNotationBm().getNotation(getPublicationNotationPK());
	}
	
	public NotationDetail getPublicationUpdatedNotation(String note) throws RemoteException
	{
		NotationPK pk = getPublicationNotationPK();
		getNotationBm().updateNotation(pk, Integer.parseInt(note));
		return getNotationBm().getNotation(pk);
	}
	
	public NotationDetail getForumNotation() throws RemoteException
	{
		return getNotationBm().getNotation(getForumNotationPK());
	}
	
	public NotationDetail getForumUpdatedNotation(String note) throws RemoteException
	{
		NotationPK pk = getForumNotationPK();
		getNotationBm().updateNotation(pk, Integer.parseInt(note));
		return getNotationBm().getNotation(pk);
	}
	
	public NotationDetail getMessageNotation() throws RemoteException
	{
		return getNotationBm().getNotation(getMessageNotationPK());
	}
	
	public NotationDetail getMessageUpdatedNotation(String note) throws RemoteException
	{
		NotationPK pk = getMessageNotationPK();
		getNotationBm().updateNotation(pk, Integer.parseInt(note));
		return getNotationBm().getNotation(pk);
	}
	
	public Collection getPublicationsBestNotations(String notationsCount) throws RemoteException
	{
		return getNotationBm().getBestNotations(
			getPublicationNotationPK(), Integer.parseInt(notationsCount));
	}
	
	private NotationBm getNotationBm() 
	{
		if (notationBm == null)
		{
			try
			{
				notationBm = ((NotationBmHome) EJBUtilitaire.getEJBObjectRef(
					JNDINames.NOTATIONBM_EJBHOME, NotationBmHome.class)).create();
			}
			catch (Exception e)
			{
				throw new NotationRuntimeException("NotationTagUtil.getNotationBm",
					SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
				
			}
		}
		return notationBm;
	}
	
	private NotationPK getPublicationNotationPK()
	{
		return new NotationPK(elementId, componentId, Notation.TYPE_PUBLICATION, authorId);
	}
	
	private NotationPK getForumNotationPK()
	{
		return new NotationPK(elementId, componentId, Notation.TYPE_FORUM, authorId);
	}
	
	private NotationPK getMessageNotationPK()
	{
		return new NotationPK(elementId, componentId, Notation.TYPE_MESSAGE, authorId);
	}
	
}