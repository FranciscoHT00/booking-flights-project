package example.microservices.flightsservice.services;

import example.microservices.flightsservice.dto.FlightDTO;
import example.microservices.flightsservice.entities.Flight;
import example.microservices.flightsservice.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public FlightDTO createFlight(FlightDTO flightDTO) {

        Flight flight = mapToEntity(flightDTO);
        Flight savedFlight = flightRepository.save(flight);
        flightDTO = mapToDTO(savedFlight);
        return flightDTO;
    }

    public List<FlightDTO> findAllFlights() {

        return StreamSupport.stream(flightRepository.findAll().spliterator(), false)
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }

    public Optional<FlightDTO> findFlightById(UUID id) {

        return flightRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<FlightDTO> updateFlightById(FlightDTO flightDTO, UUID id) {

        Optional<Flight> optional = flightRepository.findById(id);
        if (optional.isPresent()) {
            Flight existingFlight = optional.get();
            existingFlight.setFlightNumber(flightDTO.getFlightNumber());
            existingFlight.setOrigin(flightDTO.getOrigin());
            existingFlight.setDestination(flightDTO.getDestination());
            existingFlight.setDepartureTime(flightDTO.getDepartureTime());

            Flight savedFlight = flightRepository.save(existingFlight);
            return Optional.of(mapToDTO(savedFlight));
        }

        return Optional.empty();

    }

    public boolean deleteFlightById(UUID id) {
        Optional<Flight> optional = flightRepository.findById(id);
        if (optional.isPresent()) {
            flightRepository.delete(optional.get());
            return true;
        } else {
            return false;
        }
    }

    private Flight mapToEntity(FlightDTO flightDTO) {
        return Flight.builder()
                .flightNumber(flightDTO.getFlightNumber())
                .origin(flightDTO.getOrigin())
                .destination(flightDTO.getDestination())
                .departureTime(flightDTO.getDepartureTime())
                .build();
    }

    private FlightDTO mapToDTO(Flight flight) {
        return FlightDTO.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .origin(flight.getOrigin())
                .destination(flight.getDestination())
                .departureTime(flight.getDepartureTime())
                .build();
    }
}
