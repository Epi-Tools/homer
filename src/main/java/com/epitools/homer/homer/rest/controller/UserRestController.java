package com.epitools.homer.homer.rest.controller;

import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// TODO check admin permission
@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/users", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping(value="/users/{id}", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<User> getUserById(@PathVariable(value="id") Integer userId) {
        User user = userRepository.findOne(userId);
        if(user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(user);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/users/{id}", method=RequestMethod.PUT, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<User> updateUser(@PathVariable(value="id") Integer userId,
                                           @Valid @RequestBody User userDetails) {
        User user = userRepository.findOne(userId);
        if(user == null) return ResponseEntity.notFound().build();
        user.setEmail(userDetails.getEmail());
        user.setAdmin(userDetails.isAdmin());
        user.setSpices(userDetails.getSpices());
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/users/{id}", method=RequestMethod.DELETE, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<User> deleteUser(@PathVariable(value="id") Integer userId) {
        User user = userRepository.findOne(userId);
        if(user == null) return ResponseEntity.notFound().build();
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/users", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User maybeUser = userRepository.findByEmail(user.getEmail());
        if (maybeUser != null) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{ \"error\" : \"User already exist\" }");
        return ResponseEntity.ok(userRepository.save(user));
    }

    @RequestMapping(value="/users/current", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> currentUser() {
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User maybeUser = userRepository.findByEmail(user);
        if (maybeUser == null) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{ \"error\" : \"User not connected\" }");
        return ResponseEntity.ok(maybeUser);
    }
}
