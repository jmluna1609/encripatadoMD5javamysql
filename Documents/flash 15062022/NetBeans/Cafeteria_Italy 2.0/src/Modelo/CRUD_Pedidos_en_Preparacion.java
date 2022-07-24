/*
  Aqui se va el CRUD de los registros de pedidos
 */
package Modelo;

/**
 *
 * @author LUNA
 */
import DTO.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//Aqui vamos a registrar todos los predidos a las tablas:
//pedido diario y la tabla pedido diario no registrado

/*
tabla Pedido Diario = Es la tabla donde se guarda todo
 */
public class CRUD_Pedidos_en_Preparacion {

    //Instanciamos la clase Conexion_a_laBD para que se conecte a la funcion conecta();
    Connection con = new Controlador.Conexion_a_laBD().conecta();

    /*
    Guarda a la tabla pedido_preparacion
     */
    public void createPedidos_a_la_BD(DTOPedido_en_preparacion PedidoenPreparacion) {
         System.out.println("Ahora estamos en la funcion PedidoEnPreparacion: ");

        String sqlGuardaPedidoTemporal = "INSERT INTO pedido_en_preparacion VALUES (NULL,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement pst = (PreparedStatement) con.prepareCall(sqlGuardaPedidoTemporal);
            //pst.setInt(1, 0);
            pst.setInt(1, PedidoenPreparacion.getId_producto_final_Prod_Fin());
            pst.setString(2, PedidoenPreparacion.getFecha());
            pst.setDouble(3, PedidoenPreparacion.getCantidad());
            pst.setDouble(4, PedidoenPreparacion.getVenta_unitaria_bs());
            pst.setDouble(5, PedidoenPreparacion.getSubtotal_bs());
            pst.setInt(6, PedidoenPreparacion.getNumero_comanda());
            pst.setDouble(7, PedidoenPreparacion.getCosto_total());
            pst.setDouble(8, PedidoenPreparacion.getUtilidad());
            pst.executeUpdate();
            System.out.println("Se guardaron los datos que estan en preparacion");
        } catch (SQLException e) {
            System.out.println("Ocurrio un error: Clase: Registro_Pedidos " + e);
        }

    }

    public void deletePedidoSeleccionado(int id_producto_final)
    {
        try {
            String sqlBorrarfila= "DELETE FROM pedido_en_preparacion"
                    + " WHERE id_producto_final_Prod_Fin = "+id_producto_final;
            PreparedStatement borrar = con.prepareStatement(sqlBorrarfila);
            borrar.executeUpdate();
            System.out.println("Se borró el pedido con id: "+ id_producto_final);
        } catch (SQLException e) {
            System.out.println("error: Clase CRUD_Pedidos_en_Preparacion funcion: deletePedidoSeleccionado "+e.getMessage());
        }
    }
}
