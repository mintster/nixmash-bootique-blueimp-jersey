package com.nixmash.fileupload.controller;

import com.google.inject.Inject;
import com.nixmash.fileupload.core.WebGlobals;
import com.nixmash.fileupload.core.WebUI;
import com.nixmash.fileupload.resolvers.TemplatePathResolver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    private final WebGlobals webGlobals;

    @Inject
    public UploadController(TemplatePathResolver templatePathResolver, WebUI webUI, WebGlobals webGlobals) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
        this.webGlobals = webGlobals;
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
    public String restDemo(@FormDataParam("file") List<FormDataBodyPart> files) throws Exception {
        List<String> uploaded = new ArrayList<>();
        for (FormDataBodyPart bodyPart : files) {
            if (!bodyPart.getContentDisposition().getFileName().equals(StringUtils.EMPTY)) {
                File tempFile = bodyPart.getValueAs(File.class);
                String uploadedFileLocation = webGlobals.fileUploadPath + bodyPart.getContentDisposition().getFileName();
                File file = new File(uploadedFileLocation);
                FileUtils.copyFile(tempFile, file);
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
    public String uploadFileAsBodyPart(
            @FormDataParam("file") FormDataBodyPart bodyPart,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {

        Map<String, Object> model = webUI.getBasePageInfo(SINGLE_UPLOAD_PAGE);
        String uploadedFileLocation = webGlobals.fileUploadPath + fileDetail.getFileName();
        File file = new File(uploadedFileLocation);

        BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyPart.getEntity();

        FileUtils.copyInputStreamToFile(bodyPartEntity.getInputStream(), file);
        logger.info("File uploaded to : " + uploadedFileLocation);
        model.put("uploaded", uploadedFileLocation);
        return templatePathResolver.populateTemplate("single.html", model);
    }

    @POST
    @Path("/inputstream/single")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadFileAsInputStream(
            @FormDataParam("file") InputStream inputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {

        Map<String, Object> model = webUI.getBasePageInfo(SINGLE_UPLOAD_PAGE);
        String uploadedFileLocation = webGlobals.fileUploadPath + fileDetail.getFileName();
        File file = new File(uploadedFileLocation);
        FileUtils.copyInputStreamToFile(inputStream, file);
        logger.info("File uploaded to : " + uploadedFileLocation);
        model.put("uploaded", uploadedFileLocation);
        return templatePathResolver.populateTemplate("single.html", model);
    }

    // endregion

}
