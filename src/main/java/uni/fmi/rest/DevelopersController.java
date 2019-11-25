package uni.fmi.rest;

import org.springframework.web.bind.annotation.RestController;

import uni.fmi.repositories.DeveloperRepository;

@RestController
public class DevelopersController {

	private DeveloperRepository developerRepo;
	
	public DevelopersController(DeveloperRepository developerRepo) {
		this.developerRepo = developerRepo;
	}
	
}
