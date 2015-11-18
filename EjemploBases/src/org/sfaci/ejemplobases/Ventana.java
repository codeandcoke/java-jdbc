package org.sfaci.ejemplobases;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;

/**
 * Created by DAM on 16/11/2015.
 */
public class Ventana {
    private JPanel panel;
    private JTabbedPane tabbedPane1;
    private JLabel lbEstado;
    private JTextField tfNombre;
    private JTextField tfApellidos;
    private JTextField tfNacionalidad;
    private JButton btNuevo;
    private JButton btGuardar;
    private JButton btModificar;
    private JButton btEliminar;
    private JButton btCancelar;
    private JTable tabla;
    private JTextField tfBusqueda;
    private JDateChooser dcFechaNacimiento;

    private Connection conexion;

    public Ventana() {

        JFrame frame = new JFrame("Spotify");
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        try {
            conectar();
            login();
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null,
                    "No se ha podido cargar el driver del SGBD",
                    "Conectar", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null,
                    "No se ha podido conectar con el servidor. Comprueba " +
                            "que está arrancado",
                    "Conectar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void conectar() throws ClassNotFoundException,
        SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        // FIXME coger la información de conexion
        // de un fichero de configuracion (.properties)
        conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/musica", "root", "mysql");
    }

    private void login() {

        JLogin login = new JLogin();
        login.setVisible(true);

        String usuario = login.getUsuario();
        String contrasena = login.getContrasena();

        String sql = "SELECT nombre FROM usuarios WHERE " +
                "nombre = ? AND contrasena = SHA1(?)";

        try {
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, usuario);
            sentencia.setString(2, contrasena);
            ResultSet resultado = sentencia.executeQuery();

            if (!resultado.next()) {
                JOptionPane.showMessageDialog(null,
                        "Usuario/Contraseña incorrectos", "Login",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "", "",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
}
