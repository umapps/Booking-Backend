package com.umbookings.expo.utils;

import com.google.gson.annotations.SerializedName;
/**
 * @author Shrikar Kalagi
 *
 */
/**
 * Represents an error that Expo encountered with satisfying an entire request to their server
 */
public class ExpoError {

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    private ExpoError() {}

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
