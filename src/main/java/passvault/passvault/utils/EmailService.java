package passvault.passvault.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.sun.mail.smtp.SMTPTransport;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Properties;

public class EmailService {

    private static final String CLIENT_ID = "CLIENT_ID_HERE";
    private static final String CLIENT_SECRET = "CLIENT_SECRET_HERE";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN_HERE";

    public void sendOTP(String toEmail, String otp) {
        String fromEmail = "SENDER_EMAIL_HERE";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth.mechanisms", "XOAUTH2");

        Session session = Session.getInstance(props);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Password Reset OTP");
            message.setText("Your OTP for password reset is: " + otp);

            SMTPTransport smtpTransport = (SMTPTransport) session.getTransport("smtp");
            smtpTransport.connect("smtp.gmail.com", fromEmail, getAccessToken());
            smtpTransport.sendMessage(message, message.getAllRecipients());
            smtpTransport.close();

            System.out.println("OTP sent successfully to " + toEmail);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAccessToken() throws IOException {
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(new NetHttpTransport())
                .setJsonFactory(JacksonFactory.getDefaultInstance())
                .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                .build()
                .setRefreshToken(REFRESH_TOKEN);

        credential.refreshToken();
        return credential.getAccessToken();
    }
}
