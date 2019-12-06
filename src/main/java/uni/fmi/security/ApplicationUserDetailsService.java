package uni.fmi.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uni.fmi.models.RoleModel;
import uni.fmi.models.UserModel;
import uni.fmi.repositories.UserRepository;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

	private UserRepository userRepo;

	public ApplicationUserDetailsService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserModel user = (UserModel) userRepo.findByUsername(username);
		
		if(user == null) {
			throw new UsernameNotFoundException(username);
		}

		Set<RoleModel> roles = new HashSet<>(user.getRoles());

		return new UserPrincipal(user, roles);
	}

} 