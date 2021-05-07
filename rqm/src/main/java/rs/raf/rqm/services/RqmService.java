package rs.raf.rqm.services;

import org.springframework.stereotype.Service;
import rs.raf.rqm.domain.RqmDiagram;
import rs.raf.rqm.dto.ModelDTO;

import java.util.List;

@Service
public interface RqmService {

    RqmDiagram save(RqmDiagram rqmDiagram);
    void delete(Long id);
    List<ModelDTO> list(String teamName);
    RqmDiagram read(Long id);

}
