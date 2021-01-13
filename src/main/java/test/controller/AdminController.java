package test.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test.model.User;
import test.service.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserServiceImpl userService;

    @Autowired
    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String allUsers(Model model) {
        List<User> users = userService.allUsers();
        model.addAttribute("usersList", users);
        User loggedInUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        model.addAttribute("loggedInUser", loggedInUser);
        return "admin";
    }

    @PostMapping(value = "/edit")
    public String editUser(@ModelAttribute("user") User user, @RequestParam String role,
                           String password) {
        userService.edit(user, role, password);
        return "redirect:/admin";
    }

    @PostMapping(value = "/add")
    public String addUserByAdmin(@ModelAttribute("user") User user, @RequestParam String role) {
        userService.add(user, role);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        User userFromPage = userService.getById(user.getId());
        userService.delete(userFromPage);
        return "redirect:/admin";
    }

}
