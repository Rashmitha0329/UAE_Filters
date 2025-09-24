package com.iween.utilities;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailUtils {

    private static final String USERNAME = "tarun@tripgain.com";
    private static final String APP_PASSWORD = "wytm cscd ogzq ynen"; // Use Gmail App Password
   // private static final String TO_EMAIL = "rashmitha@tripgain.com";
    private static final String TO_EMAIL = "prathibha@iweensoft.com";
    public static void sendEmailWithAttachment(String reportPath) {
        File reportFile = new File(reportPath);

        if (!reportFile.exists()) {
            System.out.println("❌ Report file not found: " + reportPath);
            return;
        }

        long fileSizeBytes = reportFile.length();
        double sizeMB = fileSizeBytes / (1024.0 * 1024.0);
        System.out.printf("📄 Report size: %.2f MB%n", sizeMB);

        if (sizeMB > 25) {
            System.out.println("⚠️ Report too large to send (>25 MB). Email skipped.");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TO_EMAIL));
            
/*
         // CC (optional)
         message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(
             "manager@example.com"
         ));

         // BCC (optional)
         message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(
             "auditlog@example.com"
         ));

     */       
            message.setSubject("📊 Automated TestNG Extent Report");

            // Email body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Hi,\n\nPlease find the attached Extent Report.\n\nRegards,\nAutomation Bot");

            // Attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(reportFile);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("✅ Email sent successfully!");

        } catch (Exception e) {
            System.out.println("❌ Failed to send email.");
            e.printStackTrace();
        }
    }
}

