package com.silverpeas.tags.authentication;

//import com.silverpeas.tags.util.SiteTagUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// Referenced classes of package com.silverpeas.tags.authentication:
//            UserAuthentication

public class SQLAuthentication
    implements UserAuthentication
{

    public SQLAuthentication()
    {
    }

    public String getUserId(HttpServletRequest request)
    {
    	HttpSession session = request.getSession();
    	String userId = (String) session.getAttribute("UserId");
    	return userId;
        //return SiteTagUtil.getUserId();
    }
}
