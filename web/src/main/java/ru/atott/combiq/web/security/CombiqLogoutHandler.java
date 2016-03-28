package ru.atott.combiq.web.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import ru.atott.combiq.service.site.UrlResolver;
import ru.atott.combiq.service.site.RequestUrlResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CombiqLogoutHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        UrlResolver urlResolver = new RequestUrlResolver(httpServletRequest);
        String siteUrl = urlResolver.externalize("/");

        String refererUrl = httpServletRequest.getHeader("Referer");
        if (StringUtils.startsWithIgnoreCase(refererUrl, siteUrl)) {
            httpServletResponse.sendRedirect(refererUrl);
        } else {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath());
        }
    }
}
