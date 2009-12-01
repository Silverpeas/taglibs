package com.silverpeas.tags.util;

import com.silverpeas.util.StringUtil;
import com.stratelia.silverpeas.silvertrace.SilverTrace;

public class SiteTagUtil implements java.io.Serializable
{	
	public static int MODE_PROD		= 0;
	public static int MODE_RECETTE	= 1;
	public static int MODE_DEV		= 2;
	
	private static int		currentMode				= MODE_PROD;
	private static String	currentUserId			= "undefined";
	private static String	serverName				= "undefined";
	private static String	serverPort				= null;
	private static String	serverContext			= null;
	private static String	fileServerName			= "undefined";
	private static String 	userAuthenticationClass = "com.silverpeas.tags.authentication.BasicAuthentication";

	private static String	serverInfo		= null;
	private static String	fileServerInfo	= null;

	private static String	language		= null;
	private static String	httpMode		= null;
	
    public SiteTagUtil()
    {
    }

	public static void setMode(int mode)
	{
		currentMode = mode;
	}

	public static int getMode()
	{
		return currentMode;
	}

	public static void setUserId(String userId)
	{
		SilverTrace.info("peasUtil", "SiteTagUtil.setUserId", "root.MSG_GEN_ENTER_METHOD", "userId = "+userId);
		currentUserId = userId;
	}

	public static String getUserId()
	{
		return currentUserId;
	}

	public static void setServerName(String sName)
	{
		serverName = sName;
	}

	public static String getServerName()
	{
		return serverName;
	}

	public static void setServerPort(String sPort)
	{
		serverPort = sPort;
	}

	public static String getServerPort()
	{
		return serverPort;
	}

	public static void setServerContext(String sContext)
	{
		serverContext = sContext;
	}

	public static String getServerContext()
	{
		return serverContext;
	}

	public static void setFileServerName(String sFileServerName)
	{
		fileServerName = sFileServerName;
	}

	public static String getFileServerName()
	{
		return fileServerName;
	}

	public static String getServerLocationAndContext()
	{
		if (serverInfo == null)
		{
			serverInfo = getHttpMode() + getServerName();
			if (!getServerPort().equals("80"))
				serverInfo = getHttpMode() + getServerName() + ":"+getServerPort();
			if (getServerContext() != null)
				serverInfo += "/"+getServerContext();
		}
		return serverInfo;
	}

	public static String getFileServerLocation()
	{
		if (fileServerInfo == null)
		{
			fileServerInfo = getServerLocationAndContext()+"/"+getFileServerName()+"/";
		}
		return fileServerInfo;
	}
	
	public static String getUserAuthenticationClass()
    {
        return userAuthenticationClass;
    }

    public static void setUserAuthenticationClass(String sUserAuthenticationClass)
    {
        userAuthenticationClass = sUserAuthenticationClass;
    }

	public static void setLanguage(String theLanguage)
	{
		language = theLanguage;
	}

	public static String getLanguage()
	{
		return language;
	}

	/**
	 * Get either http:// or https:// mode
	 * @return String
	 */
	public static String getHttpMode()
	{
		return httpMode;
	}
	public static void setHttpMode(String httpProtocol)
	{
		httpMode = "http://";
		if (StringUtil.isDefined(httpProtocol))
			httpMode = httpProtocol;
	}

	public static boolean isDevMode()
	{
		return (getMode() == MODE_DEV);
	}

	public static boolean isRecetteMode()
	{
		return (getMode() == MODE_RECETTE);
	}

	public static boolean isProdMode()
	{
		return (getMode() == MODE_PROD);
	}
}