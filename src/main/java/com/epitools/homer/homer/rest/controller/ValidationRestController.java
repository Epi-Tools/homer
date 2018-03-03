package com.epitools.homer.homer.rest.controller;

import com.epitools.homer.homer.model.Bet;
import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.model.Validation;
import com.epitools.homer.homer.repository.BetRepository;
import com.epitools.homer.homer.repository.UserRepository;
import com.epitools.homer.homer.repository.ValidationRepository;
import com.epitools.homer.homer.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
public class ValidationRestController {

    @Autowired
    ValidationRepository validationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BetRepository betRepository;

    @RequestMapping(value="/validations", method= RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public List<Validation> getAllValidations() {
        return validationRepository.findAll();
    }

    @RequestMapping(value="/validations/{id}", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Validation> getValidationById(@PathVariable(value="id") Integer validationId) {
        Validation validation = validationRepository.findOne(validationId);
        if(validation == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(validation);
    }

    // TODO(carlendev) check project status and bet presence
    @Transactional
    @RequestMapping(value="/validations/{id}", method=RequestMethod.PUT, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Validation> updateValidation(@PathVariable(value="id") Integer validationId,
                                         @Valid @RequestBody Validation validationDetails) {
        Validation validation = validationRepository.findOne(validationId);
        if(validation == null) return ResponseEntity.notFound().build();
        validation.setUser(validationDetails.getUser());
        validation.setProject(validationDetails.getProject());
        validation.setStatus(validationDetails.getStatus());
        Validation updatedValidation = validationRepository.save(validation);
        return ResponseEntity.ok(updatedValidation);
    }

    @Transactional
    @RequestMapping(value="/validations/{id}", method=RequestMethod.DELETE, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> deleteValidation(@PathVariable(value="id") Integer validationId) {
        final Validation validation = validationRepository.findOne(validationId);
        if (validation == null) return ResponseEntity.notFound().build();
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final User userE = userRepository.findByEmail(user);
        final Project project = validation.getProject();
        if (userE.isAdmin().equals(0)) return Utils.jsonError("Can not delete this validation");
        if (project.getStatus() < 3) return Utils.jsonError("Can not delete this validation");
        final Bet bet = betRepository.findByUserIdAndProjectId(validation.getUser().getId(), project.getId());
        if (bet == null) return Utils.jsonError("Can not delete this validation, bet not exist");
        betRepository.delete(bet);
        validationRepository.delete(validation);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/validations", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public Validation createValidation(@Valid @RequestBody Validation validation) {
        return validationRepository.save(validation);
    }

}
