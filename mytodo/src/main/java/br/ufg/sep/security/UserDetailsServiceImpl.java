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
import br.ufg.sep.data.RoleUserRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.role.Role;
import br.ufg.sep.entity.role.RoleUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	private final CadastroRepository cadastroRepository;
	private final RoleUserRepository roleUserRepository;
    @Autowired
	public UserDetailsServiceImpl(CadastroRepository cadastroRepository, RoleUserRepository roleUserRepository) {
	this.cadastroRepository = cadastroRepository;
	this.roleUserRepository = roleUserRepository;
	}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Cadastro cadastro = cadastroRepository.findByCpf(username);
    	 
        if (cadastro == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
        	RoleUser roleUser = roleUserRepository.findByUserCpf(cadastro.getCpf());
        	if(roleUser==null) {
        		throw new UsernameNotFoundException("No user present with username: "+ username);
        	}else {
        		return new org.springframework.security.core.userdetails.User(cadastro.getCpf(), cadastro.getSenha(),
                        getAuthorities(roleUser));
        	}
        }
        	
        }
    

    private static List<GrantedAuthority> getAuthorities(RoleUser roleUser) {
    	Role x = roleUser.getRole();
    	ArrayList<Role> y = new ArrayList<>();
    	y.add(x);

    	
    	return y.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
    }
	
	

}
