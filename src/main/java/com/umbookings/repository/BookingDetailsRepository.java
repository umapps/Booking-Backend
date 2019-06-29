package com.umbookings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.umbookings.model.BookingDetails;

@Repository
public interface BookingDetailsRepository extends JpaRepository<BookingDetails, Long> {
}
