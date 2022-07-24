/*
 Procedemos a cerrar la venta del dia
 */
package Modelo;

import Controlador.Calendario;
import Controlador.Conexion_a_laBD;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author LUNA
 */
public class CRUD_Cierre_Venta {
    
     Connection con = new Conexion_a_laBD().conecta();
         
     
     /***
      * Cargar y mostrar las ventas de hoy
      * 
     * @return double Venta Total
      */
     public double ventaTotal(){
         
    double totalVenta =0;
    //Obtenemos la fecha de hoy
    Calendario fechaHoy = new Calendario();
    String fecha = fechaHoy.fechaActual();
    //SELECT SUM(`monto_bs`) FROM `registro_gastos` WHERE `fecha` = '2022-06-26'
   
        String sqlBuscaFechaComprasHoy = "SELECT SUM(monto_total_bs) FROM pedido_pagado "
                + "WHERE fecha = '" +fecha+ "';";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscaFechaComprasHoy);
            System.out.println("buscamos los registros de VENTAS DE HOY");
            if (rs.next()) {
               totalVenta =rs.getDouble((1));
                System.out.println("TOTAL VENTAS Bs "+ totalVenta);
                
            } else {
                System.out.println("No encontramos Ventas hoy bs: "+ totalVenta);
                
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Ocurrio un error en CRUDCierre Venta Funcion Venta Total: \n " + e.getMessage());
        }
      return totalVenta;
     }
     
      /***
      * Cargar los costos de los productos de hoy
      * 
     * @return double costo total
      */
     public double costoTotal(){
         
    double totalCosto =0;
    //Obtenemos la fecha de hoy
    Calendario fechaHoy = new Calendario();
    String fecha = fechaHoy.fechaActual();
    //SELECT SUM(pedido_pagado.costo_pedido) FROM pedido_pagado WHERE fecha = '2022-06-29''
   
        String sqlBuscaFechaComprasHoy = "SELECT SUM(pedido_pagado.costo_pedido) FROM pedido_pagado "
                + "WHERE fecha = '" +fecha+ "';";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscaFechaComprasHoy);
            System.out.println("buscamos los registros de COSTOS DE PRODUCTOS DE HOY");
            if (rs.next()) {
               totalCosto =rs.getDouble((1));
                System.out.println("TOTAL COSTOS Bs "+ totalCosto);
                
            } else {
                System.out.println("No encontramos COSTOS hoy bs: "+ totalCosto);
                
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Ocurrio un error en CRUDCierre Venta Funcion Venta Total: \n " + e.getMessage());
        }
      return totalCosto;
     }
     
      /***
      * Cargar la utilidad de hoy
      * 
     * @return double utilidad total
      */
     public double utilidadTotal(){
         
    double totalUtilidad =0;
    //Obtenemos la fecha de hoy
    Calendario fechaHoy = new Calendario();
    String fecha = fechaHoy.fechaActual();
    //SELECT SUM(pedido_pagado.utilidad_pedido) FROM pedido_pagado WHERE fecha = '2022-06-29'
   
        String sqlBuscaFechaComprasHoy = "SELECT SUM(pedido_pagado.utilidad_pedido) FROM pedido_pagado "
                + "WHERE fecha = '" +fecha+ "';";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscaFechaComprasHoy);
            System.out.println("buscamos los registros de COSTOS DE PRODUCTOS DE HOY");
            if (rs.next()) {
               totalUtilidad =rs.getDouble((1));
                System.out.println("TOTAL UTILIDAD Bs "+ totalUtilidad);
                
            } else {
                System.out.println("No encontramos UTILIDAD de hoy bs: "+ totalUtilidad);
                
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Ocurrio un error en CRUDCierre Venta Funcion Venta Total: \n " + e.getMessage());
        }
      return totalUtilidad;
     }
     
     
     
     
     /***
      * El efectivo que obtiene sistema de los registro de hoy
      * Se rebaja con las compras hechas
      * @param comprasHoy : Las compras registradas
      * @return  Double efectivo Bs.
      */
     public double efectivoTotalHoy (double comprasHoy)
     {
         double totalEfectivo =0;
    //Obtenemos la fecha de hoy
    Calendario fechaHoy = new Calendario();
    String fecha = fechaHoy.fechaActual();
    //SELECT sum(`monto_total_bs`) FROM `pedido_pagado` WHERE `opcion_pago` LIKE 'tarjeta' AND `fecha` = '2022-06-29'
   
        String sqlBuscaFechaComprasHoy = "SELECT SUM(monto_total_bs) FROM pedido_pagado "
                + "WHERE opcion_pago LIKE 'EFECTIVO' AND fecha = '" +fecha+ "';";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscaFechaComprasHoy);
            System.out.println("buscamos los registros de EFECTIVO DE HOY");
            if (rs.next()) {
               totalEfectivo =rs.getDouble((1));
                System.out.println("TOTAL EFECTIVO Bs "+ totalEfectivo);
                
            } else {
                System.out.println("No encontramos Efectivo hoy bs: "+ totalEfectivo);
                
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Ocurrio un error en CRUDCierre Venta Funcion Venta Total: \n " + e.getMessage());
        }
      return totalEfectivo-comprasHoy;

     }
     
     public double tarjetaTotalHoy ()
     {
         double totalTarjeta =0;
    //Obtenemos la fecha de hoy
    Calendario fechaHoy = new Calendario();
    String fecha = fechaHoy.fechaActual();
    //SELECT sum(`monto_total_bs`) FROM `pedido_pagado` WHERE `opcion_pago` LIKE 'tarjeta' AND `fecha` = '2022-06-29'
   
        String sqlBuscaFechaComprasHoy = "SELECT SUM(monto_total_bs) FROM pedido_pagado "
                + "WHERE opcion_pago LIKE 'TARJETA' AND fecha = '" +fecha+ "';";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscaFechaComprasHoy);
            System.out.println("buscamos los registros de TARJETA DE HOY");
            if (rs.next()) {
               totalTarjeta =rs.getDouble((1));
                System.out.println("TOTAL TARJETA Bs "+ totalTarjeta);
                
            } else {
                System.out.println("No encontramos Tarjeta hoy bs: "+ totalTarjeta);
                
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Ocurrio un error en CRUDCierre Venta Funcion Venta Total: \n " + e.getMessage());
        }
      return totalTarjeta;

     }
    
     public void guardaVenta (int id_trabajador, int totalComanda, String fecha, double totalTarjeta, double totalEfectivo
     , double totalGastos, double efectivoFisico, double tarjetaFisico, double diferenciaEfectivo,double diferenciaTarjeta,
     String observacion, double ventaDia, double costo_dia, double utilidad_dia)
     {
         /*Guarda la compra*/
        String sqlGuardaPersona = "INSERT INTO cierre_turno_dia VALUES (null,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        
        try {
            try ( PreparedStatement pst = (PreparedStatement) con.prepareCall(sqlGuardaPersona)) {
                pst.setInt(1, id_trabajador);
                pst.setInt(2, totalComanda);
                pst.setString(3, fecha);
                pst.setDouble(4, totalTarjeta);
                pst.setDouble(5, totalEfectivo);
                pst.setDouble(6, totalGastos);
                pst.setDouble(7, efectivoFisico);
                pst.setDouble(8, tarjetaFisico);
                pst.setDouble(9, diferenciaEfectivo);
                pst.setDouble(10, diferenciaTarjeta);
                pst.setString(11, observacion);
                pst.setDouble(12, ventaDia);
                pst.setDouble(13, costo_dia);
                pst.setDouble(14, utilidad_dia);
                int query = pst.executeUpdate();
                System.out.println("el query es: " + query);
                System.out.println("SI SE GUARDÓ a la base de datos tabla: *cierre turno dia* ");
                //cerramos pst
                JOptionPane.showMessageDialog(null, "¡ÉXITO! \n Cierre Dia Registrado");

            }
            con.close(); // cerramos la conexion
        } catch (SQLException e) {
            System.out.println("Ocurrio un error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Datos incorrectos en Cierre del dia");

        }

         
     }
}
