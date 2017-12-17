package com.epitools.homer.homer.rest.controller;

import com.epitools.homer.homer.model.Bet;
import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.repository.BetRepository;
import com.epitools.homer.homer.repository.UserRepository;
import com.epitools.homer.homer.util.Utils;
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
public class BetRestController {

    @Autowired
    BetRepository betRepository;

    @Autowired
    UserRepository userRepository;

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

    @RequestMapping(value="/bets/my", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> getMyBets() {
        final User maybeUser = Utils.getMaybeUser(userRepository);
        if (maybeUser == null) return Utils.jsonError("User not connected");
        List<Bet> bets = betRepository.findByUserId(maybeUser.getId());
        if(bets == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(bets);
    }


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
    public ResponseEntity<Bet> deleteBet(@PathVariable(value="id") Integer betId) {
        Bet bet = betRepository.findOne(betId);
        if(bet == null) return ResponseEntity.notFound().build();
        betRepository.delete(bet);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/bets", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public Bet createBet(@Valid @RequestBody Bet bet) {
        return betRepository.save(bet);
    }
}

