/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista.Reportes;

import Controlador.Calendario;
import DTO.DTOCierre_turno_dia;
import DTO.DTORegistro_gastos;
import Modelo.Buscador_de_Personas;
import Modelo.CRUDGastos_Costos;
import Modelo.CRUD_Reportes;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LUNA
 */
public class reporte_Compras extends javax.swing.JFrame {

    /**
     * Creates new form reporte_Compras
     */
    
      public DefaultTableModel modeloTabla; //Modelo de la tabla Ventas en un dia
    public int id_trabajador=0;
    public reporte_Compras() {
        initComponents();
         // this.setResizable(false); //Quitar el maximizar y ampliar pantalla
        this.setLocationRelativeTo(null); //LA PANTALLA ESTARA EN EL CENTRO
      
        modelo_tablaVentasDiaria();
        btnBuscar.setEnabled(false);
        
        AgregaplaceHolder(txtPassword);
    }
    
    
    /***
     *     permite CREAR EL PLACE HOLDER USANDO UN JTextField
     * @param txt 
     */
      public void AgregaplaceHolder (JTextField txt)
    {
        Font font= txt.getFont();
        font = font.deriveFont(Font.ITALIC);
        txt.setFont(font);
        txt.setForeground(Color.gray); 
    }
    
      /***
       * Permite Quitar el place holder
       * @param txt 
       */
    public void QuitarplaceHolder (JTextField txt)
    {
        Font font = txt.getFont();
        font = font.deriveFont(Font.PLAIN|Font.BOLD);
        txt.setFont(font);
        txt.setForeground(Color.black);
    }
    
    
     
      /***
     * Habilita el boton cerrar turno
     */
public void habilitaBoton ()
{
    //Se habilita cuando todos los campos requeridos sean ingresados
    String clave = txtPassword.getText();
   
    if(!clave.isEmpty())
    {
        System.out.println("Se lleno espacios requeridos.. habilitamos el boton");
        btnBuscar.setEnabled(true);
    }
    else
    {
        btnBuscar.setEnabled(false);
    }
    
}
     /***
      * Modelo de la tabla de ventas
      */
    
    public void modelo_tablaVentasDiaria() {
        //inicializamos la tabla 
        modeloTabla = new DefaultTableModel();
        //Agregamos columnas
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Descripción");
        modeloTabla.addColumn("Monto en Bs.");
        //Colocamos el modelo en el Jtable tabla ventas 
        tablaCompras.setModel(modeloTabla);

        //Colocamos el ancho de la columna
        //Columna Fecha
        tablaCompras.getColumnModel().getColumn(0).setMaxWidth(110);
        tablaCompras.getColumnModel().getColumn(0).setPreferredWidth(110);
        //Columna Tipo
        tablaCompras.getColumnModel().getColumn(1).setMaxWidth(90);
        tablaCompras.getColumnModel().getColumn(1).setPreferredWidth(90);
         //Columna Descripcion
        tablaCompras.getColumnModel().getColumn(2).setMaxWidth(250);
        tablaCompras.getColumnModel().getColumn(2).setPreferredWidth(250);
        //Columna Monto en Bs
        tablaCompras.getColumnModel().getColumn(3).setMaxWidth(90);
        tablaCompras.getColumnModel().getColumn(3).setPreferredWidth(90);        
         }
    
       /***
     * cargamos tabla de Ventas en un dia
     * @param fecha_inicio
     * @param fecha_fin
     */
    public void cargarCompras(String fecha_inicio, String fecha_fin) {
        
        /*necesito cargar datos*/
        CRUD_Reportes reporte= new CRUD_Reportes();
        ArrayList<DTORegistro_gastos> lisPro = reporte.reporteGastos(fecha_inicio, fecha_fin);
        String filas[] = new String[4];
        for (DTORegistro_gastos pro : lisPro) {
            filas[0] = pro.getFecha()+"";
            filas[1] = pro.getTipo_registro()+"";
            filas[2] = pro.getDetalle()+"";
            filas[3] = pro.getMonto_bs()+"";
                      
         modeloTabla.addRow(filas);
        }
        tablaCompras.setModel(modeloTabla);
            
    }
    
     /***
     * Limpiamos la tabla
     */
    private void limpiarGasto()
    {
        modeloTabla.getDataVector().removeAllElements();
        tablaCompras.updateUI();
    }
    
