package com.silverpeas.tags.authentication;

import javax.servlet.http.HttpServletRequest;

public interface UserAuthentication
{

    public abstract String getUserId(HttpServletRequest httpservletrequest);
}
