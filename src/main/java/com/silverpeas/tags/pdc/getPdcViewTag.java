package com.silverpeas.tags.pdc;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class getPdcViewTag extends TagSupport {

	public static final String PAGE_ID			= "page";
    public static final String REQUEST_ID		= "request";
    public static final String SESSION_ID		= "session";
    public static final String APPLICATION_ID	= "application";

	private String	scope		= REQUEST_ID;
	private String	name;
	private String	axisId;
	private String	valueId;
	private int		depth 		= -1;
	private String	spaceId;
	private String	componentId;
	private boolean skipSpaceId = false;

	public boolean isSpaceIdSkipped() {
		return skipSpaceId;
	}

	public void setSkipSpaceId(boolean skipSpaceId) {
		this.skipSpaceId = skipSpaceId;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public getPdcViewTag()
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

	public void setAxisId(String axisId)
	{
		this.axisId = axisId;
	}

	public String getAxisId()
	{
		return axisId;
	}

	public void setValueId(String valueId)
	{
		this.valueId = valueId;
	}

	public String getValueId()
	{
		return valueId;
	}

	public void setDepth(int depth)
	{
		this.depth = depth;
	}

	public int getDepth()
	{
		return depth;
	}

	public String getSpaceId()
	{
		return spaceId;
	}

	public void setSpaceId(String spaceId)
	{
		this.spaceId = spaceId;
	}

	public void setScope(String scope)
	{
		this.scope = scope;
	}

	public int doStartTag() throws JspTagException
	{
		//create a new object which have a reference on a pdc ejb
		PdcTagUtil ptu = new PdcTagUtil(getAxisId(), getValueId(), getDepth(), getSpaceId());
		ptu.setComponentId(getComponentId());
		pageContext.setAttribute(getName(), ptu, translateScope(scope));
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