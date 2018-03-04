package com.epitools.homer.homer.controller;

import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.model.Validation;
import com.epitools.homer.homer.repository.ProjectRepository;
import com.epitools.homer.homer.repository.UserRepository;
import com.epitools.homer.homer.repository.ValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
public class ValidationController {

    @Autowired
    ValidationRepository validationRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/validation/project/{id}", method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE})
    public String byId(@PathVariable(value="id") final Integer projectId, final Map<String, Object> model) {
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final User maybeUser = userRepository.findByEmail(user);
        if (maybeUser == null) return "redirect:/project/all";
        final Project project = projectRepository.findOne(projectId);
        if (project == null) {
            model.put("notFound", "Wrong Project Id");
            model.put("project", new Project());
            model.put("user", new User());
            model.put("validations", new ArrayList<ArrayList<Validation>>());
        }
        else {
            model.put("project", project);
            model.put("user", userRepository.findOne(project.getUserId()));
            model.put("canValidate", validationRepository.
                    findByUserAndProjectAndStatusAndValidNot(maybeUser, project, project.getStatus(), true) != null);
            final ArrayList<List<Validation>> validationListList = new ArrayList<>();
            validationListList.add(validationRepository.findByProjectAndStatus(project, 3));
            validationListList.add(validationRepository.findByProjectAndStatus(project, 4));
            validationListList.add(validationRepository.findByProjectAndStatus(project, 5));
            model.put("validations", validationListList);
        }
        return "validation/validation";
    }

}
