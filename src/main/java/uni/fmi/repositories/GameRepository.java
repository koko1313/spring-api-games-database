package uni.fmi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fmi.models.GameModel;

@Repository
public interface GameRepository extends JpaRepository<GameModel, Integer> {

	public GameModel findById(int id);
	
	public GameModel findByName(String name);
	
}
