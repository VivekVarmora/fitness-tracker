package com.fitness.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.fitness.tracker.model.User.Role;
import com.fitness.tracker.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;

	private static final String USER_ROLE = Role.USER.toString();
	private static final String ADMIN_ROLE = Role.ADMIN.toString();

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(t -> t.disable()).headers(headers -> headers.frameOptions(f -> f.disable()))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
						.permitAll().requestMatchers(HttpMethod.POST, "/fitness/users").permitAll()
						.requestMatchers(HttpMethod.GET, "/fitness/users").hasRole(ADMIN_ROLE)
						.requestMatchers(HttpMethod.GET, "/fitness/users/*").hasAnyRole(USER_ROLE, ADMIN_ROLE)
						.requestMatchers("/fitness/workout-plans/**", "/fitness/activity-logs/**")
						.hasAnyRole(USER_ROLE, ADMIN_ROLE).anyRequest().authenticated())
				.formLogin(f -> f.disable())
				.httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(new NoPopupAuthenticationEntryPoint()));

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
		var authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}