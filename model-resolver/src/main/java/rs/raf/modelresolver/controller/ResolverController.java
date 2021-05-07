package rs.raf.modelresolver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.modelresolver.dto.ModelDTO;
import rs.raf.modelresolver.service.ResolverService;

import java.util.List;

@RestController
public class ResolverController {

    private final ResolverService resolverService;

    public ResolverController(ResolverService resolverService) {

        this.resolverService = resolverService;
    }

    @GetMapping("/read")
    public ResponseEntity<List<ModelDTO>> resolveModels(@RequestParam String teamName) {

        return new ResponseEntity<>(resolverService.resolveAllModelsForTeamName(teamName), HttpStatus.OK);
    }
}
