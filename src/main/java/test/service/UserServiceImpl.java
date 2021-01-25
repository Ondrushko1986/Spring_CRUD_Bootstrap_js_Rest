package test.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.repository.RoleRepository;
import test.repository.UserRepository;
import test.model.Role;
import test.model.User;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {


    private final UserRepository userDAO;

    private final RoleRepository roleDAO;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userDAO, RoleRepository roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @Override
    public List<User> allUsers() {
        return userDAO.findAll();
    }

    @Override
    public void delete(int id) {
        userDAO.deleteById(id);
    }

    @Override
    public void getById(int id) {
        userDAO.deleteById(id);
    }


    private void newRoleFromAdmin(User user, String role) {
        Set<Role> roles = new HashSet<>();
        if (role.contains("ADMIN")) {
            roles.add(roleDAO.findRoleByRole("ROLE_ADMIN"));
        }
        if (role.contains("USER")) {
            roles.add(roleDAO.findRoleByRole("ROLE_USER"));
        }
        user.setRoles(roles);
    }

    @Override
    public void add(User user, String role) {
        newRoleFromAdmin(user, role);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }


    @Override
    public void edit(User user, String role, String password) {
        newRoleFromAdmin(user, role);
        User userFromDB = userDAO.getById(user.getId());
        if (password == null || password.isEmpty()) {
            user.setPassword(userFromDB.getPassword());
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            user.setName(userFromDB.getName());
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(userFromDB.getPassword());
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            user.setEmail(userFromDB.getEmail());
        }
        userDAO.saveAndFlush(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByName(username);
        if (user != null) {
            return user;
        } else throw new UsernameNotFoundException("User not found");
    }

}