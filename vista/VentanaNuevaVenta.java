package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaNuevaVenta extends JFrame {
    public JTextField txtFolio, txtCliente, txtServicios, txtTotal;
    public JComboBox<String> cbMetodoPago, cbEstado;
    public JButton btnRegistrarVenta;

    public VentanaNuevaVenta() {
        setTitle("Registrar Nueva Venta - Bye Velo");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 10, 10));

        add(new JLabel(" Folio:"));
        txtFolio = new JTextField("Autogenerado");
        txtFolio.setEditable(false);
        add(txtFolio);

        add(new JLabel(" Cliente:"));
        txtCliente = new JTextField();
        add(txtCliente);

        add(new JLabel(" Servicios/Productos:"));
        txtServicios = new JTextField();
        add(txtServicios);

        add(new JLabel(" Total ($):"));
        txtTotal = new JTextField();
        add(txtTotal);

        add(new JLabel(" Método de Pago:"));
        cbMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia"});
        add(cbMetodoPago);

        add(new JLabel(" Estado:"));
        cbEstado = new JComboBox<>(new String[]{"Pagado", "Pendiente"});
        add(cbEstado);

        add(new JLabel("")); // Espacio en blanco
        btnRegistrarVenta = new JButton("Registrar Venta");
        add(btnRegistrarVenta);
    }
}