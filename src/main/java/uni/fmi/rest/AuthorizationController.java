package uni.fmi.rest;

import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.fmi.models.UserModel;
import uni.fmi.repositories.UserRepository;

@RestController
public class AuthorizationController {

	private UserDetailsService userDetailsService;
	private UserRepository userRepo;

	public AuthorizationController(UserDetailsService userDetailsService, UserRepository userRepo) {
		this.userDetailsService = userDetailsService;
		this.userRepo = userRepo;
	}

	// have to be with PostMapping
	@PostMapping(path = "/login")
	public boolean login(
			@RequestParam(value = "username") String username, 
			@RequestParam(value = "password") String password) {

		UserModel user = userRepo.findUserByUsernameAndPassword(username, password);

		if(user != null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

			if(userDetails != null) {
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			return true;
		}

		return false;
	}

	// have to be with PostMapping
	@PostMapping(path = "/logout")
	public boolean logout(HttpSession session) {
		session.invalidate();

		return true;
	}
	
}
