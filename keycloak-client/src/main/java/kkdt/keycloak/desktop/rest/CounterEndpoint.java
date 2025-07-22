package kkdt.keycloak.desktop.rest;

import kkdt.keycloak.desktop.Counter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class CounterEndpoint {
    private static final AtomicInteger counter = new AtomicInteger(0);

    @RequestMapping(value = "/counter", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Counter> counter() {
        Counter results = new Counter()
            .with(c -> c.value = counter.incrementAndGet());
        return ResponseEntity.ok(results);
    }
}
