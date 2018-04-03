package com.nixmash.fileupload.controller;

import com.google.inject.Inject;
import com.nixmash.fileupload.core.WebGlobals;
import com.nixmash.fileupload.core.WebUI;
import com.nixmash.fileupload.resolvers.TemplatePathResolver;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.util.Map;

@Path("/downloads")
public class DownloadController {

    private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);
    private static final String DOWNLOAD_PAGE = "downloads";

    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;
    private final WebGlobals webGlobals;

    @Inject
    public DownloadController(TemplatePathResolver templatePathResolver, WebUI webUI, WebGlobals webGlobals) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
        this.webGlobals = webGlobals;
    }

    @GET
    public String getDownloadPage(@QueryParam("msg") String msg) {
        Map<String, Object> model = webUI.getBasePageInfo(DOWNLOAD_PAGE);
        if (msg != null) {
            msg = msg.equals("users") ? "USER" : "ADMIN";
            model.put("msg", msg);
        }
        return templatePathResolver.populateTemplate("downloads.html", model);
    }

    @GET
    @Path("/public/{ext}")
    public Response downloadPublicFile(@PathParam("ext") String ext)
    {
        String filename = String.format("%s.%s", webGlobals.downloadFileBase, ext);
        return getFile(filename);
    }

    @GET
    @Path("/users")
    public Response downloadUserFile()
    {
        return getFile(webGlobals.userDownloadFilename);
    }

    @GET
    @Path("/admin")
    public Response downloadAdminFile()
    {
        return getFile(webGlobals.adminDownloadFilename);
    }
    
    public Response getFile(String filename) {
        String downloadsPath = webGlobals.downloadsPath;
        StreamingOutput fileStream = output -> {
            try
            {
                File file = new File(downloadsPath + filename);
                byte[] data = FileUtils.readFileToByteArray(file);
                output.write(data);
                output.flush();
            }
            catch (Exception e)
            {
                throw new WebApplicationException("File Not Found !!");
            }
        };
        return Response
                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition","attachment; filename = " + filename)
                .build();
    }
}
