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

    private static final String CLIENT_ID = "60696327108-bd5sfabfc7cj7ojh295fllt13bu57hgh.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REFRESH_TOKEN = "1//04zMtF6rXtH0KCgYIARAAGAQSNwF-L9IrIWd7zZ6T6hJ0bJRubsv0fZZF8sC2QaQKIy0ZET9aN55_R4f8rOCu2gSt-b4rWO_SuKo";

    public void sendOTP(String toEmail, String otp) {
        String fromEmail = "pass.vault.mp@gmail.com";

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