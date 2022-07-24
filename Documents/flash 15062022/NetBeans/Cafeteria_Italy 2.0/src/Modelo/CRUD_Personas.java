/*
Aqui es el CRUD donde guardaremos a los nuevos clientes y usuarios
 */
package Modelo;

import Controlador.Calendario;
import Controlador.Conexion_a_laBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author LUNA
 */
public class CRUD_Personas {
     Connection con = new Conexion_a_laBD().conecta();
     
     /***
      * Añadir nueva persona a la base de datos
      * Se va a necesitar los siguientes requisitos
      * @param carnet carnet
      * @param nombre nombres
      * @param apellido apellidos
      * @param celular numero actual con whatsapp
      * @param nacimiento fecha de registro
      */
    public void createNuevaPersona(String carnet, String nombre, String apellido, String celular, String nacimiento){
        
        /*Guardo a una nueva persona*/
        String sqlGuardaPersona = "INSERT INTO persona VALUES (?,?,?,?,?);";
        try {
            try (PreparedStatement pst = (PreparedStatement) con.prepareCall(sqlGuardaPersona)) {
                pst.setString(1, carnet);
                pst.setString(2, nombre);
                pst.setString(3, apellido);
                pst.setString(4, celular);
                pst.setString(5, nacimiento);
                int query = pst.executeUpdate();
                System.out.println("el query es: " + query);
                System.out.println("SI SE GUARDÓ a la base de datos tabla: *persona* ");
                //cerramos pst
                
            }
            //con.close(); // cerramos la conexion
        } catch (SQLException e) {
            System.out.println("Ocurrio un erroral registrar al cliente: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Datos personales no registrados ");
          
        }
    }
    
    /***
     * Guarda al nuevo cliente, despues de guardar a la nueva persona en registro
     * @param carnet 
     */
    public void createNuevoCliente(String carnet)
    {
        int contador_consumos=0;
        Calendario cal= new Calendario();
        
        String fecha1erConsumo = cal.fechaActual();
        //GUARDAMOS EL NUEVO CLIENTE
         String sqlGuardaPersona = "INSERT INTO cliente VALUES (null,?,?,?);";
        try {
            try (PreparedStatement pst = (PreparedStatement) con.prepareCall(sqlGuardaPersona)) {
                pst.setString(1, carnet);
                pst.setInt(2, contador_consumos);
                pst.setString(3, fecha1erConsumo);
                int query = pst.executeUpdate();
                System.out.println("SI SE GUARDÓ a la base de datos tabla: *cliente* ");
                //cerramos pst
                JOptionPane.showMessageDialog(null, "¡ÉXITO! \n cliente registrado");
                
            }
            //con.close(); // cerramos la conexion
        } catch (SQLException e) {
            System.out.println("Ocurrio un error al registrar al cliente: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Cliente no registrado ");
          
        }
    }
    
    /***
     * FUNCION QUE GUARDA AL NUEVO USUARIO
     * @param carnet
     * @param usuario
     * @param password
     * @param cargo 
     */
      public void createNuevoUsuario(String carnet, String usuario, String password, String cargo)
    {
        //GUARDAMOS EL NUEVO USUARIO
         String sqlGuardaPersona = "INSERT INTO trabajador VALUES (null,?,?,?,?);";
        try {
            try (PreparedStatement pst = (PreparedStatement) con.prepareCall(sqlGuardaPersona)) {
                pst.setString(1, carnet);
                pst.setString(2, usuario);
                pst.setString(3, password);
                pst.setString(4, cargo);
                int query = pst.executeUpdate();
                System.out.println("SI SE GUARDÓ a la base de datos tabla: *trabajador* ");
                //cerramos pst
                JOptionPane.showMessageDialog(null, "¡ÉXITO! \n Usuario registrado");
                
            }
            //con.close(); // cerramos la conexion
        } catch (SQLException e) {
            System.out.println("Ocurrio un error al registrar al usuario: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "usuario no registrado ");
          
        }
    }
}
