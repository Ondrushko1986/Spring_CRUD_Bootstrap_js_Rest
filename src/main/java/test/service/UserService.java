package test.service;

import test.model.User;

import java.util.List;


public interface UserService {

    List<User> allUsers();

    void add(User user, String role);

    void delete(User user);

    void edit(User user, String role, String password);

    User getById(int id);





}