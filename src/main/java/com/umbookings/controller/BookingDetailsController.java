package com.umbookings.controller;

import com.umbookings.model.BookingDetails;
import com.umbookings.service.BookingDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
/**
 * @author Shrikar Kalagi
 *
 */
@RestController
@CrossOrigin
public class BookingDetailsController {
    
    @Autowired
    private BookingDetailService bookingDetailService;

    @GetMapping("/booking")
    public Page<BookingDetails> getBookings(Pageable pageable) {
        return bookingDetailService.findAll(pageable);
    }

    @GetMapping("/booking/{bookingId}")
    public BookingDetails getBooking(@PathVariable Long bookingId) {
        return bookingDetailService.findById(bookingId);
    }
    
    @PostMapping("/booking")
    public BookingDetails createQuestion(@Valid @RequestBody BookingDetails bookingDetails) {
        return bookingDetailService.save(bookingDetails);
    }

  @PutMapping("/booking/{bookingId}")
  public BookingDetails updateQuestion(@PathVariable Long bookingId,
                                 @Valid @RequestBody BookingDetails bookingDetail) {

	  return bookingDetailService.update(bookingDetail, bookingId);
  }
    @DeleteMapping("/booking/{bookingId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long bookingId) {

    	bookingDetailService.delete(bookingId);
    	return ResponseEntity.ok().build();
    }
}
