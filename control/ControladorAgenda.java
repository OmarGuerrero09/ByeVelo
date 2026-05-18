package control;

import modelo1.ConexionDB;
import vista.VentanaAgenda;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
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
        this.vista.btnModificarCita.addActionListener(this);
        this.vista.btnEliminarCita.addActionListener(this);
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
        } else if (e.getSource() == vista.btnEliminarCita) {
            eliminarCita();
        } else if (e.getSource() == vista.btnModificarCita) {
            modificarCitaPopup();
        }
    }

    private void registrarCita() {
        // 1. Obtener todos los servicios seleccionados
        java.util.List<String> seleccionados = vista.listServicios.getSelectedValuesList();
        
        if (seleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Debes seleccionar al menos un servicio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String estado = vista.cbEstado.getSelectedItem().toString();
        
        // 2. Sumar precios y armar el texto con todos los nombres
        StringBuilder serviciosConcatenados = new StringBuilder();
        double precioTotal = 0.0;

        for (int i = 0; i < seleccionados.size(); i++) {
            String serv = seleccionados.get(i);
            serviciosConcatenados.append(serv);
            
            // Agregamos un "+" entre cada servicio para que se lea bien en la base de datos
            if (i < seleccionados.size() - 1) {
                serviciosConcatenados.append(" + "); 
            }
            
            // Extraer el precio numérico (lo que está después del símbolo $) y sumarlo
            String[] partes = serv.split("\\$");
            precioTotal += Double.parseDouble(partes[1].trim());
        }

        String servicioFinal = serviciosConcatenados.toString();

        try (Connection conn = conexion.conectar()) {
            // Guardar en la tabla citas
            String sqlCita = "INSERT INTO citas (fecha_hora, cliente, servicio, estado) VALUES (?, ?, ?, ?)";
            PreparedStatement pstCita = conn.prepareStatement(sqlCita);
            pstCita.setString(1, vista.txtFechaHora.getText());
            pstCita.setString(2, vista.txtCliente.getText());
            pstCita.setString(3, servicioFinal);
            pstCita.setString(4, estado);
            pstCita.executeUpdate();
            pstCita.close();

            // Si está pagada, registrar el total sumado en las ventas
            if (estado.equals("Pagada")) {
                String sqlVenta = "INSERT INTO ventas (cliente, servicios_productos, total, metodo_pago, estado) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstVenta = conn.prepareStatement(sqlVenta);
                pstVenta.setString(1, vista.txtCliente.getText());
                pstVenta.setString(2, "Cita agendada: " + servicioFinal);
                pstVenta.setDouble(3, precioTotal); // Usamos la SUMA TOTAL de todo lo elegido
                pstVenta.setString(4, "Efectivo");
                pstVenta.setString(5, "Pagado");
                pstVenta.executeUpdate();
                pstVenta.close();
            }

            JOptionPane.showMessageDialog(vista, "Cita agendada correctamente.");
            cargarCitas();
            
            // Limpiar ventana
            vista.txtFechaHora.setText("");
            vista.txtCliente.setText("");
            vista.listServicios.clearSelection(); 
            vista.cbEstado.setSelectedIndex(0);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al guardar: " + ex.getMessage());
        }
    }
    // MÉTODO PARA ELIMINAR UNA CITA
    private void eliminarCita() {
        int fila = vista.tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona una cita de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idCita = (int) vista.tablaCitas.getValueAt(fila, 0);
        int confirmar = JOptionPane.showConfirmDialog(vista, "¿Estás seguro de eliminar esta cita?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmar == JOptionPane.YES_OPTION) {
            try (Connection conn = conexion.conectar()) {
                String sql = "DELETE FROM citas WHERE id_cita = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, idCita);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(vista, "Cita eliminada.");
                cargarCitas();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vista, "Error al eliminar: " + ex.getMessage());
            }
        }
    }

    // MÉTODO PARA MODIFICAR UNA CITA EN VENTANA EMERGENTE
    private void modificarCitaPopup() {
        int fila = vista.tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona una cita de la tabla para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idCita = (int) vista.tablaCitas.getValueAt(fila, 0);
        String fechaActual = vista.tablaCitas.getValueAt(fila, 1).toString();
        String clienteActual = vista.tablaCitas.getValueAt(fila, 2).toString();
        String servicioActual = vista.tablaCitas.getValueAt(fila, 3).toString();
        String estadoActual = vista.tablaCitas.getValueAt(fila, 4).toString();

        JTextField txtNuevoFecha = new JTextField(fechaActual);
        JTextField txtNuevoCliente = new JTextField(clienteActual);
        
        // Crear una nueva JList para el popup
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        for (int i = 0; i < vista.listServicios.getModel().getSize(); i++) {
            modeloLista.addElement(vista.listServicios.getModel().getElementAt(i));
        }
        JList<String> listNuevoServicio = new JList<>(modeloLista);
        listNuevoServicio.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollNuevoServicio = new JScrollPane(listNuevoServicio);
        scrollNuevoServicio.setPreferredSize(new java.awt.Dimension(300, 120)); // Tamaño cómodo

        // Volver a sombrear/seleccionar los servicios que el cliente ya tenía guardados
        String[] serviciosActualesArray = servicioActual.split(" \\+ ");
        java.util.ArrayList<Integer> indicesSeleccionados = new java.util.ArrayList<>();
        for (int i = 0; i < modeloLista.getSize(); i++) {
            for (String sActual : serviciosActualesArray) {
                if (modeloLista.getElementAt(i).equals(sActual)) {
                    indicesSeleccionados.add(i);
                }
            }
        }
        int[] arrIndices = indicesSeleccionados.stream().mapToInt(i -> i).toArray();
        listNuevoServicio.setSelectedIndices(arrIndices);

        JComboBox<String> cbNuevoEstado = new JComboBox<>(new String[]{"Pendiente", "Pagada"});
        cbNuevoEstado.setSelectedItem(estadoActual);

        Object[] formularioPopup = {
            "Fecha y Hora:", txtNuevoFecha,
            "Cliente:", txtNuevoCliente,
            "Servicios [Usa Ctrl para varios]:", scrollNuevoServicio,
            "Estado:", cbNuevoEstado
        };

        int opcion = JOptionPane.showConfirmDialog(vista, formularioPopup, "Modificar Cita", JOptionPane.OK_CANCEL_OPTION);
        
        if (opcion == JOptionPane.OK_OPTION) {
            String nuevoEstado = cbNuevoEstado.getSelectedItem().toString();
            
            // Volver a sumar y agrupar en base a lo nuevo seleccionado
            java.util.List<String> seleccionesNuevas = listNuevoServicio.getSelectedValuesList();
            if (seleccionesNuevas.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Debes seleccionar al menos un servicio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            StringBuilder serviciosConcatenados = new StringBuilder();
            double precioTotal = 0.0;

            for (int i = 0; i < seleccionesNuevas.size(); i++) {
                String serv = seleccionesNuevas.get(i);
                serviciosConcatenados.append(serv);
                if (i < seleccionesNuevas.size() - 1) {
                    serviciosConcatenados.append(" + ");
                }
                String[] partes = serv.split("\\$");
                precioTotal += Double.parseDouble(partes[1].trim());
            }

            String nuevoServicio = serviciosConcatenados.toString();

            try (Connection conn = conexion.conectar()) {
                String sql = "UPDATE citas SET fecha_hora = ?, cliente = ?, servicio = ?, estado = ? WHERE id_cita = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, txtNuevoFecha.getText());
                pst.setString(2, txtNuevoCliente.getText());
                pst.setString(3, nuevoServicio);
                pst.setString(4, nuevoEstado);
                pst.setInt(5, idCita);
                pst.executeUpdate();
                pst.close();

                // Generar el ingreso a ventas solo si cambió a "Pagada"
                if (nuevoEstado.equals("Pagada") && !estadoActual.equals("Pagada")) {
                    String sqlVenta = "INSERT INTO ventas (cliente, servicios_productos, total, metodo_pago, estado) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pstVenta = conn.prepareStatement(sqlVenta);
                    pstVenta.setString(1, txtNuevoCliente.getText());
                    pstVenta.setString(2, "Cita modificada a Pagada: " + nuevoServicio);
                    pstVenta.setDouble(3, precioTotal);
                    pstVenta.setString(4, "Efectivo");
                    pstVenta.setString(5, "Pagado");
                    pstVenta.executeUpdate();
                    pstVenta.close();
                }

                JOptionPane.showMessageDialog(vista, "Cita actualizada correctamente.");
                cargarCitas();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vista, "Error al actualizar: " + ex.getMessage());
            }
        }
    }
}