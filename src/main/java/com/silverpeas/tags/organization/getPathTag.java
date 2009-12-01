package com.silverpeas.tags.organization;

import java.util.Collection;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class getPathTag extends TagSupport {

	public static final String PAGE_ID			= "page";
    public static final String REQUEST_ID		= "request";
    public static final String SESSION_ID		= "session";
    public static final String APPLICATION_ID	= "application";

	private String		scope				= REQUEST_ID;
	private String		name;
	private String		spaceId;
	private String		componentId;
	private String		objectId;

	public getPathTag()
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

	public void setSpaceId(String spaceId)
	{
		this.spaceId = spaceId;
	}

	public String getSpaceId()
	{
		return spaceId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setObjectId(String objectId)
	{
		this.objectId = objectId;
	}

	public String getObjectId()
	{
		return objectId;
	}


	public void setScope(String scope)
	{
		this.scope = scope;
	}

	public int doStartTag() throws JspTagException
	{
		//create a new object which have a reference on an OrganizationController
		OrganizationTagUtil otu = new OrganizationTagUtil();
		MenuItem item = new MenuItem(getSpaceId(), getComponentId(), getObjectId());
		Collection path = null;
		try
		{
			path = otu.getItemPath(item);
		}
		catch (Exception e)
		{
		}
		pageContext.setAttribute(getName(), path, translateScope(scope));
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