package rs.raf.broker.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rs.raf.broker.domain.Endpoint;
import rs.raf.broker.domain.ServiceEntity;
import rs.raf.broker.exceptions.CustomHttpException;
import rs.raf.broker.persistance.EndpointRepository;
import rs.raf.broker.persistance.ServiceRepository;
import rs.raf.broker.services.BrokerService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class BrokerServiceImpl implements BrokerService {

    private final RestTemplate restTemplate;
    private final ServiceRepository serviceRepository;
    private final EndpointRepository endpointRepository;
    private final Logger logger = LoggerFactory.getLogger(BrokerServiceImpl.class);

    public BrokerServiceImpl(RestTemplate restTemplate, ServiceRepository serviceRepository, EndpointRepository endpointRepository) {

        this.restTemplate = restTemplate;
        this.serviceRepository = serviceRepository;
        this.endpointRepository = endpointRepository;
    }

    @Override
    public ResponseEntity<?> chainRequest(HttpServletRequest request) throws IOException {

        String endpointPath = parseRequestURIAndResolveServiceEndpointPath(request.getRequestURI(),
                request.getMethod());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity;
        if (request.getMethod().equals(HttpMethod.GET.name())) {
            httpEntity = new HttpEntity<>(headers);
            return restTemplate.exchange(endpointPath + "?" + request.getQueryString(), HttpMethod.GET, httpEntity, String.class);
        }
        httpEntity = new HttpEntity<>(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())), headers);
        try {
            return restTemplate.exchange(endpointPath + "?" + request.getQueryString(), HttpMethod.POST, httpEntity, String.class);
        } catch (HttpClientErrorException e) {
            throw new CustomHttpException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public String parseRequestURIAndResolveServiceEndpointPath(String URI, String method) {

        String[] paths = URI.split("/");
        int serviceName = 3;
        int endpointName = 4;

        ServiceEntity service = serviceRepository.findById(paths[serviceName])
                .orElseThrow(() -> new CustomHttpException(
                        String.format("Service %s not found!", paths[serviceName]),
                        HttpStatus.NOT_FOUND));

        Endpoint endpoint = endpointRepository.findById(paths[serviceName] + "_" + paths[endpointName])
                .orElseThrow(() -> new CustomHttpException(
                        String.format("Endpoint %s not found!", paths[endpointName]),
                        HttpStatus.NOT_FOUND));

        if (!endpoint.getMethod().equals(method)) {
            throw new CustomHttpException(
                    String.format("Endpoint %s not registered with given method %s!", paths[endpointName], method),
                    HttpStatus.NOT_FOUND);
        }

        if (service.getEndpoints().stream().noneMatch(ep -> ep.equals(endpoint))) {
            throw new CustomHttpException(
                    String.format("Endpoint %s not registered with given service!", paths[endpointName]),
                    HttpStatus.NOT_FOUND);
        }

        boolean hasRole = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(role -> new ArrayList<>(endpoint.getRoles())
                        .stream()
                        .anyMatch(endpointRole -> endpointRole.getAuthority().equals(role.getAuthority())));
        if (!hasRole) {
            throw new CustomHttpException(
                    String.format("Unauthorized to access %s!", paths[endpointName]),
                    HttpStatus.UNAUTHORIZED);
        }

        String endpointPath = String.format("http://%s:%s%s", service.getDomain(), service.getPort(), endpoint.getPath());
        logger.info("Broker routing: [{}][{}] - {}", method, paths[serviceName], endpointPath);
        return endpointPath;
    }
}
