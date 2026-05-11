package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaClientes extends JFrame {
    // Componentes del formulario
    public JTextField txtNombre, txtTelefono, txtCorreo, txtFechaNacimiento;
    public JButton btnGuardarCliente;
    
    // Componentes de la tabla
    public JTable tablaClientes;
    public DefaultTableModel modeloTabla;

    public VentanaClientes() {
        setTitle("Gestión de Clientes - Bye Velo");
        setSize(700, 500); // Ventana más grande para que quepan bien las columnas
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PARTE SUPERIOR: TABLA DE CLIENTES ---
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre Completo");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Correo Electrónico");
        modeloTabla.addColumn("Fecha Nac.");

        tablaClientes = new JTable(modeloTabla);
        JScrollPane sp = new JScrollPane(tablaClientes);
        add(sp, BorderLayout.CENTER);

        // --- PARTE INFERIOR: FORMULARIO DE REGISTRO ---
        JPanel panelInferior = new JPanel(new GridLayout(5, 2, 5, 5));
        panelInferior.setBorder(BorderFactory.createTitledBorder("Nuevo Cliente"));

        panelInferior.add(new JLabel(" Nombre Completo:"));
        txtNombre = new JTextField();
        panelInferior.add(txtNombre);

        panelInferior.add(new JLabel(" Teléfono:"));
        txtTelefono = new JTextField();
        panelInferior.add(txtTelefono);

        panelInferior.add(new JLabel(" Correo Electrónico:"));
        txtCorreo = new JTextField();
        panelInferior.add(txtCorreo);

        panelInferior.add(new JLabel(" Fecha de Nacimiento:"));
        txtFechaNacimiento = new JTextField();
        panelInferior.add(txtFechaNacimiento);

        panelInferior.add(new JLabel("")); // Espacio en blanco
        btnGuardarCliente = new JButton("Registrar Nuevo");
        panelInferior.add(btnGuardarCliente);

        add(panelInferior, BorderLayout.SOUTH);
    }
}