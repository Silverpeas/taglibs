package com.silverpeas.tags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;

public class DisplayPropertyTagExtraInfo extends TagExtraInfo {
    
    public boolean isValid(TagData data)
    {
        Object o = data.getAttribute("object");
        if((o != null) && (o != TagData.REQUEST_TIME_VALUE)) {
            return false;
        }
        String name = data.getAttributeString("name");      
        String scope = data.getAttributeString("scope");
        
        /*
         * If an object was provided, reject name and scope
         * attributes. Else verify that at least the name 
         * attribute is available.
         */
        if(o != null) {
            if(null != name || null != scope) {
                return false;
            }
        } else {
            if(null == name) {
                return false;                
            }
            
            if(null != scope &&
               !scope.equals(DisplayPropertyTag.PAGE_ID) &&
               !scope.equals(DisplayPropertyTag.REQUEST_ID) &&
               !scope.equals(DisplayPropertyTag.SESSION_ID) &&
               !scope.equals(DisplayPropertyTag.APPLICATION_ID)) {
                return false;
            }
        }
 
        /*
         * Verify that if an index was provided so was the 
         * property name.
         */
        if((null != data.getAttribute("index")) && 
           (null == data.getAttribute("property"))) {
            return false;
        }
        return true;
    }
}