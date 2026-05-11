package control;

import modelo1.ConexionDB;
import vista.VentanaLogin;
import vista.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControladorLogin implements ActionListener {
    private VentanaLogin vistaLogin;
    private ConexionDB conexion;

    public ControladorLogin(VentanaLogin vistaLogin) {
        this.vistaLogin = vistaLogin;
        this.conexion = new ConexionDB();
        
        // Agregar el listener al botón
        this.vistaLogin.btnIniciarSesion.addActionListener(this);
    }

    public void iniciar() {
        vistaLogin.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaLogin.btnIniciarSesion) {
            String usuario = vistaLogin.txtUsuario.getText();
            String password = new String(vistaLogin.txtPassword.getPassword());

            // VALIDACIÓN REAL CON LA BASE DE DATOS MYSQL (ByeVelo)
            try {
                Connection conn = conexion.conectar();
                if (conn != null) {
                    // Consulta para verificar si existe el usuario y la contraseña
                    String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, usuario);
                    pst.setString(2, password);
                    
                    ResultSet rs = pst.executeQuery();
                    
                    if (rs.next()) {
                        // Si hay un resultado, las credenciales son correctas
                        vistaLogin.dispose();
                        VentanaPrincipal vistaPrincipal = new VentanaPrincipal();
                        
                        // Activamos el controlador principal para que los botones funcionen
                        ControladorPrincipal ctrlPrincipal = new ControladorPrincipal(vistaPrincipal);
                        vistaPrincipal.setVisible(true);
                    } else {
                        // Si no hay resultado, el usuario no existe o la contraseña está mal
                        JOptionPane.showMessageDialog(vistaLogin, "Usuario o contraseña incorrectos.", "Error de Login", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    // Cerramos las conexiones para liberar recursos
                    rs.close();
                    pst.close();
                    conn.close();
                } else {
                    JOptionPane.showMessageDialog(vistaLogin, "No se pudo conectar a la base de datos.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vistaLogin, "Error en la base de datos: " + ex.getMessage());
            }
        }
    }
}