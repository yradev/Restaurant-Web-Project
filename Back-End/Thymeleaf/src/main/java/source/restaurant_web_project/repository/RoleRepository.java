package source.restaurant_web_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.model.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
