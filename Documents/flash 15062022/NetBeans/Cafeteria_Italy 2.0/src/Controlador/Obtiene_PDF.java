/*
AQUI VA A GUARDAR EL¿N FORMATO PDF

*/
package Controlador;

/**
 *  
 * @author LUNA
 */

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class Obtiene_PDF {
    
    //Vamos a crear  la fecha con hora actual 
    Calendario fechaHora = new Calendario();
    String fechaconHora  = fechaHora.fechaHoyconMin();
    
    //Aqui va a guardarse el pdf en el mismo proyecto
    String archivo = System.getProperty("user.dir") +"/"+ fechaconHora + ".pdf";

  public void NuevoPdf(){

    Document documento = new Document(PageSize.LETTER, 80, 80, 75, 75);

    //writer es declarado como el método utilizado para escribir en el archivo
    PdfWriter writer = null;
   
    try {      
      //Obtenemos la instancia del archivo a utilizar
      writer = PdfWriter.getInstance(documento, new FileOutputStream(archivo));
      
      
    } catch (FileNotFoundException|DocumentException ex) {
      ex.getMessage();
    }

    //Abrimos el documento para edición
    documento.open();

    //Declaramos un texto como Paragraph
    //Le podemos dar formado como alineación, tamaño y color a la fuente.
    Paragraph titulo = new Paragraph();
    titulo.setAlignment(Paragraph.ALIGN_CENTER);
    titulo.setFont(FontFactory.getFont("Arial",20,Font.BOLD, BaseColor.BLACK));
    titulo.add("Ventas de hoy: "+fechaconHora);
    
    

    try {
      //Agregamos el texto al documento
      documento.add(titulo);
    } catch (DocumentException ex) {
      ex.getMessage();
    }

    documento.close(); //Cerramos el documento
    writer.close(); //Cerramos writer
  }


  /*public static void main(String[] args) {
    //Llamamos por el método para generar el pdf
    new Obtiene_PDF().NuevoPdf();
  }
*/
}
