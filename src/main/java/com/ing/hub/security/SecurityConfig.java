package com.ing.hub.security;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	public SecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
	    this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
	}

    @Bean
     SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
        		.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**","/h2-console/**","/login").permitAll()
                    .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                    .requestMatchers("/employee/**").hasAnyRole("EMPLOYEE", "ADMIN")
                    .requestMatchers("/CUSTOMER/**").hasAnyRole("EMPLOYEE", "ADMIN", "CUSTOMER")
                    .anyRequest().authenticated()
                )
        		.formLogin(form -> form
        				.loginPage("/login")
        				.successHandler(customAuthenticationSuccessHandler) 
        	            .permitAll()
        	        )
        	        .logout(logout -> logout
        	            .logoutSuccessUrl("/login?logout")
        	        )
        	        .headers(headers -> headers
        	        	    .frameOptions(frameOptions -> frameOptions.disable())
        	        	)
            .build();
    }
    
    @Bean
     AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
     PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
     GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
            .group("admin") 
            .pathsToMatch("/admin/**")
            .build();
    }
    
    @Bean
     GroupedOpenApi employeeApi() {
        return GroupedOpenApi.builder()
            .group("employee")
            .pathsToMatch("/employee/**")
            .build();
    }
    
    @Bean
     GroupedOpenApi customerApi() {
        return GroupedOpenApi.builder()
            .group("customer")
            .pathsToMatch("/customer/**")
            .build();
    }
    
    @Bean
    public RoleHierarchy roleHierarchy() {
        var roleHierarchy = new RoleHierarchyImpl(); 
        roleHierarchy.setHierarchy("""
            ROLE_ADMIN > ROLE_EMPLOYEE
            ROLE_EMPLOYEE > ROLE_CUSTOMER
        """);
        return roleHierarchy;
    }

    @Bean
     MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }
    
}