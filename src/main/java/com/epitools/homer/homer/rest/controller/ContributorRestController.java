package com.epitools.homer.homer.rest.controller;

import com.epitools.homer.homer.model.Contributor;
import com.epitools.homer.homer.model.ContributorValidation;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// TODO(carlendev) check all this route
@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
public class ContributorRestController {

    @Autowired
    ContributorRepository contributorRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value="/contributors", method= RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public List<Contributor> getAllContributors() {
        return contributorRepository.findAll();
    }

    @RequestMapping(value="/contributors/{id}", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Contributor> getContributorById(@PathVariable(value="id") Integer contributorId) {
        Contributor contributor = contributorRepository.findOne(contributorId);
        if(contributor == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(contributor);
    }

    @RequestMapping(value="/contributors/{id}", method=RequestMethod.PUT, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Contributor> updateContributor(@PathVariable(value="id") Integer contributorId,
                                         @Valid @RequestBody Contributor contributorDetails) {
        Contributor contributor = contributorRepository.findOne(contributorId);
        if(contributor == null) return ResponseEntity.notFound().build();
        contributor.setProjectId(contributorDetails.getProjectId());
        Contributor updatedContributor = contributorRepository.save(contributor);
        return ResponseEntity.ok(updatedContributor);
    }

    @RequestMapping(value="/contributors/{id}", method=RequestMethod.DELETE, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Contributor> deleteContributor(@PathVariable(value="id") Integer contributorId) {
        Contributor contributor = contributorRepository.findOne(contributorId);
        if(contributor == null) return ResponseEntity.notFound().build();
        contributorRepository.delete(contributor);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/contributors", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public Contributor createContributor(@Valid @RequestBody Contributor contributor) {
        return contributorRepository.save(contributor);
    }

    @RequestMapping(value="/contributors/project/{id}", method= RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> contributorById(@PathVariable(value="id") final Integer projectId,
                                                  @Valid @RequestBody final ContributorValidation contributor) {
        final Project project = projectRepository.findOne(projectId);
        if (project == null || project.getStatus() != 0) return ResponseEntity.notFound().build();
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final User userE = userRepository.findByEmail(user);
        if (!project.getUserId().equals(userE.getId()))
            return Utils.jsonError("Not your project, you're not allowed to add contributors to this project");
        final User maybeContributor = userRepository.findByEmail(contributor.getEmail());
        if (maybeContributor == null) return Utils.jsonError("User not found");
        if (contributorRepository.findFirstByUserIdAndProjectId(maybeContributor.getId(), project.getId()) != null)
            return Utils.jsonError("Already contributor");
        final Contributor contributorE = new Contributor();
        contributorE.setProjectId(project.getId());
        contributorE.setUserId(maybeContributor.getId());
        contributorRepository.save(contributorE);
        return ResponseEntity.ok().build();
    }
}


