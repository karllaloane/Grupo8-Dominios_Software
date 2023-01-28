package br.ufg.sep.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class EncriptadorBasico implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return (String) rawPassword;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {

		String senha = (String) rawPassword;
		
		if(senha.equals(encodedPassword)) return true;
		else		return false;

	
	}

}
