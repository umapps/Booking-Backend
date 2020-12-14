package com.umbookings.srisudha.repository;
import com.umbookings.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SriSudhaRepository extends JpaRepository<User, Long> {

    @Query(value = "select Address_Id, Name, End_Date, Address, District, Pin_Code from address_details where Mobile_No =:mobileNumber", nativeQuery = true)
    List<String> getUserDetails(String mobileNumber);
}
