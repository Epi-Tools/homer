package com.epitools.homer.homer.rest.controller;

import com.epitools.homer.homer.model.Validation;
import com.epitools.homer.homer.repository.ValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ValidationRestController {

    @Autowired
    ValidationRepository validationRepository;

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

    @RequestMapping(value="/validations/{id}", method=RequestMethod.PUT, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Validation> updateValidation(@PathVariable(value="id") Integer validationId,
                                         @Valid @RequestBody Validation validationDetails) {
        Validation validation = validationRepository.findOne(validationId);
        if(validation == null) return ResponseEntity.notFound().build();
        validation.setUserId(validationDetails.getUserId());
        validation.setProjectId(validationDetails.getProjectId());
        validation.setProjectStatus(validationDetails.getProjectStatus());
        Validation updatedValidation = validationRepository.save(validation);
        return ResponseEntity.ok(updatedValidation);
    }

    @RequestMapping(value="/validations/{id}", method=RequestMethod.DELETE, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Validation> deleteValidation(@PathVariable(value="id") Integer validationId) {
        Validation validation = validationRepository.findOne(validationId);
        if(validation == null) return ResponseEntity.notFound().build();
        validationRepository.delete(validation);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/validations", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public Validation createValidation(@Valid @RequestBody Validation validation) {
        return validationRepository.save(validation);
    }

}
