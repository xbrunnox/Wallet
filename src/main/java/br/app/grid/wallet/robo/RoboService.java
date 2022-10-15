package br.app.grid.wallet.robo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoboService {
	
	@Autowired
	private RoboRepository repository;
	
	public Robo getById(String id) {
		return repository.findById(id).get();
	}

}
