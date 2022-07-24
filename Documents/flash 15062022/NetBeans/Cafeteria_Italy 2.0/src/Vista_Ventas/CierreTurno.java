/*Cuando FINALIZA LAS VENTAS
 */
package Vista_Ventas;

import Vista.Compras.RegistroComprasGastos;
import Controlador.Calendario;
import Controlador.RespaldoDatabase;
import Controlador.SendMail;
import Controlador.ValidarLetrasyNumeros;
import Modelo.Buscador_de_Personas;
import Modelo.CRUDGastos_Costos;
import Modelo.CRUD_Cierre_Venta;
import Vista.Acceso_al_Sistema;
import Vista.Ventana_Principal;
import java.awt.Color;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author LUNA
 */
public class CierreTurno extends javax.swing.JFrame {

    /**
     * Formulario para cerrar turno
     */
    
    public int id_trabajador=0;
    
    public CierreTurno() {
        initComponents();
        // this.setResizable(false); //Quitar el maximizar y ampliar pantalla
        this.setLocationRelativeTo(null); //LA PANTALLA ESTARA EN EL CENTRO
         btnCerrarTurno.setEnabled(false);
         
         bloquearCajadeTextos();
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
    
  public void mostramosVentas ()
  {
      //OBTENEMOS LAS COMPRAS DE HOY
      CRUDGastos_Costos compras = new CRUDGastos_Costos();  
      double comprasHoy = compras.totalComprasHoy();
      txtCompraTotal.setText(""+comprasHoy);
      
      //OBTENEMOS LAS VENTAS DE HOY 
      CRUD_Cierre_Venta venta = new CRUD_Cierre_Venta();
      double ventaHoy= venta.ventaTotal();
      txtVentaTotal.setText(""+ventaHoy);
      
      //Obtenemos la venta EFECTIVO Bs.
      double efectivo = venta.efectivoTotalHoy(comprasHoy);
      txtEfectivoSistema.setText(""+efectivo);
      //Obtenemos la venta TARJETA Bs.
      double tarjeta = venta.tarjetaTotalHoy();
      txtTarjetaSistema.setText(""+tarjeta);
      
  }
   public void ocultarVentas ()
   {
       txtCompraTotal.setText("");
       txtVentaTotal.setText("");
       txtEfectivoSistema.setText("");
       txtTarjetaSistema.setText("");
       
   }
  private void bloquearCajadeTextos (){
      
      txtTarjetaFisico.setEditable(false);
      txtEfectivofisico.setEditable(false);
      txtTotalComandas.setEditable(false);
  }
  
  private void habilitarCajadeTextos(){
      txtTarjetaFisico.setEditable(true);
      txtEfectivofisico.setEditable(true);
      txtTotalComandas.setEditable(true);
  }
  
    /***
     * Habilita el boton cerrar turno
     */
public void habilitaBoton ()
{
    //Se habilita cuando todos los campos requeridos sean ingresados
    String clave = txtPassword.getText();
    String efectivo= txtEfectivofisico.getText();
    String tarjeta= txtTarjetaFisico.getText();
    String comandas= txtTotalComandas.getText();
    String observaciones= txtAreaObservaciones.getText();
    
    if(!clave.isEmpty() && !efectivo.isEmpty() && !tarjeta.isEmpty() && !comandas.isEmpty() && !observaciones.isEmpty())
    {
        System.out.println("Se lleno espacios requeridos.. habilitamos el boton cerrar turno");
        btnCerrarTurno.setEnabled(true);
    }
    else
    {
        btnCerrarTurno.setEnabled(false);
    }
    
}
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtPassword = new javax.swing.JPasswordField();
        panelEfectivoFisico = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtEfectivofisico = new javax.swing.JTextField();
        txtTarjetaFisico = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtTotalComandas = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtEfectivoSistema = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTarjetaSistema = new javax.swing.JTextField();
        btnCerrarTurno = new javax.swing.JButton();
        panelVistaGeneral = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtVentaTotal = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCompraTotal = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txtDiferenciaEfectivo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtDiferenciaTarjeta = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnIrCompras = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaObservaciones = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CIERRE DEL DIA");
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 153, 153));
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
                .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelEfectivoFisico.setBackground(new java.awt.Color(255, 204, 153));
        panelEfectivoFisico.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registro en físico", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("EFECTIVO Bs.:");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("TARJETA Bs.:");

        txtEfectivofisico.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtEfectivofisico.setToolTipText("INGRESE EFECTIVO FISICO SOLO VENTA DE HOY Bs.");
        txtEfectivofisico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEfectivofisicoActionPerformed(evt);
            }
        });
        txtEfectivofisico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEfectivofisicoKeyTyped(evt);
            }
        });

        txtTarjetaFisico.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTarjetaFisico.setToolTipText("INGRESE MONTO EN TARJETA JORNALIZADO EL DIA DE HOY Bs");
        txtTarjetaFisico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTarjetaFisicoActionPerformed(evt);
            }
        });
        txtTarjetaFisico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTarjetaFisicoKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("COMANDAS: ");

        txtTotalComandas.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalComandas.setToolTipText("Numero de Comandas");
        txtTotalComandas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalComandasActionPerformed(evt);
            }
        });
        txtTotalComandas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTotalComandasKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout panelEfectivoFisicoLayout = new javax.swing.GroupLayout(panelEfectivoFisico);
        panelEfectivoFisico.setLayout(panelEfectivoFisicoLayout);
        panelEfectivoFisicoLayout.setHorizontalGroup(
            panelEfectivoFisicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEfectivoFisicoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEfectivoFisicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEfectivoFisicoLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTarjetaFisico, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelEfectivoFisicoLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addComponent(txtEfectivofisico, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelEfectivoFisicoLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTotalComandas, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        panelEfectivoFisicoLayout.setVerticalGroup(
            panelEfectivoFisicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEfectivoFisicoLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelEfectivoFisicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtEfectivofisico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelEfectivoFisicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTarjetaFisico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelEfectivoFisicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTotalComandas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(0, 191, 246));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registro en Sistema", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("EFECTIVO Bs:");

        txtEfectivoSistema.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtEfectivoSistema.setToolTipText("Efectivo registrado en sistema");
        txtEfectivoSistema.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtEfectivoSistema.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("TARJETA Bs.:");

        txtTarjetaSistema.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTarjetaSistema.setToolTipText("Tarjeta registrada en sistema");
        txtTarjetaSistema.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtTarjetaSistema.setEnabled(false);

        btnCerrarTurno.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnCerrarTurno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_generales/cerrar.png"))); // NOI18N
        btnCerrarTurno.setText("CERRAR DÍA");
        btnCerrarTurno.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrarTurno.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCerrarTurno.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCerrarTurno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarTurnoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtEfectivoSistema, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(txtTarjetaSistema))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(btnCerrarTurno)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtEfectivoSistema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtTarjetaSistema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCerrarTurno)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelVistaGeneral.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setText("VENTA Bs.");

        txtVentaTotal.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtVentaTotal.setToolTipText("Efectivo + Tarjeta");
        txtVentaTotal.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtVentaTotal.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("COMPRAS: ");

        txtCompraTotal.setBackground(new java.awt.Color(204, 255, 255));
        txtCompraTotal.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtCompraTotal.setToolTipText("Compras hechas el día de hoy");
        txtCompraTotal.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCompraTotal.setEnabled(false);

        javax.swing.GroupLayout panelVistaGeneralLayout = new javax.swing.GroupLayout(panelVistaGeneral);
        panelVistaGeneral.setLayout(panelVistaGeneralLayout);
        panelVistaGeneralLayout.setHorizontalGroup(
            panelVistaGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVistaGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addGap(13, 13, 13)
                .addComponent(txtVentaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVistaGeneralLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCompraTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelVistaGeneralLayout.setVerticalGroup(
            panelVistaGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVistaGeneralLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(panelVistaGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtVentaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelVistaGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCompraTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 153, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DIFERENCIA Bs.", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        txtDiferenciaEfectivo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtDiferenciaEfectivo.setToolTipText("Diferencia Efectivo (SOBRANTE = VALOR POSITIVO) (FALTANTE= VALOR NEGATIVO)");
        txtDiferenciaEfectivo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtDiferenciaEfectivo.setEnabled(false);
        txtDiferenciaEfectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiferenciaEfectivoActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setText("EFECTIVO Bs:");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setText("TARJETA Bs:");

        txtDiferenciaTarjeta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtDiferenciaTarjeta.setToolTipText("Diferencia en Tarjeta (SOBRANTE = VALOR POSITIVO) (FALTANTE= VALOR NEGATIVO)");
        txtDiferenciaTarjeta.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtDiferenciaTarjeta.setEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDiferenciaEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtDiferenciaTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(txtDiferenciaTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(txtDiferenciaEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("¿REGISTRÓ LAS COMPRAS DE HOY?");

        btnIrCompras.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnIrCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_Gastos/nota.png"))); // NOI18N
        btnIrCompras.setText("IR GASTOS");
        btnIrCompras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIrCompras.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIrCompras.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnIrCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIrComprasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(22, 22, 22))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(btnIrCompras)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIrCompras)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "OBSERVACIONES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtAreaObservaciones.setColumns(20);
        txtAreaObservaciones.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtAreaObservaciones.setRows(5);
        txtAreaObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaObservacionesKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(txtAreaObservaciones);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelVistaGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(panelEfectivoFisico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panelVistaGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelEfectivoFisico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 77, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDiferenciaEfectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiferenciaEfectivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiferenciaEfectivoActionPerformed

    private void btnIrComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIrComprasActionPerformed
        // TODO add your handling code here:
        RegistroComprasGastos compras = new RegistroComprasGastos();
        compras.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnIrComprasActionPerformed


    private void btnCerrarTurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarTurnoActionPerformed
        // Cerramos el día
        int confirmaCierre= JOptionPane.showConfirmDialog(null, "¿Desea Cerrar Venta?","Advertencia: ",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
         
       if(confirmaCierre==0)
       {
           System.out.println("Cerramos venta");
            //capturamos los datos requeridos
            //id trabajador
            int totalComanda = Integer.parseInt(txtTotalComandas.getText());
            Calendario fechaHoy = new Calendario();
            String fecha = fechaHoy.fechaActual();
            double tarjetaSistema = Double.parseDouble(txtTarjetaSistema.getText());
            double efectivoSistema = Double.parseDouble(txtEfectivoSistema.getText());
            double compras = Double.parseDouble(txtCompraTotal.getText());
            double efectivoFisico = Double.parseDouble(txtEfectivofisico.getText());
            double tarjetaFisico = Double.parseDouble(txtTarjetaFisico.getText());
            double diferenciaEfectivo = Double.parseDouble(txtDiferenciaEfectivo.getText());
            double diferenciaTarjeta = Double.parseDouble(txtDiferenciaTarjeta.getText());
            String observacion = txtAreaObservaciones.getText();
            double ventaDia = Double.parseDouble(txtVentaTotal.getText());
            
            //enviamos datos al CRUD Cierre venta
            CRUD_Cierre_Venta cierre = new CRUD_Cierre_Venta();
            double costoDia = cierre.costoTotal();
            double utilidadDia = cierre.utilidadTotal();
            cierre.guardaVenta(id_trabajador, totalComanda, fecha, tarjetaSistema, efectivoSistema,
                    compras, efectivoFisico, tarjetaFisico, diferenciaEfectivo, diferenciaTarjeta, observacion,
                    ventaDia,costoDia,utilidadDia);
            RespaldoDatabase res = new RespaldoDatabase();
            System.out.println("creamos un backup");
            res.backup();
           
            JOptionPane.showMessageDialog(null, "CERRANDO SISTEMA");
            System.exit(0);
            
       }
       else
       {
           System.out.println("No cerramos venta");
       }
    }//GEN-LAST:event_btnCerrarTurnoActionPerformed

        
    /*Creamos una funcion donde le dara el la aprobacion para poder ingresar al sistema*/
    public void confirmacion_contraseña(int sw) {
        if (sw == 1) {
            System.out.println("Usuario o contraseña exitoso, ingresará a la ventana principal");
            Ventana_Principal irVentanaPrincipal = new Ventana_Principal();
            irVentanaPrincipal.setVisible(true);
        }
        if (sw == 2) {
            System.out.println("Usuario o contraseña incorrecta");
            JOptionPane.showMessageDialog(null, "Usuario o contraseña erronea, intente nuevamente");
            dispose();
        }
    }
    
    /***
     * Calcula la diferencia de efectivo en fisico y efectivo en sistema
     * @param efectivoFisico 
     * 
     */
    private void calculaDiferenciaEfectivo(double efectivoFisico)
    {
        double efectivoSistema = Double.parseDouble(txtEfectivoSistema.getText());
        double diferencia = efectivoFisico- efectivoSistema ;
        txtDiferenciaEfectivo.setText(""+diferencia);
    }
    
     /***
     * Calcula la diferencia de efectivo en fisico y efectivo en sistema
     * @param efectivoTarjeta
     * 
     */
    private void calculaDiferenciaTarjeta(double tarjetaFisico)
    {
        double tarjetaSistema = Double.parseDouble(txtTarjetaSistema.getText());
        double diferencia =  tarjetaFisico - tarjetaSistema ;
        txtDiferenciaTarjeta.setText(""+diferencia);
    }
    private void txtEfectivofisicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEfectivofisicoActionPerformed
        // TODO add your handling code here:
        habilitaBoton();
        double efectivofisico = Double.parseDouble(txtEfectivofisico.getText());
        calculaDiferenciaEfectivo(efectivofisico);
    }//GEN-LAST:event_txtEfectivofisicoActionPerformed

   
    private void txtTarjetaFisicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTarjetaFisicoActionPerformed
        // TODO add your handling code here:
        habilitaBoton();
        double tarjetafisico = Double.parseDouble(txtTarjetaFisico.getText());
        calculaDiferenciaTarjeta(tarjetafisico);
    
    }//GEN-LAST:event_txtTarjetaFisicoActionPerformed

    private void txtTotalComandasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalComandasActionPerformed
        // TODO add your handling code here:
        habilitaBoton();
    }//GEN-LAST:event_txtTotalComandasActionPerformed

    private void txtAreaObservacionesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaObservacionesKeyTyped
        // TODO add your handling code here:
        habilitaBoton();
    }//GEN-LAST:event_txtAreaObservacionesKeyTyped

    private void txtEfectivofisicoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEfectivofisicoKeyTyped
        // TODO add your handling code here:
            //Solo va a leer numeros con una coma decimal
        int ascci = evt.getKeyChar();
        String numdec = txtEfectivofisico.getText();
        ValidarLetrasyNumeros.numerosReales(ascci, numdec, evt);
    }//GEN-LAST:event_txtEfectivofisicoKeyTyped

    private void txtTarjetaFisicoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTarjetaFisicoKeyTyped
        // TODO add your handling code here:
            //Solo va a leer numeros con una coma decimal
        int ascci = evt.getKeyChar();
        String numdec = txtTarjetaFisico.getText();
        ValidarLetrasyNumeros.numerosReales(ascci, numdec, evt);
    }//GEN-LAST:event_txtTarjetaFisicoKeyTyped

    private void txtTotalComandasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalComandasKeyTyped
        // TODO add your handling code here:
            //Solo va a leer numeros con una coma decimal
        int ascci = evt.getKeyChar();
        String numdec = txtTotalComandas.getText();
        ValidarLetrasyNumeros.numerosEnteros(ascci, numdec, evt);
    }//GEN-LAST:event_txtTotalComandasKeyTyped

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
         habilitaBoton(); //funcion para verificar que está escrito esta caja de texto
        Buscador_de_Personas encuentraId = new Buscador_de_Personas();
         String password = txtPassword.getText();
         id_trabajador = encuentraId.obtenerId_trabajador(password);
         if(id_trabajador !=0)
         {
             System.out.println("Habilitamos cajas de texto");
             mostramosVentas();
        habilitarCajadeTextos();   
         } else
         {
             bloquearCajadeTextos();
             ocultarVentas();
         }
         
    }//GEN-LAST:event_txtPasswordActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrarTurno;
    private javax.swing.JButton btnIrCompras;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelEfectivoFisico;
    private javax.swing.JPanel panelVistaGeneral;
    private javax.swing.JTextArea txtAreaObservaciones;
    private javax.swing.JTextField txtCompraTotal;
    private javax.swing.JTextField txtDiferenciaEfectivo;
    private javax.swing.JTextField txtDiferenciaTarjeta;
    private javax.swing.JTextField txtEfectivoSistema;
    private javax.swing.JTextField txtEfectivofisico;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtTarjetaFisico;
    private javax.swing.JTextField txtTarjetaSistema;
    private javax.swing.JTextField txtTotalComandas;
    private javax.swing.JTextField txtVentaTotal;
    // End of variables declaration//GEN-END:variables
}
