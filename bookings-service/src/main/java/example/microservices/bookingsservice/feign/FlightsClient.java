package example.microservices.bookingsservice.feign;

import example.microservices.bookingsservice.dto.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "flights-service", path = "api/flights")
public interface FlightsClient {

    @GetMapping("/{id}")
    ResponseEntity<FlightDTO> getFlightById(@PathVariable UUID id);

    @GetMapping("/findByNumber/{flightNumber}")
    ResponseEntity<FlightDTO> getFlightByFlightNumber(@PathVariable String flightNumber);
}
