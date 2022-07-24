/*
SON LAS VARIABLES DONDE GUARDAMOS EL MONTO TOTAL DEL PEDIDO ENTREGAO Y PAGADO
*/
package DTO;

/**
 *
 * @author Marcos LUNA
 * 
 * AQUI VAMOS ACREAR LAS VARIABLES DE LA
 * TABLA DE LA BASE DE DATOS db_italy
 * 
 */
public class DTOPedido_pagado {

private int id_pedido_pagado;
private double monto_total_bs;
private String opcion_pago;
private String ciPersona;
private int numero_comanda_pagado;
private String fecha; //En mysql es YYYY-mm-dd
private int id_Cliente_Cliente;
private double costo_pedido;
private double utilidad_pedido;
  


    public DTOPedido_pagado() {
        this.id_pedido_pagado = id_pedido_pagado;
        this.fecha = fecha;
        this.numero_comanda_pagado = numero_comanda_pagado;
        this.ciPersona = ciPersona;
         this.monto_total_bs = monto_total_bs;
    
        this.opcion_pago = opcion_pago;
        this.id_Cliente_Cliente=id_Cliente_Cliente;
        this.costo_pedido=costo_pedido;
        this.utilidad_pedido=utilidad_pedido;
    }

    public double getCosto_pedido() {
        return costo_pedido;
    }

    public void setCosto_pedido(double costo_pedido) {
        this.costo_pedido = costo_pedido;
    }

    public double getUtilidad_pedido() {
        return utilidad_pedido;
    }

    public void setUtilidad_pedido(double utilidad_pedido) {
        this.utilidad_pedido = utilidad_pedido;
    }

    
    
    public int getId_pedido_pagado() {
        return id_pedido_pagado;
    }

    public void setId_pedido_pagado(int id_pedido_pagado) {
        this.id_pedido_pagado = id_pedido_pagado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getNumero_comanda_pagado() {
        return numero_comanda_pagado;
    }

    public void setNumero_comanda_pagado(int numero_comanda_pagado) {
        this.numero_comanda_pagado = numero_comanda_pagado;
    }

    public String getCiPersona() {
        return ciPersona;
    }

    public void setCiPersona(String ciPersona) {
        this.ciPersona = ciPersona;
    }

    public double getMonto_total_bs() {
        return monto_total_bs;
    }

    public void setMonto_total_bs(double monto_total_bs) {
        this.monto_total_bs = monto_total_bs;
    }

  
    public String getOpcion_pago() {
        return opcion_pago;
    }

    public void setOpcion_pago(String opcion_pago) {
        this.opcion_pago = opcion_pago;
    }

    public int getId_Cliente_Cliente() {
        return id_Cliente_Cliente;
    }

    public void setId_Cliente_Cliente(int id_Cliente_Cliente) {
        this.id_Cliente_Cliente = id_Cliente_Cliente;
    }



   
   
   
           
   
           
           
}
