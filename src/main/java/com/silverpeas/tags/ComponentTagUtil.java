/*
 * Created on 7 juin 2005
 *
 */
package com.silverpeas.tags;

import com.silverpeas.admin.ejb.AdminBmRuntimeException;
import com.silverpeas.tags.util.Admin;
import com.silverpeas.tags.util.AuthorizationException;
import com.silverpeas.tags.util.SiteTagUtil;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;

/**
 * @author neysseri
 *
 */
public class ComponentTagUtil {
	
	private Admin admin	= null;
	private String userId = null;

	public ComponentTagUtil(String componentId, String userId)
	{
		setUserId(userId);
		init(componentId, true);
	}

	public ComponentTagUtil(String componentId, String userId, boolean check)
	{
		setUserId(userId);
		init(componentId, check);
	}

	private void init(String componentId, boolean check)
	{
		if (check)
			checkAuthorization(componentId);
	}
	
	private void checkAuthorization(String componentId)
	{
		//Check webUser rights
		if (!isUserAllowed(componentId))
  		{
			String errorMessage = "Warning ! User identified by id '"+getUserId()+"' is not allowed to access to component identified by componentId '"+componentId+"'";
			throw new AuthorizationException(errorMessage);
		}
	}
	
	private boolean isUserAllowed(String componentId)
	{
		try
		{
			return getAdmin().isUserAllowed(getUserId(), componentId);
		}
		catch (Exception e)
		{
			throw new AdminBmRuntimeException("ComponentTagUtil.isUserAllowed()", SilverpeasRuntimeException.ERROR, "root.EX_CANT_GET_REMOTE_OBJECT", e);
		}
	}

	public Admin getAdmin() 
	{
		if (admin == null) {
			admin = new Admin();
		}
		return admin;
	}

	public String getUserId() {
		if (userId == null)
			return SiteTagUtil.getUserId();
		else
			return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}