package es.eroski.azoka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		http.headers().frameOptions().disable();
		http.httpBasic()
		.and().authorizeRequests()
				.antMatchers("/actuator/**").permitAll()
				.antMatchers("/swagger-ui*/**").permitAll()
		/* Con actuator securizado */
		// .antMatchers("/actuator/**").authenticated()
				.antMatchers("/v1/personas/").authenticated()
				.antMatchers("/h2-console/**").permitAll().antMatchers("/**").permitAll();
	;

//		http.authorizeRequests().anyRequest().authenticated()
//				// httpBasic authentication
//				.and().httpBasic();
	}

//	@Bean
//	@Override
//	public UserDetailsService userDetailsService() {
//		UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER")
//				.build();
//
//		return new InMemoryUserDetailsManager(user);
//	}

//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("admin").password("{noop}password").roles("USER");
//	}

}