package com.umbookings.controller;

import com.umbookings.dto.request.BookingDetailsDTO;
import com.umbookings.model.BookingDetails;

import com.umbookings.service.BookingDetailService;
import com.umbookings.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import org.springframework.security.core.Authentication;
/**
 * @author Shrikar Kalagi
 *
 */
@RestController
@CrossOrigin
public class BookingDetailsController {
    
    @Autowired
    private BookingDetailService bookingDetailService;
	
	@Autowired
	private NotificationService notificationService;
	
    @GetMapping("/booking")
    public Page<BookingDetails> getBookings(Pageable pageable) {
        return bookingDetailService.findAll(pageable);
    }

    @GetMapping("/booking/{bookingId}")
    public BookingDetails getBooking(@PathVariable Long bookingId) {
        return bookingDetailService.findById(bookingId);
    }
    
   @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/booking")
    public BookingDetailsDTO createBooking(@Valid @RequestBody BookingDetailsDTO bookingDetails, Authentication authentication) {
    	
        return bookingDetailService.saveBooking(bookingDetails);
    }

  @PutMapping("/booking/{bookingId}")
  public BookingDetails updateBooking(@PathVariable Long bookingId,
                                 @Valid @RequestBody BookingDetails bookingDetail) {

	  return bookingDetailService.update(bookingDetail, bookingId);
  }
    @DeleteMapping("/booking/{bookingId}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long bookingId) {

    	bookingDetailService.delete(bookingId);
    	return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('NORMAL_USER')")
    @GetMapping("/sendEmail/{emailId}")
    public String sendEmail(@PathVariable String emailId) {
    	
    	return notificationService.sendEmail(emailId, "Body", "Subject");
    }

    @PreAuthorize("hasAnyRole('NORMAL_USER')")
    @GetMapping("/sendSMS/{mobileNumber}")
    public String sendSMS(@PathVariable String mobileNumber, Authentication authentication) {
    	
    	 return notificationService.sendSMS(mobileNumber, "OTP 0000");
    }
    
    @GetMapping("/sendOTP")
    public String sendSMS(@RequestParam("mobileNumber") String mobileNumber, @RequestParam("emailId") String emailId) {
    	
    	 return bookingDetailService.sendOTP(mobileNumber, emailId);
    }
}
