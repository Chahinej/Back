package com.example.SystemAlerte.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SystemAlerte.Model.Role;
import com.example.SystemAlerte.Model.User;
import com.example.SystemAlerte.Repository.RoleRepository;
import com.example.SystemAlerte.Repository.UserRepository;
import com.example.SystemAlerte.Security.PasswordConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserServiceInterface, UserDetailsService {

    private final UserRepository repU;

    private final RoleRepository repR;

    private final PasswordConfig passwordConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repU.findByUsername(username);

        if (user == null) {
            log.error("User Not Found In The DataBase");
            throw new UsernameNotFoundException("User Not Found In The DataBase");
        } else {
            log.info("User Found In The DataBase : {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
                true, true, true,
                authorities);
    }

    @Override
    public User SaveUser(User User) {
        log.info("Saving new User {} to the database", User.getUsername());
        User.setPassword(passwordConfig.passwordEncoder().encode(User.getPassword()));
        return repU.save(User);
    }

    @Override
    public Role SaveRole(Role Role) {
        log.info("Saving new Role {} to the database", Role.getName());
        return repR.save(Role);
    }

    @Override
    public void AddRoleToUser(String Username, String RoleName) {
        log.info("Adding role  {} to User {}", RoleName, Username);
        User user = repU.findByUsername(Username);
        Role role = repR.findByName(RoleName);

        user.getRoles().add(role);

    }

    @Override
    public User getUser(String username) {
        log.info("fetching user{}", username);

        return repU.findByUsername(username);

    }

    @Override
    public List<User> getUsers() {
        log.info("fetching all users");
        return repU.findAll();
    }

    @Override
    public User UpdateUser(User user) {
        Optional<User> userfct = repU.findById(user.getId());
        log.info("User updated : {}", user.getUsername());

        user.setId(userfct.get().getId());
        return repU.save(user);
    }

    @Override
    public void DeleteUser(Long  id) {
        User user = new User();
        user = repU.findById(id).get();
        log.info("User deleted : {}", user.getUsername());
        repU.deleteById(id);

    }

}
