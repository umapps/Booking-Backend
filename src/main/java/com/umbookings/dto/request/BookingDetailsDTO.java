package com.umbookings.dto.request;

import javax.validation.constraints.NotNull;

import com.umbookings.model.BookingDetails;

import lombok.Data;

/**
 * @author Shrikar Kalagi
 *
 */
@Data
public class BookingDetailsDTO {
	
	private BookingDetails bookingDetails;

	@NotNull
	private Long otp;
	
	private String status;

}
