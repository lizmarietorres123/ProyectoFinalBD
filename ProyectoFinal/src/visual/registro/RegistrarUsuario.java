package visual.registro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controllers.UsuarioController;

public class RegistrarUsuario extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtCodigo;
	private JTextField txtNombre;
	private JPasswordField txtContrasenia;
	private JComboBox<String> cbxTipo;
	
	private UsuarioController controller;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegistrarUsuario dialog = new RegistrarUsuario();
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegistrarUsuario() {
		setTitle("Registrar Usuario");
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 340, 276);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Datos del Usuario", TitledBorder.CENTER, TitledBorder.TOP, new Font("Verdana", Font.BOLD, 12), new Color(70, 130, 180)));
		panel.setBackground(Color.WHITE);
		contentPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblCodigo = new JLabel("Código:");
		lblCodigo.setBounds(23, 27, 80, 20);
		lblCodigo.setForeground(new Color(70, 130, 180));
		lblCodigo.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblCodigo);

		txtCodigo = new JTextField();
		txtCodigo.setFont(new Font("Verdana", Font.PLAIN, 10));
		txtCodigo.setBackground(new Color(224, 247, 250));
		txtCodigo.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtCodigo.setBounds(23, 49, 71, 15);
		txtCodigo.setEditable(false); 
		txtCodigo.setText(controller.getNextCode());
		panel.add(txtCodigo);
		txtCodigo.setColumns(10);
		
		JLabel lblNombre = new JLabel("Usuario:");
		lblNombre.setBounds(23, 75, 80, 20);
		lblNombre.setForeground(new Color(70, 130, 180));
		lblNombre.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setFont(new Font("Verdana", Font.PLAIN, 10));
		txtNombre.setBackground(new Color(224, 247, 250));
		txtNombre.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtNombre.setBounds(23, 96, 120, 15);
		panel.add(txtNombre);
		txtNombre.setColumns(10);
		
		JLabel lblContrasenia = new JLabel("Contraseña:");
		lblContrasenia.setBounds(183, 75, 90, 20);
		lblContrasenia.setForeground(new Color(70, 130, 180));
		lblContrasenia.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblContrasenia);

		txtContrasenia = new JPasswordField();
		txtContrasenia.setFont(new Font("Verdana", Font.PLAIN, 10));
		txtContrasenia.setBackground(new Color(224, 247, 250));
		txtContrasenia.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtContrasenia.setBounds(183, 96, 107, 15);
		panel.add(txtContrasenia);
		
		JLabel lblTipo = new JLabel("Tipo de Usuario:");
		lblTipo.setBounds(23, 130, 120, 20);
		lblTipo.setForeground(new Color(70, 130, 180));
		lblTipo.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblTipo);

		cbxTipo = new JComboBox<String>();
		cbxTipo.setBounds(23, 152, 250, 20);
		cbxTipo.setFont(new Font("Verdana", Font.PLAIN, 10));
		cbxTipo.setBackground(new Color(224, 247, 250));
		cbxTipo.setModel(new DefaultComboBoxModel<String>(new String[] {"<<Seleccione>>", "Administrador", "Médico", "Secretario/a"}));
		panel.add(cbxTipo);
		
		controller = new UsuarioController();
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 248, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPanel.add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.setFont(new Font("Verdana", Font.BOLD, 10));
		btnRegistrar.setBackground(new Color(176, 224, 230));
		btnRegistrar.setForeground(new Color(70, 130, 180));
		btnRegistrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnRegistrar.setFocusPainted(false);
		btnRegistrar.setActionCommand("OK");
		buttonPane.add(btnRegistrar);
		getRootPane().setDefaultButton(btnRegistrar);
		
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String codigo = txtCodigo.getText();
				String nombre = txtNombre.getText();
				String contrasenia = new String(txtContrasenia.getPassword());
				String tipo = cbxTipo.getSelectedItem().toString();
				
				boolean exito = controller.guardarUsuario(codigo, nombre, contrasenia, tipo);
				
				if (exito) {
					JOptionPane.showMessageDialog(null, "¡Usuario registrado con éxito!", "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
			}
		});
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Verdana", Font.BOLD, 10));
		btnCancelar.setBackground(new Color(176, 224, 230));
		btnCancelar.setForeground(new Color(70, 130, 180));
		btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCancelar.setFocusPainted(false);
		btnCancelar.setActionCommand("Cancel");
		buttonPane.add(btnCancelar);
		
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}