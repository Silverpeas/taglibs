package com.silverpeas.tags.authentication;

import com.silverpeas.tags.util.SiteTagUtil;
import javax.servlet.http.HttpServletRequest;

// Referenced classes of package com.silverpeas.tags.authentication:
//            UserAuthentication

public class BasicAuthentication
    implements UserAuthentication
{

    public BasicAuthentication()
    {
    }

    public String getUserId(HttpServletRequest request)
    {
        return SiteTagUtil.getUserId();
    }
}
