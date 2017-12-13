package com.epitools.homer.homer.rest.controller;

import com.epitools.homer.homer.model.Contributor;
import com.epitools.homer.homer.repository.ContributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class ContributorRestController {

    @Autowired
    ContributorRepository contributorRepository;

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
}


