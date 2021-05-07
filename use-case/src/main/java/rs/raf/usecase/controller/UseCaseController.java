package rs.raf.usecase.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.usecase.domain.ClassDiagram;
import rs.raf.usecase.dto.ModelDTO;
import rs.raf.usecase.service.UseCaseService;

import java.util.List;

@RestController
public class UseCaseController {

    private final UseCaseService useCaseService;

    public UseCaseController(UseCaseService useCaseService) {
        this.useCaseService = useCaseService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<ModelDTO>> list(@RequestParam String teamName) {

        return new ResponseEntity<>(useCaseService.list(teamName), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ClassDiagram> save(@RequestBody ClassDiagram classDiagram) {

        return new ResponseEntity<>(useCaseService.save(classDiagram), HttpStatus.CREATED);
    }

    @GetMapping("/read")
    public ResponseEntity<ClassDiagram> read(@RequestParam Long id) {

        return new ResponseEntity<>(useCaseService.read(id), HttpStatus.OK);
    }
}
