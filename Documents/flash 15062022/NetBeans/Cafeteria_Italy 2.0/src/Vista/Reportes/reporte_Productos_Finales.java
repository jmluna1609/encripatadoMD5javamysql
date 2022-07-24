/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista.Reportes;

import Controlador.Calendario;
import DTO.DTOPedido_en_preparacion;
import Modelo.CRUD_Reportes;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LUNA
 */
public class reporte_Productos_Finales extends javax.swing.JFrame {

    public DefaultTableModel modeloTabla; //Modelo de la tabla PRODUCTOS  vendidos
    
    public reporte_Productos_Finales() {
        initComponents();
        this.setResizable(false); //Quitar el maximizar y ampliar pantalla
        this.setLocationRelativeTo(null); //LA PANTALLA ESTARA EN EL CENTRO
        modelo_tablaProductos();
      
    }

     //Creamos el modelo de la tabla compras
    public void modelo_tablaProductos() {
        //inicializamos la tabla 
        modeloTabla = new DefaultTableModel();
        //Agregamos columnas
        modeloTabla.addColumn("Código");
        modeloTabla.addColumn("Nombre del Producto");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Bs.");
        //Colocamos el modelo en el Jtable tabla pedidos No registrados
        tablaProductos.setModel(modeloTabla);

        //Colocamos el ancho de la columna
        //Columna Código
        tablaProductos.getColumnModel().getColumn(0).setMaxWidth(60);
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(60);
        //Columna Nombre del producto
        tablaProductos.getColumnModel().getColumn(1).setMaxWidth(380);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(380);
         //Columna Cantidad
        tablaProductos.getColumnModel().getColumn(2).setMaxWidth(70);
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(70);
        //Columna Bs.
        tablaProductos.getColumnModel().getColumn(3).setMaxWidth(60);
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(60);
        }
    
     /***
     * cargamos tabla
     * @param fechaInicio
     * @param fechaFin
     */
    public void cargarProductos(String fechaInicio, String fechaFin) {
        
       
        /*necesito cargar datos*/
        CRUD_Reportes reporte= new CRUD_Reportes();
        ArrayList<DTOPedido_en_preparacion> lisPro = reporte.reporteProductos(fechaInicio,fechaFin);
        String filas[] = new String[4];
        for (DTOPedido_en_preparacion pro : lisPro) {
            filas[0] = pro.getId_producto_final()+"";
            filas[1] = pro.getNombre_producto();
            filas[2] = pro.getCantidad()+"";
            filas[3] = pro.getSubtotal_bs()+"";
            
         modeloTabla.addRow(filas);
        }
        tablaProductos.setModel(modeloTabla);
            
    }
    
    
    /***
     * Limpiamos la tabla
     */
    private void limpiarProductosVendidos()
    {
        modeloTabla.getDataVector().removeAllElements();
        tablaProductos.updateUI();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelinicial = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        fechaFin = new com.toedter.calendar.JDateChooser();
        fechaInicio = new com.toedter.calendar.JDateChooser();
        btnBuscarVentas = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("REPORTE DE PRODUCTOS");

        panelinicial.setBackground(new java.awt.Color(204, 204, 255));

        tablaProductos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "Nombre del Producto", "Cantidad", "Bs."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaProductos);

        fechaFin.setBackground(new java.awt.Color(204, 255, 204));
        fechaFin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Al:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N
        fechaFin.setDateFormatString("dd'-'MM'-'yyyy");
        fechaFin.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        fechaInicio.setBackground(new java.awt.Color(255, 204, 204));
        fechaInicio.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Del:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N
        fechaInicio.setDateFormatString("dd'-'MM'-'yyyy");
        fechaInicio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        btnBuscarVentas.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnBuscarVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_generales/buscar.png"))); // NOI18N
        btnBuscarVentas.setText("BUSCAR");
        btnBuscarVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarVentas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBuscarVentas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBuscarVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarVentasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelinicialLayout = new javax.swing.GroupLayout(panelinicial);
        panelinicial.setLayout(panelinicialLayout);
        panelinicialLayout.setHorizontalGroup(
            panelinicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelinicialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelinicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                    .addGroup(panelinicialLayout.createSequentialGroup()
                        .addComponent(fechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscarVentas)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelinicialLayout.setVerticalGroup(
            panelinicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelinicialLayout.createSequentialGroup()
                .addGroup(panelinicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelinicialLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(panelinicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelinicialLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnBuscarVentas)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelinicial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelinicial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarVentasActionPerformed

        try {
            limpiarProductosVendidos();
           // Buscamos la fecha
            //Enviamos valores para que sea convertido
            //guardamos fecha en un long
            long f_ini = fechaInicio.getDate().getTime();
            //guardamos fecha en un long
            long f_fin = fechaFin.getDate().getTime();
            //Indicamos procedencia
            String soyde = "Reporte Productos Vendidos";
            //Enviamos valores
            Calendario cal = new Calendario();
            String fecha_inicio = cal.obtengoFechaInicio(f_ini, soyde);
            String fecha_fin = cal.obtengoFechaFinal(f_fin, soyde);
            cargarProductos(fecha_inicio, fecha_fin);
           

        } catch (Exception e) {
            System.out.println("Error: Paquete Vista Ventas, Clase ReporteVentas funcion: btnBuscarVentas. \n "+e.getMessage());
        }


    }//GEN-LAST:event_btnBuscarVentasActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarVentas;
    private com.toedter.calendar.JDateChooser fechaFin;
    private com.toedter.calendar.JDateChooser fechaInicio;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelinicial;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables
}
