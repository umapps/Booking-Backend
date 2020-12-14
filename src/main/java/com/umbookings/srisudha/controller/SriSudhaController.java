package com.umbookings.srisudha.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umbookings.model.User;
import com.umbookings.repository.UserRepository;
import com.umbookings.security.UserPrincipal;
import com.umbookings.srisudha.DTO.SriSudhaUserDetailsDTO;
import com.umbookings.srisudha.repository.SriSudhaRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Shrikar Kalagi
 */
@RestController
@CrossOrigin
public class SriSudhaController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SriSudhaRepository sriSudhaRepository;

    @ApiOperation(value = "API to get SriSudha user details", response = List.class)
    @GetMapping("/get_ss_details")
    @PreAuthorize("hasAnyRole('NORMAL_USER')")
    public List<SriSudhaUserDetailsDTO> getDetails(Authentication authentication) throws Exception {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        String mobileNbr = user.get().getMobileNumber();
        List<SriSudhaUserDetailsDTO> userDetailsDTO = new ArrayList<>();
        ArrayList userDetails = (ArrayList) sriSudhaRepository.getUserDetails(mobileNbr);
        userDetails.forEach(userDetail->
                { SriSudhaUserDetailsDTO sriSudhaUserDetailsDTO = new SriSudhaUserDetailsDTO();
                    sriSudhaUserDetailsDTO.setAddressId((Integer) ((Object[]) userDetail)[0]);
                    sriSudhaUserDetailsDTO.setName((String) ((Object[]) userDetail)[1]);
                    sriSudhaUserDetailsDTO.setEndDate(((Object[]) userDetail)[2] != null ?(Date) ((Object[]) userDetail)[2] : null);
                    sriSudhaUserDetailsDTO.setAddress((String) ((Object[]) userDetail)[3]);
                    sriSudhaUserDetailsDTO.setDistrict((String) ((Object[]) userDetail)[4]);
                    sriSudhaUserDetailsDTO.setPincode((String) ((Object[]) userDetail)[5]);
                    userDetailsDTO.add(sriSudhaUserDetailsDTO);
                }
           );
        return userDetailsDTO;
    }
}