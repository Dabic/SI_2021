package rs.raf.modelresolver.service.impl;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.raf.modelresolver.dto.ModelDTO;
import rs.raf.modelresolver.service.ResolverService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResolverServiceImpl implements ResolverService {

    private final RestTemplate restTemplate;

    public ResolverServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ModelDTO> resolveAllModelsForTeamName(String teamName) {
        List<ModelDTO> finalListOfModelDTOs = new ArrayList<>();
        ResponseEntity<List<ModelDTO>> responseRQM = restTemplate.exchange("http://localhost:8081/list?teamName=" + teamName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ModelDTO>>(){});
        ResponseEntity<List<ModelDTO>> responseUseCase = restTemplate.exchange("http://localhost:8082/list?teamName=" + teamName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ModelDTO>>(){});
        ResponseEntity<List<ModelDTO>> responseUML = restTemplate.exchange("http://localhost:8083/list?teamName=" + teamName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ModelDTO>>(){});
        if (responseUseCase.getBody() != null && responseUML.getBody() != null && responseRQM.getBody() != null) {
            finalListOfModelDTOs.addAll(responseUML.getBody());
            finalListOfModelDTOs.addAll(responseUseCase.getBody());
            finalListOfModelDTOs.addAll(responseRQM.getBody());
        }
        return finalListOfModelDTOs;
    }
}
