/*
Create
Read
Update
Delete
*/
package Modelo;

/**
 *
 * @author LUNA
 */
import Controlador.Conexion_a_laBD;
import java.sql.*;
import javax.swing.JOptionPane;
public class CRUD_Pedido_Pagado {
    Connection con = new Conexion_a_laBD().conecta();  
    
    /***
     * Guardamos el pedido pagado
     * @param monto : Total que cobra el cajero
     * @param opcion_pago : tipo de pago Tarjeta o Efectivo
     * @param carnet : numero de carnet del cliente existente
     * @param comanda : numero de comanda del ultimo consumo
     * @param fecha : fecha YYYY-mm-dd actual
     * @param costo_pedido
     * @param utilidad_pedido
     */
    public void guardaPedidoPagado(double monto, String opcion_pago, String carnet, int comanda, 
            String fecha, double costo_pedido, double utilidad_pedido){
             
        System.out.println("Procedemos a guardar a la tabla pedido pagado");
        /*Guarda el pago */
        String sqlGuardaPedido = "INSERT INTO pedido_pagado VALUES (null,?,?,?,?,?,?,?,?);";
        Buscador_de_Personas persona = new Buscador_de_Personas();
        int id_cliente = persona.obtenerId_Cliente(carnet);

        
        try {
            try (PreparedStatement pst = (PreparedStatement) con.prepareCall(sqlGuardaPedido)) {
                pst.setDouble(1, monto);
                pst.setString(2, opcion_pago);
                pst.setString(3, carnet);
                pst.setInt(4, comanda);
                pst.setString(5, fecha);
                pst.setInt(6, id_cliente);
                pst.setDouble(7, costo_pedido);
                pst.setDouble(8, utilidad_pedido);
                
                int query = pst.executeUpdate();
                System.out.println("el query es: " + query);
                System.out.println("SI SE GUARDÓ a la base de datos tabla: *Pedido pagado* ");
                //cerramos pst
                JOptionPane.showMessageDialog(null, "¡ÉXITO! \n Consumo registrado");
                
            }
            
        } catch (SQLException e) {
            System.out.println("Error: Paquete: modelo, Clase CRUD_Pedido_Pagado Funcion guarda PedidoPagado " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Datos incorrectos");
          
        }
    }
    
    /***
     * Gauarda principalmente la cantidad de consumos del cliente 
     * actualizando los datos
     * @param carnet :carnet identidad
     * @param consumos_acumulados : contador de consumos
     * 
     */
    public void guardaContadorCLiente (String carnet, int consumos_acumulados)
    {
        System.out.println("Procedemos a guardar a la tabla cliente");
        System.out.println("Guardaremos los valores: \n carnet: "+carnet+" suma consumo: "+consumos_acumulados);
        /*Guarda el consumo */
        String sqlGuardaClienteConsumo = "UPDATE cliente SET contador_compras_hechas = '"+consumos_acumulados+"' WHERE cliente.ciPersona= '"+carnet+"';";
        
        //UPDATE `cliente` SET `contador_compras_hechas` = '99' WHERE cliente.ciPersona= '8439484';
        //"UPDATE `cliente` SET `contador_compras_hechas` = '"+ consumos_acumulados+"' WHERE cliente.ciPersona= '"+carnet+"';";
       
            try {
           PreparedStatement pst=con.prepareStatement(sqlGuardaClienteConsumo);
            pst.executeUpdate();
            System.out.println("SI SE GUARDÓ a la base de datos tabla: *cliente* ");
              
        } catch (SQLException e) {
                System.out.println("Error: Paquete. Modelo, Clase CRUD Pedido Pagado. Funcion: guardaContador cliente"+ e.getMessage());
        }
           
                         
        
    }
}
