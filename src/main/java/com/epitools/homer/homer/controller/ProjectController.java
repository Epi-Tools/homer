package com.epitools.homer.homer.controller;

import com.epitools.homer.homer.model.Project;
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
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/project/new")
    public String create() {
        return "project/new";
    }

    @GetMapping("/project/all")
    public String all(final Map<String, Object> model) {
        model.put("projects", projectRepository.findAll());
        model.put("username",
                userRepository.
                    findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()));
        return "project/all";
    }

    @RequestMapping(value="/project/{id}", method=RequestMethod.GET, produces={ MediaType.TEXT_HTML_VALUE })
    public String byId(@PathVariable(value="id") final Integer projectId, final Map<String, Object> model) {
        Project project = projectRepository.findOne(projectId);
        if(project == null) model.put("error", "Wrong Project Id");
        else model.put("project", project);
        return "project/project";
    }
}
