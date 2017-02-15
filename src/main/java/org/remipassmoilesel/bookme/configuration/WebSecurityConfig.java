package org.remipassmoilesel.bookme.configuration;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {



        // TODO encode passwords
        http
                // authorize essentials directories
                .authorizeRequests()
                .antMatchers(
                        Mappings.APPLICATION_ROOT,
                        Mappings.ASSETS_DIR + "/**",
                        Mappings.BOWER_COMPONENTS_DIR + "/**").permitAll()

                // others must login
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(Mappings.LOGIN_URL)
                .permitAll()
                .and()
                .logout()
                .logoutUrl(Mappings.LOGOUT_URL)
                .logoutSuccessUrl(Mappings.LOGOUT_SUCCESS_URL)
                .permitAll();

    }

}