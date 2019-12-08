package uni.fmi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fmi.models.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Integer>{

	RoleModel findRoleByName(String roleName);
	
}
