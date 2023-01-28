package br.ufg.sep.security;

import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;

@Component
public class SecurityService {

	public void logout() {
		
		UI.getCurrent().getPage().setLocation("/");
		SecurityContextLogoutHandler logouHandler = new SecurityContextLogoutHandler(); 
		logouHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
	}
}
