package DTO;


/**
 * AQUI ESTARAN LAS VARIABLES DE LOS TRABAJADORES
 */
public class DTOTrabajador {
    //ENCAPSULAMIENTO DE VARIABLES

    private int id_trabajador; //LLAVE PRIMARIA
    private String ciPersona; //LLAVE FORANEA
    private String usuario;
    private String password;

    //CONSTRUCTORES     
   
    //Constructor para el acceso al sistema
    public DTOTrabajador(String usuario, String password) {
        
        this.usuario = usuario;
        this.password = password;

        System.out.println("Llegaron datos de la vista acceso al sistema: ususario: " + usuario + " password: " + password);
    }
    
  

    //METODOS ACCESORIOS
    public int getId_trabajador() {
        return id_trabajador;
    }

    public void setId_trabajador(int id_trabajador) {
        this.id_trabajador = id_trabajador;
    }

    public String getCiPersona() {
        return ciPersona;
    }

    public void setCiPersona(String ciPersona) {
        this.ciPersona = ciPersona;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   

}
