package com.nixmash.fileupload.controller;

import com.nixmash.fileupload.core.JettyTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class UploadControllerTest extends JettyTestBase {

    private static final String SINGLE_PAGE_STRING = "Single File Uploads";
    private static final String MULTI_PAGE_STRING = "Multiple File Upload Demo";

    @Test
    public void getMultiUploadPage() {
        assertTrue(responseOK(pathResponse(MULTI_UPLOAD_PATH)));

        Response response = pathResponse(MULTI_UPLOAD_PATH);
        String html = response.readEntity(String.class);
        assertTrue(html.contains(MULTI_PAGE_STRING));
    }

    @Test
    public void getUploadPage() {
        assertTrue(responseOK(pathResponse(SINGLE_UPLOAD_PATH)));

        Response response = pathResponse(SINGLE_UPLOAD_PATH);
        String html = response.readEntity(String.class);
        assertTrue(html.contains(SINGLE_PAGE_STRING));
    }

}