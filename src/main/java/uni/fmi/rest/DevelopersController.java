package uni.fmi.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.fmi.models.DeveloperModel;
import uni.fmi.repositories.DeveloperRepository;

@RestController
public class DevelopersController {

	private DeveloperRepository developerRepo;
	
	public DevelopersController(DeveloperRepository developerRepo) {
		this.developerRepo = developerRepo;
	}
	
	
	@GetMapping(path = "/developer/all")
	public ResponseEntity<List<DeveloperModel>> getAllDevelopers() {
		return new ResponseEntity<>(developerRepo.findAll(), HttpStatus.OK);
	}
	
	
	@GetMapping(path = "/developer")
	public ResponseEntity<DeveloperModel> getDeveloperById(
			@RequestParam(name = "id") int id) {
		
		DeveloperModel developer = developerRepo.findById(id);
		
		// if the developer does not exist
		if(developer == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(developer, HttpStatus.OK);
	}
	
	
	@PostMapping(path = "/developer/insert")
	public ResponseEntity<DeveloperModel> insertDeveloper(
			@RequestParam(name = "name") String developerName,
			@RequestParam(name = "description") String description) {
		
		// if the developer already exist
		if(developerRepo.findByName(developerName) != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		DeveloperModel developer = new DeveloperModel();
		developer.setName(developerName);
		developer.setDescription(description);
		
		developer = developerRepo.saveAndFlush(developer);
		
		// if the developer is inserted
		if(developer != null) {
			return new ResponseEntity<>(developer, HttpStatus.CREATED);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	@PutMapping(path = "/developer/update")
	public ResponseEntity<DeveloperModel> updateDeveloper(
			@RequestParam(name = "id") int id,
			@RequestParam(name = "name") String developerName,
			@RequestParam(name = "description") String description) {
		
		// if the developer does not exist
		if(developerRepo.findById(id) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// if developer with this name already exist
		if(developerRepo.findByName(developerName) != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		DeveloperModel developer = developerRepo.findById(id);
		developer.setName(developerName);
		developer.setDescription(description);
		
		developer = developerRepo.saveAndFlush(developer);
		
		if(developer != null) {
			return new ResponseEntity<>(developer, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	
	@DeleteMapping(path = "/developer/delete")
	public ResponseEntity<Boolean> deleteDeveloper(
			@RequestParam(name = "id") int id) {
		
		// if the developer does not exist
		if(developerRepo.findById(id) == null) {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
		
		DeveloperModel developer = developerRepo.findById(id);
		
		developerRepo.delete(developer);
		
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
}
