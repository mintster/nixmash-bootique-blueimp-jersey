package com.nixmash.fileupload.controller;

import com.google.inject.Inject;
import com.nixmash.fileupload.core.WebGlobals;
import com.nixmash.fileupload.core.WebUI;
import com.nixmash.fileupload.resolvers.TemplatePathResolver;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.util.Map;

@Path("/downloads")
public class DownloadController {

    private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);
    private static final String DIRECT_DOWNLOAD_PAGE = "direct";

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
    @Path("/direct")
    public String getDirectDownloadPage() {
        Map<String, Object> model = webUI.getBasePageInfo(DIRECT_DOWNLOAD_PAGE);
        return templatePathResolver.populateTemplate("direct.html", model);
    }

    @GET
    @Path("/direct/{ext}")
    public Response downloadPdfFile(@PathParam("ext") String ext)
    {
        String downloadsPath = webGlobals.downloadsPath;
        String filename = String.format("%s.%s", webGlobals.downloadFileBase, ext);
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
