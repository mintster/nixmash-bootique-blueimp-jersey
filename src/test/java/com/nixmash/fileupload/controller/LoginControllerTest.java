package com.nixmash.fileupload.controller;

import com.nixmash.fileupload.core.JettyTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class LoginControllerTest extends JettyTestBase {

    private static final CharSequence LOGIN_PAGE_STRING = "Please Login";

    @Test
    public void loginPage() {
        assertTrue(responseOK(pathResponse(LOGIN_PAGE_PATH)));

        Response response = pathResponse(LOGIN_PAGE_PATH);
        String html = response.readEntity(String.class);
        assertTrue(html.contains(LOGIN_PAGE_STRING));
    }
}