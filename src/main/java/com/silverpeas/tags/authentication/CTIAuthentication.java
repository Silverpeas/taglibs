package com.silverpeas.tags.authentication;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

// Referenced classes of package com.silverpeas.tags.authentication:
//            UserAuthentication

public class CTIAuthentication
    implements UserAuthentication
{

    //private AdminBm adminBm;

    public CTIAuthentication()
    {
        //adminBm = null;
    }

    /*private AdminBm getAdminBm()
    {
        if(adminBm == null)
        {
            try
            {
                adminBm = (AdminBm)EJBDynaProxy.createProxy(JNDINames.ADMINBM_EJBHOME, com.silverpeas.admin.ejb.AdminBm.class);
            }
            catch(Exception e)
            {
                throw new AdminBmRuntimeException("CTIAuthentication.getAdminBm", 4, "root.EX_CANT_GET_REMOTE_OBJECT", e);
            }
        }
        return adminBm;
    }*/

    public String getUserId(HttpServletRequest request)
    {
        String userId = null;
        //try
        //{
            Principal user = request.getUserPrincipal();
            String userName = user.getName();
            String login = userName.substring(3, userName.indexOf(","));
            //userId = getAdminBm().getUserIdByLoginAndDomain(login, "1");
            userId = "Not yet implemented";
        //}
        /*catch(RemoteException e)
        {
            e.printStackTrace();
        }*/
        return userId;
    }
}
