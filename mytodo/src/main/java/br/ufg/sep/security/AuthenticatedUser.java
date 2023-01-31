package br.ufg.sep.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.entity.Cadastro;

@Component
public class AuthenticatedUser {

    private final CadastroRepository cadastroRepository;

    @Autowired
    public AuthenticatedUser(CadastroRepository cadastroRepository) {
        this.cadastroRepository = cadastroRepository;
    }

    private Optional<Authentication> getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(context.getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken));
    }

    public Optional<Cadastro> get() {
        return getAuthentication().map(authentication -> cadastroRepository.findByCpf(authentication.getName()));
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(SecurityConfig.LOGOUT_URL);// "/" se refere a Url de Log-out
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }

}

