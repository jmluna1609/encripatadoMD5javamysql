
package Controlador;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**
 *
 * @author LUNA
 */
public class SendMail {
    public void enviarSinArchivoAdjunto () throws AddressException, MessagingException{
        
        
            String correo ="marcoslunahbl@gmail.com";
            String password= "owzcvceshmjtvgve";
            String correodestino="marcoslunahbl@gmail.com";
            
            Properties p = new Properties();
            p.put("mail.smtp.host","smtp.gmail.com");
            p.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
          // p.put("mail.smtp.socketFactory.port", "465");
           p.setProperty("mail.smtp.starttls.enable","true");
            p.put("mail.smtp.ssl.trust","smtp.gmail.com");
            p.setProperty("mail.stmp.port","587");
            p.setProperty("mail.stmp.user", correo);
            p.setProperty("mail.smtp.auth", "true");
            Session s = Session.getDefaultInstance(p);
            MimeMessage mensaje = new MimeMessage(s);
            mensaje.setFrom(new InternetAddress(correo));
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(correodestino));
            mensaje.setSubject("prueba de video dia 12");
            mensaje.setText("Este es un mensaje que se envía desde java");
            
            Transport t = s.getTransport("smtp");
            t.connect(correo, password);
            t.sendMessage(mensaje, mensaje.getAllRecipients());
            t.close();
    }
    
      public void enviarConArchivoAdjunto () {
        
        
        try {
            String correo ="marcoslunahbl@gmail.com";
            //String password= "owzcvceshmjtvgve"; //se obtiene a traves de google
            String password="nmkjaulmatfrikrd";
           //String password = "sjtqaxsxlcuprchp"; 
           String correodestino="marcoslunahbl@gmail.com"; //el correo que le va a llegar
            
            Properties p = new Properties();
            p.put("mail.smtp.host","smtp.gmail.com");
            p.setProperty("mail.smtp.ssl.protocols", "TLSv1.2"); //OJO CON QUE SE ACTUALICE
            // p.put("mail.smtp.socketFactory.port", "465");
            p.setProperty("mail.smtp.starttls.enable","true");
            p.put("mail.smtp.ssl.trust","smtp.gmail.com");
            p.setProperty("mail.stmp.port","587");
            p.setProperty("mail.stmp.user", correo);
            p.setProperty("mail.smtp.auth", "true");
            
            Session s = Session.getDefaultInstance(p);
            BodyPart texto = new MimeBodyPart();
            texto.setText("Backup base de datos");
             Calendario cal= new Calendario();
            String fecha = cal.fechaActual();
            BodyPart adjunto = new MimeBodyPart();
           String rutaArchivo="C:\\Users\\LUNA\\Documents\\flash 15062022\\NetBeans\\Cafeteria_Italy 2.0\\backup_datos_italy"+fecha+".sql";
            //String rutaArchivo="E:\\Italy2.5\\backup_datos_italy"+fecha+".sql";
            adjunto.setDataHandler(new DataHandler(new FileDataSource(rutaArchivo)));
           
            //String nombreArchivo="backup_datos_italy"+fecha+".sql";
            String nombreArchivo="tienda.png";
            adjunto.setFileName(nombreArchivo);
            MimeMultipart m = new MimeMultipart();
            m.addBodyPart(texto);
            m.addBodyPart(adjunto);
            
            MimeMessage mensaje = new MimeMessage(s);
            mensaje.setFrom(new InternetAddress(correo));
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(correodestino));
            mensaje.setSubject("Envio del respaldo de la BD Cafetería Italy");
            mensaje.setContent(m);
            
            Transport t = s.getTransport("smtp");
            t.connect(correo, password);
            t.sendMessage(mensaje, mensaje.getAllRecipients());
            t.close();
        } catch (MessagingException ex) {
            //Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al enviar un mail: "+ex.getMessage());
        }
    }
}
