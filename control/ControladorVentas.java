package control;

import modelo1.ConexionDB;
import vista.VentanaVentas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControladorVentas implements ActionListener {
    private VentanaVentas vista;
    private ConexionDB conexion;

    public ControladorVentas(VentanaVentas vista) {
        this.vista = vista;
        this.conexion = new ConexionDB();
        
        cargarVentas(); // Llena la tabla al abrir
        this.vista.btnRegistrarVenta.addActionListener(this);
    }

    public void cargarVentas() {
        DefaultTableModel modelo = (DefaultTableModel) vista.tablaVentas.getModel();
        modelo.setRowCount(0); // Limpiar antes de llenar

        try (Connection conn = conexion.conectar()) {
            String sql = "SELECT * FROM ventas";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = new Object[7];
                fila[0] = rs.getInt("folio");
                fila[1] = rs.getString("fecha_hora");
                fila[2] = rs.getString("cliente");
                fila[3] = rs.getString("servicios_productos");
                fila[4] = "$" + rs.getDouble("total");
                fila[5] = rs.getString("metodo_pago");
                fila[6] = rs.getString("estado");
                modelo.addRow(fila);
            }
        } catch (SQLException ex) {
            System.err.println("Error al cargar ventas: " + ex.getMessage());
        }
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
                
                cargarVentas(); // Refresca la tabla
                
                vista.txtCliente.setText("");
                vista.txtServicios.setText("");
                vista.txtTotal.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al registrar: " + ex.getMessage());
            }
        }
    }
}