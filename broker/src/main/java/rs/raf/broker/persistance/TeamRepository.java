package rs.raf.broker.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.broker.domain.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {
}
