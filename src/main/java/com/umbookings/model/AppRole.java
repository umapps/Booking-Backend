package com.umbookings.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.umbookings.enums.RoleName;

/**
 * @author Shrikar Kalagi
 *
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "APP_ROLE")
public class AppRole extends BaseModel {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "app_role_id_generator")
    @SequenceGenerator(
            name = "app_role_id_generator",
            sequenceName = "app_role_id_sequence",
            allocationSize = 1
    )
	private Long id;

	private static final long serialVersionUID = 1L;
	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(length = 60, name = "ROLE_NAME")
	private RoleName name;

	@Column(name = "DESCRIPTION_TEXT")
	private String description;
}