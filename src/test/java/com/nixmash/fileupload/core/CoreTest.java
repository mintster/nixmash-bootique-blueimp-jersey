package com.nixmash.fileupload.core;

import com.nixmash.fileupload.dto.PageInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by daveburke on 6/6/17.
 */
@RunWith(JUnit4.class)
public class CoreTest extends TestBase {

    private static final String APPLICATION_ID = "FileUploader-Test";
    private static final String CLOUD_APPLICATION_ID = "FileUploader";

    private static final Logger logger = LoggerFactory.getLogger(CoreTest.class);

    @Test
    public void loggingTest() {
        assertTrue(logger.isInfoEnabled());
    }

    @Test
    public void globalsTests() {
        // webGlobals always from shared external properties file
        String cloudApplicationId = webGlobals.cloudApplicationId;
        assertEquals(cloudApplicationId, CLOUD_APPLICATION_ID);
    }

    @Test
    public void configPropertyTests() {
        // webContext always local, i.e., test.properties
        String applicationId = webContext.config().applicationId;
        assertEquals(applicationId, APPLICATION_ID);
    }

    @Test
    public void contextTests() {
        assertNotNull(webContext.config().applicationId);
    }

    @Test
    public void pageInfoTests() {
        Optional<PageInfo> pageInfo = webUI.getPageInfo("home");
        assertTrue(pageInfo.isPresent());
    }

    @Test
    public void messageTests() throws Exception {
        assertEquals(webContext.messages().get("navbar.home"), "Home");
        assertEquals(webContext.messages().get("navbar.uploads"), "Uploads");
    }

    @Test
    public void createUserKeyTests() {
        String userKey = RandomStringUtils.randomAlphanumeric(25);
        System.out.println(userKey);
        assertEquals(userKey.length(),25);

        String uuidKey= UUID.randomUUID().toString();
        assertEquals(uuidKey.length(), 36);
    }
}
