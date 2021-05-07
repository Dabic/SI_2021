package rs.raf.usecase.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.usecase.domain.ClassDiagram;

import java.util.List;

@Repository
public interface ClassDiagramRepository extends JpaRepository<ClassDiagram, Long> {

    List<ClassDiagram> findAllByTeamName(String teamName);
}
