package com.epitools.homer.homer.controller;

import com.epitools.homer.homer.model.*;
import com.epitools.homer.homer.repository.*;
import com.epitools.homer.homer.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Secured("ROLE_ADMIN")
public class AdminController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BetRepository betRepository;

    @Autowired
    ContributorRepository contributorRepository;

    @Autowired
    ValidationRepository validationRepository;

    @GetMapping("/admin")
    public String admin(final Map<String, Object> model) {
        model.put("projects", projectRepository.findByStatusNotOrderByIdDesc(7));
        model.put("finishProjects", projectRepository.findByStatusOrderByIdDesc(7));
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
            model.put("bets", new ArrayList<BetProvider>());
            model.put("contributors", new ArrayList<ContributorProvider>());
            model.put("validations", new ArrayList<Validation>());
        }
        else {
            final List<User> userList = userRepository.findAll();
            model.put("project", project);
            model.put("isDone", project.getStatus().equals(7));
            model.put("user", userRepository.findOne(project.getUserId()));
            model.put("bets", Utils.
                    getProvidedBets(betRepository.findByProjectId(project.getId()), userList));
            model.put("contributors", Utils.
                    getProvidedContributors(contributorRepository.findByProjectId(projectId), userList));
            model.put("validations", validationRepository.findByProjectIdOrderByIdDesc(project.getId()));
        }
        return "admin/project";
    }

    // TODO(carlendev) set by contributor divide spice for project
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
            final Integer contributorsNumber = contributorRepository.countAllByProjectId(project.getId());
            final List<Contributor> contributorList = contributorRepository.findByProjectId(project.getId());
            final Integer spicesFinal = ((project.getSpices() * 2) / (contributorsNumber + 1));
            maybeUser.setSpices(maybeUser.getSpices() + spicesFinal);
            contributorList.forEach(e -> {
                final User cUser = userRepository.findOne(e.getUserId());
                cUser.setSpices(cUser.getSpices() + spicesFinal);
            });
            final List<BetProvider> betProviders = Utils.
                    getProvidedBets(betRepository.findByProjectId(project.getId()), userRepository.findAll());
            betProviders.forEach(e -> userRepository.findOne(e.getUserId())
                    .setSpices(userRepository.findOne(e.getUserId()).getSpices() + e.getSpices() * 2));
            project.setStatus(7);
            validationRepository.removeByProjectId(project.getId());
            betRepository.removeByProjectId(project.getId());
        }
        else if (project.getStatus() >= 3 && project.getStatus() <= 5) {
            final Integer status = project.getStatus();
            final Integer count = validationRepository.countByProjectAndStatus(project, status);
            if (count.equals(0)) {
                final List<Bet> betList = betRepository.findByProjectId(project.getId());
                final List<Validation> validationsList = betList.stream().map(e -> {
                    final Validation validation = new Validation();
                    validation.setProject(projectRepository.findOne(e.getProjectId()));
                    validation.setUser(userRepository.findOne(e.getUserId()));
                    validation.setStatus(status);
                    return validation;
                }).collect(Collectors.toList());
                validationRepository.save(validationsList);
            }
        }
        return ResponseEntity.ok(projectRepository.save(project));
    }

}
