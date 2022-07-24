/*

*/
package DTO;

/**
 *
 * @author LUNA
 */
public class DTOCliente extends DTOPersona{
    //ENCAPSULAMIENTO
    private  int id_cliente; // llave primaria
    private String ciPersona;
    private int contador_compras_hechas;
   // private int id_pedido_pagadoPagado;

    //CONSTRUCTOR

    /***
     * Nos sirve para buscar si el cliente existe
     * @param nombre
     * @param apellido
     * @param celular 
     */
    
    public DTOCliente( String nombre, String apellido, String celular) {
        super(nombre, apellido, celular);
    }

    /***
     * PARA VER EL REPORTE DE LOS CLIENTES FRECUENTES
     * @param nombre
     * @param apellido
     * @param contador_compras_hechas
     * @param celular 
     */
    public DTOCliente( String nombre, String apellido, int contador_compras_hechas,String celular) {
        super(nombre, apellido, celular);
        this.contador_compras_hechas = contador_compras_hechas;
    }

    
    
    /***
     * Constructor: Permite almacenar la cantidad de consumos registrados
     * @param contador_compras_hechas : las compras hechas anteriormente
     */
    public DTOCliente(int contador_compras_hechas) {
        this.contador_compras_hechas = contador_compras_hechas;
    }
   
    

    //METODOS ACCESOSRIOS GETTERS Y SETTERS
    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getCiPersona() {
        return ciPersona;
    }

    public void setCiPersona(String ciPersona) {
        this.ciPersona = ciPersona;
    }

    public int getContador_compras_hechas() {
        return contador_compras_hechas;
    }

    public void setContador_compras_hechas(int contador_compras_hechas) {
        this.contador_compras_hechas = contador_compras_hechas;
    }

    /*
    public int getId_pedido_pagadoPagado() {
        return id_pedido_pagadoPagado;
    }

    public void setId_pedido_pagadoPagado(int id_pedido_pagadoPagado) {
        this.id_pedido_pagadoPagado = id_pedido_pagadoPagado;
    }
    
    */
    
}
