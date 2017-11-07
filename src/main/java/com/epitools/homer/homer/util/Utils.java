package com.epitools.homer.homer.util;

import com.google.gson.JsonObject;
import okhttp3.*;

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

}
