package pl.training.performance.reports.adapters.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ReportsRestController {

    @GetMapping("api/uuids")
    public String test() {
        return UUID.randomUUID().toString();
    }

}
