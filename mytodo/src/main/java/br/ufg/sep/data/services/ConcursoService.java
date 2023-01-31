package br.ufg.sep.data.services;

import org.springframework.stereotype.Service;

import br.ufg.sep.data.repositories.ConcursoRepository;
import br.ufg.sep.entity.Concurso;

@Service
public class ConcursoService {
	private final ConcursoRepository concursoRepoitory;
	
	public ConcursoService(ConcursoRepository cR) {
		// TODO Auto-generated constructor stub
		this.concursoRepoitory = cR;
	}
	
	public ConcursoRepository getRepository() {
		return this.concursoRepoitory;
	}
	
	public void save(Concurso c) throws java.lang.NullPointerException {
		if(c.getNome()==null)throw new NullPointerException("Concurso sem Nome!");
		this.concursoRepoitory.save(c);
	}
	

}
