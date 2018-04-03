package com.nixmash.fileupload.core;

import com.nixmash.fileupload.auth.NixmashRealm;
import com.nixmash.fileupload.db.UserDb;
import com.nixmash.fileupload.db.UserDbImpl;
import com.nixmash.fileupload.service.UserService;
import com.nixmash.fileupload.service.UserServiceImpl;
import io.bootique.BQRuntime;
import io.bootique.shiro.ShiroModule;
import io.bootique.test.junit.BQTestFactory;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.client.Client;
import java.io.IOException;

@RunWith(JUnit4.class)
public class TestBase {

    public static final String TEST_URL = "http://127.0.1.1:8001";
    private static final String YAML_CONFIG = "--config=classpath:test.yml";

    @ClassRule
    public static BQTestFactory testFactory = new BQTestFactory();

    private Client client;

    protected static WebUI webUI;
    protected static WebContext webContext;
    protected static WebGlobals webGlobals;

// region @BeforeClass and @AfterClass

    @BeforeClass
    public static void setupClass() throws IOException {

        BQRuntime runtime = testFactory.app()
                .autoLoadModules()
                .args("-s","--config=classpath:test.yml")
                .module(b -> b.bind(WebUI.class))
                .module(b -> b.bind(UserService.class).to(UserServiceImpl.class))
                .module(b -> b.bind(UserDb.class).to(UserDbImpl.class))
                .module(b -> ShiroModule.extend(b).addRealm(NixmashRealm.class))
                .createRuntime();

        webUI= runtime.getInstance(WebUI.class);
        webContext= runtime.getInstance(WebContext.class);
        webGlobals= runtime.getInstance(WebGlobals.class);
    }

    // endregion

    @Test
    public void loadsTest() {
    }


}