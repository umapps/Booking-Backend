
package com.umbookings.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.umbookings.repository.BookingDetailsRepository;
import com.umbookings.dto.request.BookingDetailsDTO;
import com.umbookings.exception.ResourceNotFoundException;
import com.umbookings.model.BookingDetails;
/**
 * @author Shrikar Kalagi
 *
 */
@Service
public class BookingDetailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(BookingDetailService.class);

	@Autowired
	private BookingDetailsRepository bookingDetailsRepository;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private RedisTemplate<String, Object> template;
	
	public Page<BookingDetails> findAll(Pageable pageable)
	{
		return bookingDetailsRepository.findAll(pageable);
	}
	
	public BookingDetails save(BookingDetails bookingDetails)
	{
		LOG.info("Booking created successfully for Mobile number - {} ", bookingDetails.getMobileNumber());
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
		currentBooking.setMobileNumber(bookingDetail.getMobileNumber());
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

	public String sendOTP(String mobileNumber, String emailId, Boolean sendSameOTPtoEMailSMS) {
		int emailOtp = generateOtp();
		int mobileOtp = generateOtp();
		String returnString = "";
		if (sendSameOTPtoEMailSMS)
		{
			// Make both OTP same, if we want to send same OTP to email and Mobile
			//Same OTP needed in Booking, Different OTP needed while Registering
			emailOtp = mobileOtp;

		}
		try {
			if (emailId.trim().length() > 0) {

				template.opsForValue().set( emailId, emailOtp );
				template.expire( emailId, 120, TimeUnit.MINUTES );
				String emailString = "UMAPPS > OTP is " + emailOtp;
				notificationService.sendEmail(emailId, emailString, emailString);
				returnString = returnString + "OTP sent successfully to " + emailId ;
			}
			if (mobileNumber.trim().length() > 0) {

				template.opsForValue().set( mobileNumber, mobileOtp );
				template.expire( mobileNumber, 200, TimeUnit.SECONDS );
				String smsString = "UMAPPS > OTP is " + mobileOtp;
				notificationService.sendSMS(mobileNumber, smsString);
				returnString = returnString + "  and  " +mobileNumber;
			}
			return  returnString;
		}
		catch (Exception e)
		{
			LOG.info("Sending OTP to Mobile nbr - {} and Email id {} failed with error {}",mobileNumber, emailId, e);
			return "OTP sending failed to" + emailId +"  and  " +mobileNumber;
		}
	}

	private int generateOtp() {
		Random rand = new Random();
		int otp = rand.nextInt(9999);
		if (otp < 1000)
		{
			otp = otp + 1000;
		}
		return otp;
	}

	public BookingDetailsDTO saveBooking(@Valid BookingDetailsDTO bookingDetailsDTO) {
		template.opsForHash().get("otpList", bookingDetailsDTO.getBookingDetails().getMobileNumber());

		if (template.opsForHash().get("otpList", bookingDetailsDTO.getBookingDetails().getMobileNumber()).equals(bookingDetailsDTO.getOtp().toString()))
		{
			bookingDetailsRepository.save(bookingDetailsDTO.getBookingDetails());
			bookingDetailsDTO.setStatus("Booking Created successfully");
		}
		else
		{
			bookingDetailsDTO.setStatus("Invalid OTP, Booking not created");
		}
		return bookingDetailsDTO;
	}
	
}
