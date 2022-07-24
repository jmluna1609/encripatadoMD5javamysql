/*
*INICIA EL SISTEMA Y PASA A LA CLASE Acceso_al_Sistema
 */
package Controlador;

import Vista.Acceso_al_Sistema;
import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Jose Marcos LUNA
 */
public class InicioSistema {
    /*para que no se habra una doble pantalla*/
 private static ServerSocket SERVER_SOCKET;
    public static void main(String[] args) {
       
        /*
         * Instanciamos una clase
         */
        Acceso_al_Sistema iniciarSitema = new Acceso_al_Sistema();
        /* 
       Abre la ventana y nos vamos a la clase JFrame Acceso_al_Sistema
         */
        try {
            SERVER_SOCKET = new ServerSocket(1334);
            
        iniciarSitema.setVisible(true);
        } catch (IOException e) {
            System.out.println("Error al iniciar sistema: "+ e.getMessage());
            System.exit(0);
        }
    }
}
