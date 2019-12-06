package uni.fmi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fmi.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {

	public UserModel findByUsername(String username);
	
	public UserModel findUserByUsernameAndPassword(String username, String password);
	
}
