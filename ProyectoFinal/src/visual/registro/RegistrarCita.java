package visual.registro;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controllers.CitaController;

import javax.swing.SwingConstants;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import logico.Doctor;
import logico.Paciente;
import utilidad.Formato;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;

public class RegistrarCita extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtIdCita;
	private JSpinner spnFecha;
	private JComboBox<DoctorItem> cbxDoctor;
	private JTextField txtNombrePersona;
	private JTextField txtIdPersona;
	private JLabel lblTipo;
	private JLabel lblInfoDoctor;
	private boolean filtrando = false;

	// Instancia del controlador
	private final CitaController controller;

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

	public static void main(String[] args) {
		try {
			RegistrarCita dialog = new RegistrarCita();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RegistrarCita() {
		this.controller = new CitaController();

		setTitle("Registrar: Cita");
		setBounds(100, 100, 548, 390);
		setLocationRelativeTo(null);
		setModal(true);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(new Color(240, 248, 255));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(25, 10, 482, 290);
		panel.setBorder(new TitledBorder(
				new LineBorder(new Color(70, 130, 180), 1, true), 
				" Registrar Cita ", 
				TitledBorder.LEFT, 
				TitledBorder.TOP, 
				new Font("Dialog", Font.BOLD, 13), 
				new Color(70, 130, 180)));
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		contentPanel.add(panel);

		JLabel lblCodigo = new JLabel("Código:");
		lblCodigo.setForeground(new Color(70, 130, 180));
		lblCodigo.setFont(new Font("Dialog", Font.BOLD, 12));
		lblCodigo.setBounds(25, 30, 60, 22);
		panel.add(lblCodigo);

		txtIdCita = new JTextField(controller.generarNuevoIdCita());
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

		spnFecha = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
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

		JLabel lblIdentificacion = new JLabel("Cédula:");
		lblIdentificacion.setForeground(new Color(70, 130, 180));
		lblIdentificacion.setFont(new Font("Dialog", Font.BOLD, 12));
		lblIdentificacion.setBounds(25, 100, 100, 22);
		panel.add(lblIdentificacion);

		txtIdPersona = new JTextField();
		txtIdPersona.setFont(new Font("Dialog", Font.PLAIN, 12));
		txtIdPersona.setBorder(new LineBorder(new Color(70, 130, 180)));
		txtIdPersona.setBackground(new Color(245, 250, 255));
		txtIdPersona.setBounds(78, 100, 200, 22);
		txtIdPersona.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				 manejarAutocompletadoCedula(e);
			}
		});
		panel.add(txtIdPersona);

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
		lblTipo.setBounds(346, 118, 150, 22);
		panel.add(lblTipo);

		JLabel lblPaciente = new JLabel("Nombre:");
		lblPaciente.setForeground(new Color(70, 130, 180));
		lblPaciente.setFont(new Font("Dialog", Font.BOLD, 12));
		lblPaciente.setBounds(24, 145, 100, 22);
		panel.add(lblPaciente);

		txtNombrePersona = new JTextField();
		txtNombrePersona.setFont(new Font("Dialog", Font.PLAIN, 12));
		txtNombrePersona.setBackground(new Color(245, 250, 255));
		txtNombrePersona.setBorder(new LineBorder(new Color(70, 130, 180)));
		txtNombrePersona.setBounds(77, 145, 380, 22);
		panel.add(txtNombrePersona);

		JLabel lblSeccionDoctor = new JLabel("DATOS DEL DOCTOR", SwingConstants.CENTER);
		lblSeccionDoctor.setOpaque(true);
		lblSeccionDoctor.setBackground(new Color(230, 242, 250));
		lblSeccionDoctor.setForeground(new Color(70, 130, 180));
		lblSeccionDoctor.setFont(new Font("Dialog", Font.BOLD, 11));
		lblSeccionDoctor.setBounds(24, 182, 433, 22);
		panel.add(lblSeccionDoctor);

		JLabel lblDoctor = new JLabel("Doctor:");
		lblDoctor.setForeground(new Color(70, 130, 180));
		lblDoctor.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDoctor.setBounds(24, 215, 100, 22);
		panel.add(lblDoctor);

		cbxDoctor = new JComboBox<>();
		cbxDoctor.setEditable(true);
		cbxDoctor.setRenderer(new DoctorCellRenderer());
		cbxDoctor.setFont(new Font("Dialog", Font.PLAIN, 12));
		cbxDoctor.setBackground(Color.WHITE);
		cbxDoctor.setBorder(new LineBorder(new Color(70, 130, 180)));
		cbxDoctor.setBounds(77, 215, 380, 24);

		JTextField editor = (JTextField) cbxDoctor.getEditor().getEditorComponent();
		editor.setBackground(new Color(245, 250, 255));
		editor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP || 
					e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					return;
				}

				String texto = editor.getText();
				int caretPosition = editor.getCaretPosition();

				actualizarComboDoctores(texto);

				editor.setText(texto);
				try {
					editor.setCaretPosition(Math.min(caretPosition, texto.length()));
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
		lblInfoDoctor.setBounds(77, 243, 380, 20);
		panel.add(lblInfoDoctor);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(Color.WHITE);
		buttonPane.setBorder(new LineBorder(new Color(70, 130, 180)));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("Registrar");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ejecutarRegistroCita();
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

		actualizarComboDoctores("");
	}

	private void abrirVentanaCrearPaciente() {
		RegistrarPaciente regPacienteDialog = new RegistrarPaciente(null);
		regPacienteDialog.setDatosIniciales(
			txtIdPersona.getText().trim(), 
			txtNombrePersona.getText().trim(), 
			""
		);
		regPacienteDialog.setModal(true);
		regPacienteDialog.setVisible(true);

		Paciente pacienteNuevo = regPacienteDialog.getPacienteCreado();
		if (pacienteNuevo != null) {
			controller.setAuxPaciente(pacienteNuevo);
			txtIdPersona.setText(pacienteNuevo.getCedula());
			txtNombrePersona.setText(pacienteNuevo.getNombre());
			lblTipo.setText("*Encontrado.");
			lblTipo.setForeground(new Color(0, 128, 0));
		}
	}

	private void manejarAutocompletadoCedula(KeyEvent e) {
		String textoActual = txtIdPersona.getText();
		String cedulaSugerida = controller.autocompletarCedula(textoActual, e.getKeyCode());

		if (cedulaSugerida != null) {
			txtNombrePersona.setText(controller.getAuxPaciente().getNombre());
			lblTipo.setText("*Encontrado.");
			lblTipo.setForeground(new Color(0, 128, 0));

			int posInicioSeleccion = textoActual.length();
			txtIdPersona.setText(cedulaSugerida);
			txtIdPersona.setSelectionStart(posInicioSeleccion);
			txtIdPersona.setSelectionEnd(cedulaSugerida.length());
		} else {
			Paciente p = controller.getAuxPaciente();
			if (p != null) {
				txtNombrePersona.setText(p.getNombre());
				lblTipo.setText("*Encontrado.");
				lblTipo.setForeground(new Color(0, 128, 0));
			} else if (txtIdPersona.getText().trim().isEmpty()) {
				txtNombrePersona.setText("");
				lblTipo.setText("");
			} else {
				txtNombrePersona.setText("");
				lblTipo.setText("*No encontrado.");
				lblTipo.setForeground(Color.RED);
			}
		}
	}

	private void actualizarComboDoctores(String filtro) {
		filtrando = true;
		cbxDoctor.removeAllItems();
		cbxDoctor.addItem(new DoctorItem(null));

		List<Doctor> doctores = controller.obtenerDoctoresFiltrados(filtro);
		for (Doctor d : doctores) {
			cbxDoctor.addItem(new DoctorItem(d));
		}

		filtrando = false;
	}

	private void seleccionarDoctor() {
		if (filtrando) return;

		Object itemSeleccionado = cbxDoctor.getSelectedItem();
		if (itemSeleccionado instanceof DoctorItem) {
			DoctorItem item = (DoctorItem) itemSeleccionado;
			if (item.getDoctor() != null) {
				Doctor doc = item.getDoctor();
				controller.setAuxDoctor(doc);
				
				String esp = (doc.getEspecialidades() != null && !doc.getEspecialidades().isEmpty())
						? String.join(", ", doc.getEspecialidades()) : "General";
				lblInfoDoctor.setText("✓ " + doc.getNombre() + " (" + esp + ")");
				lblInfoDoctor.setForeground(new Color(0, 128, 0));
				return;
			}
		}

		controller.limpiarDoctor();
		lblInfoDoctor.setText("🩺 Escriba o seleccione un doctor...");
		lblInfoDoctor.setForeground(new Color(120, 130, 140));
	}

	private void ejecutarRegistroCita() {
		boolean exito = controller.registrarCita(
			txtIdCita.getText(),
			txtIdPersona.getText(),
			txtNombrePersona.getText(),
			(Date) spnFecha.getValue()
		);

		if (exito) {
			JOptionPane.showMessageDialog(null, "Cita registrada exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
			limpiarCampos();
		}
	}

	private void limpiarCampos() {
		txtIdPersona.setText("");
		txtNombrePersona.setText("");
		lblTipo.setText("");
		controller.limpiarPaciente();
		controller.limpiarDoctor();
		
		actualizarComboDoctores("");
		lblInfoDoctor.setText("🩺 Escriba o seleccione un doctor...");
		lblInfoDoctor.setForeground(new Color(120, 130, 140));
		txtIdCita.setText(controller.generarNuevoIdCita());
	}
}