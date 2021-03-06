package com.epitools.homer.homer.rest.controller;

import com.epitools.homer.homer.model.*;
import com.epitools.homer.homer.repository.BetRepository;
import com.epitools.homer.homer.repository.ProjectRepository;
import com.epitools.homer.homer.repository.UserRepository;
import com.epitools.homer.homer.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
public class BetRestController {

    @Autowired
    BetRepository betRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @RequestMapping(value="/bets", method= RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public List<Bet> getAllBets() {
        return betRepository.findAll();
    }

    @RequestMapping(value="/bets/{id}", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Bet> getBetById(@PathVariable(value="id") Integer betId) {
        Bet bet = betRepository.findOne(betId);
        if(bet == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(bet);
    }

    @RequestMapping(value="/bets/project/{id}", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<Bet>> getBetByProjectId(@PathVariable(value="id") Integer projectId) {
        List<Bet> bets = betRepository.findByProjectId(projectId);
        if(bets == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(bets);
    }


    @RequestMapping(value="/bets/project/provided/{id}", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<BetProvider>> getBetByProjectProvidedId(@PathVariable(value="id") Integer projectId) {
        final List<Bet> bets = betRepository.findByProjectId(projectId);
        final List <BetProvider> providedBets = new ArrayList<>();
        if (bets.isEmpty()) return ResponseEntity.ok().body(providedBets);
        providedBets.addAll(Utils.getProvidedBets(bets, userRepository.findAll()));
        return ResponseEntity.ok().body(providedBets);
    }

    @RequestMapping(value="/bets/my", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> getMyBets() {
        final User maybeUser = Utils.getMaybeUser(userRepository);
        if (maybeUser == null) return Utils.jsonError("User not connected");
        List<Bet> bets = betRepository.findByUserId(maybeUser.getId());
        if(bets == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(bets);
    }

    @RequestMapping(value="/bets/project/my", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> getMyProjectBets() {
        final User maybeUser = Utils.getMaybeUser(userRepository);
        if (maybeUser == null) return Utils.jsonError("User not connected");
        final List<Bet> betList = betRepository.findByUserId(maybeUser.getId());
        if (betList == null) return ResponseEntity.notFound().build();
        final List<Project> projectList = projectRepository.findAll();
        if (projectList == null) return ResponseEntity.notFound().build();
        List<BetProjectProvider> betProjectProviderList = Utils.getProvidedProjectBets(betList,
                userRepository.findAll(), projectList);
        if (betProjectProviderList == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(betProjectProviderList);
    }

    // TODO(carlendev) don't forget
    // TODO(carlendev) check project status, it should be equal to zero
    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/bets/{id}", method=RequestMethod.PUT, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Bet> updateBet(@PathVariable(value="id") Integer betId,
                                           @Valid @RequestBody Bet betDetails) {
        Bet bet = betRepository.findOne(betId);
        if(bet == null) return ResponseEntity.notFound().build();
        bet.setSpices(betDetails.getSpices());
        bet.setUserId(betDetails.getUserId());
        bet.setProjectId(betDetails.getProjectId());
        Bet updatedBet = betRepository.save(bet);
        return ResponseEntity.ok(updatedBet);
    }

    @RequestMapping(value="/bets/{id}", method=RequestMethod.DELETE, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> deleteBet(@PathVariable(value="id") Integer betId) {
        final Bet bet = betRepository.findOne(betId);
        if (bet == null) return ResponseEntity.notFound().build();
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final User userE = userRepository.findByEmail(user);
        final Project project = projectRepository.findOne(bet.getProjectId());
        if (project == null) return ResponseEntity.notFound().build();
        if (userE.isAdmin().equals(0) && !project.getUserId().equals(userE.getId()))
            return Utils.jsonError("Can not delete this bet");
        if (project.getStatus() > 1 && userE.isAdmin().equals(0)) return Utils.jsonError("Can not delete this bet");
        if (project.getStatus() > 1 && userE.isAdmin().equals(1)) betRepository.delete(bet);
        else {
            final User userB = userRepository.findOne(bet.getUserId());
            userB.setSpices(userB.getSpices() + bet.getSpices());
            project.setCurrentSpices(project.getCurrentSpices() - bet.getSpices());
            projectRepository.save(project);
            userRepository.save(userB);
            betRepository.delete(bet);
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/bets", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> createBet(@Valid @RequestBody Bet bet) {
        final Project project = projectRepository.findOne(bet.getProjectId());
        if (project == null || project.getStatus() != 1) return ResponseEntity.notFound().build();
        if (!bet.getSpices().equals(5) &&
                !bet.getSpices().equals(15)) return Utils.jsonError("Bet should be equal to 5 or 15");
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final User userE = userRepository.findByEmail(user);
        final Bet betE = new Bet();
        if (project.getCurrentSpices() + bet.getSpices() >
                project.getSpices()) return Utils.jsonError("Max spices is reached");
        if (userE.getSpices() - bet.getSpices() < 0) return Utils.jsonError("User haven't enough spices");
        final List<Bet> betsUser = betRepository.findByUserId(userE.getId());
        if (!betsUser.isEmpty()) {
            List <Bet> betsUserProject = betsUser.stream()
                    .filter(e -> e.getProjectId().equals(bet.getProjectId()))
                    .collect(Collectors.toList());
            if (!betsUserProject.isEmpty()) return Utils.jsonError("User have already bet");
        }
        betE.setSpices(bet.getSpices());
        betE.setProjectId((bet.getProjectId()));
        betE.setUserId(userE.getId());
        betRepository.save(betE);
        project.setCurrentSpices(project.getCurrentSpices() + betE.getSpices());
        if (project.getCurrentSpices().equals(project.getSpices())) project.setStatus(project.getStatus() + 1);
        projectRepository.save(project);
        userE.setSpices(userE.getSpices() - betE.getSpices());
        userRepository.save(userE);
        return ResponseEntity.ok().build();
    }
}
