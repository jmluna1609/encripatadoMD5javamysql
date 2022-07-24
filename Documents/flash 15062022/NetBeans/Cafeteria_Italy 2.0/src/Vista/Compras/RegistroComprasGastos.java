/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista.Compras;

import Controlador.Calendario;
import Controlador.ValidarLetrasyNumeros;
import DTO.DTORegistro_gastos;
import Modelo.CRUDGastos_Costos;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LUNA
 */
public class RegistroComprasGastos extends javax.swing.JFrame {

     public DefaultTableModel modeloTabla; //Modelo de la tabla compras
     CRUDGastos_Costos crudCompras = new CRUDGastos_Costos();
    public RegistroComprasGastos() {
        initComponents();
          this.setResizable(false); //Quitar el maximizar y ampliar pantalla
        this.setLocationRelativeTo(null); //LA PANTALLA ESTARA EN EL CENTRO
        modelo_tablaCompras();
        cargarCompras();
        
        AgregaplaceHolder(txtAreaDetalle);
        AgregaplaceHoldertxt(txtPassword);
        radioGastos.setSelected(true);
        
    }
    
      /***
     *     permite CREAR EL PLACE HOLDER
     * @param txtArea
     */
      public void AgregaplaceHolder (JTextArea txtArea)
    {
        Font font= txtArea.getFont();
        font = font.deriveFont(Font.ITALIC);
        txtArea.setFont(font);
        txtArea.setForeground(Color.gray); 
    }
    
      /***
       * Permite Quitar el place holder
     * @param txtArea 
       */
    public void QuitarplaceHolder (JTextArea txtArea)
    {
        Font font = txtArea.getFont();
        font = font.deriveFont(Font.PLAIN|Font.BOLD);
        txtArea.setFont(font);
        txtArea.setForeground(Color.black);
    }
    
    
        
