package com.pms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	@Lazy
	private AuthenticationEntrypoint authenticationEntrypoint;

	@Autowired
	@Lazy
	private UserDetailsService userDetailsService;

	@Autowired
	@Lazy
	private RequestFilter requestFilter;

	@Bean
	public DaoAuthenticationProvider doAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(encryptPassword());
		return daoAuthenticationProvider;

	}

	@Bean
	public BCryptPasswordEncoder encryptPassword() {
		// TODO Auto-generated method stub
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).build();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						authorize -> authorize
								.requestMatchers("/auth/login", "/api/register/**","/api/current-user", "all", "/actuator/**",
										"/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/**",
										"/v3/api-docs/**", "/v2/api-docs/**", "/webjars/**", "/actuator/**","/purchase/download/**", "/purchase/**")
								.permitAll())
				.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
				.exceptionHandling(handling -> handling.authenticationEntryPoint(authenticationEntrypoint))
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		httpSecurity.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

}
