package rs.raf.usecase.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rs.raf.usecase.domain.ClassDiagram;
import rs.raf.usecase.dto.ModelDTO;
import rs.raf.usecase.exceptions.CustomHttpException;
import rs.raf.usecase.persistance.ClassDiagramRepository;
import rs.raf.usecase.service.UseCaseService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UseCaseServiceImpl implements UseCaseService {

    private final ClassDiagramRepository classDiagramRepository;
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(UseCaseServiceImpl.class);

    public UseCaseServiceImpl(ClassDiagramRepository classDiagramRepository, RestTemplate restTemplate) {
        this.classDiagramRepository = classDiagramRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public ClassDiagram save(ClassDiagram classDiagram) {

        checkIfModelValid(classDiagram);
        ifClassDiagramExistsDelete(classDiagram.getId());
        return classDiagramRepository.save(classDiagram);
    }

    @Override
    public List<ModelDTO> list(String teamName) {

        return classDiagramRepository
                .findAllByTeamName(teamName)
                .stream()
                .map(classDiagram -> new ModelDTO(classDiagram.getId(), classDiagram.getName())).collect(Collectors.toList());
    }

    @Override
    public ClassDiagram read(Long id) {

        return classDiagramRepository.findById(id).orElse(null);
    }

    public void ifClassDiagramExistsDelete(Long id) {

        if (id != null) {
            if (classDiagramRepository.findById(id).isPresent()) {
                classDiagramRepository.deleteById(id);
            }
        }
    }

    public void checkIfModelValid(ClassDiagram classDiagram) {

        classDiagram.setModelType("use-case");
        HttpEntity<ClassDiagram> httpEntity = new HttpEntity<>(classDiagram);
        try {
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:8085/validate", HttpMethod.POST, httpEntity, String.class);
        } catch (HttpClientErrorException e) {
            logger.error("Model is not valid! " + e.getResponseBodyAsString());
            throw new CustomHttpException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }
}
