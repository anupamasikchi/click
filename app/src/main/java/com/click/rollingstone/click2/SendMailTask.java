package com.click.rollingstone.click2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;


 public class SendMailTask extends AsyncTask {

        Message msg;
        Context context;
        String filename = "";

        public SendMailTask(String filename){
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    String pwd = "";
                    try {
                        SendMailTask.AESCrypt("encrypt");
                        pwd = SendMailTask.decrypt("6mlBnct9kAWeXP9LHnupuA==\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new PasswordAuthentication("smarteyeintrusion@gmail.com", pwd);
                }
            });
            this.msg = new MimeMessage(session);
            this.filename = filename;
            Click2Logging.getInstance().write("In mail task.");
        }

     @Override
     protected Object doInBackground(Object[] params) {
         try {
              if (isNetworkConnected()) {
                 setEmailSettings();
                 Transport.send(msg);
                 Click2Logging.getInstance().write("SendMailTask: Mail sent.");
                 return true;
              } else {
                  Click2Logging.getInstance().write("Internet not connected.Email is queued.");
                  LocalDB localDB = new LocalDB(context);
                  localDB.addEmail(new Mail(filename));
              }
         } catch (AuthenticationFailedException e) {
             Click2Logging.getInstance().write("SendMailTask: Error while sending email - "+e.getMessage());
             return false;
         } catch (MessagingException e) {
             Click2Logging.getInstance().write("SendMailTask: Error while sending email - " + e.getMessage());
             return false;
         } catch (Exception e) {
             Click2Logging.getInstance().write("SendMailTask: Error while sending email - " + e.getMessage());
             e.printStackTrace();
             return false;
         }
         return true;
     }

     public void setEmailSettings() {
         Click2Logging.getInstance().write("Creating email send task");

         byte[] b = new byte[1];
         if (filename!=null && (!filename.equals(""))) {

             File imagefile = new File(filename);
             FileInputStream fis = null;
             try {
                 fis = new FileInputStream(imagefile);
             } catch (FileNotFoundException e) {
                 Click2Logging.getInstance().write("Could not open image file");
             }

             Bitmap bm = BitmapFactory.decodeStream(fis);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
             b = baos.toByteArray();
         }

         String emailId = getEmail(this.context);
         String body = "Suspicious attempt to unlock your android phone. User image captured and attached herewith.";
         String subject = "Suspected security breach";

         try {
             msg.setFrom(new InternetAddress("smarteyeintrusion@gmail.com", "App Lock Securities"));
             msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailId, emailId));
             msg.setSubject(subject);
             msg.setText(body);

             Multipart mp = new MimeMultipart();
             MimeBodyPart htmlPart = new MimeBodyPart();
             htmlPart.setContent(body, "text/html");
             mp.addBodyPart(htmlPart);

             MimeBodyPart attachment = new MimeBodyPart();
             attachment.setFileName("image.jpg");
             DataSource src = new ByteArrayDataSource(b, "application/jpg");
             attachment.setDataHandler(new DataHandler(src));
             mp.addBodyPart(attachment);

             msg.setContent(mp);
             Click2Logging.getInstance().write("Done creating email object.");
         }catch(Exception e){
             Click2Logging.getInstance().write("Error while sending mail.");
         }
     }
     static String getEmail(Context context){
         AccountManager accountManager = AccountManager.get(context);
         Account account= getAccount(accountManager);
         if (account == null){
             return null;
         }
         else{
             return account.name;
         }
     }
     private static Account getAccount(AccountManager accountManager) {
         Account[] accounts = accountManager.getAccountsByType("com.google");
         Account account;
         if (accounts.length > 0) {
             account=accounts[0];
         }
         else{
             account=null;
         }
         return account;
     }

     public void setApplicationContext(Context context){
         this.context = context;
     }

     private boolean isNetworkConnected() {
         ConnectivityManager connectivityManager = (ConnectivityManager) context
                 .getSystemService(Context.CONNECTIVITY_SERVICE);

         NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

         // Check the current state of the Network Information
         if (networkInfo == null)
             return false;
         if (networkInfo.isConnected() == false)
             return false;
         if (networkInfo.isAvailable() == false)
             return false;
         return true;
     }

     private static Cipher cipher;
     private static SecretKeySpec key;
     private static AlgorithmParameterSpec spec;


     public static void AESCrypt(String password) throws Exception
     {
         // hash password with SHA-256 and crop the output to 128-bit for key
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         digest.update(password.getBytes("UTF-8"));
         byte[] keyBytes = new byte[32];
         System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

         cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
         key = new SecretKeySpec(keyBytes, "AES");
         spec = getIV();
     }

     public static AlgorithmParameterSpec getIV()
     {
         byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
         IvParameterSpec ivParameterSpec;
         ivParameterSpec = new IvParameterSpec(iv);

         return ivParameterSpec;
     }

     /*public static String encrypt(String plainText) throws Exception
     {
         cipher.init(Cipher.ENCRYPT_MODE, key, spec);
         byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
         String encryptedText = new String(Base64.encode(encrypted, Base64.DEFAULT), "UTF-8");

         return encryptedText;
     }*/

     public static String decrypt(String cryptedText) throws Exception
     {
         cipher.init(Cipher.DECRYPT_MODE, key, spec);
         byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
         byte[] decrypted = cipher.doFinal(bytes);
         String decryptedText = new String(decrypted, "UTF-8");

         return decryptedText;
     }

     public static void main(){
         SendMailTask sm = new SendMailTask("");

     }
 }

