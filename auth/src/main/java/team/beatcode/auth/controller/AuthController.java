package team.beatcode.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    /**
     *
     * @return
     */
    @RequestMapping("/CheckAuth")
    public String checkAuth() {
        // TODO
        return null;
    }

    @RequestMapping("/Login")
    public String login() {
        // TODO
        return null;
    }

    @RequestMapping("/Logout")
    public String logout() {
        // TODO
        return null;
    }

    @RequestMapping("/Register")
    public String register() {
        // TODO
        return null;
    }
}
