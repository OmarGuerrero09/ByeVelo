package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    public JButton btnInicio, btnAgenda, btnClientes, btnVentas, btnCerrarSesion;
    public JButton btnNuevaCita, btnNuevoCliente, btnRegistrarVenta;
    public JTextField txtBusqueda;
    private JLabel lblLogoGrande;
    
    // Recuperamos las etiquetas de la imagen
    public JLabel lblCitasHoy, lblClientesNuevos, lblIngresos;

    public VentanaPrincipal() {
        setTitle("Ventana Principal - Bye Velo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Lateral
        JPanel panelLateral = new JPanel(new GridLayout(6, 1, 10, 10));
        panelLateral.setBackground(Color.LIGHT_GRAY);
        panelLateral.setPreferredSize(new Dimension(150, 600));

        btnInicio = new JButton("Inicio");
        btnAgenda = new JButton("Agenda");
        btnClientes = new JButton("Clientes");
        btnVentas = new JButton("Ventas y Cobros");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        panelLateral.add(btnInicio); panelLateral.add(btnAgenda);
        panelLateral.add(btnClientes); panelLateral.add(btnVentas);
        panelLateral.add(new JLabel("")); panelLateral.add(btnCerrarSesion);
        add(panelLateral, BorderLayout.WEST);

        // Panel Central
        JPanel panelCentral = new JPanel(null);

        // --- LOGO GRANDE Y CENTRADO ---
        lblLogoGrande = new JLabel();
        ImageIcon iconoOriginal = new ImageIcon("C:\\Users\\Omar0\\Downloads\\SinFondoByeVelo.png"); // Asegúrate de que el nombre coincida con tu imagen
        Image imgEscalada = iconoOriginal.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        lblLogoGrande.setIcon(new ImageIcon(imgEscalada));
        lblLogoGrande.setBounds(175, 20, 300, 200);
        panelCentral.add(lblLogoGrande);

        // --- INDICADORES (RECUPERADOS Y CON BORDE) ---
        // Se colocan justo debajo del logo
        lblCitasHoy = new JLabel("Citas de hoy:", SwingConstants.CENTER);
        lblCitasHoy.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro estilo mockup
        lblCitasHoy.setBounds(50, 240, 150, 30);
        
        lblClientesNuevos = new JLabel("Clientes nuevos:", SwingConstants.CENTER);
        lblClientesNuevos.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lblClientesNuevos.setBounds(250, 240, 150, 30);
        
        lblIngresos = new JLabel("Ingresos del dia:", SwingConstants.CENTER);
        lblIngresos.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lblIngresos.setBounds(450, 240, 150, 30);

        panelCentral.add(lblCitasHoy);
        panelCentral.add(lblClientesNuevos);
        panelCentral.add(lblIngresos);

        // --- BARRA DE BÚSQUEDA ---
        // Ajustamos la posición (y) hacia abajo para que no se empalme
        txtBusqueda = new JTextField(" Búsqueda de Cliente");
        txtBusqueda.setBounds(50, 290, 550, 35);
        panelCentral.add(txtBusqueda);

        // --- BOTONES DE ACCIÓN CENTRALES ---
        btnNuevaCita = new JButton("Nueva Cita");
        btnNuevaCita.setBounds(50, 350, 180, 40);
        btnNuevoCliente = new JButton("Nuevo Cliente");
        btnNuevoCliente.setBounds(50, 410, 180, 40);
        btnRegistrarVenta = new JButton("Registrar venta");
        btnRegistrarVenta.setBounds(50, 470, 180, 40);

        panelCentral.add(btnNuevaCita);
        panelCentral.add(btnNuevoCliente);
        panelCentral.add(btnRegistrarVenta);

        add(panelCentral, BorderLayout.CENTER);
    }
}