package com.fdahpstudydesigner.util;

import java.security.SecureRandom;
import java.util.Base64;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CSPFilter implements Filter {

  public static final String POLICY = "default-src 'self';frame-ancestors 'self';form-action 'self'; "
      + "script-src 'self' https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/js/bootstrap-multiselect.min.js http://www.google-analytics.com/analytics.js 'unsafe-inline' 'unsafe-eval';"
      + "connect-src 'self' https://www.google-analytics.com/j/collect;"
      + "img-src 'self' blob: data:;"
      + "style-src 'self' https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/css/bootstrap-multiselect.css  http://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc2/css/bootstrap-glyphicons.css  https://fonts.googleapis.com 'unsafe-inline'; "
      + "font-src 'self' https://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc2/fonts/glyphiconshalflings-regular.ttf  https://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc2/fonts/glyphiconshalflings-regular.woff https://fonts.gstatic.com;";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain)
      throws IOException, ServletException {
    if (response instanceof HttpServletResponse) {
      ((HttpServletResponse)response).setHeader( "Content-Security-Policy", POLICY);
    }
    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException { }

  @Override
  public void destroy() { }

}
