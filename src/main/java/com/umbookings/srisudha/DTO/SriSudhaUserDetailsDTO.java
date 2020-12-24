package com.umbookings.srisudha.DTO;

import com.umbookings.model.BookingDetails;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

	@Size(min = 6, max = 6)
	private String pincode;

}
