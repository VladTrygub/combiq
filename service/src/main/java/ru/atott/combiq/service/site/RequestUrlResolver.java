package ru.atott.combiq.service.site;

import javax.servlet.http.HttpServletRequest;

public class RequestUrlResolver extends HostPortUrlResolver {

    public RequestUrlResolver(HttpServletRequest request) {
        super(request.getServerName(), request.getServerPort());
    }
}
