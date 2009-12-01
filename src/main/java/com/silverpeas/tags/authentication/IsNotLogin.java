package com.silverpeas.tags.authentication;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class IsNotLogin extends BodyTagSupport {

	BodyContent bodyContent;

	public int doStartTag() throws JspTagException
	{
		return EVAL_BODY_TAG;
	}

	public void setBodyContent(BodyContent bodyContent) {
		this.bodyContent = bodyContent;
	}

	public int doEndTag() throws JspTagException
	{
		try
		{
			HttpSession session = pageContext.getSession();
			
			String userId = (String) session.getAttribute("UserId");
			if (userId == null || "-1".equals(userId))
			{
				if (bodyContent != null)
					bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}
		}
		catch (java.io.IOException e)
		{
			throw new JspTagException("IO Error : "+e.getMessage());
		}
		return EVAL_PAGE;
	}
}