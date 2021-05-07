package rs.raf.rqm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.rqm.domain.RqmDiagram;
import rs.raf.rqm.dto.ModelDTO;
import rs.raf.rqm.services.RqmService;

import java.util.List;

@RestController
public class RqmController {

    private final RqmService rqmService;

    public RqmController(RqmService rqmService) {
        this.rqmService = rqmService;
    }

    @GetMapping("/read")
    public ResponseEntity<RqmDiagram> read(@RequestParam Long id) {

        return new ResponseEntity<>(rqmService.read(id), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ModelDTO>> list(@RequestParam String teamName) {

        return new ResponseEntity<>(rqmService.list(teamName), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<RqmDiagram> save(@RequestBody RqmDiagram rqmDiagram) {

        return new ResponseEntity<>(rqmService.save(rqmDiagram), HttpStatus.CREATED);
    }

    @GetMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long id) {

        rqmService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
