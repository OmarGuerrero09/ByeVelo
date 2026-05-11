package control;

import modelo1.ConexionDB;
import vista.VentanaNuevaVenta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;

public class ControladorNuevaVenta implements ActionListener {
    private VentanaNuevaVenta vista;
    private ConexionDB conexion;

    public ControladorNuevaVenta(VentanaNuevaVenta vista) {
        this.vista = vista;
        this.conexion = new ConexionDB();
        this.vista.btnRegistrarVenta.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrarVenta) {
            try (Connection conn = conexion.conectar()) {
                String sql = "INSERT INTO ventas (cliente, servicios_productos, total, metodo_pago, estado) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, vista.txtCliente.getText());
                pst.setString(2, vista.txtServicios.getText());
                pst.setDouble(3, Double.parseDouble(vista.txtTotal.getText()));
                pst.setString(4, vista.cbMetodoPago.getSelectedItem().toString());
                pst.setString(5, vista.cbEstado.getSelectedItem().toString());
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(vista, "Venta registrada con éxito.");
                
                // Limpiamos los campos
                vista.txtCliente.setText("");
                vista.txtServicios.setText("");
                vista.txtTotal.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al registrar: " + ex.getMessage());
            }
        }
    }
}