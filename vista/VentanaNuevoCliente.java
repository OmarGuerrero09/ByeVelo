package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaNuevoCliente extends JFrame {
    public JTextField txtNombre, txtTelefono, txtCorreo, txtFechaNacimiento;
    public JButton btnGuardarCliente;

    public VentanaNuevoCliente() {
        setTitle("Registrar Nuevo Cliente");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        add(txtTelefono);

        add(new JLabel("Correo Electrónico:"));
        txtCorreo = new JTextField();
        add(txtCorreo);

        add(new JLabel("Fecha de Nacimiento:"));
        txtFechaNacimiento = new JTextField();
        add(txtFechaNacimiento);

        add(new JLabel("")); // Espacio
        btnGuardarCliente = new JButton("Registrar Nuevo");
        add(btnGuardarCliente);
    }
}