package com.nixmash.fileupload.controller;

import com.google.inject.Inject;
import com.nixmash.fileupload.core.WebGlobals;
import com.nixmash.fileupload.core.WebUI;
import com.nixmash.fileupload.dto.PostImage;
import com.nixmash.fileupload.resolvers.TemplatePathResolver;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/uploads")
public class BlueImpController {

    private static final Logger logger = LoggerFactory.getLogger(BlueImpController.class);

    // region Constants

    private static final String BLUEIMP_PAGE = "blueimp";

    // endregion

    // region Constructor

    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;
    private final WebGlobals webGlobals;

    @Inject
    public BlueImpController(TemplatePathResolver templatePathResolver, WebUI webUI, WebGlobals webGlobals) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
        this.webGlobals = webGlobals;
    }

    // endregion

    // region BlueImp Page @GET and Upload


    @GET
    @Path("/blueimp")
    public String getBlueImpPage() {
        Map<String, Object> model = webUI.getBasePageInfo(BLUEIMP_PAGE);
        return templatePathResolver.populateTemplate("blueimp.html", model);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/blueimp")
    public Map<String, Object> blueImpPageSubmit(@FormDataParam("files[]") FormDataBodyPart uploaded) throws IOException {
        List<PostImage> list = new ArrayList<PostImage>();

        FormDataBodyPart bodyPart = uploaded;
        ContentDisposition headerOfFilePart = bodyPart.getContentDisposition();

        InputStream fileInputStream = bodyPart.getValueAs(InputStream.class);
        String filename = headerOfFilePart.getFileName();
        logger.info("Uploading {}", filename);

        String fileExtension = FilenameUtils.getExtension(filename).toLowerCase();
        String newFilenameBase = RandomStringUtils.randomAlphanumeric(25);
        String newFilename = String.format("%s.%s", newFilenameBase, fileExtension);
        String contentType = String.valueOf(bodyPart.getMediaType());
        String storageFilePath = webGlobals.fileUploadPath + newFilename;
        File storageFile = new File(storageFilePath + newFilename);

        if (fileInputStream.read() > 0) {
            String temporaryFile = "/tmp/" + filename;
            FileUtils.copyInputStreamToFile(fileInputStream, storageFile);
//                FileUtils.copyFile(new File(temporaryFile), storageFile);
            logger.info("File uploaded to : " + storageFile);

            BufferedImage thumbnail = Thumbnails.of(storageFile)
                    .size(160, 160)
                    .allowOverwrite(true)
                    .outputFormat("png")
                    .asBufferedImage();
            String thumbnailFilename = newFilenameBase + "-thumbnail.png";
            File thumbnailFile = new File(storageFilePath + thumbnailFilename);
            ImageIO.write(thumbnail, "png", thumbnailFile);

            PostImage image = new PostImage();
            image.setPostId(-1L);
            image.setName(filename);
            image.setThumbnailFilename(thumbnailFilename);
            image.setNewFilename(newFilename);
            image.setContentType(contentType);
            image.setSize(storageFile.length());
            image.setThumbnailSize(thumbnailFile.length());
//                image = postService.addImage(image);
            image.setId(RandomUtils.nextLong(1L, 1000L));

            image.setUrl("/posts/photos/picture/" + image.getId());
            image.setThumbnailUrl("/posts/photos/thumbnail/" + image.getId());
            image.setDeleteUrl("/posts/photos/delete/" + image.getId());
            image.setDeleteType("DELETE");
            logger.info(image.toString());
            list.add(image);
        }
        Map<String, Object> files = new HashMap<>();
        files.put("files", list);
        return files;
    }
// endregion

}
