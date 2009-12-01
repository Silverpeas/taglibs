package com.silverpeas.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class HelloWorldTag
    extends TagSupport {

    public int doStartTag()
        throws JspException
    {
        try {
            pageContext.getOut().print("Hello JSP tag World"); 
        } catch(IOException ioe) { 
            throw new JspTagException("Error:IOException while writing to the user");
        }
        return SKIP_BODY;
    }
}

