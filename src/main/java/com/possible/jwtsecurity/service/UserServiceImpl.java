package com.possible.jwtsecurity.service;

import com.possible.jwtsecurity.model.AppUser;
import com.possible.jwtsecurity.model.Role;
import org.springframework.data.domain.Page;

public class UserServiceImpl implements UserService {
    @Override
    public AppUser saveUser(AppUser user) {
        return null;
    }

    @Override
    public AppUser getUser(String username) {
        return null;
    }

    @Override
    public Page<AppUser> getUsers() {
        return null;
    }

    @Override
    public Role saveRole(Role role) {
        return null;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {

    }
}
