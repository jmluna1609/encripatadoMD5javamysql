/*
 Esta clase permite buscar a las personas por distintas maneras

*/
package Modelo;

import Controlador.Conexion_a_laBD;
import DTO.DTOCliente;
import DTO.DTOPersona;
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

public class Buscador_de_Personas {
    Connection con = new Conexion_a_laBD().conecta();
    
    /***
     * Obtiene el id_del trabajador ingresando el password
     * @param password : clave del empleado
     * @return int retorna un valor entero, que se sería el id del trabajador
     */
    public int obtenerId_trabajador (String password){
         
         int id_trabajador = 0;

        String sqlBuscarId_trabajador = "SELECT id_trabajador FROM trabajador "
                + "WHERE password LIKE '" + password + "';";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscarId_trabajador);
            System.out.println("Comparo contraseñas");
            if (rs.next()) {
                /*int id_trabajador*/
                System.out.println("id trabajador: " + rs.getInt(1));
                id_trabajador = rs.getInt(1);
                System.out.println("Contraseña coincide");

            } else {
                System.out.println("Contraseña equivocada");
                JOptionPane.showMessageDialog(null, "Contraseña inválida");

            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Ocurrio un error en Buscador personas funcion obtenerId: \n " + e.getMessage());
        }

        return id_trabajador;
        
     }
    
    /***
     * Obtener el id del cliente
     * Se necesita el siguiente parametro
     * @param carnet el carnet de la cliente
     * @return int Retorna valora entero
     */
    public int obtenerId_Cliente (String carnet){
         
         int id_cliente = 0;

        String sqlBuscarId_trabajador = "SELECT id_cliente FROM cliente "
                + "WHERE ciPersona LIKE '" + carnet + "';";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscarId_trabajador);
            System.out.println("Comparo el carnet de la persona");
            if (rs.next()) {
                /*int id_trabajador*/
                System.out.println("id cliente: " + rs.getInt(1));
                id_cliente = rs.getInt(1);
                System.out.println("carnet coincide");

            } else {
                System.out.println("Carnet equivocado");
               
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Ocurrio un error en Buscador cliente funcion obtenerId cliente: \n " + e.getMessage());
        }

        return id_cliente;
        
     }
    
    
    
    
    /***
     * Busca si la cliente esta registrada
     * @param carnet
     * @return DTOPErsona
     */
    public DTO.DTOPersona buscarPersona(String carnet) {

        DTO.DTOPersona persona = null;
        System.out.println("Procedemos a buscar los datos del cliente con el carnet: "+carnet);
        String sqlBuscarEmpleado = "SELECT nombre, apellido, celular FROM persona"
                + " WHERE ci LIKE '" + carnet + "';";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscarEmpleado);

            if (rs.next()) {
                /* Obtenemos datos de la base de datos tabla personas
                String nombre, String apellido, 
                String celular */
                //System.out.println("codigo detrabajador: "+rs.getString(8));
                System.out.println("Encontramos datos, lo estamos enviando a la clase DTOPersona");
                return new DTOPersona(rs.getString(1), rs.getString(2), rs.getString(3));
            }
            else {
                //JOptionPane.showMessageDialog(null, "No se encuentró a la Persona");

            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Error: paquete modelo, clase Buscador Personas, funcion buscar persona: "+ e.getMessage());
        }

        return persona;
    }
    
    /***
     * Busca si el cliente esta registrado en la Base de datos
     * @param carnet
     * @return DTOCLiente
     */
    public DTO.DTOCliente buscarCliente(String carnet) {

        DTO.DTOCliente cliente = null;
        /*
        SELECT persona.nombre, persona.apellido, persona.celular 
        FROM cliente, persona WHERE cliente.ciPersona 
        LIKE '8439484' 
        and cliente.ciPersona=persona.ci;
        */
        System.out.println("Procedemos a buscar los datos del cliente con el carnet: "+carnet);
        String sqlBuscarEmpleado = "SELECT persona.nombre, persona.apellido, persona.celular"
                + " FROM persona,cliente"
                + " WHERE ci LIKE '" + carnet + "'"
                + " and cliente.ciPersona=persona.ci;";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscarEmpleado);

            if (rs.next()) {
                /* Obtenemos datos de la base de datos tabla personas
                String nombre, String apellido, 
                String celular */
                //System.out.println("codigo detrabajador: "+rs.getString(8));
                System.out.println("Encontramos datos, lo estamos enviando a la clase DTOPersona");
                return new DTOCliente(rs.getString(1), rs.getString(2), rs.getString(3));
            }
            else {
               // JOptionPane.showMessageDialog(null, "No se encuentró al cliente");

            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Error: paquete modelo, clase Buscador Personas, funcion buscar cliente: "+ e.getMessage());
        }

        return cliente;
    }
    
    public DTOCliente buscarConsumos (String carnet){
        DTOCliente consumo = null;
        System.out.println("Vamos a buscar el ultimo numero que se contabilizó en base de datos.");        
        String sqlBuscarConsumoMAX= "SELECT MAX(contador_compras_hechas) FROM cliente WHERE ciPersona LIKE '"+carnet+"';";
         
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscarConsumoMAX);

            if (rs.next()) {
                /* Obtenemos datos de la base de datos tabla personas
                int contador_compras_hechas */
                //System.out.println("codigo detrabajador: "+rs.getString(8));
                System.out.println("Encontramos datos, lo estamos enviando a la clase DTOCliente");
                return new DTOCliente(rs.getInt(1));
            }
            else {
                //JOptionPane.showMessageDialog(null, "No se encuentra ese dato");
                  System.out.println("El cliente no fue encontrado");
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Error: paquete modelo, clase Buscador Personas, funcion buscar consumos: "+ e.getMessage());
        }       
                
                
         return consumo;
    }
    
    /***
     * Vamos a buscar el Id_ del pedido Pagado
     * @return int id pagado encontrado
     */
    public int buscar_id_pedido_pagado (){
        System.out.println("Buscamos el id del pedido pagado recientemente");
        
        int id_encontrado = 0;
         String sqlIdMAX= "SELECT MAX(id_pedido_pagado) FROM pedido_pagado WHERE 1;";
         
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlIdMAX);

            if (rs.next()) {
                /* Obtenemos datos de la base de datos tabla personas
                int id_pedido_pagado */
                //System.out.println("codigo detrabajador: "+rs.getString(8));
                System.out.println("Encontramos id ultimo pagado");
                id_encontrado=rs.getInt(1);
            }
            else {
                JOptionPane.showMessageDialog(null, "No se encuentra ese dato");

            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Error: paquete modelo, clase Buscador Personas, funcion buscar id pedido pagado: "+ e.getMessage());
        }       
                
        
        
        return id_encontrado;
    }
}
