package test.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import test.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import test.service.UserServiceImpl;

@Controller
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/user")
    public String user(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        model.addAttribute("loggedInUser", loggedInUser);
        return "user";
    }

    @GetMapping(value = "/")
    public String getHomePage() {
        return "login";
    }

}


