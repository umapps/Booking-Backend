package com.umbookings.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * @author Shrikar Kalagi
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
//Cannot have a table name with User
@Table(name = "UM_USER")
public class User extends BaseModel{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_id_generator")
    @SequenceGenerator(
            name = "user_id_generator",
            sequenceName = "user_id_sequence",
            allocationSize = 1
    )
    private Long id;
	
	@NotBlank
	@Column(name = "FIRST_NAME_TEXT")
	private String firstName;

	@Column(name = "LAST_NAME_TEXT")
	private String lastName;
	
	@NotBlank
    @Column(name = "MOBILE_NUMBER", unique = true)
    @Size(min = 10, max = 10)
    private String mobileNumber;
	
	@Column(name = "DOB_DATE")
	private Date dob;

	@Column(name = "EMAIL_IDENTIFIER", unique = true)
	private String emailId;

	@JsonIgnore
	@Column(name = "PASSWORD_TEXT")
	private String password;
	
	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "PIN_CODE")
	private String pinCode;
}
