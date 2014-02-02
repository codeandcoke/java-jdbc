package org.sfsoft.sentenciassql.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import static org.sfsoft.sentenciassql.util.Constantes.AccionDialogo;

/**
 * Ejemplo que consulta y lanza sentencias de inserción, modificación y eliminación contra una Base de Datos 
 * MySQL utilizando el driver JDBC.
 *  - Se emplea la clase PreparedStatement en lugar de Statement
 *  - Permite seleccionar de un ComboBox la Base de Datos con la que el usuario desea conectar
 * 	- Invoca a una función almacenada de MySQL para mostrar la puntuación total de los personajes
 *  - Invoca a un procedimiento almaceado para eliminar todos los personajes
 * @author Santiago Faci
 * @version 1.0
 */
public class SentenciasSQL {

	private JFrame frmSentenciasSql;
	private JMenuBar menuBar;
	private JMenu mnServidor;
	private JMenuItem mntmConectar;
	private JMenuItem mntmDesconectar;
	private JMenuItem mntmSalir;
	private JPanel panelBuscar;
	private JTextField txtFiltro;
	private JButton btBuscar;
	private JScrollPane scrollPane;
	private JTablaDatos tablaDatos;
	private JButton btCancelar;
	private JPanel panelDatos;
	private JPanel panelSuperior;
	private JPanel panelInferior;
	private JButton btNuevo;
	private JButton btModificar;
	private JButton btEliminar;
	private JLabel lbTotalPuntos;
	private JButton btEliminarTodos;
	
	private Connection conexion;
	private AccionDialogo accion;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SentenciasSQL window = new SentenciasSQL();
					window.frmSentenciasSql.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SentenciasSQL() {
		initialize();
		inicializar();
	}
	
	/*
	 * Inicializa algunos aspectos del interfaz antes de que el usuario tome el cotnrol del mismo
	 */
	private void inicializar() {
		
		// Coloca el JFrame en el centro de la pantalla. También funciona con JDialog
		frmSentenciasSql.setLocationRelativeTo(null);
	}
	
