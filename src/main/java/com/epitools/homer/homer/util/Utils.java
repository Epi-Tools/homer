package com.epitools.homer.homer.util;

import com.epitools.homer.homer.model.*;
import com.epitools.homer.homer.repository.UserRepository;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final String baseUrl = "https://blih.cleverapps.io";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static Response authBlih(final String login, final String password) throws NoSuchAlgorithmException,
            SignatureException, InvalidKeyException, IOException {
        final OkHttpClient client = new OkHttpClient();
        final JsonObject jData = new JsonObject();
        jData.addProperty("email", login);
        jData.addProperty("pwd", password);
        final RequestBody body = RequestBody.create(JSON, jData.toString());
        Request request = new Request.Builder().url(baseUrl + "/api/auth").post(body).build();
        return client.newCall(request).execute();
    }

    public static User getMaybeUser(final UserRepository userRepository) {
        final String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userRepository.findByEmail(user);
    }

    public static ResponseEntity<Object> jsonError(final String msg) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .body("{ \"error\" : \"" + msg + "\" }");
    }

    public static List<BetProvider> getProvidedBets(final List<Bet> bets, final List<User> users) {
        final List<User> usersFiltered = new ArrayList<>();
        bets.forEach(e -> {
            final Integer userId = e.getUserId();
            usersFiltered.addAll(users.stream().
                    filter(f -> f.getId().equals(userId))
                    .collect(Collectors.toList()));
        });
        final List<BetProvider> betProviders = new ArrayList<>();
        bets.forEach(e -> {
            final Integer userId = e.getUserId();
            final User user = usersFiltered.stream().filter(f -> f.getId().equals(userId)).findFirst().get();
            if (user.getId() != null) {
                final BetProvider bet = new BetProvider();
                bet.setId(e.getId());
                bet.setProjectId(e.getProjectId());
                bet.setUserId(user.getId());
                bet.setUsername(user.getEmail());
                bet.setSpices(e.getSpices());
                betProviders.add(bet);
            }
        });
        return betProviders;
    }

    public static List<ContributorProvider> getProvidedContributors(final List<Contributor> contributors,
                                                                    final List<User> users) {
        final List<User> usersFiltered = new ArrayList<>();
        contributors.forEach(e -> {
            final Integer userId = e.getUserId();
            usersFiltered.addAll(users.stream().
                    filter(f -> f.getId().equals(userId))
                    .collect(Collectors.toList()));
        });
        final List<ContributorProvider> contributorProviders = new ArrayList<>();
        contributors.forEach(e -> {
            final Integer userId = e.getUserId();
            final User user = usersFiltered.stream().filter(f -> f.getId().equals(userId)).findFirst().get();
            if (user.getId() != null) {
                final ContributorProvider contributorProvider = new ContributorProvider();
                contributorProvider.setId(e.getId());
                contributorProvider.setProjectId(e.getProjectId());
                contributorProvider.setUserId(user.getId());
                contributorProvider.setUsername(user.getEmail());
                contributorProviders.add(contributorProvider);
            }
        });
        return contributorProviders;
    }
}
