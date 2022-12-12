package source.restaurant_web_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.models.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    Address findAddressById(Long id);
}
