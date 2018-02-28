package com.epitools.homer.homer.controller;

import com.epitools.homer.homer.model.BetProvider;
import com.epitools.homer.homer.model.ContributorProvider;
import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.repository.BetRepository;
import com.epitools.homer.homer.repository.ContributorRepository;
import com.epitools.homer.homer.repository.ProjectRepository;
import com.epitools.homer.homer.repository.UserRepository;
import com.epitools.homer.homer.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO(carlendev) add 404 custom
// TODO(carlendev) remove devtools
// TODO(carlendev) check if project is on current user
// TODO(carlendev) reverse project list
@Controller
@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BetRepository betRepository;

    @Autowired
    ContributorRepository contributorRepository;

    @GetMapping("/project/new")
    public String create() {
        return "project/new";
    }

    @GetMapping("/project/all")
    public String all(final Map<String, Object> model) {
        final List<Project> projects = projectRepository.findByStatusNotOrderByIdDesc(7);
        final List<Project> finishProjects = projectRepository.findByStatusOrderByIdDesc(7);
        final List<User> users = new ArrayList<>();
        final List<User> usersFinish = new ArrayList<>();
        for (Project project : projects) users.add(userRepository.findOne(project.getUserId()));
        for (Project project : finishProjects) usersFinish.add(userRepository.findOne(project.getUserId()));
        model.put("projects", projects);
        model.put("users", users);
        model.put("finishProjects", finishProjects);
        model.put("usersFinish", usersFinish);
        return "project/all";
    }

    @GetMapping("/project/my")
    public String my(final Map<String, Object> model) {
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User maybeUser = userRepository.findByEmail(user);
        if (maybeUser == null) return "redirect:/project/all";
        final List<Project> projects = projectRepository
                .findByUserIdAndStatusNotOrderByIdDesc(maybeUser.getId(), 7);
        final List<Project> finishProjects = projectRepository
                .findByUserIdAndStatusOrderByIdDesc(maybeUser.getId(), 7);
        model.put("projects", projects);
        model.put("finishProjects", finishProjects);
        model.put("username", maybeUser.getEmail());
        return "project/my";
    }

    @RequestMapping(value="/project/{id}", method=RequestMethod.GET, produces={ MediaType.TEXT_HTML_VALUE })
    public String byId(@PathVariable(value="id") final Integer projectId, final Map<String, Object> model) {
        final Project project = projectRepository.findOne(projectId);
        if (project == null) {
            model.put("notFound", "Wrong Project Id");
            model.put("project", new Project());
            model.put("bets", new ArrayList<BetProvider>());
            model.put("contributors", new ArrayList<ContributorProvider>());
        }
        else {
            final List<User> userList = userRepository.findAll();
            model.put("project", project);
            model.put("user", userRepository.findOne(project.getUserId()));
            model.put("bets", Utils.
                    getProvidedBets(betRepository.findByProjectId(project.getId()), userList));
            model.put("contributors", Utils.
                    getProvidedContributors(contributorRepository.findByProjectId(projectId), userList));
        }
        return "project/project";
    }

    @RequestMapping(value="/project/edit/{id}", method=RequestMethod.GET, produces={ MediaType.TEXT_HTML_VALUE })
    public String edit(@PathVariable(value="id") final Integer projectId, final Map<String, Object> model) {
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final Project project = projectRepository.findOne(projectId);
        final User maybeUser = userRepository.findByEmail(user);
        if (maybeUser == null) return "redirect:/project/all";
        else if (project == null || !project.getUserId().equals(maybeUser.getId())) {
            model.put("notFound", "Wrong Project Id");
            model.put("project", new Project());
        }
        else if (project.getStatus() != 0) {
            model.put("notFound",
                    "Project status does not allow you to edit the project, only a admin can do it");
            model.put("project", new Project());
        }
        else model.put("project", project);
        return "project/edit";
    }
}
