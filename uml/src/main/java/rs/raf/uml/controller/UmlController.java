package rs.raf.uml.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.uml.domain.ClassDiagram;
import rs.raf.uml.dto.ModelDTO;
import rs.raf.uml.services.UmlService;

import java.util.List;

@RestController
public class UmlController {

    private final UmlService umlService;

    public UmlController(UmlService umlService) {
        this.umlService = umlService;
    }

    @PostMapping("/save")
    public ResponseEntity<ClassDiagram> save(@RequestBody ClassDiagram classDiagram) {

        return new ResponseEntity<>(umlService.save(classDiagram), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ModelDTO>> list(@RequestParam String teamName) {

        return new ResponseEntity<>(umlService.list(teamName), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@RequestBody ClassDiagram classDiagram) {

        umlService.delete(classDiagram);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/read")
    public ResponseEntity<ClassDiagram> read(@RequestParam Long id) {

        return new ResponseEntity<>(umlService.read(id), HttpStatus.OK);
    }
}
