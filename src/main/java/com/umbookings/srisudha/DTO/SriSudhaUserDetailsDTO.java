package com.umbookings.srisudha.DTO;

import com.umbookings.model.BookingDetails;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Shrikar Kalagi
 *
 */
@Data
public class SriSudhaUserDetailsDTO {

	@NotNull
	private Integer addressId;

	private String name;

	private Date endDate;

	private String address;

	private String district;

	private String pincode;

}
