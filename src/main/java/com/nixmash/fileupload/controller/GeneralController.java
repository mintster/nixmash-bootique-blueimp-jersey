package com.nixmash.fileupload.controller;

import com.google.inject.Inject;
import com.nixmash.fileupload.core.WebUI;
import com.nixmash.fileupload.resolvers.TemplatePathResolver;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by daveburke on 6/26/17.
 */
@Path("/")
public class GeneralController {

    private static final Logger logger = LoggerFactory.getLogger(GeneralController.class);

    // region Constants

    private static final String HOME_PAGE = "home";
    private static final String UNAUTHORIZED_PAGE = "unauthorized";

    // endregion

    // region Constructor

    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;

    @Inject
    public GeneralController(TemplatePathResolver templatePathResolver, WebUI webUI) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
    }

    // endregion

    // region Home Page

    @GET
    public String home() {
        Map<String, Object> model = webUI.getBasePageInfo(HOME_PAGE);
        return templatePathResolver.populateTemplate("home.html", model);
    }

    // endregion

    // region login/logout redirection

    @GET
    @Path("/login.jsp")
    public Response redirectLogin() throws URISyntaxException {
        URI targetURIForRedirection = new URI("/login");
        return Response.seeOther(targetURIForRedirection).build();
    }

    @GET
    @Path("/logout")
    public Response logout() throws URISyntaxException {
        SecurityUtils.getSubject().logout();
        URI targetURIForRedirection = new URI("/?logout=true");
        return Response.temporaryRedirect(targetURIForRedirection).build();
    }

    // endregion


    @GET
    @Path("/unauthorized")
    public String unauthorized() {
        Map<String, Object> model = webUI.getBasePageInfo(UNAUTHORIZED_PAGE);
        return templatePathResolver.populateTemplate("unauthorized.html", model);
    }
}
