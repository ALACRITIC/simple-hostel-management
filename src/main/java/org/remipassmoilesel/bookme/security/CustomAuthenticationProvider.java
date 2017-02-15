package org.remipassmoilesel.bookme.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

/**
 * Custom authentificator implemented to prevent embedded database restrictions on security
 * <p>
 * On embedded database tables are dropped, and we cannot use application.properties to avoid that, and that's so fun :)
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private CredentialService credentialService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName().trim();
        String password = authentication.getCredentials().toString().trim();

        try {
            Credential storedCredentials = credentialService.testCredentials(username, password);

            // if user is authenticated return Spring authentication
            if (storedCredentials != null) {
                return new UsernamePasswordAuthenticationToken(username, storedCredentials.getPassword(),
                        Arrays.asList(new SimpleGrantedAuthority(storedCredentials.getRole())));
            }

        } catch (IOException e) {
            logger.error("Error while accessing database", e);
        }

        throw new BadCredentialsException("Bad credentials");
    }

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}