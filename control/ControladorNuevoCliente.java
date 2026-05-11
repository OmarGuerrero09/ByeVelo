package control;

import modelo1.ConexionDB;
import vista.VentanaNuevoCliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;

public class ControladorNuevoCliente implements ActionListener {
    private VentanaNuevoCliente vista;
    private ConexionDB conexion;

    public ControladorNuevoCliente(VentanaNuevoCliente vista) {
        this.vista = vista;
        this.conexion = new ConexionDB();
        this.vista.btnGuardarCliente.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnGuardarCliente) {
            String nombre = vista.txtNombre.getText();
            String tel = vista.txtTelefono.getText();
            String correo = vista.txtCorreo.getText();
            String fecha = vista.txtFechaNacimiento.getText();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El nombre es obligatorio");
                return;
            }

            try (Connection conn = conexion.conectar()) {
                String sql = "INSERT INTO clientes (nombre_completo, telefono, correo, fecha_nacimiento) VALUES (?,?,?,?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, nombre);
                pst.setString(2, tel);
                pst.setString(3, correo);
                pst.setString(4, fecha);
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(vista, "Cliente guardado en ByeVelo");
                
                // Limpiar campos después de guardar
                vista.txtNombre.setText("");
                vista.txtTelefono.setText("");
                vista.txtCorreo.setText("");
                vista.txtFechaNacimiento.setText("");
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vista, "Error al guardar: " + ex.getMessage());
            }
        }
    }
}