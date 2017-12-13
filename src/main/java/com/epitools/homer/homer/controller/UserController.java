package com.epitools.homer.homer.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class UserController {

    @GetMapping("/user")
    public String user() {
        return "user";
    }

}
