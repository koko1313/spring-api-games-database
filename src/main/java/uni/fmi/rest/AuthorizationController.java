package uni.fmi.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import uni.fmi.models.RoleModel;
import uni.fmi.models.UserModel;
import uni.fmi.repositories.RoleRepository;
import uni.fmi.repositories.UserRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "true")
@RestController
public class AuthorizationController {

	private UserDetailsService userDetailsService;
	private UserRepository userRepo;
	private RoleRepository roleRepo;
	
	private PasswordEncoder passwordEncoder;

	public AuthorizationController(UserDetailsService userDetailsService, UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.passwordEncoder = passwordEncoder;
	}
	

	@PostMapping(path = "/login")
	public ResponseEntity<UserModel> login(
			@RequestParam(value = "username") String username, 
			@RequestParam(value = "password") String password) {
		
		UserModel user = userRepo.findByUsername(username);

		if(user != null) {
			// if the password matches
			if(passwordEncoder.matches(password, user.getPassword())) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
	
				if(userDetails != null) {
					Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
	
				return new ResponseEntity<>(user, HttpStatus.OK);
			}
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	@PostMapping(path = "/register")
	public ResponseEntity<UserModel> register(
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "repassword") String repassword) {
		
		// if username or password is null
		if(username.equals("") || password.equals("")) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		// if there is already user with this name
		if(userRepo.findByUsername(username) != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		// if both passwords does not match
		if(!password.equals(repassword)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		UserModel user = new UserModel();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		
		// set ROLE_USER
		List<RoleModel> roles = new ArrayList<RoleModel>();
		RoleModel role = roleRepo.findRoleByName("ROLE_USER");
		roles.add(role);
		
		user.setRoles(roles);
		
		user = userRepo.saveAndFlush(user);
		
		if(user != null) {
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	

	@PostMapping(path = "/logout-user")
	public boolean logout(HttpSession session) {
		session.invalidate();

		return true;
	}
	
	
	@GetMapping(path = "/getWhoAmI")
	public ResponseEntity<UserModel> getWhoAmI(Authentication authentication) {
		
		// if there is no authenticated user
		if(authentication == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		UserModel loggedUser = userRepo.findByUsername(loggedUsername);
		
		return new ResponseEntity<>(loggedUser, HttpStatus.OK);
	}
	
}
