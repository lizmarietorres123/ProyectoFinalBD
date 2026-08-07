package visual.standby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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

import controlador.UsuarioController;
import logico.catalogo.Usuario;

public class RegistrarUsuario extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JCheckBox chkMostrarPassword;
	private JComboBox<String> cbTipo;
	private JButton btnGuardar;
	private JButton btnCancelar;

	private Usuario usuarioEdicion = null;
	private boolean esModoVerDetalles = false;
	private UsuarioController usuarioController;

	public RegistrarUsuario(Usuario auxUsuario) {
		this(auxUsuario, false);
	}

	public RegistrarUsuario(Usuario auxUsuario, boolean esVerDetalles) {
		this.usuarioController = new UsuarioController();
		this.usuarioEdicion = auxUsuario;
		this.esModoVerDetalles = esVerDetalles;

		actualizarTitulo();
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 360, 310);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(
				new LineBorder(new Color(135, 206, 235), 2),
				esModoVerDetalles ? "Detalles del Usuario" : (usuarioEdicion == null ? "Datos del Usuario" : "Modificar Usuario"),
				TitledBorder.CENTER,
				TitledBorder.TOP,
				new Font("Verdana", Font.BOLD, 11),
				new Color(70, 130, 180)
		));
		panel.setBackground(Color.WHITE);
		contentPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblUsername = new JLabel("Usuario:");
		lblUsername.setBounds(20, 35, 90, 20);
		lblUsername.setForeground(new Color(70, 130, 180));
		lblUsername.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblUsername);

		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Verdana", Font.PLAIN, 10));
		txtUsername.setBackground(new Color(224, 247, 250));
		txtUsername.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtUsername.setBounds(115, 37, 190, 20);
		panel.add(txtUsername);

		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(20, 75, 90, 20);
		lblPassword.setForeground(new Color(70, 130, 180));
		lblPassword.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Verdana", Font.PLAIN, 10));
		txtPassword.setBackground(new Color(224, 247, 250));
		txtPassword.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtPassword.setBounds(115, 77, 190, 20);
		panel.add(txtPassword);

		// Opción para Mostrar Contraseña
		chkMostrarPassword = new JCheckBox("Mostrar contraseña");
		chkMostrarPassword.setFont(new Font("Verdana", Font.PLAIN, 9));
		chkMostrarPassword.setBackground(Color.WHITE);
		chkMostrarPassword.setForeground(new Color(70, 130, 180));
		chkMostrarPassword.setBounds(115, 100, 190, 20);
		chkMostrarPassword.addActionListener(e -> {
			if (chkMostrarPassword.isSelected()) {
				txtPassword.setEchoChar((char) 0);
			} else {
				txtPassword.setEchoChar('•');
			}
		});
		panel.add(chkMostrarPassword);

		JLabel lblTipo = new JLabel("Tipo/Rol:");
		lblTipo.setBounds(20, 135, 90, 20);
		lblTipo.setForeground(new Color(70, 130, 180));
		lblTipo.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblTipo);

		cbTipo = new JComboBox<>(new String[] { "Administrador", "Doctor", "Asistente" });
		cbTipo.setFont(new Font("Verdana", Font.PLAIN, 10));
		cbTipo.setBackground(new Color(224, 247, 250));
		cbTipo.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		cbTipo.setBounds(115, 137, 190, 22);
		panel.add(cbTipo);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 248, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPanel.add(buttonPane, BorderLayout.SOUTH);

		btnGuardar = new JButton();
		btnGuardar.setFont(new Font("Verdana", Font.BOLD, 10));
		btnGuardar.setBackground(new Color(176, 224, 230));
		btnGuardar.setForeground(new Color(70, 130, 180));
		btnGuardar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnGuardar.setFocusPainted(false);
		buttonPane.add(btnGuardar);

		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (esModoVerDetalles) {
					// Cambiar a modo edición dentro de la misma ventana
					esModoVerDetalles = false;
					actualizarEstadoCampos(true);
					actualizarTitulo();
					btnGuardar.setText("Guardar Cambios");
					return;
				}

				String username = txtUsername.getText().trim();
				String password = new String(txtPassword.getPassword()).trim();
				String tipo = (String) cbTipo.getSelectedItem();

				if (username.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Por favor complete todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (usuarioEdicion == null) {
					Usuario nuevoUsuario = new Usuario(username, password, tipo);
					usuarioController.registrar(nuevoUsuario);
					JOptionPane.showMessageDialog(null, "¡Usuario registrado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				} else {
					usuarioController.actualizar(usuarioEdicion, username, password, tipo);
					JOptionPane.showMessageDialog(null, "¡Usuario modificado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				}
				dispose();
			}
		});

		btnCancelar = new JButton(esModoVerDetalles ? "Cerrar" : "Cancelar");
		btnCancelar.setFont(new Font("Verdana", Font.BOLD, 10));
		btnCancelar.setBackground(new Color(176, 224, 230));
		btnCancelar.setForeground(new Color(70, 130, 180));
		btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCancelar.setFocusPainted(false);
		buttonPane.add(btnCancelar);

		btnCancelar.addActionListener(e -> dispose());

		if (usuarioEdicion != null) {
			txtUsername.setText(usuarioEdicion.getNombre());
			txtPassword.setText(usuarioEdicion.getPassword());
			cbTipo.setSelectedItem(usuarioEdicion.getTipo());
		}

		if (esModoVerDetalles) {
			actualizarEstadoCampos(false);
			btnGuardar.setText("Modificar");
		} else {
			btnGuardar.setText(usuarioEdicion == null ? "Registrar" : "Guardar Cambios");
		}
	}

	private void actualizarTitulo() {
		setTitle(esModoVerDetalles ? "Detalles del Usuario" : (usuarioEdicion == null ? "Registrar Usuario" : "Modificar Usuario"));
	}

	private void actualizarEstadoCampos(boolean habilitado) {
		txtUsername.setEditable(habilitado);
		txtPassword.setEditable(habilitado);
		cbTipo.setEnabled(habilitado);
		btnCancelar.setText(habilitado ? "Cancelar" : "Cerrar");
	}
}