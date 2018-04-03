package com.nixmash.fileupload.auth;

import com.nixmash.fileupload.core.TestDbBase;
import com.nixmash.fileupload.dto.CurrentUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@SuppressWarnings("FieldCanBeLocal")
@RunWith(JUnit4.class)
public class ShiroTest extends TestDbBase {

     private final String ADMIN_ROLE = "admin";
     private final String USER_ROLE = "user";
     private final String ADMIN_PERMISSION = "nixmash:all";
     private final String USER_PERMISSION = "nixmash:view";

    @BeforeClass
    public static void setupSecurityManager() {
        DefaultSecurityManager sm = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(sm);
        ThreadContext.bind(sm);
    }

    @Test
    public void testBobLogin() {
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        subject.login(new UsernamePasswordToken("bob", "password"));
        subject.checkPermission(ADMIN_PERMISSION);
        subject.checkPermission(USER_PERMISSION);
        subject.checkRole(ADMIN_ROLE);
        subject.checkRole(USER_ROLE);
        subject.logout();
    }

    @Test
    public void testKenLogin() {
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        subject.login(new UsernamePasswordToken("ken", "halo"));
        subject.checkPermission(USER_PERMISSION);
        subject.checkRole(USER_ROLE);
        subject.logout();
    }

    @Test(expected = IncorrectCredentialsException.class)
    public void testBadPasswordLogin() {
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        subject.login(new UsernamePasswordToken("bob", "badpassword"));
        subject.logout();
    }

    @Test(expected = UnauthorizedException.class)
    public void testNotFoundPermission() {
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        subject.login(new UsernamePasswordToken("bob", "password"));
        subject.checkPermission("bad:permission");
        subject.logout();
    }

    @Test
    public void currentUserTest() throws Exception {
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        subject.login(new UsernamePasswordToken("bob", "password"));
        CurrentUser currentUser = userService.createCurrentUser(subject);
        Assert.assertTrue((boolean) currentUser.getAdministrator());
    }
}
