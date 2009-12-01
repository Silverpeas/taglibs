package com.silverpeas.tags;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.publication.control.PublicationBm;
import com.stratelia.webactiv.util.publication.control.PublicationBmHome;
import com.stratelia.webactiv.util.publication.info.model.InfoTextDetail;
import com.stratelia.webactiv.util.publication.model.CompletePublication;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;

public class PublicationContentTag extends TagSupport {

    public static final String PAGE_ID = "page";
    public static final String REQUEST_ID = "request";
    public static final String SESSION_ID = "session";
    public static final String APPLICATION_ID = "application";

    /* the object that we are going to show */
    protected Object obj = null;

    /* the name of the object that we are going to show */
    protected String objName = null;

    /* the scope of the object that we are going to show */
    protected String objScope = null;

    public void setObject(Object o)
    {
        this.obj = o;
    }

    public void setName(String name)
    {
        this.objName = name;
    }

    public void setScope(String scope)
    {
        this.objScope = scope;
    }

    public int doStartTag() throws JspException
    {
		PublicationDetail pubDetail = (PublicationDetail) getPointedObject(objName, objScope);
		if (pubDetail != null)
		{
			try
			{
				CompletePublication publication = getPublicationBm().getCompletePublication(pubDetail.getPK());
				processObject(publication);	
			}
			catch (Exception e)
			{
				throw new JspTagException("getCompletePublication failed ! : "+objName);
			}
		}
        return SKIP_BODY;
    }

    protected Object getPointedObject(String name, String scope) throws JspException
    {
        Object rc = null;
        if(null != scope) {
            rc = pageContext.getAttribute(name, translateScope(scope));
        } else {
            rc = pageContext.findAttribute(name);
        }
        if(null == rc) {
            throw new JspTagException("No object : "+name);
        }

        return rc;
    }

    protected int translateScope(String scope) throws JspException
    {
        if(scope.equalsIgnoreCase(PAGE_ID)) {
            return PageContext.PAGE_SCOPE;
        } else if(scope.equalsIgnoreCase(REQUEST_ID)) {
            return PageContext.REQUEST_SCOPE;
        } else if(scope.equalsIgnoreCase(SESSION_ID)) {
            return PageContext.SESSION_SCOPE;
        } else if(scope.equalsIgnoreCase(APPLICATION_ID)) {
            return PageContext.APPLICATION_SCOPE;
        }

        // No such scope, this is probably an error maybe the
        // TagExtraInfo associated with thit tag was not configured
        // signal that by throwing a JspException
        throw new JspTagException("No such scope : " + scope);
    }

    protected void processObject(CompletePublication cp) throws JspException
	{
        try {
            if(null != cp) {
				ArrayList				contentList	= (ArrayList) cp.getInfoDetail().getInfoTextList();
				StringBuffer			content		= new StringBuffer();
				if (contentList != null) {
					Iterator it = contentList.iterator();
					while (it.hasNext()) {
						InfoTextDetail textDetail = (InfoTextDetail) it.next();
						content.append(textDetail.getContent());
					}
				}
				pageContext.getOut().println(content.toString());
            } else {
                pageContext.getOut().println("INSTEAD_NULL");
            }
        } catch(java.io.IOException ioe) {
            // User probably disconnected ...
            throw new JspTagException("IO_ERROR");
        }
    }

    protected void clearProperties()
    {
        obj      = null;
        objName  = null;
        objScope = null;
    }

	private PublicationBm getPublicationBm() throws JspTagException
	{
		try
		{
			PublicationBmHome home = (PublicationBmHome) EJBUtilitaire.getEJBObjectRef("ejb/PublicationBm", PublicationBmHome.class);
			PublicationBm publicationBm = home.create();
			return publicationBm;
		}
		catch (Exception e)
		{
			throw new JspTagException("NamingException : "+e.getMessage());
		}	
	}
}