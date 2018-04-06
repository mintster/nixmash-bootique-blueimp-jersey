package com.nixmash.fileupload;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.fileupload.auth.NixmashRealm;
import com.nixmash.fileupload.auth.NixmashRoleFilter;
import com.nixmash.fileupload.controller.GeneralController;
import com.nixmash.fileupload.db.FileDb;
import com.nixmash.fileupload.db.FileDbImpl;
import com.nixmash.fileupload.db.UserDb;
import com.nixmash.fileupload.db.UserDbImpl;
import com.nixmash.fileupload.service.FileService;
import com.nixmash.fileupload.service.FileServiceImpl;
import com.nixmash.fileupload.service.UserService;
import com.nixmash.fileupload.service.UserServiceImpl;
import io.bootique.Bootique;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.JettyModule;
import io.bootique.shiro.ShiroModule;
import io.bootique.shiro.web.ShiroWebModule;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher implements Module {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {

        Bootique
                .app(args)
                .autoLoadModules()
                .module(Launcher.class)
                .args("--server", "--config=classpath:bootique.yml")
                .exec();
    }

    @Override
    public void configure(Binder binder) {

        binder.bind(UserService.class).to(UserServiceImpl.class);
        binder.bind(UserDb.class).to(UserDbImpl.class);

        binder.bind(FileService.class).to(FileServiceImpl.class);
        binder.bind(FileDb.class).to(FileDbImpl.class);

        Package pkg = GeneralController.class.getPackage();
        JerseyModule.extend(binder).addPackage(pkg).addFeature(MultiPartFeature.class);

        JettyModule.extend(binder).addStaticServlet("s1", "/css/*", "/img/*", "/js/*", "/fonts/*");
        ShiroModule.extend(binder).addRealm(NixmashRealm.class);
        ShiroWebModule.extend(binder).setFilter("roles", NixmashRoleFilter.class);
    }


}
