package example.microservices.bookingsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightDTO {

    private UUID id;
    private String flightNumber;
    private String origin;
    private String destination;
    private String departureTime;

}
