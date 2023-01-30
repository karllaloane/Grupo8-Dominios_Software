package br.ufg.sep.security;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;

import br.ufg.sep.views.login.LoginView;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurityConfigurerAdapter {
	
    public static final String LOGOUT_URL = "/login";

	
	 @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new EncriptadorBasico();
	    }
	
	protected void configure(HttpSecurity http) throws Exception{
		super.configure(http);
		setLoginView(http,LoginView.class,LOGOUT_URL); // /login se refere a url de log-out da classe
	}
	
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/images/**");
		super.configure(web);
	}

	
	/*
	
	private static class CrmInMemoryUserDetailsManager extends InMemoryUserDetailsManager {
	    public CrmInMemoryUserDetailsManager() {
	      createUser(new User("user",
	              "{noop}userpass",
	              Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))));
	    }
	  }
	
	 @Bean
	  public InMemoryUserDetailsManager userDetailsService() {
	    return new CrmInMemoryUserDetailsManager();
	  }
*/
	
}
