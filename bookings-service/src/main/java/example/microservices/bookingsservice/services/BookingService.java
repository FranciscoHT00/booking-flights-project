package example.microservices.bookingsservice.services;

import example.microservices.bookingsservice.dto.BookingDTO;
import example.microservices.bookingsservice.entities.Booking;
import example.microservices.bookingsservice.feign.FlightsClient;
import example.microservices.bookingsservice.repositories.BookingRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightsClient flightsClient;

    @Autowired
    public BookingService(BookingRepository bookingRepository, FlightsClient flightsClient) {
        this.bookingRepository = bookingRepository;
        this.flightsClient = flightsClient;
    }

    public Optional<BookingDTO> createBooking(BookingDTO bookingDTO) {

        try{
            ResponseEntity<?> response = flightsClient.getFlightByFlightNumber(bookingDTO.getFlightNumber());
            if(response.getStatusCode().is2xxSuccessful()) {
                Booking booking = mapToEntity(bookingDTO);
                Booking savedBooking = bookingRepository.save(booking);
                bookingDTO = mapToDTO(savedBooking);
                return Optional.of(bookingDTO);
            }else {
                return Optional.empty();
            }
        }catch (FeignException.NotFound e){
            return Optional.empty();
        }
    }

    public List<BookingDTO> findAllBookings() {

        return StreamSupport.stream(bookingRepository.findAll().spliterator(), false)
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }

    public Optional<BookingDTO> findBookingById(UUID id) {

        return bookingRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<BookingDTO> updateBookingById(BookingDTO bookingDTO, UUID id) {

        Optional<Booking> optional = bookingRepository.findById(id);
        if (optional.isPresent()) {
            Booking existingBooking = optional.get();
            existingBooking.setFlightNumber(bookingDTO.getFlightNumber());
            existingBooking.setPassengerName(bookingDTO.getPassengerName());

            Booking savedBooking = bookingRepository.save(existingBooking);
            return Optional.of(mapToDTO(savedBooking));
        }

        return Optional.empty();

    }

    public boolean deleteBookingById(UUID id) {
        Optional<Booking> optional = bookingRepository.findById(id);
        if (optional.isPresent()) {
            bookingRepository.delete(optional.get());
            return true;
        } else {
            return false;
        }
    }

    private Booking mapToEntity(BookingDTO bookingDTO) {
        return Booking.builder()
                .flightNumber(bookingDTO.getFlightNumber())
                .passengerName(bookingDTO.getPassengerName())
                .build();
    }

    private BookingDTO mapToDTO(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .flightNumber(booking.getFlightNumber())
                .passengerName(booking.getPassengerName())
                .build();
    }
}
