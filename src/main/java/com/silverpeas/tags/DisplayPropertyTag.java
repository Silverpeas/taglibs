package com.silverpeas.tags;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class DisplayPropertyTag extends TagSupport {

    public static final String PAGE_ID = "page";
    public static final String REQUEST_ID = "request";
    public static final String SESSION_ID = "session";
    public static final String APPLICATION_ID = "application";

	protected SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    /* the object that we are going to show */
    protected Object obj = null;

    /* the name of the object that we are going to show */
    protected String objName = null;

    /* the scope of the object that we are going to show */
    protected String objScope = null;

    /* the property in the object that we are going to show */
    protected String property = null;

    /* If the property is indexed then this is this index */
    protected String index = null;

	protected String dateFormat = "dd/MM/yyyy";

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

    public void setProperty(String property)
    {
        this.property = property;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

	public void setDateFormat(String dateFormat)
    {
        this.dateFormat = dateFormat;
		format.applyPattern(dateFormat);
    }

    public int doStartTag() throws JspException
    {
        processObject(getPointed());
        return SKIP_BODY;
    }

    protected Object getPointed() throws JspException
    {
        Object value = (null == obj ? getPointedObject(objName, objScope) : obj);
        if(null != property) {
            value = getPointedProperty(value);
        }

        return value;
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

    protected Object getPointedProperty(Object v) throws JspException
    {
        try {
            Object indexParam = null;
            if(null != index) {
                if(index.startsWith("#")) {
                    /* this is a number */
                    indexParam = new Integer(index.substring(1));
                } else {
                    /* this is a simple String */
                    indexParam = index;
                }
            }
            return BeanUtil.getObjectPropertyValue(v, property, indexParam);
        } catch(InvocationTargetException ite) {
            throw new JspTagException("REFLECTED_OBJECT_EXCEPTION");
        } catch(IllegalAccessException iae) {
            throw new JspTagException("ILLEGAL_METHOD_ACCESS");
        } catch(IntrospectionException ie) {
            throw new JspTagException("INTEROSPECTION_FAILED");
        } catch(NoSuchMethodException nme) {
            throw new JspTagException("NO_METHOD_FOR_PROPERTY");
        }
    }

    protected void processObject(Object v) throws JspException
	{
        try {
            if(null != v) {
				if (v instanceof java.util.Date)
				{
					pageContext.getOut().println(format.format((java.util.Date) v));
				}
				else
				{
					pageContext.getOut().println(v.toString());
				}
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
        property = null;
        index    = null;
    }
}