package org.sfsoft.holasql.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Diálogo para recoger los datos de conexión con un SGBD
 * @author Santiago Faci
 * @version 1.0
 */
public class JConecta extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtUsuario;
	private JPasswordField txtContrasena;
	
	private String usuario;
	private String contrasena;
	public enum Accion {
		ACEPTAR, CANCELAR
	}
	private Accion accion;

	/**
	 * El usuario ha pulsado aceptar. Se recogen los datos del formulario como atributos de la clase
	 * y se esconde el formulario
	 */
	private void aceptar() {
		
		usuario = txtUsuario.getText();
		contrasena = String.valueOf(txtContrasena.getPassword());
		
		accion = Accion.ACEPTAR;
		setVisible(false);
	}
	
	/**
	 * El usuario cancela. Se esconde el formulario 
	 */
	private void cancelar() {
		
		accion = Accion.CANCELAR;
		setVisible(false);
	}
	
	/*
	 * Getters para la recogida de información del formulario desde la ventana principal 
	 * de la aplicación
	 */
	
	public String getUsuario() {
		return usuario;
	}
	
	public String getContrasena() {
		return contrasena;
	}
	
	public Accion getAccion() {
		return accion;
	}
	
	public Accion mostrarDialogo() {
		setVisible(true);
		
		return accion;
	}
	
	/**
	 * Crea el diálogo y lo muestra en pantalla
	 */
	public JConecta() {
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setModal(true);
		setTitle("Conectar");
		setBounds(100, 100, 271, 161);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setBounds(23, 35, 66, 14);
		contentPanel.add(lblUsuario);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a");
		lblContrasea.setBounds(23, 65, 66, 14);
		contentPanel.add(lblContrasea);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(136, 32, 106, 20);
		contentPanel.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		txtContrasena = new JPasswordField();
		txtContrasena.setBounds(136, 62, 106, 20);
		contentPanel.add(txtContrasena);
		txtContrasena.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						aceptar();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelar();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setLocationRelativeTo(null);
	}
	
}
