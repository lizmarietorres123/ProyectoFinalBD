package visual.consultorio;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import bd.catalogo.PacienteDAO;
import logico.Clinica;
import logico.consultorio.Paciente;
import utilidad.Formato;

import java.math.BigDecimal;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.util.Calendar;

public class CrearPaciente extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextField txtApellido;
	private JTextField txtTelefono;
	private JTextField txtCedula;
	private JTextField txtPeso;
	private JTextField txtEstatura;
	private JSpinner spnFechaNacim;
	private JComboBox<String> cbxSexo;
	private JComboBox<String> cbxTipoSangre;
	private JComboBox<String> cbxEstado;
	private JTextArea txtDireccion;

	private Paciente miPaciente = null;
	private Paciente pacienteCreado = null;

	public static void main(String[] args) {
		try {
			CrearPaciente dialog = new CrearPaciente(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CrearPaciente(Paciente pac) {
		this.miPaciente = pac;

		if (miPaciente == null) {
			setTitle("Registrar Paciente");
		} else {
			setTitle("Modificar Paciente");
		}

		setBounds(100, 100, 631, 480);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setForeground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Datos del Paciente", TitledBorder.CENTER, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(70, 130, 180)));
		panel.setForeground(Color.BLACK);
		panel.setBackground(Color.WHITE);
		panel.setBounds(12, 13, 580, 235);
		contentPanel.add(panel);
		panel.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(12, 40, 60, 20);
		lblNombre.setForeground(new Color(70, 130, 180));
		lblNombre.setFont(new Font("Verdana", Font.BOLD, 12));
		panel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtNombre.setBackground(new Color(224, 247, 250));
		txtNombre.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtNombre.setBounds(77, 39, 190, 20);
		panel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setBounds(280, 40, 65, 20);
		lblApellido.setForeground(new Color(70, 130, 180));
		lblApellido.setFont(new Font("Verdana", Font.BOLD, 12));
		panel.add(lblApellido);

		txtApellido = new JTextField();
		txtApellido.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtApellido.setBackground(new Color(224, 247, 250));
		txtApellido.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtApellido.setBounds(350, 39, 215, 20);
		panel.add(txtApellido);
		txtApellido.setColumns(10);

		JLabel lblCedula = new JLabel("Cédula:");
		lblCedula.setBounds(369, 90, 60, 20);
		lblCedula.setForeground(new Color(70, 130, 180));
		lblCedula.setFont(new Font("Verdana", Font.BOLD, 12));
		panel.add(lblCedula);

		txtTelefono = new JTextField();
		txtTelefono.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtTelefono.setBackground(new Color(224, 247, 250));
		txtTelefono.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtTelefono.setBounds(447, 118, 118, 20);
		panel.add(txtTelefono);
		txtTelefono.setColumns(10);

		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setBounds(369, 117, 66, 20);
		lblTelefono.setForeground(new Color(70, 130, 180));
		lblTelefono.setFont(new Font("Verdana", Font.BOLD, 12));
		panel.add(lblTelefono);

		txtCedula = new JTextField();
		txtCedula.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtCedula.setBackground(new Color(224, 247, 250));
		txtCedula.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtCedula.setBounds(447, 91, 118, 20);
		panel.add(txtCedula);
		txtCedula.setColumns(10);

		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setBounds(258, 104, 43, 20);
		lblSexo.setForeground(new Color(70, 130, 180));
		lblSexo.setFont(new Font("Verdana", Font.BOLD, 12));
		panel.add(lblSexo);

		cbxSexo = new JComboBox<>();
		cbxSexo.setBounds(302, 101, 43, 26);
		cbxSexo.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		cbxSexo.setBackground(new Color(224, 247, 250));
		cbxSexo.setModel(new DefaultComboBoxModel<>(new String[] {"F", "M"}));
		panel.add(cbxSexo);

		JLabel lblFechaNacimiento = new JLabel("<html>Fecha de<br>Nacimiento:<html>");
		lblFechaNacimiento.setBounds(12, 100, 92, 29);
		panel.add(lblFechaNacimiento);
		lblFechaNacimiento.setForeground(new Color(70, 130, 180));
		lblFechaNacimiento.setFont(new Font("Verdana", Font.BOLD, 12));

		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setBounds(12, 159, 80, 20);
		lblDireccion.setForeground(new Color(70, 130, 180));
		lblDireccion.setFont(new Font("Verdana", Font.BOLD, 12));
		panel.add(lblDireccion);

		spnFechaNacim = new JSpinner();
		spnFechaNacim.setModel(new SpinnerDateModel(new Date(974260800000L), new Date(-1576697400000L), new Date(), Calendar.DAY_OF_YEAR));
		spnFechaNacim.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		spnFechaNacim.setBackground(new Color(224, 247, 250));
		Formato.setSpinner(spnFechaNacim);
		spnFechaNacim.setBounds(97, 101, 107, 26);
		panel.add(spnFechaNacim);

		JPanel panelDireccion = new JPanel();
		panelDireccion.setBounds(97, 159, 468, 49);
		panel.add(panelDireccion);
		panelDireccion.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollDireccion = new JScrollPane();
		scrollDireccion.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		panelDireccion.add(scrollDireccion, BorderLayout.CENTER);

		txtDireccion = new JTextArea();
		txtDireccion.setLineWrap(true);
		txtDireccion.setWrapStyleWord(true);
		txtDireccion.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtDireccion.setBackground(new Color(224, 247, 250));
		scrollDireccion.setViewportView(txtDireccion);

		JPanel panelCondicion = new JPanel();
		panelCondicion.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Condición", TitledBorder.CENTER, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(70, 130, 180)));
		panelCondicion.setBounds(15, 258, 580, 110);
		panelCondicion.setBackground(Color.WHITE);
		contentPanel.add(panelCondicion);
		panelCondicion.setLayout(null);

		JLabel lblPeso = new JLabel("Peso:");
		lblPeso.setBounds(15, 30, 49, 20);
		panelCondicion.add(lblPeso);
		lblPeso.setForeground(new Color(70, 130, 180));
		lblPeso.setFont(new Font("Bahnschrift", Font.BOLD, 13));

		txtPeso = new JTextField();
		txtPeso.setBounds(60, 27, 49, 26);
		txtPeso.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtPeso.setBackground(new Color(224, 247, 250));
		txtPeso.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		panelCondicion.add(txtPeso);
		txtPeso.setColumns(10);

		JLabel lblPesoMedida = new JLabel("lb");
		lblPesoMedida.setForeground(new Color(70, 130, 180));
		lblPesoMedida.setFont(new Font("Verdana", Font.BOLD, 12));
		lblPesoMedida.setBounds(115, 30, 33, 20);
		panelCondicion.add(lblPesoMedida);

		JLabel lblEstatura = new JLabel("Estatura:");
		lblEstatura.setForeground(new Color(70, 130, 180));
		lblEstatura.setFont(new Font("Verdana", Font.BOLD, 12));
		lblEstatura.setBounds(200, 30, 66, 20);
		panelCondicion.add(lblEstatura);

		txtEstatura = new JTextField();
		txtEstatura.setBounds(270, 27, 41, 26);
		txtEstatura.setFont(new Font("Verdana", Font.BOLD, 12));
		txtEstatura.setBackground(new Color(224, 247, 250));
		txtEstatura.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		panelCondicion.add(txtEstatura);
		txtEstatura.setColumns(10);

		JLabel lblEstMedida = new JLabel("ft");
		lblEstMedida.setForeground(new Color(70, 130, 180));
		lblEstMedida.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblEstMedida.setBounds(315, 30, 33, 20);
		panelCondicion.add(lblEstMedida);

		JLabel lblTipoDeSangre = new JLabel("Tipo de Sangre:");
		lblTipoDeSangre.setForeground(new Color(70, 130, 180));
		lblTipoDeSangre.setFont(new Font("Verdana", Font.BOLD, 12));
		lblTipoDeSangre.setBounds(383, 30, 114, 20);
		panelCondicion.add(lblTipoDeSangre);

		cbxTipoSangre = new JComboBox<>();
		cbxTipoSangre.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		cbxTipoSangre.setBackground(new Color(224, 247, 250));
		cbxTipoSangre.setModel(new DefaultComboBoxModel<>(new String[] {"A+", "A−", "B+", "B−", "AB+", "AB−", "O+", "O-"}));
		cbxTipoSangre.setBounds(494, 27, 71, 26);
		panelCondicion.add(cbxTipoSangre);

		JLabel lblEstado = new JLabel("Estado:");
		lblEstado.setForeground(new Color(70, 130, 180));
		lblEstado.setFont(new Font("Verdana", Font.BOLD, 12));
		lblEstado.setBounds(15, 70, 60, 20);
		panelCondicion.add(lblEstado);

		cbxEstado = new JComboBox<>();
		cbxEstado.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		cbxEstado.setBackground(new Color(224, 247, 250));
		cbxEstado.setModel(new DefaultComboBoxModel<>(new String[] {"Activo", "Inactivo"}));
		cbxEstado.setBounds(77, 68, 120, 26);
		panelCondicion.add(cbxEstado);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 248, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnRegistrar = new JButton(miPaciente != null ? "Modificar" : "Registrar");
		btnRegistrar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnRegistrar.setBackground(new Color(176, 224, 230));
		btnRegistrar.setForeground(new Color(70, 130, 180));
		btnRegistrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnRegistrar.setFocusPainted(false);
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (miPaciente != null) {
					modificarPaciente();
				} else {
					registrarPaciente();
				}
			}
		});
		btnRegistrar.setActionCommand("OK");
		buttonPane.add(btnRegistrar);
		getRootPane().setDefaultButton(btnRegistrar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnCancelar.setBackground(new Color(176, 224, 230));
		btnCancelar.setForeground(new Color(70, 130, 180));
		btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCancelar.setFocusPainted(false);
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setActionCommand("Cancel");
		buttonPane.add(btnCancelar);

		cargarDatos();
	}

	private void registrarPaciente() {
		String nombre = txtNombre.getText();
		String apellido = txtApellido.getText();
		String cedula = txtCedula.getText();
		String telefono = txtTelefono.getText();
		Date fecNacim = (Date) spnFechaNacim.getValue();
		String sexo = (String) cbxSexo.getSelectedItem();
		String pesoStr = txtPeso.getText();
		String estaturaStr = txtEstatura.getText();
		String tipoSangre = (String) cbxTipoSangre.getSelectedItem();
		String direccion = txtDireccion.getText();
		String estado = (String) cbxEstado.getSelectedItem();

		if (Formato.entradaVacia(nombre, "Debe ingresar el nombre del paciente.")) return;
		if (Formato.entradaVacia(apellido, "Debe ingresar el apellido del paciente.")) return;
		if (Formato.entradaVacia(cedula, "Debe ingresar la cédula del paciente.")) return;

		if (existeCedula(cedula.trim())) {
			Formato.entradaVacia("", "Ya existe un paciente registrado con esta cédula.");
			return;
		}

		if (Formato.entradaVacia(telefono, "Debe ingresar el teléfono del paciente.")) return;
		if (Formato.verificarEntradaRegex(telefono.trim(), "^[0-9-]+$", "El teléfono solo puede contener números y guiones.")) return;
		if (Formato.entradaVacia(direccion, "Debe ingresar la dirección del paciente.")) return;
		if (Formato.entradaVacia(pesoStr, "Debe ingresar el peso del paciente.")) return;
		if (Formato.verificarEntradaRegex(pesoStr.trim(), "^[0-9]+(\\.[0-9]+)?$", "El peso debe ser un número válido.")) return;
		if (Formato.entradaVacia(estaturaStr, "Debe ingresar la estatura del paciente.")) return;
		if (Formato.verificarEntradaRegex(estaturaStr.trim(), "^[0-9]+(\\.[0-9]+)?$", "La estatura debe ser un número válido.")) return;
		BigDecimal pesoVal = new BigDecimal(pesoStr.trim());
		BigDecimal estaturaVal = new BigDecimal(estaturaStr.trim());

		Paciente paciente = new Paciente(
				Clinica.genCodigoPacientes,
				nombre.trim(),
				apellido.trim(),
				cedula.trim(),
				telefono.trim(),
				fecNacim,
				sexo,
				pesoVal,
				estaturaVal,
				tipoSangre,
				direccion.trim(),
				estado
		);

		PacienteDAO.getInstance().guardarPaciente(paciente);

		Clinica.getInstancia().regPaciente(paciente);
		this.pacienteCreado = paciente;

		JOptionPane.showMessageDialog(null, "Paciente registrado con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}

	private boolean existeCedula(String cedula) {
		if (cedula == null || Clinica.getInstancia().getPacientes() == null) {
			return false;
		}
		String cedulaLimpia = cedula.replace("-", "").replaceAll("\\s+", "");

		for (Paciente p : Clinica.getInstancia().getPacientes()) {
			if (p.getCedula() != null) {
				String cedulaP = p.getCedula().replace("-", "").replaceAll("\\s+", "");
				if (cedulaP.equalsIgnoreCase(cedulaLimpia)) {
					return true;
				}
			}
		}
		return false;
	}

	private void modificarPaciente() {
		if (miPaciente == null) return;

		String nombre = txtNombre.getText();
		String apellido = txtApellido.getText();
		String telefono = txtTelefono.getText();
		Date fecNacim = (Date) spnFechaNacim.getValue();
		String sexo = (String) cbxSexo.getSelectedItem();
		String pesoStr = txtPeso.getText();
		String estaturaStr = txtEstatura.getText();
		String tipoSangre = (String) cbxTipoSangre.getSelectedItem();
		String direccion = txtDireccion.getText();
		String estado = (String) cbxEstado.getSelectedItem();

		if (Formato.entradaVacia(nombre, "Debe ingresar el nombre del paciente.")) return;
		if (Formato.entradaVacia(apellido, "Debe ingresar el apellido del paciente.")) return;
		if (Formato.entradaVacia(telefono, "Debe ingresar el teléfono del paciente.")) return;
		if (Formato.verificarEntradaRegex(telefono.trim(), "^[0-9-]+$", "El teléfono solo puede contener números y guiones.")) return;
		if (Formato.entradaVacia(direccion, "Debe ingresar la dirección del paciente.")) return;
		if (Formato.entradaVacia(pesoStr, "Debe ingresar el peso del paciente.")) return;
		if (Formato.verificarEntradaRegex(pesoStr.trim(), "^[0-9]+(\\.[0-9]+)?$", "El peso debe ser un número válido.")) return;
		if (Formato.entradaVacia(estaturaStr, "Debe ingresar la estatura del paciente.")) return;
		if (Formato.verificarEntradaRegex(estaturaStr.trim(), "^[0-9]+(\\.[0-9]+)?$", "La estatura debe ser un número válido.")) return;

		BigDecimal pesoVal = new BigDecimal(pesoStr.trim());
		BigDecimal estaturaVal = new BigDecimal(estaturaStr.trim());

		miPaciente.setNombre(nombre.trim());
		miPaciente.setApellido(apellido.trim());
		miPaciente.setTelefono(telefono.trim());
		miPaciente.setFecNacim(fecNacim);
		miPaciente.setSexo(sexo);
		miPaciente.setPeso(pesoVal);
		miPaciente.setEstatura(estaturaVal);
		miPaciente.setTipoSangre(tipoSangre);
		miPaciente.setDireccion(direccion.trim());
		miPaciente.setEstado(estado);

		PacienteDAO.getInstance().actualizarPaciente(miPaciente);

		JOptionPane.showMessageDialog(null, "Paciente modificado con éxito.", "Modificación Exitosa", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}

	public Paciente getPacienteCreado() {
		return pacienteCreado;
	}

	public void setDatosIniciales(String cedula, String nombre, String apellido) {
		if (miPaciente == null) {
			if (cedula != null && !cedula.isEmpty()) {
				txtCedula.setText(cedula);
			}
			if (nombre != null && !nombre.isEmpty()) {
				txtNombre.setText(nombre);
			}
			if (apellido != null && !apellido.isEmpty()) {
				txtApellido.setText(apellido);
			}
		}
	}

	private void cargarDatos() {
		if (miPaciente != null) {
			txtNombre.setText(miPaciente.getNombre());
			txtApellido.setText(miPaciente.getApellido());
			txtCedula.setText(miPaciente.getCedula());
			txtCedula.setEditable(false);
			txtTelefono.setText(miPaciente.getTelefono());
			txtDireccion.setText(miPaciente.getDireccion());
			txtPeso.setText(miPaciente.getPeso() != null ? miPaciente.getPeso().toString() : "");
			txtEstatura.setText(miPaciente.getEstatura() != null ? miPaciente.getEstatura().toString() : "");
			cbxSexo.setSelectedItem(miPaciente.getSexo());
			cbxTipoSangre.setSelectedItem(miPaciente.getTipoSangre());
			if (miPaciente.getEstado() != null) {
				cbxEstado.setSelectedItem(miPaciente.getEstado());
			}
			if (miPaciente.getFecNacim() != null) {
				spnFechaNacim.setValue(miPaciente.getFecNacim());
			}
		}
	}
}