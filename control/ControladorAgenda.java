package control;

import modelo1.ConexionDB;
import vista.VentanaAgenda;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControladorAgenda implements ActionListener {
    private VentanaAgenda vista;
    private ConexionDB conexion;

    public ControladorAgenda(VentanaAgenda vista) {
        this.vista = vista;
        this.conexion = new ConexionDB();
        
        // Cargar los datos en cuanto se abre la ventana
        cargarCitas();
        
        this.vista.btnRegistrarCita.addActionListener(this);
    }

    // MÉTODO PARA MOSTRAR LAS CITAS EN LA TABLA
    public void cargarCitas() {
        DefaultTableModel modelo = (DefaultTableModel) vista.tablaCitas.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de cargar

        try (Connection conn = conexion.conectar()) {
            String sql = "SELECT * FROM citas";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id_cita");
                fila[1] = rs.getString("fecha_hora");
                fila[2] = rs.getString("cliente");
                fila[3] = rs.getString("servicio");
                fila[4] = rs.getString("estado");
                modelo.addRow(fila);
            }
        } catch (SQLException ex) {
            System.err.println("Error al cargar citas: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrarCita) {
            registrarCita();
        }
    }

    private void registrarCita() {
        try (Connection conn = conexion.conectar()) {
            String sql = "INSERT INTO citas (fecha_hora, cliente, servicio, estado) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, vista.txtFechaHora.getText());
            pst.setString(2, vista.txtCliente.getText());
            pst.setString(3, vista.txtServicio.getText());
            pst.setString(4, vista.cbEstado.getSelectedItem().toString());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(vista, "Cita agendada correctamente.");
            
            // Recargar la tabla para mostrar la nueva cita de inmediato
            cargarCitas();
            
            // Limpiar campos
            vista.txtFechaHora.setText("");
            vista.txtCliente.setText("");
            vista.txtServicio.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al guardar: " + ex.getMessage());
        }
    }
}