package com.silverpeas.tags.authentication;

import com.silverpeas.tags.util.SiteTagUtil;
import javax.servlet.http.HttpServletRequest;

// Referenced classes of package com.silverpeas.tags.authentication:
//            UserAuthentication

public class AuthenticationManager
{

    public AuthenticationManager()
    {
    }

    public static String getUserId(HttpServletRequest request)
    {
        String userId = null;
        try
        {
            UserAuthentication userAuthentication = (UserAuthentication)Class.forName(SiteTagUtil.getUserAuthenticationClass()).newInstance();
            userId = userAuthentication.getUserId(request);
        }
        catch(InstantiationException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return userId;
    }
}
