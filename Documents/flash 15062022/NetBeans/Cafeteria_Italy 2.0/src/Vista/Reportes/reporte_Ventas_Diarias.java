/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista.Reportes;

import Controlador.Calendario;
import DTO.DTOCierre_turno_dia;
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
public class reporte_Ventas_Diarias extends javax.swing.JFrame {

    /**
     * Creates new form reporte_Ventas_Diarias
     */
    
     public DefaultTableModel modeloTabla; //Modelo de la tabla Ventas en un dia
    public int id_trabajador=0;
    
    public reporte_Ventas_Diarias() {
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
        modeloTabla.addColumn("Ventas");
        modeloTabla.addColumn("Costos");
        modeloTabla.addColumn("Utilidad");
        modeloTabla.addColumn("Gastos");
        //Colocamos el modelo en el Jtable tabla ventas 
        tablaVentas_Diarias.setModel(modeloTabla);

        //Colocamos el ancho de la columna
        //Columna Fecha
        tablaVentas_Diarias.getColumnModel().getColumn(0).setMaxWidth(110);
        tablaVentas_Diarias.getColumnModel().getColumn(0).setPreferredWidth(110);
        //Columna Ventas
        tablaVentas_Diarias.getColumnModel().getColumn(1).setMaxWidth(90);
        tablaVentas_Diarias.getColumnModel().getColumn(1).setPreferredWidth(90);
         //Columna Costos
        tablaVentas_Diarias.getColumnModel().getColumn(2).setMaxWidth(90);
        tablaVentas_Diarias.getColumnModel().getColumn(2).setPreferredWidth(90);
        //Columna Utilidad
        tablaVentas_Diarias.getColumnModel().getColumn(3).setMaxWidth(90);
        tablaVentas_Diarias.getColumnModel().getColumn(3).setPreferredWidth(90);        
        //Columna Gastos
        tablaVentas_Diarias.getColumnModel().getColumn(4).setMaxWidth(90);
        tablaVentas_Diarias.getColumnModel().getColumn(4).setPreferredWidth(90);        
        }
    
       /***
     * cargamos tabla de Ventas en un dia
     * @param fecha_inicio
     * @param fecha_fin
     */
    public void cargarVentas(String fecha_inicio, String fecha_fin) {
        
        /*necesito cargar datos*/
        CRUD_Reportes reporte= new CRUD_Reportes();
        ArrayList<DTOCierre_turno_dia> lisPro = reporte.reporteDiario(fecha_inicio, fecha_fin);
        String filas[] = new String[5];
        for (DTOCierre_turno_dia pro : lisPro) {
            filas[0] = pro.getFecha()+"";
            filas[1] = pro.getVentaDia()+"";
            filas[2] = pro.getCosto_dia()+"";
            filas[3] = pro.getUtilidad_dia()+"";
            filas[4] = pro.getTotal_gastos_bs()+"";            
            
         modeloTabla.addRow(filas);
        }
        tablaVentas_Diarias.setModel(modeloTabla);
            
    }
    
     /***
     * Limpiamos la tabla
     */
    private void limpiarVentaDia()
    {
        modeloTabla.getDataVector().removeAllElements();
        tablaVentas_Diarias.updateUI();
    }
    
     /***
      * Aqui vamos a calcular el total de la suma de la TABLA PEDIDO
      */
     public void totalVenta() {
        //Aqui va a recorer cada fila para que vaya haciendo sumas
       double venta = 0, costo =0, utilidad = 0, gasto=0;
        for (int i = 0; i < tablaVentas_Diarias.getRowCount(); i++) {
            double subVentaFila_i = 0;
            double subCostoFila_i=0;
            double subUtilidadFila_i=0;
            double subGastosFila_i=0;
            subVentaFila_i = Double.parseDouble(tablaVentas_Diarias.getValueAt(i, 1).toString());
            subCostoFila_i = Double.parseDouble(tablaVentas_Diarias.getValueAt(i, 2).toString());
            subUtilidadFila_i = Double.parseDouble(tablaVentas_Diarias.getValueAt(i, 3).toString());
            subGastosFila_i = Double.parseDouble(tablaVentas_Diarias.getValueAt(i, 4).toString());
            venta = venta + subVentaFila_i;
            costo = costo + subCostoFila_i;
            utilidad= utilidad + subUtilidadFila_i;
            gasto=gasto+subGastosFila_i;
        }

        mostrartotal(venta,costo, utilidad, gasto);
    }
     
     
/***
 * Envia la suma total a la caja txtVentas
     * @param venta
     * @param costo
     * @param utilidad
     * @param gasto

 */
    public void mostrartotal(double venta, double costo, double utilidad, double gasto) {
        //Vamos a validar dos decimales
        //DecimalFormat formato = new DecimalFormat();
        //formato.setMaximumFractionDigits(2);
        //txtCobrarBs.setText(formato.format(totalbs));
        txtVentas.setText("" + venta);
        txtCostos.setText(""+ costo);
        txtUtilidad.setText(""+utilidad);
        txtGastos.setText(""+gasto);
        
    }
    
