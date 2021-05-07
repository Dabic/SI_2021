package rs.raf.modelvalidator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.modelvalidator.domain.ClassDiagram;
import rs.raf.modelvalidator.services.ValidatorService;

@RestController
public class ValidatorController {

    private final ValidatorService validatorService;

    public ValidatorController(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody ClassDiagram classDiagram) {
        validatorService.validate(classDiagram);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
