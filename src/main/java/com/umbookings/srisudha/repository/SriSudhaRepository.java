package com.umbookings.srisudha.repository;
import com.umbookings.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SriSudhaRepository extends JpaRepository<User, Long> {

    @Query(value = "select Address_Id, Name, End_Date, Address, District, Pin_Code from address_details where Mobile_No =:mobileNumber", nativeQuery = true)
    List<String> getUserDetails(String mobileNumber);

    @Modifying
    @Transactional
    @Query(value = "UPDATE address_details SET Name=:name, Address=:address, District=:district, Pin_Code=:pincode, Logged_User=:loggedUser  where Address_Id=:addressId", nativeQuery = true)
    void updateUserDetails(Integer addressId, String name, String address, String district, String pincode, String loggedUser);
}
