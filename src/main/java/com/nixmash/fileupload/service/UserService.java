package com.nixmash.fileupload.service;

import com.google.inject.ImplementedBy;
import com.nixmash.fileupload.dto.CurrentUser;
import com.nixmash.fileupload.dto.Role;
import com.nixmash.fileupload.dto.User;
import org.apache.shiro.subject.Subject;

import java.util.List;

@ImplementedBy(UserServiceImpl.class)
public interface UserService {
    User addUser(User user);
    User getUser(String username);
    List<Role> getRoles(Long userId);
    CurrentUser getCurrentUser(Subject subject);

}
