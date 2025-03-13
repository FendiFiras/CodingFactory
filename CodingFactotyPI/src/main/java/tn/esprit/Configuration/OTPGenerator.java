package tn.esprit.Configuration;

import java.util.Random;

public class OTPGenerator {
    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Génère un nombre entre 100000 et 999999
        return String.valueOf(otp);
    }
}