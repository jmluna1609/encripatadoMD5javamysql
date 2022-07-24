/*
AQUI PERMITE VALIDAR EL INGRESO AL SISTEMA
 */
package Modelo;

import Controlador.Conexion_a_laBD;
import Controlador.Encriptacion;
import Vista.Acceso_al_Sistema;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author LUNA
 */
public class CRUD_Acceso_al_sistema {

    Connection con = new Conexion_a_laBD().conecta();
    int sw = 0; //es el interruptor para confirmar el acceso

    //AQUI LEE EL USUARIO Y CLAVE DEL TRABAJADOR Y VALIDA
    public void ingreso_al_sistema(String usuario, String contraseña) {

        try {

            System.out.println("Entramos a la clase CRUD acceso al sistema ");
            /*Le envio los datos que ingreso el usuario para la funcion
                 ACCESO SISTEMA*/

            String user = usuario.toUpperCase();

            System.out.println("recibimos los datos desde la clase de variables tabla trabajadores: " + user + " contraseña: " + contraseña);

            String sqlBuscar = "SELECT * FROM trabajador WHERE usuario= '" + usuario + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscar);
              //Vamos a decodificar la contraseña encontrada en la base de datos
                Encriptacion decodifica = new Encriptacion();
             String pass = "";
             
            if (rs.next()) {
                pass = decodifica.decodificar(rs.getString("password"));
                System.out.println("encontramos el pass de la base de datos es : " + pass);

            }
            
            if (pass.equals(contraseña)) {
                System.out.println("comprobamos si se encontró un parecido entre usuairo y contraseña");
                JOptionPane.showMessageDialog(null, "BIENVENIDO!!");
                sw = 1;
                Acceso_al_Sistema access = new Acceso_al_Sistema();
                access.confirmacion_al_sistema(sw);
            } else {
                sw = 2;
                Acceso_al_Sistema access = new Acceso_al_Sistema();
                access.confirmacion_al_sistema(sw);
            }

        } catch (HeadlessException | SQLException e) {
            System.out.println("Se detectó un problema: " + e.getMessage());
        }

    }

}
