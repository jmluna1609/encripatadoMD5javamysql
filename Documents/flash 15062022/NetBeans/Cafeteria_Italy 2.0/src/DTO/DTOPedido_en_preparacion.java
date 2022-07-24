/*
AQUI SE ENLISTA LOS PEDIDOS QUE HACE EL CLIENTE CUANDO INGRESA AL NEGOCIO
*/
package DTO;

/**
 *
 * @author LUNA
 */
public class DTOPedido_en_preparacion extends DTOProducto_final{
    private int id_pedido_preparacion;
    private int id_producto_final_Prod_Fin;
    private String fecha; // en mysql es YYYY/mm/dd
    private double cantidad;
    private double venta_unitaria_bs;
    private double subtotal_bs;
    private int numero_comanda;
    private double costo_total;
    private double utilidad;
    public DTOPedido_en_preparacion() {
        this.id_pedido_preparacion = id_pedido_preparacion;
        this.fecha = fecha;
        this.id_producto_final_Prod_Fin = id_producto_final_Prod_Fin;
        this.cantidad = cantidad;
        this.venta_unitaria_bs = venta_unitaria_bs;
        this.subtotal_bs = subtotal_bs;
        this.numero_comanda = numero_comanda;
        this.costo_total=costo_total;
        this.utilidad=utilidad;
    }

    /***
     * Funcion para adquirir datos de las VENTAS EN UN DIA
     * @param cantidad
     * @param subtotal_bs
     * @param costo_total
     * @param utilidad
     * @param id_producto_final
     * @param nombre_producto 
     */
    public DTOPedido_en_preparacion(int id_producto_final,String nombre_producto,
            double cantidad, double subtotal_bs, 
            double costo_total, double utilidad) {
        super(id_producto_final, nombre_producto);
        this.cantidad = cantidad;
        this.subtotal_bs = subtotal_bs;
        this.costo_total = costo_total;
        this.utilidad = utilidad;
    }

    
    
    /***
     * Constructor para obtener el reporte de los productos vendidos
     * @param id_Producto_final
     * @param nombre_producto
     * @param cantidad
     * @param subtotal_bs
      
     */
    public DTOPedido_en_preparacion(int id_Producto_final, String nombre_producto, double cantidad, double subtotal_bs ) {
        super(id_Producto_final,nombre_producto);
        this.cantidad = cantidad;
        this.subtotal_bs = subtotal_bs;
    }
    
    
    
    /***
     * Aqui vamos a guardar los pedidos que se estan preparando
     * es decir si quieren cambiar o modificar
     * @param id_pedido_preparacion
     * @param id_producto_final_Producto_Fin
     * @param fecha
     * @param cantidad
     * @param venta_unitaria_bs
     * @param subtotal_bs
     * @param numero_comanda 
     * @param costo_total 
     * @param utilidad 
     */
    //Aqui vamos aguardar los pedidos que se estan preparando
    //Es decir por si quieren cambiar algo entonces se podra modificar.
    
    public DTOPedido_en_preparacion(int id_pedido_preparacion,int id_producto_final_Producto_Fin,
            String fecha, double cantidad, double venta_unitaria_bs, double subtotal_bs, int numero_comanda,
            Double costo_total, Double utilidad) {
        this.id_pedido_preparacion = id_pedido_preparacion;
        this.fecha = fecha;
        this.id_producto_final_Prod_Fin = id_producto_final_Producto_Fin;
        this.cantidad = cantidad;
        this.venta_unitaria_bs = venta_unitaria_bs;
        this.subtotal_bs = subtotal_bs;
        this.numero_comanda = numero_comanda;
        this.costo_total=costo_total;
        this.utilidad = utilidad;
    }
    
    
    public int getId_pedido_preparacion() {
        return id_pedido_preparacion;
    }

    public void setId_pedido_preparacion(int id_pedido_preparacion) {
        this.id_pedido_preparacion = id_pedido_preparacion;
    }

    public double getCosto_total() {
        return costo_total;
    }

    public void setCosto_total(double costo_total) {
        this.costo_total = costo_total;
    }

    public double getUtilidad() {
        return utilidad;
    }

    //metodos accesorio getters y setters
    public void setUtilidad(double utilidad) {
        this.utilidad = utilidad;
    }

    public int getId_pedido_no_registrado() {
        return id_pedido_preparacion;
    }

    public void setId_pedido_no_registrado(int id_pedido_preparacion) {
        this.id_pedido_preparacion = id_pedido_preparacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId_producto_final_Prod_Fin() {
        return id_producto_final_Prod_Fin;
    }

    public void setId_producto_final_Prod_Fin(int id_producto_final_Prod_Fin) {
        this.id_producto_final_Prod_Fin = id_producto_final_Prod_Fin;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getVenta_unitaria_bs() {
        return venta_unitaria_bs;
    }

    public void setVenta_unitaria_bs(double venta_unitaria_bs) {
        this.venta_unitaria_bs = venta_unitaria_bs;
    }

    public double getSubtotal_bs() {
        return subtotal_bs;
    }

    public void setSubtotal_bs(double subtotal_bs) {
        this.subtotal_bs = subtotal_bs;
    }

     public int getNumero_comanda() {
        return numero_comanda;
    }

    public void setNumero_comanda(int numero_comanda) {
        this.numero_comanda = numero_comanda;
    }
    
    
    
}
