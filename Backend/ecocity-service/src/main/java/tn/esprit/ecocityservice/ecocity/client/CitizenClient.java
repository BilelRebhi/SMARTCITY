package tn.esprit.ecocityservice.ecocity.client;

import tn.esprit.ecocityservice.ecocity.client.dto.CitizenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** Synchronous call to MS1 (Citizen), found through Eureka by its logical name. */
@FeignClient(name = "${ms1.service-name}")
public interface CitizenClient {

    @GetMapping("/api/citizens/{id}")
    CitizenDTO getCitizen(@PathVariable("id") Long id);
}
