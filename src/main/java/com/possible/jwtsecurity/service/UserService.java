package com.possible.jwtsecurity.service;

import com.possible.jwtsecurity.model.AppUser;
import com.possible.jwtsecurity.model.Role;
import org.springframework.data.domain.Page;

public interface UserService {
    AppUser saveUser(AppUser user);
    AppUser getUser(String username) throws Throwable;
    Page<AppUser> getUsers();
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);

}
