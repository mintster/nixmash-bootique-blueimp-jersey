/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.nixmash.fileupload.controller;

import com.google.inject.Inject;
import com.nixmash.fileupload.core.WebUI;
import com.nixmash.fileupload.resolvers.TemplatePathResolver;
import com.nixmash.fileupload.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;

@Path("/login")
public class LoginController {

    private static transient final Logger log = LoggerFactory.getLogger(LoginController.class);

    private static final String LOGIN_PAGE = "login";
    private static final String CURRENT_USER = "CurrentUser";

    private UserService userService;
    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;

    @Inject
    public LoginController(TemplatePathResolver templatePathResolver,
                           WebUI webUI, UserService userService) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
        this.userService = userService;
    }

    @GET
    public String loginPage() {
        Map<String, Object> model = new HashMap<>();
        model.put("pageinfo", webUI.getPageInfo(LOGIN_PAGE));
        return templatePathResolver.populateTemplate("login.html", model);
    }

    @POST
    public String onSubmit(@FormParam("username") String username,
                           @FormParam("password") String password,
                           @FormParam("rememberMe") Boolean rememberMe) throws Exception {

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        Map<String, Object> model = new HashMap<>();
        try {
            token.setRememberMe(rememberMe);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            subject.getSession().setAttribute(CURRENT_USER, userService.getCurrentUser(subject));
        } catch (AuthenticationException e) {
            log.debug("Error authenticating.", e);
            model.put("pageinfo", webUI.getPageInfo(LOGIN_PAGE));
            model.put("error", "error");
            return templatePathResolver.populateTemplate("login.html", model);
        }

        Session session = SecurityUtils.getSubject().getSession();
        if (SecurityUtils.getSubject().getPrincipals() != null) {
            Subject subject = SecurityUtils.getSubject();
            model.put("redirectUrl", getRedirectUrl(subject));
            model.put("user", session.getAttribute(CURRENT_USER));
        }
        return templatePathResolver.populateTemplate("redirect.html", model);
    }

    private String getRedirectUrl(Subject subject) {
        SavedRequest request = WebUtils.getSavedRequest(null);
        if (request != null) {
            String queryParam = StringUtils.substringAfterLast(request.getRequestUrl(), "/");
            if (queryParam.equals("admin") && !subject.hasRole("admin"))
                queryParam = "users";
            return "/downloads?msg=" + queryParam;
        }
        return "/";
    }
}