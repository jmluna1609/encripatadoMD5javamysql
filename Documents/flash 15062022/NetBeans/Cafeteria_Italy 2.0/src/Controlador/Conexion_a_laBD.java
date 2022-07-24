/* 
 *Conexion con la base de datos
 */
package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JOSE MARCOS LUNA
 */
public class Conexion_a_laBD {

    Connection conexion = null;

    public Connection conecta() {
        /*Creamos variables para conectarnos a la base de datos
        **user: nombre de usuario, url: coneccion jdbc, password: contraseña*/
        String user = "root";
        String url = "jdbc:mysql://localhost:3306/datos_italy";
        //String url = "jdbc:mysql://localhost:3306/db_italy07072022";
        String password = "";
        /*
        **try{} catch(){} 
        **Para poder capturar errores y siga corriendo el sistema
         */
        try {
            /*
            En sistemas antiguos, para que DriverManager tuviera "registrados" 
            los drivers, era necesario cargar la clase en la máquina virtual.
            Para eso es el Class.forName(), simplemente carga la clase con 
            el nombre indicado. A partir de JDK 6, los drivers JDBC 4
            ya se registran automáticamente y no es necesario el Class.forName()
            sólo que estén en el classpath de la JVM.
             */
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                /*Enviamos a DriverManager url,user,password y guarda en 
                conexxion*/
                conexion = DriverManager.getConnection(url, user, password);
                System.out.println("EXITO! SI SE CONECTOO");

            } catch (SQLException ex) {
                Logger.getLogger(Conexion_a_laBD.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("ALGO PASOOOO CHOQUIIITOOO");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conexion_a_laBD.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("PERO QUEE HA PASADOO");
        }
        return conexion;
    }

    /*Aqui es donde cierra la base de datos*/
    private Connection cerrar() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion_a_laBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conexion;
    }

    /*Desde aqui se usaran las funciones para interactuar con la base de datos*/
 /*Creamos 2 arrays list para mostrar en las tablas de los Jtables (area Recursos Humanos*/
    // List<Datos_TablaRegistro_Trabajadores> listaRegistro_Trabajadores = new ArrayList<>();
    // List<Datos_TablaRegistro_PlanillaTrabajadores> listaRegistro_PlanillaTrabajadores = new ArrayList<>();

    /*Funcion para insertar datos a la BD lunvas*/
 /* public boolean inserta(DatosDTO datos) {
        boolean estado = true;
        try {
            conecta();
            PreparedStatement ps = conexion.prepareStatement("insert into Personas(Nombre,Edad,Sexo) values(?,?,?)");
            ps.setString(1, datos.getNombre());
            ps.setString(2, datos.getEdad());
            ps.setString(3, datos.getSexo());
            ps.execute();
        } catch (SQLException ex) {
            estado = false;
        } finally {
            cerrar(); 
        }
        return estado;
    }
     */
 /*Va a llamar los datos de la tabla registro_trabajadores y la tabla registro_personas
    public boolean cargar() {
        boolean estado = true;
        Datos_TablaRegistro_Trabajadores datos;
        try {
            conecta();
           
          String SQLCargarEmpleado= "select * from registro_trabajadores";
           /*String SQLCargarEmpleado="SELECT  registro_trabajadores.Carnet, registro_personas.nombre,registro_personas.apellido,"
                    + "registro_trabajadores.Cargo, registro_personas.celular, registro_trabajadores.FechaIngreso "
                    + "FROM registro_personas, registro_trabajadores"
                    + " WHERE registro_personas.NumeroP = registro_trabajadores.RegistroPersonas_NumeroP";
            
            PreparedStatement ps1 = conexion.prepareStatement(SQLCargarEmpleado);
            ResultSet resultado = ps1.executeQuery();
           
            while (resultado.next()) {
                  
                datos = new Datos_TablaRegistro_Trabajadores();
               
                datos.setCarnet(resultado.getString("Carnet"));
                datos.setNombre(resultado.getString("Nombre"));
                datos.setApellido(resultado.getString("Apellido"));
                datos.setCelular(resultado.getString("celular"));
                datos.setFechaIngreso(resultado.getString("Ingreso"));
                 
                listaDatos.add(datos);
                
            }
        } catch (SQLException ex) {
        } finally {
            cerrar();
        }
        return estado;

    }
     */
 /*
    public boolean actualiza(DatosDTO datos) {
        boolean estado = true;
        try {
            conecta();
            PreparedStatement ps = conexion.prepareStatement("update Personas set Nombre = ?, Edad = ? , Sexo = ? where id = ?");
            ps.setString(1, datos.getNombre());
            ps.setString(2, datos.getEdad());
            ps.setString(3, datos.getSexo());
            ps.setInt(4, datos.getId());
            ps.execute();
        } catch (SQLException ex) {
            estado = false;
        } finally {
            cerrar();
        }
        return estado;
    }

    public boolean eliminar(DatosDTO datos) {
        boolean estado = true;
        try {
            conecta();
            PreparedStatement ps = conexion.prepareStatement("delete from Personas where id = ?");
            ps.setInt(1,datos.getId());
            ps.execute();
        } catch (SQLException ex) {
            estado = false;
        } finally {
            cerrar();
        }
        return estado;
    }*/
 /*
    public List<Datos_TablaRegistro_Trabajadores> getDatos() {
        return listaDatos;
    }
     */
}
