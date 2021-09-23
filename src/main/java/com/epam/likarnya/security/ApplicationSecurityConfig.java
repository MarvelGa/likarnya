package com.epam.likarnya.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService service;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/messages/**", "/static/topstyle.css*", "/templates/fragments")
                .permitAll()
                .antMatchers("/likarnya/admin/**").hasRole("ADMIN")
                .antMatchers("/likarnya/doctor-cabinet/**").hasRole("DOCTOR")
                .antMatchers("/likarnya/nurse-cabinet/**").hasRole("NURSE")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/likarnya/login")
                .failureUrl("/likarnya/login-error")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/likarnya/success", true)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/likarnya/perform-logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/likarnya/perform-logout.html", "POST"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
//                .deleteCookies("loginUser")
                .logoutSuccessUrl("/likarnya/login");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(service);
        return provider;
    }
}
