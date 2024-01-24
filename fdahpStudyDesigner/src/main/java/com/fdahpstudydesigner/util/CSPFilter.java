package com.fdahpstudydesigner.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CSPFilter implements Filter {

  public static final String STRICT_POLICY =
      "default-src 'self';frame-ancestors 'self';form-action 'self'; "
          + "script-src 'self' https://www.googletagmanager.com/gtag/js https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/js/bootstrap-multiselect.min.js http://www.google-analytics.com/analytics.js 'nonce-{nonce}';"
          + "connect-src 'self' https://www.google-analytics.com/j/collect https://www.google-analytics.com/g/collect;"
          + "img-src 'self' blob: data:;"
          + "style-src 'self' https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/css/bootstrap-multiselect.css  http://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc2/css/bootstrap-glyphicons.css  https://fonts.googleapis.com 'nonce-{nonce}';"
          + "font-src 'self' https://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc2/fonts/glyphiconshalflings-regular.ttf  https://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc2/fonts/glyphiconshalflings-regular.woff https://fonts.gstatic.com;";

  public static final String LENIENT_POLICY =
      "default-src 'self';frame-ancestors 'self';form-action 'self'; "
          + "script-src 'self' https://www.googletagmanager.com/gtag/js https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/js/bootstrap-multiselect.min.js http://www.google-analytics.com/analytics.js 'unsafe-inline';"
          + "connect-src 'self' https://www.google-analytics.com/j/collect https://www.google-analytics.com/g/collect;"
          + "img-src 'self' blob: data:;"
          + "style-src 'self' https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/css/bootstrap-multiselect.css  http://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc2/css/bootstrap-glyphicons.css  https://fonts.googleapis.com 'unsafe-inline';"
          + "font-src 'self' https://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc2/fonts/glyphiconshalflings-regular.ttf  https://netdna.bootstrapcdn.com/bootstrap/3.0.0-rc2/fonts/glyphiconshalflings-regular.woff https://fonts.gstatic.com;";

  private static final int NONCE_SIZE = 32; //recommended is at least 128 bits/16 bytes
  private static final String CSP_NONCE_ATTRIBUTE = "nonce";
  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    String query = req.getRequestURI();
    String disallowCspUrls = "/fdahpStudyDesigner/adminStudies/viewBasicInfo.do,"
        + "/fdahpStudyDesigner/adminStudies/viewQuestionnaire.do,"
        + "/fdahpStudyDesigner/adminDashboard/viewUserDetails.do,"
        + "/fdahpStudyDesigner/adminStudies/addOrEditResource.do,"
        + "/fdahpStudyDesigner/adminStudies/consentInfo.do,"
        + "/fdahpStudyDesigner/adminStudies/viewActiveTask.do,"
        + "/fdahpStudyDesigner/adminStudies/consentReview.do,"
        + "/fdahpStudyDesigner/adminStudies/viewStudyQuestionnaires.do,"
        + "/fdahpStudyDesigner/adminStudies/getResourceList.do,"
        + "/fdahpStudyDesigner/adminStudies/questionStep.do,"
        + "/fdahpStudyDesigner/adminStudies/comprehensionQuestionList.do,"
        + "/fdahpStudyDesigner/adminStudies/comprehensionQuestionPage.do,"
        + "/fdahpStudyDesigner/adminStudies/viewActiveTask.do,"
        + "/fdahpStudyDesigner/adminStudies/consentListPage.do,"
        + "/fdahpStudyDesigner/adminStudies/viewSettingAndAdmins.do,"
        + "/fdahpStudyDesigner/adminStudies/overviewStudyPages.do,"
        + "/fdahpStudyDesigner/adminStudies/formQuestion.do,"
        + "/fdahpStudyDesigner/adminStudies/viewStudyActiveTasks.do,"
        + "/fdahpStudyDesigner/adminStudies/formStep.do";
    if (response instanceof HttpServletResponse) {
      if (Arrays.asList(disallowCspUrls.split(",")).contains(query)) {
        ((HttpServletResponse) response).setHeader("Content-Security-Policy", LENIENT_POLICY);
      } else {
        byte[] nonceArray = new byte[NONCE_SIZE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(nonceArray);
        String nonce = Base64.getEncoder().encodeToString(nonceArray);
        request.setAttribute(CSP_NONCE_ATTRIBUTE, nonce);
        ((HttpServletResponse) response).setHeader("Content-Security-Policy",
            STRICT_POLICY.replace("{nonce}", nonce));
      }
    }
    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException { }

  @Override
  public void destroy() { }

}
