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

import uni.fmi.models.GenreModel;
import uni.fmi.repositories.GenreRepository;

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
	
	
	@PostMapping(path = "/genre/insert")
	public ResponseEntity<GenreModel> insert(
			@RequestParam(name = "name") String genreName) {
		
		// if the genre already exist
		if(genreRepo.findByName(genreName) != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
	
	
	@PutMapping(path = "/genre/update")
	public ResponseEntity<GenreModel> update(
			@RequestParam(name = "id") int id,
			@RequestParam(name = "name")String genreName) {
		
		// if the genre does not exist
		if(genreRepo.findById(id) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// if genre with this name already exist
		if(genreRepo.findByName(genreName) != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		GenreModel genre = genreRepo.findById(id);
		genre.setName(genreName);
		
		genre = genreRepo.saveAndFlush(genre);
		
		if(genre != null) {
			return new ResponseEntity<>(genre, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	
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