      /***
     *     permite CREAR EL PLACE HOLDER
     * @param txt
     */
      public void AgregaplaceHoldertxt (JTextField txt)
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
    public void QuitarplaceHoldertxt (JTextField txt)
    {
        Font font = txt.getFont();
        font = font.deriveFont(Font.PLAIN|Font.BOLD);
        txt.setFont(font);
        txt.setForeground(Color.black);
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radioGroupTipoRegistro = new javax.swing.ButtonGroup();
        panelGastosCompras = new javax.swing.JPanel();
        btnRegistrar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaDetalle = new javax.swing.JTextArea();
        txtMontoBs = new javax.swing.JTextField();
        lblMonto = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        radioGastos = new javax.swing.JRadioButton();
        radioCosto = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablacompras = new javax.swing.JTable();
        txtPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("COSTOS Y GASTOS");
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        panelGastosCompras.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                panelGastosComprasFocusGained(evt);
            }
        });

        btnRegistrar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_Gastos/nota.png"))); // NOI18N
        btnRegistrar.setText("REGISTRAR");
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRegistrar.setIconTextGap(0);
        btnRegistrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnSalir.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_Gastos/anterior.png"))); // NOI18N
        btnSalir.setText("SALIR");
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setIconTextGap(0);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalirMouseClicked(evt);
            }
        });
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        txtAreaDetalle.setColumns(20);
        txtAreaDetalle.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtAreaDetalle.setRows(3);
        txtAreaDetalle.setText("Detalle");
        txtAreaDetalle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAreaDetalleFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAreaDetalleFocusLost(evt);
            }
        });
        txtAreaDetalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaDetalleKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(txtAreaDetalle);

        txtMontoBs.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtMontoBs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMontoBsKeyTyped(evt);
            }
        });

        lblMonto.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblMonto.setText("Monto en Bs:");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tipo de Compra:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        radioGroupTipoRegistro.add(radioGastos);
        radioGastos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        radioGastos.setText("Gasto");

        radioGroupTipoRegistro.add(radioCosto);
        radioCosto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        radioCosto.setText("Costo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radioCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radioCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tablacompras.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tablacompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Fecha", "Personal", "Tipo", "Detalle", "Monto Bs."
            }
        ));
        tablacompras.setAlignmentX(0.8F);
        tablacompras.setGridColor(new java.awt.Color(204, 204, 204));
        tablacompras.setShowGrid(true);
        jScrollPane2.setViewportView(tablacompras);

        txtPassword.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPassword.setText("Contraseña");
        txtPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtPassword.setEchoChar('\u0000');
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasswordFocusLost(evt);
            }
        });

        javax.swing.GroupLayout panelGastosComprasLayout = new javax.swing.GroupLayout(panelGastosCompras);
        panelGastosCompras.setLayout(panelGastosComprasLayout);
        panelGastosComprasLayout.setHorizontalGroup(
            panelGastosComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGastosComprasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGastosComprasLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelGastosComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelGastosComprasLayout.createSequentialGroup()
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRegistrar))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelGastosComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGastosComprasLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSalir)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelGastosComprasLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelGastosComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblMonto)
                            .addComponent(txtMontoBs, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelGastosComprasLayout.setVerticalGroup(
            panelGastosComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGastosComprasLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(panelGastosComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelGastosComprasLayout.createSequentialGroup()
                        .addComponent(lblMonto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMontoBs, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelGastosComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGastosComprasLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panelGastosComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSalir)
                            .addComponent(btnRegistrar)))
                    .addGroup(panelGastosComprasLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGastosCompras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelGastosCompras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
     //Creamos el modelo de la tabla compras
    public void modelo_tablaCompras() {
        //inicializamos la tabla 
        modeloTabla = new DefaultTableModel();
        //Agregamos columnas
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Nombres");
        modeloTabla.addColumn("Apellidos");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Detalle");
        modeloTabla.addColumn("Monto Bs.");
        //Colocamos el modelo en el Jtable tabla pedidos No registrados
        tablacompras.setModel(modeloTabla);

        //Colocamos el ancho de la columna
        //Columna FECHA
        tablacompras.getColumnModel().getColumn(0).setMaxWidth(90);
        tablacompras.getColumnModel().getColumn(0).setPreferredWidth(90);
        //Columna NOMBRES
        tablacompras.getColumnModel().getColumn(1).setMaxWidth(100);
        tablacompras.getColumnModel().getColumn(1).setPreferredWidth(100);
         //Columna APELLIDOS
        tablacompras.getColumnModel().getColumn(2).setMaxWidth(100);
        tablacompras.getColumnModel().getColumn(2).setPreferredWidth(100);
        //Columna TIPO
        tablacompras.getColumnModel().getColumn(3).setMaxWidth(60);
        tablacompras.getColumnModel().getColumn(3).setPreferredWidth(60);
        //Columna DETALLE
        tablacompras.getColumnModel().getColumn(4).setMaxWidth(190);
        tablacompras.getColumnModel().getColumn(4).setPreferredWidth(190);
        //Columna MONTO Bs.
        tablacompras.getColumnModel().getColumn(5).setMaxWidth(60);
        tablacompras.getColumnModel().getColumn(5).setPreferredWidth(60); 
    }
    
    /*
    funcion que lee el tipo de compra que hicieron
    PUEDE SER COSTO O GASTO
    */
    public String tipocompra(){
         String tipo=" ";
        if(radioGastos.isSelected()){
            tipo="GASTO";
            System.out.println("Tipo de compra: "+tipo);
        }
        if(radioCosto.isSelected()){
            tipo="COSTO";
            System.out.println("Tipo de compra: "+tipo);
         }
         return tipo;
    }
    
    
    public void limpiar ()
    {
       txtAreaDetalle.setText("");
        txtMontoBs.setText("");
        txtPassword.setText("");
       
        if(txtAreaDetalle.getText().length()==0)
        {
           //agrega el placeholder
            AgregaplaceHolder(txtAreaDetalle);
            txtAreaDetalle.setText("Detalle");
        }
         if(txtPassword.getText().length()==0)
        {
           //agrega el placeholder
            AgregaplaceHoldertxt(txtPassword);
            txtPassword.setEchoChar('\u0000');
            txtPassword.setText("Contraseña");
        }
    }
    
    /*
    Registra la compra.
    */
    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        try {
            
        Calendario cal=new Calendario();
        String compra=tipocompra(); //variable tipo de compra
        String detalle= txtAreaDetalle.getText(); //variable detalle
        double monto_bs=Double.parseDouble(txtMontoBs.getText()); //variable monto
        String clave_personal=txtPassword.getText(); //con esto vamos a detectar el id del trabajador
        String fecha=cal.fechaActual();
        System.out.println("Estoy en el boton registrar y me llego estos datos:"
                + "Tipo de compra: "+compra+" detalle: "+detalle+" monto en Bs. "+monto_bs+" Clave: "+ clave_personal+" fecha de hoy: "+fecha);
        System.out.println("Ahora lo enviaré al DTORegistro_gastos");
       //instanciamos un objeto de la clase DTORegistro_gastos
       DTORegistro_gastos compras = new DTORegistro_gastos(fecha, compra, detalle, monto_bs);
       //Lo que se envió al paquete DTO, clase DTORegistro_gastos 
       //será para luego guardarlo en el paquete Modelo, clase CRUDGastos_Costos
       int confirmaRegistro = JOptionPane.showConfirmDialog(null, "¿Desea registrarlo?","Advertencia: ",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
          //Enviaré la contraseña directamente al paquete
        CRUDGastos_Costos crud = new CRUDGastos_Costos();
       if(confirmaRegistro==0)
       {
        
        crud.createGastoCosto(clave_personal, compras);
        //Despues ahora nos vamos directo al paquete Modelo a la clase CRUDGastos_Costos
        
       }
       else
       {
           JOptionPane.showMessageDialog(null, "COMPRA NO REGISTRADA \n Contraseña Incorrecta \n Regresando al menú Ventas");
           dispose();
       
       }
       } catch (HeadlessException | NumberFormatException e) {
           JOptionPane.showMessageDialog(null, "Los campos estan vacios \n Regresando al menú Ventas");
            System.out.println("error en el boton registro: "+e.getMessage());
           dispose();
        }
      //Limpia los espacios
      limpiar();
      limpiarCompras();
      cargarCompras();
              
    }//GEN-LAST:event_btnRegistrarActionPerformed
 
    /***
     * cargamos tabla
     */
    private void cargarCompras() {
        String monto="";
        /*necesito cargar datos*/
        CRUDGastos_Costos compras= new CRUDGastos_Costos();
        ArrayList<DTORegistro_gastos> lisPro = compras.readComprasHoy();
        String filas[] = new String[6];
      
        try {
            for (DTORegistro_gastos pro : lisPro) {
               
            filas[0] = pro.getFecha();
            filas[1] = pro.getNombre();
            filas[2] = pro.getApellido();
            filas[3] = pro.getTipo_registro();
            filas[4] = pro.getDetalle();
            filas[5] = monto+pro.getMonto_bs();
            
         modeloTabla.addRow(filas);
        }
        tablacompras.setModel(modeloTabla);
         
        } catch (Exception e) {
        }
           
    }
    
    /***
     * Limpiamos la tabla
     */
    private void limpiarCompras()
    {
        modeloTabla.getDataVector().removeAllElements();
        tablacompras.updateUI();
    }
    
    private void btnSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseClicked
      
    }//GEN-LAST:event_btnSalirMouseClicked

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
  // se va a cerrar la ventana
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtAreaDetalleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaDetalleKeyTyped
        // Lo cambiara a matyusculas
        ValidarLetrasyNumeros.letraSoloMayuscula(evt);
    }//GEN-LAST:event_txtAreaDetalleKeyTyped

    private void txtMontoBsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMontoBsKeyTyped
        //Solo va a leer numeros con una coma decimal
        int ascci = evt.getKeyChar();
        String numdec = txtMontoBs.getText();
        ValidarLetrasyNumeros.numerosReales(ascci, numdec, evt);
    }//GEN-LAST:event_txtMontoBsKeyTyped

    private void panelGastosComprasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_panelGastosComprasFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_panelGastosComprasFocusGained

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        // TODO add your handling code here:
         this.requestFocusInWindow();
    }//GEN-LAST:event_formWindowGainedFocus

    private void txtAreaDetalleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAreaDetalleFocusGained
          if(txtAreaDetalle.getText().equals("Detalle")){
            txtAreaDetalle.setText(null);
            txtAreaDetalle.requestFocus();
            //quita el placeholder
            QuitarplaceHolder(txtAreaDetalle);
           
        }
    }//GEN-LAST:event_txtAreaDetalleFocusGained

    private void txtAreaDetalleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAreaDetalleFocusLost
        // TODO add your handling code here:
          if(txtAreaDetalle.getText().length()==0)
        {
           //agrega el placeholder
            AgregaplaceHolder(txtAreaDetalle);
            txtAreaDetalle.setText("Detalle");
        }
    }//GEN-LAST:event_txtAreaDetalleFocusLost

    private void txtPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusLost
        
          if(txtPassword.getText().length()==0)
        {
           //agrega el placeholder
            AgregaplaceHoldertxt(txtPassword);
            txtPassword.setEchoChar('\u0000');
            txtPassword.setText("Contraseña");
        }
    }//GEN-LAST:event_txtPasswordFocusLost

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
      if(txtPassword.getText().equals("Contraseña")){
            txtPassword.setText(null);
            txtPassword.requestFocus();
            //añade un caracter a txtpassword
            txtPassword.setEchoChar('*');
            //quita el placegholder
            QuitarplaceHoldertxt(txtPassword);
        }
    }//GEN-LAST:event_txtPasswordFocusGained



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblMonto;
    private javax.swing.JPanel panelGastosCompras;
    private javax.swing.JRadioButton radioCosto;
    private javax.swing.JRadioButton radioGastos;
    private javax.swing.ButtonGroup radioGroupTipoRegistro;
    public javax.swing.JTable tablacompras;
    private javax.swing.JTextArea txtAreaDetalle;
    private javax.swing.JTextField txtMontoBs;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
}
