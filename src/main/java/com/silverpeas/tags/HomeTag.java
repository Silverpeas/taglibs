package com.silverpeas.tags;

import javax.ejb.EJBHome;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.exception.UtilException;
import com.stratelia.webactiv.util.publication.control.PublicationBmHome;

public class HomeTag extends TagSupport {

	protected String name;
	protected String type;

	public void setName(String name)
	{
		this.name = name;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int doStartTag() throws JspException
	{
		try
		{
			EJBHome home = (EJBHome) EJBUtilitaire.getEJBObjectRef(name, PublicationBmHome.class);

			pageContext.setAttribute(this.getId(), home);
			return SKIP_BODY;
		}
		catch (UtilException e)
		{
			throw new JspTagException("NamingException : "+e.getMessage());
		}
	}
}