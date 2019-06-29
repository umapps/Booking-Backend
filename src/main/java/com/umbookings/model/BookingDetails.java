package com.umbookings.model;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.umbookings.enums.BookingStatus;
import com.umbookings.enums.BookingType;

import lombok.*;
/**
 * @author Shrikar Kalagi
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "BOOKING")
public class BookingDetails extends BaseModel {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "booking_id_generator")
    @SequenceGenerator(
            name = "booking_id_generator",
            sequenceName = "booking_id_sequence",
            allocationSize = 1
    )
    private Long id;

	@NotBlank
    @Column(name = "mobile_number")
    @Size(min = 10, max = 10)
    private String mobileNbr;
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "booking_date", nullable = false)
    private Date bookingDate;
    
    @Column(name = "email_identifier")
    private String emailIdentifier;
    
    @Column(name = "person_count")
    private String personCount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_type")
    private BookingType bookingType;
    
}
