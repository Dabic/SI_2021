package rs.raf.usecase.service;

import org.springframework.stereotype.Service;
import rs.raf.usecase.domain.ClassDiagram;
import rs.raf.usecase.dto.ModelDTO;

import java.util.List;

@Service
public interface UseCaseService {
    ClassDiagram save(ClassDiagram classDiagram);
    List<ModelDTO> list(String teamName);
    ClassDiagram read(Long id);
}
