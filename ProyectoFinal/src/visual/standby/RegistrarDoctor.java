package visual.standby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controlador.DoctorController;
import logico.consultorio.Clinica;
import logico.Doctor;
import logico.catalogo.Especialidad;

public class RegistrarDoctor extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtIdDoctor;
	private JTextField txtNombre;
	private JSpinner spnCupoDiario;
	private JComboBox<String> cbEspecialidades;
	private JButton btnGuardar;
	private JButton btnCancelar;

	private Doctor doctorEdicion = null;
	private boolean esModoVerDetalles = false;
	private DoctorController doctorController;

	public RegistrarDoctor(Doctor auxDoctor) {
		this(auxDoctor, false);
	}

	public RegistrarDoctor(Doctor auxDoctor, boolean esVerDetalles) {
		this.doctorController = new DoctorController();
		this.doctorEdicion = auxDoctor;
		this.esModoVerDetalles = esVerDetalles;

		actualizarTitulo();
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 370, 310);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(
				new LineBorder(new Color(135, 206, 235), 2),
				esModoVerDetalles ? "Detalles del Doctor" : (doctorEdicion == null ? "Información del Doctor" : "Modificar Información"),
				TitledBorder.CENTER,
				TitledBorder.TOP,
				new Font("Verdana", Font.BOLD, 11),
				new Color(70, 130, 180)
		));
		panel.setBackground(Color.WHITE);
		contentPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblId = new JLabel("Código ID:");
		lblId.setBounds(20, 30, 90, 20);
		lblId.setForeground(new Color(70, 130, 180));
		lblId.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblId);

		txtIdDoctor = new JTextField();
		txtIdDoctor.setFont(new Font("Verdana", Font.PLAIN, 10));
		txtIdDoctor.setBackground(new Color(224, 247, 250));
		txtIdDoctor.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtIdDoctor.setBounds(115, 30, 200, 20);
		txtIdDoctor.setEditable(false);
		panel.add(txtIdDoctor);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(20, 65, 90, 20);
		lblNombre.setForeground(new Color(70, 130, 180));
		lblNombre.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setFont(new Font("Verdana", Font.PLAIN, 10));
		txtNombre.setBackground(new Color(224, 247, 250));
		txtNombre.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtNombre.setBounds(115, 65, 200, 20);
		panel.add(txtNombre);

		JLabel lblCupo = new JLabel("Cupo Diario:");
		lblCupo.setBounds(20, 100, 90, 20);
		lblCupo.setForeground(new Color(70, 130, 180));
		lblCupo.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblCupo);

		spnCupoDiario = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
		spnCupoDiario.setFont(new Font("Verdana", Font.PLAIN, 10));
		spnCupoDiario.setBounds(115, 100, 200, 22);
		panel.add(spnCupoDiario);

		JLabel lblEspecialidades = new JLabel("Especialidad:");
		lblEspecialidades.setBounds(20, 135, 90, 20);
		lblEspecialidades.setForeground(new Color(70, 130, 180));
		lblEspecialidades.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblEspecialidades);

		// Selección Única de Especialidad
		cbEspecialidades = new JComboBox<>();
		cbEspecialidades.setFont(new Font("Verdana", Font.PLAIN, 10));
		cbEspecialidades.setBackground(new Color(224, 247, 250));
		cbEspecialidades.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		cbEspecialidades.setBounds(115, 135, 200, 22);

		ArrayList<Especialidad> espClinica = Clinica.getInstancia().getEspecialidades();
		if (espClinica != null && !espClinica.isEmpty()) {
			for (Especialidad esp : espClinica) {
				cbEspecialidades.addItem(esp.getNombre());
			}
		} else {
			cbEspecialidades.addItem("Medicina General");
			cbEspecialidades.addItem("Pediatría");
			cbEspecialidades.addItem("Cardiología");
		}
		panel.add(cbEspecialidades);

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
					esModoVerDetalles = false;
					actualizarEstadoCampos(true);
					actualizarTitulo();
					btnGuardar.setText("Guardar Cambios");
					return;
				}

				String id = txtIdDoctor.getText();
				String nombre = txtNombre.getText().trim();
				int cupo = (Integer) spnCupoDiario.getValue();
				String especialidadSeleccionada = (String) cbEspecialidades.getSelectedItem();

				if (nombre.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Debe ingresar el nombre del doctor.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (especialidadSeleccionada == null) {
					JOptionPane.showMessageDialog(null, "Seleccione una especialidad.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// Garantiza únicamente una especialidad
				ArrayList<String> auxEsp = new ArrayList<>();
				auxEsp.add(especialidadSeleccionada);

				if (doctorEdicion == null) {
					Doctor nuevoDoctor = new Doctor(id, nombre, cupo, auxEsp);
					doctorController.registrar(nuevoDoctor);
					JOptionPane.showMessageDialog(null, "¡Doctor registrado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				} else {
					doctorEdicion.setNombre(nombre);
					doctorEdicion.setCupoDiario(cupo);
					doctorEdicion.setEspecialidades(auxEsp);
					doctorController.actualizar(doctorEdicion);
					JOptionPane.showMessageDialog(null, "¡Doctor modificado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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

		if (doctorEdicion != null) {
			txtIdDoctor.setText(doctorEdicion.getIdDoctor());
			txtNombre.setText(doctorEdicion.getNombre());
			spnCupoDiario.setValue(doctorEdicion.getCupoDiario());

			if (doctorEdicion.getEspecialidades() != null && !doctorEdicion.getEspecialidades().isEmpty()) {
				cbEspecialidades.setSelectedItem(doctorEdicion.getEspecialidades().get(0));
			}
		} else {
			txtIdDoctor.setText("DOC-" + (doctorController.obtenerTodos().size() + 1));
		}

		if (esModoVerDetalles) {
			actualizarEstadoCampos(false);
			btnGuardar.setText("Modificar");
		} else {
			btnGuardar.setText(doctorEdicion == null ? "Registrar" : "Guardar Cambios");
		}
	}

	private void actualizarTitulo() {
		setTitle(esModoVerDetalles ? "Detalles del Doctor" : (doctorEdicion == null ? "Registrar Doctor" : "Modificar Doctor"));
	}

	private void actualizarEstadoCampos(boolean habilitado) {
		txtNombre.setEditable(habilitado);
		spnCupoDiario.setEnabled(habilitado);
		cbEspecialidades.setEnabled(habilitado);
		btnCancelar.setText(habilitado ? "Cancelar" : "Cerrar");
	}
}