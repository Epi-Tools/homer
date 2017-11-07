package com.epitools.homer.homer.util;

import com.google.gson.JsonObject;
import okhttp3.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

public class Utils {

    private static final String baseUrl = "https://blih.epitech.eu/";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String HMAC_SHA512 = "HmacSHA512";

    public static String hash(final String password) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-512");
            byte[] passHash = sha256.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aPassHash : passHash)
                sb.append(Integer.toString((aPassHash & 0xff) + 0x100, 16).substring(1));
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return null;
    }
    private static String toHexString(final byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) formatter.format("%02x", b);
        return formatter.toString();
    }

    private static String HMAC_SHA512_encode(final String login, final String token) throws SignatureException,
            NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(token.getBytes(), HMAC_SHA512);
        Mac mac = Mac.getInstance(HMAC_SHA512);
        mac.init(secretKeySpec);
        return toHexString(mac.doFinal(login.getBytes()));
    }

    public static String authBlih(final String login, final String password) throws NoSuchAlgorithmException,
            SignatureException, InvalidKeyException, IOException {
        final OkHttpClient client = new OkHttpClient();
        final String data = HMAC_SHA512_encode(login, hash(password));
        final JsonObject jData = new JsonObject();
        jData.addProperty("user", login);
        jData.addProperty("signature", data);
        final RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder().url(baseUrl + "whoami").post(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        return response.body().string();
    }

}
