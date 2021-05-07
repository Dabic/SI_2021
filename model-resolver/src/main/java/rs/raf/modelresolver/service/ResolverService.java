package rs.raf.modelresolver.service;

import org.springframework.stereotype.Service;
import rs.raf.modelresolver.dto.ModelDTO;

import java.util.List;

@Service
public interface ResolverService {

    List<ModelDTO> resolveAllModelsForTeamName(String teamName);
}
