package rs.raf.uml.services;

import org.springframework.stereotype.Service;
import rs.raf.uml.domain.ClassDiagram;
import rs.raf.uml.dto.ModelDTO;

import java.util.List;

@Service
public interface UmlService {

    ClassDiagram save(ClassDiagram classDiagram);
    List<ModelDTO> list(String teamName);
    ClassDiagram read(Long id);
    void delete(ClassDiagram classDiagram);
}
