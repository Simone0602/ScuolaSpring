package com.exprivia.demo.config;

import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.exprivia.demo.model.Studente;
import com.exprivia.demo.repository.DocenteRepo;
import com.exprivia.demo.repository.StudentRepo;

@Configuration
@AllArgsConstructor
public class ApplicationConfig {
	
	private final StudentRepo studentRepository;
	private final DocenteRepo docenteRepository;

	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			Optional<Studente> studente = studentRepository.findStudentByUserCode(username);
			if(studente.isPresent()) {
				return studente.get();
			}else {
				return docenteRepository.findDocenteByCodiceFiscale(username).get();
			}
		};
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
