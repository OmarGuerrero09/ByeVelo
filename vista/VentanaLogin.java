package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Autor: Edwin Omar Guerrero Godina
 */
public class VentanaLogin extends JFrame {
    public JTextField txtUsuario;
    public JPasswordField txtPassword;
    public JButton btnIniciarSesion;
    private JLabel lblLogo;

    public VentanaLogin() {
        setTitle("Inicio de sesión - Bye Velo");
        setSize(400, 400); // Un poco más alta para que quepa el logo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // --- SECCIÓN DEL LOGO ---
        lblLogo = new JLabel();
        // Coloca tu imagen en la carpeta raíz del proyecto y cambia el nombre aquí
        ImageIcon icono = new ImageIcon("C:\\Users\\Omar0\\Downloads\\SinFondoByeVelo.png"); 
        // Redimensionamos la imagen para el login (120x120)
        Image img = icono.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(img));
        lblLogo.setBounds(140, 20, 120, 120); // Centrado horizontalmente
        add(lblLogo);

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setBounds(60, 160, 100, 25);
        add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(60, 185, 280, 30);
        add(txtUsuario);

        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setBounds(60, 225, 100, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(60, 250, 280, 30);
        add(txtPassword);

        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setBounds(130, 300, 140, 40);
        add(btnIniciarSesion);
    }
}