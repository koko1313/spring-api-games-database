package uni.fmi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fmi.models.GenreModel;

@Repository
public interface GenreRepository extends JpaRepository<GenreModel, Integer> {

	public GenreModel findById(int id);
	
	public GenreModel findByName(String name);
	
}