	/*
	 * Carga el driver JDBC para MySQL y conecta con el SGBD
	 */
	private void conectar() {
		
		JConecta jConecta = new JConecta();
		if (jConecta.mostrarDialogo() == AccionDialogo.CANCELAR)
			return;
		
		String servidor = jConecta.getServidor();
		String baseDatos = jConecta.getBaseDatos();
		String usuario = jConecta.getUsuario();
		String contrasena = jConecta.getContrasena();
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conexion = DriverManager.getConnection("jdbc:mysql://" + servidor + ":3306" + "/" + baseDatos, 
					usuario, contrasena);
			JOptionPane.showMessageDialog(null, "Se ha conectado con éxito");
			
			tablaDatos.setConexion(conexion);
			cargarDatos();
			
			activarControles();
			
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
			
			desactivarControles();
			tablaDatos.vaciar();
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
	
	/*
	 * Carga los datos en la tabla, según la Base de Datos a la que ha conectado el usuario
	 */
	private void cargarDatos() {
		
		try {
			tablaDatos.listar();
			
			if (tablaDatos.getRowCount() == 0)
				JOptionPane.showMessageDialog(null, "No hay datos que mostrar");
			
			mostrarPuntuacionTotal();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	/*
	 * Muestra la puntuación total invocando a una función almacenada de MySQL
	 */
	private void mostrarPuntuacionTotal() throws SQLException {
		
		PreparedStatement sentencia = conexion.prepareStatement("select get_total_puntos()");
		ResultSet resultado = sentencia.executeQuery();
		resultado.next();
		
		String puntos = resultado.getString(1);
		if (resultado.wasNull())
			lbTotalPuntos.setText("Total Puntos: 0");
		else 
			lbTotalPuntos.setText("Total Puntos: " + puntos);
		
		// Libera los recursos del cursor (ResultSet)
		try {
			resultado.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		resultado = null;
	}
	
	/*
	 * Realiza la búsqueda según el filtro introducido por el usuario en la caja de texto
	 */
	private void buscar() {
		
		if (txtFiltro.getText().length() == 0) {
			cargarDatos();
			return;
		}
		
		if (txtFiltro.getText().length() < 2) 
			return;
		
		try {
			this.tablaDatos.listar(txtFiltro.getText());
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	/*
	 * El usuario cancela el filtrado de datos. Vuelven a mostrarse todos los registros en la tabla
	 */
	private void cancelarBusqueda() {
		
		this.txtFiltro.setText("");
		cargarDatos();
	}
	
	/*
	 * Da de alta un nuevo personaje
	 */
	private void nuevo() {
		
		JPersonaje jPersonaje = new JPersonaje();
		if (jPersonaje.mostrarDialogo() == AccionDialogo.CANCELAR)
			return;
		
		try {
			if (tablaDatos.existe(jPersonaje.getNombre())) {
				JOptionPane.showMessageDialog(null, "Ya existe un personaje con este nombre", "Alta", 
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			tablaDatos.nuevo(jPersonaje.getNombre(), jPersonaje.getNivel(), 
					jPersonaje.getEnergia(), jPersonaje.getPuntos());
			cargarDatos();
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, "No se ha podido registrar el personaje.\n"
					+ "Se ha producido un error", "Alta", JOptionPane.ERROR_MESSAGE);
			
			sqle.printStackTrace();
		}
	}
	
	/*
	 * Modifica la información del personaje que el usuario ha seleccionado en la tabla
	 */
	private void modificar() {
		
		int filaSeleccionada = 0;
		
		filaSeleccionada = tablaDatos.getSelectedRow();
		if (filaSeleccionada == -1)
			return;
		
		JPersonaje jPersonaje = new JPersonaje();
		jPersonaje.setNombre((String) tablaDatos.getValueAt(filaSeleccionada, 0));
		jPersonaje.setNivel((String) tablaDatos.getValueAt(filaSeleccionada, 1));
		jPersonaje.setEnergia((String) tablaDatos.getValueAt(filaSeleccionada, 2));
		jPersonaje.setPuntos((String) tablaDatos.getValueAt(filaSeleccionada, 3));

		if (jPersonaje.mostrarDialogo() == AccionDialogo.CANCELAR)
			return;
		
		try {
			String nombreOriginal = (String) tablaDatos.getValueAt(filaSeleccionada, 0);
			
			// Si ha modificado el nombre hay que comprobar que el nuevo nombre no exista ya
			if (!nombreOriginal.equals(jPersonaje.getNombre()))
				if (tablaDatos.existe(jPersonaje.getNombre())) {
					JOptionPane.showMessageDialog(null, "Ya existe un personaje con este nombre", "Alta", 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			
			tablaDatos.modificar(nombreOriginal, jPersonaje.getNombre(), jPersonaje.getNivel(), jPersonaje.getEnergia(), 
					jPersonaje.getPuntos());
			cargarDatos();
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, "No se ha podido modificar el personaje\n"
					+ "Se ha producido un error", "Modificar", JOptionPane.ERROR_MESSAGE);
			sqle.printStackTrace();
		}	
	}
	
	/*
	 * El usuario elimina el personaje que ha seleccionado de la tabla
	 */
	private void eliminar() {
	
		if (JOptionPane.showConfirmDialog(null, "¿Está seguro?", "Eliminar", JOptionPane.YES_NO_OPTION) == 
				JOptionPane.NO_OPTION) 
			return;
		
		try {
			tablaDatos.eliminar();
			
			JOptionPane.showMessageDialog(null, "El personaje se ha eliminado correctamente");
			
			cargarDatos();
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, "No se ha podido eliminar el personaje.\n" +
					"Se ha producido un error", "Eliminar", JOptionPane.ERROR_MESSAGE);
			sqle.printStackTrace();
		}
	}
	
	/*
	 * Elimina todos los personajes de la Base de Datos utilizando un procedimiento almacenado
	 */
	private void eliminarTodos() {
		
		if (JOptionPane.showConfirmDialog(null, "¿Está seguro?", "Eliminar", JOptionPane.YES_NO_OPTION) == 
				JOptionPane.NO_OPTION) 
			return;
		
		try {
			CallableStatement procedimiento = conexion.prepareCall("{ call eliminar_personajes() }");
			procedimiento.execute();
			
			cargarDatos();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	/*
	 * Activa los controles del interfaz de usuario
	 */
	private void activarControles() {
		
		this.txtFiltro.setEnabled(true);
		this.btBuscar.setEnabled(true);
		this.btCancelar.setEnabled(true);
		this.btNuevo.setEnabled(true);
		this.btModificar.setEnabled(true);
		this.btEliminar.setEnabled(true);
		this.btEliminarTodos.setEnabled(true);
		
		mntmConectar.setEnabled(false);
		mntmDesconectar.setEnabled(true);
	}
	
	/*
	 * Desactiva los controles del interfaz de usuario
	 */
	private void desactivarControles() {
		
		this.txtFiltro.setEnabled(false);
		this.btBuscar.setEnabled(false);
		this.btCancelar.setEnabled(false);
		this.btNuevo.setEnabled(false);
		this.btModificar.setEnabled(false);
		this.btEliminar.setEnabled(false);
		this.btEliminarTodos.setEnabled(false);
		
		mntmConectar.setEnabled(true);
		mntmDesconectar.setEnabled(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSentenciasSql = new JFrame();
		frmSentenciasSql.setTitle("Sentencias SQL");
		frmSentenciasSql.setBounds(100, 100, 450, 300);
		frmSentenciasSql.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		frmSentenciasSql.setJMenuBar(menuBar);
		
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
		
		panelSuperior = new JPanel();
		frmSentenciasSql.getContentPane().add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new BorderLayout(0, 0));
		
		panelBuscar = new JPanel();
		panelSuperior.add(panelBuscar, BorderLayout.NORTH);
		
		panelDatos = new JPanel();
		panelSuperior.add(panelDatos, BorderLayout.SOUTH);
		
		panelInferior = new JPanel();
		frmSentenciasSql.getContentPane().add(panelInferior, BorderLayout.SOUTH);
		
		lbTotalPuntos = new JLabel("Total Puntos:");
		panelInferior.add(lbTotalPuntos);
		
		btNuevo = new JButton("Nuevo");
		btNuevo.setEnabled(false);
		btNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nuevo();
			}
		});
		panelDatos.add(btNuevo);
		
		btModificar = new JButton("Modificar");
		btModificar.setEnabled(false);
		btModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modificar();
			}
		});
		panelDatos.add(btModificar);
		
		btEliminar = new JButton("Eliminar");
		btEliminar.setEnabled(false);
		btEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminar();
			}
		});
		panelDatos.add(btEliminar);
		
		btEliminarTodos = new JButton("Eliminar Todos");
		btEliminarTodos.setEnabled(false);
		btEliminarTodos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminarTodos();
			}
		});
		panelDatos.add(btEliminarTodos);
		
		txtFiltro = new JTextField();
		txtFiltro.setEnabled(false);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				buscar();
			}
		});
		panelBuscar.add(txtFiltro);
		txtFiltro.setColumns(10);
		
		btBuscar = new JButton("Buscar");
		btBuscar.setEnabled(false);
		btBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buscar();
			}
		});
		panelBuscar.add(btBuscar);
		
		btCancelar = new JButton("X");
		btCancelar.setEnabled(false);
		btCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelarBusqueda();
			}
		});
		panelBuscar.add(btCancelar);
		
		scrollPane = new JScrollPane();
		frmSentenciasSql.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		tablaDatos = new JTablaDatos();
		scrollPane.setViewportView(tablaDatos);
	}

}
