package com.epitools.homer.homer.controller;

import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.repository.ProjectRepository;
import com.epitools.homer.homer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/admin")
    public String admin(final Map<String, Object> model) {
        model.put("projects", projectRepository.findAll());
        return "admin/admin";
    }

    @RequestMapping(value="/admin/project/{id}", method=RequestMethod.GET, produces={ MediaType.TEXT_HTML_VALUE })
    public String adminProjectEdit(@PathVariable(value="id") final Integer projectId, final Map<String, Object> model) {
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User maybeUser = userRepository.findByEmail(user);
        if (maybeUser == null) return "redirect:/project/all";
        else model.put("user", maybeUser);
        final Project project = projectRepository.findOne(projectId);
        if (project == null) model.put("error", "Wrong Project Id");
        else model.put("project", project);
        return "admin/project";
    }

}
