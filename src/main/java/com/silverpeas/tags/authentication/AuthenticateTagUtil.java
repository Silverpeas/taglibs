package com.silverpeas.tags.authentication;

import java.io.Serializable;

import com.silverpeas.admin.ejb.AdminBm;
import com.silverpeas.admin.ejb.AdminBmRuntimeException;
import com.silverpeas.authentication.ejb.AuthenticationBm;
import com.silverpeas.tags.util.EJBDynaProxy;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

public class AuthenticateTagUtil implements Serializable {

	private AdminBm 			adminBm 		= null;
	private AuthenticationBm 	authentication 	= null;
	
	public AuthenticateTagUtil()
	{
	}
	
	public String authenticate(String login, String password, String domainId, String sessionId)
	{
		try
		{
			//Authenticate the user (through Silverpeas or LDAP or...)
			String key = getAuthenticationBm().authenticate(login, password, domainId);
			
			String userId = getAdminBm().authenticate(key, sessionId);
			
			return userId;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	private AdminBm getAdminBm() 
	{
		if (adminBm == null) {
			try
			{
				adminBm = (AdminBm)EJBDynaProxy.createProxy(JNDINames.ADMINBM_EJBHOME, AdminBm.class);
			}
			catch (Exception e)
			{
				throw new AdminBmRuntimeException("AuthenticateTag.getAdminBm", SilverpeasRuntimeException.ERROR,"root.EX_CANT_GET_REMOTE_OBJECT",e);
			}
		}
		return adminBm;
    }
	
	private AuthenticationBm getAuthenticationBm()
	{
		if (authentication == null) {
			try
			{
				authentication = (AuthenticationBm)EJBDynaProxy.createProxy(JNDINames.AUTHENTICATIONBM_EJBHOME, AuthenticationBm.class);
			}
			catch (Exception e)
			{
				throw new AdminBmRuntimeException("AuthenticateTag.getAuthenticationBm", SilverpeasRuntimeException.ERROR,"root.EX_CANT_GET_REMOTE_OBJECT",e);
			}
		}
		return authentication;
	}
}
