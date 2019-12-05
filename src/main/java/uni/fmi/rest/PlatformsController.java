package uni.fmi.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.fmi.models.PlatformModel;
import uni.fmi.repositories.PlatformRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class PlatformsController {

	private PlatformRepository platformRepo;
	
	public PlatformsController(PlatformRepository platformRepo) {
		this.platformRepo = platformRepo;
	}
	
	
	@GetMapping(path = "/platform/all")
	public ResponseEntity<List<PlatformModel>> getAll() {
		return new ResponseEntity<>(platformRepo.findAll(), HttpStatus.OK);
	}
	
	
	@GetMapping(path = "/platform")
	public ResponseEntity<PlatformModel> getById(
			@RequestParam(name = "id") int id) {
		
		PlatformModel platform = platformRepo.findById(id);
		
		// if the platform does not exist
		if(platform == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(platform, HttpStatus.OK);
	}
	
	
	@PostMapping(path = "/platform/insert")
	public ResponseEntity<PlatformModel> insert(
			@RequestParam(name = "name") String platformName) {
		
		// if platformName is null
		if(platformName.equals("")) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		// if the platform already exist
		if(platformRepo.findByName(platformName) != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		PlatformModel platform = new PlatformModel();
		platform.setName(platformName);
		
		platform = platformRepo.saveAndFlush(platform);
		
		// if the platform is inserted
		if(platform != null) {
			return new ResponseEntity<>(platform, HttpStatus.CREATED);
		}
		
		// is the platform is not inserted
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	@PutMapping(path = "/platform/update")
	public ResponseEntity<PlatformModel> update(
			@RequestParam(name = "id") int id,
			@RequestParam(name = "name") String platformName) {
		
		// if the platform does not exist
		if(platformRepo.findById(id) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// if platformName is null
		if(platformName.equals("")) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		// if platform with this name already exist
		PlatformModel platform = platformRepo.findByName(platformName);
		if(platform != null) {
			// and it's different than current
			if(platform.getId() != id) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		}
		
		platform = platformRepo.findById(id);
		platform.setName(platformName);
		
		platform = platformRepo.saveAndFlush(platform);
		
		return new ResponseEntity<>(platform, HttpStatus.OK);
	}
	
	
	@DeleteMapping(path = "/platform/delete")
	public ResponseEntity<Boolean> delete(
			@RequestParam(name = "id") int id) {
		
		// if the platform does not exist
		if(platformRepo.findById(id) == null) {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
		
		PlatformModel platform = platformRepo.findById(id);
		
		platformRepo.delete(platform);
		
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
}
