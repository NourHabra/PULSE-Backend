package com.pulse.user.dto;

public class OtpVerificationRequest {
    private String email;
    private int  otp;

    public OtpVerificationRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int  getOtp() {
        return otp;
    }

    public void setOtp(int  otp) {
        this.otp = otp;
    }
}
