package uni.fmi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fmi.models.DeveloperModel;

@Repository
public interface DeveloperRepository extends JpaRepository<DeveloperModel, Integer> {

	public DeveloperModel findById(int id);
	
	public DeveloperModel findByName(String name);
	
}
