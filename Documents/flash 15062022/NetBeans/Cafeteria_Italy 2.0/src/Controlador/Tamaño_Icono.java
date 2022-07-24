/*
*AQUI ESTABLECEMOS EL TAMAÑO DE LOS ICONOS
 */
package Controlador;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Jose Marcos LUNA
 */
public class Tamaño_Icono {

    //vamos a dar el tamaño de la imagen
    public void pintarImagen(JLabel lbl, String ruta) {
        ImageIcon imagen = new ImageIcon(ruta);
        ImageIcon icono = new ImageIcon(imagen.getImage().getScaledInstance(
                lbl.getWidth(), lbl.getHeight(), Image.SCALE_DEFAULT));
        lbl.setIcon(icono);
        //this.repaint();    
    }
    //vamos a dar el tamaño de la imagen del Button

    public void pintarImagenBoton(JButton btn, String ruta) {
        ImageIcon imagen = new ImageIcon(ruta);

        ImageIcon icono = new ImageIcon(imagen.getImage().getScaledInstance(
                btn.getWidth(), btn.getHeight(), Image.SCALE_DEFAULT));
        btn.setIcon(icono);
        //this.repaint();    
    }

}
