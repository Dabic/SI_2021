package rs.raf.broker.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public interface BrokerService {

    ResponseEntity<?> chainRequest(HttpServletRequest request) throws IOException;
}
