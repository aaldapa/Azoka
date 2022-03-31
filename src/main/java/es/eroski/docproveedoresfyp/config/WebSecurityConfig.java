package es.eroski.docproveedoresfyp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
	    return new HttpSessionEventPublisher();
	}
	

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		http.headers().frameOptions().disable();
		http.httpBasic().and().authorizeRequests().antMatchers("/actuator/**").permitAll()
				.antMatchers("/swagger-ui*/**").permitAll()
				/* Con actuator securizado */
				// .antMatchers("/actuator/**").authenticated()
				.antMatchers("/h2-console/**").permitAll()
//				.antMatchers("/**").permitAll();
				.anyRequest().authenticated()
				.and()
				//Sin limite de sessiones conectados con el mismo user
				.sessionManagement().maximumSessions(-1)
				//Cada authenticacion usa una nueva session 
				.and().sessionFixation().newSession()
				//Spring security no creara sessiones
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}
}