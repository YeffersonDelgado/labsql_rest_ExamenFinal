package link.softbond.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import link.softbond.entities.Problema;
import link.softbond.repositorios.ProblemaRepository;

@RestController
@RequestMapping("/problema")
@CrossOrigin
public class ProblemaController {

	@Autowired
	private ProblemaRepository problemaRepository;
	
	
	@GetMapping("/list")
	public List<Problema> listar(){
		return problemaRepository.findAll();
	}
	 
	
}
