package com.epitools.homer.homer.controller;

import com.epitools.homer.homer.model.Bet;
import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.repository.BetRepository;
import com.epitools.homer.homer.repository.ProjectRepository;
import com.epitools.homer.homer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class BetController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BetRepository betRepository;

    // TODO: check redirection if user is not connected
    @RequestMapping(value="/bet/project/{id}", method= RequestMethod.GET, produces={ MediaType.TEXT_HTML_VALUE })
    public String byId(@PathVariable(value="id") final Integer projectId, final Map<String, Object> model) {
        final Project project = projectRepository.findOne(projectId);
        if (project == null) {
            model.put("notFound", "Project not found");
            model.put("project", new Project());
        } else model.put("project", project);
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        model.put("user", userRepository.findByEmail(user));
        final List<Bet> bets = betRepository.findByProjectId(projectId);
        model.put("bets", bets);
        return "bet/project";
    }

}
