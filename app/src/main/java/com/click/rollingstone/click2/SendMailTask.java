package com.click.rollingstone.click2;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MimeType;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;


 public class SendMailTask extends AsyncTask {

        Message msg;

        public SendMailTask(String to, String subject, String body,byte[] pdf1)
                throws AddressException, MessagingException, UnsupportedEncodingException {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");




            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("applocksecurities@gmail.com", "charminar");
                }
            });
            this.msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("applocksecurities@gmail.com", "Sarda Click"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to, to));
            msg.setSubject(subject);
            msg.setText(body);

        Multipart mp = new MimeMultipart();
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(body, "text/html");
        mp.addBodyPart(htmlPart);

        MimeBodyPart attachment = new MimeBodyPart();
        attachment.setFileName("pdf1.jpg");
        DataSource src = new ByteArrayDataSource(pdf1, "application/jpg");
        attachment.setDataHandler(new DataHandler(src));
        mp.addBodyPart(attachment);

        msg.setContent(mp);

        }

     @Override
     protected Object doInBackground(Object[] params) {
         if (BuildConfig.DEBUG) Log.v(SendMailTask.class.getName(), "doInBackground()");
         try {
             Transport.send(msg);
             Click2Logging.getInstance().write("SendMailTask: Mail sent");
             return true;

         } catch (AuthenticationFailedException e) {
             Click2Logging.getInstance().write("SendMailTask: Error while sending email - "+e.getMessage());
             return false;
         } catch (MessagingException e) {
             Click2Logging.getInstance().write("SendMailTask: Error while sending email - " + e.getMessage());
             return false;
         } catch (Exception e) {
             Click2Logging.getInstance().write("SendMailTask: Error while sending email - " + e.getMessage());
             return false;
         }
     }
 }

