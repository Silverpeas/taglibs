package com.silverpeas.tags.util;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.stratelia.silverpeas.silvertrace.SilverTrace;

public class SiteTag extends TagSupport {

	int		mode					= 0;
	String	userId					= "undefined";
	String	serverName				= "undefined";
	String	serverPort				= null;
	String	context					= null;
	String	fileServerName			= "undefined";
	String 	userAuthenticationClass	= "com.silverpeas.tags.authentication.BasicAuthentication";
	String	language 				= null;
	String	httpMode				= null;
	
	public void setMode(int mode)
	{
		this.mode = mode;
	}

	public void setUserId(String userId)
	{
		SilverTrace.info("peasUtil", "SiteTag.setUserId", "root.MSG_GEN_ENTER_METHOD", "userId = "+userId);
		this.userId = userId;
	}

	public void setServer(String sName)
	{
		this.serverName = sName;
	}

	public void setPort(String sPort)
	{
		this.serverPort = sPort;
	}

	public void setContext(String context)
	{
		this.context = context;
	}

	public void setFileServer(String fileServerName)
	{
		this.fileServerName = fileServerName;
	}
	
	public void setUserAuthenticationClass(String userAuthenticationClass) {
		this.userAuthenticationClass = userAuthenticationClass;
	}

	/**
	 * Set language for the site (Ex: en, fr)
	 * @param language
	 */
	public void setLanguage(String language)
	{
		this.language = language;
	}

	/**
	 * Set httpMode for the site (Ex: http:// our https://)
	 * @param mode
	 */
	public void setHttpMode(String httpMode)
	{
		this.httpMode = httpMode;
	}

	public int doStartTag() throws JspTagException
	{
		SiteTagUtil.setMode(this.mode);
		SiteTagUtil.setUserId(this.userId);
		SiteTagUtil.setServerName(this.serverName);
		SiteTagUtil.setServerPort(this.serverPort);
		SiteTagUtil.setServerContext(this.context);
		SiteTagUtil.setFileServerName(this.fileServerName);
		SiteTagUtil.setUserAuthenticationClass(userAuthenticationClass);
		SiteTagUtil.setLanguage(this.language);
		SiteTagUtil.setHttpMode(this.httpMode);
		return EVAL_PAGE;
	}

	protected int translateScope(String scope)
    {
        return PageContext.SESSION_SCOPE;
    }
}