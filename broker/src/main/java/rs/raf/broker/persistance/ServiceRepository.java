package rs.raf.broker.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.broker.domain.ServiceEntity;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, String> {

    Optional<ServiceEntity> findByDomain(String domain);
    Optional<ServiceEntity> findByPort(Integer port);
}
