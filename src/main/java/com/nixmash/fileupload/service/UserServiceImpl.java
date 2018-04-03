package com.nixmash.fileupload.service;

import com.google.inject.Inject;
import com.nixmash.fileupload.db.UserDb;
import com.nixmash.fileupload.dto.CurrentUser;
import com.nixmash.fileupload.dto.Role;
import com.nixmash.fileupload.dto.SocialUser;
import com.nixmash.fileupload.dto.User;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserServiceImpl implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public static final String CURRENT_USER = "CurrentUser";
    public static final String SOCIAL_USER = "SocialUser";

    private UserDb userDb;

    @Inject
    public UserServiceImpl(UserDb userDb) {
        this.userDb = userDb;
    }

    // region users

    @Override
    public User getUser(String username) {
        return userDb.getUser(username);
    }

    @Override
    public User addUser(User user) {
        user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        logger.info("Adding User with email: " + user.getEmail() + " hashedPassword: " +user.getPassword());
        return userDb.addUser(user);
    }

    @Override
    public List<Role> getRoles(Long userId) {
        return userDb.getRoles(userId);
    }

    // endregion

    // region CurrentUser

    @Override
    public CurrentUser createCurrentUser(Subject subject) {
        User user = this.getUser(subject.getPrincipals().toString());
        CurrentUser currentUser = new CurrentUser(user);
        List<Role> roles = this.getRoles(user.getUserId());

        for (Role role : roles) {
            if (role.getRoleName().equals("admin")) {
                currentUser.setAdministrator(true);
            }
            currentUser.getRoles().add(role.getRoleName());
            currentUser.getPermissions().add(role.getPermission());
        }
        return currentUser;
    }

    // endregion

    // region Social Users

    @Override
    public SocialUser addSocialUser(SocialUser user) {
        logger.info("Adding Social User with screen name: " + user.getScreenName());
        return userDb.addSocialUser(user);
    }

    @Override
    public SocialUser getSocialUser(String username) {
        return userDb.getSocialUser(username);
    }

    @Override
    public SocialUser getSocialUser(String accessToken, String secret) {
        SocialUser socialUser = userDb.getSocialUserByAccessToken(accessToken, secret);
        return socialUser;
    }
    // endregion
}
