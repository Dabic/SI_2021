package rs.raf.rqm.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.rqm.domain.RqmRow;

public interface RqmRowRepository extends JpaRepository<RqmRow, Long> {
}