    public void compras (String fecha)
    {
          //OBTENEMOS LAS COMPRAS DE HOY
      CRUDGastos_Costos compra = new CRUDGastos_Costos();  
      double comp= compra.obtenercomprasFechadas(fecha);
      txtGastos.setText(""+comp);
    }
    
    public void mostrarUtilidadBruta ()
    {
       double utilidadParcial= Double.parseDouble(txtUtilidad.getText()) ;
       double compra = Double.parseDouble(txtGastos.getText());
       double utilidadBruta = utilidadParcial - compra;
       txtUtilidadBruta.setText(""+utilidadBruta);
    }
      
      
      public void ocultarVentas ()
   {
       txtVentas.setText("");
       txtCostos.setText("");
       txtGastos.setText("");
       txtUtilidad.setText("");
       txtUtilidadBruta.setText("");
       limpiarVentaDia();
       
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
        fechaInicio = new com.toedter.calendar.JDateChooser();
        fechaFin = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        txtVentas = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUtilidadBruta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCostos = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtGastos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtUtilidad = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaVentas_Diarias = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        txtPassword = new javax.swing.JPasswordField();
        btnBuscar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("VENTAS DIARIAS");
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        fechaInicio.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DEL:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 0, 12))); // NOI18N
        fechaInicio.setDateFormatString("dd'-'MM'-'yyyy");
        fechaInicio.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        fechaFin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "AL:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 0, 12))); // NOI18N
        fechaFin.setDateFormatString("dd'-'MM'-'yyyy");
        fechaFin.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total en Bs.", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        txtVentas.setEditable(false);
        txtVentas.setBackground(new java.awt.Color(255, 255, 255));
        txtVentas.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtVentas.setToolTipText("Ventas registradas en Sistema");

        jLabel1.setText("Utilidad Bruta:");

        jLabel2.setText("Ventas:");

        txtUtilidadBruta.setEditable(false);
        txtUtilidadBruta.setBackground(new java.awt.Color(255, 255, 255));
        txtUtilidadBruta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtUtilidadBruta.setToolTipText("Ganancia aproximada");

        jLabel3.setText("Costos por Elaboración:");

        txtCostos.setEditable(false);
        txtCostos.setBackground(new java.awt.Color(255, 255, 255));
        txtCostos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtCostos.setToolTipText("Costo de los productos vendidos");

        jLabel4.setText("Gastos:");

        txtGastos.setEditable(false);
        txtGastos.setBackground(new java.awt.Color(255, 255, 255));
        txtGastos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtGastos.setToolTipText("Compras registrados en sistema");

        jLabel5.setText("Utilidad:");

        txtUtilidad.setEditable(false);
        txtUtilidad.setBackground(new java.awt.Color(255, 255, 255));
        txtUtilidad.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtUtilidad.setToolTipText("Utilidad sin descontar gastos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtVentas, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(txtCostos, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUtilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtUtilidadBruta, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUtilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUtilidadBruta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCostos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGastos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        tablaVentas_Diarias.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tablaVentas_Diarias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Fecha", "Ventas", "Costos", "Utilidad", "Gastos"
            }
        ));
        jScrollPane1.setViewportView(tablaVentas_Diarias);

        jPanel3.setBackground(new java.awt.Color(255, 153, 153));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addContainerGap())
        );

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnBuscar))
                .addGap(8, 8, 8)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
     try {
            limpiarVentaDia();
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
            cargarVentas(fecha_inicio, fecha_fin);
           

        } catch (Exception e) {
            System.out.println("Error: Paquete Vista Ventas, Clase ReporteVentas funcion: btnBuscarVentas. \n "+e.getMessage());
        }

        totalVenta();
        mostrarUtilidadBruta();
        
        
        
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        this.requestFocusInWindow();
    }//GEN-LAST:event_formWindowGainedFocus

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
             ocultarVentas();
         }
    }//GEN-LAST:event_txtPasswordActionPerformed

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private com.toedter.calendar.JDateChooser fechaFin;
    private com.toedter.calendar.JDateChooser fechaInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaVentas_Diarias;
    private javax.swing.JTextField txtCostos;
    private javax.swing.JTextField txtGastos;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUtilidad;
    private javax.swing.JTextField txtUtilidadBruta;
    private javax.swing.JTextField txtVentas;
    // End of variables declaration//GEN-END:variables
}
