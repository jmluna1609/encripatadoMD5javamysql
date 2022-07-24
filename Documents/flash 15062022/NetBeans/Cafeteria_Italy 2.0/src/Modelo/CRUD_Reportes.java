/*
 VAMOS A VER LOS REPORTES SEGUN REQUIERA EL USUARIO
 */
package Modelo;

import Controlador.Conexion_a_laBD;
import DTO.DTOCierre_turno_dia;
import DTO.DTOCliente;
import DTO.DTOPedido_en_preparacion;
import DTO.DTORegistro_gastos;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author LUNA
 */
public class CRUD_Reportes {

    Connection con = new Conexion_a_laBD().conecta();

    /**
     * *
     * Indicará la suma de los productos que se vendieron
     *
     * @param fechaInicio
     * @param fechaFin
     * @return ArrayList<> Retorna valores encontrados en la base de datos
     */
    public ArrayList<DTOPedido_en_preparacion> reporteProductos(String fechaInicio, String fechaFin) {

        System.out.println("estoy en CRUD Reportes Productos");
        System.out.println("Fechas recibidas: Del " + fechaInicio + " Al: " + fechaFin);
        ArrayList< DTOPedido_en_preparacion> reporteProductos = new ArrayList<>();
        /* SELECT producto_final.id_producto_final, producto_final.nombre_producto, 
       sum(pedido_en_preparacion.cantidad),sum(pedido_en_preparacion.subtotal_bs) 
       FROM pedido_en_preparacion, producto_final 
       WHERE pedido_en_preparacion.id_producto_final_Prod_Fin = producto_final.id_producto_final
       and pedido_en_preparacion.fecha 
       BETWEEN '2022-06-29' AND '2022-06-29'
       GROUP BY producto_final.id_producto_final;
         */

        String SQLCargarReporte = "SELECT producto_final.id_producto_final, "
                + "producto_final.nombre_producto,"
                + " sum(pedido_en_preparacion.cantidad),"
                + " sum(pedido_en_preparacion.subtotal_bs)"
                + " FROM pedido_en_preparacion,"
                + " producto_final"
                + " WHERE pedido_en_preparacion.id_producto_final_Prod_Fin = producto_final.id_producto_final"
                + " and pedido_en_preparacion.fecha BETWEEN  '" + fechaInicio + "' AND '" + fechaFin + "' "
                + "GROUP BY producto_final.id_producto_final order by pedido_en_preparacion.cantidad DESC;";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQLCargarReporte);
            while (rs.next() == true) {

                reporteProductos.add(new DTOPedido_en_preparacion(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4)));
                System.out.println("Estoy dentro del while y estamos leyendo la tabla Pedidos en preparacion");

            }
        } catch (SQLException e) {
            System.out.println("Error:Paquete modelo, Clase CRUD Reportes, funcion reporteProductos " + e.getMessage());
        }

        return reporteProductos;
    }

    /**
     * *
     * Indica las ventas que ocurrio en un dia INCLUYE COSTOS Y UTILIDADES
     *
     * @param fechaUnica
     * @return ArrayList<>
     */
    public ArrayList<DTOPedido_en_preparacion> reporteVentas_UnDia(String fechaUnica) {
        System.out.println("Estoy en reportes de ventas en un día");
        System.out.println("Tengo la fecha: " + fechaUnica);
        ArrayList< DTOPedido_en_preparacion> reporteVentasUnDia = new ArrayList<>();

        /*
        SELECT producto_final.id_producto_final, producto_final.nombre_producto,
        sum(pedido_en_preparacion.cantidad),sum(pedido_en_preparacion.subtotal_bs),
        SUM(pedido_en_preparacion.costo_total), SUM(pedido_en_preparacion.utilidad) 
        FROM pedido_en_preparacion, producto_final 
        WHERE pedido_en_preparacion.id_producto_final_Prod_Fin = producto_final.id_producto_final
        and pedido_en_preparacion.fecha = '2022-06-29' 
        GROUP BY producto_final.id_producto_final;
         */
        String sqlCargaVentasUnDia = "SELECT producto_final.id_producto_final, producto_final.nombre_producto, "
                + "sum(pedido_en_preparacion.cantidad),sum(pedido_en_preparacion.subtotal_bs), "
                + "SUM(pedido_en_preparacion.costo_total), SUM(pedido_en_preparacion.utilidad) "
                + "FROM pedido_en_preparacion, producto_final "
                + "WHERE pedido_en_preparacion.id_producto_final_Prod_Fin = producto_final.id_producto_final "
                + "and pedido_en_preparacion.fecha = '" + fechaUnica + "' "
                + "GROUP BY producto_final.id_producto_final;";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlCargaVentasUnDia);
            while (rs.next() == true) {
                reporteVentasUnDia.add(new DTOPedido_en_preparacion(rs.getInt(1),
                        rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6)));
                System.out.println("Estoy dentro del while y estamos leyendo la tabla Pedidos en preparacion");

            }
        } catch (SQLException e) {
            System.out.println("Error: paquete Modelo, Clase: crud_reportes, funcion reporteVentasUnDia. " + e.getMessage());
        }

        return reporteVentasUnDia;
    }

    /**
     * *
     * Obtiene el reporte de las ventas diarias
     *
     * @param fecha_inicio
     * @param fecha_fin
     * @return ArrayList<>
     */
    public ArrayList<DTOCierre_turno_dia> reporteDiario(String fecha_inicio, String fecha_fin) {
        System.out.println("Estoy en la funcion REPORTE VENTAS DIARIAS");
        System.out.println("Buscamos las ventas de la fecha: " + fecha_inicio + " al: " + fecha_fin);
        ArrayList<DTOCierre_turno_dia> reportDia = new ArrayList<>();
        /*
        SELECT cierre_turno_dia.fecha, cierre_turno_dia.ventaDia, 
       cierre_turno_dia.costo_dia, cierre_turno_dia.utilidad_dia, cierre_turno_dia.total_gastos_bs  
       FROM cierre_turno_dia  
       WHERE  cierre_turno_dia.fecha 
       BETWEEN '2022-06-29' AND '2022-07-04'
         */
        String sqlReporteDiario = "SELECT cierre_turno_dia.fecha, cierre_turno_dia.ventaDia, "
                + "cierre_turno_dia.costo_dia, cierre_turno_dia.utilidad_dia, cierre_turno_dia.total_gastos_bs  "
                + "FROM cierre_turno_dia "
                + "WHERE  cierre_turno_dia.fecha "
                + "BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' ";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlReporteDiario);
            while (rs.next() == true) {
                reportDia.add(new DTOCierre_turno_dia(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5)));
                System.out.println("Estoy dentro del while y estamos leyendo la tabla cierre turno dia");

            }
        } catch (SQLException e) {
            System.out.println("Error: paquete Modelo, Clase: crud_reportes, funcion reporte diario. " + e.getMessage());
        }
        return reportDia;
    }

    /**
     * *
     * Envia el reporte de los gastos de un rago de fechas
     *
     * @param fecha_inicio
     * @param fecha_fin
     * @return ArrayList<>
     */
    public ArrayList<DTORegistro_gastos> reporteGastos(String fecha_inicio, String fecha_fin) {
        System.out.println("Estoy en la funcion reporte Gastos");
        System.out.println("Las fechas que recibí son: " + fecha_inicio + " al: " + fecha_fin);
        ArrayList< DTORegistro_gastos> gastos = new ArrayList<>();

        /*
       SELECT registro_gastos.fecha, registro_gastos.tipo_registro, registro_gastos.detalle, registro_gastos.monto_bs FROM registro_gastos   
       WHERE  registro_gastos.fecha BETWEEN '2022-06-29' AND '2022-07-04'
         */
        String sqlReporteDiario = " SELECT registro_gastos.fecha, registro_gastos.tipo_registro, "
                + "registro_gastos.detalle, registro_gastos.monto_bs "
                + "FROM registro_gastos "
                + "WHERE  registro_gastos.fecha BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'; ";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlReporteDiario);
            while (rs.next() == true) {
                gastos.add(new DTORegistro_gastos(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4)));
                System.out.println("Estoy dentro del while y estamos leyendo la tabla cierre turno dia");
            }
        } catch (SQLException e) {
            System.out.println("Error: paquete Modelo, Clase: crud_reportes, funcion reporte diario. " + e.getMessage());
        }

        return gastos;

    }

    /**
     * *
     * Indicará los clientes que nos visitan
     *
     * @return ArrayList <>
     */
    public ArrayList<DTOCliente> reporteClientes() {
        System.out.println("estoy en CRUD Reportes Top Clientes");
        ArrayList<DTOCliente> cliente = new ArrayList<>();

        /*
        select persona.nombre,persona.apellido, cliente.contador_compras_hechas, 
        persona.celular 
        FROM persona, cliente 
        WHERE cliente.ciPersona = persona.ci 
        ORDER BY cliente.contador_compras_hechas DESC
         */
        String sqlObtenerCLiente = " select persona.nombre,persona.apellido, cliente.contador_compras_hechas, "
                + "persona.celular "
                + "FROM persona, cliente "
                + "WHERE cliente.ciPersona = persona.ci "
                + "ORDER BY cliente.contador_compras_hechas DESC";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlObtenerCLiente);
            while (rs.next() == true) {
                cliente.add(new DTOCliente(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4)));
                System.out.println("Leyendo clientes");
            }
        } catch (SQLException e) {
            System.out.println("Error: paquete Modelo, Clase: crud_reportes, funcion reporte Clientes. " + e.getMessage());
        }

        return cliente;

    }
}
