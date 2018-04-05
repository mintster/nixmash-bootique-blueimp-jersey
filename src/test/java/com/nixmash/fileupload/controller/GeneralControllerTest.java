package com.nixmash.fileupload.controller;

import com.nixmash.fileupload.core.JettyTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class GeneralControllerTest extends JettyTestBase {

    private static final CharSequence HOME_PAGE_STRING = "Home Page";

    @Test
    public void home() {
        assertTrue(responseOK(pathResponse(HOME_PAGE_PATH)));

        Response response = pathResponse(HOME_PAGE_PATH);
        String html = response.readEntity(String.class);
        assertTrue(html.contains(HOME_PAGE_STRING));
    }
}