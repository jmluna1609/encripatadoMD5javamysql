/*
 DATOS LA TABLA PERSONAS
*/
package DTO;

/*
 Aqui vamos a crear las variables
con el constructor
Con metodos accesorios Getters y Setters
DE LA TABLA DE LA BASE DE DATOS "REGISTRO_PERSONA"
 */
public class DTOPersona {
    //ENCAPSULAMIENTO
    private String ci; //LLAVE PRIMARIA
    private String nombre;
    private String apellido;
    private String celular;
    private String nacimiento;
    
    //CREAMOS LOS CONSTRUCTORES
    //PARA ENCONTRAR LOS DATOS DEL CLIENTE
/***
 * Almacena datos del cliente
 * @param nombre  String
 * @param apellido String
 * @param celular String
 * 
 */
    public DTOPersona(String nombre, String apellido, String celular) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.celular = celular;
    }
    
    
    /***
     * PARA QUE MUESTRE EN LA TABLA DE GASTOS Y COSTOS
     * @param nombre:  empleado
     * @param apellido: empleado
     **/
    public DTOPersona(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
        System.out.println("Capturamos los nombres: "+nombre+" apellidos: "+apellido);
    }

    public DTOPersona() {
    
    }
    
    
    //Constructor para el acceso al sistema
    public DTOPersona(String ci)
    {
        this.ci = ci;
    }

    //METODOS ACCESORIOS GETTERS AND SETTERS
    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(String nacimiento) {
        this.nacimiento = nacimiento;
    }
    
}


