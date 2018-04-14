package com.nixmash.fileupload.controller;

import com.nixmash.fileupload.core.JettyTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class GeneralControllerTest extends JettyTestBase {

    private static final CharSequence HOME_PAGE_STRING = "Home Page";
    private static final CharSequence UNAUTHORIZED_PAGE_STRING = "Unauthorized";
    private static final String CONTACT_PAGE_PATH = "/contact";
    private static final CharSequence CONTACT_PAGE_STRING = "contact";

    @Test
    public void home() {
        assertTrue(responseOK(pathResponse(HOME_PAGE_PATH)));

        Response response = pathResponse(HOME_PAGE_PATH);
        String html = response.readEntity(String.class);
        assertTrue(html.contains(HOME_PAGE_STRING));
    }

    @Test
    public void unauthorizedPage() {
        assertTrue(responseOK(pathResponse(UNAUTHORIZED_PAGE_PATH)));

        Response response = pathResponse(UNAUTHORIZED_PAGE_PATH);
        String html = response.readEntity(String.class);
        assertTrue(html.contains(UNAUTHORIZED_PAGE_STRING));
    }

    @Test
    public void contactPage() {
        assertTrue(responseOK(pathResponse(CONTACT_PAGE_PATH)));

        Response response = pathResponse(CONTACT_PAGE_PATH);
        String html = response.readEntity(String.class);
        assertTrue(html.contains(CONTACT_PAGE_STRING));
    }

    @Test
    public void contactPathSubmit() {
        Form form = new Form();
        form.param("fullName", "bob");
        form.param("email", "bob@aol.com");
        form.param("message", "my test message");
        Response response = getTargetUrl("/contact")
                .request()
                .post(Entity.entity(form, "application/x-www-form-urlencoded"), Response.class);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }


}