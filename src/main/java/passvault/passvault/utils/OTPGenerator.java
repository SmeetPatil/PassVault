package passvault.passvault.utils;

import java.security.SecureRandom;

public class OTPGenerator {
    private static final String OTP_CHARS = "0123456789";
    private static final int OTP_LENGTH = 6;

    public String generateOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(OTP_CHARS.charAt(random.nextInt(OTP_CHARS.length())));
        }

        return otp.toString();
    }
}
