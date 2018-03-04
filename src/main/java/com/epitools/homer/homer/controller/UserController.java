package com.epitools.homer.homer.controller;

import com.epitools.homer.homer.model.Bet;
import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.model.Validation;
import com.epitools.homer.homer.repository.BetRepository;
import com.epitools.homer.homer.repository.ProjectRepository;
import com.epitools.homer.homer.repository.UserRepository;
import com.epitools.homer.homer.repository.ValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BetRepository betRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ValidationRepository validationRepository;

    @GetMapping("/user")
    public String user(final Map<String, Object> model) {
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final User userE = userRepository.findByEmail(user);
        final List<Bet> betLists = betRepository.findByUserId(userE.getId());
        final List<Project> projects = new ArrayList<>();
        final List<Project> myProjects = projectRepository
                .findByUserIdAndStatusNotOrderByIdDesc(userE.getId(), 7);
        final List<Project> myFinishProjects = projectRepository
                .findByUserIdAndStatusOrderByIdDesc(userE.getId(), 7);
        final List<Validation> validationList = validationRepository.findByUserAndValidNot(userE, true);
        betLists.forEach(e -> projects.add(projectRepository.findOne(e.getProjectId())));
        model.put("projects", projects);
        model.put("myProjects", myProjects);
        model.put("myFinishProjects", myFinishProjects);
        model.put("bets", betLists);
        model.put("validations", validationList);
        model.put("user", userE);
        return "user/user";
    }

}
