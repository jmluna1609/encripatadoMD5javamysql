package encodedecodemd5;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Base64;

public class Main {

    public static void main(String[] args) {
        String secretKey = "EsunaLlaveSecreta";
        Main mMain = new Main();
        String cadenaAEncriptar = JOptionPane.showInputDialog("Ingresa la cadena a encriptar");
        String cadenaEncriptada = mMain.codificar(secretKey, cadenaAEncriptar);
        JOptionPane.showMessageDialog(null, "Cadena encriptada: " + cadenaEncriptada);
        String cadenaDesencriptada = mMain.decodificar(secretKey, cadenaEncriptada);
        JOptionPane.showMessageDialog(null, "Cadena desencriptada: " + cadenaDesencriptada);

    }

    public String codificar(String secretKey, String cadena) {
        String encriptacion = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5"); //tipo de encriptacion
            byte[] llavePassword = md5.digest(secretKey.getBytes("utf-8")); //tipo de escritura
            byte[] BytesKey = Arrays.copyOf(llavePassword, 24);
            
            SecretKey key = new SecretKeySpec(BytesKey, "DESede");
            Cipher cifrado = Cipher.getInstance("DESede"); 
            cifrado.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainTextBytes = cadena.getBytes("utf-8");
            byte[] buf = cifrado.doFinal(plainTextBytes); //ya esta cifrado
            byte[] base64Bytes = Base64.encodeBase64(buf); 
            encriptacion = new String(base64Bytes);
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            JOptionPane.showMessageDialog(null, "Algo salió mal");
        }
        return encriptacion;
    }

    public String decodificar(String secretKey, String cadenaEncriptada) {
        String desencriptacion = "";
        try {
            byte[] message = Base64.decodeBase64(cadenaEncriptada.getBytes("utf-8"));
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md5.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = decipher.doFinal(message);
            desencriptacion = new String(plainText, "UTF-8");

        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            JOptionPane.showMessageDialog(null, "Algo salió mal");
        }
        return desencriptacion;
    }
}
