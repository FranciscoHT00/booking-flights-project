package example.microservices.flightsservice.repositories;

import example.microservices.flightsservice.entities.Flight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlightRepository extends CrudRepository<Flight, UUID> {
}
