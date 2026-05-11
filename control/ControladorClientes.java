package control;

import modelo1.ConexionDB;
import vista.VentanaClientes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControladorClientes implements ActionListener {
    private VentanaClientes vistaClientes;
    private ConexionDB conexion;

    public ControladorClientes(VentanaClientes vistaClientes) {
        this.vistaClientes = vistaClientes;
        this.conexion = new ConexionDB();
        
        // Llenar la tabla en cuanto se abra la ventana
        cargarClientes();
        
        // Escuchar el botón interno de la ventana de registro
        this.vistaClientes.btnGuardarCliente.addActionListener(this);
    }

    public void iniciar() {
        vistaClientes.setVisible(true);
    }

    // MÉTODO PARA MOSTRAR LOS CLIENTES EN LA TABLA
    public void cargarClientes() {
        DefaultTableModel modelo = (DefaultTableModel) vistaClientes.tablaClientes.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de cargar para no duplicar

        try (Connection conn = conexion.conectar()) {
            String sql = "SELECT * FROM clientes";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id_cliente");
                fila[1] = rs.getString("nombre_completo");
                fila[2] = rs.getString("telefono");
                fila[3] = rs.getString("correo");
                fila[4] = rs.getString("fecha_nacimiento");
                modelo.addRow(fila);
            }
        } catch (SQLException ex) {
            System.err.println("Error al cargar clientes: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaClientes.btnGuardarCliente) {
            guardarDatos();
        }
    }

    private void guardarDatos() {
        String nombre = vistaClientes.txtNombre.getText();
        String tel = vistaClientes.txtTelefono.getText();
        String correo = vistaClientes.txtCorreo.getText();
        String fecha = vistaClientes.txtFechaNacimiento.getText();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vistaClientes, "El nombre es obligatorio");
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
            JOptionPane.showMessageDialog(vistaClientes, "Cliente guardado en ByeVelo");
            
            // Recargar la tabla automáticamente para ver el cliente que acabas de guardar
            cargarClientes();
            
            // Limpiar los campos de texto
            vistaClientes.txtNombre.setText("");
            vistaClientes.txtTelefono.setText("");
            vistaClientes.txtCorreo.setText("");
            vistaClientes.txtFechaNacimiento.setText("");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vistaClientes, "Error al guardar: " + ex.getMessage());
        }
    }
}