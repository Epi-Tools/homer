package com.epitools.homer.homer.provider;

import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.repository.UserRepository;
import com.epitools.homer.homer.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // TODO(carlendev) fix when user doesn't exist
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String email = authentication.getName();
        final String password = authentication.getCredentials().toString();
        try {
            final okhttp3.Response resp = Utils.authBlih(email, password);
            JSONObject jsonObj = new JSONObject(resp.body().string());
            if (!jsonObj.has("token"))  throw new BadCredentialsException("Invalid credentials");
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            final User user = userRepository.findByEmail(email);
            if (user == null) {
                User nUser = new User();
                nUser.setEmail(email);
                nUser.setAdmin(false);
                userRepository.save(nUser);
                grantedAuthorities.add(new SimpleGrantedAuthority(nUser.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER"));
                return new UsernamePasswordAuthenticationToken(email, passwordEncoder.encode(jsonObj.getString("token")), grantedAuthorities);
            }
            grantedAuthorities.add(new SimpleGrantedAuthority(user.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER"));
            return new UsernamePasswordAuthenticationToken(email, passwordEncoder.encode(jsonObj.getString("token")), grantedAuthorities);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        throw new BadCredentialsException("Invalid credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}