package com.example.fourchelin.common.config;

import com.example.fourchelin.common.exception.CustomAccessDeniedHandler;
import com.example.fourchelin.common.exception.CustomAuthenticationEntryPoint;
import com.example.fourchelin.common.filter.MemberAuthenticationFilter;
import com.example.fourchelin.common.security.UserDetailsServiceImpl;
import com.example.fourchelin.common.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final CacheService cacheService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());

        http.authorizeHttpRequests((authorization) ->
                authorization.requestMatchers("/api/members/signup", "/api/members/login", "/actuator/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
        );

        http.exceptionHandling((except) ->
                except.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        );

        http.exceptionHandling((except) ->
                except.accessDeniedHandler(new CustomAccessDeniedHandler())
        );

        http.addFilterBefore(memberAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public MemberAuthenticationFilter memberAuthenticationFilter() {
        return new MemberAuthenticationFilter(userDetailsService, cacheService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
