package com.silverpeas.tags.util;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class LoopTag extends BodyTagSupport {

	int times = 0;
	BodyContent bodyContent;

	public void setTimes(Integer times)
	{
		this.times = times.intValue();
	}

	public void setTimes(String times)
	{
		setTimes(new Integer(times));
	}

	public int doStartTag() throws JspTagException
	{
		if (times > 0)
		{
			return EVAL_BODY_TAG;
		} else {
			return SKIP_BODY;
		}
	}

	public void setBodyContent(BodyContent bodyContent) {
		this.bodyContent = bodyContent;
	}

	public int doAfterBody() throws JspTagException
	{
		if (times > 1)
		{
			times--;
			return EVAL_BODY_TAG;
		} else {
			return SKIP_BODY;
		}
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
}