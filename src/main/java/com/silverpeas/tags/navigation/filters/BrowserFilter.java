/**
 * Copyright (C) 2000 - 2012 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.tags.navigation.filters;

import java.io.IOException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Redirect on page on user-agent conditions.
 * @author svuillet
 */
public class BrowserFilter implements Filter {

  private final static Logger LOGGER = Logger.getLogger(BrowserFilter.class);

  // Configured params
  private String[] goodBrowserIds = null;
  private String[] badBrowserIds = null;
  private String badBrowserUrl;

  // Filter param keys
  public static final String KEY_BROWSER_IDS = "browserIds";
  public static final String KEY_UNSUPPORTED_BROWSER_IDS = "unsupportedBrowserIds";
  public static final String KEY_BAD_BROWSER_URL = "badBrowserUrl";

  @Override
  public void destroy() {
    goodBrowserIds = null;
    badBrowserUrl = null;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
      throws IOException, ServletException {
    String userAgent = ((HttpServletRequest) req).getHeader("User-Agent");
    if (userAgent != null) {
      if (goodBrowserIds != null) {
        for (String browser_id : goodBrowserIds) {
          if (userAgent.toLowerCase().contains(browser_id.toLowerCase())) {
            chain.doFilter(req, resp);
            return;
          }
        }
        // Unsupported browser
        String url = ((HttpServletRequest) req).getRequestURL().toString();
        if (url.contains(this.badBrowserUrl) || url.toLowerCase().endsWith(".jpg")
            || url.toLowerCase().endsWith(".png") || url.toLowerCase().endsWith(".gif")
            || url.toLowerCase().endsWith(".ico") || url.toLowerCase().endsWith(".css") ||
            url.toLowerCase().endsWith(".js")) {
          chain.doFilter(req, resp);
          return;
        }
        LOGGER.info("Badbrowser ! User-agent : " + userAgent);
        ((HttpServletResponse) resp).sendRedirect(((HttpServletRequest) req).getContextPath() +
            badBrowserUrl);
      } else {
        // Unsupported browser
        String url = ((HttpServletRequest) req).getRequestURL().toString();
        if (url.contains(this.badBrowserUrl) || url.toLowerCase().endsWith(".jpg")
            || url.toLowerCase().endsWith(".png") || url.toLowerCase().endsWith(".gif")
            || url.toLowerCase().endsWith(".ico") || url.toLowerCase().endsWith(".css") ||
            url.toLowerCase().endsWith(".js")) {
          chain.doFilter(req, resp);
          return;
        }
        for (String browser_id : badBrowserIds) {
          if (userAgent.toLowerCase().contains(browser_id.toLowerCase())) {
            LOGGER.info("Badbrowser ! User-agent : " + userAgent);
            ((HttpServletResponse) resp).sendRedirect(((HttpServletRequest) req).getContextPath() +
                badBrowserUrl);
            return;
          }
        }
        chain.doFilter(req, resp);
      }
    } else {
      // No filter for robots
      chain.doFilter(req, resp);
    }
  }

  @Override
  public void init(FilterConfig cfg) throws ServletException {
    URL conf = BrowserFilter.class.getClassLoader().getResource("log4j.properties");
    if (conf != null)
      PropertyConfigurator.configure(conf);
    String ids = cfg.getInitParameter(KEY_BROWSER_IDS);
    if (ids != null) {
      this.goodBrowserIds = ids.split(",");
      for (int i = 0; i < goodBrowserIds.length; i++) {
        goodBrowserIds[i] = goodBrowserIds[i].trim();
      }
    } else {
      ids = cfg.getInitParameter(KEY_UNSUPPORTED_BROWSER_IDS);
      if (ids != null) {
        this.badBrowserIds = ids.split(",");
        for (int i = 0; i < badBrowserIds.length; i++) {
          badBrowserIds[i] = badBrowserIds[i].trim();
        }
      } else {
        throw new IllegalArgumentException(
            "BrowserFilter requires param browserIds or unsupportedBrowserIds");
      }
    }

    badBrowserUrl = cfg.getInitParameter(KEY_BAD_BROWSER_URL);
    if (badBrowserUrl == null) {
      throw new IllegalArgumentException("BrowserFilter requires param badBrowserUrl");
    }
  }
}
