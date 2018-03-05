package com.epitools.homer.homer.controller;

import com.epitools.homer.homer.model.Contributor;
import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.repository.ContributorRepository;
import com.epitools.homer.homer.repository.ProjectRepository;
import com.epitools.homer.homer.repository.UserRepository;
import com.epitools.homer.homer.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
public class ContributorController {

    @Autowired
    ContributorRepository contributorRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value="/contributor/project/{id}", method= RequestMethod.GET, produces={ MediaType.TEXT_HTML_VALUE })
    public String byProjectIdId(@PathVariable(value="id") final Integer projectId, final Map<String, Object> model) {
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final Project project = projectRepository.findOne(projectId);
        final User maybeUser = userRepository.findByEmail(user);
        if (maybeUser == null) return "redirect:/project/all";
        if (project == null || !project.getUserId().equals(maybeUser.getId())) {
            model.put("notFound", "Wrong Project Id");
            model.put("project", new Project());
            model.put("user", new User());
        }
        else if (project.getStatus() != 0) {
            model.put("notFound",
                    "Project status does not allow you to edit the project, only a admin can do it");
            model.put("project", new Project());
            model.put("user", new User());
        }
        else {
            model.put("project", project);
            model.put("user", userRepository.findOne(project.getUserId()));
            model.put("contributors",
                    Utils.getProvidedContributors(contributorRepository.findByProjectId(project.getId()),
                            userRepository.findAll()));
        }
        return "contributor/contributor";
    }

    @RequestMapping(value="/contributor/{id}", method=RequestMethod.DELETE, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> deleteContributor(@PathVariable(value="id") Integer contributorId) {
        final User maybeUser = Utils.getMaybeUser(userRepository);
        if (maybeUser == null) return Utils.jsonError("User not connected");
        final Contributor contributor= contributorRepository.findOne(contributorId);
        if (contributor == null) return ResponseEntity.notFound().build();
        final Project project = projectRepository.findOne(contributor.getProjectId());
        if (project == null) return ResponseEntity.notFound().build();
        if (maybeUser.isAdmin().equals(0) && !project.getUserId().equals(maybeUser.getId()))
            return Utils.jsonError("Can not delete contributor on this project");
        if (maybeUser.isAdmin().equals(0) && !project.getStatus().equals(0))
            return Utils.jsonError("Can not delete contributor on this project");
        contributorRepository.delete(contributor);
        return ResponseEntity.ok().build();
    }

}
