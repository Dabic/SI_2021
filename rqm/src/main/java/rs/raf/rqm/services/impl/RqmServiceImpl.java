package rs.raf.rqm.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rs.raf.rqm.domain.RqmDiagram;
import rs.raf.rqm.dto.ModelDTO;
import rs.raf.rqm.exceptions.CustomHttpException;
import rs.raf.rqm.persistance.RqmDiagramRepository;
import rs.raf.rqm.persistance.RqmRowRepository;
import rs.raf.rqm.services.RqmService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RqmServiceImpl implements RqmService {

    private final RqmDiagramRepository rqmDiagramRepository;
    private final RqmRowRepository rqmRowRepository;

    public RqmServiceImpl(RqmDiagramRepository rqmDiagramRepository, RqmRowRepository rqmRowRepository) {

        this.rqmDiagramRepository = rqmDiagramRepository;
        this.rqmRowRepository = rqmRowRepository;
    }

    @Override
    public RqmDiagram save(RqmDiagram rqmDiagram) {

        deleteRqmDiagram(rqmDiagram);
        return rqmDiagramRepository.save(rqmDiagram);
    }

    @Override
    public void delete(Long id) {

        rqmDiagramRepository.deleteById(id);
    }

    @Override
    public List<ModelDTO> list(String teamName) {

        return rqmDiagramRepository.findAllByTeamName(teamName)
                .stream()
                .map(diagram -> new ModelDTO(diagram.getId(), diagram.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public RqmDiagram read(Long id) {

        return rqmDiagramRepository.findById(id)
                .orElseThrow(() -> new CustomHttpException(
                        String.format("RQM Diagram %s not found!", id),
                        HttpStatus.NOT_FOUND));
    }

    public void deleteRqmDiagram(RqmDiagram rqmDiagram) {

        if (rqmDiagram.getId() != null) {
            rqmDiagramRepository.deleteById(rqmDiagram.getId());
        }
    }
}
