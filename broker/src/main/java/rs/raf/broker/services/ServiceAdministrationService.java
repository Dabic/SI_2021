package rs.raf.broker.services;

import org.springframework.stereotype.Service;
import rs.raf.broker.domain.ServiceEntity;

@Service
public interface ServiceAdministrationService {

    void save(ServiceEntity service);
    void update(ServiceEntity service);
    void delete(String serviceId);
}
