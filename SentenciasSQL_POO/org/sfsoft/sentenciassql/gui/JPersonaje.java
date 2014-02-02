package org.sfsoft.sentenciassql.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;

import org.sfsoft.sentenciassql.base.Personaje;
import org.sfsoft.sentenciassql.util.Constantes.AccionDialogo;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Dialog con el que el usuario introduce información o modifica un Personaje
 * @author Santiago Faci
 * @version 1.0
 */
public class JPersonaje extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextField txtNivel;
	private JTextField txtEnergia;
	private JTextField txtPuntos;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	
	private Personaje personaje;
	private AccionDialogo accion;

	/**
	 * Getters y setters para obtener y fijar información en la ventana
	 * @return
	 */
	
	public void setPersonaje(Personaje personaje) {
		this.personaje = personaje;
		
		txtNombre.setText(personaje.getNombre());
		txtNivel.setText(String.valueOf(personaje.getNivel()));
		txtEnergia.setText(String.valueOf(personaje.getEnergia()));
		txtPuntos.setText(String.valueOf(personaje.getPuntos()));	
	}
	
	public Personaje getPersonaje() {
		
		return personaje;
	}
	
	public AccionDialogo mostrarDialogo() {
		
		setVisible(true);
		return accion;
	}
	
	/**
	 * Se invoca cuando el usuario ha pulsado en Aceptar. Recoge y valida la información de las cajas de texto
	 * y cierra la ventana
	 */
	private void aceptar() {
		
		if (txtNombre.getText().equals(""))
			return;
		
		try {
			if (txtNivel.getText().equals(""))
				txtNivel.setText("0");
			if (txtEnergia.getText().equals(""))
				txtEnergia.setText("0");
			if (txtPuntos.getText().equals(""))
				txtPuntos.setText("0");
			
			personaje = new Personaje();
			personaje.setNombre(txtNombre.getText());
			personaje.setNivel(Integer.parseInt(txtNivel.getText()));
			personaje.setEnergia(Integer.parseInt(txtEnergia.getText()));
			personaje.setPuntos(Integer.parseInt(txtPuntos.getText()));
			
			accion = AccionDialogo.ACEPTAR;
			setVisible(false);
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(null, "Comprueba que los datos son correctos", "Personaje", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Invocado cuando el usuario cancela. Simplemente cierra la ventana
	 */
	private void cancelar() {
		accion = AccionDialogo.CANCELAR;
		setVisible(false);
	}
	
	/**
	 * Constructor. Crea la ventana
	 */
	public JPersonaje() {
		setModal(true);
		setTitle("Personaje");
		setBounds(100, 100, 284, 253);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtNombre = new JTextField();
		txtNombre.setBounds(100, 22, 86, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);
		
		txtNivel = new JTextField();
		txtNivel.setBounds(100, 64, 60, 20);
		contentPanel.add(txtNivel);
		txtNivel.setColumns(10);
		
		txtEnergia = new JTextField();
		txtEnergia.setBounds(100, 106, 60, 20);
		contentPanel.add(txtEnergia);
		txtEnergia.setColumns(10);
		
		txtPuntos = new JTextField();
		txtPuntos.setBounds(100, 148, 60, 20);
		contentPanel.add(txtPuntos);
		txtPuntos.setColumns(10);
		
		lblNewLabel = new JLabel("Nombre");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(10, 25, 46, 14);
		contentPanel.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Nivel");
		lblNewLabel_1.setBounds(10, 67, 46, 14);
		contentPanel.add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("Energ\u00EDa");
		lblNewLabel_2.setBounds(10, 109, 46, 14);
		contentPanel.add(lblNewLabel_2);
		
		lblNewLabel_3 = new JLabel("Puntos");
		lblNewLabel_3.setBounds(10, 151, 46, 14);
		contentPanel.add(lblNewLabel_3);
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
	}
}
