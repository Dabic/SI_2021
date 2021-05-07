package rs.raf.modelvalidator.services;

import org.springframework.stereotype.Service;
import rs.raf.modelvalidator.domain.ClassDiagram;

@Service
public interface ValidatorService {
    public void validate(ClassDiagram classDiagram);
}
