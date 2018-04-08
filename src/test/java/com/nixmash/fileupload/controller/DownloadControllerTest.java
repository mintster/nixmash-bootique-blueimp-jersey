package com.nixmash.fileupload.controller;

import com.nixmash.fileupload.core.JettyTestBase;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class DownloadControllerTest extends JettyTestBase {


    private static final String DOWNLOADS_FILE_STRING = "File Downloads";

    @Test
    public void getDirectDownloadPage() {
        assertTrue(responseOK(pathResponse(DOWNLOADS_PATH)));

        Response response = pathResponse(DOWNLOADS_PATH);
        String html = response.readEntity(String.class);
        assertTrue(html.contains(DOWNLOADS_FILE_STRING));
    }
}