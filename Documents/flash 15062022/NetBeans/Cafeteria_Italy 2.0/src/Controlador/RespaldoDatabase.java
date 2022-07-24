package Controlador;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LUNA
 */
public class RespaldoDatabase {

    public void backup() {
        Calendario cal = new Calendario();
        String fecha = cal.fechaActual();
        System.out.println("procedemos a sacar un respaldo de la base de datos");
        try {
            Process p = Runtime.getRuntime().exec("mysqldump -u root datos_italy");
            InputStream inputs= p.getInputStream();
            try (FileOutputStream fouts = new FileOutputStream("backup_datos_italy" + fecha + ".sql")) {
                byte[] buffer = new byte[1000];
                int leido = inputs.read(buffer);
                while(leido >0)
                {
                    fouts.write(buffer,0,leido);
                    leido = inputs.read(buffer);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RespaldoDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
