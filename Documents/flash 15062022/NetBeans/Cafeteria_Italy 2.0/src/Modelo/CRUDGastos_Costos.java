/*
 VAMOS AÑADIR NUEVO GASTO/COSTO Y LEER TODOS LOS GASTOS
 */
package Modelo;

/**
 *
 * @author LUNA
 */
import Controlador.Calendario;
import Controlador.Conexion_a_laBD;
import DTO.DTORegistro_gastos;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class CRUDGastos_Costos {

    Connection con = new Conexion_a_laBD().conecta();

    

    //AQUI VA A GUARDAR EL DATO DE UN GASTO O COSTO
    public void createGastoCosto(String password, DTORegistro_gastos compras) {
        Buscador_de_Personas encuentraId = new Buscador_de_Personas();
        int id_trabajador_Trabajador = encuentraId.obtenerId_trabajador(password); //Lo obtengo cuando compare contraseñas en la anterior funcion comparaPassword
        String fecha = compras.getFecha();
        String tipo_registro = compras.getTipo_registro();
        double monto_bs = compras.getMonto_bs();
        String detalle = compras.getDetalle();
        System.out.println("datos recibidos de DTO a CRUD gastos fecha: " + fecha
                + "tipo registro: " + tipo_registro + " monto: " + monto_bs + " detalle: " + detalle);
        /*Guarda la compra*/
        String sqlGuardaPersona = "INSERT INTO registro_gastos VALUES (null,?,?,?,?,?);";
        try {
            try ( PreparedStatement pst = (PreparedStatement) con.prepareCall(sqlGuardaPersona)) {
                pst.setInt(1, id_trabajador_Trabajador);
                pst.setString(2, fecha);
                pst.setString(3, tipo_registro);
                pst.setDouble(4, monto_bs);
                pst.setString(5, detalle);
                int query = pst.executeUpdate();
                System.out.println("el query es: " + query);
                System.out.println("SI SE GUARDÓ a la base de datos tabla: *registro gastos* ");
                //cerramos pst
                JOptionPane.showMessageDialog(null, "¡ÉXITO! \n Compra Registrada");

            }
            con.close(); // cerramos la conexion
        } catch (SQLException e) {
            System.out.println("Ocurrio un error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Datos incorrectos");

        }

    }

//--------------------CARGAR LAS COMPRAS-----------------
    /***
     * Aqui vamos a leer todas las compras, de forma descendente
     * @return arraylist
     */
    public ArrayList<DTORegistro_gastos> readCompras() {

        //aqui va a mostrar los datos paraque aparezaca en la tabla de Empleados
        /*Los datos son Nombre, Apellido, Cargo, carnet, celular  y fecha de ingreso
         */
        ArrayList<DTORegistro_gastos> listaCompras = new ArrayList<>();
        String SQLCargarCompras = "SELECT registro_gastos.fecha, persona.nombre, persona.apellido ,"
                + "registro_gastos.tipo_registro, registro_gastos.detalle,"
                + "registro_gastos.monto_bs FROM registro_gastos, trabajador,persona "
                + "WHERE registro_gastos.id_trabajador_Trabajador= trabajador.id_trabajador "
                + "and persona.ci=trabajador.ciPersona order BY registro_gastos.id_gasto DESC;";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQLCargarCompras);
            while (rs.next()) {

                listaCompras.add(new DTORegistro_gastos(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6)));
                System.out.println("Estoy dentro del while y estamos leyendo la tabla de compras");

            }
        } catch (SQLException e) {
            System.out.println("Ocurrio un error al cargar las compras: " + e.getMessage());
        }
        return listaCompras;
    }
    
    /***
     * Va a leer las compras realizadas el dia de hoy
     * @return ArrayList <DTORegistro_gastos>
     */
    public ArrayList<DTORegistro_gastos> readComprasHoy(){
        //Obtengo la fecha de hoy
        Calendario cal = new Calendario();
        String fechaHoy= cal.fechaActual();
        
        ArrayList<DTORegistro_gastos> comprasHoy = new ArrayList<>();
        
         //aqui va a mostrar los datos paraque aparezaca en la tabla de Empleados
        /*Los datos son Nombre, Apellido, Cargo, carnet, celular  y fecha de ingreso
         */
        String SQLCargarCompras = "SELECT registro_gastos.fecha, persona.nombre, persona.apellido ,"
                + "registro_gastos.tipo_registro, registro_gastos.detalle,"
                + "registro_gastos.monto_bs FROM registro_gastos, trabajador,persona "
                + "WHERE registro_gastos.id_trabajador_Trabajador= trabajador.id_trabajador "
                + "and persona.ci=trabajador.ciPersona "
                + "and registro_gastos.fecha = '"+fechaHoy+"' "
                + "order BY registro_gastos.id_gasto DESC;";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQLCargarCompras);
            while (rs.next()==true) {

                comprasHoy.add(new DTORegistro_gastos(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6)));
                System.out.println("Estoy dentro del while y estamos leyendo la tabla de compras");

            }
        } catch (SQLException e) {
            System.out.println("Ocurrio un error al cargar las compras: " + e.getMessage());
        }
        return comprasHoy;
        
    }

    /***
     * Funcion de compras de la fecha actual
     * @return double
     */
    public double totalComprasHoy() {
        double totalCompras = 0;
        //Obtenemos la fecha de hoy
        Calendario fechaHoy = new Calendario();
        String fecha = fechaHoy.fechaActual();
        //SELECT SUM(`monto_bs`) FROM `registro_gastos` WHERE `fecha` = '2022-06-26'

        String sqlBuscaFechaComprasHoy = "SELECT SUM(monto_bs) FROM registro_gastos "
                + "WHERE fecha = '" + fecha + "';";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscaFechaComprasHoy);
            System.out.println("buscamos los registros de COMPRAS DE HOY");
            if (rs.next()) {
                totalCompras = rs.getDouble((1));
                System.out.println("TOTAL COMPRAS Bs " + totalCompras);

            } else {
                System.out.println("No encontramos compras hoy bs: " + totalCompras);

            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Ocurrio un error en CRUDGastos_Costos:Funcion TOTALCOMPRASHOY: \n " + e.getMessage());
        }

        return totalCompras;
    }
    
    /***
     * Compras de cualquier fecha que se envíe
     * @param fecha
     * @return double
     */
    public double obtenercomprasFechadas(String fecha)
    {
         double totalCompras = 0;
         //SELECT SUM(`monto_bs`) FROM `registro_gastos` WHERE `fecha` = '2022-06-26'

        String sqlBuscaFechaComprasHoy = "SELECT SUM(monto_bs) FROM registro_gastos "
                + "WHERE fecha = '" + fecha + "';";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlBuscaFechaComprasHoy);
            System.out.println("buscamos los registros de COMPRAS DE LA FECHA "+fecha);
            if (rs.next()) {
                totalCompras = rs.getDouble((1));
                System.out.println("TOTAL COMPRAS Bs " + totalCompras);

            } else {
                System.out.println("No encontramos compras hoy bs: " + totalCompras);

            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Ocurrio un error en CRUDGastos_Costos: funcion:compras fechadas \n " + e.getMessage());
        }

        return totalCompras;
    }
}
