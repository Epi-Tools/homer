package com.epitools.homer.homer.controller;

import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.model.Update;
import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.repository.ProjectRepository;
import com.epitools.homer.homer.repository.UserRepository;
import com.epitools.homer.homer.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@Secured("ROLE_ADMIN")
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
        if (project == null) {
            model.put("notFound", "Wrong Project Id");
            model.put("project", new Project());
        }
        else model.put("project", project);
        return "admin/project";
    }

    @RequestMapping(value="api/admin/projects/status", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> udpateStatus(@Valid @RequestBody final Update update) {
        final Project project = projectRepository.findOne(update.getId());
        if (project == null) return Utils.jsonError("Cannot find project");
        project.setStatus(update.getStatus());
        return ResponseEntity.ok(projectRepository.save(project));
    }


}
