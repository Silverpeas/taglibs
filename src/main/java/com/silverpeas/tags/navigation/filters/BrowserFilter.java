package com.silverpeas.tags.navigation.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Redirect on page on user-agent conditions.
 * @author svuillet
 */
public class BrowserFilter implements Filter {

	// Be default, support all moderns browsers
	private static final String[] DEFAULT_BROWSERS = { "Chrome", "Firefox", "Safari", "Opera", "MSIE 9", "MSIE 8" };
	
	// Configured params
	private String[] browserIds;
	private String badBrowserUrl;

	// Filter param keys
	public static final String KEY_BROWSER_IDS = "browserIds";
	public static final String KEY_BAD_BROWSER_URL = "badBrowserUrl";

	@Override
	public void destroy() {
		browserIds = null;
		badBrowserUrl = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {		
		String userAgent = ((HttpServletRequest) req).getHeader("User-Agent");
		for (String browser_id : browserIds) {
			if (userAgent.contains(browser_id.trim())) {
				chain.doFilter(req, resp);
				return;
			}
		}
		// Unsupported browser
		String url = ((HttpServletRequest) req).getRequestURL().toString();
		if (url.contains(this.badBrowserUrl) || url.toLowerCase().endsWith(".jpg")
				|| url.toLowerCase().endsWith(".png") || url.toLowerCase().endsWith(".gif") 
				|| url.toLowerCase().endsWith(".ico") || url.toLowerCase().endsWith(".css") || url.toLowerCase().endsWith(".js")) {
			chain.doFilter(req, resp);
			return;
		}
		((HttpServletResponse) resp).sendRedirect(((HttpServletRequest) req).getContextPath() + badBrowserUrl);
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		String ids = cfg.getInitParameter(KEY_BROWSER_IDS);
		this.browserIds = (ids != null) ? ids.split(",") : DEFAULT_BROWSERS;
		badBrowserUrl = cfg.getInitParameter(KEY_BAD_BROWSER_URL);
		if (badBrowserUrl == null) {
			throw new IllegalArgumentException("BrowserFilter requires param badBrowserUrl");
		}
	}
}
