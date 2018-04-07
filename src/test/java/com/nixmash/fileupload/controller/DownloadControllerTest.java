package com.nixmash.fileupload.controller;

import com.nixmash.fileupload.core.JettyTestBase;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class DownloadControllerTest extends JettyTestBase {


    private static final String DIRECT_PAGE_STRING = "Direct File Downloads";

    @Test
    public void getDirectDownloadPage() {
        assertTrue(responseOK(pathResponse(DIRECT_DOWNLOAD_PATH)));

        Response response = pathResponse(DIRECT_DOWNLOAD_PATH);
        String html = response.readEntity(String.class);
        assertTrue(html.contains(DIRECT_PAGE_STRING));
    }
}