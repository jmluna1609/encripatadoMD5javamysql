/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista.Reportes;

import Controlador.Calendario;
import DTO.DTOPedido_en_preparacion;
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
public class reporte_Ventas_enUn_Dia extends javax.swing.JFrame {

    /**
     * Creates new form reporte_Ventas_enUn_Dia
     */
    public DefaultTableModel modeloTabla; //Modelo de la tabla Ventas en un dia
    public int id_trabajador=0;
    public reporte_Ventas_enUn_Dia() {
        initComponents();
        // this.setResizable(false); //Quitar el maximizar y ampliar pantalla
        this.setLocationRelativeTo(null); //LA PANTALLA ESTARA EN EL CENTRO
       modelo_tablaVentas();
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
    
    public void modelo_tablaVentas() {
        //inicializamos la tabla 
        modeloTabla = new DefaultTableModel();
        //Agregamos columnas
        modeloTabla.addColumn("Código");
        modeloTabla.addColumn("Nombre del Producto");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Venta");
        modeloTabla.addColumn("Costo");
        modeloTabla.addColumn("Utilidad");
        //Colocamos el modelo en el Jtable tabla ventas 
        tablaVentas.setModel(modeloTabla);

        //Colocamos el ancho de la columna
        //Columna Código
        tablaVentas.getColumnModel().getColumn(0).setMaxWidth(70);
        tablaVentas.getColumnModel().getColumn(0).setPreferredWidth(70);
        //Columna Nombre del producto
        tablaVentas.getColumnModel().getColumn(1).setMaxWidth(400);
        tablaVentas.getColumnModel().getColumn(1).setPreferredWidth(400);
         //Columna Cantidad
        tablaVentas.getColumnModel().getColumn(2).setMaxWidth(60);
        tablaVentas.getColumnModel().getColumn(2).setPreferredWidth(60);
        //Columna Venta
        tablaVentas.getColumnModel().getColumn(3).setMaxWidth(85);
        tablaVentas.getColumnModel().getColumn(3).setPreferredWidth(85);        
        //Columna Costo
        tablaVentas.getColumnModel().getColumn(4).setMaxWidth(85);
        tablaVentas.getColumnModel().getColumn(4).setPreferredWidth(85);        
        //Columna Utilidad
        tablaVentas.getColumnModel().getColumn(5).setMaxWidth(80);
        tablaVentas.getColumnModel().getColumn(5).setPreferredWidth(80);    
        }
    
       /***
     * cargamos tabla de Ventas en un dia
     * @param fechaUnica
     * 
     */
    public void cargarVentas(String fechaUnica) {
        
        /*necesito cargar datos*/
        CRUD_Reportes reporte= new CRUD_Reportes();
        ArrayList<DTOPedido_en_preparacion> lisPro = reporte.reporteVentas_UnDia(fechaUnica);
        String filas[] = new String[6];
        for (DTOPedido_en_preparacion pro : lisPro) {
            filas[0] = pro.getId_producto_final()+"";
            filas[1] = pro.getNombre_producto();
            filas[2] = pro.getCantidad()+"";
            filas[3] = pro.getSubtotal_bs()+"";
            filas[4] = pro.getCosto_total()+"";
            filas[5] = pro.getUtilidad()+"";
            
            
         modeloTabla.addRow(filas);
        }
        tablaVentas.setModel(modeloTabla);
            
    }
    
     /***
     * Limpiamos la tabla
     */
    private void limpiarVentaUnDia()
    {
        modeloTabla.getDataVector().removeAllElements();
        tablaVentas.updateUI();
    }
    
     /***
      * Aqui vamos a calcular el total de la suma de la TABLA PEDIDO
      */
     public void totalVenta() {
        //Aqui va a recorer cada fila para que vaya haciendo sumas
       double venta = 0, costo =0, utilidad = 0;
        for (int i = 0; i < tablaVentas.getRowCount(); i++) {
            double subVentaFila_i = 0;
            double subCostoFila_i=0;
            double subUtilidadFila_i=0;
            subVentaFila_i = Double.parseDouble(tablaVentas.getValueAt(i, 3).toString());
            subCostoFila_i = Double.parseDouble(tablaVentas.getValueAt(i, 4).toString());
            subUtilidadFila_i = Double.parseDouble(tablaVentas.getValueAt(i, 5).toString());
            venta = venta + subVentaFila_i;
            costo = costo + subCostoFila_i;
            utilidad= utilidad + subUtilidadFila_i;
        }

        mostrartotal(venta,costo, utilidad);
    }
     
     
/***
 * Envia la suma total a la caja txtVentas
     * @param venta
     * @param costo
     * @param utilidad

 */
    public void mostrartotal(double venta, double costo, double utilidad) {
        //Vamos a validar dos decimales
        //DecimalFormat formato = new DecimalFormat();
        //formato.setMaximumFractionDigits(2);
        //txtCobrarBs.setText(formato.format(totalbs));
        txtVenta.setText("" + venta);
        txtCosto.setText(""+ costo);
        txtUtilidad.setText(""+utilidad);
        
      
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
       txtVenta.setText("");
       txtCosto.setText("");
       txtGastos.setText("");
       txtUtilidad.setText("");
       txtUtilidadBruta.setText("");
       limpiarVentaUnDia();
       
   }
    
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtPassword = new javax.swing.JPasswordField();
        fecha = new com.toedter.calendar.JDateChooser();
        btnBuscar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtVenta = new javax.swing.JTextField();
        txtCosto = new javax.swing.JTextField();
        txtGastos = new javax.swing.JTextField();
        txtUtilidad = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtUtilidadBruta = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("VENTAS EN UN DÍA");
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));

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
                .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtPassword)
                .addContainerGap())
        );

        fecha.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fecha", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N
        fecha.setDateFormatString("dd'-'MM'-'yyyy");
        fecha.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

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

        jPanel3.setBackground(new java.awt.Color(153, 255, 153));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total en Bs.", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText("Venta:");

        jLabel2.setText("Costos:");

        jLabel3.setText("Gastos:");

        jLabel4.setText("Utilidad:");

        txtVenta.setEditable(false);
        txtVenta.setBackground(new java.awt.Color(255, 255, 255));
        txtVenta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtVenta.setToolTipText("Venta del día");
        txtVenta.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        txtCosto.setEditable(false);
        txtCosto.setBackground(new java.awt.Color(255, 255, 255));
        txtCosto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtCosto.setToolTipText("Costos de los productos finales");
        txtCosto.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        txtGastos.setEditable(false);
        txtGastos.setBackground(new java.awt.Color(255, 255, 255));
        txtGastos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtGastos.setToolTipText("Gastos registrados en el día");
        txtGastos.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        txtUtilidad.setEditable(false);
        txtUtilidad.setBackground(new java.awt.Color(255, 255, 255));
        txtUtilidad.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtUtilidad.setToolTipText("Ganancia sin contar los gastos");
        txtUtilidad.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        jLabel5.setText("Utilidad Bruta:");

        txtUtilidadBruta.setEditable(false);
        txtUtilidadBruta.setBackground(new java.awt.Color(255, 255, 255));
        txtUtilidadBruta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtUtilidadBruta.setToolTipText("Ganancias del día");
        txtUtilidadBruta.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtUtilidad, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUtilidadBruta, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGastos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUtilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUtilidadBruta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Cantitdad", "Venta", "Costo", "Utilidad"
            }
        ));
        jScrollPane1.setViewportView(tablaVentas);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnBuscar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fecha, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        
         try {
            limpiarVentaUnDia();
           // Buscamos la fecha
            //Enviamos valores para que sea convertido
            //guardamos fecha en un long
            long f_unica = fecha.getDate().getTime();
            //Indicamos procedencia
            String soyde = "Reporte Ventas un dia";
            //Enviamos valores
            Calendario cal = new Calendario();
            String fecha_unica = cal.obtengoFechaInicio(f_unica, soyde);
            cargarVentas(fecha_unica);
            compras(fecha_unica);
            totalVenta();
            mostrarUtilidadBruta();
        } catch (Exception e) {
            System.out.println("Error: Paquete Vista Ventas, Clase ReporteVentas funcion: btnBuscarVentas. \n "+e.getMessage());
        }
         
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
             ocultarVentas();
         }
    }//GEN-LAST:event_txtPasswordActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtGastos;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUtilidad;
    private javax.swing.JTextField txtUtilidadBruta;
    private javax.swing.JTextField txtVenta;
    // End of variables declaration//GEN-END:variables
}
