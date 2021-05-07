package rs.raf.broker.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rs.raf.broker.domain.Endpoint;
import rs.raf.broker.domain.Role;
import rs.raf.broker.domain.ServiceEntity;
import rs.raf.broker.exceptions.CustomHttpException;
import rs.raf.broker.persistance.EndpointRepository;
import rs.raf.broker.persistance.RoleRepository;
import rs.raf.broker.persistance.ServiceRepository;
import rs.raf.broker.services.ServiceAdministrationService;

@Service
public class ServiceAdministrationServiceImpl implements ServiceAdministrationService {

    private final ServiceRepository serviceRepository;
    private final EndpointRepository endpointRepository;
    private final RoleRepository roleRepository;
    private final Logger logger = LoggerFactory.getLogger(ServiceAdministrationServiceImpl.class);

    public ServiceAdministrationServiceImpl(ServiceRepository serviceRepository, EndpointRepository endpointRepository, RoleRepository roleRepository) {
        this.serviceRepository = serviceRepository;
        this.endpointRepository = endpointRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void save(ServiceEntity service) {
        ifPortRegisteredThrowException(service);
        service.getEndpoints().forEach(this::ifEndpointExistsTrowException);
        service.getEndpoints().forEach(this::ifEndpointMethodBadThrowException);
        service.getEndpoints().forEach(endpoint -> endpoint.getRoles().forEach(this::ifRoleNotFoundThrowException));
        serviceRepository.save(service);
        logger.info("{} on [{}:{}] service successfully registered!", service.getName(), service.getDomain(), service.getPort());
    }

    @Override
    public void update(ServiceEntity service) {
        ServiceEntity serviceToUpdate = getServiceByIdIfNotFoundThrowException(service);
        if (!serviceToUpdate.getPort().equals(service.getPort())) {
            ifPortRegisteredThrowException(service);
            serviceToUpdate.setPort(service.getPort());
        }
        serviceToUpdate.setDomain(service.getDomain());
        serviceToUpdate.setEndpoints(service.getEndpoints());
        serviceRepository.save(serviceToUpdate);
    }

    @Override
    public void delete(String serviceId) {
        ServiceEntity toDelete = new ServiceEntity();
        toDelete.setName(serviceId);
        ServiceEntity service = getServiceByIdIfNotFoundThrowException(toDelete);
        serviceRepository.delete(service);
    }

    public void ifEndpointExistsTrowException(Endpoint endpoint) {
        if (endpointRepository.findById(endpoint.getName()).isPresent()) {
            throw new CustomHttpException(
                    String.format("Endpoint %s already exists!", endpoint.getName()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public void ifRoleNotFoundThrowException(Role role) {
        roleRepository.findById(role.getName())
                .orElseThrow(() -> new CustomHttpException(
                        String.format("Role %s not found!", role.getName()),
                        HttpStatus.NOT_FOUND));
    }

    public void ifPortRegisteredThrowException(ServiceEntity service) {
        if (serviceRepository.findByPort(service.getPort()).isPresent()) {
            throw new CustomHttpException(
                    String.format("Port %s already registered!", service.getPort()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public void ifEndpointMethodBadThrowException(Endpoint endpoint) {
        if (!endpoint.getMethod().equals(HttpMethod.GET.name()) && !endpoint.getMethod().equals(HttpMethod.POST.name())) {
            throw new CustomHttpException(
                    String.format("Endpoint method %s is incorrect!", endpoint.getMethod()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public ServiceEntity getServiceByIdIfNotFoundThrowException(ServiceEntity service) {
        return serviceRepository.findById(service.getName())
                .orElseThrow(() -> new CustomHttpException(
                        String.format("Service %s not found!", service.getName()),
                        HttpStatus.NOT_FOUND));
    }
}
