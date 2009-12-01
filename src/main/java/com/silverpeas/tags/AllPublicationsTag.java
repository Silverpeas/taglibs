package com.silverpeas.tags;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.publication.control.PublicationBm;
import com.stratelia.webactiv.util.publication.control.PublicationBmHome;
import com.stratelia.webactiv.util.publication.model.PublicationPK;

public class AllPublicationsTag extends BodyTagSupport {

	private Iterator	iterator;
	
	private String		name = "publication";
	private String		componentId;
	private int			iterations = 1;
	private boolean		iterationsUsed = false;

	private PublicationBm publicationBm;

	public AllPublicationsTag()
	{
		super();
	}

	public void setIterations(int iterations)
	{
		this.iterations = iterations;
		iterationsUsed = true;
	}

	/*public void setName(String name)
	{
		this.name = name;
	}*/

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public int doStartTag() throws JspTagException
	{
		Collection allPublications = getAllPublications();
		if (allPublications.size()>0)
			iterator = allPublications.iterator();
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
		if (iterations > 0 && iterator.hasNext()) {
			pageContext.setAttribute(name, iterator.next(), PageContext.PAGE_SCOPE);
			if (iterationsUsed)
				iterations--;

			return EVAL_BODY_TAG;
		}
		else
		{
			return SKIP_BODY;
		}
	}

	private PublicationBm getPublicationBm() throws JspTagException
	{
		try
		{
			PublicationBmHome home = (PublicationBmHome) EJBUtilitaire.getEJBObjectRef("ejb/PublicationBm", PublicationBmHome.class);
			publicationBm = home.create();
			return publicationBm;
		}
		catch (Exception e)
		{
			throw new JspTagException("NamingException : "+e.getMessage());
		}	
	}

	private Collection getAllPublications() throws JspTagException
	{
		try
		{
			Collection allPublications = getPublicationBm().getAllPublications(new PublicationPK("useless", "useless", componentId), "P.pubCreationDate desc");
			return allPublications;
		}
		catch (Exception e)
		{
			throw new JspTagException("Getting info failed : "+e.getMessage());
		}
	}
}