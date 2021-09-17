package com.skillbox.socialnetwork.config;

import com.skillbox.socialnetwork.api.security.TokenAuthenticationFilter;
import com.skillbox.socialnetwork.api.security.TokenAuthenticationManager;
import com.skillbox.socialnetwork.api.security.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailServiceImpl userDetailsService;
    //private final TokenAuthenticationManager tokenAuthenticationManager;

    public SecurityConfig(UserDetailServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
       // this.tokenAuthenticationManager = tokenAuthenticationManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                //.addFilterAfter(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout().logoutUrl("/api/auth/logout")
                .and()
                .formLogin().disable()
                .httpBasic();
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
//    @Bean(name = "TokenAuthenticationFilter")
//    public TokenAuthenticationFilter tokenAuthenticationFilter() {
//        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter();
//        tokenAuthenticationFilter.setAuthenticationManager(tokenAuthenticationManager);
//        return tokenAuthenticationFilter;
//    }
}