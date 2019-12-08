package uni.fmi.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.fmi.models.DeveloperModel;
import uni.fmi.repositories.DeveloperRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "true")
@RestController
public class DevelopersController {

	private DeveloperRepository developerRepo;
	
	public DevelopersController(DeveloperRepository developerRepo) {
		this.developerRepo = developerRepo;
	}
	
	
	@GetMapping(path = "/developer/all")
	public ResponseEntity<List<DeveloperModel>> getAll() {
		return new ResponseEntity<>(developerRepo.findAll(), HttpStatus.OK);
	}
	
	
	@GetMapping(path = "/developer")
	public ResponseEntity<DeveloperModel> getById(
			@RequestParam(name = "id") int id) {
		
		DeveloperModel developer = developerRepo.findById(id);
		
		// if the developer does not exist
		if(developer == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(developer, HttpStatus.OK);
	}
	
	
	@Secured("ROLE_ADMIN")
	@PostMapping(path = "/developer/insert")
	public ResponseEntity<DeveloperModel> insert(
			@RequestParam(name = "name") String developerName,
			@RequestParam(name = "description", required = false) String description) {
		
		// if developerName is null
		if(developerName.equals("")) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		// if the developer already exist
		if(developerRepo.findByName(developerName) != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
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
	
	
	@Secured("ROLE_ADMIN")
	@PutMapping(path = "/developer/update")
	public ResponseEntity<DeveloperModel> update(
			@RequestParam(name = "id") int id,
			@RequestParam(name = "name", required = false) String developerName,
			@RequestParam(name = "description", required = false) String description) {
		
		// if the developer does not exist
		if(developerRepo.findById(id) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// if developerName is null
		if(developerName.equals("")) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		// if developer with this name already exist
		DeveloperModel developer = developerRepo.findByName(developerName);
		if(developer != null) {
			// and it's different than current
			if(developer.getId() != id) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		}
		
		developer = developerRepo.findById(id);
		if(developerName != null) developer.setName(developerName);
		if(description != null) developer.setDescription(description);
		
		developer = developerRepo.saveAndFlush(developer);
		
		return new ResponseEntity<>(developer, HttpStatus.OK);
	}
	
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping(path = "/developer/delete")
	public ResponseEntity<Boolean> delete(
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
