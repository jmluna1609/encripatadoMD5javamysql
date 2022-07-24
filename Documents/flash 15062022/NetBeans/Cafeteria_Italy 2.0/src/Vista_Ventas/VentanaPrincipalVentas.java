package Vista_Ventas;

import Vista.Reportes.reporte_Clientes;
import Vista.Reportes.reporte_Productos_Finales;
import Vista.Compras.RegistroComprasGastos;
import Controlador.Calendario;
import Controlador.ValidarLetrasyNumeros;
import DTO.DTOCliente;
import DTO.DTOPedido_en_preparacion;
import DTO.DTOPersona;
import Modelo.Buscador_de_Personas;
import Modelo.CRUD_Pedidos_en_Preparacion;
import Modelo.Buscador_de_Productos;
import Modelo.CRUD_Personas;
import Modelo.CRUD_Pedido_Pagado;
import Vista.Acceso_al_Sistema;
import Vista.Reportes.reporte_Compras;
import Vista.Reportes.reporte_Ventas_Diarias;
import Vista.Reportes.reporte_Ventas_enUn_Dia;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jose Marcos Luna
 */
public class VentanaPrincipalVentas extends javax.swing.JFrame implements Printable {

    /**
     * Creates new form Ventas
     */
    //vamos a crear tablas modelo personalizado
    public DefaultTableModel modelo1; //Modelo de la tabla pedidoElegido, Tabla principal
    public DefaultTableModel modelo2; //Modelo de la tabla pedidos en preparacion, Tabla secundaria
    public DefaultTableModel modelo3; //modelo de la tabla cobro de consumo, Tabla de la derecha para cobrar

    public int swcodigoProducto = 0; //Bandera para detectar el codigo de cada producto
    public double cantidad = 1; //Cantidad que solicita el cliente, por defecto se coloca en uno
    public double subtotal = 0; //Subtotal de cada producto
    public String descripcion = null;
    public double precioUnitario = 0;

    public String[] pedidoElegido = new String[4]; //Vector que permite guardar los datos de cada pedidoElegido ([0]Cantidad, [1]descripcion,
    // [2]P/U Bs. y [3]Subtotal Bs.)
    public String[] pedEnPreparacionEnTabla = new String[5]; //Vector que permite guardar los datos desde la tabla pedidoElegido 
    //a la tabla pedidoElegido en preparacion
    //([0]Numero Comanda, [1]Para
    // [2]Cantidad y [3] Descripcion, [4] Subtotal
    public Object[] filasElegidas = new Object[4]; //Vector que permite guardar los datos de cada pedido a Cobrar ([0]Cantidad, [1]descripcion,
    // [2]P/U Bs. y [3]Subtotal Bs.)

    public int comanda = 0; //Numero de comanda
    public double totalbs = 0; //cifra total del pedidoElegido
    public String para = "Mesa"; //Define si es para mesa llevar o delivery

    public int comandaPagado = 0; //Comanda que se guardará a la BD una vez que esté pagado

    public VentanaPrincipalVentas() {
        initComponents();
        //llamamos al modelo de la tabla de pedidoElegido
        modelo_de_la_tablapedido();
        modelo_tabla_Pedidos_en_Preparacion();
        modelo_de_la_tablacobroConsumo();

        //iniciamos con el color por defecto en  para de pedidoElegido
        //Vamos a cambiar el color de letra cuando es seleccionado el label
        lblMESA.setForeground(Color.red);
        lblLLEVAR.setForeground(Color.black);
        lblDELIVERY.setForeground(Color.black);

        //iniciamos las acciones de clic derecho
        clicDerechoMenutablaPedido();
        clicDerechoTablaPedidoenPreparacion();
        //Iniciamos el contados de comandas
        lblComanda.setText("0");

        // this.setResizable(false); //Quitar el maximizar y ampliar pantalla
        this.setLocationRelativeTo(null); //LA PANTALLA ESTARA EN EL CENTRO
        this.imagenes();

        String tipoCobro = tipocobro();
        System.out.println("Iniciamos el tipo de cobro en: " + tipoCobro);

        btnRegistrar.setForeground(Color.white);
        btnRegistrar.setEnabled(false);
        btnGuardaNuevoCliente.setEnabled(false);
        radioTipoEfectivo.setSelected(true);

    }

    public void contador_comandas() {
        comanda++;
        lblComanda.setText("" + comanda);
    }

    //Creamos el modelo de la tabla de pedidos NO registrados
    private void modelo_tabla_Pedidos_en_Preparacion() {
        //inicializamos la tabla 
        modelo2 = new DefaultTableModel();
        //Agregamos columnas
        modelo2.addColumn("NC"); //numero de comanda
        modelo2.addColumn("Para");//Si es para: mesa, llevar, delivery
        modelo2.addColumn("Cant."); //cantidad del producto
        modelo2.addColumn("Descripción"); //nombre del producto
        modelo2.addColumn("Sub Total.Bs."); //subtotal
        // modelo2.addColumn("TotalBs."); //Total de la compra
        //Colocamos el modelo en el Jtable tabla pedidos No registrados
        tablaPedidosEnPreparacion.setModel(modelo2);

        //Colocamos el ancho de la columna
        //Columna NUMERO COMANDA
        tablaPedidosEnPreparacion.getColumnModel().getColumn(0).setMaxWidth(40);
        tablaPedidosEnPreparacion.getColumnModel().getColumn(0).setPreferredWidth(40);
        //Columna PARA
        tablaPedidosEnPreparacion.getColumnModel().getColumn(1).setMaxWidth(65);
        tablaPedidosEnPreparacion.getColumnModel().getColumn(1).setPreferredWidth(65);
        //Columna CANT.
        tablaPedidosEnPreparacion.getColumnModel().getColumn(2).setMaxWidth(45);
        tablaPedidosEnPreparacion.getColumnModel().getColumn(2).setPreferredWidth(45);
        //Columna DESCRIPCION
        tablaPedidosEnPreparacion.getColumnModel().getColumn(3).setMaxWidth(350);
        tablaPedidosEnPreparacion.getColumnModel().getColumn(3).setPreferredWidth(350);
        //Columna SUBTOTAL
        tablaPedidosEnPreparacion.getColumnModel().getColumn(4).setMaxWidth(80);
        tablaPedidosEnPreparacion.getColumnModel().getColumn(4).setPreferredWidth(80);
        //Columna Total
        //tablaPedidosEnPreparacion.getColumnModel().getColumn(5).setMaxWidth(60);
        //tablaPedidosEnPreparacion.getColumnModel().getColumn(5).setPreferredWidth(60);
    }

    //Creamos el modelo de la tabla pedidoElegido
    private void modelo_de_la_tablapedido() {
        //Inicializamos la tabla
        modelo1 = new DefaultTableModel();
        //Agregamos colimnas anuestro modelo
        modelo1.addColumn("Cantidad");
        modelo1.addColumn("Descripción");
        modelo1.addColumn("P/U Bs.");
        modelo1.addColumn("Subtotal Bs.");
        //Colocamos el modelo a la tabla
        tablaPedido.setModel(modelo1);
        //Colocamos el ancho de la columna

        //Colocamos el ancho de la columna
        //Columna CANTIDAD
        tablaPedido.getColumnModel().getColumn(0).setMaxWidth(65);
        tablaPedido.getColumnModel().getColumn(0).setPreferredWidth(65);
        //Columna DESCRIPCION
        tablaPedido.getColumnModel().getColumn(1).setMaxWidth(405);
        tablaPedido.getColumnModel().getColumn(1).setPreferredWidth(405);
        //Columna PRECIO UNITARIO
        tablaPedido.getColumnModel().getColumn(2).setMaxWidth(70);
        tablaPedido.getColumnModel().getColumn(2).setPreferredWidth(70);
        //Columna SUBTOTAL
        tablaPedido.getColumnModel().getColumn(3).setMaxWidth(75);
        tablaPedido.getColumnModel().getColumn(3).setPreferredWidth(75);
    }

    //Creamos el modelo de la tabla cobro Consumo
    private void modelo_de_la_tablacobroConsumo() {
        //Inicializamos la tabla
        modelo3 = new DefaultTableModel();
        //Agregamos colimnas anuestro modelo
        modelo3.addColumn("Cantidad");
        modelo3.addColumn("Descripción");
        modelo3.addColumn("P/U Bs.");
        modelo3.addColumn("Subtotal Bs.");
        //Colocamos el modelo a la tabla
        tablaCobroConsumo.setModel(modelo3);
        //Colocamos el ancho de la columna

        //Colocamos el ancho de la columna
        //Columna CANTIDAD
        tablaCobroConsumo.getColumnModel().getColumn(0).setMaxWidth(65);
        tablaCobroConsumo.getColumnModel().getColumn(0).setPreferredWidth(65);
        //Columna DESCRIPCION
        tablaCobroConsumo.getColumnModel().getColumn(1).setMaxWidth(405);
        tablaCobroConsumo.getColumnModel().getColumn(1).setPreferredWidth(405);
        //Columna PRECIO UNITARIO
        tablaCobroConsumo.getColumnModel().getColumn(2).setMaxWidth(70);
        tablaCobroConsumo.getColumnModel().getColumn(2).setPreferredWidth(70);
        //Columna SUBTOTAL
        tablaCobroConsumo.getColumnModel().getColumn(3).setMaxWidth(75);
        tablaCobroConsumo.getColumnModel().getColumn(3).setPreferredWidth(75);
    }

    public void agregadatosTablaPedidos() {

        pedidoElegido[0] = "" + (int) cantidad;
        pedidoElegido[1] = "" + descripcion;
        pedidoElegido[2] = "" + precioUnitario;
        subtotal = cantidad * precioUnitario;
        pedidoElegido[3] = "" + subtotal;
        modelo1.addRow(pedidoElegido);
        CalcularTotalBs();
    }

    /**
     * *
     * TIPO DE cobro TARJETA O EFECTIVO
     *
     * @return String
     */
    public String tipocobro() {
        String tipocobro = " ";
        if (radioTipoEfectivo.isSelected()) {
            tipocobro = "EFECTIVO";
            System.out.println("Tipo de cobro: " + tipocobro);
        }
        if (radioTipoTarjeta.isSelected()) {
            tipocobro = "TARJETA";
            System.out.println("Tipo de cobro: " + tipocobro);
        }
        return tipocobro;
    }

    /**
     * *
     * Aqui va a calcular el total que se va a cobrar en Bs.
     *
     **
     */
    public void CalcularTotalCobrarBs() {
        //Aqui va a recorer cada fila para que vaya haciendo sumas
        double cobrarbs = 0;
        for (int i = 0; i < tablaCobroConsumo.getRowCount(); i++) {
            double subtotalFila_i = Double.parseDouble(tablaCobroConsumo.getValueAt(i, 3).toString());
            cobrarbs = cobrarbs + subtotalFila_i;
        }
        //Vamos a validar dos decimales
        //DecimalFormat formato = new DecimalFormat();
        // formato.setMaximumFractionDigits(2);
        //txtCobrarBs.setText(formato.format(totalbs));
        txtTotalCobrarBs.setText("" + cobrarbs);

    }

    //CALCULAR CAMBIO
    public void cambioBs() {

        double efectivo = Double.parseDouble(txtEfectivo.getText());
        double totalCobrar = Double.parseDouble(txtTotalCobrarBs.getText());
        double cambio = efectivo - totalCobrar;
        txtCambio.setText(cambio + "");

    }

    //Aqui podra editarse la cantidad DENTRO DE LA TABLA PEDIDO y se cambiará por defecto el total
    public void nuevosubtotal(int fila) {
        //int fila=tablaPedido.getSelectedRow();
        if (fila >= 0) {
            precioUnitario = Double.parseDouble(tablaPedido.getValueAt(fila, 2).toString());
            subtotal = cantidad * precioUnitario;

            System.out.println("el nuevo valor en la columna subtotal: " + subtotal);
            tablaPedido.setValueAt("" + subtotal, fila, 3);
            CalcularTotalBs();

        }
        cantidad = 1;

    }

    //Aqui vamos a calcular el total de la suma de la TABLA PEDIDO
    public void CalcularTotalBs() {
        //Aqui va a recorer cada fila para que vaya haciendo sumas
        totalbs = 0;
        for (int i = 0; i < tablaPedido.getRowCount(); i++) {
            double subtotalFila_i = 0;
            subtotalFila_i = Double.parseDouble(tablaPedido.getValueAt(i, 3).toString());
            totalbs = totalbs + subtotalFila_i;
        }

        mostrartotal(totalbs);
    }

    //aqui va a mostrar en las cajas de texto los valores.
    public void mostrartotal(double totalbs) {
        //Vamos a validar dos decimales
        //DecimalFormat formato = new DecimalFormat();
        //formato.setMaximumFractionDigits(2);
        //txtCobrarBs.setText(formato.format(totalbs));
        txtTotalBs.setText("" + totalbs);
    }

    private void imagenes() {
        /*importamos la clase Tamaño icono para luego crear un objeto
    luego llamamos la funcion pintar imagen para que pueda configurar el tamaño de las imagenes*/
 /*    Tamaño_Icono llamaClaseTamIcon = new Tamaño_Icono();
     llamaClaseTamIcon.pintarImagen(lblMesa, "src\\iconos_Ventas\\mesa.png");
        llamaClaseTamIcon.pintarImagen(lblDelivery, "src\\iconos_Ventas\\repartidor.png");
        llamaClaseTamIcon.pintarImagen(lblLlevar, "src\\iconos_Ventas\\llevar.png");

        llamaClaseTamIcon.pintarImagenBoton(btnPreparar, "src\\iconos_Ventas\\btnOrdenar.png");
        llamaClaseTamIcon.pintarImagenBoton(btnLimpiar, "src\\iconos_Ventas\\btnLimpiar.png");
//    llamaClaseTamIcon.pintarImagenBoton(btnCancelar, "src\\iconos_Ventas\\btnCancelar.png");
        llamaClaseTamIcon.pintarImagenBoton(btnRegistrar, "src\\iconos_Ventas\\btnRegistrar.png");
//        llamaClaseTamIcon.pintarImagenBoton(btnReimprimir, "src\\iconos_Ventas\\btnReimprimir.png");
         */
    }

