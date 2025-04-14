package tn.esprit.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import com.twilio.Twilio;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
        System.out.println("✅ Twilio initialized with Account SID: " + accountSid);
    }
}
