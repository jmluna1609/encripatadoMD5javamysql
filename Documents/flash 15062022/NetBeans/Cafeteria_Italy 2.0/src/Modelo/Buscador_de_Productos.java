/***
 * Es la clase que facilita la busqueda de productos
  ***/
package Modelo;

import DTO.*;
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
public class Buscador_de_Productos {

    //Instanciamos la clase Conexion_a_laBD para que se conecte a la funcion conecta();
    Connection con = new Controlador.Conexion_a_laBD().conecta();

    /***
     * Va añadir datos a la tabla pedido..
     *Estamos llamando esta funcion para que pueda leer los datos de la base de datos.
     *int swcodProduct = es el codigo del producto que va a buscar por sql
     * @param swcodProduct
     * @return 
     */
    public DTOProducto_final obtenerDatos(int swcodProduct) {
        //Crear una variable para guardar los datos que salgan de la base de datos.
        DTO.DTOProducto_final DatosProducto = null;
        System.out.println("Buscamos el codigo: " + swcodProduct);
        //Este SQL sera para buscar los datos del producto
        String sqlBuscarEmpleado = "SELECT * FROM producto_final "
                + "WHERE id_producto_final = " + swcodProduct + ";";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscarEmpleado);

            if (rs.next()) {
                return new DTO.DTOProducto_final(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getDouble(4), rs.getDouble(5),
                        rs.getDouble(6),rs.getString(7));

            }/*Los numeros registrados son la posicion que tiene cada columna de la base de datos
            y esos numero o esas posiciones se lo transfiere al constructor que se creo 
            en la clase DTOProductos */ else {
                JOptionPane.showMessageDialog(null, "No se encuentra ese dato");

            }

        } catch (HeadlessException | SQLException e) {
            System.out.println("Encontramos un error al buscar el codigo: "+e.getMessage());
        }
        return DatosProducto;
    }

    /***
     * Va a buscar el numero de id del producto de la tabla pedido
    para que sea guardado en la tabla pedido diario no registrado
    Estamos llamando esta funcion para que pueda leer los datos de la base de datos.
    se encia el String nombre producto para que compare con el codigo sql Buscar id producto
     * @param nombreProducto
     * @return DTOProducto_final
     */
    public DTOProducto_final obtenerDatosId(String nombreProducto) {
        //Crear una variable para guardar los datos que salgan de la base de datos.
        DTO.DTOProducto_final DatosProducto = null;

        System.out.println("Buscamos el ID del prodcuto: " + nombreProducto);
        //Este SQL sera para buscar el nombre del producto
        String sqlBuscaridProducto = "SELECT * FROM producto_final "
                + "WHERE nombre_producto LIKE '" + nombreProducto + "';";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscaridProducto);
            //Si encuentra entonces va a mostrar los datos y se va a enviar a la clase DTO producto final
            if (rs.next()) {
                
                System.out.println("Dentro del if: Clase Selecciona Pedido");
                return new DTO.DTOProducto_final(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getDouble(4), rs.getDouble(5),
                        rs.getDouble(6), rs.getString(7));

            }/*Los numeros registrados son la posicion que tiene cada columna de la base de datos
            y esos numero o esas posiciones se lo transfiere al constructor que se creo 
            en la clase DTOProducto */ else {
                JOptionPane.showMessageDialog(null, "No se encuentra ese dato");

            }

        } catch (HeadlessException | SQLException e) {
            System.out.println("Error: Clase Selecciona Pedido: "+ e.getMessage());
        }
        return DatosProducto;
    }
    
    /**
    Obtener datos Id V2 solo va retornar el numero entero del id de la 
    * tabla producto final 
     * @param nombreProducto
     * @return 
    **/
    public int obtenerDatosIdV2(String nombreProducto) {
        //Crear una variable para guardar los datos que salgan de la base de datos.
        int id_producto_final=0;

        System.out.println("Buscamos el ID del prodcuto: " + nombreProducto);
        //Este SQL sera para buscar el nombre del producto
        String sqlBuscaridProducto = "SELECT id_producto_final FROM producto_final "
                + "WHERE nombre_producto LIKE '" + nombreProducto + "';";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscaridProducto);
            //Si encuentra entonces va a mostrar los datos y se va a enviar a la clase DTO producto final
            if (rs.next()) {
                
                System.out.println("Dentro del if: Clase: Buscador de productos");
                id_producto_final=rs.getInt(1);
                System.out.println("Encontramos el codigo "+ id_producto_final+ " del producto: "+nombreProducto);

            }/*El numero getInt(1) indica la columnda de la tabla producto final de la base de datos
            */ else {
                JOptionPane.showMessageDialog(null, "No se encuentra ese dato");

            }

        } catch (HeadlessException | SQLException e) {
            System.out.println("Error: Clase Selecciona Pedido: "+ e.getMessage());
        }
        return id_producto_final;
    }
    
    /***
     * BUSCAMOS LA COMANDA Y LA FECHA DEL PEDIDO EN PREPARACION
     * @param id_producto_final
     * @param comanda
     * @param fecha
     * @return DTOPedido_en_preparacion
     */
    public DTOPedido_en_preparacion obtenerPedidoEnPreparacion(int id_producto_final, int comanda, String fecha) {
        System.out.println("Estoy dentro de la clase Buscador de productos."
                + "En la funcion obtenerPedidoEnPreparacion");
        
        //Crear una variable para guardar los datos que salgan de la base de datos.
        DTO.DTOPedido_en_preparacion pedidoEnPreparacionSeleccionado = null;
        System.out.println("Buscamos el id del producto final N: "+id_producto_final);
        System.out.println("Buscamos la comanda N°: " + comanda);
        System.out.println("Buscamos la fecha: " + fecha);

        //Este SQL sera para buscar el PEDIDO EN PREPARACION
        String sqlBuscaridProducto = "SELECT * FROM pedido_en_preparacion "
                + "WHERE id_producto_final_Prod_Fin= "+id_producto_final+
                " fecha LIKE '" + fecha + "' AND numero_comanda = " + comanda + ";";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscaridProducto);
            //Si encuentra entonces va a mostrar los datos y se va a enviar a la clase DTO producto final
            if (rs.next()) {
                System.out.println("Encontramos la comanda y la fecha hora se envio a la clase DTOPedido en preparacion");
                return new DTO.DTOPedido_en_preparacion(rs.getInt(1), rs.getInt(2),
                        rs.getString(3), rs.getDouble(4), rs.getDouble(5),
                        rs.getDouble(6), rs.getInt(7), rs.getDouble(8),rs.getDouble(9));
                
                  } /*Los numeros registrados son la posicion que tiene cada columna de la base de datos
            y esos numero o esas posiciones se lo transfiere al constructor que se creó 
            en la clase DTOProducto */ 
            else {
                JOptionPane.showMessageDialog(null, "No se encuentra ese dato");

            }

        } catch (HeadlessException | SQLException e) {
            System.out.println("Error: Clase: Buscador de productos Funcion: ObtenerPedidoEnpreparacion: "+e.getMessage());
        }

        return pedidoEnPreparacionSeleccionado;
    }
    
    /***
     * Obtengo el costo del producto
     * @param id_producto
     * @return double
     */
    public double obtenerCosto (int id_producto)
    {
        System.out.println("buscamos el costo del producto: "+ id_producto);
        double costo=0;
        /*
        SELECT costo_bs FROM producto_final WHERE id_producto_final = '10082'
        */
        String sqlObtenerCosto = "SELECT costo_bs FROM producto_final WHERE id_producto_final = '"+id_producto+"' ;";
        try {
             Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlObtenerCosto);
            //Si encuentra entonces va a mostrar los datos y se va a enviar a la clase DTO producto final
            if (rs.next()) {
                System.out.println("El costo en la tabla Producto Final");
                costo= rs.getDouble(1);
                  }  
            else {
                System.out.println("No encontré el costo");
            }
            
        } catch (SQLException e) {
            System.out.println("Error: Paq. Modelo, Class: Buscador_Productos, Funcion: obtenerCosto. "+e.getMessage());
        }
        return costo;
    }

}
