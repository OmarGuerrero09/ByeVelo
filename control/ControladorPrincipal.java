package control;

import modelo1.ConexionDB;
import vista.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;

public class ControladorPrincipal implements ActionListener {
    private VentanaPrincipal vista;
    private ConexionDB conexion;

    public ControladorPrincipal(VentanaPrincipal vista) {
        this.vista = vista;
        this.conexion = new ConexionDB();
        
        // ACTIVAR ESCUCHA DE BOTONES
        this.vista.btnClientes.addActionListener(this);      
        this.vista.btnNuevoCliente.addActionListener(this);   
        this.vista.btnAgenda.addActionListener(this);
        this.vista.btnNuevaCita.addActionListener(this);
        this.vista.btnVentas.addActionListener(this);
        this.vista.btnRegistrarVenta.addActionListener(this);
        this.vista.btnCerrarSesion.addActionListener(this);
        // ACTIVAR LA BARRA DE BÚSQUEDA (Reacciona al presionar Enter)
        this.vista.txtBusqueda.addActionListener(this);

        // Efecto visual: Quitar el texto por defecto al hacer clic en la barra
        this.vista.txtBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (vista.txtBusqueda.getText().equals(" Búsqueda de Cliente")) {
                    vista.txtBusqueda.setText("");
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (vista.txtBusqueda.getText().isEmpty()) {
                    vista.txtBusqueda.setText(" Búsqueda de Cliente");
                }
            }
        });
        // Cargar los contadores al iniciar la ventana
        actualizarContadores();

        // TRUCO: Actualizar contadores automáticamente cuando la ventana vuelve a estar activa
        this.vista.addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                actualizarContadores();
            }
        });
    }

    // MÉTODO PARA CONSULTAR MYSQL Y LLENAR LOS RECUKADROS
    public void actualizarContadores() {
        try (Connection conn = conexion.conectar()) {
            
            // 1. Citas (Contamos el total de citas registradas)
            String sqlCitas = "SELECT COUNT(*) AS total_citas FROM citas";
            Statement stCitas = conn.createStatement();
            ResultSet rsCitas = stCitas.executeQuery(sqlCitas);
            if (rsCitas.next()) {
                vista.lblCitasHoy.setText("Citas de hoy: " + rsCitas.getInt("total_citas"));
            }

            // 2. Clientes nuevos (Contamos el total de clientes)
            String sqlClientes = "SELECT COUNT(*) AS total_clientes FROM clientes";
            Statement stClientes = conn.createStatement();
            ResultSet rsClientes = stClientes.executeQuery(sqlClientes);
            if (rsClientes.next()) {
                vista.lblClientesNuevos.setText("Clientes nuevos: " + rsClientes.getInt("total_clientes"));
            }

            // 3. Ingresos del día (Sumamos la columna 'total' de las ventas hechas HOY)
            String sqlIngresos = "SELECT SUM(total) AS ingresos_hoy FROM ventas WHERE DATE(fecha_hora) = CURDATE()";
            Statement stIngresos = conn.createStatement();
            ResultSet rsIngresos = stIngresos.executeQuery(sqlIngresos);
            if (rsIngresos.next()) {
                double ingresos = rsIngresos.getDouble("ingresos_hoy");
                // Si ingresos es nulo (no hay ventas hoy), mostramos 0.00
                vista.lblIngresos.setText("Ingresos del dia: $" + String.format("%.2f", ingresos));
            }

        } catch (SQLException ex) {
            System.err.println("Error al actualizar contadores: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // --- SECCIÓN CLIENTES ---
        if (e.getSource() == vista.btnClientes) {
            VentanaClientes vClientes = new VentanaClientes();
            new ControladorClientes(vClientes);
            vClientes.setVisible(true); 
        } 
        else if (e.getSource() == vista.btnNuevoCliente) {
            VentanaNuevoCliente vNuevoCliente = new VentanaNuevoCliente();
            new ControladorNuevoCliente(vNuevoCliente);
            vNuevoCliente.setVisible(true);
        }

        // --- SECCIÓN VENTAS ---
        else if (e.getSource() == vista.btnVentas) {
            VentanaVentas vVentas = new VentanaVentas();
            new ControladorVentas(vVentas);
            vVentas.setVisible(true);
        }
        else if (e.getSource() == vista.btnRegistrarVenta) {
            VentanaNuevaVenta vNuevaVenta = new VentanaNuevaVenta();
            new ControladorNuevaVenta(vNuevaVenta);
            vNuevaVenta.setVisible(true);
        }

        // --- SECCIÓN AGENDA ---
        else if (e.getSource() == vista.btnAgenda || e.getSource() == vista.btnNuevaCita) {
            VentanaAgenda vAgenda = new VentanaAgenda();
            new ControladorAgenda(vAgenda);
            vAgenda.setVisible(true);
        } 
        // --- BÚSQUEDA RÁPIDA DE CLIENTE ---
        else if (e.getSource() == vista.txtBusqueda) {
            String busqueda = vista.txtBusqueda.getText().trim();
            // Evitar buscar si está vacío o tiene el texto por defecto
            if (!busqueda.isEmpty() && !busqueda.equals("Búsqueda de Cliente")) {
                realizarBusqueda(busqueda);
            }
        }
        // --- CERRAR SESIÓN ---
        else if (e.getSource() == vista.btnCerrarSesion) {
            vista.dispose();
            VentanaLogin vLogin = new VentanaLogin();
            new ControladorLogin(vLogin).iniciar();
        }
    }
    // MÉTODO PARA BUSCAR CLIENTES EN LA BD Y MOSTRAR SUS CITAS
    private void realizarBusqueda(String nombre) {
        try (Connection conn = conexion.conectar()) {
            
            // EL TRUCO: Reemplazamos los espacios por "%" para que busque cualquier cosa entre las palabras
            // Si buscas "Juan Perez", se convierte en "%Juan%Perez%" y encontrará a "Juan Carlos Perez"
            String busquedaSQL = "%" + nombre.replace(" ", "%") + "%";

            // 1. Buscar al cliente en la tabla clientes
            String sqlCliente = "SELECT * FROM clientes WHERE nombre_completo LIKE ?";
            PreparedStatement pstCliente = conn.prepareStatement(sqlCliente);
            pstCliente.setString(1, busquedaSQL);
            ResultSet rsCliente = pstCliente.executeQuery();

            StringBuilder resultados = new StringBuilder();
            boolean hayResultados = false;

            while (rsCliente.next()) {
                hayResultados = true;
                String nombreEncontrado = rsCliente.getString("nombre_completo");
                
                // Datos del cliente
                resultados.append("👤 Nombre: ").append(nombreEncontrado).append("\n")
                          .append("📞 Teléfono: ").append(rsCliente.getString("telefono")).append("\n")
                          .append("✉️ Correo: ").append(rsCliente.getString("correo")).append("\n");

                // 2. Buscar si este cliente tiene citas registradas
                String sqlCitas = "SELECT * FROM citas WHERE cliente = ?";
                PreparedStatement pstCitas = conn.prepareStatement(sqlCitas);
                pstCitas.setString(1, nombreEncontrado);
                ResultSet rsCitas = pstCitas.executeQuery();

                boolean tieneCitas = false;
                resultados.append("📅 CITAS:\n");
                
                while (rsCitas.next()) {
                    tieneCitas = true;
                    resultados.append("   • Fecha/Hora: ").append(rsCitas.getString("fecha_hora")).append("\n")
                              .append("     Servicio: ").append(rsCitas.getString("servicio")).append("\n")
                              .append("     Estado: ").append(rsCitas.getString("estado")).append("\n\n");
                }

                if (!tieneCitas) {
                    resultados.append("   No tiene citas programadas.\n");
                }
                
                resultados.append("------------------------------------------\n");
                
                rsCitas.close();
                pstCitas.close();
            }

            // Mostrar el cuadro de diálogo con toda la información armada
            if (hayResultados) {
                JOptionPane.showMessageDialog(vista, resultados.toString(), "Resultados de la búsqueda", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró ningún cliente que coincida con: " + nombre, "Sin resultados", JOptionPane.WARNING_MESSAGE);
            }
            
            rsCliente.close();
            pstCliente.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al consultar la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}