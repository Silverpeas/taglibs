package com.silverpeas.tags.authentication;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class AuthenticateTag extends TagSupport {
	
	private String 	login 		= null;
	private String 	password 	= null;
	private String 	domainId 	= "0";
	

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int doStartTag() throws JspException {
		
		HttpSession session = pageContext.getSession();
		
		AuthenticateTagUtil authentication = new AuthenticateTagUtil();
		String userId = authentication.authenticate(getLogin(), getPassword(), getDomainId(), session.getId());
		
		if (userId == null || "-1".equals(userId))
			session.removeAttribute("UserId");
		else
			session.setAttribute("UserId", userId);
		
		return EVAL_PAGE;
	}

}