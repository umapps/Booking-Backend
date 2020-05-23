package com.umbookings.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Shrikar Kalagi
 *
 */
@RestController
@CrossOrigin
public class StaticContent {

    //This URL is mapped to health check of EC2 from ELB
    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "   <body>\n" +
                "      <p>\n" +
                "         Visit <a href=\"https://www.uttaradimath.org\">www.uttaradimath.org</a> for more details\n" +
                "      </p>\n" +
                "   </body>\n" +
                "</html>";
    }

    @GetMapping("/")
    public String defaultResponse() {
        return healthcheck();
    }
}
