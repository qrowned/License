package dev.qrowned.license.server.controller.interceptor;

import dev.qrowned.license.server.LicenseServerConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public final class ApiKeyFilter extends GenericFilterBean {

    private final LicenseServerConfig licenseServerConfig;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest httpServletRequest)) return;

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String header = httpServletRequest.getHeader("API-KEY");

        if (httpServletRequest.getServletPath().startsWith("/check/")
                || (this.licenseServerConfig.getApiKeys().contains(header)
                || this.licenseServerConfig.getIpWhitelist().contains(ServletRequestHelper.getClientIpAddressIfServletRequestExist()))
        )
            filterChain.doFilter(servletRequest, servletResponse);
        else
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please provide a valid API-KEY included in the header of your request!");
    }

}