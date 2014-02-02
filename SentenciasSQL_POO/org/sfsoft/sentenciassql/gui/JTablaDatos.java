package org.sfsoft.sentenciassql.gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.sfsoft.sentenciassql.base.Personaje;
import org.sfsoft.sentenciassql.util.Constantes;

/**
 * Clase que muestra el contenido de una tabla de MySQL en un JTable
 * Se han añadido métodos que permiten registrar, modificar y eliminar contenido de la Base de Datos
 * @author Santiago Faci
 * @version 2.0
 */
public class JTablaDatos extends JTable {
	
	private DefaultTableModel modeloDatos;
	
	// Indica si estamos trabajando en modo depuración
	private static final boolean DEBUG = false; 
	
	public JTablaDatos() {
		super();

		inicializar();
	}
	
	/**
	 * Inicializa la tabla, creando las columnas
	 */
	private void inicializar() {
		
		modeloDatos = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};
		
		modeloDatos.addColumn(Constantes.NOMBRE);
		modeloDatos.addColumn(Constantes.NIVEL);
		modeloDatos.addColumn(Constantes.ENERGIA);
		modeloDatos.addColumn(Constantes.PUNTOS);
		
		this.setModel(modeloDatos);
	}
	
	/**
	 * Lista el contenido de la tabla
	 * @throws SQLException En caso de que haya algún problema de conexión con MySQL
	 */
	public void listar() throws SQLException {
		
		ArrayList<Personaje> personajes;
		
		personajes = SentenciasSQL.database.getPersonajes();
		cargarFilas(personajes);
	}
	
	/**
	 * Lista el contenido de la tabla aplicando un filtro
	 * @param filtro 
	 * @throws SQLException En caso de que haya algún problema de conexión con MySQL
	 */
	public void listar(String filtro) throws SQLException {
		
		ArrayList<Personaje> personajes;
		
		personajes = 
			SentenciasSQL.database.getPersonajes(filtro);
		cargarFilas(personajes);
	}

	/*
	 * 'Pinta' los datos en el JTable
	 */
	private void cargarFilas(ArrayList<Personaje> personajes) throws SQLException {
	
		modeloDatos.setNumRows(0);
		
		for (Personaje personaje : personajes) {
			Object[] fila = new Object[]{
					personaje.getNombre(),
					String.valueOf(personaje.getNivel()), 
					String.valueOf(personaje.getEnergia()), 
					String.valueOf(personaje.getPuntos())};
			modeloDatos.addRow(fila);
		}
	}
	
	/**
	 * Elimina todo el contenido del control JTable
	 */
	public void vaciar() {
		
		modeloDatos.setNumRows(0);
	}
	
	/**
	 * Elimina, de la Base de Datos, el personaje seleccionado
	 * @throws SQLException En caso de que haya algún problema de conexión con MySQL
	 */
	public void eliminar() throws SQLException {
		
		int filaSeleccionada = 0;
		
		filaSeleccionada = getSelectedRow();
		if (filaSeleccionada == -1) 
			return;
		
		String nombreSeleccionado = (String) getValueAt(filaSeleccionada, 0);
		SentenciasSQL.database.eliminarPersonaje(nombreSeleccionado);
	}
	
	public Personaje getPersonajeSeleccionado() throws SQLException {
		
		String nombre = (String) getValueAt(getSelectedRow(), 0);
		
		return SentenciasSQL.database.getPersonaje(nombre);
	}
}
