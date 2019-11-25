package uni.fmi.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping(path = "/test")
	public ResponseEntity<String> doGet() {
		return new ResponseEntity<>("You just did a GET request", HttpStatus.OK);
	}
	
	@PostMapping(path = "/test")
	public ResponseEntity<String> doPost() {
		return new ResponseEntity<>("You just did a POST request", HttpStatus.OK);
	}
	
	@PutMapping(path = "/test")
	public ResponseEntity<String> doPut() {
		return new ResponseEntity<>("You just did a PUT request", HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/test")
	public ResponseEntity<String> doDelete() {
		return new ResponseEntity<>("You just did a DELETE request", HttpStatus.OK);
	}
	
}
