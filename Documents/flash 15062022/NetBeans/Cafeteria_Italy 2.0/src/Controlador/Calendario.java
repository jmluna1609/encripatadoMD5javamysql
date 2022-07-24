/*
AQUI VAMOS A OBTENER LAS FECHAS QUE NECESITEMOS
*/
package Controlador;

import Modelo.CRUD_Reportes;
import Vista.Reportes.reporte_Productos_Finales;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author LUNA
 */
public class Calendario {
    LocalDate fechaHoy = LocalDate.now();
    public String fechaActual ()
    {
        System.out.println("La fecha de hoy es: "+fechaHoy.toString());
        
        return fechaHoy.toString();
    }
    
    
    public String fechaHoyconMin ()
    {
         // Obtenemos la fecha actual con minuto y segundo
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
    Date date =new Date();
    String fechaconHora = df.format(date);
    
            return fechaconHora;
    }
    
    /***
     * Obtenemos valores de las fechas ingresadas por el usuario
     * lo convertimos a el tipo de dato que corresponde a la base de datos
     * @param fecha_ini
     * @param procedencia
     * @return String
     */
    public String obtengoFechaInicio(long fecha_ini, String procedencia)
    {
       //pasamos la fecha a un formato fecha Mysql
        Date fechaFormatoMySQL_Inicio;
        fechaFormatoMySQL_Inicio = new java.sql.Date(fecha_ini);
        //lo convertimos a String
        String fecha_inicio = fechaFormatoMySQL_Inicio.toString(); //ESTO ENVIAREMOS AL CRUD
             
        String f_inicio= "";
      System.out.println("La fecha inicio es: "+fecha_inicio);
        
        if(procedencia.equalsIgnoreCase("Reporte Productos Vendidos"))
        {
            System.out.println("Detectamos fechas del Reporte de productos vendidos: ");
            System.out.println("Enviando fecha inicio");
           f_inicio =fecha_inicio;
            
        }
        
         if(procedencia.equalsIgnoreCase("TopClientes"))
        {
            System.out.println("Detectamos fechas de Top Clientes: ");
            System.out.println("Enviando fecha inicio");
            f_inicio =fecha_inicio; 
        }
         if(procedencia.equalsIgnoreCase("Reporte Ventas un dia"))
         {
             System.out.println("Detectamos fecha en VENTAS DE UN DIA");
             System.out.println("Enviando fecha unica");
              f_inicio =fecha_inicio; 
         }
         if(procedencia.equalsIgnoreCase("Reporte Ventas Diarias"))
         {
             System.out.println("Detectamos fechas de Ventas Diarias");
             System.out.println("enviando fecha inicio");
             f_inicio = fecha_inicio;
         }
         if(procedencia.equalsIgnoreCase("RegistroUsuario"))
         {
             System.out.println("Detectamos feccha de Nacimiento");
             System.out.println("enviando fecha nacimiento");
             f_inicio = fecha_inicio;
         }
       return f_inicio; 
    }
    
    /***
     * Obtenemos valores de las fechas ingresadas por el usuario
     * lo convertimos a el tipo de dato que corresponde a la base de datos
     * @param fe_fin 
     * @param procedencia 
     * @return  String
     */
    public String obtengoFechaFinal(long fe_fin, String procedencia)
    {
              
      //pasamos la fecha a un formato fecha Mysql
        Date fechaFormatoMySQL_Fin;
        fechaFormatoMySQL_Fin = new java.sql.Date(fe_fin);
        //lo convertimos a String
        String fecha_fin = fechaFormatoMySQL_Fin.toString(); //ESTO ENVIAREMOS AL CRUD
        
        System.out.println("La fecha fin es: "+ fecha_fin);
        String f_fin="";
        if(procedencia.equalsIgnoreCase("Reporte Productos Vendidos"))
        {
            System.out.println("Detectamos fechas del Reporte de productos vendidos: ");
            System.out.println("Enviando fechas");
            f_fin = fecha_fin;
        }
        
         if(procedencia.equalsIgnoreCase("TopClientes"))
        {
            System.out.println("Detectamos fechas de Top Clientes: ");
            System.out.println("Enviando fechas");
            f_fin= fecha_fin; 
        }
         
          if(procedencia.equalsIgnoreCase("Reporte Ventas Diarias"))
         {
             System.out.println("Detectamos fechas de Ventas Diarias");
             System.out.println("enviando fecha inicio");
             f_fin = fecha_fin;
         }
        return f_fin;
    }
}
