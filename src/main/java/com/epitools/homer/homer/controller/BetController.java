package com.epitools.homer.homer.controller;

import com.epitools.homer.homer.model.*;
import com.epitools.homer.homer.repository.BetRepository;
import com.epitools.homer.homer.repository.ProjectRepository;
import com.epitools.homer.homer.repository.UserRepository;
import com.epitools.homer.homer.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (project == null || project.getStatus() != 1) {
            model.put("notFound", "Project not found");
            model.put("project", new Project());
        } else model.put("project", project);
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final User userE = userRepository.findByEmail(user);
        model.put("user", userE);
        final List<Bet> bets = betRepository.findByProjectId(projectId);
        final List<Bet> betsUser = betRepository.findByUserId(userE.getId());
        if (betsUser.isEmpty()) model.put("canBet", true);
        else {
            List <Bet> betsUserProject = betsUser.stream()
                    .filter(e -> e.getProjectId().equals(projectId))
                    .collect(Collectors.toList());
            if (betsUserProject.isEmpty()) model.put("canBet", true);
            else model.put("canBet", false);
        }
        final List<BetProvider> providedBets = Utils.getProvidedBets(bets, userRepository.findAll());
        model.put("bets", providedBets);
        return "bet/project";
    }

    // TODO check if user already bet and bet value 5 or 15 AND check project max spices and status
    // TODO AND user current spices > 0
    @RequestMapping(value="/bet/project/{id}", method= RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> betById(@Valid @RequestBody final BetValidation bet) {
        final Project project = projectRepository.findOne(bet.getId());
        if (project == null || project.getStatus() != 1) return Utils.jsonError("Cannot find project");
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final User userE = userRepository.findByEmail(user);
        final Bet betE = new Bet();
        betE.setSpices(bet.getSpices());
        betE.setProjectId((bet.getId()));
        betE.setUserId(userE.getId());
        betRepository.save(betE);
        project.setCurrentSpices(project.getCurrentSpices() + betE.getSpices());
        projectRepository.save(project);
        userE.setSpices(userE.getSpices() - betE.getSpices());
        userRepository.save(userE);
        return ResponseEntity.ok().build();
    }


}