     /***
      * Aqui vamos a calcular el total de la suma de la TABLA compras
      */
     public void totalCompra() {
        //Aqui va a recorer cada fila para que vaya haciendo sumas
       double  gasto=0;
        for (int i = 0; i < tablaCompras.getRowCount(); i++) {
            
            double subGastosFila_i=0;
            subGastosFila_i = Double.parseDouble(tablaCompras.getValueAt(i, 3).toString());
            gasto=gasto+subGastosFila_i;
        }

        mostrartotal(gasto);
    }
     
     
/***
 * Envia la suma total a la caja txtVentas
   
     * @param gasto

 */
    public void mostrartotal(double gasto) {
        //Vamos a validar dos decimales
        //DecimalFormat formato = new DecimalFormat();
        //formato.setMaximumFractionDigits(2);
        //txtCobrarBs.setText(formato.format(totalbs));
       txtTotalGasto.setText(""+gasto);
        
    }
       
      
      public void ocultarCompras ()
   {
       txtTotalGasto.setText("");
       limpiarGasto();
       
   }
    
 
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtPassword = new javax.swing.JPasswordField();
        fechaInicio = new com.toedter.calendar.JDateChooser();
        fechaFin = new com.toedter.calendar.JDateChooser();
        btnBuscar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtTotalGasto = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCompras = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("REPORTE COMPRAS");
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 102, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 102)));

        txtPassword.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPassword.setText("Contraseña");
        txtPassword.setEchoChar('\u0000');
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasswordFocusLost(evt);
            }
        });
        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtPassword)
                .addContainerGap())
        );

        fechaInicio.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Del:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 0, 12))); // NOI18N
        fechaInicio.setDateFormatString("dd'-'MM'-'yyyy");
        fechaInicio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fechaFin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Al:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 0, 12))); // NOI18N
        fechaFin.setDateFormatString("dd'-'MM'-'yyyy");
        fechaFin.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        btnBuscar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_generales/buscar.png"))); // NOI18N
        btnBuscar.setText("BUSCAR");
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBuscar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(0, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total Bs:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        txtTotalGasto.setEditable(false);
        txtTotalGasto.setBackground(new java.awt.Color(255, 255, 255));
        txtTotalGasto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTotalGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTotalGasto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Fecha", "Tipo", "Descripcion", "Monto Bs."
            }
        ));
        jScrollPane1.setViewportView(tablaCompras);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(236, 236, 236)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fechaInicio, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                            .addComponent(fechaFin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        try {
            limpiarGasto();
           // Buscamos la fecha
            //Enviamos valores para que sea convertido
            //guardamos fecha en un long
            long f_ini = fechaInicio.getDate().getTime();
            //guardamos fecha en un long
            long f_fin = fechaFin.getDate().getTime();
            //Indicamos procedencia
            String soyde = "Reporte Ventas Diarias";
            //Enviamos valores
            Calendario cal = new Calendario();
            String fecha_inicio = cal.obtengoFechaInicio(f_ini, soyde);
            String fecha_fin = cal.obtengoFechaFinal(f_fin, soyde);
            cargarCompras(fecha_inicio, fecha_fin);
           

        } catch (Exception e) {
            System.out.println("Error: Paquete Vista Ventas, Clase ReporteVentas funcion: btnBuscarVentas. \n "+e.getMessage());
        }

        totalCompra();
        
        
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        this.requestFocusInWindow();
    }//GEN-LAST:event_formWindowGainedFocus

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
       if(txtPassword.getText().equals("Contraseña")){
            txtPassword.setText(null);
            txtPassword.requestFocus();
            //añade un caracter a txtpassword
            txtPassword.setEchoChar('*');
            //quita el placegholder
            QuitarplaceHolder(txtPassword);
        }
    }//GEN-LAST:event_txtPasswordFocusGained

    private void txtPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusLost
          if(txtPassword.getText().length()==0)
        {
           //agrega el placeholder
            AgregaplaceHolder(txtPassword);
            txtPassword.setEchoChar('\u0000');
            txtPassword.setText("Contraseña");
        }
    }//GEN-LAST:event_txtPasswordFocusLost

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        Buscador_de_Personas encuentraId = new Buscador_de_Personas();
         String password = txtPassword.getText();
         id_trabajador = encuentraId.obtenerId_trabajador(password);
         if(id_trabajador !=0)
         {
             System.out.println("Habilitamos cajas de texto");
             habilitaBoton(); //funcion para verificar que está escrito esta caja de texto
        
         } else
         {
             btnBuscar.setEnabled(false);
             ocultarCompras();
         }
    }//GEN-LAST:event_txtPasswordActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private com.toedter.calendar.JDateChooser fechaFin;
    private com.toedter.calendar.JDateChooser fechaInicio;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaCompras;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtTotalGasto;
    // End of variables declaration//GEN-END:variables
}
