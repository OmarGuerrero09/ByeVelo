package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaVentas extends JFrame {
    // Formulario
    public JTextField txtCliente, txtServicios, txtTotal;
    public JComboBox<String> cbMetodoPago, cbEstado;
    public JButton btnRegistrarVenta;
    
    // Tabla
    public JTable tablaVentas;
    public DefaultTableModel modeloTabla;

    public VentanaVentas() {
        setTitle("Ventas y Cobros - Bye Velo");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PARTE SUPERIOR: TABLA DE VENTAS ---
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Folio");
        modeloTabla.addColumn("Fecha/Hora");
        modeloTabla.addColumn("Cliente");
        modeloTabla.addColumn("Servicios");
        modeloTabla.addColumn("Total");
        modeloTabla.addColumn("Método");
        modeloTabla.addColumn("Estado");

        tablaVentas = new JTable(modeloTabla);
        JScrollPane sp = new JScrollPane(tablaVentas);
        add(sp, BorderLayout.CENTER);

        // --- PARTE INFERIOR: FORMULARIO ---
        JPanel panelInferior = new JPanel(new GridLayout(3, 4, 10, 10));
        panelInferior.setBorder(BorderFactory.createTitledBorder("Registrar Nueva Venta"));

        panelInferior.add(new JLabel(" Cliente:"));
        txtCliente = new JTextField();
        panelInferior.add(txtCliente);

        panelInferior.add(new JLabel(" Servicios:"));
        txtServicios = new JTextField();
        panelInferior.add(txtServicios);

        panelInferior.add(new JLabel(" Total ($):"));
        txtTotal = new JTextField();
        panelInferior.add(txtTotal);

        panelInferior.add(new JLabel(" Método de Pago:"));
        cbMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia"});
        panelInferior.add(cbMetodoPago);

        panelInferior.add(new JLabel(" Estado:"));
        cbEstado = new JComboBox<>(new String[]{"Pagado", "Pendiente"});
        panelInferior.add(cbEstado);

        panelInferior.add(new JLabel("")); // Espacio
        btnRegistrarVenta = new JButton("Registrar Venta");
        panelInferior.add(btnRegistrarVenta);

        add(panelInferior, BorderLayout.SOUTH);
    }
}