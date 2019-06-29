package com.example.umbookings.model;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.umbookings.enums.BookingStatus;
import com.example.umbookings.enums.BookingType;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "BOOKING")
public class BookingDetails extends BaseModel {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "booking_generator", strategy = GenerationType.AUTO)
    @SequenceGenerator(
            name = "booking_generator",
            sequenceName = "booking_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(columnDefinition = "mobile_number", nullable = false)
    @Size(min = 10, max = 10)
    private String mobileNbr;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "booking_date", nullable = false)
    private Date bookingDate;
    
    @Column(columnDefinition = "email_identifier")
    private String emailIdentifier;
    
    @Column(columnDefinition = "person_count")
    private String personCount;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "booking_status")
    private BookingStatus bookingStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "booking_type")
    private BookingType bookingType;
    
}
