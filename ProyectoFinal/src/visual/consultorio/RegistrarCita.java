package visual.consultorio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import logico.consultorio.Cita;
import logico.Clinica;
import logico.catalogo.Doctor;
import logico.catalogo.EstadoCita;
import logico.consultorio.Paciente;
import utilidad.Formato;

public class RegistrarCita extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtIdCita;
	private JSpinner spnFecha;
	private JComboBox<PacienteItem> cbxPaciente;
	private JComboBox<DoctorItem> cbxDoctor;
	private JLabel lblTipo;
	private JLabel lblInfoDoctor;
	private TitledBorder borderPanel;
	private JButton okButton;

	private boolean filtrandoDoctor = false;
	private boolean filtrandoPaciente = false;

	private Paciente auxPaciente;
	private Doctor auxDoctor;
	private Cita citaEditar;

	private static class PacienteItem {
		private final Paciente paciente;

		public PacienteItem(Paciente paciente) {
			this.paciente = paciente;
		}

		public Paciente getPaciente() {
			return paciente;
		}

		@Override
		public String toString() {
			if (paciente == null) {
				return "";
			}
			String apellido = (paciente.getApellido() != null) ? paciente.getApellido() : "";
			return paciente.getCedula() + " - " + (paciente.getNombre() + " " + apellido).trim();
		}
	}

	private class PacienteCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setBorder(new EmptyBorder(4, 6, 4, 6));

			if (value instanceof PacienteItem) {
				PacienteItem item = (PacienteItem) value;
				if (item.getPaciente() == null) {
					label.setText("-- Escriba cédula o nombre del paciente --");
					label.setForeground(Color.GRAY);
					label.setFont(new Font("Dialog", Font.ITALIC, 12));
				} else {
					Paciente p = item.getPaciente();
					String apellido = (p.getApellido() != null) ? p.getApellido() : "";
					label.setText("👤 " + p.getCedula() + " - " + (p.getNombre() + " " + apellido).trim());
					label.setFont(new Font("Dialog", Font.PLAIN, 12));

					if (isSelected) {
						label.setBackground(new Color(70, 130, 180));
						label.setForeground(Color.WHITE);
					} else {
						label.setBackground(Color.WHITE);
						label.setForeground(new Color(40, 40, 40));
					}
				}
			}
			return label;
		}
	}

	private static class DoctorItem {
		private final Doctor doctor;

		public DoctorItem(Doctor doctor) {
			this.doctor = doctor;
		}

		public Doctor getDoctor() {
			return doctor;
		}

		@Override
		public String toString() {
			if (doctor == null) {
				return "";
			}
			String especialidad = "";
			if (doctor.getEspecialidades() != null && !doctor.getEspecialidades().isEmpty()) {
				especialidad = " (" + doctor.getEspecialidades().get(0) + ")";
			}
			return "Dr. " + doctor.getNombre() + especialidad;
		}
	}

	private class DoctorCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setBorder(new EmptyBorder(4, 6, 4, 6));

			if (value instanceof DoctorItem) {
				DoctorItem item = (DoctorItem) value;
				if (item.getDoctor() == null) {
					label.setText("-- Escriba o seleccione un doctor --");
					label.setForeground(Color.GRAY);
					label.setFont(new Font("Dialog", Font.ITALIC, 12));
				} else {
					Doctor d = item.getDoctor();
					String especialidades = (d.getEspecialidades() != null && !d.getEspecialidades().isEmpty())
							? "  •  " + String.join(", ", d.getEspecialidades()) : "";
					label.setText("👨‍⚕️ Dr. " + d.getNombre() + especialidades);
					label.setFont(new Font("Dialog", Font.PLAIN, 12));

					if (isSelected) {
						label.setBackground(new Color(70, 130, 180));
						label.setForeground(Color.WHITE);
					} else {
						label.setBackground(Color.WHITE);
						label.setForeground(new Color(40, 40, 40));
					}
				}
			}
			return label;
		}
	}

	public RegistrarCita() {
		this(null);
	}

	public RegistrarCita(Cita cita) {
		this.citaEditar = cita;
		this.auxPaciente = null;
		this.auxDoctor = null;

		setTitle(citaEditar == null ? "Registrar: Cita" : "Modificar: Cita");
		setBounds(100, 100, 548, 340);
		setLocationRelativeTo(null);
		setModal(true);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(new Color(240, 248, 255));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(25, 10, 482, 240);
		borderPanel = new TitledBorder(
				new LineBorder(new Color(70, 130, 180), 1, true),
				citaEditar == null ? " Registrar Cita " : " Modificar Cita ",
				TitledBorder.LEFT,
				TitledBorder.TOP,
				new Font("Dialog", Font.BOLD, 13),
				new Color(70, 130, 180));
		panel.setBorder(borderPanel);
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		contentPanel.add(panel);

		JLabel lblCodigo = new JLabel("Código:");
		lblCodigo.setForeground(new Color(70, 130, 180));
		lblCodigo.setFont(new Font("Dialog", Font.BOLD, 12));
		lblCodigo.setBounds(25, 30, 60, 22);
		panel.add(lblCodigo);

		txtIdCita = new JTextField(citaEditar == null ? "CIT-" + Clinica.genCodigoCitas : citaEditar.getId());
		txtIdCita.setEnabled(false);
		txtIdCita.setEditable(false);
		txtIdCita.setFont(new Font("Dialog", Font.BOLD, 12));
		txtIdCita.setBackground(new Color(245, 245, 245));
		txtIdCita.setForeground(new Color(70, 130, 180));
		txtIdCita.setBorder(new LineBorder(new Color(200, 200, 200)));
		txtIdCita.setBounds(90, 30, 140, 22);
		panel.add(txtIdCita);

		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setForeground(new Color(70, 130, 180));
		lblFecha.setFont(new Font("Dialog", Font.BOLD, 12));
		lblFecha.setBounds(294, 30, 60, 22);
		panel.add(lblFecha);

		Date fechaInicial = (citaEditar != null && citaEditar.getFechaConsulta() != null) ? citaEditar.getFechaConsulta() : new Date();
		spnFecha = new JSpinner(new SpinnerDateModel(fechaInicial, null, null, Calendar.DAY_OF_YEAR));
		spnFecha.setFont(new Font("Dialog", Font.PLAIN, 12));
		spnFecha.setBorder(new LineBorder(new Color(70, 130, 180)));
		Formato.setSpinner(spnFecha);
		Formato.colorSpinner(spnFecha, new Color(240, 248, 255));
		spnFecha.setBounds(337, 30, 120, 22);
		panel.add(spnFecha);

		JLabel lblSeccionPaciente = new JLabel("DATOS DEL PACIENTE", SwingConstants.CENTER);
		lblSeccionPaciente.setOpaque(true);
		lblSeccionPaciente.setBackground(new Color(230, 242, 250));
		lblSeccionPaciente.setForeground(new Color(70, 130, 180));
		lblSeccionPaciente.setFont(new Font("Dialog", Font.BOLD, 11));
		lblSeccionPaciente.setBounds(25, 68, 433, 22);
		panel.add(lblSeccionPaciente);

		JLabel lblIdentificacion = new JLabel("Paciente:");
		lblIdentificacion.setForeground(new Color(70, 130, 180));
		lblIdentificacion.setFont(new Font("Dialog", Font.BOLD, 12));
		lblIdentificacion.setBounds(25, 100, 70, 22);
		panel.add(lblIdentificacion);

		cbxPaciente = new JComboBox<>();
		cbxPaciente.setEditable(true);
		cbxPaciente.setRenderer(new PacienteCellRenderer());
		cbxPaciente.setFont(new Font("Dialog", Font.PLAIN, 12));
		cbxPaciente.setBackground(Color.WHITE);
		cbxPaciente.setBorder(new LineBorder(new Color(70, 130, 180)));
		cbxPaciente.setBounds(95, 100, 243, 24);

		JTextField editorPaciente = (JTextField) cbxPaciente.getEditor().getEditorComponent();
		editorPaciente.setBackground(new Color(245, 250, 255));
		editorPaciente.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP ||
						e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					return;
				}

				String texto = editorPaciente.getText();
				int caretPosition = editorPaciente.getCaretPosition();

				actualizarComboPacientes(texto);

				editorPaciente.setText(texto);
				try {
					editorPaciente.setCaretPosition(Math.min(caretPosition, texto.length()));
				} catch (Exception ignored) {}

				if (!texto.trim().isEmpty() && cbxPaciente.getItemCount() > 1) {
					cbxPaciente.showPopup();
				}
			}
		});

		cbxPaciente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seleccionarPaciente();
			}
		});
		panel.add(cbxPaciente);

		JButton btnCrear = new JButton("+ Nuevo");
		btnCrear.setFont(new Font("Dialog", Font.BOLD, 11));
		btnCrear.setForeground(new Color(70, 130, 180));
		btnCrear.setBackground(new Color(240, 248, 255));
		btnCrear.setBounds(348, 100, 110, 22);
		btnCrear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abrirVentanaCrearPaciente();
			}
		});
		panel.add(btnCrear);

		lblTipo = new JLabel("");
		lblTipo.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblTipo.setBounds(95, 124, 243, 18);
		panel.add(lblTipo);

		JLabel lblSeccionDoctor = new JLabel("DATOS DEL DOCTOR", SwingConstants.CENTER);
		lblSeccionDoctor.setOpaque(true);
		lblSeccionDoctor.setBackground(new Color(230, 242, 250));
		lblSeccionDoctor.setForeground(new Color(70, 130, 180));
		lblSeccionDoctor.setFont(new Font("Dialog", Font.BOLD, 11));
		lblSeccionDoctor.setBounds(25, 145, 433, 22);
		panel.add(lblSeccionDoctor);

		JLabel lblDoctor = new JLabel("Doctor:");
		lblDoctor.setForeground(new Color(70, 130, 180));
		lblDoctor.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDoctor.setBounds(25, 178, 60, 22);
		panel.add(lblDoctor);

		cbxDoctor = new JComboBox<>();
		cbxDoctor.setEditable(true);
		cbxDoctor.setRenderer(new DoctorCellRenderer());
		cbxDoctor.setFont(new Font("Dialog", Font.PLAIN, 12));
		cbxDoctor.setBackground(Color.WHITE);
		cbxDoctor.setBorder(new LineBorder(new Color(70, 130, 180)));
		cbxDoctor.setBounds(95, 178, 363, 24);

		JTextField editorDoctor = (JTextField) cbxDoctor.getEditor().getEditorComponent();
		editorDoctor.setBackground(new Color(245, 250, 255));
		editorDoctor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP ||
						e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					return;
				}

				String texto = editorDoctor.getText();
				int caretPosition = editorDoctor.getCaretPosition();

				actualizarComboDoctores(texto);

				editorDoctor.setText(texto);
				try {
					editorDoctor.setCaretPosition(Math.min(caretPosition, texto.length()));
				} catch (Exception ignored) {}

				if (!texto.trim().isEmpty() && cbxDoctor.getItemCount() > 1) {
					cbxDoctor.showPopup();
				}
			}
		});

		cbxDoctor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seleccionarDoctor();
			}
		});
		panel.add(cbxDoctor);

		lblInfoDoctor = new JLabel("🩺 Escriba o seleccione un doctor...", SwingConstants.LEFT);
		lblInfoDoctor.setForeground(new Color(120, 130, 140));
		lblInfoDoctor.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblInfoDoctor.setBounds(95, 204, 363, 20);
		panel.add(lblInfoDoctor);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(Color.WHITE);
		buttonPane.setBorder(new LineBorder(new Color(70, 130, 180)));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton(citaEditar == null ? "Registrar" : "Guardar");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ejecutarGuardadoCita();
			}
		});
		okButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		okButton.setBackground(new Color(224, 247, 250));
		okButton.setForeground(new Color(70, 130, 180));
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancelar");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		cancelButton.setBackground(new Color(224, 247, 250));
		cancelButton.setForeground(new Color(70, 130, 180));
		buttonPane.add(cancelButton);

		actualizarComboPacientes("");
		actualizarComboDoctores("");

		if (citaEditar != null) {
			cargarDatosModificacion();
		}
	}

	private void cargarDatosModificacion() {
		if (citaEditar == null) return;

		if (citaEditar.getPaciente() != null) {
			auxPaciente = citaEditar.getPaciente();
			for (int i = 0; i < cbxPaciente.getItemCount(); i++) {
				PacienteItem item = cbxPaciente.getItemAt(i);
				if (item != null && item.getPaciente() != null && item.getPaciente().getCedula().equalsIgnoreCase(auxPaciente.getCedula())) {
					cbxPaciente.setSelectedIndex(i);
					lblTipo.setText("*Encontrado.");
					lblTipo.setForeground(new Color(0, 128, 0));
					break;
				}
			}
		}

		if (citaEditar.getDoctor() != null) {
			auxDoctor = citaEditar.getDoctor();
			for (int i = 0; i < cbxDoctor.getItemCount(); i++) {
				DoctorItem item = cbxDoctor.getItemAt(i);
				if (item != null && item.getDoctor() != null && item.getDoctor().getId().equalsIgnoreCase(auxDoctor.getId())) {
					cbxDoctor.setSelectedIndex(i);
					String esp = (auxDoctor.getEspecialidades() != null && !auxDoctor.getEspecialidades().isEmpty())
							? String.join(", ", auxDoctor.getEspecialidades()) : "General";
					lblInfoDoctor.setText("✓ " + auxDoctor.getNombre() + " (" + esp + ")");
					lblInfoDoctor.setForeground(new Color(0, 128, 0));
					break;
				}
			}
		}
	}

	private void actualizarComboPacientes(String filtro) {
		filtrandoPaciente = true;
		cbxPaciente.removeAllItems();
		cbxPaciente.addItem(new PacienteItem(null));

		String f = filtro.trim().toLowerCase();
		for (Paciente p : Clinica.getInstancia().getPacientes()) {
			String cedula = p.getCedula() != null ? p.getCedula().toLowerCase() : "";
			String nombre = p.getNombre() != null ? p.getNombre().toLowerCase() : "";
			String apellido = p.getApellido() != null ? p.getApellido().toLowerCase() : "";
			String nombreCompleto = (nombre + " " + apellido).trim();

			if (f.isEmpty() || cedula.contains(f) || nombreCompleto.contains(f)) {
				cbxPaciente.addItem(new PacienteItem(p));
			}
		}
		filtrandoPaciente = false;
	}

	private void seleccionarPaciente() {
		if (filtrandoPaciente) return;

		Object itemSeleccionado = cbxPaciente.getSelectedItem();
		if (itemSeleccionado instanceof PacienteItem) {
			PacienteItem item = (PacienteItem) itemSeleccionado;
			if (item.getPaciente() != null) {
				auxPaciente = item.getPaciente();
				lblTipo.setText("*Encontrado.");
				lblTipo.setForeground(new Color(0, 128, 0));
				return;
			}
		}

		auxPaciente = null;
		lblTipo.setText("");
	}

	private void abrirVentanaCrearPaciente() {
		cbxPaciente.setSelectedIndex(-1);
		auxPaciente = null;
		lblTipo.setText("");

		RegistrarPaciente regPacienteDialog = new RegistrarPaciente(null);

		JTextField editorPaciente = (JTextField) cbxPaciente.getEditor().getEditorComponent();
		String textoFiltro = editorPaciente.getText().trim();

		regPacienteDialog.setDatosIniciales(
				textoFiltro,
				"",
				""
		);

		regPacienteDialog.setModal(true);
		regPacienteDialog.setVisible(true);

		Paciente pacienteNuevo = regPacienteDialog.getPacienteCreado();
		if (pacienteNuevo != null) {
			auxPaciente = pacienteNuevo;
			actualizarComboPacientes("");

			for (int i = 0; i < cbxPaciente.getItemCount(); i++) {
				PacienteItem item = cbxPaciente.getItemAt(i);
				if (item.getPaciente() != null && item.getPaciente().getCedula().equalsIgnoreCase(pacienteNuevo.getCedula())) {
					cbxPaciente.setSelectedIndex(i);
					lblTipo.setText("*Encontrado.");
					lblTipo.setForeground(new Color(0, 128, 0));
					break;
				}
			}
		} else {
			((JTextField) cbxPaciente.getEditor().getEditorComponent()).setText("");
		}
	}

	private List<Doctor> obtenerDoctoresFiltrados(String filtro) {
		List<Doctor> doctoresEncontrados = new ArrayList<>();
		String filtroNorm = (filtro == null) ? "" : filtro.toLowerCase().trim();

		if (Clinica.getInstancia().getDoctores() != null) {
			for (Doctor d : Clinica.getInstancia().getDoctores()) {
				boolean matchNombre = d.getNombre() != null && d.getNombre().toLowerCase().contains(filtroNorm);
				boolean matchId = d.getId() != null && d.getId().toLowerCase().contains(filtroNorm);
				boolean matchEspecialidad = d.getEspecialidades() != null && d.getEspecialidades().stream()
						.anyMatch(esp -> esp.toLowerCase().contains(filtroNorm));

				if (filtroNorm.isEmpty() || matchNombre || matchId || matchEspecialidad) {
					doctoresEncontrados.add(d);
				}
			}
		}
		return doctoresEncontrados;
	}

	private void actualizarComboDoctores(String filtro) {
		filtrandoDoctor = true;
		cbxDoctor.removeAllItems();
		cbxDoctor.addItem(new DoctorItem(null));

		List<Doctor> doctores = obtenerDoctoresFiltrados(filtro);
		if (doctores != null) {
			for (Doctor d : doctores) {
				cbxDoctor.addItem(new DoctorItem(d));
			}
		}

		filtrandoDoctor = false;
	}

	private void seleccionarDoctor() {
		if (filtrandoDoctor) return;

		Object itemSeleccionado = cbxDoctor.getSelectedItem();
		if (itemSeleccionado instanceof DoctorItem) {
			DoctorItem item = (DoctorItem) itemSeleccionado;
			if (item.getDoctor() != null) {
				Doctor doc = item.getDoctor();
				auxDoctor = doc;

				String esp = (doc.getEspecialidades() != null && !doc.getEspecialidades().isEmpty())
						? String.join(", ", doc.getEspecialidades()) : "General";
				lblInfoDoctor.setText("✓ " + doc.getNombre() + " (" + esp + ")");
				lblInfoDoctor.setForeground(new Color(0, 128, 0));
				return;
			}
		}

		auxDoctor = null;
		lblInfoDoctor.setText("🩺 Escriba o seleccione un doctor...");
		lblInfoDoctor.setForeground(new Color(120, 130, 140));
	}

	private void ejecutarGuardadoCita() {
		if (auxPaciente == null) {
			seleccionarPaciente();
		}

		if (auxPaciente == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar o crear un paciente válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (auxDoctor == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar un doctor.", "Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Date fechaSel = (Date) spnFecha.getValue();

		if (citaEditar == null) {
			// REGISTRO
			LocalDateTime fechaRegistro = LocalDateTime.now();
			Time horaConsulta = new Time(fechaSel.getTime());

			Cita nuevaCita = new Cita(
					txtIdCita.getText(),
					fechaRegistro,
					fechaSel,
					horaConsulta,
					EstadoCita.PROGRAMADA,
					auxPaciente,
					auxDoctor
			);

			Clinica.getInstancia().regCita(nuevaCita);
			JOptionPane.showMessageDialog(this, "Cita registrada exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
			limpiarCampos();
		} else {
			// MODIFICACIÓN
			citaEditar.setPaciente(auxPaciente);
			citaEditar.setDoctor(auxDoctor);
			citaEditar.setFechaConsulta(fechaSel);
			citaEditar.setHoraConsulta(new Time(fechaSel.getTime()));

			JOptionPane.showMessageDialog(this, "Cita modificada exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	}

	private void limpiarCampos() {
		actualizarComboPacientes("");
		((JTextField) cbxPaciente.getEditor().getEditorComponent()).setText("");
		lblTipo.setText("");
		auxPaciente = null;
		auxDoctor = null;

		actualizarComboDoctores("");
		lblInfoDoctor.setText("🩺 Escriba o seleccione un doctor...");
		lblInfoDoctor.setForeground(new Color(120, 130, 140));
		txtIdCita.setText("CIT-" + Clinica.genCodigoCitas);
	}
}