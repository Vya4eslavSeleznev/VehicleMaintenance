package com.vehicle.maintenance.security;

import com.vehicle.maintenance.security.jwt.JwtSecurityConfigurer;
import com.vehicle.maintenance.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
public class SpringSecurityConfig {

    private JwtTokenProvider jwtTokenProvider;

    private static final String[] AUTH_WHITELIST = {
      "/v2/api-docs",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui.html",
      "/webjars/**",
      "/v3/api-docs/**",
      "/swagger-ui/**"
    };

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
          .httpBasic().disable()
          .formLogin().disable()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
          .authorizeRequests()
          .antMatchers(AUTH_WHITELIST).permitAll()
          .antMatchers("/auth/signin").permitAll()
          .antMatchers("/maintenance/conservation").hasAuthority("USER")
          .antMatchers("/customer/**").hasAuthority("ADMIN")
          .antMatchers("/car/**").hasAuthority("ADMIN")
          .antMatchers("/maintenance/**").hasAuthority("ADMIN")
          .anyRequest().authenticated().and()
          .apply(new JwtSecurityConfigurer(jwtTokenProvider));

        return http.build();
    }
}
