package org.sfsoft.holasql.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.sfsoft.holasql.gui.JConecta;
import org.sfsoft.holasql.gui.JConecta.Accion;

/**
 * Ejemplo que conecta/desconecta con MySQL utilizando el driver JDBC
 * @author Santiago Faci
 *
 */
public class HolaSql {

	private JFrame frmHolasql;
	private JMenuBar menuBar;
	private JMenu mnServidor;
	private JMenuItem mntmConectar;
	private JMenuItem mntmDesconectar;
	private JMenuItem mntmSalir;
	
	private Connection conexion;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HolaSql window = new HolaSql();
					window.frmHolasql.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HolaSql() {
		initialize();
	}
	
	/*
	 * Carga el driver JDBC para MySQL y conecta con el SGBD
	 */
	private void conectar() {
		
		/*
		 * Muestra la ventana de conexión y recoge los datos del usuario. La ventana es modal
		 * por lo que espera hasta que el usuario la cierre
		 */
		JConecta jConecta = new JConecta();
		if (jConecta.mostrarDialogo() == Accion.CANCELAR)
			return;
		
		String servidor = jConecta.getServidor();
		String usuario = jConecta.getUsuario();
		String contrasena = jConecta.getContrasena();
		
		try {
			/*
			 * Carga el driver de conexión JDBC para conectar con MySQL
			 */
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conexion = DriverManager.getConnection("jdbc:mysql://" + servidor + ":3306" + "/", usuario, contrasena);
			JOptionPane.showMessageDialog(null, "Se ha conectado con éxito");
			
			mntmConectar.setEnabled(false);
			mntmDesconectar.setEnabled(true);
			
		} catch (ClassNotFoundException cnfe) {
			JOptionPane.showMessageDialog(null, "No se ha podido cargar el driver de la Base de Datos");
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, "No se ha podido conectar con la Base de Datos");
			sqle.printStackTrace();
		} catch (InstantiationException ie) {
			ie.printStackTrace();
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		}
	}
	
	/*
	 * Desconecta de MySQL
	 */
	private void desconectar() {
		
		try {
			conexion.close();
			conexion = null;	
			
			JOptionPane.showMessageDialog(null, "Se ha desconectado de la Base de Datos");
			
			mntmConectar.setEnabled(true);
			mntmDesconectar.setEnabled(false);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
	}
	
	/*
	 * Sale de la aplicación
	 */
	private void salir() {
		
		desconectar();
		System.exit(0);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHolasql = new JFrame();
		frmHolasql.setTitle("HolaSql");
		frmHolasql.setBounds(100, 100, 450, 300);
		frmHolasql.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmHolasql.setLocationRelativeTo(null);
		
		menuBar = new JMenuBar();
		frmHolasql.setJMenuBar(menuBar);
		
		mnServidor = new JMenu("Servidor");
		menuBar.add(mnServidor);
		
		mntmConectar = new JMenuItem("Conectar");
		mntmConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conectar();
			}
		});
		mnServidor.add(mntmConectar);
		
		mntmDesconectar = new JMenuItem("Desconectar");
		mntmDesconectar.setEnabled(false);
		mntmDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desconectar();
			}
		});
		mnServidor.add(mntmDesconectar);
		
		mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salir();
			}
		});
		mnServidor.add(mntmSalir);
	}

}
