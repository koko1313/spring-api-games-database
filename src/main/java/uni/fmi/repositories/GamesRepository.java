package uni.fmi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fmi.models.GameModel;

@Repository
public interface GamesRepository extends JpaRepository<GameModel, Integer> {

}
