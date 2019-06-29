package com.example.umbookings.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.umbookings.exception.ResourceNotFoundException;
import com.example.umbookings.model.BookingDetails;
import com.example.umbookings.repository.BookingDetailsRepository;

@Service
public class BookingDetailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(BookingDetailService.class);

	@Autowired
	private BookingDetailsRepository bookingDetailsRepository;
	
	public Page<BookingDetails> findAll(Pageable pageable)
	{
		return bookingDetailsRepository.findAll(pageable);
	}
	
	public BookingDetails save(BookingDetails bookingDetails)
	{
		LOG.info("Booking id {} created successfully", bookingDetails.getId());
		return bookingDetailsRepository.save(bookingDetails);
	}
	
	public void delete(Long id)
	{
		BookingDetails currentBooking = bookingDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Booking id not found - " + id));
        bookingDetailsRepository.deleteById(currentBooking.getId());
        LOG.info("Booking id {} deleted", id);
	}
	
	public BookingDetails update( BookingDetails bookingDetail , Long id)
	{
		BookingDetails currentBooking = bookingDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Booking id not found - " + id));

		currentBooking.setBookingDate(bookingDetail.getBookingDate());
		currentBooking.setMobileNbr(bookingDetail.getMobileNbr());
		currentBooking.setEmailIdentifier(bookingDetail.getEmailIdentifier());
		currentBooking.setPersonCount(bookingDetail.getPersonCount());
		currentBooking.setBookingType(bookingDetail.getBookingType());
		currentBooking.setBookingStatus(bookingDetail.getBookingStatus());
		bookingDetailsRepository.save(currentBooking);
		LOG.info("Booking id {} updated", id);
		return currentBooking;
	}

	public BookingDetails findById(Long bookingId) {
		return bookingDetailsRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking id not found - " + bookingId));
	}
	
}
