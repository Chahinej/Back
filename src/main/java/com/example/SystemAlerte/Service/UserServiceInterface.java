package com.example.SystemAlerte.Service;

import java.util.List;

import com.example.SystemAlerte.Model.Role;
import com.example.SystemAlerte.Model.User;

public interface UserServiceInterface {
    User SaveUser(User User);

    Role SaveRole(Role Role);

    void AddRoleToUser(String Username, String RoleName);

    User getUser(String username);

    List<User> getUsers();

    User UpdateUser(User user);

    void DeleteUser(Long id);

}
