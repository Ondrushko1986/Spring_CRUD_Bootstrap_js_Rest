package test.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import test.model.User;
import test.service.UserServiceImpl;


import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest")
public class RestController {

    private final UserServiceImpl userService;
    ObjectMapper mapper = new ObjectMapper();

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

    @GetMapping(value = "admin")
    public ResponseEntity<List<User>> allUsers() {
        final List<User> users = userService.allUsers();
        if (users == null || users.isEmpty()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<User> editUser(@RequestBody String updateUser) throws IOException {
        User userFromRequest = mapper.readValue(updateUser, User.class);
        String userRoles = userFromRequest.getRoles().toString();
        userService.edit(userFromRequest, userRoles, userFromRequest.getPassword());
        return new ResponseEntity<>(userFromRequest,HttpStatus.OK);
    }


    @PostMapping(value = "/add")
    public ResponseEntity<User> addUserByAdmin(@RequestBody String newUser) throws JsonProcessingException {
        User user = mapper.readValue(newUser, User.class);
        String userRoles = user.getRoles().toString();
        userService.add(user, userRoles);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
