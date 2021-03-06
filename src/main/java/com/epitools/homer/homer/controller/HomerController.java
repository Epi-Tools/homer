package com.epitools.homer.homer.controller;

import com.epitools.homer.homer.repository.ProjectRepository;
import com.epitools.homer.homer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class HomerController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    private void getProjects(final Map<String, Object> model) {
        model.put("projects", projectRepository.findByStatusNotOrderByIdDesc(7));
    }

    @GetMapping("/")
    public String defaultPage(final Map<String, Object> model) {
        getProjects(model);
        return "home";
    }

    @GetMapping("/home")
    public String home(final Map<String, Object> model) {
        getProjects(model);
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

}
