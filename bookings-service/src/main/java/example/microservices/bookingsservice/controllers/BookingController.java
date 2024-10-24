package example.microservices.bookingsservice.controllers;

import example.microservices.bookingsservice.dto.BookingDTO;
import example.microservices.bookingsservice.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/user")
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
        Optional<BookingDTO> createdBooking = bookingService.createBooking(bookingDTO);
        return createdBooking.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/getAll")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.findAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable UUID id) {
        Optional<BookingDTO> bookingDTO = bookingService.findBookingById(id);
        return bookingDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@RequestBody BookingDTO bookingDTO, @PathVariable UUID id) {
        Optional<BookingDTO> updatedBooking = bookingService.updateBookingById(bookingDTO, id);
        return updatedBooking.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID id) {
        if (bookingService.deleteBookingById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
