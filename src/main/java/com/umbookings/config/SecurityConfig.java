package com.umbookings.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.umbookings.security.CustomUserDetailsService;
import com.umbookings.security.JwtAuthenticationEntryPoint;
import com.umbookings.security.JwtAuthenticationFilter;

/**
 * @author Shrikar Kalagi
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		logger.info("Enter configure() AuthenticationManagerBuilder");
		authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
		logger.info("Exit configure() AuthenticationManagerBuilder");
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		logger.info("Enter configure() WebSecurity");
		web.ignoring().antMatchers("/configuration/ui", "/swagger-resources/**",
				"/configuration/security", "/swagger-ui.html","/swagger-ui.html/**","/v2/api-docs", "/webjars/**");

		super.configure(web);
		logger.info("Exit configure() WebSecurity");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		logger.info("Enter configure() HttpSecurity");
		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
	        .antMatchers("/sign-up").permitAll()
		        .antMatchers("/sign-in").permitAll()
				.antMatchers("/healthcheck").permitAll()
				.antMatchers("/").permitAll()
				.antMatchers("/sendRegisterOTP/**").permitAll()
				.antMatchers("/check-validity/**").permitAll()
				.antMatchers("/is-registered/**").permitAll()
		        .anyRequest()
				.authenticated();
		// Add our custom JWT security filter
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		logger.info("Exit configure() HttpSecurity");
	}

}
