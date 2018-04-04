package com.nixmash.fileupload.controller;

import com.google.inject.Inject;
import com.nixmash.fileupload.core.WebUI;
import com.nixmash.fileupload.resolvers.TemplatePathResolver;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by daveburke on 6/26/17.
 */
@Path("/uploads")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    // region Constants

    private static final String SINGLE_UPLOAD_PAGE = "single";
    private static final String MULTI_UPLOAD_PAGE = "multi";

    // endregion

    // region Constructor

    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;

    @Inject
    public UploadController(TemplatePathResolver templatePathResolver, WebUI webUI) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
    }

    // endregion

    // region MultUpload Page

    @GET
    @Path("/multi")
    public String getMultiUploadPage() {
        Map<String, Object> model = webUI.getBasePageInfo(MULTI_UPLOAD_PAGE);
        return templatePathResolver.populateTemplate("multi.html", model);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/multi")
    public String restDemo(@FormDataParam("file") List<FormDataBodyPart> files) throws IOException {
        List<String> uploaded = new ArrayList<>();
        for (FormDataBodyPart bodyPart : files) {
            ContentDisposition headerOfFilePart =  bodyPart.getContentDisposition();
            InputStream fileInputStream = bodyPart.getValueAs(InputStream.class);
            if (fileInputStream.read() > 0) {
                String uploadedFileLocation = "/tmp/" + headerOfFilePart.getFileName();
                writeToFile(fileInputStream, uploadedFileLocation);
                logger.info("File uploaded to : " + uploadedFileLocation);
                uploaded.add(uploadedFileLocation);
            }
        }
        Map<String, Object> model = webUI.getBasePageInfo(MULTI_UPLOAD_PAGE);
        if (uploaded.size() > 0) {
            model.put("uploaded", uploaded);
        }
        return templatePathResolver.populateTemplate("multi.html", model);
    }

    // endregion

    // region Single Uploads Page

    @GET
    @Path("/single")
    public String getUploadPage() {
        Map<String, Object> model = webUI.getBasePageInfo(SINGLE_UPLOAD_PAGE);
        return templatePathResolver.populateTemplate("single.html", model);
    }

    @POST
    @Path("/single")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {

        Map<String, Object> model = webUI.getBasePageInfo(SINGLE_UPLOAD_PAGE);
        if (uploadedInputStream.read() > 0) {
            String uploadedFileLocation = "/tmp/" + fileDetail.getFileName();
            writeToFile(uploadedInputStream, uploadedFileLocation);
            logger.info("File uploaded to : " + uploadedFileLocation);
            String out = uploadedFileLocation;
            model.put("uploaded", out);
        }
        return templatePathResolver.populateTemplate("single.html", model);
    }

    private void writeToFile(InputStream uploadedInputStream,
                             String uploadedFileLocation) {

        try {
            OutputStream out;
            int read;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // endregion

}
