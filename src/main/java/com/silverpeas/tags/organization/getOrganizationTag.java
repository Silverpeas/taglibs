package com.silverpeas.tags.organization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.silverpeas.tags.authentication.AuthenticationManager;
import com.stratelia.silverpeas.peasCore.MainSessionController;

public class getOrganizationTag extends TagSupport {

	public static final String PAGE_ID			= "page";
    public static final String REQUEST_ID		= "request";
    public static final String SESSION_ID		= "session";
    public static final String APPLICATION_ID	= "application";

	private String					scope				= REQUEST_ID;
	private String					name;
	private String					visibilityFilter	= null;
	private MainSessionController	mainSessionCtrl		= null;

	public getOrganizationTag()
	{
		super();
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
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

	public void setScope(String scope)
	{
		this.scope = scope;
	}

	public void setMainSessionCtrl(MainSessionController mainSessionCtrl)
	{
		this.mainSessionCtrl = mainSessionCtrl;
	}

	public int doStartTag() throws JspTagException
	{
		String userId = AuthenticationManager.getUserId((HttpServletRequest)pageContext.getRequest());
		
		//create a new object which have a reference on an OrganizationController
		OrganizationTagUtil otu = new OrganizationTagUtil(getVisibilityFilter(), mainSessionCtrl);
		otu.setUserId(userId);
		
		pageContext.setAttribute(getName(), otu, translateScope(scope));
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