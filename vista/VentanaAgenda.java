package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaAgenda extends JFrame {
    public JTextField txtFechaHora, txtCliente;
    public JList<String> listServicios;
    public JComboBox<String> cbEstado;
    public JButton btnRegistrarCita, btnModificarCita, btnEliminarCita;

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
        // Usamos GridBagLayout para que los campos de texto no se estiren como la lista
        JPanel panelInferior = new JPanel(new GridBagLayout());
        panelInferior.setBorder(BorderFactory.createTitledBorder("Nueva Cita"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5); // Márgenes entre componentes
        gbc.fill = GridBagConstraints.BOTH;

        // Fila 0: Fecha y Hora
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3; gbc.weighty = 0.0;
        panelInferior.add(new JLabel(" Fecha y Hora:"), gbc);
        txtFechaHora = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.7;
        panelInferior.add(txtFechaHora, gbc);

        // Fila 1: Cliente
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3; gbc.weighty = 0.0;
        panelInferior.add(new JLabel(" Cliente:"), gbc);
        txtCliente = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.7;
        panelInferior.add(txtCliente, gbc);

        // Fila 2: Servicio(s)
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3; gbc.weighty = 0.0;
        panelInferior.add(new JLabel(" Servicio(s) [Usa Ctrl]:"), gbc);
        
        // AQUÍ ESTÁ LA LISTA COMPLETA DE SERVICIOS (59 en total)
        String[] listaServicios = {
            // --- MUJERES ---
            "Mujer - Bikini completo - $200", "Mujer - Bikini sencillo - $170", "Mujer - Ingles - $130", 
            "Mujer - Zona perianal - $90", "Mujer - Piernas completas - $350", "Mujer - Medias piernas - $260", 
            "Mujer - Espalda completa - $250", "Mujer - Media espalda - $170", "Mujer - Brazos completos - $230", 
            "Mujer - Medios brazos - $180", "Mujer - Abdomen completo - $200", "Mujer - Medio abdomen - $130", 
            "Mujer - Glúteos - $200", "Mujer - Manos - $45", "Mujer - Pies - $45", "Mujer - Dedos manos/pies - $35", 
            "Mujer - Pecho c/busto - $130", "Mujer - Pezones - $40", "Mujer - Axilas - $80", 
            "Mujer - Rostro completo - $180", "Mujer - Cejas - $100", "Mujer - Patillas - $70", 
            "Mujer - Cuello - $70", "Mujer - Mentón - $50", "Mujer - Mejillas - $50", "Mujer - Nariz - $50", 
            "Mujer - Bigote - $50", "Mujer - Frente - $40", "Mujer - Perfil de rostro - $50",
            
            // --- HOMBRES ---
            "Hombre - Bikini completo - $300", "Hombre - Ingles - $180", "Hombre - Zona perianal - $130", 
            "Hombre - Piernas completas - $380", "Hombre - Medias piernas - $300", "Hombre - Espalda completa - $320", 
            "Hombre - Media espalda - $200", "Hombre - Brazos completos - $280", "Hombre - Medios brazos - $180", 
            "Hombre - Abdomen completo - $270", "Hombre - Medio abdomen - $150", "Hombre - Línea de abdomen - $70", 
            "Hombre - Glúteos - $280", "Hombre - Manos - $100", "Hombre - Pies - $80", "Hombre - Dedos manos/pies - $45", 
            "Hombre - Pecho - $230", "Hombre - Pezones - $50", "Hombre - Axilas - $130", 
            "Hombre - Rostro completo - $280", "Hombre - Cejas - $130", "Hombre - Patillas - $130", 
            "Hombre - Cuello - $170", "Hombre - Mentón - $70", "Hombre - Mejillas - $150", "Hombre - Nariz - $75", 
            "Hombre - Bigote - $70", "Hombre - Frente - $40", "Hombre - Perfil de rostro - $130", 
            "Hombre - Delineado de barba - $150"
        };
        
        listServicios = new JList<>(listaServicios);
        listServicios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listServicios.setVisibleRowCount(5); // Le decimos que muestre unas 5 filas por defecto
        JScrollPane scrollServicios = new JScrollPane(listServicios);
        
        // Le damos peso vertical (weighty = 1.0) SOLAMENTE a la lista para que tome el espacio extra
        gbc.gridx = 1; gbc.weightx = 0.7; gbc.weighty = 1.0; 
        panelInferior.add(scrollServicios, gbc);

        // Fila 3: Estado
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3; gbc.weighty = 0.0; // Quitamos el peso vertical
        panelInferior.add(new JLabel(" Estado:"), gbc);
        cbEstado = new JComboBox<>(new String[]{"Pendiente", "Pagada"});
        gbc.gridx = 1; gbc.weightx = 0.7;
        panelInferior.add(cbEstado, gbc);

        // --- SECCIÓN DE BOTONES ---
        JPanel panelContenedorSur = new JPanel(new BorderLayout());
        panelContenedorSur.add(panelInferior, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        btnRegistrarCita = new JButton("Registrar Nueva");
        btnModificarCita = new JButton("Modificar Cita");
        btnEliminarCita = new JButton("Eliminar Cita");

        panelBotones.add(btnRegistrarCita);
        panelBotones.add(btnModificarCita);
        panelBotones.add(btnEliminarCita);

        panelContenedorSur.add(panelBotones, BorderLayout.SOUTH);

        add(panelContenedorSur, BorderLayout.SOUTH);
    }
}