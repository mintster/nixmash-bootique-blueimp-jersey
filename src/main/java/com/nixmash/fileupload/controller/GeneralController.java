package com.nixmash.fileupload.controller;

import com.google.inject.Inject;
import com.nixmash.fileupload.core.WebGlobals;
import com.nixmash.fileupload.core.WebUI;
import com.nixmash.fileupload.dto.EmailForm;
import com.nixmash.fileupload.dto.EmailMessage;
import com.nixmash.fileupload.resolvers.TemplatePathResolver;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Properties;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

/**
 * Created by daveburke on 6/26/17.
 */
@Path("/")
public class GeneralController {

    private static final Logger logger = LoggerFactory.getLogger(GeneralController.class);

    // region Constants

    private static final String HOME_PAGE = "home";
    private static final String UNAUTHORIZED_PAGE = "unauthorized";
    private static final String CONTACT_PAGE = "contact";

    // endregion

    // region Constructor

    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;
    private final WebGlobals webGlobals;

    @Inject
    public GeneralController(TemplatePathResolver templatePathResolver, WebUI webUI, WebGlobals webGlobals) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
        this.webGlobals = webGlobals;
    }

    // endregion

    // region Home Page

    @GET
    public String home() {
        Map<String, Object> model = webUI.getBasePageInfo(HOME_PAGE);
        return templatePathResolver.populateTemplate("home.html", model);
    }

    // endregion

    // region login/logout redirection

    @GET
    @Path("/login.jsp")
    public Response redirectLogin() throws URISyntaxException {
        URI targetURIForRedirection = new URI("/login");
        return Response.seeOther(targetURIForRedirection).build();
    }

    @GET
    @Path("/logout")
    public Response logout() throws URISyntaxException {
        SecurityUtils.getSubject().logout();
        URI targetURIForRedirection = new URI("/?logout=true");
        return Response.temporaryRedirect(targetURIForRedirection).build();
    }

    // endregion

    // region Unauthorized
    @GET
    @Path("/unauthorized")
    public String unauthorized() {
        Map<String, Object> model = webUI.getBasePageInfo(UNAUTHORIZED_PAGE);
        return templatePathResolver.populateTemplate("unauthorized.html", model);
    }
    // endregion

    // region Contact Us
    @GET
    @Path("/contact")
    public String contactPage() {
        Map<String, Object> model = webUI.getBasePageInfo(CONTACT_PAGE);
        return templatePathResolver.populateTemplate("contact.html", model);
    }

    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Path("/contact")
    public String contactPageSubmit(@BeanParam EmailForm emailForm) {
        logger.info(emailForm.toString());
        Boolean successful = sendContactEmail(emailForm);
        Map<String, Object> model = getFeedback(successful);
        return templatePathResolver.populateTemplate("contact.html", model);
    }

    // region private mail utility methods

    private Map<String, Object> getFeedback(Boolean successful) {
        Map<String, Object> model = webUI.getBasePageInfo(CONTACT_PAGE);
        if (successful) {
            model.put("msg", webUI.getMessage("email.msg.success"));
            model.put("alertStyle", "success");
        } else {
            model.put("msg", webUI.getMessage("email.msg.fail"));
            model.put("alertStyle", "danger");
        }
        return model;
    }

    private EmailMessage getContactMessage(EmailForm emailForm) {
        EmailMessage msg = new EmailMessage();
        String to = webGlobals.mailContactTo;

        if (!webGlobals.inProductionMode)
            to = webGlobals.mailDeveloperTo;
        msg.setTo(to);
        msg.setFrom(emailForm.getEmail());
        msg.setHost(webGlobals.mailHost);
        msg.setSubject(webUI.getMessage("email.contact.subject"));
        msg.setBody(emailForm.getMessage());
        return msg;
    }

    private Boolean sendContactEmail(EmailForm emailForm) {
        Boolean successful = true;

        EmailMessage msg = getContactMessage(emailForm);

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", msg.getHost());
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(msg.getFrom()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(msg.getTo()));
            message.setSubject(msg.getSubject());
            message.setText(msg.getBody());

            Transport.send(message);
            logger.info("Contact message sent successfully....");

        } catch (javax.mail.MessagingException e) {
            logger.error("Exception sending contact email: " + e.getMessage());
            successful = false;
        }
        return successful;
    }

    // endregion

}
