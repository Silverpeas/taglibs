package com.silverpeas.tags.almanach;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collection;

import com.silverpeas.tags.ComponentTagUtil;
import com.stratelia.webactiv.almanach.control.ejb.AlmanachBm;
import com.stratelia.webactiv.almanach.control.ejb.AlmanachBmHome;
import com.stratelia.webactiv.almanach.control.ejb.AlmanachRuntimeException;
import com.stratelia.webactiv.almanach.model.EventDetail;
import com.stratelia.webactiv.almanach.model.EventPK;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;


public class AlmanachTagUtil extends ComponentTagUtil {
	
	private AlmanachBm	almanachBm	    = null;
	private String		componentId 	= null;
	private String 		spaceId         = "useless";

	public AlmanachTagUtil(String componentId, String userId)
	{
		super(componentId, userId);
		
		this.componentId = componentId;
	}

	public Collection getMonthEvents() throws  RemoteException {
		return getAlmanachBm().getMonthEvents(new EventPK("", spaceId, componentId), Calendar.getInstance().getTime());
	}

	public Collection getAllEvents() throws  RemoteException {
		return getAlmanachBm().getAllEvents(new EventPK("", spaceId, componentId));
	}

	public EventDetail getEventDetail (String id) throws RemoteException {
		return getAlmanachBm().getEventDetail(new EventPK(id, spaceId, componentId));
	}	
	
	private AlmanachBm getAlmanachBm() 
	{
		if (almanachBm == null)
		{
			try
			{
				almanachBm = ((AlmanachBmHome) EJBUtilitaire.getEJBObjectRef(JNDINames.ALMANACHBM_EJBHOME, AlmanachBmHome.class)).create();
			}
			catch (Exception e)
			{
				throw new AlmanachRuntimeException("AlmanachTagUtil.getAlmanachBm", SilverpeasRuntimeException.ERROR,"root.EX_CANT_GET_REMOTE_OBJECT",e);
				
			}
		}
		return almanachBm;
	}
}