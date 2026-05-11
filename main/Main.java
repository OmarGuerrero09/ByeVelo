package main;

import vista.VentanaLogin;
import control.ControladorLogin;

public class Main {
    public static void main(String[] args) {
        // LookAndFeel para que se vea más moderno en el sistema operativo
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Instanciamos el modelo MVC
        VentanaLogin login = new VentanaLogin();
        ControladorLogin ctrlLogin = new ControladorLogin(login);
        
        // Arrancamos la aplicación
        ctrlLogin.iniciar();
    }
}