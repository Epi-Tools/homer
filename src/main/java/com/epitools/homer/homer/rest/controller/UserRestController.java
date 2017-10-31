package com.epitools.homer.homer.rest.controller;

import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value="/users", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping(value="/users", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) {
        User user = userRepository.findOne(userId);
        if(user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(user);
    }

    @RequestMapping(value="/users/{id}", method=RequestMethod.PUT, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<User> updateUser(@PathVariable(value="id") Long userId,
                                           @Valid @RequestBody User userDetails) {
        User user = userRepository.findOne(userId);
        if(user == null) return ResponseEntity.notFound().build();
        user.setEmail(userDetails.getEmail());
        user.setAdmin(userDetails.isAdmin());
        user.setSpices(userDetails.getSpices());
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }



}
