package com.nixmash.fileupload.controller;

import com.nixmash.fileupload.core.JettyTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class BlueImpControllerTest extends JettyTestBase {

    private static final String BLUEIMP_PAGE_STRING = "BlueIimp jQuery File Upload";


    @Test
    public void getBlueImpPage() {
        assertTrue(responseOK(pathResponse(BLUEIMP_PAGE_PATH)));

        Response response = pathResponse(BLUEIMP_PAGE_PATH);
        String html = response.readEntity(String.class);
        assertTrue(html.contains(BLUEIMP_PAGE_STRING));
    }
}