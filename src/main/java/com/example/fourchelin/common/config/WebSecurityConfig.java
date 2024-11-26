package com.example.fourchelin.common.config;

import com.example.fourchelin.common.filter.MemberAuthenticationFilter;
import com.example.fourchelin.common.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable());
        http.sessionManagement((session) ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        );

        http.authorizeHttpRequests((authorization) ->
                authorization.requestMatchers("/api/members/**", "/api/searches/**").permitAll()
                        .anyRequest().authenticated()
        );

        http.addFilterBefore(memberAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public MemberAuthenticationFilter memberAuthenticationFilter() {
        return new MemberAuthenticationFilter(userDetailsService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
