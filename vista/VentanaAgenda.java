package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaAgenda extends JFrame {
    public JTextField txtFechaHora, txtCliente, txtServicio;
    public JComboBox<String> cbEstado;
    public JButton btnRegistrarCita;
    // Nueva tabla y modelo para mostrar los datos
    public JTable tablaCitas;
    public DefaultTableModel modeloTabla;

    public VentanaAgenda() {
        setTitle("Agenda de Citas - Bye Velo");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PARTE SUPERIOR: TABLA ---
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Fecha/Hora");
        modeloTabla.addColumn("Cliente");
        modeloTabla.addColumn("Servicio");
        modeloTabla.addColumn("Estado");
        
        tablaCitas = new JTable(modeloTabla);
        JScrollPane sp = new JScrollPane(tablaCitas);
        add(sp, BorderLayout.CENTER);

        // --- PARTE INFERIOR: FORMULARIO DE REGISTRO ---
        JPanel panelInferior = new JPanel(new GridLayout(5, 2, 5, 5));
        panelInferior.setBorder(BorderFactory.createTitledBorder("Nueva Cita"));

        panelInferior.add(new JLabel(" Fecha y Hora:"));
        txtFechaHora = new JTextField();
        panelInferior.add(txtFechaHora);

        panelInferior.add(new JLabel(" Cliente:"));
        txtCliente = new JTextField();
        panelInferior.add(txtCliente);

        panelInferior.add(new JLabel(" Servicio:"));
        txtServicio = new JTextField();
        panelInferior.add(txtServicio);

        panelInferior.add(new JLabel(" Estado:"));
        cbEstado = new JComboBox<>(new String[]{"Pendiente", "Confirmada", "Completada", "Cancelada"});
        panelInferior.add(cbEstado);

        panelInferior.add(new JLabel(""));
        btnRegistrarCita = new JButton("Registrar Cita");
        panelInferior.add(btnRegistrarCita);

        add(panelInferior, BorderLayout.SOUTH);
    }
}