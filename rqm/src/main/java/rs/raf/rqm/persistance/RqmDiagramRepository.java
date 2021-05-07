package rs.raf.rqm.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.rqm.domain.RqmDiagram;

import java.util.List;

public interface RqmDiagramRepository extends JpaRepository<RqmDiagram, Long> {

    List<RqmDiagram> findAllByTeamName(String teamName);
}
