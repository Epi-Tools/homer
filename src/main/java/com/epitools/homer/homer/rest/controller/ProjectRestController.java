package com.epitools.homer.homer.rest.controller;

import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

public class ProjectRestController {
    
    @Autowired
    ProjectRepository projectRepository;

    @RequestMapping(value="/projects", method= RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @RequestMapping(value="/projects/{id}", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Project> getProjectById(@PathVariable(value="id") Long projectId) {
        Project project = projectRepository.findOne(projectId);
        if(project == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(project);
    }

    @RequestMapping(value="/projects/{id}", method=RequestMethod.PUT, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Project> updateProject(@PathVariable(value="id") Long projectId,
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
        Project updatedProject = projectRepository.save(project);
        return ResponseEntity.ok(updatedProject);
    }

    @RequestMapping(value="/projects/{id}", method=RequestMethod.DELETE, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Project> deleteProject(@PathVariable(value="id") Long projectId) {
        Project project = projectRepository.findOne(projectId);
        if(project == null) return ResponseEntity.notFound().build();
        projectRepository.delete(project);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/projects", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public Project createProject(@Valid @RequestBody Project project) {
        return projectRepository.save(project);
    }
}
