package com.epitools.homer.homer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectController {

    @GetMapping("/project/new")
    public String create() {
        return "project/new";
    }

    @GetMapping("/project/all")
    public String all() {
        return "project/all";
    }
}
