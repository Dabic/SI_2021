package rs.raf.broker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.broker.domain.ServiceEntity;
import rs.raf.broker.services.ServiceAdministrationService;

@RestController
@RequestMapping("/api/services")
public class ServiceAdministrationController {

    private final ServiceAdministrationService serviceAdministrationService;

    public ServiceAdministrationController(ServiceAdministrationService serviceAdministrationService) {
        this.serviceAdministrationService = serviceAdministrationService;
    }

    @PostMapping
    public ResponseEntity<ServiceEntity> save(@RequestBody ServiceEntity service) {
        serviceAdministrationService.save(service);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ServiceEntity> update(@RequestBody ServiceEntity service) {
        serviceAdministrationService.update(service);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<ServiceEntity> delete(@RequestParam("serviceId") String serviceId) {
        serviceAdministrationService.delete(serviceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
