package uni.fmi.repositories;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fmi.models.GameModel;

@Repository
public interface GameRepository extends JpaRepository<GameModel, Integer> {

	public GameModel findById(int id);
	
	public GameModel findByName(String name);
	
	public HashSet<GameModel> findByGenresIdIn(List<Integer> genres_id_list);
	
	public HashSet<GameModel> findByPlatformsIdIn(List<Integer> platforms_id_list);
	
	public HashSet<GameModel> findByGenresIdInAndPlatformsIdIn(List<Integer> genres_id_list, List<Integer> platforms_id_list);
	
}
