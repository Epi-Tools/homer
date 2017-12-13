package com.epitools.homer.homer.rest.controller;

import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.repository.ProjectRepository;
import com.epitools.homer.homer.repository.UserRepository;
import com.epitools.homer.homer.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// TODO(carlendev) add AddSpices method
// TODO(carlendev) refactor check if userid et project userid match
// TODO(carlendev) check bootstrap not found error
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class ProjectRestController {
    
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value="/projects", method= RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @RequestMapping(value="/projects/{id}", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Project> getProjectById(@PathVariable(value="id") Integer projectId) {
        Project project = projectRepository.findOne(projectId);
        if(project == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(project);
    }

    @RequestMapping(value="/projects/my", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> getMyProject() {
        final User maybeUser = Utils.getMaybeUser(userRepository);
        if (maybeUser == null) return Utils.jsonError("User not connected");
        List<Project> projects = projectRepository.findByUserId(maybeUser.getId());
        if(projects == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(projects);
    }

    @RequestMapping(value="/projects/{id}", method=RequestMethod.PUT, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> updateProject(@PathVariable(value="id") Integer projectId,
                                           @Valid @RequestBody Project projectDetails) {
        final User maybeUser = Utils.getMaybeUser(userRepository);
        if (maybeUser == null) return Utils.jsonError("User not connected");
        final Project project = projectRepository.findOne(projectId);
        if(project == null) return ResponseEntity.notFound().build();
        if (!project.getUserId().equals(maybeUser.getId())) return Utils.jsonError("Can not edit this project");
        project.setUserId(projectDetails.getUserId() == null ? project.getUserId() : projectDetails.getUserId());
        project.setSpices(projectDetails.getSpices());
        project.setCurrentSpices(projectDetails.getCurrentSpices() == null ?
                project.getCurrentSpices() : projectDetails.getCurrentSpices());
        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());
        project.setFollowUp(projectDetails.getFollowUp());
        project.setFollowUp1(projectDetails.getFollowUp1());
        project.setDelivery(projectDetails.getDelivery());
        project.setDateFollowUp(projectDetails.getDateFollowUp());
        project.setDateFollowUp1(projectDetails.getDateFollowUp1());
        project.setDateDelivery(projectDetails.getDateDelivery());
        project.setStatus(projectDetails.getStatus() == null ? project.getStatus() : projectDetails.getStatus());
        Project updatedProject = projectRepository.save(project);
        return ResponseEntity.ok(updatedProject);
    }

    // TODO: make check on project status
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @RequestMapping(value="/projects/{id}", method=RequestMethod.DELETE, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> deleteProject(@PathVariable(value="id") Integer projectId) {
        final User maybeUser = Utils.getMaybeUser(userRepository);
        if (maybeUser == null) return Utils.jsonError("User not connected");
        final Project project = projectRepository.findOne(projectId);
        if(project == null) return ResponseEntity.notFound().build();
        if (!project.getUserId().equals(maybeUser.getId())) return Utils.jsonError("Can not delete this project");
        projectRepository.delete(project);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/projects", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public Project createProject(@Valid @RequestBody Project project) {
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        project.setUserId(userRepository.findByEmail(user).getId());
        project.setCurrentSpices(0);
        project.setStatus(0);
        return projectRepository.save(project);
    }
}