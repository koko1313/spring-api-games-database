package uni.fmi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fmi.models.PlatformModel;

@Repository
public interface PlatformRepository extends JpaRepository<PlatformModel, Integer> {

	public PlatformModel findById(int id);
	
	public PlatformModel findByName(String name);
	
}