    //Esta funcion aplica para cuando haga clic derecho sobre la tabla pedidoElegido
    public void clicDerechoMenutablaPedido() {

        //Creamos el menu para cuando haga clic derecho
        //JMenuItem editar= new JMenuItem("Editar");
        JMenuItem quitar = new JMenuItem("Eliminar");
        // popupTablaPedido.add(editar);
        popupTablaPedido.addSeparator();
        popupTablaPedido.add(quitar);//,getIcon("\\iconos_Ventas\\borrarfila.png", 25, 25));
        popupTablaPedido.addSeparator();

        tablaPedido.setComponentPopupMenu(popupTablaPedido);

        //Vamos añadir el codigo a cada opcion
        quitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Sirve para poder enviar para quien va la orden de eliminar
                int bandera = 0;
                eliminarfila(bandera);
            }
        });

    }

    //Esta funcion aplica para cuando haga
    //clic derecho sobre la tabla pedidoElegido en preparacion
    //DE ESA MANERA NUNCA SE REGISTRARA A LA TABLA DE PEDIDOS DIARIOS
    public void clicDerechoTablaPedidoenPreparacion() {

        //Creamos el menu para cuando haga clic derecho
        //JMenuItem editar= new JMenuItem("Editar");
        JMenuItem quitar = new JMenuItem("Quitar pedido");
        JMenuItem cobrar = new JMenuItem("Cobrar pedido");
        // popupTablaPedido.add(editar);
        popupTablaPreparacion.addSeparator();
        popupTablaPreparacion.add(quitar);//,getIcon("\\iconos_Ventas\\borrarfila.png", 25, 25));
        popupTablaPreparacion.addSeparator();
        popupTablaPreparacion.add(cobrar);// aqui va a cobrar el pedido
        popupTablaPreparacion.addSeparator();

        tablaPedidosEnPreparacion.setComponentPopupMenu(popupTablaPreparacion);

        //Vamos añadir el codigo a cada opcion
        quitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Sirve para poder enviar para quien va la orden de eliminar
                int bandera = 1;
                eliminarfila(bandera);
            }
        });

        //VAMOS HACER QUE PASE DATOS A LA TABLA COBRO CONSUMO
        cobrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //HABILITAR EL BOTON REGISTRAR
                btnRegistrar.setEnabled(true);
                btnRegistrar.setForeground(Color.green);
                //SALTA A EL PANEL DE COBRAR CLIENTE
                tabbedMenuCobrar.setSelectedIndex(1);
                //tabbedMenuCobrar.setEnabledAt(0, false);
                tabbedMenuCobrar.setEnabledAt(1, true);
                //panelCobrarCliente.setVisible(true); //cambio de panel a cpbrar cliente
                //AGREGA DATOS A TABLA COBRAR
                agregoDatosATablaCobrar();
            }
        });

    }

    /**
     * *
     * Agrego datos de la tabla pedido en preparacion a la tabla Tabla a Cobrar
     * con la facilidad de seleccionar la cantidad de pedidos que guste
     *
     */
    public void agregoDatosATablaCobrar() {
        //double []precioUni;

        int[] filas = tablaPedidosEnPreparacion.getSelectedRows();
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro que desea cobrar los pedidos seleccionados? \n", "ADVERTENCIA", 1);
        if (confirmacion == 0) {
            System.out.println("hagarré " + filas.length + " filas");
            System.out.println("Procedo a pasar los datos a la tabla cobrar consumo ");
            for (int i = 0; i < filas.length; i++) {
                System.out.println("fila: " + i);
                filasElegidas[0] = modelo2.getValueAt(filas[i], 2); //CANTIDAD
                filasElegidas[1] = modelo2.getValueAt(filas[i], 3); // DESCRIPCION
                // double [] precioUni = new double [i]; 
                double precioUni = Double.parseDouble(tablaPedidosEnPreparacion.getValueAt(filas[i], 4).toString()) / Double.parseDouble(tablaPedidosEnPreparacion.getValueAt(filas[i], 2).toString());

                filasElegidas[2] = precioUni; // PRECIO UNITARIO
                filasElegidas[3] = modelo2.getValueAt(filas[i], 4); //SUBTOTAL Bs.
                modelo3.addRow(filasElegidas);
                comandaPagado = Integer.parseInt(tablaPedidosEnPreparacion.getValueAt(filas[i], 0).toString());//NUMERO COMANDA
            }
            System.out.println("Numero de comanda que se va a cobrar: " + comandaPagado);
            CalcularTotalCobrarBs();

            System.out.println("Limpiamos los elementos seleccionados");
            for (int i = filas.length - 1; i >= 0; i--) {
                System.out.println("se elimina la fila: " + i);
                modelo2.removeRow(filas[i]);
            }
            /*  int fila = tablaPedido.getRowCount();
        for (int i = fila - 1; i >= 0; i--) {
            modelo1.removeRow(i);
            CalcularTotalBs();
        }
             */

        }

    }

    //aqui vamos a instanciar las imagenes enviamos la ruta y las dimensiones
    /*public Icon getIcon (String ruta, int width, int height){
        Icon icono= new ImageIcon(new ImageIcon(getClass().getResource(ruta)).getImage().getScaledInstance(width, height,0));
        return icono;
    }*/
    //aqui va a eliminar una fila de la tabla pedidoElegido
    public void eliminarfila(int bandera) {

        int n1 = JOptionPane.showConfirmDialog(null, "¿Eliminar la fila?", "ADVERTENCIA", 0);
        if (n1 == 0 && bandera == 0) {
            int fila = tablaPedido.getSelectedRow();
            if (fila >= 0) {
                modelo1.removeRow(fila);
                CalcularTotalBs();
            }
        } else {
            if (n1 == 0 && bandera == 1) {
                int fila = tablaPedidosEnPreparacion.getSelectedRow();
                if (fila >= 0) {
                    //Buscamos el numero de la fila y el nombre del producto
                    Buscador_de_Productos buscaprod = new Buscador_de_Productos();
                    String nombreProducto = tablaPedidosEnPreparacion.getValueAt(fila, 3).toString();
                    //Obtenemos el id del producto seleccionado
                    int id_producto = buscaprod.obtenerDatosIdV2(nombreProducto);

                    //¨Procedemos a eliminar de la base de datos enviando el id del producto
                    CRUD_Pedidos_en_Preparacion delete = new CRUD_Pedidos_en_Preparacion();
                    delete.deletePedidoSeleccionado(id_producto);

                    //Eliminamos la fila de la tabla Pedidos en preparacion
                    modelo2.removeRow(fila);

                }

            }
        }

    }

    //Esta funcion va a eliminar todo lo que esta en la tabla pedidoElegido
    public void eliminarTodo() {

        int n1 = JOptionPane.showConfirmDialog(null, "¿Eliminar toda la orden?", "ADVERTENCIA", 0);
        if (n1 == 0) {
            int fila = tablaPedido.getRowCount();
            for (int i = fila - 1; i >= 0; i--) {
                modelo1.removeRow(i);
                CalcularTotalBs();
            }
        } else {

        }
    }

    //Esta funcion va a eliminar todo lo que esta en la tabla pedidoElegido
    public void limpiartabla() {

        int fila = tablaPedido.getRowCount();
        for (int i = fila - 1; i >= 0; i--) {
            modelo1.removeRow(i);
            CalcularTotalBs();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupTablaPedido = new javax.swing.JPopupMenu();
        popupTablaPreparacion = new javax.swing.JPopupMenu();
        grupoTipoPago = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        panelTipo = new javax.swing.JPanel();
        lblMesa = new javax.swing.JLabel();
        lblDelivery = new javax.swing.JLabel();
        lblMESA = new javax.swing.JLabel();
        lblLlevar = new javax.swing.JLabel();
        lblLLEVAR = new javax.swing.JLabel();
        lblDELIVERY = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPedido = new javax.swing.JTable();
        panelBotones = new javax.swing.JPanel();
        btnLimpiar = new javax.swing.JButton();
        btnPreparar = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        lblMESA1 = new javax.swing.JLabel();
        lblMESA2 = new javax.swing.JLabel();
        lblMESA4 = new javax.swing.JLabel();
        panelPago = new javax.swing.JPanel();
        panelPedidosNOregistrados = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaPedidosEnPreparacion = new javax.swing.JTable();
        panelNumeroComanda = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        lblComanda = new javax.swing.JLabel();
        tabbedMenuCobrar = new javax.swing.JTabbedPane();
        PanelSubMenuProductos = new javax.swing.JPanel();
        subMenu = new javax.swing.JTabbedPane();
        panelBebidasFrias = new javax.swing.JPanel();
        btnFrappucino = new javax.swing.JButton();
        btnFrappeMoka = new javax.swing.JButton();
        btnPersonales = new javax.swing.JButton();
        btnJugoFrutillaCONAGUA = new javax.swing.JButton();
        btnJugoPlatanoCONAGUA = new javax.swing.JButton();
        btnJugoPapayaCONAGUA = new javax.swing.JButton();
        btnFrapuccinoOreo = new javax.swing.JButton();
        btnJugoPapayaCONLECHE = new javax.swing.JButton();
        btnJugoPlatanoCONLECHE = new javax.swing.JButton();
        btnJugoFrutillaCONLECHE = new javax.swing.JButton();
        btnAffogato = new javax.swing.JButton();
        btnCappuccinoFrio = new javax.swing.JButton();
        btnJugoDurazPapaAgua = new javax.swing.JButton();
        btnJugoDurazPapaLeche = new javax.swing.JButton();
        btnFrappeBaileys = new javax.swing.JButton();
        btnJugoMixAgua = new javax.swing.JButton();
        btnJugoMixLeche = new javax.swing.JButton();
        btnNutelatteFrio = new javax.swing.JButton();
        panelBebidasCalientes = new javax.swing.JPanel();
        btnLatte = new javax.swing.JButton();
        btnAmericano = new javax.swing.JButton();
        btnXpresso = new javax.swing.JButton();
        btnTe = new javax.swing.JButton();
        btnLeche = new javax.swing.JButton();
        btnChocolate = new javax.swing.JButton();
        btnCapuccinoVainilla = new javax.swing.JButton();
        btnCapuccinoSimple = new javax.swing.JButton();
        btnSubmarinoBonBom = new javax.swing.JButton();
        btnSubmarinoOreo = new javax.swing.JButton();
        btnMochaccino = new javax.swing.JButton();
        btnCafeIrlandes = new javax.swing.JButton();
        btnCapuccinoCompleto = new javax.swing.JButton();
        btnSubmarino = new javax.swing.JButton();
        btnChaiLate = new javax.swing.JButton();
        btnCafeBomBom = new javax.swing.JButton();
        btnToddy = new javax.swing.JButton();
        btnChocolateLeche = new javax.swing.JButton();
        btnChocolateMavabisco = new javax.swing.JButton();
        btnNuteLatte = new javax.swing.JButton();
        btnTrimate = new javax.swing.JButton();
        btnCapuccinoBaylies = new javax.swing.JButton();
        btnMateAnis = new javax.swing.JButton();
        btnMateCoca = new javax.swing.JButton();
        btnMateManzanilla = new javax.swing.JButton();
        panelMasitas = new javax.swing.JPanel();
        btnAlfajor = new javax.swing.JButton();
        btnRolloPequeño = new javax.swing.JButton();
        btnPieDuraznoUnidad = new javax.swing.JButton();
        btnHojaldre = new javax.swing.JButton();
        btnPastel = new javax.swing.JButton();
        btnQuequeNaranja = new javax.swing.JButton();
        btnCupcakeChocolate = new javax.swing.JButton();
        btnGalleta12uChocolate = new javax.swing.JButton();
        btnYemas = new javax.swing.JButton();
        btnDona = new javax.swing.JButton();
        btnRolloGrande = new javax.swing.JButton();
        btnPieManzana = new javax.swing.JButton();
        btnPieFrutillaUnidad = new javax.swing.JButton();
        btnCroissant = new javax.swing.JButton();
        btnEmpanada = new javax.swing.JButton();
        btnGalleta12UNaranja = new javax.swing.JButton();
        btnPastel3Leches = new javax.swing.JButton();
        btnPastelSelvaNegra = new javax.swing.JButton();
        btnPorcionItaly = new javax.swing.JButton();
        btnCupcakeFrutilla = new javax.swing.JButton();
        btnCupcakeVainilla = new javax.swing.JButton();
        btnGalletas12uCoco = new javax.swing.JButton();
        btnGalletas12uVainilla = new javax.swing.JButton();
        btnPieDuraznoEntero = new javax.swing.JButton();
        btnPieFrutillaEntero = new javax.swing.JButton();
        panelTortas = new javax.swing.JPanel();
        btnEspecialSelvaNegra = new javax.swing.JButton();
        btnTradicionalPequeño = new javax.swing.JButton();
        btnPequeñoChocolate = new javax.swing.JButton();
        btnTradicionalMediano = new javax.swing.JButton();
        btnTradicionalGrande = new javax.swing.JButton();
        btnEspecialMocka = new javax.swing.JButton();
        btnMedianoChocolate = new javax.swing.JButton();
        btnMedianoJalea = new javax.swing.JButton();
        btnFotoTorta = new javax.swing.JButton();
        btnEspecialTresLeches = new javax.swing.JButton();
        panelHeladería = new javax.swing.JPanel();
        btnMalteadaFresa = new javax.swing.JButton();
        btnMalteadaChocolate = new javax.swing.JButton();
        btnMalteadaVainilla = new javax.swing.JButton();
        btnCopaBomBom = new javax.swing.JButton();
        btnCopaChipsAhoy = new javax.swing.JButton();
        btnCopaOreo = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnTorta50p2p = new javax.swing.JButton();
        btnTorta100p5p = new javax.swing.JButton();
        btnTorta150p6p = new javax.swing.JButton();
        btnTorta300p15p = new javax.swing.JButton();
        btnTorta500p25p = new javax.swing.JButton();
        btnFotoTortaPequeña = new javax.swing.JButton();
        btnFotoTortaGrande = new javax.swing.JButton();
        btnTorta50pCuadrado = new javax.swing.JButton();
        btnTorta100pCuadrado = new javax.swing.JButton();
        btnTorta200p10p = new javax.swing.JButton();
        btnTorta400p22p = new javax.swing.JButton();
        btnFotoTortaMediana = new javax.swing.JButton();
        panelCobrarCliente = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtTotalCobrarBs = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaCobroConsumo = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtCarnet = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtWhatsapp = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        txtEfectivo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCambio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtConsumosCliente = new javax.swing.JTextField();
        btnGuardaNuevoCliente = new javax.swing.JButton();
        panelTipoPago = new javax.swing.JPanel();
        radioTipoEfectivo = new javax.swing.JRadioButton();
        radioTipoTarjeta = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtComanda = new javax.swing.JTextPane();
        btnImprimir = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtTotalBs = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        submenuComprasGastos = new javax.swing.JMenuItem();
        menuReportes = new javax.swing.JMenu();
        menuTopTen = new javax.swing.JMenu();
        submenuTopClientes = new javax.swing.JMenuItem();
        submenuTopProductos = new javax.swing.JMenuItem();
        menuVentas = new javax.swing.JMenu();
        subMenuVentasDiarias = new javax.swing.JMenuItem();
        subMenuVentasUnDia = new javax.swing.JMenuItem();
        menuCompras = new javax.swing.JMenuItem();
        menuCierreTurno = new javax.swing.JMenu();
        menuSalir = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("VENTAS");

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));

        panelTipo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelTipo.setForeground(new java.awt.Color(0, 255, 0));

        lblMesa.setBackground(new java.awt.Color(255, 204, 0));
        lblMesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_Ventas/mesa.png"))); // NOI18N
        lblMesa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblMesa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblMesa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMesaMouseClicked(evt);
            }
        });

        lblDelivery.setBackground(new java.awt.Color(204, 204, 255));
        lblDelivery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_Ventas/repartidor.png"))); // NOI18N
        lblDelivery.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblDelivery.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblDelivery.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDeliveryMouseClicked(evt);
            }
        });

        lblMESA.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMESA.setText("MESA");

        lblLlevar.setBackground(new java.awt.Color(0, 204, 255));
        lblLlevar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_Ventas/llevar.png"))); // NOI18N
        lblLlevar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblLlevar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblLlevar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLlevarMouseClicked(evt);
            }
        });

        lblLLEVAR.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblLLEVAR.setText("LLEVAR");

        lblDELIVERY.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDELIVERY.setText("DELIVERY");

        javax.swing.GroupLayout panelTipoLayout = new javax.swing.GroupLayout(panelTipo);
        panelTipo.setLayout(panelTipoLayout);
        panelTipoLayout.setHorizontalGroup(
            panelTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTipoLayout.createSequentialGroup()
                .addGroup(panelTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTipoLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(lblMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelTipoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblMESA, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTipoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblLlevar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTipoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblLLEVAR, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(panelTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDELIVERY)
                    .addComponent(lblDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panelTipoLayout.setVerticalGroup(
            panelTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTipoLayout.createSequentialGroup()
                .addGroup(panelTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblLlevar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMESA)
                    .addComponent(lblLLEVAR)
                    .addComponent(lblDELIVERY))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaPedido.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tablaPedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Cantidad", "Descripción", "P/U Bs.", "Subtotal Bs."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaPedido.setRowHeight(24);
        tablaPedido.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablaPedido.setShowGrid(true);
        tablaPedido.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tablaPedidoAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tablaPedido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tablaPedidoMouseReleased(evt);
            }
        });
        tablaPedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaPedidoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tablaPedidoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tablaPedidoKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tablaPedido);

        panelBotones.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ACCIONES PARA EL PEDIDO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 10))); // NOI18N

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_Ventas/btnLimpiar.png"))); // NOI18N
        btnLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnPreparar.setBackground(new java.awt.Color(255, 204, 153));
        btnPreparar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_Ventas/btnOrdenar.png"))); // NOI18N
        btnPreparar.setToolTipText("Ordenar Pedido");
        btnPreparar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPreparar.setMargin(new java.awt.Insets(0, 14, 0, 14));
        btnPreparar.setName(""); // NOI18N
        btnPreparar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPrepararMouseEntered(evt);
            }
        });
        btnPreparar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrepararActionPerformed(evt);
            }
        });

        btnRegistrar.setBackground(new java.awt.Color(51, 211, 0));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_Ventas/btnRegistrar.png"))); // NOI18N
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        lblMESA1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMESA1.setText("PREPARAR");

        lblMESA2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMESA2.setText("LIMPIAR");

        lblMESA4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMESA4.setText("REGISTRAR");

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBotonesLayout.createSequentialGroup()
                        .addComponent(btnPreparar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBotonesLayout.createSequentialGroup()
                        .addComponent(lblMESA1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBotonesLayout.createSequentialGroup()
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBotonesLayout.createSequentialGroup()
                        .addComponent(lblMESA2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMESA4)))
                .addContainerGap())
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnPreparar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMESA2)
                    .addComponent(lblMESA4)
                    .addComponent(lblMESA1)))
        );

        btnPreparar.getAccessibleContext().setAccessibleDescription("ORDENAR");

        panelPago.setBackground(new java.awt.Color(204, 255, 204));

        panelPedidosNOregistrados.setBackground(new java.awt.Color(204, 255, 255));
        panelPedidosNOregistrados.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PEDIDOS EN PREPARACIÓN:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        tablaPedidosEnPreparacion.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tablaPedidosEnPreparacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Comanda", "Descripción", "Total Bs."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaPedidosEnPreparacion.setToolTipText("NC: Numero de Comanda, Cant: Cantidad, Descripcion, SubTbs: Subtotal Bs., TotalBs: Total del pedido");
        tablaPedidosEnPreparacion.setRowHeight(24);
        tablaPedidosEnPreparacion.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tablaPedidosEnPreparacion.setShowGrid(true);
        tablaPedidosEnPreparacion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaPedidosEnPreparacionMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaPedidosEnPreparacion);

        javax.swing.GroupLayout panelPedidosNOregistradosLayout = new javax.swing.GroupLayout(panelPedidosNOregistrados);
        panelPedidosNOregistrados.setLayout(panelPedidosNOregistradosLayout);
        panelPedidosNOregistradosLayout.setHorizontalGroup(
            panelPedidosNOregistradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPedidosNOregistradosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
        );
        panelPedidosNOregistradosLayout.setVerticalGroup(
            panelPedidosNOregistradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPedidosNOregistradosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelPagoLayout = new javax.swing.GroupLayout(panelPago);
        panelPago.setLayout(panelPagoLayout);
        panelPagoLayout.setHorizontalGroup(
            panelPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelPagoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelPedidosNOregistrados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelPagoLayout.setVerticalGroup(
            panelPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPagoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelPedidosNOregistrados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelPedidosNOregistrados.getAccessibleContext().setAccessibleName("PEDIDOS EN PREPARACION:");

        panelNumeroComanda.setBackground(new java.awt.Color(0, 0, 0));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("COMANDA N°");

        lblComanda.setFont(new java.awt.Font("Arial", 0, 40)); // NOI18N
        lblComanda.setForeground(new java.awt.Color(255, 255, 255));
        lblComanda.setText("100");

        javax.swing.GroupLayout panelNumeroComandaLayout = new javax.swing.GroupLayout(panelNumeroComanda);
        panelNumeroComanda.setLayout(panelNumeroComandaLayout);
        panelNumeroComandaLayout.setHorizontalGroup(
            panelNumeroComandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumeroComandaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNumeroComandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(lblComanda))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelNumeroComandaLayout.setVerticalGroup(
            panelNumeroComandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNumeroComandaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblComanda)
                .addContainerGap())
        );

        tabbedMenuCobrar.setBackground(new java.awt.Color(255, 204, 204));
        tabbedMenuCobrar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        tabbedMenuCobrar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        PanelSubMenuProductos.setBackground(new java.awt.Color(204, 255, 204));
        PanelSubMenuProductos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        subMenu.setBackground(new java.awt.Color(204, 204, 255));
        subMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 83, 202)));
        subMenu.setFont(new java.awt.Font("Arial Narrow", 1, 14)); // NOI18N

        panelBebidasFrias.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnFrappucino.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnFrappucino.setText("FRAPPUCCINO");
        btnFrappucino.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFrappucinoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFrappucinoMouseExited(evt);
            }
        });
        btnFrappucino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrappucinoActionPerformed(evt);
            }
        });

        btnFrappeMoka.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnFrappeMoka.setText("FRAPPE DE MOKA");
        btnFrappeMoka.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFrappeMokaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFrappeMokaMouseExited(evt);
            }
        });
        btnFrappeMoka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrappeMokaActionPerformed(evt);
            }
        });

        btnPersonales.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnPersonales.setText("PERSONALES");
        btnPersonales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPersonalesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPersonalesMouseExited(evt);
            }
        });
        btnPersonales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPersonalesActionPerformed(evt);
            }
        });

        btnJugoFrutillaCONAGUA.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnJugoFrutillaCONAGUA.setText("JUGO FRUTILLA CON AGUA");
        btnJugoFrutillaCONAGUA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnJugoFrutillaCONAGUAMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnJugoFrutillaCONAGUAMouseExited(evt);
            }
        });
        btnJugoFrutillaCONAGUA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugoFrutillaCONAGUAActionPerformed(evt);
            }
        });

        btnJugoPlatanoCONAGUA.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnJugoPlatanoCONAGUA.setText("JUGO PLÁTANO CON AGUA");
        btnJugoPlatanoCONAGUA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnJugoPlatanoCONAGUAMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnJugoPlatanoCONAGUAMouseExited(evt);
            }
        });
        btnJugoPlatanoCONAGUA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugoPlatanoCONAGUAActionPerformed(evt);
            }
        });

        btnJugoPapayaCONAGUA.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnJugoPapayaCONAGUA.setText("JUGO PAPAYA CON AGUA");
        btnJugoPapayaCONAGUA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnJugoPapayaCONAGUAMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnJugoPapayaCONAGUAMouseExited(evt);
            }
        });
        btnJugoPapayaCONAGUA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugoPapayaCONAGUAActionPerformed(evt);
            }
        });

        btnFrapuccinoOreo.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnFrapuccinoOreo.setText("FRAPPUCCINO OREO");
        btnFrapuccinoOreo.setToolTipText("");
        btnFrapuccinoOreo.setName(""); // NOI18N
        btnFrapuccinoOreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFrapuccinoOreoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFrapuccinoOreoMouseExited(evt);
            }
        });
        btnFrapuccinoOreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrapuccinoOreoActionPerformed(evt);
            }
        });

        btnJugoPapayaCONLECHE.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnJugoPapayaCONLECHE.setText("JUGO PAPAYA CON LECHE");
        btnJugoPapayaCONLECHE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnJugoPapayaCONLECHEMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnJugoPapayaCONLECHEMouseExited(evt);
            }
        });
        btnJugoPapayaCONLECHE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugoPapayaCONLECHEActionPerformed(evt);
            }
        });

        btnJugoPlatanoCONLECHE.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnJugoPlatanoCONLECHE.setText("JUGO PLÁTANO CON LECHE");
        btnJugoPlatanoCONLECHE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnJugoPlatanoCONLECHEMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnJugoPlatanoCONLECHEMouseExited(evt);
            }
        });
        btnJugoPlatanoCONLECHE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugoPlatanoCONLECHEActionPerformed(evt);
            }
        });

        btnJugoFrutillaCONLECHE.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnJugoFrutillaCONLECHE.setText("JUGO FRUTILLA CON LECHE");
        btnJugoFrutillaCONLECHE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnJugoFrutillaCONLECHEMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnJugoFrutillaCONLECHEMouseExited(evt);
            }
        });
        btnJugoFrutillaCONLECHE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugoFrutillaCONLECHEActionPerformed(evt);
            }
        });

        btnAffogato.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnAffogato.setText("AFFOGATO");
        btnAffogato.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAffogatoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAffogatoMouseExited(evt);
            }
        });
        btnAffogato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAffogatoActionPerformed(evt);
            }
        });

        btnCappuccinoFrio.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCappuccinoFrio.setText("CAPPUCCINO FRÍO");
        btnCappuccinoFrio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCappuccinoFrioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCappuccinoFrioMouseExited(evt);
            }
        });
        btnCappuccinoFrio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCappuccinoFrioActionPerformed(evt);
            }
        });

        btnJugoDurazPapaAgua.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnJugoDurazPapaAgua.setText("JUGO DE DURAZNO CON PAPAYA CON AGUA");
        btnJugoDurazPapaAgua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnJugoDurazPapaAguaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnJugoDurazPapaAguaMouseExited(evt);
            }
        });
        btnJugoDurazPapaAgua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugoDurazPapaAguaActionPerformed(evt);
            }
        });

        btnJugoDurazPapaLeche.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnJugoDurazPapaLeche.setText("JUGO DE DURAZNO CON PAPAYA CON LECHE");
        btnJugoDurazPapaLeche.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnJugoDurazPapaLecheMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnJugoDurazPapaLecheMouseExited(evt);
            }
        });
        btnJugoDurazPapaLeche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugoDurazPapaLecheActionPerformed(evt);
            }
        });

        btnFrappeBaileys.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnFrappeBaileys.setText("FRAPPE BAILEYS");
        btnFrappeBaileys.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFrappeBaileysMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFrappeBaileysMouseExited(evt);
            }
        });
        btnFrappeBaileys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrappeBaileysActionPerformed(evt);
            }
        });

        btnJugoMixAgua.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnJugoMixAgua.setText("JUGO MIX CON AGUA");
        btnJugoMixAgua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnJugoMixAguaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnJugoMixAguaMouseExited(evt);
            }
        });
        btnJugoMixAgua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugoMixAguaActionPerformed(evt);
            }
        });

        btnJugoMixLeche.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnJugoMixLeche.setText("JUGO MIX CON LECHE");
        btnJugoMixLeche.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnJugoMixLecheMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnJugoMixLecheMouseExited(evt);
            }
        });
        btnJugoMixLeche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugoMixLecheActionPerformed(evt);
            }
        });

        btnNutelatteFrio.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnNutelatteFrio.setText("NUTELATTE FRÍO");
        btnNutelatteFrio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNutelatteFrioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNutelatteFrioMouseExited(evt);
            }
        });
        btnNutelatteFrio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNutelatteFrioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBebidasFriasLayout = new javax.swing.GroupLayout(panelBebidasFrias);
        panelBebidasFrias.setLayout(panelBebidasFriasLayout);
        panelBebidasFriasLayout.setHorizontalGroup(
            panelBebidasFriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBebidasFriasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBebidasFriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBebidasFriasLayout.createSequentialGroup()
                        .addComponent(btnAffogato)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCappuccinoFrio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFrappeBaileys)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFrappeMoka))
                    .addGroup(panelBebidasFriasLayout.createSequentialGroup()
                        .addComponent(btnFrappucino)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFrapuccinoOreo))
                    .addGroup(panelBebidasFriasLayout.createSequentialGroup()
                        .addComponent(btnJugoMixLeche)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNutelatteFrio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPersonales))
                    .addComponent(btnJugoDurazPapaAgua)
                    .addGroup(panelBebidasFriasLayout.createSequentialGroup()
                        .addComponent(btnJugoDurazPapaLeche)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnJugoFrutillaCONAGUA))
                    .addGroup(panelBebidasFriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBebidasFriasLayout.createSequentialGroup()
                            .addComponent(btnJugoPapayaCONLECHE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnJugoPlatanoCONAGUA))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBebidasFriasLayout.createSequentialGroup()
                            .addComponent(btnJugoFrutillaCONLECHE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnJugoPapayaCONAGUA)))
                    .addGroup(panelBebidasFriasLayout.createSequentialGroup()
                        .addComponent(btnJugoPlatanoCONLECHE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnJugoMixAgua)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBebidasFriasLayout.setVerticalGroup(
            panelBebidasFriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBebidasFriasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBebidasFriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAffogato)
                    .addComponent(btnCappuccinoFrio)
                    .addComponent(btnFrappeBaileys)
                    .addComponent(btnFrappeMoka))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBebidasFriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFrappucino)
                    .addComponent(btnFrapuccinoOreo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnJugoDurazPapaAgua)
                .addGap(18, 18, 18)
                .addGroup(panelBebidasFriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnJugoDurazPapaLeche)
                    .addComponent(btnJugoFrutillaCONAGUA))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBebidasFriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnJugoFrutillaCONLECHE)
                    .addComponent(btnJugoPapayaCONAGUA))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBebidasFriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnJugoPapayaCONLECHE)
                    .addComponent(btnJugoPlatanoCONAGUA))
                .addGap(18, 18, 18)
                .addGroup(panelBebidasFriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnJugoPlatanoCONLECHE)
                    .addComponent(btnJugoMixAgua))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBebidasFriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnJugoMixLeche)
                    .addComponent(btnNutelatteFrio)
                    .addComponent(btnPersonales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(88, Short.MAX_VALUE))
        );

        subMenu.addTab("BEBIDAS FRÍAS", panelBebidasFrias);

        panelBebidasCalientes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnLatte.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnLatte.setText("LATTE");
        btnLatte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLatte.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLatte.setMinimumSize(null);
        btnLatte.setName(""); // NOI18N
        btnLatte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLatteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLatteMouseExited(evt);
            }
        });
        btnLatte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLatteActionPerformed(evt);
            }
        });

        btnAmericano.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnAmericano.setText("CAFÉ AMERICANO");
        btnAmericano.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAmericano.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAmericanoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAmericanoMouseExited(evt);
            }
        });
        btnAmericano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAmericanoActionPerformed(evt);
            }
        });

        btnXpresso.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnXpresso.setText("CAFÉ EXPRESO");
        btnXpresso.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXpresso.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnXpresso.setName(""); // NOI18N
        btnXpresso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnXpressoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnXpressoMouseExited(evt);
            }
        });
        btnXpresso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXpressoActionPerformed(evt);
            }
        });

        btnTe.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTe.setText("TÉ");
        btnTe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTe.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTe.setMinimumSize(null);
        btnTe.setName(""); // NOI18N
        btnTe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTeMouseExited(evt);
            }
        });
        btnTe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTeActionPerformed(evt);
            }
        });

        btnLeche.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnLeche.setText("LECHE");
        btnLeche.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLeche.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLeche.setMinimumSize(null);
        btnLeche.setName(""); // NOI18N
        btnLeche.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLecheMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLecheMouseExited(evt);
            }
        });
        btnLeche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLecheActionPerformed(evt);
            }
        });

        btnChocolate.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnChocolate.setText("CHOCOLATE");
        btnChocolate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChocolate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChocolate.setName(""); // NOI18N
        btnChocolate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnChocolateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnChocolateMouseExited(evt);
            }
        });
        btnChocolate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChocolateActionPerformed(evt);
            }
        });

        btnCapuccinoVainilla.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCapuccinoVainilla.setText("CAPPUCCINO DE VAINILLA");
        btnCapuccinoVainilla.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapuccinoVainilla.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCapuccinoVainilla.setName(""); // NOI18N
        btnCapuccinoVainilla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCapuccinoVainillaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCapuccinoVainillaMouseExited(evt);
            }
        });
        btnCapuccinoVainilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapuccinoVainillaActionPerformed(evt);
            }
        });

        btnCapuccinoSimple.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCapuccinoSimple.setText("CAPPUCCINO SIMPLE");
        btnCapuccinoSimple.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapuccinoSimple.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCapuccinoSimple.setName(""); // NOI18N
        btnCapuccinoSimple.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCapuccinoSimpleMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCapuccinoSimpleMouseExited(evt);
            }
        });
        btnCapuccinoSimple.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapuccinoSimpleActionPerformed(evt);
            }
        });

        btnSubmarinoBonBom.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnSubmarinoBonBom.setText("SUBMARINO DE BON O BOM");
        btnSubmarinoBonBom.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSubmarinoBonBom.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSubmarinoBonBom.setName(""); // NOI18N
        btnSubmarinoBonBom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSubmarinoBonBomMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSubmarinoBonBomMouseExited(evt);
            }
        });
        btnSubmarinoBonBom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmarinoBonBomActionPerformed(evt);
            }
        });

        btnSubmarinoOreo.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnSubmarinoOreo.setText("SUBMARINO DE OREO");
        btnSubmarinoOreo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSubmarinoOreo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSubmarinoOreo.setName(""); // NOI18N
        btnSubmarinoOreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSubmarinoOreoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSubmarinoOreoMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnSubmarinoOreoMousePressed(evt);
            }
        });
        btnSubmarinoOreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmarinoOreoActionPerformed(evt);
            }
        });

        btnMochaccino.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnMochaccino.setText("MOCACCINO");
        btnMochaccino.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMochaccino.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMochaccino.setName(""); // NOI18N
        btnMochaccino.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMochaccinoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMochaccinoMouseExited(evt);
            }
        });
        btnMochaccino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMochaccinoActionPerformed(evt);
            }
        });

        btnCafeIrlandes.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCafeIrlandes.setText("CAFÉ IRLANDES");
        btnCafeIrlandes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCafeIrlandes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCafeIrlandes.setName(""); // NOI18N
        btnCafeIrlandes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCafeIrlandesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCafeIrlandesMouseExited(evt);
            }
        });
        btnCafeIrlandes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCafeIrlandesActionPerformed(evt);
            }
        });

        btnCapuccinoCompleto.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCapuccinoCompleto.setText("CAPPUCCINO COMPLETO");
        btnCapuccinoCompleto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapuccinoCompleto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCapuccinoCompleto.setName(""); // NOI18N
        btnCapuccinoCompleto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCapuccinoCompletoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCapuccinoCompletoMouseExited(evt);
            }
        });
        btnCapuccinoCompleto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapuccinoCompletoActionPerformed(evt);
            }
        });

        btnSubmarino.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnSubmarino.setText("SUBMARINO");
        btnSubmarino.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSubmarino.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSubmarino.setName(""); // NOI18N
        btnSubmarino.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSubmarinoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSubmarinoMouseExited(evt);
            }
        });
        btnSubmarino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmarinoActionPerformed(evt);
            }
        });

        btnChaiLate.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnChaiLate.setText("CHAI LATTE");
        btnChaiLate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChaiLate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChaiLate.setName(""); // NOI18N
        btnChaiLate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnChaiLateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnChaiLateMouseExited(evt);
            }
        });
        btnChaiLate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChaiLateActionPerformed(evt);
            }
        });

        btnCafeBomBom.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCafeBomBom.setText("CAFÉ BOM BOM");
        btnCafeBomBom.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCafeBomBom.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCafeBomBom.setName(""); // NOI18N
        btnCafeBomBom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCafeBomBomMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCafeBomBomMouseExited(evt);
            }
        });
        btnCafeBomBom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCafeBomBomActionPerformed(evt);
            }
        });

        btnToddy.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnToddy.setText("TODDY");
        btnToddy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnToddy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToddy.setName(""); // NOI18N
        btnToddy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnToddyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnToddyMouseExited(evt);
            }
        });
        btnToddy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToddyActionPerformed(evt);
            }
        });

        btnChocolateLeche.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnChocolateLeche.setText("CHOCOLATE CON LECHE");
        btnChocolateLeche.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChocolateLeche.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChocolateLeche.setName(""); // NOI18N
        btnChocolateLeche.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnChocolateLecheMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnChocolateLecheMouseExited(evt);
            }
        });
        btnChocolateLeche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChocolateLecheActionPerformed(evt);
            }
        });

        btnChocolateMavabisco.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnChocolateMavabisco.setText("CHOCOLATE CON MALVABISCO");
        btnChocolateMavabisco.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChocolateMavabisco.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChocolateMavabisco.setName(""); // NOI18N
        btnChocolateMavabisco.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnChocolateMavabiscoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnChocolateMavabiscoMouseExited(evt);
            }
        });
        btnChocolateMavabisco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChocolateMavabiscoActionPerformed(evt);
            }
        });

        btnNuteLatte.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnNuteLatte.setText("NUTE LATTE");
        btnNuteLatte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuteLatte.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuteLatte.setName(""); // NOI18N
        btnNuteLatte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNuteLatteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNuteLatteMouseExited(evt);
            }
        });
        btnNuteLatte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuteLatteActionPerformed(evt);
            }
        });

        btnTrimate.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTrimate.setText("TRIMATE");
        btnTrimate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTrimate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTrimate.setName(""); // NOI18N
        btnTrimate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTrimateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTrimateMouseExited(evt);
            }
        });
        btnTrimate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrimateActionPerformed(evt);
            }
        });

        btnCapuccinoBaylies.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCapuccinoBaylies.setText("CAPPUCCINO CON BAYLIES");
        btnCapuccinoBaylies.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapuccinoBaylies.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCapuccinoBaylies.setName(""); // NOI18N
        btnCapuccinoBaylies.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCapuccinoBayliesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCapuccinoBayliesMouseExited(evt);
            }
        });
        btnCapuccinoBaylies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapuccinoBayliesActionPerformed(evt);
            }
        });

        btnMateAnis.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnMateAnis.setText("MATE ANIS");
        btnMateAnis.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMateAnis.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMateAnis.setName(""); // NOI18N
        btnMateAnis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMateAnisMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMateAnisMouseExited(evt);
            }
        });
        btnMateAnis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMateAnisActionPerformed(evt);
            }
        });

        btnMateCoca.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnMateCoca.setText("MATE COCA");
        btnMateCoca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMateCoca.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMateCoca.setName(""); // NOI18N
        btnMateCoca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMateCocaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMateCocaMouseExited(evt);
            }
        });
        btnMateCoca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMateCocaActionPerformed(evt);
            }
        });

        btnMateManzanilla.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnMateManzanilla.setText("MATE MANZANILLA");
        btnMateManzanilla.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMateManzanilla.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMateManzanilla.setName(""); // NOI18N
        btnMateManzanilla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMateManzanillaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMateManzanillaMouseExited(evt);
            }
        });
        btnMateManzanilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMateManzanillaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBebidasCalientesLayout = new javax.swing.GroupLayout(panelBebidasCalientes);
        panelBebidasCalientes.setLayout(panelBebidasCalientesLayout);
        panelBebidasCalientesLayout.setHorizontalGroup(
            panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBebidasCalientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBebidasCalientesLayout.createSequentialGroup()
                        .addComponent(btnAmericano)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCafeBomBom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXpresso)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelBebidasCalientesLayout.createSequentialGroup()
                        .addGroup(panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBebidasCalientesLayout.createSequentialGroup()
                                .addComponent(btnChaiLate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnChocolate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnChocolateMavabisco))
                            .addGroup(panelBebidasCalientesLayout.createSequentialGroup()
                                .addComponent(btnChocolateLeche)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLatte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLeche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnMateAnis))
                            .addGroup(panelBebidasCalientesLayout.createSequentialGroup()
                                .addComponent(btnCafeIrlandes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCapuccinoBaylies)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCapuccinoCompleto))
                            .addGroup(panelBebidasCalientesLayout.createSequentialGroup()
                                .addComponent(btnCapuccinoVainilla)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCapuccinoSimple))
                            .addGroup(panelBebidasCalientesLayout.createSequentialGroup()
                                .addComponent(btnMateCoca)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnMateManzanilla)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnMochaccino)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNuteLatte))
                            .addGroup(panelBebidasCalientesLayout.createSequentialGroup()
                                .addComponent(btnSubmarino)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSubmarinoBonBom)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSubmarinoOreo))
                            .addGroup(panelBebidasCalientesLayout.createSequentialGroup()
                                .addComponent(btnTe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnToddy)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTrimate)))
                        .addGap(0, 21, Short.MAX_VALUE))))
        );
        panelBebidasCalientesLayout.setVerticalGroup(
            panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBebidasCalientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCafeBomBom)
                    .addGroup(panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAmericano)
                        .addComponent(btnXpresso)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCafeIrlandes)
                    .addComponent(btnCapuccinoBaylies)
                    .addComponent(btnCapuccinoCompleto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCapuccinoVainilla)
                    .addComponent(btnCapuccinoSimple))
                .addGap(18, 18, 18)
                .addGroup(panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChaiLate)
                    .addComponent(btnChocolate)
                    .addComponent(btnChocolateMavabisco))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChocolateLeche)
                    .addComponent(btnLatte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLeche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMateAnis))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMateCoca)
                    .addComponent(btnMateManzanilla)
                    .addComponent(btnMochaccino)
                    .addComponent(btnNuteLatte))
                .addGap(18, 18, 18)
                .addGroup(panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubmarino)
                    .addComponent(btnSubmarinoBonBom)
                    .addComponent(btnSubmarinoOreo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBebidasCalientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnToddy)
                    .addComponent(btnTrimate))
                .addContainerGap(88, Short.MAX_VALUE))
        );

        subMenu.addTab("BEBIDAS CALIENTES", panelBebidasCalientes);

        panelMasitas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnAlfajor.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnAlfajor.setText("ALFAJOR");
        btnAlfajor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAlfajorMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAlfajorMouseExited(evt);
            }
        });
        btnAlfajor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlfajorActionPerformed(evt);
            }
        });

        btnRolloPequeño.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnRolloPequeño.setText("ROLLO PEQUEÑO");
        btnRolloPequeño.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRolloPequeñoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRolloPequeñoMouseExited(evt);
            }
        });
        btnRolloPequeño.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRolloPequeñoActionPerformed(evt);
            }
        });

        btnPieDuraznoUnidad.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnPieDuraznoUnidad.setText("PIE DE DURAZNO UNIDAD");
        btnPieDuraznoUnidad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPieDuraznoUnidadMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPieDuraznoUnidadMouseExited(evt);
            }
        });
        btnPieDuraznoUnidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPieDuraznoUnidadActionPerformed(evt);
            }
        });

        btnHojaldre.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnHojaldre.setText("HOJALDRE");
        btnHojaldre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHojaldreMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHojaldreMouseExited(evt);
            }
        });
        btnHojaldre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHojaldreActionPerformed(evt);
            }
        });

        btnPastel.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnPastel.setText("PASTEL");
        btnPastel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPastelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPastelMouseExited(evt);
            }
        });
        btnPastel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPastelActionPerformed(evt);
            }
        });

        btnQuequeNaranja.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnQuequeNaranja.setText("QUEQUE DE NARANJA");
        btnQuequeNaranja.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnQuequeNaranjaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnQuequeNaranjaMouseExited(evt);
            }
        });
        btnQuequeNaranja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuequeNaranjaActionPerformed(evt);
            }
        });

        btnCupcakeChocolate.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCupcakeChocolate.setText("CUPCAKE CHOCOLATE");
        btnCupcakeChocolate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCupcakeChocolateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCupcakeChocolateMouseExited(evt);
            }
        });
        btnCupcakeChocolate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCupcakeChocolateActionPerformed(evt);
            }
        });

        btnGalleta12uChocolate.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnGalleta12uChocolate.setText("GALLETAS 12U. CHOCOLATE");
        btnGalleta12uChocolate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGalleta12uChocolateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGalleta12uChocolateMouseExited(evt);
            }
        });
        btnGalleta12uChocolate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGalleta12uChocolateActionPerformed(evt);
            }
        });

        btnYemas.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnYemas.setText("YEMAS");
        btnYemas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnYemasMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnYemasMouseExited(evt);
            }
        });
        btnYemas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYemasActionPerformed(evt);
            }
        });

        btnDona.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnDona.setText("DONA");
        btnDona.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDonaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDonaMouseExited(evt);
            }
        });
        btnDona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDonaActionPerformed(evt);
            }
        });

        btnRolloGrande.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnRolloGrande.setText("ROLLO GRANDE");
        btnRolloGrande.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRolloGrandeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRolloGrandeMouseExited(evt);
            }
        });
        btnRolloGrande.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRolloGrandeActionPerformed(evt);
            }
        });

        btnPieManzana.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnPieManzana.setText("PIE DE MANZANA");
        btnPieManzana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPieManzanaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPieManzanaMouseExited(evt);
            }
        });
        btnPieManzana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPieManzanaActionPerformed(evt);
            }
        });

        btnPieFrutillaUnidad.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnPieFrutillaUnidad.setText("PIE DE FRUTILLA UNIDAD");
        btnPieFrutillaUnidad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPieFrutillaUnidadMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPieFrutillaUnidadMouseExited(evt);
            }
        });
        btnPieFrutillaUnidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPieFrutillaUnidadActionPerformed(evt);
            }
        });

        btnCroissant.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCroissant.setText("CROISSANT");
        btnCroissant.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCroissantMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCroissantMouseExited(evt);
            }
        });
        btnCroissant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCroissantActionPerformed(evt);
            }
        });

        btnEmpanada.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnEmpanada.setText("EMPANADA");
        btnEmpanada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEmpanadaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEmpanadaMouseExited(evt);
            }
        });
        btnEmpanada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpanadaActionPerformed(evt);
            }
        });

        btnGalleta12UNaranja.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnGalleta12UNaranja.setText("GALLETAS 12U. NARANJA");
        btnGalleta12UNaranja.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGalleta12UNaranjaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGalleta12UNaranjaMouseExited(evt);
            }
        });
        btnGalleta12UNaranja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGalleta12UNaranjaActionPerformed(evt);
            }
        });

        btnPastel3Leches.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnPastel3Leches.setText("PASTEL 3 LECHES");
        btnPastel3Leches.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPastel3LechesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPastel3LechesMouseExited(evt);
            }
        });
        btnPastel3Leches.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPastel3LechesActionPerformed(evt);
            }
        });

        btnPastelSelvaNegra.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnPastelSelvaNegra.setText("PASTEL SELVA NEGRA");
        btnPastelSelvaNegra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPastelSelvaNegraMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPastelSelvaNegraMouseExited(evt);
            }
        });
        btnPastelSelvaNegra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPastelSelvaNegraActionPerformed(evt);
            }
        });

        btnPorcionItaly.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnPorcionItaly.setText("PORCION ITALY");
        btnPorcionItaly.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPorcionItalyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPorcionItalyMouseExited(evt);
            }
        });
        btnPorcionItaly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPorcionItalyActionPerformed(evt);
            }
        });

        btnCupcakeFrutilla.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCupcakeFrutilla.setText("CUPCAKE FRUTILLA");
        btnCupcakeFrutilla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCupcakeFrutillaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCupcakeFrutillaMouseExited(evt);
            }
        });
        btnCupcakeFrutilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCupcakeFrutillaActionPerformed(evt);
            }
        });

        btnCupcakeVainilla.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCupcakeVainilla.setText("CUPCAKE VAINILLA");
        btnCupcakeVainilla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCupcakeVainillaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCupcakeVainillaMouseExited(evt);
            }
        });
        btnCupcakeVainilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCupcakeVainillaActionPerformed(evt);
            }
        });

        btnGalletas12uCoco.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnGalletas12uCoco.setText("GALLETAS 12U. COCO");
        btnGalletas12uCoco.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGalletas12uCocoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGalletas12uCocoMouseExited(evt);
            }
        });
        btnGalletas12uCoco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGalletas12uCocoActionPerformed(evt);
            }
        });

        btnGalletas12uVainilla.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnGalletas12uVainilla.setText("GALLETAS 12U. VAINILLA");
        btnGalletas12uVainilla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGalletas12uVainillaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGalletas12uVainillaMouseExited(evt);
            }
        });
        btnGalletas12uVainilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGalletas12uVainillaActionPerformed(evt);
            }
        });

        btnPieDuraznoEntero.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnPieDuraznoEntero.setText("PIE DE DURAZNO ENTERO");
        btnPieDuraznoEntero.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPieDuraznoEnteroMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPieDuraznoEnteroMouseExited(evt);
            }
        });
        btnPieDuraznoEntero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPieDuraznoEnteroActionPerformed(evt);
            }
        });

        btnPieFrutillaEntero.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnPieFrutillaEntero.setText("PIE DE FRUTILLA ENTERO");
        btnPieFrutillaEntero.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPieFrutillaEnteroMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPieFrutillaEnteroMouseExited(evt);
            }
        });
        btnPieFrutillaEntero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPieFrutillaEnteroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMasitasLayout = new javax.swing.GroupLayout(panelMasitas);
        panelMasitas.setLayout(panelMasitasLayout);
        panelMasitasLayout.setHorizontalGroup(
            panelMasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMasitasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMasitasLayout.createSequentialGroup()
                        .addComponent(btnAlfajor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCroissant)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCupcakeChocolate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCupcakeFrutilla))
                    .addGroup(panelMasitasLayout.createSequentialGroup()
                        .addComponent(btnPorcionItaly)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnQuequeNaranja)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRolloGrande))
                    .addGroup(panelMasitasLayout.createSequentialGroup()
                        .addComponent(btnCupcakeVainilla)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDona)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEmpanada))
                    .addGroup(panelMasitasLayout.createSequentialGroup()
                        .addComponent(btnGalleta12uChocolate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGalletas12uCoco))
                    .addGroup(panelMasitasLayout.createSequentialGroup()
                        .addComponent(btnGalleta12UNaranja)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGalletas12uVainilla)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHojaldre))
                    .addGroup(panelMasitasLayout.createSequentialGroup()
                        .addComponent(btnPastel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPastel3Leches)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPastelSelvaNegra))
                    .addGroup(panelMasitasLayout.createSequentialGroup()
                        .addComponent(btnPieDuraznoEntero)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPieDuraznoUnidad))
                    .addGroup(panelMasitasLayout.createSequentialGroup()
                        .addComponent(btnPieFrutillaEntero)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPieFrutillaUnidad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPieManzana))
                    .addGroup(panelMasitasLayout.createSequentialGroup()
                        .addComponent(btnRolloPequeño)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnYemas)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        panelMasitasLayout.setVerticalGroup(
            panelMasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMasitasLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelMasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAlfajor)
                    .addComponent(btnCroissant)
                    .addComponent(btnCupcakeChocolate)
                    .addComponent(btnCupcakeFrutilla))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCupcakeVainilla)
                    .addComponent(btnDona)
                    .addComponent(btnEmpanada))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGalleta12uChocolate)
                    .addComponent(btnGalletas12uCoco))
                .addGap(18, 18, 18)
                .addGroup(panelMasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGalletas12uVainilla)
                    .addComponent(btnGalleta12UNaranja)
                    .addComponent(btnHojaldre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPastel)
                    .addComponent(btnPastel3Leches)
                    .addComponent(btnPastelSelvaNegra))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPieDuraznoEntero)
                    .addComponent(btnPieDuraznoUnidad))
                .addGap(18, 18, 18)
                .addGroup(panelMasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPieFrutillaEntero)
                    .addComponent(btnPieFrutillaUnidad)
                    .addComponent(btnPieManzana))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelMasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPorcionItaly)
                    .addComponent(btnQuequeNaranja)
                    .addComponent(btnRolloGrande))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRolloPequeño)
                    .addComponent(btnYemas))
                .addGap(108, 108, 108))
        );

        subMenu.addTab("MASITAS", panelMasitas);

        panelTortas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnEspecialSelvaNegra.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnEspecialSelvaNegra.setText("ESPECIAL SELVA NEGRA");
        btnEspecialSelvaNegra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEspecialSelvaNegraMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEspecialSelvaNegraMouseExited(evt);
            }
        });
        btnEspecialSelvaNegra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEspecialSelvaNegraActionPerformed(evt);
            }
        });

        btnTradicionalPequeño.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTradicionalPequeño.setText("TRADICIONAL PEQUEÑO");
        btnTradicionalPequeño.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTradicionalPequeñoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTradicionalPequeñoMouseExited(evt);
            }
        });
        btnTradicionalPequeño.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTradicionalPequeñoActionPerformed(evt);
            }
        });

        btnPequeñoChocolate.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnPequeñoChocolate.setText("PEQUEÑO CON CHOCOLATE");
        btnPequeñoChocolate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPequeñoChocolateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPequeñoChocolateMouseExited(evt);
            }
        });
        btnPequeñoChocolate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPequeñoChocolateActionPerformed(evt);
            }
        });

        btnTradicionalMediano.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTradicionalMediano.setText("TRADICIONAL MEDIANO");
        btnTradicionalMediano.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTradicionalMedianoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTradicionalMedianoMouseExited(evt);
            }
        });
        btnTradicionalMediano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTradicionalMedianoActionPerformed(evt);
            }
        });

        btnTradicionalGrande.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTradicionalGrande.setText("TRADICIONAL GRANDE");
        btnTradicionalGrande.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTradicionalGrandeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTradicionalGrandeMouseExited(evt);
            }
        });
        btnTradicionalGrande.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTradicionalGrandeActionPerformed(evt);
            }
        });

        btnEspecialMocka.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnEspecialMocka.setText("ESPECIAL MOCKA");
        btnEspecialMocka.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEspecialMockaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEspecialMockaMouseExited(evt);
            }
        });
        btnEspecialMocka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEspecialMockaActionPerformed(evt);
            }
        });

        btnMedianoChocolate.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnMedianoChocolate.setText("MEDIANO CON CHOCOLATE");
        btnMedianoChocolate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMedianoChocolateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMedianoChocolateMouseExited(evt);
            }
        });
        btnMedianoChocolate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMedianoChocolateActionPerformed(evt);
            }
        });

        btnMedianoJalea.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnMedianoJalea.setText("MEDIANO CON JALEA");
        btnMedianoJalea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMedianoJaleaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMedianoJaleaMouseExited(evt);
            }
        });
        btnMedianoJalea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMedianoJaleaActionPerformed(evt);
            }
        });

        btnFotoTorta.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnFotoTorta.setText("FOTO TORTA");
        btnFotoTorta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFotoTortaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFotoTortaMouseExited(evt);
            }
        });
        btnFotoTorta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoTortaActionPerformed(evt);
            }
        });

        btnEspecialTresLeches.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnEspecialTresLeches.setText("ESPECIAL TRES LECHES");
        btnEspecialTresLeches.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEspecialTresLechesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEspecialTresLechesMouseExited(evt);
            }
        });
        btnEspecialTresLeches.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEspecialTresLechesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTortasLayout = new javax.swing.GroupLayout(panelTortas);
        panelTortas.setLayout(panelTortasLayout);
        panelTortasLayout.setHorizontalGroup(
            panelTortasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTortasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTortasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTortasLayout.createSequentialGroup()
                        .addComponent(btnEspecialMocka)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEspecialTresLeches)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEspecialSelvaNegra))
                    .addGroup(panelTortasLayout.createSequentialGroup()
                        .addComponent(btnFotoTorta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMedianoChocolate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMedianoJalea))
                    .addGroup(panelTortasLayout.createSequentialGroup()
                        .addComponent(btnPequeñoChocolate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTradicionalGrande))
                    .addGroup(panelTortasLayout.createSequentialGroup()
                        .addComponent(btnTradicionalMediano)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTradicionalPequeño)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        panelTortasLayout.setVerticalGroup(
            panelTortasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTortasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTortasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEspecialMocka)
                    .addComponent(btnEspecialTresLeches)
                    .addComponent(btnEspecialSelvaNegra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTortasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFotoTorta)
                    .addComponent(btnMedianoChocolate)
                    .addComponent(btnMedianoJalea))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTortasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPequeñoChocolate)
                    .addComponent(btnTradicionalGrande))
                .addGap(18, 18, 18)
                .addGroup(panelTortasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTradicionalMediano)
                    .addComponent(btnTradicionalPequeño))
                .addContainerGap())
        );

        subMenu.addTab("TORTAS", panelTortas);

        btnMalteadaFresa.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnMalteadaFresa.setText("MALTEADA DE FRESA");
        btnMalteadaFresa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMalteadaFresaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMalteadaFresaMouseExited(evt);
            }
        });
        btnMalteadaFresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMalteadaFresaActionPerformed(evt);
            }
        });

        btnMalteadaChocolate.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnMalteadaChocolate.setText("MALTEADA DE CHOCOLATE");
        btnMalteadaChocolate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMalteadaChocolateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMalteadaChocolateMouseExited(evt);
            }
        });
        btnMalteadaChocolate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMalteadaChocolateActionPerformed(evt);
            }
        });

        btnMalteadaVainilla.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnMalteadaVainilla.setText("MALTEADA DE VAINILLA");
        btnMalteadaVainilla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMalteadaVainillaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMalteadaVainillaMouseExited(evt);
            }
        });
        btnMalteadaVainilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMalteadaVainillaActionPerformed(evt);
            }
        });

        btnCopaBomBom.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCopaBomBom.setText("COPA BOM BOM");
        btnCopaBomBom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCopaBomBomMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCopaBomBomMouseExited(evt);
            }
        });
        btnCopaBomBom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopaBomBomActionPerformed(evt);
            }
        });

        btnCopaChipsAhoy.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCopaChipsAhoy.setText("COPA CHIPS AHOY");
        btnCopaChipsAhoy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCopaChipsAhoyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCopaChipsAhoyMouseExited(evt);
            }
        });
        btnCopaChipsAhoy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopaChipsAhoyActionPerformed(evt);
            }
        });

        btnCopaOreo.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnCopaOreo.setText("COPA OREO");
        btnCopaOreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCopaOreoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCopaOreoMouseExited(evt);
            }
        });
        btnCopaOreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopaOreoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelHeladeríaLayout = new javax.swing.GroupLayout(panelHeladería);
        panelHeladería.setLayout(panelHeladeríaLayout);
        panelHeladeríaLayout.setHorizontalGroup(
            panelHeladeríaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeladeríaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHeladeríaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHeladeríaLayout.createSequentialGroup()
                        .addComponent(btnMalteadaFresa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMalteadaChocolate))
                    .addGroup(panelHeladeríaLayout.createSequentialGroup()
                        .addComponent(btnMalteadaVainilla)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCopaBomBom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCopaChipsAhoy))
                    .addComponent(btnCopaOreo))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        panelHeladeríaLayout.setVerticalGroup(
            panelHeladeríaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeladeríaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHeladeríaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMalteadaFresa)
                    .addComponent(btnMalteadaChocolate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelHeladeríaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMalteadaVainilla)
                    .addComponent(btnCopaBomBom)
                    .addComponent(btnCopaChipsAhoy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCopaOreo)
                .addContainerGap(294, Short.MAX_VALUE))
        );

        subMenu.addTab("HELADERIA", panelHeladería);

        btnTorta50p2p.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTorta50p2p.setText("T. 50 PERSONAS NORMAL CON 2 PIEZAS");
        btnTorta50p2p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTorta50p2pMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTorta50p2pMouseExited(evt);
            }
        });
        btnTorta50p2p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTorta50p2pActionPerformed(evt);
            }
        });

        btnTorta100p5p.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTorta100p5p.setText("T. 100 PERSONAS NORMAL CON 5 PIEZAS");
        btnTorta100p5p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTorta100p5pMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTorta100p5pMouseExited(evt);
            }
        });
        btnTorta100p5p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTorta100p5pActionPerformed(evt);
            }
        });

        btnTorta150p6p.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTorta150p6p.setText("T. 150 PERSONAS NORMAL CON 6 PIEZAS");
        btnTorta150p6p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTorta150p6pMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTorta150p6pMouseExited(evt);
            }
        });
        btnTorta150p6p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTorta150p6pActionPerformed(evt);
            }
        });

        btnTorta300p15p.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTorta300p15p.setText("T. 300 PERSONAS NORMAL CON 15 PIEZAS");
        btnTorta300p15p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTorta300p15pMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTorta300p15pMouseExited(evt);
            }
        });
        btnTorta300p15p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTorta300p15pActionPerformed(evt);
            }
        });

        btnTorta500p25p.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTorta500p25p.setText("T. 500 PERSONAS NORMAL CON 25 PIEZAS");
        btnTorta500p25p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTorta500p25pMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTorta500p25pMouseExited(evt);
            }
        });
        btnTorta500p25p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTorta500p25pActionPerformed(evt);
            }
        });

        btnFotoTortaPequeña.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnFotoTortaPequeña.setText("FOTO TORTA NORMAL PEQUEÑA");
        btnFotoTortaPequeña.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFotoTortaPequeñaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFotoTortaPequeñaMouseExited(evt);
            }
        });
        btnFotoTortaPequeña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoTortaPequeñaActionPerformed(evt);
            }
        });

        btnFotoTortaGrande.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnFotoTortaGrande.setText("FOTO TORTA NORMAL GRANDE");
        btnFotoTortaGrande.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFotoTortaGrandeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFotoTortaGrandeMouseExited(evt);
            }
        });
        btnFotoTortaGrande.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoTortaGrandeActionPerformed(evt);
            }
        });

        btnTorta50pCuadrado.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTorta50pCuadrado.setText("T. 50 PERSONAS NORMAL CUADRADO ENTERO");
        btnTorta50pCuadrado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTorta50pCuadradoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTorta50pCuadradoMouseExited(evt);
            }
        });
        btnTorta50pCuadrado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTorta50pCuadradoActionPerformed(evt);
            }
        });

        btnTorta100pCuadrado.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTorta100pCuadrado.setText("T. 100 PERSONAS NORMAL CUADRADO ENTERO");
        btnTorta100pCuadrado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTorta100pCuadradoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTorta100pCuadradoMouseExited(evt);
            }
        });
        btnTorta100pCuadrado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTorta100pCuadradoActionPerformed(evt);
            }
        });

        btnTorta200p10p.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTorta200p10p.setText("T. 200 PERSONAS NORMAL CON 10 PIEZAS");
        btnTorta200p10p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTorta200p10pMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTorta200p10pMouseExited(evt);
            }
        });
        btnTorta200p10p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTorta200p10pActionPerformed(evt);
            }
        });

        btnTorta400p22p.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnTorta400p22p.setText("T. 400 PERSONAS NORMAL CON 22 PIEZAS");
        btnTorta400p22p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTorta400p22pMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTorta400p22pMouseExited(evt);
            }
        });
        btnTorta400p22p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTorta400p22pActionPerformed(evt);
            }
        });

        btnFotoTortaMediana.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        btnFotoTortaMediana.setText("FOTO TORTA NORMAL MEDIANA");
        btnFotoTortaMediana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFotoTortaMedianaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFotoTortaMedianaMouseExited(evt);
            }
        });
        btnFotoTortaMediana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoTortaMedianaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnTorta300p15p)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFotoTortaPequeña))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnTorta500p25p)
                        .addGap(1, 1, 1)
                        .addComponent(btnFotoTortaMediana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTorta100p5p)
                            .addComponent(btnTorta150p6p)
                            .addComponent(btnTorta50pCuadrado)
                            .addComponent(btnTorta100pCuadrado)
                            .addComponent(btnTorta200p10p)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(btnTorta400p22p)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFotoTortaGrande, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnTorta50p2p)
                    .addContainerGap(272, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(btnTorta50pCuadrado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTorta100p5p)
                .addGap(18, 18, 18)
                .addComponent(btnTorta100pCuadrado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTorta150p6p)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTorta200p10p)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTorta300p15p)
                    .addComponent(btnFotoTortaPequeña))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTorta400p22p)
                    .addComponent(btnFotoTortaGrande))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTorta500p25p)
                    .addComponent(btnFotoTortaMediana))
                .addContainerGap(53, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnTorta50p2p)
                    .addContainerGap(366, Short.MAX_VALUE)))
        );

        subMenu.addTab("CONTRATOS TORTAS", jPanel5);

        javax.swing.GroupLayout PanelSubMenuProductosLayout = new javax.swing.GroupLayout(PanelSubMenuProductos);
        PanelSubMenuProductos.setLayout(PanelSubMenuProductosLayout);
        PanelSubMenuProductosLayout.setHorizontalGroup(
            PanelSubMenuProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSubMenuProductosLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(subMenu)
                .addContainerGap())
        );
        PanelSubMenuProductosLayout.setVerticalGroup(
            PanelSubMenuProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSubMenuProductosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedMenuCobrar.addTab("MENÚ", PanelSubMenuProductos);

        panelCobrarCliente.setBackground(new java.awt.Color(204, 204, 255));
        panelCobrarCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtTotalCobrarBs.setEditable(false);
        txtTotalCobrarBs.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("TOTAL Bs.");

        tablaCobroConsumo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tablaCobroConsumo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cantidad", "Descripción", "P/U Bs.", "Subtotal Bs."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaCobroConsumo.setRowHeight(24);
        tablaCobroConsumo.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablaCobroConsumo.setShowGrid(true);
        tablaCobroConsumo.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tablaCobroConsumoAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tablaCobroConsumo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tablaCobroConsumoMouseReleased(evt);
            }
        });
        tablaCobroConsumo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaCobroConsumoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tablaCobroConsumoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tablaCobroConsumoKeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(tablaCobroConsumo);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalCobrarBs, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalCobrarBs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap())
        );

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("Nombre:");

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setText("Apellido:");

        txtNombre.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        txtApellido.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 51));
        jLabel11.setText("C.I.:");

        txtCarnet.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtCarnet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCarnetActionPerformed(evt);
            }
        });
        txtCarnet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCarnetKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setText("WhatsApp:");

        txtWhatsapp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtWhatsapp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtWhatsappKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtWhatsapp, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel11)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCarnet, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel10)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtWhatsapp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtCarnet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtEfectivo.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        txtEfectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEfectivoActionPerformed(evt);
            }
        });
        txtEfectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEfectivoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEfectivoKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("EFECTIVO Bs.");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("CAMBIO Bs.");

        txtCambio.setEditable(false);
        txtCambio.setBackground(new java.awt.Color(255, 255, 236));
        txtCambio.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(29, 29, 29)
                        .addComponent(txtCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtEfectivo)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addComponent(txtCambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("VISITAS:");

        txtConsumosCliente.setBackground(new java.awt.Color(102, 204, 255));
        txtConsumosCliente.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtConsumosCliente.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtConsumosCliente.setEnabled(false);

        btnGuardaNuevoCliente.setBackground(new java.awt.Color(255, 0, 255));
        btnGuardaNuevoCliente.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnGuardaNuevoCliente.setText("GUARDAR NUEVO CLIENTE");
        btnGuardaNuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardaNuevoClienteActionPerformed(evt);
            }
        });

        panelTipoPago.setBackground(new java.awt.Color(51, 255, 51));
        panelTipoPago.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tipo de Pago", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N
        panelTipoPago.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        grupoTipoPago.add(radioTipoEfectivo);
        radioTipoEfectivo.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        radioTipoEfectivo.setText("Efectivo");
        radioTipoEfectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioTipoEfectivoActionPerformed(evt);
            }
        });

        grupoTipoPago.add(radioTipoTarjeta);
        radioTipoTarjeta.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        radioTipoTarjeta.setText("Tarjeta");
        radioTipoTarjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioTipoTarjetaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTipoPagoLayout = new javax.swing.GroupLayout(panelTipoPago);
        panelTipoPago.setLayout(panelTipoPagoLayout);
        panelTipoPagoLayout.setHorizontalGroup(
            panelTipoPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTipoPagoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioTipoEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioTipoTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelTipoPagoLayout.setVerticalGroup(
            panelTipoPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTipoPagoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelTipoPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioTipoEfectivo)
                    .addComponent(radioTipoTarjeta)))
        );

        javax.swing.GroupLayout panelCobrarClienteLayout = new javax.swing.GroupLayout(panelCobrarCliente);
        panelCobrarCliente.setLayout(panelCobrarClienteLayout);
        panelCobrarClienteLayout.setHorizontalGroup(
            panelCobrarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCobrarClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCobrarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelCobrarClienteLayout.createSequentialGroup()
                        .addGroup(panelCobrarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCobrarClienteLayout.createSequentialGroup()
                                .addComponent(panelTipoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtConsumosCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnGuardaNuevoCliente))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        panelCobrarClienteLayout.setVerticalGroup(
            panelCobrarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCobrarClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelCobrarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCobrarClienteLayout.createSequentialGroup()
                        .addComponent(panelTipoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGuardaNuevoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelCobrarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtConsumosCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        tabbedMenuCobrar.addTab("COBRAR CLIENTE", panelCobrarCliente);

        jPanel7.setBackground(new java.awt.Color(51, 204, 255));

        txtComanda.setFont(new java.awt.Font("Bell MT", 1, 8)); // NOI18N
        txtComanda.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        jScrollPane4.setViewportView(txtComanda);

        btnImprimir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos_Ventas/btnReimprimir.png"))); // NOI18N
        btnImprimir.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        btnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnImprimir.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(193, 193, 193)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(239, 239, 239)
                        .addComponent(btnImprimir)))
                .addContainerGap(239, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(116, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedMenuCobrar.addTab("comanda", jPanel2);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("TOTAL Bs.");

        txtTotalBs.setEditable(false);
        txtTotalBs.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        txtTotalBs.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 608, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalBs, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(panelTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(panelNumeroComanda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(tabbedMenuCobrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalBs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addComponent(panelPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelNumeroComanda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tabbedMenuCobrar, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenuBar1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jMenu1.setText("Registros");

        submenuComprasGastos.setText("Costos/Gastos");
        submenuComprasGastos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                submenuComprasGastosMouseClicked(evt);
            }
        });
        submenuComprasGastos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submenuComprasGastosActionPerformed(evt);
            }
        });
        jMenu1.add(submenuComprasGastos);

        jMenuBar1.add(jMenu1);

        menuReportes.setText("Reportes");

        menuTopTen.setText("Top Ten");

        submenuTopClientes.setText("Clientes");
        submenuTopClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submenuTopClientesActionPerformed(evt);
            }
        });
        menuTopTen.add(submenuTopClientes);

        submenuTopProductos.setText("Productos");
        submenuTopProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submenuTopProductosActionPerformed(evt);
            }
        });
        menuTopTen.add(submenuTopProductos);

        menuReportes.add(menuTopTen);

        menuVentas.setText("Ventas");

        subMenuVentasDiarias.setText("Ventas Diarias");
        subMenuVentasDiarias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuVentasDiariasActionPerformed(evt);
            }
        });
        menuVentas.add(subMenuVentasDiarias);

        subMenuVentasUnDia.setText("Ventas en un Día");
        subMenuVentasUnDia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuVentasUnDiaActionPerformed(evt);
            }
        });
        menuVentas.add(subMenuVentasUnDia);

        menuReportes.add(menuVentas);

        menuCompras.setText("Compras/ Gastos");
        menuCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuComprasActionPerformed(evt);
            }
        });
        menuReportes.add(menuCompras);

        jMenuBar1.add(menuReportes);

        menuCierreTurno.setText("Cerrar Turno");
        menuCierreTurno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuCierreTurnoMouseClicked(evt);
            }
        });
        menuCierreTurno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCierreTurnoActionPerformed(evt);
            }
        });
        jMenuBar1.add(menuCierreTurno);

        menuSalir.setText("Salir");
        menuSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuSalirMouseClicked(evt);
            }
        });
        menuSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSalirActionPerformed(evt);
            }
        });
        jMenuBar1.add(menuSalir);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblMesaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMesaMouseClicked
        //Vamos a cambiar el color de letra cuando es seleccionado el label
        lblMESA.setForeground(Color.red);
        lblLLEVAR.setForeground(Color.black);
        lblDELIVERY.setForeground(Color.black);
        String n = JOptionPane.showInputDialog(null, "Numero de mesa?");
        para = "Mesa:" + n + "";

    }//GEN-LAST:event_lblMesaMouseClicked

    private void lblLlevarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLlevarMouseClicked
        //Vamos a cambiar el color de letra cuando es seleccionado el label
        lblMESA.setForeground(Color.black);
        lblLLEVAR.setForeground(Color.red);
        lblDELIVERY.setForeground(Color.black);
        para = "Llevar";

    }//GEN-LAST:event_lblLlevarMouseClicked

    private void lblDeliveryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDeliveryMouseClicked
        //Vamos a cambiar el color de letra cuando es seleccionado el label 
        lblMESA.setForeground(Color.black);
        lblLLEVAR.setForeground(Color.black);
        lblDELIVERY.setForeground(Color.red);
        para = "Delivery";

    }//GEN-LAST:event_lblDeliveryMouseClicked

    private void btnPrepararMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrepararMouseEntered

    }//GEN-LAST:event_btnPrepararMouseEntered

    private void submenuComprasGastosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submenuComprasGastosMouseClicked


    }//GEN-LAST:event_submenuComprasGastosMouseClicked

    private void submenuComprasGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submenuComprasGastosActionPerformed
        // vamos a acceder a la ventaa de registro de compras y Gastos
        RegistroComprasGastos irRegistros = new RegistroComprasGastos();
        irRegistros.setVisible(true);
    }//GEN-LAST:event_submenuComprasGastosActionPerformed

    private void submenuTopClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submenuTopClientesActionPerformed
        // vamos a acceder a la ventaa de registro de Top Ten clientes
        reporte_Clientes irTopClientes = new reporte_Clientes();
        irTopClientes.setVisible(true);
    }//GEN-LAST:event_submenuTopClientesActionPerformed

    private void menuSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSalirActionPerformed

    }//GEN-LAST:event_menuSalirActionPerformed

    private void menuSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuSalirMouseClicked
        // TODO add your handling code here:
        int n1 = JOptionPane.showConfirmDialog(null, "¿Desea salir del sistema Ventas?", "ADVERTENCIA", 0);
        if (n1 == 0) {
            dispose();
            Acceso_al_Sistema IraVentanaPrincipal = new Acceso_al_Sistema();
            IraVentanaPrincipal.setVisible(true);

        } else {

        }
    }//GEN-LAST:event_menuSalirMouseClicked

    private void btnEspecialTresLechesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEspecialTresLechesMouseExited
        // TODO add your handling code here:
        btnEspecialTresLeches.setBackground(Color.white);
    }//GEN-LAST:event_btnEspecialTresLechesMouseExited

    private void btnEspecialTresLechesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEspecialTresLechesMouseEntered
        // TODO add your handling code here:
        btnEspecialTresLeches.setBackground(Color.cyan);
    }//GEN-LAST:event_btnEspecialTresLechesMouseEntered

    private void btnFotoTortaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoTortaMouseExited
        // TODO add your handling code here:
        btnFotoTorta.setBackground(Color.white);
    }//GEN-LAST:event_btnFotoTortaMouseExited

    private void btnFotoTortaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoTortaMouseEntered
        // TODO add your handling code here:
        btnFotoTorta.setBackground(Color.cyan);
    }//GEN-LAST:event_btnFotoTortaMouseEntered

    private void btnMedianoJaleaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMedianoJaleaMouseExited
        // TODO add your handling code here:
        btnMedianoJalea.setBackground(Color.white);
    }//GEN-LAST:event_btnMedianoJaleaMouseExited

    private void btnMedianoJaleaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMedianoJaleaMouseEntered
        // TODO add your handling code here:
        btnMedianoJalea.setBackground(Color.cyan);
    }//GEN-LAST:event_btnMedianoJaleaMouseEntered

    private void btnMedianoChocolateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMedianoChocolateMouseExited
        // TODO add your handling code here:
        btnMedianoChocolate.setBackground(Color.white);
    }//GEN-LAST:event_btnMedianoChocolateMouseExited

    private void btnMedianoChocolateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMedianoChocolateMouseEntered
        // TODO add your handling code here:
        btnMedianoChocolate.setBackground(Color.cyan);
    }//GEN-LAST:event_btnMedianoChocolateMouseEntered

    private void btnEspecialMockaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEspecialMockaMouseExited
        // TODO add your handling code here:
        btnEspecialMocka.setBackground(Color.white);
    }//GEN-LAST:event_btnEspecialMockaMouseExited

    private void btnEspecialMockaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEspecialMockaMouseEntered
        // TODO add your handling code here:
        btnEspecialMocka.setBackground(Color.cyan);
    }//GEN-LAST:event_btnEspecialMockaMouseEntered

    private void btnTradicionalGrandeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTradicionalGrandeMouseExited
        // TODO add your handling code here:
        btnTradicionalGrande.setBackground(Color.white);
    }//GEN-LAST:event_btnTradicionalGrandeMouseExited

    private void btnTradicionalGrandeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTradicionalGrandeMouseEntered
        // TODO add your handling code here:
        btnTradicionalGrande.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTradicionalGrandeMouseEntered

    private void btnTradicionalMedianoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTradicionalMedianoMouseExited
        // TODO add your handling code here:
        btnTradicionalMediano.setBackground(Color.white);
    }//GEN-LAST:event_btnTradicionalMedianoMouseExited

    private void btnTradicionalMedianoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTradicionalMedianoMouseEntered
        // TODO add your handling code here:
        btnTradicionalMediano.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTradicionalMedianoMouseEntered

    private void btnPequeñoChocolateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPequeñoChocolateMouseExited
        // TODO add your handling code here:
        btnPequeñoChocolate.setBackground(Color.white);
    }//GEN-LAST:event_btnPequeñoChocolateMouseExited

    private void btnPequeñoChocolateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPequeñoChocolateMouseEntered
        // TODO add your handling code here:
        btnPequeñoChocolate.setBackground(Color.cyan);
    }//GEN-LAST:event_btnPequeñoChocolateMouseEntered

    private void btnTradicionalPequeñoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTradicionalPequeñoMouseExited
        // TODO add your handling code here:
        btnTradicionalPequeño.setBackground(Color.white);
    }//GEN-LAST:event_btnTradicionalPequeñoMouseExited

    private void btnTradicionalPequeñoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTradicionalPequeñoMouseEntered
        // TODO add your handling code here:
        btnTradicionalPequeño.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTradicionalPequeñoMouseEntered

    private void btnEspecialSelvaNegraMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEspecialSelvaNegraMouseExited
        // TODO add your handling code here:
        btnEspecialSelvaNegra.setBackground(Color.white);
    }//GEN-LAST:event_btnEspecialSelvaNegraMouseExited

    private void btnEspecialSelvaNegraMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEspecialSelvaNegraMouseEntered
        // TODO add your handling code here:
        btnEspecialSelvaNegra.setBackground(Color.cyan);
    }//GEN-LAST:event_btnEspecialSelvaNegraMouseEntered

    private void btnEmpanadaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmpanadaMouseExited
        // TODO add your handling code here:
        btnEmpanada.setBackground(Color.white);
    }//GEN-LAST:event_btnEmpanadaMouseExited

    private void btnEmpanadaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmpanadaMouseEntered
        // TODO add your handling code here:
        btnEmpanada.setBackground(Color.cyan);
    }//GEN-LAST:event_btnEmpanadaMouseEntered

    private void btnCroissantMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCroissantMouseExited
        // TODO add your handling code here:
        btnCroissant.setBackground(Color.white);
    }//GEN-LAST:event_btnCroissantMouseExited

    private void btnCroissantMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCroissantMouseEntered
        // TODO add your handling code here:
        btnCroissant.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCroissantMouseEntered

    private void btnPieFrutillaUnidadMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPieFrutillaUnidadMouseExited
        // TODO add your handling code here:
        btnPieFrutillaUnidad.setBackground(Color.white);
    }//GEN-LAST:event_btnPieFrutillaUnidadMouseExited

    private void btnPieFrutillaUnidadMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPieFrutillaUnidadMouseEntered
        // TODO add your handling code here:
        btnPieFrutillaUnidad.setBackground(Color.cyan);
    }//GEN-LAST:event_btnPieFrutillaUnidadMouseEntered

    private void btnPieManzanaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPieManzanaMouseExited
        // TODO add your handling code here:
        btnPieManzana.setBackground(Color.white);
    }//GEN-LAST:event_btnPieManzanaMouseExited

    private void btnPieManzanaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPieManzanaMouseEntered
        // TODO add your handling code here:
        btnPieManzana.setBackground(Color.cyan);
    }//GEN-LAST:event_btnPieManzanaMouseEntered

    private void btnRolloGrandeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRolloGrandeMouseExited
        // TODO add your handling code here:
        btnRolloGrande.setBackground(Color.white);
    }//GEN-LAST:event_btnRolloGrandeMouseExited

    private void btnRolloGrandeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRolloGrandeMouseEntered
        // TODO add your handling code here:
        btnRolloGrande.setBackground(Color.cyan);
    }//GEN-LAST:event_btnRolloGrandeMouseEntered

    private void btnDonaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDonaMouseExited
        // TODO add your handling code here:
        btnDona.setBackground(Color.white);
    }//GEN-LAST:event_btnDonaMouseExited

    private void btnDonaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDonaMouseEntered
        // TODO add your handling code here:
        btnDona.setBackground(Color.cyan);
    }//GEN-LAST:event_btnDonaMouseEntered

    private void btnYemasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnYemasMouseExited
        // TODO add your handling code here:
        btnYemas.setBackground(Color.white);
    }//GEN-LAST:event_btnYemasMouseExited

    private void btnYemasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnYemasMouseEntered
        // TODO add your handling code here:
        btnYemas.setBackground(Color.cyan);
    }//GEN-LAST:event_btnYemasMouseEntered

    private void btnGalleta12uChocolateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGalleta12uChocolateMouseExited
        // TODO add your handling code here:
        btnGalleta12uChocolate.setBackground(Color.white);
    }//GEN-LAST:event_btnGalleta12uChocolateMouseExited

    private void btnGalleta12uChocolateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGalleta12uChocolateMouseEntered
        // TODO add your handling code here:
        btnGalleta12uChocolate.setBackground(Color.cyan);
    }//GEN-LAST:event_btnGalleta12uChocolateMouseEntered

    private void btnCupcakeChocolateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCupcakeChocolateMouseExited
        // TODO add your handling code here:
        btnCupcakeChocolate.setBackground(Color.white);
    }//GEN-LAST:event_btnCupcakeChocolateMouseExited

    private void btnCupcakeChocolateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCupcakeChocolateMouseEntered
        // TODO add your handling code here:
        btnCupcakeChocolate.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCupcakeChocolateMouseEntered

    private void btnQuequeNaranjaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQuequeNaranjaMouseExited
        // TODO add your handling code here:
        btnQuequeNaranja.setBackground(Color.white);
    }//GEN-LAST:event_btnQuequeNaranjaMouseExited

    private void btnQuequeNaranjaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQuequeNaranjaMouseEntered
        // TODO add your handling code here:
        btnQuequeNaranja.setBackground(Color.cyan);
    }//GEN-LAST:event_btnQuequeNaranjaMouseEntered

    private void btnPastelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPastelMouseExited

        btnPastel.setBackground(Color.white);

    }//GEN-LAST:event_btnPastelMouseExited

    private void btnPastelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPastelMouseEntered
        // TODO add your handling code here:
        btnPastel.setBackground(Color.cyan);
    }//GEN-LAST:event_btnPastelMouseEntered

    private void btnHojaldreMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHojaldreMouseExited
        // TODO add your handling code here:
        btnHojaldre.setBackground(Color.white);
    }//GEN-LAST:event_btnHojaldreMouseExited

    private void btnHojaldreMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHojaldreMouseEntered
        // TODO add your handling code here:
        btnHojaldre.setBackground(Color.cyan);
    }//GEN-LAST:event_btnHojaldreMouseEntered

    private void btnPieDuraznoUnidadMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPieDuraznoUnidadMouseExited
        // TODO add your handling code here:
        btnPieDuraznoUnidad.setBackground(Color.white);
    }//GEN-LAST:event_btnPieDuraznoUnidadMouseExited

    private void btnPieDuraznoUnidadMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPieDuraznoUnidadMouseEntered
        // TODO add your handling code here:
        btnPieDuraznoUnidad.setBackground(Color.cyan);
    }//GEN-LAST:event_btnPieDuraznoUnidadMouseEntered

    private void btnRolloPequeñoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRolloPequeñoMouseExited
        // TODO add your handling code here:
        btnRolloPequeño.setBackground(Color.white);
    }//GEN-LAST:event_btnRolloPequeñoMouseExited

    private void btnRolloPequeñoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRolloPequeñoMouseEntered
        // TODO add your handling code here:
        btnRolloPequeño.setBackground(Color.cyan);
    }//GEN-LAST:event_btnRolloPequeñoMouseEntered

    private void btnAlfajorMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlfajorMouseExited
        // TODO add your handling code here:
        btnAlfajor.setBackground(Color.white);
    }//GEN-LAST:event_btnAlfajorMouseExited

    private void btnAlfajorMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlfajorMouseEntered
        // TODO add your handling code here:
        btnAlfajor.setBackground(Color.cyan);
    }//GEN-LAST:event_btnAlfajorMouseEntered

    private void btnChocolateMavabiscoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChocolateMavabiscoMouseExited
        // TODO add your handling code here:
        btnChocolateMavabisco.setBackground(Color.white);
    }//GEN-LAST:event_btnChocolateMavabiscoMouseExited

    private void btnChocolateMavabiscoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChocolateMavabiscoMouseEntered
        // TODO add your handling code here:
        btnChocolateMavabisco.setBackground(Color.cyan);
    }//GEN-LAST:event_btnChocolateMavabiscoMouseEntered

    private void btnChocolateLecheMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChocolateLecheMouseExited
        // TODO add your handling code here:
        btnChocolateLeche.setBackground(Color.white);
    }//GEN-LAST:event_btnChocolateLecheMouseExited

    private void btnChocolateLecheMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChocolateLecheMouseEntered
        // TODO add your handling code here:
        btnChocolateLeche.setBackground(Color.cyan);
    }//GEN-LAST:event_btnChocolateLecheMouseEntered

    private void btnToddyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnToddyMouseExited
        // TODO add your handling code here:
        btnToddy.setBackground(Color.white);
    }//GEN-LAST:event_btnToddyMouseExited

    private void btnToddyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnToddyMouseEntered
        // TODO add your handling code here:
        btnToddy.setBackground(Color.cyan);
    }//GEN-LAST:event_btnToddyMouseEntered

    private void btnCafeBomBomMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCafeBomBomMouseExited
        // TODO add your handling code here:
        btnCafeBomBom.setBackground(Color.white);
    }//GEN-LAST:event_btnCafeBomBomMouseExited

    private void btnCafeBomBomMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCafeBomBomMouseEntered
        // TODO add your handling code here:
        btnCafeBomBom.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCafeBomBomMouseEntered

    private void btnChaiLateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChaiLateMouseExited
        // TODO add your handling code here:
        btnChaiLate.setBackground(Color.white);
    }//GEN-LAST:event_btnChaiLateMouseExited

    private void btnChaiLateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChaiLateMouseEntered
        // TODO add your handling code here:
        btnChaiLate.setBackground(Color.cyan);
    }//GEN-LAST:event_btnChaiLateMouseEntered

    private void btnSubmarinoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmarinoMouseExited
        // TODO add your handling code here:
        btnSubmarino.setBackground(Color.white);
    }//GEN-LAST:event_btnSubmarinoMouseExited

    private void btnSubmarinoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmarinoMouseEntered
        // TODO add your handling code here:
        btnSubmarino.setBackground(Color.cyan);
    }//GEN-LAST:event_btnSubmarinoMouseEntered

    private void btnCapuccinoCompletoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCapuccinoCompletoMouseExited
        // TODO add your handling code here:
        btnCapuccinoCompleto.setBackground(Color.white);
    }//GEN-LAST:event_btnCapuccinoCompletoMouseExited

    private void btnCapuccinoCompletoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCapuccinoCompletoMouseEntered
        // TODO add your handling code here:
        btnCapuccinoCompleto.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCapuccinoCompletoMouseEntered

    private void btnCafeIrlandesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCafeIrlandesMouseExited
        // TODO add your handling code here:
        btnCafeIrlandes.setBackground(Color.white);
    }//GEN-LAST:event_btnCafeIrlandesMouseExited

    private void btnCafeIrlandesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCafeIrlandesMouseEntered
        // TODO add your handling code here:
        btnCafeIrlandes.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCafeIrlandesMouseEntered

    private void btnMochaccinoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMochaccinoMouseExited
        // TODO add your handling code here:
        btnMochaccino.setBackground(Color.white);
    }//GEN-LAST:event_btnMochaccinoMouseExited

    private void btnMochaccinoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMochaccinoMouseEntered
        // TODO add your handling code here:
        btnMochaccino.setBackground(Color.cyan);
    }//GEN-LAST:event_btnMochaccinoMouseEntered

    private void btnSubmarinoOreoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmarinoOreoMousePressed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnSubmarinoOreoMousePressed

    private void btnSubmarinoOreoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmarinoOreoMouseExited
        // TODO add your handling code here:
        btnSubmarinoOreo.setBackground(Color.white);
    }//GEN-LAST:event_btnSubmarinoOreoMouseExited

    private void btnSubmarinoOreoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmarinoOreoMouseEntered
        // TODO add your handling code here:
        btnSubmarinoOreo.setBackground(Color.cyan);
    }//GEN-LAST:event_btnSubmarinoOreoMouseEntered

    private void btnSubmarinoBonBomMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmarinoBonBomMouseExited
        // TODO add your handling code here:
        btnSubmarinoBonBom.setBackground(Color.white);
    }//GEN-LAST:event_btnSubmarinoBonBomMouseExited

    private void btnSubmarinoBonBomMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmarinoBonBomMouseEntered
        // TODO add your handling code here:
        btnSubmarinoBonBom.setBackground(Color.cyan);
    }//GEN-LAST:event_btnSubmarinoBonBomMouseEntered

    private void btnCapuccinoSimpleMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCapuccinoSimpleMouseExited
        // TODO add your handling code here:
        btnCapuccinoSimple.setBackground(Color.white);
    }//GEN-LAST:event_btnCapuccinoSimpleMouseExited

    private void btnCapuccinoSimpleMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCapuccinoSimpleMouseEntered
        // TODO add your handling code here:
        btnCapuccinoSimple.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCapuccinoSimpleMouseEntered

    private void btnCapuccinoVainillaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCapuccinoVainillaMouseExited
        // TODO add your handling code here:
        btnCapuccinoVainilla.setBackground(Color.white);
    }//GEN-LAST:event_btnCapuccinoVainillaMouseExited

    private void btnCapuccinoVainillaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCapuccinoVainillaMouseEntered
        // TODO add your handling code here:
        btnCapuccinoVainilla.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCapuccinoVainillaMouseEntered

    private void btnChocolateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChocolateMouseExited
        // TODO add your handling code here:
        btnChocolate.setBackground(Color.white);
    }//GEN-LAST:event_btnChocolateMouseExited

    private void btnChocolateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChocolateMouseEntered
        // TODO add your handling code here:
        btnChocolate.setBackground(Color.cyan);
    }//GEN-LAST:event_btnChocolateMouseEntered

    private void btnLecheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLecheActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10032;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnLecheActionPerformed

    private void btnLecheMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLecheMouseExited
        // TODO add your handling code here:
        btnLeche.setBackground(Color.white);
    }//GEN-LAST:event_btnLecheMouseExited

    private void btnLecheMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLecheMouseEntered
        // TODO add your handling code here:
        btnLeche.setBackground(Color.cyan);
    }//GEN-LAST:event_btnLecheMouseEntered

    private void btnTeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTeMouseExited
        // TODO add your handling code here:
        btnTe.setBackground(Color.white);
    }//GEN-LAST:event_btnTeMouseExited

    private void btnTeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTeMouseEntered
        // TODO add your handling code here:
        btnTe.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTeMouseEntered

    private void btnXpressoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXpressoMouseExited
        // TODO add your handling code here:
        btnXpresso.setBackground(Color.white);
    }//GEN-LAST:event_btnXpressoMouseExited

    private void btnXpressoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXpressoMouseEntered
        // TODO add your handling code here:
        btnXpresso.setBackground(Color.cyan);
    }//GEN-LAST:event_btnXpressoMouseEntered

    private void btnAmericanoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAmericanoMouseExited
        // si se sale del boton el color cambia
        btnAmericano.setBackground(Color.white);
    }//GEN-LAST:event_btnAmericanoMouseExited

    private void btnAmericanoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAmericanoMouseEntered
        // si es seleccionado el color del boton cambia
        btnAmericano.setBackground(Color.cyan);
    }//GEN-LAST:event_btnAmericanoMouseEntered

    private void btnLatteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLatteMouseExited
        // TODO add your handling code here:
        btnLatte.setBackground(Color.white);
    }//GEN-LAST:event_btnLatteMouseExited

    private void btnLatteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLatteMouseEntered
        // TODO add your handling code here:
        btnLatte.setBackground(Color.cyan);
    }//GEN-LAST:event_btnLatteMouseEntered

    private void btnJugoFrutillaCONLECHEMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoFrutillaCONLECHEMouseExited
        // TODO add your handling code here:
        btnJugoFrutillaCONLECHE.setBackground(Color.white);
    }//GEN-LAST:event_btnJugoFrutillaCONLECHEMouseExited

    private void btnJugoFrutillaCONLECHEMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoFrutillaCONLECHEMouseEntered
        // TODO add your handling code here:
        btnJugoFrutillaCONLECHE.setBackground(Color.cyan);
    }//GEN-LAST:event_btnJugoFrutillaCONLECHEMouseEntered

    private void btnJugoPlatanoCONLECHEMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoPlatanoCONLECHEMouseExited
        // TODO add your handling code here:
        btnJugoPlatanoCONLECHE.setBackground(Color.white);
    }//GEN-LAST:event_btnJugoPlatanoCONLECHEMouseExited

    private void btnJugoPlatanoCONLECHEMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoPlatanoCONLECHEMouseEntered
        // TODO add your handling code here:
        btnJugoPlatanoCONLECHE.setBackground(Color.cyan);
    }//GEN-LAST:event_btnJugoPlatanoCONLECHEMouseEntered

    private void btnJugoPapayaCONLECHEMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoPapayaCONLECHEMouseExited
        // TODO add your handling code here:
        btnJugoPapayaCONLECHE.setBackground(Color.white);
    }//GEN-LAST:event_btnJugoPapayaCONLECHEMouseExited

    private void btnJugoPapayaCONLECHEMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoPapayaCONLECHEMouseEntered
        // TODO add your handling code here:
        btnJugoPapayaCONLECHE.setBackground(Color.cyan);
    }//GEN-LAST:event_btnJugoPapayaCONLECHEMouseEntered

    private void btnFrapuccinoOreoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFrapuccinoOreoMouseExited
        // TODO add your handling code here:
        btnFrapuccinoOreo.setBackground(Color.white);
    }//GEN-LAST:event_btnFrapuccinoOreoMouseExited

    private void btnFrapuccinoOreoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFrapuccinoOreoMouseEntered
        // TODO add your handling code here:
        btnFrapuccinoOreo.setBackground(Color.cyan);
    }//GEN-LAST:event_btnFrapuccinoOreoMouseEntered

    private void btnJugoPapayaCONAGUAMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoPapayaCONAGUAMouseExited
        // TODO add your handling code here:
        btnJugoPapayaCONAGUA.setBackground(Color.white);

    }//GEN-LAST:event_btnJugoPapayaCONAGUAMouseExited

    private void btnJugoPapayaCONAGUAMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoPapayaCONAGUAMouseEntered
        btnJugoPapayaCONAGUA.setBackground(Color.cyan);
    }//GEN-LAST:event_btnJugoPapayaCONAGUAMouseEntered

    private void btnJugoPlatanoCONAGUAMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoPlatanoCONAGUAMouseExited
        // TODO add your handling code here:
        btnJugoPlatanoCONAGUA.setBackground(Color.white);
    }//GEN-LAST:event_btnJugoPlatanoCONAGUAMouseExited

    private void btnJugoPlatanoCONAGUAMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoPlatanoCONAGUAMouseEntered
        // TODO add your handling code here:
        btnJugoPlatanoCONAGUA.setBackground(Color.cyan);
    }//GEN-LAST:event_btnJugoPlatanoCONAGUAMouseEntered

    private void btnJugoFrutillaCONAGUAMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoFrutillaCONAGUAMouseExited
        // TODO add your handling code here:
        btnJugoFrutillaCONAGUA.setBackground(Color.white);
    }//GEN-LAST:event_btnJugoFrutillaCONAGUAMouseExited

    private void btnJugoFrutillaCONAGUAMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoFrutillaCONAGUAMouseEntered
        // TODO add your handling code here:
        btnJugoFrutillaCONAGUA.setBackground(Color.cyan);
    }//GEN-LAST:event_btnJugoFrutillaCONAGUAMouseEntered

    private void btnPersonalesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPersonalesMouseExited
        // TODO add your handling code here:
        btnPersonales.setBackground(Color.white);
    }//GEN-LAST:event_btnPersonalesMouseExited

    private void btnPersonalesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPersonalesMouseEntered
        // TODO add your handling code here:
        btnPersonales.setBackground(Color.cyan);
    }//GEN-LAST:event_btnPersonalesMouseEntered

    private void btnFrappeMokaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFrappeMokaMouseExited
        // TODO add your handling code here:
        btnFrappeMoka.setBackground(Color.white);
    }//GEN-LAST:event_btnFrappeMokaMouseExited

    private void btnFrappeMokaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFrappeMokaMouseEntered
        // TODO add your handling code here:
        btnFrappeMoka.setBackground(Color.cyan);
    }//GEN-LAST:event_btnFrappeMokaMouseEntered

    private void btnFrappucinoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFrappucinoMouseExited
        // TODO add your handling code here:
        btnFrappucino.setBackground(Color.white);
    }//GEN-LAST:event_btnFrappucinoMouseExited

    private void btnFrappucinoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFrappucinoMouseEntered
        btnFrappucino.setBackground(Color.cyan);
    }//GEN-LAST:event_btnFrappucinoMouseEntered

    private void btnNuteLatteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuteLatteMouseEntered
        // TODO add your handling code here:
        btnNuteLatte.setBackground(Color.cyan);
    }//GEN-LAST:event_btnNuteLatteMouseEntered

    private void btnNuteLatteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuteLatteMouseExited
        // TODO add your handling code here:
        btnNuteLatte.setBackground(Color.white);
    }//GEN-LAST:event_btnNuteLatteMouseExited

    private void btnTrimateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTrimateMouseEntered
        // TODO add your handling code here:
        btnTrimate.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTrimateMouseEntered

    private void btnTrimateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTrimateMouseExited
        // TODO add your handling code here:
        btnTrimate.setBackground(Color.white);
    }//GEN-LAST:event_btnTrimateMouseExited

    private void btnAffogatoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAffogatoMouseEntered
        // TODO add your handling code here:
        btnAffogato.setBackground(Color.cyan);
    }//GEN-LAST:event_btnAffogatoMouseEntered

    private void btnAffogatoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAffogatoMouseExited
        // TODO add your handling code here:
        btnAffogato.setBackground(Color.white);
    }//GEN-LAST:event_btnAffogatoMouseExited

    private void btnCappuccinoFrioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCappuccinoFrioMouseEntered
        // TODO add your handling code here:
        btnCappuccinoFrio.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCappuccinoFrioMouseEntered

    private void btnCappuccinoFrioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCappuccinoFrioMouseExited
        // TODO add your handling code here:
        btnCappuccinoFrio.setBackground(Color.white);
    }//GEN-LAST:event_btnCappuccinoFrioMouseExited

    private void btnJugoDurazPapaAguaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoDurazPapaAguaMouseEntered
        // TODO add your handling code here:
        btnJugoDurazPapaAgua.setBackground(Color.cyan);
    }//GEN-LAST:event_btnJugoDurazPapaAguaMouseEntered

    private void btnJugoDurazPapaAguaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoDurazPapaAguaMouseExited
        // TODO add your handling code here:
        btnJugoDurazPapaAgua.setBackground(Color.white);
    }//GEN-LAST:event_btnJugoDurazPapaAguaMouseExited

    private void btnJugoDurazPapaLecheMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoDurazPapaLecheMouseEntered
        // TODO add your handling code here:
        btnJugoDurazPapaLeche.setBackground(Color.cyan);
    }//GEN-LAST:event_btnJugoDurazPapaLecheMouseEntered

    private void btnJugoDurazPapaLecheMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoDurazPapaLecheMouseExited
        // TODO add your handling code here:
        btnJugoDurazPapaLeche.setBackground(Color.white);
    }//GEN-LAST:event_btnJugoDurazPapaLecheMouseExited

    private void btnFrappeBaileysMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFrappeBaileysMouseEntered
        // TODO add your handling code here:
        btnFrappeBaileys.setBackground(Color.cyan);
    }//GEN-LAST:event_btnFrappeBaileysMouseEntered

    private void btnFrappeBaileysMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFrappeBaileysMouseExited
        // TODO add your handling code here:
        btnFrappeBaileys.setBackground(Color.white);
    }//GEN-LAST:event_btnFrappeBaileysMouseExited

    private void btnJugoMixAguaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoMixAguaMouseEntered
        // TODO add your handling code here:
        btnJugoMixAgua.setBackground(Color.cyan);
    }//GEN-LAST:event_btnJugoMixAguaMouseEntered

    private void btnJugoMixAguaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoMixAguaMouseExited
        // TODO add your handling code here:
        btnJugoMixAgua.setBackground(Color.white);
    }//GEN-LAST:event_btnJugoMixAguaMouseExited

    private void btnJugoMixLecheMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoMixLecheMouseEntered
        // TODO add your handling code here:
        btnJugoMixLeche.setBackground(Color.cyan);
    }//GEN-LAST:event_btnJugoMixLecheMouseEntered

    private void btnJugoMixLecheMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugoMixLecheMouseExited
        // TODO add your handling code here:
        btnJugoMixLeche.setBackground(Color.white);
    }//GEN-LAST:event_btnJugoMixLecheMouseExited

    private void btnNutelatteFrioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNutelatteFrioMouseEntered
        // TODO add your handling code here:
        btnNutelatteFrio.setBackground(Color.cyan);
    }//GEN-LAST:event_btnNutelatteFrioMouseEntered

    private void btnNutelatteFrioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNutelatteFrioMouseExited
        // TODO add your handling code here:
        btnNutelatteFrio.setBackground(Color.white);
    }//GEN-LAST:event_btnNutelatteFrioMouseExited

    private void btnMalteadaFresaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMalteadaFresaMouseEntered
        // TODO add your handling code here:
        btnMalteadaFresa.setBackground(Color.cyan);
    }//GEN-LAST:event_btnMalteadaFresaMouseEntered

    private void btnMalteadaFresaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMalteadaFresaMouseExited
        // TODO add your handling code here:
        btnMalteadaFresa.setBackground(Color.white);
    }//GEN-LAST:event_btnMalteadaFresaMouseExited

    private void btnMalteadaChocolateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMalteadaChocolateMouseEntered
        // TODO add your handling code here:
        btnMalteadaChocolate.setBackground(Color.cyan);
    }//GEN-LAST:event_btnMalteadaChocolateMouseEntered

    private void btnMalteadaChocolateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMalteadaChocolateMouseExited
        // TODO add your handling code here:
        btnMalteadaChocolate.setBackground(Color.white);
    }//GEN-LAST:event_btnMalteadaChocolateMouseExited

    private void btnMalteadaVainillaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMalteadaVainillaMouseEntered
        // TODO add your handling code here:
        btnMalteadaVainilla.setBackground(Color.cyan);
    }//GEN-LAST:event_btnMalteadaVainillaMouseEntered

    private void btnMalteadaVainillaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMalteadaVainillaMouseExited
        // TODO add your handling code here:
        btnMalteadaVainilla.setBackground(Color.white);
    }//GEN-LAST:event_btnMalteadaVainillaMouseExited

    private void btnCopaBomBomMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopaBomBomMouseEntered
        // TODO add your handling code here:
        btnCopaBomBom.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCopaBomBomMouseEntered

    private void btnCopaBomBomMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopaBomBomMouseExited
        // TODO add your handling code here:
        btnCopaBomBom.setBackground(Color.white);
    }//GEN-LAST:event_btnCopaBomBomMouseExited

    private void btnCopaChipsAhoyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopaChipsAhoyMouseEntered
        // TODO add your handling code here:
        btnCopaChipsAhoy.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCopaChipsAhoyMouseEntered

    private void btnCopaChipsAhoyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopaChipsAhoyMouseExited
        // TODO add your handling code here:
        btnCopaChipsAhoy.setBackground(Color.white);
    }//GEN-LAST:event_btnCopaChipsAhoyMouseExited

    private void btnCopaOreoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopaOreoMouseEntered
        // TODO add your handling code here:
        btnCopaOreo.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCopaOreoMouseEntered

    private void btnCopaOreoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopaOreoMouseExited
        // TODO add your handling code here:
        btnCopaOreo.setBackground(Color.white);
    }//GEN-LAST:event_btnCopaOreoMouseExited

    private void btnGalleta12UNaranjaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGalleta12UNaranjaMouseEntered
        // TODO add your handling code here:
        btnGalleta12UNaranja.setBackground(Color.cyan);
    }//GEN-LAST:event_btnGalleta12UNaranjaMouseEntered

    private void btnGalleta12UNaranjaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGalleta12UNaranjaMouseExited
        // TODO add your handling code here:
        btnGalleta12UNaranja.setBackground(Color.white);
    }//GEN-LAST:event_btnGalleta12UNaranjaMouseExited

    private void btnPastel3LechesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPastel3LechesMouseEntered
        // TODO add your handling code here:
        btnPastel3Leches.setBackground(Color.cyan);
    }//GEN-LAST:event_btnPastel3LechesMouseEntered

    private void btnPastel3LechesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPastel3LechesMouseExited
        // TODO add your handling code here:
        btnPastel3Leches.setBackground(Color.white);
    }//GEN-LAST:event_btnPastel3LechesMouseExited

    private void btnPastelSelvaNegraMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPastelSelvaNegraMouseEntered
        // TODO add your handling code here:
        btnPastelSelvaNegra.setBackground(Color.cyan);
    }//GEN-LAST:event_btnPastelSelvaNegraMouseEntered

    private void btnPastelSelvaNegraMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPastelSelvaNegraMouseExited
        // TODO add your handling code here:
        btnPastelSelvaNegra.setBackground(Color.white);
    }//GEN-LAST:event_btnPastelSelvaNegraMouseExited

    private void btnPorcionItalyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPorcionItalyMouseEntered
        // TODO add your handling code here:
        btnPorcionItaly.setBackground(Color.cyan);
    }//GEN-LAST:event_btnPorcionItalyMouseEntered

    private void btnPorcionItalyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPorcionItalyMouseExited
        // TODO add your handling code here:
        btnPorcionItaly.setBackground(Color.white);
    }//GEN-LAST:event_btnPorcionItalyMouseExited

    private void btnCupcakeFrutillaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCupcakeFrutillaMouseEntered
        // TODO add your handling code here:
        btnCupcakeFrutilla.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCupcakeFrutillaMouseEntered

    private void btnCupcakeFrutillaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCupcakeFrutillaMouseExited
        // TODO add your handling code here:
        btnCupcakeFrutilla.setBackground(Color.white);
    }//GEN-LAST:event_btnCupcakeFrutillaMouseExited

    private void btnCupcakeVainillaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCupcakeVainillaMouseEntered
        // TODO add your handling code here:
        btnCupcakeVainilla.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCupcakeVainillaMouseEntered

    private void btnCupcakeVainillaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCupcakeVainillaMouseExited
        // TODO add your handling code here:
        btnCupcakeVainilla.setBackground(Color.white);
    }//GEN-LAST:event_btnCupcakeVainillaMouseExited

    private void btnAffogatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAffogatoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10058;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo " + e.getMessage());
            }
        }

    }//GEN-LAST:event_btnAffogatoActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        eliminarTodo();
        CalcularTotalBs();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnAmericanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAmericanoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10026;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }

    }//GEN-LAST:event_btnAmericanoActionPerformed

    private void btnCafeBomBomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCafeBomBomActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10043;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCafeBomBomActionPerformed

    private void btnXpressoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXpressoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10027;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnXpressoActionPerformed

    private void btnCafeIrlandesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCafeIrlandesActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10044;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCafeIrlandesActionPerformed

    private void btnCapuccinoCompletoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapuccinoCompletoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10037;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCapuccinoCompletoActionPerformed

    private void btnCapuccinoVainillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapuccinoVainillaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10035;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCapuccinoVainillaActionPerformed

    private void btnCapuccinoSimpleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapuccinoSimpleActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10036;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCapuccinoSimpleActionPerformed

    private void btnChaiLateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChaiLateActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10042;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnChaiLateActionPerformed

    private void btnChocolateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChocolateActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10033;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnChocolateActionPerformed

    private void btnChocolateMavabiscoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChocolateMavabiscoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10031;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnChocolateMavabiscoActionPerformed

    private void btnChocolateLecheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChocolateLecheActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10030;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnChocolateLecheActionPerformed

    private void btnLatteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLatteActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10034;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnLatteActionPerformed

    private void btnMochaccinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMochaccinoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10041;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnMochaccinoActionPerformed

    private void btnNuteLatteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuteLatteActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10057;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnNuteLatteActionPerformed

    private void btnSubmarinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmarinoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10038;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnSubmarinoActionPerformed

    private void btnSubmarinoBonBomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmarinoBonBomActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10039;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnSubmarinoBonBomActionPerformed

    private void btnSubmarinoOreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmarinoOreoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10040;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnSubmarinoOreoActionPerformed

    private void btnTeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTeActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10056;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTeActionPerformed

    private void btnToddyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToddyActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10029;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnToddyActionPerformed

    private void btnTrimateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrimateActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10028;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTrimateActionPerformed

    private void btnCappuccinoFrioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCappuccinoFrioActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10059;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCappuccinoFrioActionPerformed

    private void btnFrappeBaileysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrappeBaileysActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10061;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnFrappeBaileysActionPerformed

    private void btnFrappeMokaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrappeMokaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10048;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnFrappeMokaActionPerformed

    private void btnFrappucinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrappucinoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10046;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnFrappucinoActionPerformed

    private void btnFrapuccinoOreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrapuccinoOreoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10047;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnFrapuccinoOreoActionPerformed

    private void btnJugoDurazPapaAguaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugoDurazPapaAguaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10063;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnJugoDurazPapaAguaActionPerformed

    private void btnJugoDurazPapaLecheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugoDurazPapaLecheActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10065;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnJugoDurazPapaLecheActionPerformed

    private void btnJugoFrutillaCONAGUAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugoFrutillaCONAGUAActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10052;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnJugoFrutillaCONAGUAActionPerformed

    private void btnJugoFrutillaCONLECHEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugoFrutillaCONLECHEActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10055;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnJugoFrutillaCONLECHEActionPerformed

    private void btnJugoPapayaCONAGUAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugoPapayaCONAGUAActionPerformed

        // aqui se envia el codigo del producto
        swcodigoProducto = 10051;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnJugoPapayaCONAGUAActionPerformed

    private void btnJugoPlatanoCONAGUAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugoPlatanoCONAGUAActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10050;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnJugoPlatanoCONAGUAActionPerformed

    private void btnJugoPlatanoCONLECHEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugoPlatanoCONLECHEActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10053;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnJugoPlatanoCONLECHEActionPerformed

    private void btnJugoMixAguaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugoMixAguaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10062;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnJugoMixAguaActionPerformed

    private void btnJugoMixLecheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugoMixLecheActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10064;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnJugoMixLecheActionPerformed

    private void btnJugoPapayaCONLECHEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugoPapayaCONLECHEActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10054;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnJugoPapayaCONLECHEActionPerformed

    private void btnNutelatteFrioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNutelatteFrioActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10060;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnNutelatteFrioActionPerformed

    private void btnPersonalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPersonalesActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10049;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnPersonalesActionPerformed

    private void btnCopaBomBomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopaBomBomActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10069;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCopaBomBomActionPerformed

    private void btnCopaChipsAhoyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopaChipsAhoyActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10070;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCopaChipsAhoyActionPerformed

    private void btnCopaOreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopaOreoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10071;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCopaOreoActionPerformed

    private void btnMalteadaChocolateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMalteadaChocolateActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10067;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnMalteadaChocolateActionPerformed

    private void btnMalteadaFresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMalteadaFresaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10066;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnMalteadaFresaActionPerformed

    private void btnMalteadaVainillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMalteadaVainillaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10068;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnMalteadaVainillaActionPerformed

    private void btnAlfajorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlfajorActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10010;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnAlfajorActionPerformed

    private void btnCroissantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCroissantActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10018;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCroissantActionPerformed

    private void btnCupcakeChocolateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCupcakeChocolateActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10075;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCupcakeChocolateActionPerformed

    private void btnCupcakeFrutillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCupcakeFrutillaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10025;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCupcakeFrutillaActionPerformed

    private void btnCupcakeVainillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCupcakeVainillaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10013;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCupcakeVainillaActionPerformed

    private void btnDonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDonaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10017;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnDonaActionPerformed

    private void btnEmpanadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpanadaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10019;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnEmpanadaActionPerformed

    private void btnGalleta12uChocolateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGalleta12uChocolateActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10023;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnGalleta12uChocolateActionPerformed

    private void btnGalleta12UNaranjaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGalleta12UNaranjaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10077;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnGalleta12UNaranjaActionPerformed

    private void btnHojaldreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHojaldreActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10011;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnHojaldreActionPerformed

    private void btnPastelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPastelActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10012;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnPastelActionPerformed

    private void btnPastel3LechesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPastel3LechesActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10073;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnPastel3LechesActionPerformed

    private void btnPastelSelvaNegraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPastelSelvaNegraActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10074;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnPastelSelvaNegraActionPerformed

    private void btnPieDuraznoUnidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPieDuraznoUnidadActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10016;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnPieDuraznoUnidadActionPerformed

    private void btnPieFrutillaUnidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPieFrutillaUnidadActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10015;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnPieFrutillaUnidadActionPerformed

    private void btnPieManzanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPieManzanaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10014;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnPieManzanaActionPerformed

    private void btnPorcionItalyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPorcionItalyActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10072;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnPorcionItalyActionPerformed

    private void btnQuequeNaranjaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuequeNaranjaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10022;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnQuequeNaranjaActionPerformed

    private void btnRolloGrandeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRolloGrandeActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10021;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnRolloGrandeActionPerformed

    private void btnRolloPequeñoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRolloPequeñoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10020;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario

        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnRolloPequeñoActionPerformed

    private void btnYemasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYemasActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10024;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnYemasActionPerformed

    private void btnTradicionalPequeñoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTradicionalPequeñoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10000;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTradicionalPequeñoActionPerformed

    private void btnPequeñoChocolateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPequeñoChocolateActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10001;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        };
    }//GEN-LAST:event_btnPequeñoChocolateActionPerformed

    private void btnTradicionalMedianoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTradicionalMedianoActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10002;

        //Esta funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTradicionalMedianoActionPerformed

    private void btnMedianoChocolateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMedianoChocolateActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10003;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnMedianoChocolateActionPerformed

    private void btnMedianoJaleaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMedianoJaleaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10004;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnMedianoJaleaActionPerformed

    private void btnFotoTortaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoTortaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10005;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnFotoTortaActionPerformed

    private void btnEspecialSelvaNegraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEspecialSelvaNegraActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10006;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnEspecialSelvaNegraActionPerformed

    private void btnEspecialMockaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEspecialMockaActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10007;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnEspecialMockaActionPerformed

    private void btnEspecialTresLechesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEspecialTresLechesActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10008;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnEspecialTresLechesActionPerformed

    private void btnTradicionalGrandeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTradicionalGrandeActionPerformed
        // aqui se envia el codigo del producto
        swcodigoProducto = 10009;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTradicionalGrandeActionPerformed

    private void tablaPedidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaPedidoKeyTyped
        // TODO add your handling code here
    }//GEN-LAST:event_tablaPedidoKeyTyped

    private void tablaPedidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaPedidoKeyPressed

    }//GEN-LAST:event_tablaPedidoKeyPressed

    //AQUI ESTAMOS VIENDO SI LÉ LO QUE PRESIONAMOS DENTRO DE LA TABLA PEDIDO
    private void tablaPedidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaPedidoKeyReleased

        //selecciona la fila de la columna 
        int fila = tablaPedido.getSelectedRow();

        if (fila >= 0) {
            //Aqui va a atrapar el nuevo valor de la cantidad
            cantidad = Double.parseDouble(tablaPedido.getValueAt(fila, 0).toString());
            System.out.println("el nuevo valor en la columna cantidad: " + cantidad);
            //El valor de la cantidad se guarda y luego:
            //se envia el numero de fila para que con ese dato supa sumar el nuevo subtotal
            nuevosubtotal(fila);
        }

    }//GEN-LAST:event_tablaPedidoKeyReleased

    private void tablaPedidoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPedidoMouseReleased

    }//GEN-LAST:event_tablaPedidoMouseReleased

    private void btnPrepararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrepararActionPerformed
        System.out.println("sumamos el numero de comandas");
        //Contabiliza el numero de comandas a preparar
        contador_comandas();
        //manda los productos productos a la otra tabla 
        //para comenzar a preparar
        System.out.println("estamos entrando a la funcion Agrega datos en la tabla en preparacion");
        agregadatosTablaPedidosEnpreparacion();
        //SALTA A EL PANEL DE COBRAR CLIENTE
        tabbedMenuCobrar.setSelectedIndex(2);
        //tabbedMenuCobrar.setEnabledAt(0, false);
        tabbedMenuCobrar.setEnabledAt(2, true);

        //Elimina todo lo que tiene en la tabla 
        //Manda a imprimir
        // ImprimirComanda imprimir = new ImprimirComanda();
        limpiartabla();
    }//GEN-LAST:event_btnPrepararActionPerformed

    /**
     * *
     *  //Pasará datos a la tabla pequeña (PEDIDOS EN PREPARACIÓN) //guardará a
     * la tabla de la base de datos
     */
    public void agregadatosTablaPedidosEnpreparacion() {

        //Instanciamos la clase registro pedidos
        //Luego nos servira para enviar todos los datos que coleccionemos
        CRUD_Pedidos_en_Preparacion guardar_pedido_alaBD = new CRUD_Pedidos_en_Preparacion();
        System.out.println("Se instanció el objeto de la clase registro pedidos: " + guardar_pedido_alaBD.toString());
        int idpedido_preparacion; //id del pedidoElegido en preparacion
        Calendario fechaHoy = new Calendario(); //fecha actual
        String fecha;
        //Para encontrar el idDelproducto haremos ciertas maniobras
        //Buscaremos igualando las palabras y el que sea igual se copiara el codigo
        int idProducto = 0;  //id producto
        double cant = 0;       // cantidad
        double venta_unitaria_bs = 0; // precio unitario bs
        double subtotal_bs = 0; // precio subtotal cant*venta unitaria
        int numero_comanda = 0; //numero de comanda

        System.out.println("""
                           Comienza a entrar al ciclo for 
                            Aqui traslada directo a la tabla pedido en preparacion""");

        for (int i = 0; i < tablaPedido.getRowCount(); i++) {
            //datos que sacaremos de la tabla pedidoElegido
            idpedido_preparacion = 0;
            fecha = fechaHoy.fechaActual();
            //Extrae la cantidad del pedidoElegido
            cant = Double.parseDouble(tablaPedido.getValueAt(i, 0).toString());
            //La logica de esta funcion es 
            /*crear una variable con el nombre de: "DTOProducto_final" para que envie a la clase "Buscador_de_Productos" 
         y entre a la funcion  #obtenerDatosId# dentro de esa funcion se envia una variable para String
         esa variable es sacada de la fila i y la columna uno. Es decir es sacado de la columna Descripcion
         una vez enviado entonces se aplica lo que esta dentro de la funcion obtener datos
         Irá a la clase "Buscador_de_Productos()" y entrará a la funcion #ObtenerDatosId#
             */
            DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatosId(tablaPedido.getValueAt(i, 1).toString());
            /*HASTA AQUI TENEMOS LOS DATOS DE LA FILA i DE LA TABLA PRINCIPAL
            
            Una vez realizado la operacion de la funcion de arriba entonces sacamos solo el id del producto */
            idProducto = selecprod.getId_producto_final();

            //Obtenemos el costo del producto ANTES QUE SE ENVÍE A GUARDAR
            Buscador_de_Productos prod = new Buscador_de_Productos();
            double costo_unitario = prod.obtenerCosto(idProducto);
            double costo_total = costo_unitario * cant; //ESTO ENVÍO A LA BASE DE DATOS

            //Extrae el precio unitario
            venta_unitaria_bs = Double.parseDouble(tablaPedido.getValueAt(i, 2).toString());
            //extrae el subtotal de cada producto seleccionado
            subtotal_bs = Double.parseDouble(tablaPedido.getValueAt(i, 3).toString());
            //Obtenemos la ganancia
            double utilidad = subtotal_bs - costo_total;
            //Saca el numero de comanda
            numero_comanda = comanda;

            //Creamos un objeto para enviar los datos al DTO 
            DTOPedido_en_preparacion pedEnPreparacion
                    = new DTOPedido_en_preparacion(idpedido_preparacion, idProducto,
                            fecha, cant, venta_unitaria_bs, subtotal_bs, numero_comanda,
                            costo_total, utilidad);
            System.out.println("vamos a leer los datos. Id predido en preparacion: " + idpedido_preparacion + "\r\n Fecha: " + fecha + "\r\n id del producto:" + idProducto
                    + "\r\n Cantidad: " + cant + "\r\n precio unitario: " + venta_unitaria_bs + "\r\n Subtotal Bs: " + subtotal_bs + " \r\n "
                    + " Comanda: " + comanda + " costo Total: " + costo_total + " utilidad: " + utilidad);

            System.out.println("AHORA VAMOS A ENVIAR LOS DATOS A LA clase Registro pedidos y a la funcion Pedido no registrado");
            //Aqui se envia toda la info acumulada para que se guarde a la base de datos CLASE: "CRUD_Pedidos_en_preparacion"
            guardar_pedido_alaBD.createPedidos_a_la_BD(pedEnPreparacion);

        }
        //PARA IMPRIMIR
        diseño_comanda();
        String datos = new String();
        //Enviamos todos los datos a la tabla siguiente
        //Pondra el pedidoElegido a la tabla de pedidos en preparacion
        for (int i = 0; i < tablaPedido.getRowCount(); i++) {
            pedEnPreparacionEnTabla[0] = "" + comanda; //NUMERO COMANDA
            pedEnPreparacionEnTabla[1] = "" + para;  //PARA: mesa llevar delivery
            pedEnPreparacionEnTabla[2] = "" + tablaPedido.getValueAt(i, 0); //CANTIDAD
            pedEnPreparacionEnTabla[3] = "" + tablaPedido.getValueAt(i, 1); //DESCRIPCION
            pedEnPreparacionEnTabla[4] = "" + tablaPedido.getValueAt(i, 3); //SUBTOTAL Bs
            //pedEnPreparacionEnTabla[5] = "" + totalbs;  //TOTAL Bs
            modelo2.addRow(pedEnPreparacionEnTabla);
        }

        //Instanciamos la clase imprimir
        // ImprimirComanda imprime = new ImprimirComanda();
        // imprime.setVisible(true);
        System.out.println("Cantidad,Descripción,P/U,Subtotal \n" + datos);
    }


    private void tablaPedidosEnPreparacionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPedidosEnPreparacionMouseClicked

    }//GEN-LAST:event_tablaPedidosEnPreparacionMouseClicked

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        System.out.println("registramos el pago");
        System.out.println("Reunimos todos los datos que requiere las tablas para la base de datos");
        //RECOLECTAMOS DATOS PARA GUARDAR EN LA TABLA pedido pagado
        double totalBs = Double.parseDouble(txtTotalCobrarBs.getText());
        String opcionPago = tipocobro();
        String carnet = txtCarnet.getText();
        String carnet2 = carnet;
        int numero_comanda = comandaPagado;
        Calendario fechaHoy = new Calendario();
        String fecha = fechaHoy.fechaActual();

        /*
        Vamos a obtener el nombre y la cantidad y el subtotal de los productos en la tabla cobro consumo
         */
        double cantidad = 0;
        String nombre;
        int id_producto = 0;
        double costo_unitario = 0;
        double costo_pedido = 0; //Total del costo del pedido
        Buscador_de_Productos prod = new Buscador_de_Productos();
        for (int i = 0; i < tablaCobroConsumo.getRowCount(); i++) {
            cantidad = Double.parseDouble(tablaCobroConsumo.getValueAt(i, 0).toString());
            nombre = tablaCobroConsumo.getValueAt(i, 1).toString();
            id_producto = prod.obtenerDatosIdV2(nombre);
            costo_unitario = prod.obtenerCosto(id_producto);

            costo_pedido = costo_pedido + (cantidad * costo_unitario);
        }

        double utilidad_pedido = totalBs - costo_pedido;
        //Enviamos al CRUD PEDIDO PAGADO
        CRUD_Pedido_Pagado guardar = new CRUD_Pedido_Pagado();
        guardar.guardaPedidoPagado(totalBs, opcionPago, carnet, numero_comanda, fecha, costo_pedido, utilidad_pedido);

        //RECOLECTAMOS DATOS PARA GUARDAR EN LA TABLA cliente
        //tengo ci persona que es el carnet
        int contador = Integer.parseInt(txtConsumosCliente.getText()) + 1;

        //ENVIAMOS AL CRUD PEDIDO PAGADO
        guardar.guardaContadorCLiente(carnet2, contador);

        //SALTA A EL PANEL DE COBRAR CLIENTE
        tabbedMenuCobrar.setSelectedIndex(2);
        //tabbedMenuCobrar.setEnabledAt(0, false);
        tabbedMenuCobrar.setEnabledAt(2, true);
        // PARA IMPRIMIR
       // diseñoRecibo();
        limpiarDatos();
    }//GEN-LAST:event_btnRegistrarActionPerformed

    /**
     * *
     * diseño para imprimir RECIBO
     
    public void diseñoRecibo() {

        /*
        ********************************
   *PASTELERÌA ITALY* 
Dirección (Casa Matríz): Av. Alfonso Ugarte & José Arzabe El Alto. (A una cuadra del teleférico Azul Estación Plaza Libertad) 
*********************************
Fecha: 11/07/2022 - 11:22:11
-----------------------------------------
DETALLE 
-----------------------------------------
 1 CAPPUCCINO FBs.11
.......................................................
=======================
Total:	Bs.11.0.-
-----------------------------------------
-----------------------------------------

Carnet:__________________

Nombres y Apellido:________

________________________

Firma:___________________
¿Te gustaría ser parte del club de clientes preferentes? 
Danos tu WhatsApp y sé parte del club: ________________________
**********************************
*Tu propina motiva mi trabajo*
(^_^)Si la atención fue buena, deja tu propina. (^_^)
**********************************
         */
    /*    txtComanda.setText("");
        txtComanda.setText(txtComanda.getText() + "********************************\n");
        txtComanda.setText(txtComanda.getText() + "   *PASTELERÍA ITALY* \n");
        txtComanda.setText(txtComanda.getText() + "Dirección (Casa Matríz): Av. Alfonso Ugarte & José Arzabe ");
        txtComanda.setText(txtComanda.getText() + "El Alto.");
        txtComanda.setText(txtComanda.getText() + "\n");
        txtComanda.setText(txtComanda.getText() + "********************************\n");
        txtComanda.setText(txtComanda.getText() + "********************************\n");
        
        txtComanda.setText(txtComanda.getText() + "******CONSUMO******\n");

        //Obtengo el tiempo 
        Date dd = new Date();
        SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timef = new SimpleDateFormat("hh:mm:ss");
        String date = datef.format(dd);
        String time = timef.format(dd);
        txtComanda.setText(txtComanda.getText() + "Fecha: " + date + " - " + time + "\n");
        txtComanda.setText(txtComanda.getText() + "---------------------------------------\n");
        txtComanda.setText(txtComanda.getText() + "DETALLE \n");
        txtComanda.setText(txtComanda.getText() + "---------------------------------------\n");
        //agregamos el jtable de los productos
        DefaultTableModel modeloTablaCobroConsumo = (DefaultTableModel) tablaCobroConsumo.getModel();
        int filas = tablaCobroConsumo.getRowCount();
        System.out.println("filas " + filas);
        for (int i = 0; i < filas; i++) {
            System.out.println("for de los pedidos para imprimir");
            String cant = modeloTablaCobroConsumo.getValueAt(i, 0).toString(); // cantidad
            String detalle = modeloTablaCobroConsumo.getValueAt(i, 1).toString(); //item
            double precio = Double.parseDouble(modeloTablaCobroConsumo.getValueAt(i, 3).toString()); // cal price

            txtComanda.setText(txtComanda.getText() + " " + cant + " " + detalle.toUpperCase() + "\t" + " Bs." + (int) precio + "\n");

            txtComanda.setText(txtComanda.getText() + "......................................................\n");

        }
        txtComanda.setText(txtComanda.getText() + "=======================\n");
        txtComanda.setText(txtComanda.getText() + "Total:" + "\t" + "Bs." + txtTotalCobrarBs.getText() + ".-\n");
        txtComanda.setText(txtComanda.getText() + "-----------------------------------------\n");
        
        
        txtComanda.setText(txtComanda.getText() + "¿QUIERES SER PARTE DEL CLUB DE CLIENTES PREFERENTES?\n");
        
        txtComanda.setText(txtComanda.getText() + "\n");
        txtComanda.setText(txtComanda.getText() + "PREGÚNTAME ¿CÓMO?\n");
        txtComanda.setText(txtComanda.getText() + "=======================\n");
        txtComanda.setText(txtComanda.getText() + "**GRACIAS POR LA PREFERENCIA****\n");
        

    }
*/
    /**
     * *
     * Limpia toda la tabla y caja de textos del espacio donde se cobra
     */
    public void limpiarDatos() {
        int fila = tablaCobroConsumo.getRowCount();
        for (int i = fila - 1; i >= 0; i--) {
            modelo3.removeRow(i);
        }
        txtNombre.setText("");
        txtApellido.setText("");
        txtCarnet.setText("");
        txtWhatsapp.setText("");
        txtCambio.setText("");
        txtEfectivo.setText("");
        txtTotalCobrarBs.setText("");
        txtConsumosCliente.setText("");
        btnRegistrar.setEnabled(false);
    }

    private void tablaPedidoAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tablaPedidoAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaPedidoAncestorAdded

    private void btnGalletas12uCocoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGalletas12uCocoMouseEntered
        btnGalletas12uCoco.setBackground(Color.cyan);

    }//GEN-LAST:event_btnGalletas12uCocoMouseEntered

    private void btnGalletas12uCocoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGalletas12uCocoMouseExited
        // TODO add your handling code here:
        btnGalletas12uCoco.setBackground(Color.white);

    }//GEN-LAST:event_btnGalletas12uCocoMouseExited

    private void btnGalletas12uCocoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGalletas12uCocoActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10082;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnGalletas12uCocoActionPerformed

    private void btnGalletas12uVainillaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGalletas12uVainillaMouseEntered
        // TODO add your handling code here:
        btnGalletas12uVainilla.setBackground(Color.cyan);
    }//GEN-LAST:event_btnGalletas12uVainillaMouseEntered

    private void btnGalletas12uVainillaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGalletas12uVainillaMouseExited
        // TODO add your handling code here:
        btnGalletas12uVainilla.setBackground(Color.white);
    }//GEN-LAST:event_btnGalletas12uVainillaMouseExited

    private void btnGalletas12uVainillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGalletas12uVainillaActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10083;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnGalletas12uVainillaActionPerformed

    private void btnPieDuraznoEnteroMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPieDuraznoEnteroMouseEntered
        // TODO add your handling code here:
        btnPieDuraznoEntero.setBackground(Color.cyan);
    }//GEN-LAST:event_btnPieDuraznoEnteroMouseEntered

    private void btnPieDuraznoEnteroMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPieDuraznoEnteroMouseExited
        // TODO add your handling code here:
        btnPieDuraznoEntero.setBackground(Color.white);
    }//GEN-LAST:event_btnPieDuraznoEnteroMouseExited

    private void btnPieDuraznoEnteroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPieDuraznoEnteroActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10084;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnPieDuraznoEnteroActionPerformed

    private void btnPieFrutillaEnteroMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPieFrutillaEnteroMouseEntered
        // TODO add your handling code here:
        btnPieFrutillaEntero.setBackground(Color.cyan);
    }//GEN-LAST:event_btnPieFrutillaEnteroMouseEntered

    private void btnPieFrutillaEnteroMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPieFrutillaEnteroMouseExited
        // TODO add your handling code here:
        btnPieFrutillaEntero.setBackground(Color.white);
    }//GEN-LAST:event_btnPieFrutillaEnteroMouseExited

    private void btnPieFrutillaEnteroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPieFrutillaEnteroActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10085;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnPieFrutillaEnteroActionPerformed

    private void btnTorta50p2pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTorta50p2pActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10086;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTorta50p2pActionPerformed

    private void btnTorta50p2pMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta50p2pMouseExited
        // TODO add your handling code here:
        btnTorta50p2p.setBackground(Color.white);
    }//GEN-LAST:event_btnTorta50p2pMouseExited

    private void btnTorta50p2pMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta50p2pMouseEntered
        // TODO add your handling code here:
        btnTorta50p2p.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTorta50p2pMouseEntered

    private void btnTorta100p5pMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta100p5pMouseEntered
        // TODO add your handling code here:
        btnTorta100p5p.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTorta100p5pMouseEntered

    private void btnTorta100p5pMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta100p5pMouseExited
        // TODO add your handling code here:
        btnTorta100p5p.setBackground(Color.white);
    }//GEN-LAST:event_btnTorta100p5pMouseExited

    private void btnTorta100p5pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTorta100p5pActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10097;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTorta100p5pActionPerformed

    private void btnTorta150p6pMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta150p6pMouseEntered
        // TODO add your handling code here:
        btnTorta150p6p.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTorta150p6pMouseEntered

    private void btnTorta150p6pMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta150p6pMouseExited
        // TODO add your handling code here:
        btnTorta150p6p.setBackground(Color.white);
    }//GEN-LAST:event_btnTorta150p6pMouseExited

    private void btnTorta150p6pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTorta150p6pActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10099;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTorta150p6pActionPerformed

    private void btnTorta300p15pMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta300p15pMouseEntered
        // TODO add your handling code here:
        btnTorta300p15p.setBackground(Color.white);
    }//GEN-LAST:event_btnTorta300p15pMouseEntered

    private void btnTorta300p15pMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta300p15pMouseExited
        // TODO add your handling code here:
        btnTorta300p15p.setBackground(Color.white);
    }//GEN-LAST:event_btnTorta300p15pMouseExited

    private void btnTorta300p15pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTorta300p15pActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10101;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTorta300p15pActionPerformed

    private void btnTorta500p25pMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta500p25pMouseEntered
        // TODO add your handling code here:
        btnTorta500p25p.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTorta500p25pMouseEntered

    private void btnTorta500p25pMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta500p25pMouseExited
        // TODO add your handling code here:
        btnTorta500p25p.setBackground(Color.white);
    }//GEN-LAST:event_btnTorta500p25pMouseExited

    private void btnTorta500p25pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTorta500p25pActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10103;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTorta500p25pActionPerformed

    private void btnFotoTortaPequeñaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoTortaPequeñaMouseEntered
        // TODO add your handling code here:
        btnFotoTortaPequeña.setBackground(Color.cyan);
    }//GEN-LAST:event_btnFotoTortaPequeñaMouseEntered

    private void btnFotoTortaPequeñaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoTortaPequeñaMouseExited
        // TODO add your handling code here:
        btnFotoTortaPequeña.setBackground(Color.white);
    }//GEN-LAST:event_btnFotoTortaPequeñaMouseExited

    private void btnFotoTortaPequeñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoTortaPequeñaActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10104;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnFotoTortaPequeñaActionPerformed

    private void btnFotoTortaGrandeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoTortaGrandeMouseEntered
        // TODO add your handling code here:
        btnFotoTortaGrande.setBackground(Color.cyan);
    }//GEN-LAST:event_btnFotoTortaGrandeMouseEntered

    private void btnFotoTortaGrandeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoTortaGrandeMouseExited
        // TODO add your handling code here:
        btnFotoTortaGrande.setBackground(Color.white);
    }//GEN-LAST:event_btnFotoTortaGrandeMouseExited

    private void btnFotoTortaGrandeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoTortaGrandeActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10106;
        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);
        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {
            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnFotoTortaGrandeActionPerformed

    private void tablaCobroConsumoAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tablaCobroConsumoAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaCobroConsumoAncestorAdded

    private void tablaCobroConsumoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaCobroConsumoMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaCobroConsumoMouseReleased

    private void tablaCobroConsumoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaCobroConsumoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaCobroConsumoKeyPressed

    private void tablaCobroConsumoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaCobroConsumoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaCobroConsumoKeyReleased

    private void tablaCobroConsumoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaCobroConsumoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaCobroConsumoKeyTyped

    private void txtEfectivoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEfectivoKeyTyped
        // Vamos a validar a usar solo numeros
        int ascci = evt.getKeyChar();
        String numdec = txtEfectivo.getText();
        ValidarLetrasyNumeros.numerosReales(ascci, numdec, evt);


    }//GEN-LAST:event_txtEfectivoKeyTyped

    private void txtEfectivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEfectivoKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtEfectivoKeyPressed

    private void txtEfectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEfectivoActionPerformed
        // TODO add your handling code here:
        cambioBs();
    }//GEN-LAST:event_txtEfectivoActionPerformed

    private void radioTipoTarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioTipoTarjetaActionPerformed
        // TODO add your handling code here:
        txtEfectivo.setEditable(false);
        txtEfectivo.setText(txtTotalCobrarBs.getText());
        txtCambio.setText("");


    }//GEN-LAST:event_radioTipoTarjetaActionPerformed

    private void radioTipoEfectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioTipoEfectivoActionPerformed
        // TODO add your handling code here:
        txtEfectivo.setEditable(true);
        txtEfectivo.setText("");
    }//GEN-LAST:event_radioTipoEfectivoActionPerformed

    private void txtCarnetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCarnetKeyTyped
        // TODO add your handling code here:
        //Solo va a leer numeros con una coma decimal
        int ascci = evt.getKeyChar();
        String numdec = txtCarnet.getText();
        ValidarLetrasyNumeros.numerosEnteros(ascci, numdec, evt);
    }//GEN-LAST:event_txtCarnetKeyTyped

    private void txtWhatsappKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWhatsappKeyTyped
        // TODO add your handling code here:
        //Solo va a leer numeros con una coma decimal
        int ascci = evt.getKeyChar();
        String numdec = txtWhatsapp.getText();
        ValidarLetrasyNumeros.numerosEnteros(ascci, numdec, evt);
    }//GEN-LAST:event_txtWhatsappKeyTyped

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped
        // TODO add your handling code here:
        ValidarLetrasyNumeros.letraSoloMayuscula(evt);
    }//GEN-LAST:event_txtApellidoKeyTyped

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        // TODO add your handling code here:
        ValidarLetrasyNumeros.letraSoloMayuscula(evt);
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtCarnetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCarnetActionPerformed
        String carnet = txtCarnet.getText();
        // Buscar si existe el cliente
        DTOPersona persona = new Buscador_de_Personas().buscarPersona(carnet);
        DTOCliente cliente = new Buscador_de_Personas().buscarCliente(carnet);

        System.out.println("Ahora procedemos a anotar si existe un dato guardado");
        if (cliente != null) {
            System.out.println(" Exito! se encontró datos "
                    + "Nombre: " + persona.getNombre() + " Apellido: "
                    + persona.getApellido() + " Celular: " + persona.getCelular());
            txtNombre.setText(persona.getNombre());
            txtApellido.setText(persona.getApellido());
            txtWhatsapp.setText(persona.getCelular());

            //Bloqueamos el boton de añadir nuevo cliente
            btnGuardaNuevoCliente.setEnabled(false);
        } else {
            System.out.println("No encontró cliente, ENTONCES CREAMOS NUEVO CLIENTE");
            JOptionPane.showMessageDialog(null, " Escriba nuevamente el CI  \n ó Registre al nuevo cliente");
            //Procedemos a limpiar las cajas de texto
            txtCarnet.setText("");
            txtNombre.setText("");
            txtApellido.setText("");
            txtWhatsapp.setText("");

            btnGuardaNuevoCliente.setEnabled(true);
        }

        System.out.println("Ahora vamos anotar los consumos hizo el cliente");
        DTOCliente consumos = new Buscador_de_Personas().buscarConsumos(carnet);
        if (consumos != null) {
            System.out.println(" Exito! se encontró cantidad maxima de consumos "
                    + "Consumo maximo: " + consumos.getContador_compras_hechas());
            txtConsumosCliente.setText("" + consumos.getContador_compras_hechas());
        }
    }//GEN-LAST:event_txtCarnetActionPerformed

    private void btnGuardaNuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaNuevoClienteActionPerformed
        //GUARDAMOS AL NUEVO CLIENTE

        String carnet = txtCarnet.getText();
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String celular = txtWhatsapp.getText();
        Calendario fechaHoy = new Calendario();
        String fecha = fechaHoy.fechaActual();
        if (!apellido.isEmpty() && !carnet.isEmpty() || !nombre.isEmpty() && !carnet.isEmpty()) {
            System.out.println("creamos datos personales del cliente nuevo");
            CRUD_Personas nuevo = new CRUD_Personas();
            nuevo.createNuevaPersona(carnet, nombre, apellido, celular, fecha);
            System.out.println("Creamos al nuevo cliente");
            CRUD_Personas cliente_nuevo = new CRUD_Personas();
            cliente_nuevo.createNuevoCliente(carnet);
            JOptionPane.showMessageDialog(null, "Cliente guardado");
        }
        btnGuardaNuevoCliente.setEnabled(false);
    }//GEN-LAST:event_btnGuardaNuevoClienteActionPerformed

    private void menuCierreTurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCierreTurnoActionPerformed


    }//GEN-LAST:event_menuCierreTurnoActionPerformed

    private void menuCierreTurnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCierreTurnoMouseClicked
        // TODO add your handling code here:
        //Cierre del dia
        CierreTurno cierre = new CierreTurno();
        cierre.setVisible(true);

    }//GEN-LAST:event_menuCierreTurnoMouseClicked

    private void submenuTopProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submenuTopProductosActionPerformed
        reporte_Productos_Finales productos = new reporte_Productos_Finales();
        productos.setVisible(true);
    }//GEN-LAST:event_submenuTopProductosActionPerformed

    private void subMenuVentasDiariasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuVentasDiariasActionPerformed
        // TODO add your handling code here:
        reporte_Ventas_Diarias ventas_diarias = new reporte_Ventas_Diarias();
        ventas_diarias.setVisible(true);
    }//GEN-LAST:event_subMenuVentasDiariasActionPerformed

    private void subMenuVentasUnDiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuVentasUnDiaActionPerformed
        // TODO add your handling code here:
        reporte_Ventas_enUn_Dia ventas_un_dia = new reporte_Ventas_enUn_Dia();
        ventas_un_dia.setVisible(true);
    }//GEN-LAST:event_subMenuVentasUnDiaActionPerformed

    private void menuComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuComprasActionPerformed
        // TODO add your handling code here:
        reporte_Compras compras = new reporte_Compras();
        compras.setVisible(true);
    }//GEN-LAST:event_menuComprasActionPerformed

    private void btnCapuccinoBayliesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCapuccinoBayliesMouseEntered
        // TODO add your handling code here:
        btnCapuccinoBaylies.setBackground(Color.cyan);
    }//GEN-LAST:event_btnCapuccinoBayliesMouseEntered

    private void btnCapuccinoBayliesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCapuccinoBayliesMouseExited
        // TODO add your handling code here:
        btnCapuccinoBaylies.setBackground(Color.white);
    }//GEN-LAST:event_btnCapuccinoBayliesMouseExited

    private void btnCapuccinoBayliesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapuccinoBayliesActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10045;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnCapuccinoBayliesActionPerformed

    private void btnMateAnisMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMateAnisMouseEntered
        // TODO add your handling code here:
        btnMateAnis.setBackground(Color.cyan);
    }//GEN-LAST:event_btnMateAnisMouseEntered

    private void btnMateAnisMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMateAnisMouseExited
        // TODO add your handling code here:
        btnMateAnis.setBackground(Color.white);
    }//GEN-LAST:event_btnMateAnisMouseExited

    private void btnMateAnisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMateAnisActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10111;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnMateAnisActionPerformed

    private void btnMateCocaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMateCocaMouseEntered
        // TODO add your handling code here:
        btnMateCoca.setBackground(Color.cyan);
    }//GEN-LAST:event_btnMateCocaMouseEntered

    private void btnMateCocaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMateCocaMouseExited
        // TODO add your handling code here:
        btnMateCoca.setBackground(Color.white);
    }//GEN-LAST:event_btnMateCocaMouseExited

    private void btnMateCocaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMateCocaActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10110;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnMateCocaActionPerformed

    private void btnMateManzanillaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMateManzanillaMouseEntered
        // TODO add your handling code here:
        btnMateManzanilla.setBackground(Color.cyan);
    }//GEN-LAST:event_btnMateManzanillaMouseEntered

    private void btnMateManzanillaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMateManzanillaMouseExited
        // TODO add your handling code here:
        btnMateManzanilla.setBackground(Color.white);
    }//GEN-LAST:event_btnMateManzanillaMouseExited

    private void btnMateManzanillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMateManzanillaActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10112;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnMateManzanillaActionPerformed

    private void btnTorta50pCuadradoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta50pCuadradoMouseEntered
        // TODO add your handling code here:
        btnTorta50pCuadrado.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTorta50pCuadradoMouseEntered

    private void btnTorta50pCuadradoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta50pCuadradoMouseExited
        // TODO add your handling code here:
        btnTorta50pCuadrado.setBackground(Color.white);
    }//GEN-LAST:event_btnTorta50pCuadradoMouseExited

    private void btnTorta50pCuadradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTorta50pCuadradoActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10087;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTorta50pCuadradoActionPerformed

    private void btnTorta100pCuadradoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta100pCuadradoMouseEntered
        // TODO add your handling code here:
        btnTorta100pCuadrado.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTorta100pCuadradoMouseEntered

    private void btnTorta100pCuadradoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta100pCuadradoMouseExited
        btnTorta100pCuadrado.setBackground(Color.white);
    }//GEN-LAST:event_btnTorta100pCuadradoMouseExited

    private void btnTorta100pCuadradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTorta100pCuadradoActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10089;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTorta100pCuadradoActionPerformed

    private void btnTorta200p10pMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta200p10pMouseEntered
        // TODO add your handling code here:
        btnTorta200p10p.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTorta200p10pMouseEntered

    private void btnTorta200p10pMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta200p10pMouseExited
        // TODO add your handling code here:
        btnTorta200p10p.setBackground(Color.white);
    }//GEN-LAST:event_btnTorta200p10pMouseExited

    private void btnTorta200p10pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTorta200p10pActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10091;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTorta200p10pActionPerformed

    private void btnTorta400p22pMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta400p22pMouseEntered
        // TODO add your handling code here:
        btnTorta400p22p.setBackground(Color.cyan);
    }//GEN-LAST:event_btnTorta400p22pMouseEntered

    private void btnTorta400p22pMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTorta400p22pMouseExited
        // TODO add your handling code here:
        btnTorta400p22p.setBackground(Color.white);
    }//GEN-LAST:event_btnTorta400p22pMouseExited

    private void btnTorta400p22pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTorta400p22pActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10093;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnTorta400p22pActionPerformed

    private void btnFotoTortaMedianaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoTortaMedianaMouseEntered
        // TODO add your handling code here:
        btnFotoTortaMediana.setBackground(Color.cyan);
    }//GEN-LAST:event_btnFotoTortaMedianaMouseEntered

    private void btnFotoTortaMedianaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoTortaMedianaMouseExited
        // TODO add your handling code here:
        btnFotoTortaMediana.setBackground(Color.white);
    }//GEN-LAST:event_btnFotoTortaMedianaMouseExited

    private void btnFotoTortaMedianaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoTortaMedianaActionPerformed
        // TODO add your handling code here:
        // aqui se envia el codigo del producto
        swcodigoProducto = 10105;

        //La siguiente funcion es clave porque va a enviar el codigo del producto al paquete "modelo"
        //de alli va a buscar el producto y lo va a retornar con nuevos datos
        // los cuales escogeremos solo la descripcion y el precio unitario
        DTO.DTOProducto_final selecprod = new Buscador_de_Productos().obtenerDatos(swcodigoProducto);

        //Aqui va a mostrar los datos del codigo del
        //producto que enocntro
        if (selecprod != null) {

            try {
                System.out.println("Se encontro el codigo y ahora se llena a la tabla pedidos");
                descripcion = "" + selecprod.getNombre_producto();
                precioUnitario = selecprod.getVenta_bs();
                //Agrega los datos a la tabla de pedidos funcion: agregadatosTablaPedidos();
                agregadatosTablaPedidos();
            } catch (Exception e) {
                System.out.println("Ocurrio un error y no encontro el codigo");
            }
        }
    }//GEN-LAST:event_btnFotoTortaMedianaActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        //Vamos a imprimir la comanda
        try {

            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(OrientationRequested.PORTRAIT);
            aset.add(MediaSizeName.INVOICE);
            // job.print( aset);
            PrinterJob imprimir = PrinterJob.getPrinterJob();
            imprimir.setPrintable(this);
            boolean top = imprimir.printDialog();
            if (top) {
                imprimir.print(aset);
            }
        } catch (PrinterException e) {
            System.out.println("ERROR AL IMPRIMIR " + e);
        }
        // dispose();
    }//GEN-LAST:event_btnImprimirActionPerformed

    /**
     * *
     *
     * aqui vamos a usar para poder imprimir
     */
    @Override
    public int print(Graphics graf, PageFormat pageFor, int index) throws PrinterException {
        if (index > 0) {
            //aqui es para que solo la primera pagina se imprime
            return NO_SUCH_PAGE;
        }
        //aqui vamos a definir lo que vamos a imprimir 
        //vamos a imprimir solo el JPanel que se llama panelModelo Comanda
        Graphics2D hub = (Graphics2D) graf;
        //tamaño del marco se le da un formato a la pagina
        hub.translate(pageFor.getImageableX() + 0, pageFor.getImageableY() + 0);
        //escala de pagina En este caso se va a imprimir el tmaaño lo q 
        //he diseñado mi Jpanel
        hub.scale(1.0, 1.0);

        txtComanda.print(graf);
        return PAGE_EXISTS;
    }

    /**
     * *
     * diseño para imprimir
     */
    public void diseño_comanda() {

       // txtComanda.setText(txtComanda.getText() + "-----------------------------------------\n");
       // txtComanda.setText("   *CAFETERÍA ITALY* \n");
       txtComanda.setText("");
        txtComanda.setText(txtComanda.getText() + "********************************\n");
        txtComanda.setText(txtComanda.getText() + "   *PASTELERÍA ITALY* \n");
        txtComanda.setText(txtComanda.getText() + "Dirección (Casa Matríz): Av. Alfonso Ugarte & José Arzabe ");
        txtComanda.setText(txtComanda.getText() + "El Alto. ");
        txtComanda.setText(txtComanda.getText() + "\n");
        txtComanda.setText(txtComanda.getText() + "********************************\n");

        //Obtengo el tiempo 
        Date dd = new Date();
        SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timef = new SimpleDateFormat("hh:mm:ss");
        String date = datef.format(dd);
        String time = timef.format(dd);
        txtComanda.setText(txtComanda.getText() + "FECHA: " + date + " - " + time + "\n");
        txtComanda.setText(txtComanda.getText() + "COMANDA: " + comanda + "\n");
        txtComanda.setText(txtComanda.getText() + "PARA " + para.toUpperCase() + "\n");
        txtComanda.setText(txtComanda.getText() + "-----------------------------------------\n");
        txtComanda.setText(txtComanda.getText() + "DETALLE \n");
        txtComanda.setText(txtComanda.getText() + "-----------------------------------------\n");
        //agregamos el jtable de los productos
        DefaultTableModel modeloTablaPedido = (DefaultTableModel) tablaPedido.getModel();
        int filas = tablaPedido.getRowCount();
        System.out.println("filas " + filas);
        for (int i = 0; i < filas; i++) {
            System.out.println("for de los pedidos para imprimir");
            String cant = modeloTablaPedido.getValueAt(i, 0).toString(); // cantidad
            String detalle = modeloTablaPedido.getValueAt(i, 1).toString(); //item
            double precio = Double.parseDouble(modeloTablaPedido.getValueAt(i, 3).toString()); // cal price

            txtComanda.setText(txtComanda.getText() + " " + cant + " " + detalle.toUpperCase() + "\t" + ".Bs." + (int) precio + "\n");

            txtComanda.setText(txtComanda.getText() + "..........................................................\n");

        }
        txtComanda.setText(txtComanda.getText() + "=======================\n");
        txtComanda.setText(txtComanda.getText() + "Total:" + "\t" + "Bs." + (int) totalbs + ".-\n");
        txtComanda.setText(txtComanda.getText() + "-----------------------------------------\n");

       // txtComanda.setText(txtComanda.getText() + "-----------------------------------------\n");
        
        //txtComanda.setText(txtComanda.getText() + "..................DATOS:..................\n");
        
        //txtComanda.setText(txtComanda.getText() + "\n");
        txtComanda.setText(txtComanda.getText() + "¿Te gustaría ser parte del club de clientes preferentes? \n");
       
        txtComanda.setText(txtComanda.getText() + "Carnet: ______________________\n");
        txtComanda.setText(txtComanda.getText() + "\n");
        txtComanda.setText(txtComanda.getText() + "Nombres y Apellido:\n ");
        txtComanda.setText(txtComanda.getText() + "\n ______________________\n");
        txtComanda.setText(txtComanda.getText() + "\n");
       // txtComanda.setText(txtComanda.getText() + "Firma: _______________________\n");
        txtComanda.setText(txtComanda.getText() + "WhatsApp: _____________________\n");
        
        txtComanda.setText(txtComanda.getText() + "\n");
        txtComanda.setText(txtComanda.getText() + "******************************\n");
        txtComanda.setText(txtComanda.getText() + "(^_^)Si la atención fue buena, deja tu propina. (^_^)\n");
        txtComanda.setText(txtComanda.getText() + "******************************\n");
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelSubMenuProductos;
    private javax.swing.JButton btnAffogato;
    private javax.swing.JButton btnAlfajor;
    private javax.swing.JButton btnAmericano;
    private javax.swing.JButton btnCafeBomBom;
    private javax.swing.JButton btnCafeIrlandes;
    private javax.swing.JButton btnCappuccinoFrio;
    private javax.swing.JButton btnCapuccinoBaylies;
    private javax.swing.JButton btnCapuccinoCompleto;
    private javax.swing.JButton btnCapuccinoSimple;
    private javax.swing.JButton btnCapuccinoVainilla;
    private javax.swing.JButton btnChaiLate;
    private javax.swing.JButton btnChocolate;
    private javax.swing.JButton btnChocolateLeche;
    private javax.swing.JButton btnChocolateMavabisco;
    private javax.swing.JButton btnCopaBomBom;
    private javax.swing.JButton btnCopaChipsAhoy;
    private javax.swing.JButton btnCopaOreo;
    private javax.swing.JButton btnCroissant;
    private javax.swing.JButton btnCupcakeChocolate;
    private javax.swing.JButton btnCupcakeFrutilla;
    private javax.swing.JButton btnCupcakeVainilla;
    private javax.swing.JButton btnDona;
    private javax.swing.JButton btnEmpanada;
    private javax.swing.JButton btnEspecialMocka;
    private javax.swing.JButton btnEspecialSelvaNegra;
    private javax.swing.JButton btnEspecialTresLeches;
    private javax.swing.JButton btnFotoTorta;
    private javax.swing.JButton btnFotoTortaGrande;
    private javax.swing.JButton btnFotoTortaMediana;
    private javax.swing.JButton btnFotoTortaPequeña;
    private javax.swing.JButton btnFrappeBaileys;
    private javax.swing.JButton btnFrappeMoka;
    private javax.swing.JButton btnFrappucino;
    private javax.swing.JButton btnFrapuccinoOreo;
    private javax.swing.JButton btnGalleta12UNaranja;
    private javax.swing.JButton btnGalleta12uChocolate;
    private javax.swing.JButton btnGalletas12uCoco;
    private javax.swing.JButton btnGalletas12uVainilla;
    private javax.swing.JButton btnGuardaNuevoCliente;
    private javax.swing.JButton btnHojaldre;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnJugoDurazPapaAgua;
    private javax.swing.JButton btnJugoDurazPapaLeche;
    private javax.swing.JButton btnJugoFrutillaCONAGUA;
    private javax.swing.JButton btnJugoFrutillaCONLECHE;
    private javax.swing.JButton btnJugoMixAgua;
    private javax.swing.JButton btnJugoMixLeche;
    private javax.swing.JButton btnJugoPapayaCONAGUA;
    private javax.swing.JButton btnJugoPapayaCONLECHE;
    private javax.swing.JButton btnJugoPlatanoCONAGUA;
    private javax.swing.JButton btnJugoPlatanoCONLECHE;
    private javax.swing.JButton btnLatte;
    private javax.swing.JButton btnLeche;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnMalteadaChocolate;
    private javax.swing.JButton btnMalteadaFresa;
    private javax.swing.JButton btnMalteadaVainilla;
    private javax.swing.JButton btnMateAnis;
    private javax.swing.JButton btnMateCoca;
    private javax.swing.JButton btnMateManzanilla;
    private javax.swing.JButton btnMedianoChocolate;
    private javax.swing.JButton btnMedianoJalea;
    private javax.swing.JButton btnMochaccino;
    private javax.swing.JButton btnNuteLatte;
    private javax.swing.JButton btnNutelatteFrio;
    private javax.swing.JButton btnPastel;
    private javax.swing.JButton btnPastel3Leches;
    private javax.swing.JButton btnPastelSelvaNegra;
    private javax.swing.JButton btnPequeñoChocolate;
    private javax.swing.JButton btnPersonales;
    private javax.swing.JButton btnPieDuraznoEntero;
    private javax.swing.JButton btnPieDuraznoUnidad;
    private javax.swing.JButton btnPieFrutillaEntero;
    private javax.swing.JButton btnPieFrutillaUnidad;
    private javax.swing.JButton btnPieManzana;
    private javax.swing.JButton btnPorcionItaly;
    private javax.swing.JButton btnPreparar;
    private javax.swing.JButton btnQuequeNaranja;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnRolloGrande;
    private javax.swing.JButton btnRolloPequeño;
    private javax.swing.JButton btnSubmarino;
    private javax.swing.JButton btnSubmarinoBonBom;
    private javax.swing.JButton btnSubmarinoOreo;
    private javax.swing.JButton btnTe;
    private javax.swing.JButton btnToddy;
    private javax.swing.JButton btnTorta100p5p;
    private javax.swing.JButton btnTorta100pCuadrado;
    private javax.swing.JButton btnTorta150p6p;
    private javax.swing.JButton btnTorta200p10p;
    private javax.swing.JButton btnTorta300p15p;
    private javax.swing.JButton btnTorta400p22p;
    private javax.swing.JButton btnTorta500p25p;
    private javax.swing.JButton btnTorta50p2p;
    private javax.swing.JButton btnTorta50pCuadrado;
    private javax.swing.JButton btnTradicionalGrande;
    private javax.swing.JButton btnTradicionalMediano;
    private javax.swing.JButton btnTradicionalPequeño;
    private javax.swing.JButton btnTrimate;
    private javax.swing.JButton btnXpresso;
    private javax.swing.JButton btnYemas;
    private javax.swing.ButtonGroup grupoTipoPago;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblComanda;
    private javax.swing.JLabel lblDELIVERY;
    private javax.swing.JLabel lblDelivery;
    private javax.swing.JLabel lblLLEVAR;
    private javax.swing.JLabel lblLlevar;
    private javax.swing.JLabel lblMESA;
    private javax.swing.JLabel lblMESA1;
    private javax.swing.JLabel lblMESA2;
    private javax.swing.JLabel lblMESA4;
    private javax.swing.JLabel lblMesa;
    private javax.swing.JMenu menuCierreTurno;
    private javax.swing.JMenuItem menuCompras;
    private javax.swing.JMenu menuReportes;
    private javax.swing.JMenu menuSalir;
    private javax.swing.JMenu menuTopTen;
    private javax.swing.JMenu menuVentas;
    private javax.swing.JPanel panelBebidasCalientes;
    private javax.swing.JPanel panelBebidasFrias;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelCobrarCliente;
    private javax.swing.JPanel panelHeladería;
    private javax.swing.JPanel panelMasitas;
    private javax.swing.JPanel panelNumeroComanda;
    private javax.swing.JPanel panelPago;
    private javax.swing.JPanel panelPedidosNOregistrados;
    private javax.swing.JPanel panelTipo;
    private javax.swing.JPanel panelTipoPago;
    private javax.swing.JPanel panelTortas;
    private javax.swing.JPopupMenu popupTablaPedido;
    private javax.swing.JPopupMenu popupTablaPreparacion;
    private javax.swing.JRadioButton radioTipoEfectivo;
    private javax.swing.JRadioButton radioTipoTarjeta;
    private javax.swing.JTabbedPane subMenu;
    private javax.swing.JMenuItem subMenuVentasDiarias;
    private javax.swing.JMenuItem subMenuVentasUnDia;
    private javax.swing.JMenuItem submenuComprasGastos;
    private javax.swing.JMenuItem submenuTopClientes;
    private javax.swing.JMenuItem submenuTopProductos;
    private javax.swing.JTabbedPane tabbedMenuCobrar;
    private javax.swing.JTable tablaCobroConsumo;
    public javax.swing.JTable tablaPedido;
    private javax.swing.JTable tablaPedidosEnPreparacion;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCambio;
    private javax.swing.JTextField txtCarnet;
    private javax.swing.JTextPane txtComanda;
    private javax.swing.JTextField txtConsumosCliente;
    private javax.swing.JTextField txtEfectivo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTotalBs;
    private javax.swing.JTextField txtTotalCobrarBs;
    private javax.swing.JTextField txtWhatsapp;
    // End of variables declaration//GEN-END:variables
}
