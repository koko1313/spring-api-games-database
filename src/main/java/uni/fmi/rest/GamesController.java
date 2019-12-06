package uni.fmi.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import uni.fmi.fileManipulations.FileManipulations;
import uni.fmi.models.DeveloperModel;
import uni.fmi.models.GameModel;
import uni.fmi.models.GenreModel;
import uni.fmi.models.PlatformModel;
import uni.fmi.repositories.DeveloperRepository;
import uni.fmi.repositories.GameRepository;
import uni.fmi.repositories.GenreRepository;
import uni.fmi.repositories.PlatformRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class GamesController {

	GameRepository gameRepo;
	DeveloperRepository developerRepo;
	GenreRepository genreRepo;
	PlatformRepository platformRepo;
	
	HttpServletRequest request;
	
	final String PATH_TO_IMAGES_FOLDER = "/assets/images/games/";
	
	public GamesController(GameRepository gameRepo, DeveloperRepository developerRepo, GenreRepository genreRepo, PlatformRepository platformRepo, HttpServletRequest request) {
		this.gameRepo = gameRepo;
		this.developerRepo = developerRepo;
		this.genreRepo = genreRepo;
		this.platformRepo = platformRepo;
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
	

	@GetMapping(path = "/game/search")
	public ResponseEntity<HashSet<GameModel>> getByCriteria(
			@RequestParam(name = "genres_id_list", required = false) List<Integer> genres_id_list,
			@RequestParam(name = "platforms_id_list", required = false) List<Integer> platforms_id_list) {
		
		HashSet<GameModel> games = new HashSet<>();
		
		// if there is both genres_id_list and platforms_id_list parameters
		if(genres_id_list != null && platforms_id_list != null) {
			games = gameRepo.findByGenresIdInAndPlatformsIdIn(genres_id_list, platforms_id_list);
		}
		else if(genres_id_list != null) {
			games = gameRepo.findByGenresIdIn(genres_id_list);
		}
		else if(platforms_id_list != null) {
			games = gameRepo.findByPlatformsIdIn(platforms_id_list);
		}
		
		return new ResponseEntity<>(games, HttpStatus.OK);
	}
	
	
	@PostMapping(path = "/game/insert")
	public ResponseEntity<GameModel> insert(
			@RequestParam(name = "name") String name,
			@RequestParam(name = "description", required = false) String description,
			@RequestParam(name = "image", required = false) MultipartFile image,
			@RequestParam(name = "developer_id", required = false, defaultValue = "0") int developer_id,
			@RequestParam(name = "genres_id_list", required = false) List<Integer> genres_id_list,
			@RequestParam(name = "platforms_id_list", required = false)List<Integer> platforms_id_list) {
		
		// if the name of the game is null
		if(name.equals("")) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		// if the game already exist
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
		
		List<PlatformModel> platforms = getPlatformsById(platforms_id_list);
		
		// if there is platforms_id_list parameter but some of the platforms are not found
		if(platforms_id_list != null && platforms.contains(null)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// here we will save the image name if there is image parameter
		String imageName = null;
		
		// if there is image parameter
		if(image != null) {
			String customName = generateRandomString();
			
			String realPathToImagesFolder = request.getServletContext().getRealPath(PATH_TO_IMAGES_FOLDER);
			
			imageName = FileManipulations.saveFileToDisk(customName, image, realPathToImagesFolder);
		}
		
		GameModel game = new GameModel();
		game.setName(name);
		game.setDescription(description);
		game.setImage(imageName);
		game.setDeveloper(developer);
		game.setGenres(genres);
		game.setPlatforms(platforms);
		
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
			@RequestParam(name = "genres_id_list", required = false) List<Integer> genres_id_list,
			@RequestParam(name = "platforms_id_list", required = false)List<Integer> platforms_id_list) {
		
		// if the game does not exist
		if(gameRepo.findById(id) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// if the name of the game is null
		if(name.equals("")) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		// if game with this name already exist
		GameModel game = gameRepo.findByName(name);
		if(game != null) {
			// and it's different than current
			if(game.getId() != id) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
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
		
		List<PlatformModel> platforms = getPlatformsById(platforms_id_list);
		
		// if there is platforms_id_list parameter but some of the platforms are not found
		if(platforms_id_list != null && platforms.contains(null)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// here we will save the image name if there is image parameter
		String imageName = null;
		
		// if there is image parameter
		if(image != null) {
			String realPathToImagesFolder = request.getServletContext().getRealPath(PATH_TO_IMAGES_FOLDER);
			
			// delete the old image
			String oldImageName = gameRepo.findById(id).getImage();
			FileManipulations.deleteFileFromDisk(oldImageName, realPathToImagesFolder);
			
			// save the new image
			String customName = generateRandomString();
			imageName = FileManipulations.saveFileToDisk(customName, image, realPathToImagesFolder);
		}
		
		game = gameRepo.findById(id);
		if(name != null) game.setName(name);
		if(description != null) game.setDescription(description);
		if(image != null) game.setImage(imageName);
		if(developer_id != 0) game.setDeveloper(developer);
		if(genres_id_list != null) game.setGenres(genres);
		if(platforms_id_list != null) game.setPlatforms(platforms);
		
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
		String realPathToImagesFolder = request.getServletContext().getRealPath(PATH_TO_IMAGES_FOLDER);
		FileManipulations.deleteFileFromDisk(game.getImage(), realPathToImagesFolder);
		
		gameRepo.delete(game);
		
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	@GetMapping(path = "/images-folder")
	public ResponseEntity<String> getPathToImagesFolder() {
		return new ResponseEntity<>(request.getServletContext().getRealPath(PATH_TO_IMAGES_FOLDER), HttpStatus.OK);
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
	
	
	private List<PlatformModel> getPlatformsById(List<Integer> idList) {
		if(idList == null) return null;
		
		List<PlatformModel> platforms = new ArrayList<>();
		
		for(int platform_id : idList) {
			PlatformModel platform = platformRepo.findById(platform_id);
			
			platforms.add(platform);
		}
		
		return platforms;
	}
	
	
	private String generateRandomString() {
		int length = 30;
		boolean useLetters = true;
		boolean useNumbers = true;
		return RandomStringUtils.random(length, useLetters, useNumbers);
	}
	
}
