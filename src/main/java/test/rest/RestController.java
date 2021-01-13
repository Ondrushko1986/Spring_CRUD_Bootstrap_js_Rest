package test.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import test.model.User;
import test.service.UserServiceImpl;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest")
public class RestController {

    private final UserServiceImpl userService;

    @Autowired
    public RestController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/loggedInUser")
    public ResponseEntity<User> loggedInUser() {
        User loggedInUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> allUsers() {
        final List<User> users = userService.allUsers();
        if (users == null || users.isEmpty()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<User> editUser(@RequestBody User user, @RequestParam String role,
                                         String password) {
        userService.edit(user, role, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<User> addUserByAdmin(@RequestBody User user, @RequestParam String role) {
        userService.add(user, role);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<User> deleteUser(@RequestBody User user) {
        User userFromPage = userService.getById(user.getId());
        userService.delete(userFromPage);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
