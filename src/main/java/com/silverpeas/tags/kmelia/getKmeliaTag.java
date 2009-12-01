package com.silverpeas.tags.kmelia;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.silverpeas.tags.authentication.AuthenticationManager;
import com.silverpeas.tags.util.AuthorizationException;

public class getKmeliaTag extends TagSupport {

	public static final String PAGE_ID			= "page";
    public static final String REQUEST_ID		= "request";
    public static final String SESSION_ID		= "session";
    public static final String APPLICATION_ID	= "application";

	private String		name;
	private String		componentId;
	private String		scope	= REQUEST_ID;
	private String		visibilityFilter	= null;
	private String 		forceReload = "false";

	public getKmeliaTag()
	{
		super();
	}

	public String getForceReload() {
		return forceReload;
	}

	public void setForceReload(String forceReload) {
		this.forceReload = forceReload;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		if (name == null || name.equals("null"))
			name = componentId;
		return name;
	}

	public void setVisibilityFilter(String visibilityFilter)
	{
		this.visibilityFilter = visibilityFilter;
	}

	public String getVisibilityFilter()
	{
		return visibilityFilter;
	}
	
	public void setSpaceId(String spaceId)
	{
		//useless
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public void setScope(String scope)
	{
		this.scope = scope;
	}

	public int doStartTag() throws JspTagException, AuthorizationException
	{
		int iScope = translateScope(scope);
		
		KmeliaTagUtil ktu = (KmeliaTagUtil) pageContext.getAttribute(getName(), iScope);
		if ( ("true".equals(forceReload)) || (ktu == null) )
		{
			String userId = AuthenticationManager.getUserId((HttpServletRequest)pageContext.getRequest());
			//the object is not in the scope of the page context
			//create a new object which have a reference on Kmelia EJB
			ktu = new KmeliaTagUtil(componentId, userId);
			ktu.setVisibilityFilter(visibilityFilter);
			
			pageContext.setAttribute(getName(), ktu, iScope);
		}			
		return EVAL_PAGE;
	}

	protected int translateScope(String scope)
    {
        if(scope.equalsIgnoreCase(PAGE_ID)) {
            return PageContext.PAGE_SCOPE;
        } else if(scope.equalsIgnoreCase(REQUEST_ID)) {
            return PageContext.REQUEST_SCOPE;
        } else if(scope.equalsIgnoreCase(SESSION_ID)) {
            return PageContext.SESSION_SCOPE;
        } else if(scope.equalsIgnoreCase(APPLICATION_ID)) {
            return PageContext.APPLICATION_SCOPE;
        } else
			return PageContext.REQUEST_SCOPE;
    }
}