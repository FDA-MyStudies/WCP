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

  public static final String POLICY =
      "default-src 'self';frame-ancestors 'self';form-action 'self'; "
          + "script-src 'self' https://www.googletagmanager.com/gtag/js https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/js/bootstrap-multiselect.min.js http://www.google-analytics.com/analytics.js 'nonce-{nonce}';"
          + "connect-src 'self' https://www.google-analytics.com/j/collect https://www.google-analytics.com/g/collect;"
          + "img-src 'self' blob: data:;"
          + "style-src 'self' https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/css/bootstrap-multiselect.css  http://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc2/css/bootstrap-glyphicons.css  https://fonts.googleapis.com 'nonce-{nonce}';"
          + "font-src 'self' https://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc2/fonts/glyphiconshalflings-regular.ttf  https://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc2/fonts/glyphiconshalflings-regular.woff https://fonts.gstatic.com;";
  private static final int NONCE_SIZE = 32; //recommended is at least 128 bits/16 bytes
  private static final String CSP_NONCE_ATTRIBUTE = "nonce";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain)
      throws IOException, ServletException {
    if (response instanceof HttpServletResponse) {
      byte[] nonceArray = new byte[NONCE_SIZE];
      SecureRandom secureRandom = new SecureRandom();
      secureRandom.nextBytes(nonceArray);
      String nonce = Base64.getEncoder().encodeToString(nonceArray);
      request.setAttribute(CSP_NONCE_ATTRIBUTE, nonce);
      ((HttpServletResponse) response).setHeader("Content-Security-Policy",
          POLICY.replace("{nonce}", nonce));
    }
    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException { }

  @Override
  public void destroy() { }

}
