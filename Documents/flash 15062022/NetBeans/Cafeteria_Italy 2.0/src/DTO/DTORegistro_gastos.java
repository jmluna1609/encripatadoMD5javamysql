/*
 * VAMOS A REGISTRAR LOS GASTOS Y LOS COSTOS
 */
package DTO;

/**
 *
 * @author LUNA
 */
public class DTORegistro_gastos extends DTOPersona{
    private int id_gasto;
    private int id_trabajador_Trabajador;
    private String fecha; //EN mysql se debe guardar la fecha YYYY-mm-dd
    private String tipo_registro;
    private String detalle;
    private double monto_bs;
    
 
   
    public DTORegistro_gastos(String fecha, String nombre, String apellido,
            String tipo_registro, String detalle, double monto_bs) {
        super(nombre, apellido);
        this.fecha = fecha;
        this.tipo_registro = tipo_registro;
        this.detalle = detalle;
        this.monto_bs = monto_bs;
       
    }
     
    /***
     * CONSTRUCTOR DONDE ALMACENA DATOS PARA GUARDAR LAS COMPRAS A LA BASE DE DATOS EN 
     * LA TABLA registro_gastos
     * 
     * TAMBIEN SIRVE PARA EL REPORTE DE COMPRAS
     * 
     * super esta VACIO PORQUE NO SE AÑADE DATOS EN LA CLASE PADRE
     * @param fecha
     * @param tipo_registro
     * @param detalle
     * @param monto_bs 
     */
    public DTORegistro_gastos(String fecha, String tipo_registro, String detalle, double monto_bs) {
        super();
        this.fecha = fecha;
        this.tipo_registro = tipo_registro;
        this.detalle = detalle;
        this.monto_bs = monto_bs;
        
    }

    
    
    

    public int getId_gasto() {
        return id_gasto;
    }

    public void setId_gasto(int id_gasto) {
        this.id_gasto = id_gasto;
    }

    public int getId_trabajador_Trabajador() {
        return id_trabajador_Trabajador;
    }

    public void setId_trabajador_Trabajador(int id_trabajador_Trabajador) {
        this.id_trabajador_Trabajador = id_trabajador_Trabajador;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo_registro() {
        return tipo_registro;
    }

    public void setTipo_registro(String tipo_registro) {
        this.tipo_registro = tipo_registro;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public double getMonto_bs() {
        return monto_bs;
    }

    public void setMonto_bs(double monto_bs) {
        this.monto_bs = monto_bs;
    }

  
    
    
}
