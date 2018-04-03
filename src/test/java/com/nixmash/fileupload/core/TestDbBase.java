package com.nixmash.fileupload.core;

import com.nixmash.fileupload.auth.NixmashRealm;
import com.nixmash.fileupload.db.UserDb;
import com.nixmash.fileupload.db.UserDbImpl;
import com.nixmash.fileupload.service.UserService;
import com.nixmash.fileupload.service.UserServiceImpl;
import io.bootique.BQRuntime;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.shiro.ShiroModule;
import io.bootique.test.junit.BQTestFactory;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.client.Client;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@RunWith(JUnit4.class)
public class TestDbBase {

    public static final String TEST_URL = "http://127.0.1.1:8001";
    private static final String YAML_CONFIG = "--config=classpath:test.yml";

    @ClassRule
    public static BQTestFactory testFactory = new BQTestFactory();

    private Client client;

    protected static UserService userService;

    protected static BQRuntime runtime;
    // region @BeforeClass and @AfterClass

    @BeforeClass
    public static void setupClass() throws IOException, SQLException {

        runtime = testFactory.app()
                .autoLoadModules()
                .args("-s","--config=classpath:test.yml")
                .module(b -> b.bind(WebUI.class))
                .module(b -> b.bind(UserService.class).to(UserServiceImpl.class))
                .module(b -> b.bind(UserDb.class).to(UserDbImpl.class))
                .module(b -> ShiroModule.extend(b).addRealm(NixmashRealm.class))
                .createRuntime();

        userService = runtime.getInstance(UserService.class);

        DataSourceFactory datasource = runtime.getInstance(DataSourceFactory.class);
        WebConfig webConfig = runtime.getInstance(WebConfig.class);
        Connection connection = datasource.forName(webConfig.datasourceName).getConnection();

        String sql = webConfig.configPath + "/populate.sql";
        if (webConfig.datasourceName.equals("MySqlTest")) {
            try (Statement statement = connection.createStatement()) {
                File script = new File(sql);
                ScriptRunner sr = new ScriptRunner(connection);
                sr.setLogWriter(null);
                Reader reader = new BufferedReader(new FileReader(script));
                sr.runScript(reader);
                reader.close();
            }
        }

    }

    // endregion

    @Test
    public void loadsTest() {
    }

}