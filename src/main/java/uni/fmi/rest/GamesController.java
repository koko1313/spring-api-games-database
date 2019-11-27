package uni.fmi.rest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import uni.fmi.models.DeveloperModel;
import uni.fmi.models.GameModel;
import uni.fmi.models.GenreModel;
import uni.fmi.repositories.DeveloperRepository;
import uni.fmi.repositories.GameRepository;
import uni.fmi.repositories.GenreRepository;

@RestController
public class GamesController {

	GameRepository gameRepo;
	DeveloperRepository developerRepo;
	GenreRepository genreRepo;
	
	HttpServletRequest request;
	
	public GamesController(GameRepository gameRepo, DeveloperRepository developerRepo, GenreRepository genreRepo, HttpServletRequest request) {
		this.gameRepo = gameRepo;
		this.developerRepo = developerRepo;
		this.genreRepo = genreRepo;
		this.request = request;
	}
	
	
	@GetMapping(path = "/game/all")
	public ResponseEntity<List<GameModel>> getAll() {
		return new ResponseEntity<>(gameRepo.findAll(), HttpStatus.OK);
	}
	
	
	@GetMapping(path = "/game")
	public ResponseEntity<GameModel> getById(
			@RequestParam(name = "id") int id) {
		
		GameModel game = gameRepo.findById(id);
		
		//if the game does not exist
		if(game == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(game, HttpStatus.OK);
	}
	
	
	@PostMapping(path = "/game/insert")
	public ResponseEntity<GameModel> insert(
			@RequestParam(name = "name") String name,
			@RequestParam(name = "description", required = false) String description,
			@RequestParam(name = "image", required = false) MultipartFile image,
			@RequestParam(name = "developer_id", required = false, defaultValue = "0") int developer_id,
			@RequestParam(name = "genres_id_list", required = false) List<Integer> genres_id_list) {
		
		// if the game already exist
		if(gameRepo.findByName(name) != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		DeveloperModel developer = getDeveloperById(developer_id);
		
		// if there is developer_id parameter but the developer is not found
		if(developer_id != 0 && developer == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<GenreModel> genres = getGenresById(genres_id_list);
		
		// if there is genres_id_list parameter but some of the genres are not found
		if(genres_id_list != null && genres.contains(null)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// here we will save the image name if there is image parameter
		String imageName = null;
		
		// if there is image parameter
		if(image != null) {
			String customName = "game-" + generateRandomString();
			imageName = saveImageToDisk(customName, image);
		}
		
		GameModel game = new GameModel();
		game.setName(name);
		game.setDescription(description);
		game.setImage(imageName);
		game.setDeveloper(developer);
		game.setGenres(genres);
		
		game = gameRepo.saveAndFlush(game);
		
		// if the game is inserted
		if(game != null) {
			return new ResponseEntity<>(game, HttpStatus.CREATED);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	@PutMapping(path = "game/update")
	public ResponseEntity<GameModel> update(
			@RequestParam(name = "id") int id,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "description", required = false) String description,
			@RequestParam(name = "image", required = false) MultipartFile image,
			@RequestParam(name = "developer_id", required = false, defaultValue = "0") int developer_id,
			@RequestParam(name = "genres_id_list", required = false) List<Integer> genres_id_list) {
		
		// if the game does not exist
		if(gameRepo.findById(id) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// if game with this name already exist
		if(gameRepo.findByName(name) != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		DeveloperModel developer = getDeveloperById(developer_id);
		
		// if there is developer_id parameter but the developer is not found
		if(developer_id != 0 && developer == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<GenreModel> genres = getGenresById(genres_id_list);
		
		// if there is genres_id_list parameter but some of the genres are not found
		if(genres_id_list != null && genres.contains(null)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// here we will save the image name if there is image parameter
		String imageName = null;
		
		// if there is image parameter
		if(image != null) {
			// delete the old image
			String oldImageName = gameRepo.findById(id).getImage();
			deleteImageFromDisk(oldImageName);
			
			// save the new image
			String customName = generateRandomString();
			imageName = saveImageToDisk(customName, image);
		}
		
		GameModel game = gameRepo.findById(id);
		if(name != null) game.setName(name);
		if(description != null) game.setDescription(description);
		if(image != null) game.setImage(imageName);
		if(developer_id != 0) game.setDeveloper(developer);
		if(genres_id_list != null) game.setGenres(genres);
		
		game = gameRepo.saveAndFlush(game);
		
		return new ResponseEntity<>(game, HttpStatus.OK);
	}
	
	
	@DeleteMapping(path = "/game/delete")
	public ResponseEntity<Boolean> delete(
			@RequestParam(name = "id") int id) {
		
		// if the game does not exist
		if(gameRepo.findById(id) == null) {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
		
		GameModel game = gameRepo.findById(id);
		
		// delete the game image from the disk
		deleteImageFromDisk(game.getImage());
		
		gameRepo.delete(game);
		
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	
	// #######################################################################
	
	
	private DeveloperModel getDeveloperById(int id) {
		if(id == 0) return null;
		
		return developerRepo.findById(id);
	}
	
	
	private List<GenreModel> getGenresById(List<Integer> idList) {
		
		if(idList == null) return null;
		
		List<GenreModel> genres = new ArrayList<>();
		
		for(int genre_id : idList) {
			GenreModel genre = genreRepo.findById(genre_id);
			
			genres.add(genre);
		}
		
		return genres;
	}
	
	
	/**
	 * Saves the image to the disk with custom name
	 * 
	 * @param customName
	 * @param image
	 * @return the name of the image with file extension
	 */
	private String saveImageToDisk(String customName, MultipartFile image) {
		try {
			String imageName;
			
			String uploadsDir = "/assets/images/games/";
			imageName = customName + "." + image.getContentType().split("/")[1];
			
            String realPathtoUploads =  request.getServletContext().getRealPath(uploadsDir); // get the path to webapp folder and upload folder in it
            
            if(!new File(realPathtoUploads).exists()) {
                new File(realPathtoUploads).mkdirs();
            }

            String filePath = realPathtoUploads + imageName;
            File dest = new File(filePath);
            
			image.transferTo(dest);
			
			return imageName;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Deletes image from the disk
	 * @param imageName
	 */
	private void deleteImageFromDisk(String imageName) {
		String uploadsDir = "/assets/images/games/";
		
        String realPathtoUploads =  request.getServletContext().getRealPath(uploadsDir); // get the path to webapp folder and upload folder in it

        String filePath = realPathtoUploads + imageName;
        File dest = new File(filePath);
        
        dest.delete();
	}
	
	
	private String generateRandomString() {
		int length = 30;
		boolean useLetters = true;
		boolean useNumbers = true;
		return RandomStringUtils.random(length, useLetters, useNumbers);
	}
	
}
