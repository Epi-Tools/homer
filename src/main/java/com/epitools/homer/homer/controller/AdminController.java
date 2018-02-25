package com.epitools.homer.homer.controller;

import com.epitools.homer.homer.model.BetProvider;
import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.model.Update;
import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.repository.BetRepository;
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
import java.util.List;
import java.util.Map;

@Controller
@Secured("ROLE_ADMIN")
public class AdminController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BetRepository betRepository;

    @GetMapping("/admin")
    public String admin(final Map<String, Object> model) {
        model.put("projects", projectRepository.findAllByOrderByIdDesc());
        return "admin/admin";
    }

    @GetMapping("/admin/spices")
    public String adminSpices(final Map<String, Object> model) {
        model.put("users", userRepository.findAll());
        return "admin/spices";
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
            model.put("isDone", true);
        }
        else {
            model.put("project", project);
            model.put("isDone", project.getStatus().equals(7));
        }
        return "admin/project";
    }

    // TODO set by contributor divide spice for project
    @RequestMapping(value="api/admin/projects/status", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> updateStatus(@Valid @RequestBody final Update update) {
        final Project project = projectRepository.findOne(update.getId());
        if (project == null) return Utils.jsonError("Cannot find project");
        // INFO NOT REDONE PROJECT
        if (project.getStatus().equals(7)) return ResponseEntity.ok(projectRepository.save(project));
        project.setStatus(update.getStatus());
        if (project.getStatus().equals(6)) {
            final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            final User maybeUser = userRepository.findByEmail(user);
            if (maybeUser == null) return Utils.jsonError("Cannot find project");
            maybeUser.setSpices(maybeUser.getSpices() + ((project.getSpices() * 2)));
            final List<BetProvider> betProviders = Utils.
                    getProvidedBets(betRepository.findByProjectId(project.getId()), userRepository.findAll());
            betProviders.forEach(e -> userRepository.findOne(e.getUserId())
                    .setSpices(userRepository.findOne(e.getUserId()).getSpices() + e.getSpices() * 2));
            project.setStatus(7);
        }
        return ResponseEntity.ok(projectRepository.save(project));
    }

}
