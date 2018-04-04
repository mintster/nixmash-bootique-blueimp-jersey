package com.nixmash.fileupload.controller;

import com.google.inject.Inject;
import com.nixmash.fileupload.core.WebUI;
import com.nixmash.fileupload.resolvers.TemplatePathResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daveburke on 6/26/17.
 */
@Path("/")
public class RestController {

    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    // region Constants

    private static final String HOME_PAGE = "home";

    // endregion

    // region Constructor

    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;

    @Inject
    public RestController(TemplatePathResolver templatePathResolver, WebUI webUI) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
    }

    // endregion

    // region Home Page

    @GET
    @Path("/rest/addfile/{fieldnum}")
    @Produces(MediaType.TEXT_HTML)
    public String addFile(@PathParam("fieldnum") Integer fieldnum)  {
        Map<String, Object> model = new HashMap<>();
        model.put("fieldName", String.format("file%s", fieldnum));
        return templatePathResolver.populateTemplate("rest/addfile.html", model);
    }

    // endregion

}
