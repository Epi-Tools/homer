package com.epitools.homer.homer.rest.controller;

import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.repository.ProjectRepository;
import com.epitools.homer.homer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// TODO(carlendev) add AddSpices method
@RestController
@RequestMapping("/api")
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

    @RequestMapping(value="/projects/{id}", method=RequestMethod.PUT, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Project> updateProject(@PathVariable(value="id") Integer projectId,
                                           @Valid @RequestBody Project projectDetails) {
        Project project = projectRepository.findOne(projectId);
        if(project == null) return ResponseEntity.notFound().build();
        project.setUserId(projectDetails.getUserId());
        project.setSpices(projectDetails.getSpices());
        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());
        project.setFollowUp(projectDetails.getFollowUp());
        project.setFollowUp1(projectDetails.getFollowUp1());
        project.setDelivery(projectDetails.getDelivery());
        project.setDateFollowUp(projectDetails.getDateFollowUp());
        project.setDateFollowUp1(projectDetails.getDateFollowUp1());
        project.setDateDelivery(projectDetails.getDateDelivery());
        project.setStatus(projectDetails.getStatus());
        Project updatedProject = projectRepository.save(project);
        return ResponseEntity.ok(updatedProject);
    }

    @RequestMapping(value="/projects/{id}", method=RequestMethod.DELETE, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Project> deleteProject(@PathVariable(value="id") Integer projectId) {
        Project project = projectRepository.findOne(projectId);
        if(project == null) return ResponseEntity.notFound().build();
        projectRepository.delete(project);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/projects", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public Project createProject(@Valid @RequestBody Project project) {
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        project.setUserId(userRepository.findByEmail(user).getId());
        project.setStatus(0);
        return projectRepository.save(project);
    }
}
