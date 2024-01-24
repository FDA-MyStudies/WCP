/** */
package com.fdahpstudydesigner.util;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/** @author BTC */
public class CrossScriptingFilter implements Filter {
  private static Logger logger = LogManager.getLogger(CrossScriptingFilter.class);

  @Override
  public void destroy() {
    // Unused
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    logger.info("Inlter CrossScriptingFilter  ...............");
    chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
    logger.info("Outlter CrossScriptingFilter ...............");
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Unused
  }
}
