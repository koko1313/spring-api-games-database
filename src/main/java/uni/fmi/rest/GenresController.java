package uni.fmi.rest;

import java.util.List;
import java.util.Optional;

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
	public ResponseEntity<List<GenreModel>> getAllGenres() {
		return new ResponseEntity<>(genreRepo.findAll(), HttpStatus.OK);
	}
	
	
	@GetMapping(path = "/genre")
	public ResponseEntity<GenreModel> getGenreById(
			@RequestParam(name = "id") int id) {
		
		Optional<GenreModel> genreFromDB = genreRepo.findById(id);
		
		// if the genre does not exist
		if(!genreFromDB.isPresent()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		
		GenreModel genre = genreFromDB.get();
		return new ResponseEntity<>(genre, HttpStatus.OK);
	}
	
	
	@PostMapping(path = "/genre/insert")
	public ResponseEntity<GenreModel> insertGenre(
			@RequestParam(name = "name") String genreName) {
		
		GenreModel genreFromDB = genreRepo.findByName(genreName);
		
		// if the genre already exist
		if(genreFromDB != null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		GenreModel genre = new GenreModel();
		genre.setName(genreName);
		
		genre = genreRepo.saveAndFlush(genre);
		
		// if the genre is inserted
		if(genre != null) {
			return new ResponseEntity<>(genre, HttpStatus.CREATED);
		}
		
		// is the genre is not inserted
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	
	@PutMapping(path = "/genre/update")
	public ResponseEntity<GenreModel> updateGenre(
			@RequestParam(name = "id") int id,
			@RequestParam(name = "name")String genreName) {
		
		Optional<GenreModel> genreFromDB = genreRepo.findById(id);
		
		// if the genre does not exist
		if(!genreFromDB.isPresent()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		
		GenreModel genre = genreFromDB.get();
		genre.setName(genreName);
		
		genre = genreRepo.saveAndFlush(genre);
		
		if(genre != null) {
			return new ResponseEntity<>(genre, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
	
	@DeleteMapping(path = "/genre/delete")
	public ResponseEntity<Boolean> deleteGenre(
			@RequestParam(name = "id") int id) {
		
		Optional<GenreModel> genreFromDB = genreRepo.findById(id);
		
		// if the genre does not exist
		if(!genreFromDB.isPresent()) {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
		
		GenreModel genre = genreFromDB.get();
		
		genreRepo.delete(genre);
		
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
}
