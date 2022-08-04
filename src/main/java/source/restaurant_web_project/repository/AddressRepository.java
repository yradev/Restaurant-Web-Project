package source.restaurant_web_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.model.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
}
