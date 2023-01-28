package br.ufg.sep.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.ufg.sep.data.CadastroRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.role.Role;
import br.ufg.sep.entity.role.RoleUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	private final CadastroRepository cadastroRepository;
	
    @Autowired
	public UserDetailsServiceImpl(CadastroRepository cadastroRepository) {
	this.cadastroRepository = cadastroRepository;
	}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Cadastro cadastro = cadastroRepository.findByCpf(username);
        if (cadastro == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
        	     	 
        		return new org.springframework.security.core.userdetails.User(cadastro.getCpf(), cadastro.getSenha(),
                        getAuthorities(cadastro.getRoles()));
        	}
        }
    

    private static List<GrantedAuthority> getAuthorities(List<RoleUser> list) {
    	
    	ArrayList<Role> y = new ArrayList<>();
    	
    	list.forEach(roleUserUnique -> y.add(roleUserUnique.getRole()));
    	
    	
    	
    	return y.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
    }
	
	

}
