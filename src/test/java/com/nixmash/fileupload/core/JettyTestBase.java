package com.nixmash.fileupload.core;

import com.nixmash.fileupload.auth.NixmashRealm;
import com.nixmash.fileupload.controller.GeneralController;
import com.nixmash.fileupload.db.UserDb;
import com.nixmash.fileupload.db.UserDbImpl;
import com.nixmash.fileupload.service.UserService;
import com.nixmash.fileupload.service.UserServiceImpl;
import io.bootique.BQRuntime;
import io.bootique.jersey.JerseyModule;
import io.bootique.shiro.ShiroModule;
import io.bootique.shiro.web.ShiroWebModule;
import io.bootique.test.junit.BQTestFactory;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;

@RunWith(JUnit4.class)
public class JettyTestBase {

    public static final String TEST_URL = "http://127.0.1.1:8001";

    protected static final String HOME_PAGE_PATH = "/";
    protected static final String SINGLE_UPLOAD_PATH = "/uploads/single";
    protected static final String MULTI_UPLOAD_PATH = "/uploads/multi";
    protected static final String BLUEIMP_PAGE_PATH = "/uploads/blueimp";

    @ClassRule
    public static BQTestFactory testFactory = new BQTestFactory();

    private static Client client;

    protected static WebUI webUI;
    protected static WebContext webContext;
    protected static WebGlobals webGlobals;

// region @BeforeClass and @AfterClass

    @BeforeClass
    public static void setupClass() throws IOException {

        Package pkg = GeneralController.class.getPackage();

        BQRuntime runtime = testFactory.app()
                .autoLoadModules()
                .args("-s","--config=classpath:test.yml")
                .module(b -> b.bind(WebUI.class))
                .module(b -> JerseyModule.extend(b).addPackage(pkg).addFeature(MultiPartFeature.class))
                .module(b -> b.bind(UserService.class).to(UserServiceImpl.class))
                .module(b -> b.bind(UserDb.class).to(UserDbImpl.class))
                .module(b -> ShiroModule.extend(b).addRealm(NixmashRealm.class))
                .module(b -> ShiroWebModule.extend(b).initAllExtensions())
                .createRuntime();

        runtime.run();

        webUI= runtime.getInstance(WebUI.class);
        webContext= runtime.getInstance(WebContext.class);
        webGlobals= runtime.getInstance(WebGlobals.class);

        ClientConfig config = new ClientConfig();
        client = ClientBuilder.newClient(config);
    }

    // endregion

    @Test
    public void loadsTest() {
    }

    protected Response pathResponse(String path) {
        WebTarget base = client.target(TEST_URL);
        return base.path(path).request().get();
    }

    protected Boolean responseOK(Response response) {
        return Response.Status.OK.getStatusCode() == response.getStatus();
    }

}