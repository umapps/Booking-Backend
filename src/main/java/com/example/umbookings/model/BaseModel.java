package com.example.umbookings.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.*;
//@JsonIgnoreProperties(
//        value = {"createdAt", "updatedAt"},
//        allowGetters = true
//)

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)

public abstract class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp", nullable = false, updatable = false)
    @CreatedDate
    private Date createdTimestamp;

	@JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_timestamp", nullable = false)
    @LastModifiedDate
    private Date updatedTimestamp;

    
}