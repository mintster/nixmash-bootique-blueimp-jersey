package com.nixmash.fileupload.db;


import com.nixmash.fileupload.dto.Role;
import com.nixmash.fileupload.dto.User;

import java.util.List;

public interface UserDb {

    User addUser(User user);
    User getUser(String username);
    List<Role> getRoles(Long userId);
}
