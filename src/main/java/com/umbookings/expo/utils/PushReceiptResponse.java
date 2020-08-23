package com.umbookings.expo.utils;

import com.google.gson.annotations.SerializedName;

import java.util.Map;
/**
 * @author Shrikar Kalagi
 *
 */
/**
 * Represents the response from Expo's servers when retrieving push receipts
 */
public class PushReceiptResponse {

    @SerializedName("data")
    private Map<String, PushReceipt> receipts;

    private PushReceiptResponse() {}

    /**
     *
     * @return a map where the key is the receiptID and the value is the PushReceipt
     */
    public Map<String, PushReceipt> getReceipts() {
        return receipts;
    }

}
