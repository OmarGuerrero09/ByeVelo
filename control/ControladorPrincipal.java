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
        
        // --- CERRAR SESIÓN ---
        else if (e.getSource() == vista.btnCerrarSesion) {
            vista.dispose();
            VentanaLogin vLogin = new VentanaLogin();
            new ControladorLogin(vLogin).iniciar();
        }
    }
}