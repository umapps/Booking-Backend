package com.umbookings.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * @author Shrikar Kalagi
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "USER_ROLE")
public class UserRole extends BaseModel{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_role_id_generator")
    @SequenceGenerator(
            name = "user_role_id_generator",
            sequenceName = "user_role_id_sequence",
            allocationSize = 1
    )
	private Long id;

	@Column(name = "USER_ID", nullable = false, updatable = false)
	private Long userId;

	@Column(name = "ROLE_ID", nullable = false, updatable = true)
	private Long roleId;
}

