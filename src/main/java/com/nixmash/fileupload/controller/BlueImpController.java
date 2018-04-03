package com.nixmash.fileupload.controller;

import com.google.inject.Inject;
import com.nixmash.fileupload.core.WebGlobals;
import com.nixmash.fileupload.core.WebUI;
import com.nixmash.fileupload.dto.PostImage;
import com.nixmash.fileupload.resolvers.TemplatePathResolver;
import com.nixmash.fileupload.service.FileService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("/uploads")
public class BlueImpController {

    private static final Logger logger = LoggerFactory.getLogger(BlueImpController.class);

    // region Constants

    private static final String BLUEIMP_PAGE = "blueimp";
    private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif))$)";
    private Pattern pattern;

    // endregion

    // region Constructor

    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;
    private final WebGlobals webGlobals;
    private final FileService fileService;

    @Inject
    public BlueImpController(TemplatePathResolver templatePathResolver, WebUI webUI, WebGlobals webGlobals, FileService fileService) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
        this.webGlobals = webGlobals;
        this.fileService = fileService;
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
    public Map<String, Object> blueImpPageSubmit(@FormDataParam("files[]") List<FormDataBodyPart> uploaded) throws IOException {
        List<PostImage> list = new ArrayList<PostImage>();
        pattern = Pattern.compile(IMAGE_PATTERN);

        for (FormDataBodyPart bodyPart : uploaded) {
            ContentDisposition fileDetails = bodyPart.getContentDisposition();
            File tempFile = bodyPart.getValueAs(File.class);
            String filename = fileDetails.getFileName();

            if (!filename.equals(StringUtils.EMPTY)) {

                // only png/gif/jpg files accepted
                if (!isValidImageFile(filename))
                    break;

                String fileExtension = FilenameUtils.getExtension(filename).toLowerCase();
                String uploadedFileLocation = webGlobals.fileUploadPath + filename;
                File file = new File(uploadedFileLocation);
                FileUtils.copyFile(tempFile, file);

                logger.info("File uploaded to : " + uploadedFileLocation);

                BufferedImage thumbnail = Thumbnails.of(file)
                        .size(160, 160)
                        .allowOverwrite(true)
                        .outputFormat("png")
                        .asBufferedImage();
                File thumbnailFile = new File(webGlobals.thumbnailUploadPath + filename);
                ImageIO.write(thumbnail, "png", thumbnailFile);

                PostImage image = new PostImage();
                image.setPostId(-1L);
                image.setImageName(filename);
                image.setThumbnailFilename(filename);
                image.setNewFilename(filename);
                image.setContentType(String.valueOf(bodyPart.getMediaType()));
                image.setSize(FileUtils.sizeOf(tempFile));
                image.setThumbnailSize(FileUtils.sizeOf(thumbnailFile));
                image = fileService.addPostImage(image);
                image.setId(image.getId());

                image.setUrl("/uploads/photos/picture/" + image.getId());
                image.setThumbnailUrl("/uploads/photos/thumbnail/" + image.getId());
                image.setDeleteUrl("/uploads/photos/delete/" + image.getId());
                image.setDeleteType("DELETE");
                logger.info(image.toString());
                list.add(image);
            }
        }
        Map<String, Object> files = new HashMap<>();
        files.put("files", list);
        return files;
    }

// endregion

    // region Individual Photo handling

    @GET
    @Path(value = "/photos/picture/{id}")
    public void picture(@Context HttpServletResponse response, @PathParam("id") Long id) {
        PostImage image = fileService.getPostImage(id);
        File imageFile = new File(webGlobals.fileUploadPath + image.getName());
        response.setContentType(image.getContentType());
        response.setContentLength(image.getSize().intValue());
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            logger.error("Could not show picture " + id, e);
        }
    }

    @GET
    @Path(value = "/photos/thumbnail/{id}")
    public void thumbnail(@Context HttpServletResponse response, @PathParam("id") Long id) {
        PostImage image = fileService.getPostImage(id);
        File imageFile = new File(webGlobals.thumbnailUploadPath + image.getName());
        response.setContentType(image.getContentType());
        response.setContentLength(image.getThumbnailSize().intValue());
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            logger.error("Could not show thumbnail " + id, e);
        }
    }

    @DELETE
    @Path(value = "/photos/delete/{id}")
    public List<Map<String, Object>> deletePhoto(@PathParam("id") Long id) {
        PostImage image = fileService.getPostImage(id);
        File imageFile = new File(webGlobals.fileUploadPath + image.getName());
        imageFile.delete();
        File thumbnailFile = new File(webGlobals.thumbnailUploadPath + image.getName());
        thumbnailFile.delete();
        fileService.deletePostImage(id);
        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> success = new HashMap<>();
        success.put("success", true);
        results.add(success);
        return results;
    }

    // endregion

    // region private utility methods

    private boolean isValidImageFile(final String image) {
        Matcher matcher = pattern.matcher(image);
        return matcher.matches();
    }

    // endregion
}
