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

import uni.fmi.models.GenreModel;
import uni.fmi.repositories.GenreRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "true")
@RestController
public class GenresController {

	private GenreRepository genreRepo;
	
	public GenresController(GenreRepository genreRepo) {
		this.genreRepo = genreRepo;
	}
	
	
	@GetMapping(path = "/genre/all")
	public ResponseEntity<List<GenreModel>> getAll() {
		return new ResponseEntity<>(genreRepo.findAll(), HttpStatus.OK);
	}
	
	
	@GetMapping(path = "/genre")
	public ResponseEntity<GenreModel> getById(
			@RequestParam(name = "id") int id) {
		
		GenreModel genre = genreRepo.findById(id);
		
		// if the genre does not exist
		if(genre == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(genre, HttpStatus.OK);
	}
	
	
	@Secured("ROLE_ADMIN")
	@PostMapping(path = "/genre/insert")
	public ResponseEntity<GenreModel> insert(
			@RequestParam(name = "name") String genreName) {
		
		// if genreName is null
		if(genreName.equals("")) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		// if the genre already exist
		if(genreRepo.findByName(genreName) != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		GenreModel genre = new GenreModel();
		genre.setName(genreName);
		
		genre = genreRepo.saveAndFlush(genre);
		
		// if the genre is inserted
		if(genre != null) {
			return new ResponseEntity<>(genre, HttpStatus.CREATED);
		}
		
		// is the genre is not inserted
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	@Secured("ROLE_ADMIN")
	@PutMapping(path = "/genre/update")
	public ResponseEntity<GenreModel> update(
			@RequestParam(name = "id") int id,
			@RequestParam(name = "name") String genreName) {
		
		// if the genre does not exist
		if(genreRepo.findById(id) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// if genreName is null
		if(genreName.equals("")) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		// if genre with this name already exist
		GenreModel genre = genreRepo.findByName(genreName);
		if(genre != null) {
			// and it's different than current
			if(genre.getId() != id) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		}
		
		genre = genreRepo.findById(id);
		genre.setName(genreName);
		
		genre = genreRepo.saveAndFlush(genre);
		
		return new ResponseEntity<>(genre, HttpStatus.OK);
	}
	
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping(path = "/genre/delete")
	public ResponseEntity<Boolean> delete(
			@RequestParam(name = "id") int id) {
		
		// if the genre does not exist
		if(genreRepo.findById(id) == null) {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
		
		GenreModel genre = genreRepo.findById(id);
		
		genreRepo.delete(genre);
		
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
}
