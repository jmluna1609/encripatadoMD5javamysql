/*
SON DATOS DONDE SE VA A GUARDAR CUANDO SE CIERRA O FINALIZA LA VENTA DEL DIA
*/
package DTO;

/**
 *
 * @author LUNA
 */
public class DTOCierre_turno_dia {
  private int id_cierre;
  private int id_trabajador_Trabajador;
  private int total_comanda;
  private String fecha; //EN MYSQL ES YYYY-mm-dd
  private double total_tarjeta_bs;
  private double total_efectivo_bs;
  private double total_gastos_bs;  
  private double efectivo_fisico_bs;
  private double tarjeta_fisico_bs;
  private double diferencia_bs;
  private double diferencia_tarjeta_bs;
  private String observacion;
  private double ventaDia;
  private double costo_dia;
  private double utilidad_dia;

    public DTOCierre_turno_dia(){
        this.id_cierre = id_cierre;
        this.id_trabajador_Trabajador = id_trabajador_Trabajador;
        this.total_comanda = total_comanda;
        this.fecha = fecha;
        this.total_tarjeta_bs = total_tarjeta_bs;
        this.total_efectivo_bs = total_efectivo_bs;
        this.total_gastos_bs = total_gastos_bs;
        this.efectivo_fisico_bs = efectivo_fisico_bs;
        this.tarjeta_fisico_bs = tarjeta_fisico_bs;
        this.diferencia_bs = diferencia_bs;
        this.diferencia_tarjeta_bs = diferencia_tarjeta_bs;
        this.observacion = observacion;
        this.ventaDia= ventaDia;
        this.costo_dia = costo_dia;
        this.utilidad_dia = utilidad_dia;
    }

    /***
     * Aqui almacenamos los sigtes datos
     * para el reporte diario
     * @param fecha
     * @param ventaDia
     * @param costo_dia
     * @param utilidad_dia
     * @param total_gastos_bs 
     */
    public DTOCierre_turno_dia(String fecha, double ventaDia, double costo_dia, double utilidad_dia, double total_gastos_bs) {
        this.fecha = fecha;
        this.total_gastos_bs = total_gastos_bs;
        this.ventaDia = ventaDia;
        this.costo_dia = costo_dia;
        this.utilidad_dia = utilidad_dia;
    }

    
    public double getVentaDia() {
        return ventaDia;
    }

    public void setVentaDia(double ventaDia) {
        this.ventaDia = ventaDia;
    }

    public double getCosto_dia() {
        return costo_dia;
    }

    public void setCosto_dia(double costo_dia) {
        this.costo_dia = costo_dia;
    }

    public double getUtilidad_dia() {
        return utilidad_dia;
    }

    public void setUtilidad_dia(double utilidad_dia) {
        this.utilidad_dia = utilidad_dia;
    }
  
 

   

    public int getId_cierre() {
        return id_cierre;
    }

    public void setId_cierre(int id_cierre) {
        this.id_cierre = id_cierre;
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

   
    public int getTotal_comanda() {
        return total_comanda;
    }

    public void setTotal_comanda(int total_comanda) {
        this.total_comanda = total_comanda;
    }

    public double getTotal_efectivo_bs() {
        return total_efectivo_bs;
    }

    public void setTotal_efectivo_bs(double total_efectivo_bs) {
        this.total_efectivo_bs = total_efectivo_bs;
    }

    public double getTotal_tarjeta_bs() {
        return total_tarjeta_bs;
    }

    public void setTotal_tarjeta_bs(double total_tarjeta_bs) {
        this.total_tarjeta_bs = total_tarjeta_bs;
    }

    public double getTotal_gastos_bs() {
        return total_gastos_bs;
    }

    public void setTotal_gastos_bs(double total_gastos_bs) {
        this.total_gastos_bs = total_gastos_bs;
    }

    public double getEfectivo_fisico_bs() {
        return efectivo_fisico_bs;
    }

    public void setEfectivo_fisico_bs(double efectivo_fisico_bs) {
        this.efectivo_fisico_bs = efectivo_fisico_bs;
    }

    public double getDiferencia_bs() {
        return diferencia_bs;
    }

    public void setDiferencia_bs(double diferencia_bs) {
        this.diferencia_bs = diferencia_bs;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public double getTarjeta_fisico_bs() {
        return tarjeta_fisico_bs;
    }

    public void setTarjeta_fisico_bs(double tarjeta_fisico_bs) {
        this.tarjeta_fisico_bs = tarjeta_fisico_bs;
    }

    public double getDiferencia_tarjeta_bs() {
        return diferencia_tarjeta_bs;
    }

    public void setDiferencia_tarjeta_bs(double diferencia_tarjeta_bs) {
        this.diferencia_tarjeta_bs = diferencia_tarjeta_bs;
    }

  
  
}
