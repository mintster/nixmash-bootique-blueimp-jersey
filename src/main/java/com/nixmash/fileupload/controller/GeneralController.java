package com.nixmash.fileupload.controller;

import com.google.inject.Inject;
import com.nixmash.fileupload.core.WebUI;
import com.nixmash.fileupload.resolvers.TemplatePathResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Map;

/**
 * Created by daveburke on 6/26/17.
 */
@Path("/")
public class GeneralController {

    private static final Logger logger = LoggerFactory.getLogger(GeneralController.class);

    // region Constants

    private static final String HOME_PAGE = "home";

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

}
