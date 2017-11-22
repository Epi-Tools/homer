package com.epitools.homer.homer.util;

import com.epitools.homer.homer.model.User;
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

public class Utils {

    private static final String baseUrl = "http://localhost:3000";
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

}
