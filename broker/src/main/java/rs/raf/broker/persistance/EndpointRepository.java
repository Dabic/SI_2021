package rs.raf.broker.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.broker.domain.Endpoint;

@Repository
public interface EndpointRepository extends JpaRepository<Endpoint, String> {
}
