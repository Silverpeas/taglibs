package com.silverpeas.tags;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class IterateTag extends BodyTagSupport {

	private String name;
	private Iterator iterator;
	private String type;

	public IterateTag()
	{
		super();
	}

	public void setCollection(Collection collection)
	{
		if (collection.size()>0)
			iterator = collection.iterator();
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int doStartTag() throws JspTagException
	{
		if (iterator==null)
		{
			return SKIP_BODY;
		}
		return addNext(iterator);
	}

	public int doAfterBody() throws JspTagException
	{
		return addNext(iterator);
	}

	public int doEndTag() throws JspTagException
	{
		try
		{
			if (bodyContent != null)
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
		}
		catch (java.io.IOException e)
		{
			throw new JspTagException("IO Error : "+e.getMessage());
		}
		return EVAL_PAGE;
	}

	protected int addNext(Iterator iterator) throws JspTagException
	{
		if (iterator.hasNext())
		{
			pageContext.setAttribute(name, iterator.next(), PageContext.PAGE_SCOPE);
			return EVAL_BODY_TAG;
		}
		else
		{
			return SKIP_BODY;
		}
	}
}