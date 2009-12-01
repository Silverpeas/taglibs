/*
 * Created on 7 juin 2005
 *
 */
package com.silverpeas.tags.util;

import java.rmi.RemoteException;

import com.silverpeas.admin.ejb.AdminBm;
import com.silverpeas.admin.ejb.AdminBmRuntimeException;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.ComponentInst;
import com.stratelia.webactiv.beans.admin.SpaceInst;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

/**
 * @author neysseri
 *
 */
public class Admin {
	
	private AdminBm	adminBm	= null;
	
	public Admin()
	{
	}
	
	//check if the user is allowed to access the required component
	public boolean isUserAllowed(String userId, String componentId) throws RemoteException {
		 SilverTrace.info("peasUtil", "Admin.isUserAllowed()", "root.MSG_GEN_ENTER_METHOD", "userId = "+userId+", componentId = "+componentId);
		 boolean isAllowed = false;

		 if(componentId == null)
		 {   
			 isAllowed = false;
		 }
		 else
		 {
			 //ComponentInst 	componentInst 	= getAdminBm().getComponentInst(componentId);
			 //String 		spaceId 		= componentInst.getDomainFatherId();
			 isAllowed = getAdminBm().isComponentAvailable("useless", componentId, userId);
		 }

		 return isAllowed;
	}
	
	public ComponentInst getComponentInst(String componentId) throws RemoteException
	{
		return getAdminBm().getComponentInst(componentId);
	}
	
	public SpaceInst getSpaceInst(String spaceId) throws RemoteException
	{
		return getAdminBm().getSpaceInstById(spaceId);
	}
	
	private AdminBm getAdminBm() 
	{
		if (adminBm == null) {
			try
			{
				/*AdminBmHome adminBmHome = (AdminBmHome) EJBUtilitaire.getEJBObjectRef(JNDINames.ADMINBM_EJBHOME, AdminBmHome.class);
				adminBm = adminBmHome.create();*/
				
				adminBm = (AdminBm)EJBDynaProxy.createProxy(JNDINames.ADMINBM_EJBHOME, AdminBm.class);
			}
			catch (Exception e)
			{
				throw new AdminBmRuntimeException("Admin.getAdminBm", SilverpeasRuntimeException.ERROR,"root.EX_CANT_GET_REMOTE_OBJECT",e);
			}
		}
		return adminBm;
	}

}
