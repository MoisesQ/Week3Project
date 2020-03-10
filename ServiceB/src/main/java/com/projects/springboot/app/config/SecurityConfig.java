package com.projects.springboot.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  /**
   * Configures basic authentication with specific filters.
   * @param http server http security object to use.
   * @return http object with specific filters.
   */
  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    return http
        .csrf().disable()
        .httpBasic()
        .and()
        .formLogin().disable()
        .build();

  }

  /**
   * Add user's authentication.
   * @return user's authentication map.
   */
  @Bean
  public MapReactiveUserDetailsService userDetailsService() {
    UserDetails user = User.builder()
        .username("user")
        .password("{noop}user")
        .roles("USER")
        .build();
    return new MapReactiveUserDetailsService(user);
  }
}
