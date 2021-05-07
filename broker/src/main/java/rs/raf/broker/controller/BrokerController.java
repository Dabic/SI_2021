package rs.raf.broker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.broker.services.BrokerService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/broker")
public class BrokerController {

    private final BrokerService brokerService;

    public BrokerController(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @RequestMapping(value = "**")
    public ResponseEntity<?> broker (HttpServletRequest request) throws IOException {
        return brokerService.chainRequest(request);
    }
}
