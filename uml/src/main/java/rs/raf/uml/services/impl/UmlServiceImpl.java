package rs.raf.uml.services.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rs.raf.uml.domain.ClassDiagram;
import rs.raf.uml.dto.ModelDTO;
import rs.raf.uml.exceptions.CustomHttpException;
import rs.raf.uml.persistance.ClassDiagramRepository;
import rs.raf.uml.services.UmlService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UmlServiceImpl implements UmlService {

    private final ClassDiagramRepository classDiagramRepository;
    private final RestTemplate restTemplate;

    public UmlServiceImpl(ClassDiagramRepository classDiagramRepository, RestTemplate restTemplate) {
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

    @Override
    public void delete(ClassDiagram classDiagram) {

        findAndThrowExceptionIfNotFound(classDiagram);
        classDiagramRepository.deleteById(classDiagram.getId());
    }

    public void findAndThrowExceptionIfNotFound(ClassDiagram classDiagram) {
        classDiagramRepository.findById(classDiagram.getId())
                .orElseThrow(() -> new CustomHttpException(
                        String.format("Class diagram %s not found!", classDiagram.getId()),
                        HttpStatus.NOT_FOUND));
    }

    public void ifClassDiagramExistsDelete(Long id) {
        if (id != null) {
            if (classDiagramRepository.findById(id).isPresent()) {
                classDiagramRepository.deleteById(id);
            }
        }
    }

    public void checkIfModelValid(ClassDiagram classDiagram) {
        classDiagram.setModelType(new String("uml"));
        HttpEntity<ClassDiagram> httpEntity = new HttpEntity<>(classDiagram);
        try {
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:8085/validate", HttpMethod.POST, httpEntity, String.class);
        } catch (HttpClientErrorException e) {
            throw new CustomHttpException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }
}
