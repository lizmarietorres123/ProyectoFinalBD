package visual.registro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.DefaultComboBoxModel;

import controllers.ConsultaController;
import logico.Cita;
import logico.Clinica;
import logico.Consulta;
import logico.Diagnostico;
import logico.EstadoCita;
import logico.Paciente;
import visual.ListarVacuna;

public class RealizarConsulta extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtBuscarCita;
	private JComboBox<String> cbxCita;
	private JTextField txtPaciente;
	private JTextField txtDoctor;
	private JTextField txtFechaCita;
	private JTextArea txtDiagnostico;
	private JButton btnCrearDiagnostico;
	private JButton btnVerDiagnosticos;
	private JTextArea txtTratamiento;
	private JTextArea txtObservaciones;
	private JCheckBox chckEsImportante;

	private ArrayList<Diagnostico> diagnosticosActuales = new ArrayList<>();
	private Paciente pacienteActual = null;
	private Cita citaElegida = null;
	private ConsultaController controller;

	public static void main(String[] args) {
		try {
			RealizarConsulta dialog = new RealizarConsulta();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RealizarConsulta() {
		controller = new ConsultaController();

		setTitle("Realizar Consulta");
		setBounds(100, 100, 685, 580);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panelCita = new JPanel();
		panelCita.setBackground(Color.WHITE);
		panelCita.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Informacion de la Cita", TitledBorder.CENTER, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(70, 130, 180)));
		panelCita.setBounds(20, 11, 614, 150);
		contentPanel.add(panelCita);
		panelCita.setLayout(null);

		JLabel lblBuscarCita = new JLabel("Buscar:");
		lblBuscarCita.setForeground(new Color(70, 130, 180));
		lblBuscarCita.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblBuscarCita.setBounds(10, 30, 50, 14);
		panelCita.add(lblBuscarCita);

		txtBuscarCita = new JTextField();
		txtBuscarCita.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtBuscarCita.setBackground(Color.WHITE);
		txtBuscarCita.setBounds(65, 27, 140, 20);
		txtBuscarCita.setToolTipText("Filtrar por código, nombre o cédula");
		txtBuscarCita.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				cargarCitasFiltro(txtBuscarCita.getText().trim());
				if (cbxCita.getItemCount() > 1 && !txtBuscarCita.getText().trim().isEmpty()) {
					cbxCita.showPopup();
				}
			}
		});
		panelCita.add(txtBuscarCita);
		txtBuscarCita.setColumns(10);

		JLabel lblCita = new JLabel("Cita:");
		lblCita.setForeground(new Color(70, 130, 180));
		lblCita.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblCita.setBounds(215, 30, 35, 14);
		panelCita.add(lblCita);

		cbxCita = new JComboBox<>();
		cbxCita.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		cbxCita.setBackground(new Color(224, 247, 250));
		cbxCita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarDatosCita();
			}
		});
		cbxCita.setModel(new DefaultComboBoxModel<>(new String[] {"<<Seleccione>>"}));
		cbxCita.setBounds(255, 27, 339, 20);
		panelCita.add(cbxCita);

		JLabel lblPaciente = new JLabel("Paciente:");
		lblPaciente.setForeground(new Color(70, 130, 180));
		lblPaciente.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblPaciente.setBounds(10, 65, 80, 14);
		panelCita.add(lblPaciente);

		txtPaciente = new JTextField();
		txtPaciente.setEditable(false);
		txtPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtPaciente.setBackground(new Color(224, 247, 250));
		txtPaciente.setBounds(100, 62, 494, 20);
		panelCita.add(txtPaciente);
		txtPaciente.setColumns(10);

		JLabel lblDoctor = new JLabel("Doctor:");
		lblDoctor.setForeground(new Color(70, 130, 180));
		lblDoctor.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblDoctor.setBounds(10, 95, 80, 14);
		panelCita.add(lblDoctor);

		txtDoctor = new JTextField();
		txtDoctor.setEditable(false);
		txtDoctor.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtDoctor.setBackground(new Color(224, 247, 250));
		txtDoctor.setBounds(100, 92, 494, 20);
		panelCita.add(txtDoctor);
		txtDoctor.setColumns(10);

		JLabel lblFechaCita = new JLabel("Fecha/Hora:");
		lblFechaCita.setForeground(new Color(70, 130, 180));
		lblFechaCita.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblFechaCita.setBounds(10, 120, 80, 14);
		panelCita.add(lblFechaCita);

		txtFechaCita = new JTextField();
		txtFechaCita.setEditable(false);
		txtFechaCita.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtFechaCita.setBackground(new Color(224, 247, 250));
		txtFechaCita.setBounds(100, 117, 494, 20);
		panelCita.add(txtFechaCita);
		txtFechaCita.setColumns(10);

		JPanel panelConsulta = new JPanel();
		panelConsulta.setBackground(Color.WHITE);
		panelConsulta.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Datos de la Consulta", TitledBorder.CENTER, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(70, 130, 180)));
		panelConsulta.setBounds(20, 170, 614, 335);
		contentPanel.add(panelConsulta);
		panelConsulta.setLayout(null);

		JLabel lblDiagnostico = new JLabel("Diagnósticos:");
		lblDiagnostico.setForeground(new Color(70, 130, 180));
		lblDiagnostico.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblDiagnostico.setBounds(10, 30, 85, 14);
		panelConsulta.add(lblDiagnostico);

		JScrollPane scrollDiagVis = new JScrollPane();
		scrollDiagVis.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollDiagVis.setBounds(100, 25, 205, 50);
		panelConsulta.add(scrollDiagVis);

		txtDiagnostico = new JTextArea();
		txtDiagnostico.setEditable(false);
		txtDiagnostico.setLineWrap(true);
		txtDiagnostico.setWrapStyleWord(true);
		txtDiagnostico.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
		txtDiagnostico.setBackground(new Color(224, 247, 250));
		scrollDiagVis.setViewportView(txtDiagnostico);

		btnCrearDiagnostico = new JButton("Agregar Diagnóstico");
		btnCrearDiagnostico.setFont(new Font("Bahnschrift", Font.BOLD, 11));
		btnCrearDiagnostico.setBackground(new Color(176, 224, 230));
		btnCrearDiagnostico.setForeground(new Color(70, 130, 180));
		btnCrearDiagnostico.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCrearDiagnostico.setFocusPainted(false);
		btnCrearDiagnostico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCrearDiagnostico();
			}
		});
		btnCrearDiagnostico.setBounds(315, 25, 135, 25);
		panelConsulta.add(btnCrearDiagnostico);

		btnVerDiagnosticos = new JButton("Ver Diagnósticos");
		btnVerDiagnosticos.setFont(new Font("Bahnschrift", Font.BOLD, 11));
		btnVerDiagnosticos.setBackground(new Color(176, 224, 230));
		btnVerDiagnosticos.setForeground(new Color(70, 130, 180));
		btnVerDiagnosticos.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnVerDiagnosticos.setFocusPainted(false);
		btnVerDiagnosticos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarDiagnosticos();
			}
		});
		btnVerDiagnosticos.setBounds(455, 25, 139, 25);
		panelConsulta.add(btnVerDiagnosticos);

		JLabel lblTratamiento = new JLabel("Tratamiento:");
		lblTratamiento.setForeground(new Color(70, 130, 180));
		lblTratamiento.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblTratamiento.setBounds(10, 88, 80, 14);
		panelConsulta.add(lblTratamiento);

		JScrollPane scrollTratamiento = new JScrollPane();
		scrollTratamiento.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollTratamiento.setBounds(100, 85, 494, 65);
		panelConsulta.add(scrollTratamiento);

		txtTratamiento = new JTextArea();
		txtTratamiento.setLineWrap(true);
		txtTratamiento.setWrapStyleWord(true);
		txtTratamiento.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtTratamiento.setBackground(new Color(224, 247, 250));
		scrollTratamiento.setViewportView(txtTratamiento);

		JLabel lblObservaciones = new JLabel("Observaciones:");
		lblObservaciones.setForeground(new Color(70, 130, 180));
		lblObservaciones.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblObservaciones.setBounds(10, 163, 90, 14);
		panelConsulta.add(lblObservaciones);

		JScrollPane scrollObservaciones = new JScrollPane();
		scrollObservaciones.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollObservaciones.setBounds(100, 160, 494, 65);
		panelConsulta.add(scrollObservaciones);

		txtObservaciones = new JTextArea();
		txtObservaciones.setLineWrap(true);
		txtObservaciones.setWrapStyleWord(true);
		txtObservaciones.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtObservaciones.setBackground(new Color(224, 247, 250));
		scrollObservaciones.setViewportView(txtObservaciones);

		chckEsImportante = new JCheckBox("Marcar como Importante (Agregar al Resumen del Paciente)");
		chckEsImportante.setBackground(Color.WHITE);
		chckEsImportante.setForeground(new Color(70, 130, 180));
		chckEsImportante.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		chckEsImportante.setBounds(100, 295, 494, 23);
		panelConsulta.add(chckEsImportante);

		JButton btnAplicarVacunas = new JButton("Aplicar Vacunas");
		btnAplicarVacunas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pacienteActual != null) {
					ListarVacuna listarVac = new ListarVacuna(pacienteActual, "Agregar");
					listarVac.setModal(true);
					listarVac.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Debe seleccionar una cita con un paciente asignado primero.", "Atención", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnAplicarVacunas.setForeground(new Color(70, 130, 180));
		btnAplicarVacunas.setFont(new Font("Bahnschrift", Font.BOLD, 11));
		btnAplicarVacunas.setFocusPainted(false);
		btnAplicarVacunas.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnAplicarVacunas.setBackground(new Color(176, 224, 230));
		btnAplicarVacunas.setBounds(100, 235, 494, 23);
		panelConsulta.add(btnAplicarVacunas);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 248, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnRealizar = new JButton("Realizar Consulta");
		btnRealizar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnRealizar.setBackground(new Color(176, 224, 230));
		btnRealizar.setForeground(new Color(70, 130, 180));
		btnRealizar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnRealizar.setFocusPainted(false);
		btnRealizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realizarConsulta();
			}
		});
		btnRealizar.setActionCommand("OK");
		buttonPane.add(btnRealizar);
		getRootPane().setDefaultButton(btnRealizar);

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

		cargarCitasFiltro("");
	}

	private void cargarCitasFiltro(String filtro) {
		ActionListener[] listeners = cbxCita.getActionListeners();
		for (ActionListener listener : listeners) {
			cbxCita.removeActionListener(listener);
		}

		cbxCita.removeAllItems();
		cbxCita.addItem("<<Seleccione>>");

		String f = filtro.toLowerCase();

		for (Cita cita : Clinica.getInstancia().getCitas()) {
			if (cita.getEstado() == EstadoCita.PROGRAMADA) {
				String idCita = cita.getIdCita() != null ? cita.getIdCita().toLowerCase() : "";
				String nombrePers = cita.getNombrePersona() != null ? cita.getNombrePersona().toLowerCase() : "";
				String idPers = cita.getIdPersona() != null ? cita.getIdPersona().toLowerCase() : "";

				if (f.isEmpty() || idCita.contains(f) || nombrePers.contains(f) || idPers.contains(f)) {
					String item = cita.getIdCita() + " - " + cita.getNombrePersona() + " (" + cita.getIdPersona() + ")" + " - (" + utilidad.Formato.getDateString(cita.getFechaHora()) + ")";
					cbxCita.addItem(item);
				}
			}
		}

		for (ActionListener listener : listeners) {
			cbxCita.addActionListener(listener);
		}

		cargarDatosCita();
	}

	private void cargarDatosCita() {
		if (cbxCita.getSelectedIndex() > 0) {
			String codigo = cbxCita.getSelectedItem().toString().split(" ")[0];
			citaElegida = controller.obtenerCitaPorCodigo(codigo);

			if (citaElegida != null) {
				pacienteActual = citaElegida.getPaciente();
				if (pacienteActual == null) {
					pacienteActual = Clinica.getInstancia().buscarPacienteXIdentificacion(citaElegida.getIdPersona());
				}

				if (pacienteActual != null) {
					txtPaciente.setText(pacienteActual.getNombre() + " " + (pacienteActual.getApellido() != null ? pacienteActual.getApellido() : "") + " - " + pacienteActual.getCedula());
				} else {
					txtPaciente.setText(citaElegida.getNombrePersona() + " - " + citaElegida.getIdPersona());
				}

				txtDoctor.setText(citaElegida.getDoctor() != null ? citaElegida.getDoctor().getNombre() : "");
				txtFechaCita.setText(utilidad.Formato.getDateString(citaElegida.getFechaHora()));
			}
		} else {
			limpiarCampos();
			pacienteActual = null;
		}
	}

	private void abrirCrearDiagnostico() {
		CrearDiagnostico dialogo = new CrearDiagnostico(diagnosticosActuales, pacienteActual);
		dialogo.setModal(true);
		dialogo.setLocationRelativeTo(this);
		dialogo.setVisible(true);
		Diagnostico diag = dialogo.getDiagnosticoCreado();
		if (diag != null) {
			diagnosticosActuales.add(diag);
			actualizarTextoDiagnosticos();
			verificarVigilanciaDiagnosticos();
		}
	}

	private void actualizarTextoDiagnosticos() {
		if (diagnosticosActuales.isEmpty()) {
			txtDiagnostico.setText("");
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < diagnosticosActuales.size(); i++) {
				Diagnostico d = diagnosticosActuales.get(i);
				String enf = (d.getEnfermedadDiagnosticada() != null) ? d.getEnfermedadDiagnosticada().getNombre() : "Sin Enfermedad";
				sb.append("• ").append(d.getCodigoDiagnostico()).append(" - ").append(enf).append("\n");
			}
			txtDiagnostico.setText(sb.toString().trim());
			txtDiagnostico.setCaretPosition(0);
		}
	}

	private void mostrarDiagnosticos() {
		if (diagnosticosActuales.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No hay diagnósticos creados en esta consulta.", "Diagnósticos de la Consulta", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		if (diagnosticosActuales.size() == 1) {
			CrearDiagnostico dialogo = new CrearDiagnostico(diagnosticosActuales.get(0), diagnosticosActuales);
			dialogo.setModal(true);
			dialogo.setLocationRelativeTo(this);
			dialogo.setVisible(true);
		} else {
			String[] opciones = new String[diagnosticosActuales.size()];
			for (int i = 0; i < diagnosticosActuales.size(); i++) {
				Diagnostico d = diagnosticosActuales.get(i);
				String enf = (d.getEnfermedadDiagnosticada() != null) ? d.getEnfermedadDiagnosticada().getNombre() : "Sin Enfermedad";
				opciones[i] = d.getCodigoDiagnostico() + " - " + enf;
			}

			String seleccion = (String) JOptionPane.showInputDialog(
					this,
					"Seleccione el diagnóstico que desea ver o editar:",
					"Ver Diagnósticos",
					JOptionPane.QUESTION_MESSAGE,
					null,
					opciones,
					opciones[0]
			);

			if (seleccion != null) {
				for (Diagnostico d : diagnosticosActuales) {
					if (seleccion.startsWith(d.getCodigoDiagnostico())) {
						CrearDiagnostico dialogo = new CrearDiagnostico(d, diagnosticosActuales);
						dialogo.setModal(true);
						dialogo.setLocationRelativeTo(this);
						dialogo.setVisible(true);
						break;
					}
				}
			}
		}

		actualizarTextoDiagnosticos();
		verificarVigilanciaDiagnosticos();
	}

	private void verificarVigilanciaDiagnosticos() {
		boolean bajoVigilancia = false;
		for (Diagnostico d : diagnosticosActuales) {
			if (d.getEnfermedadDiagnosticada() != null && d.getEnfermedadDiagnosticada().isVigilancia()) {
				bajoVigilancia = true;
				break;
			}
		}

		if (bajoVigilancia) {
			chckEsImportante.setSelected(true);
		}
		chckEsImportante.setEnabled(true);
	}

	private void realizarConsulta() {
		if (cbxCita.getSelectedIndex() <= 0) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar una cita.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (pacienteActual == null) {
			JOptionPane.showMessageDialog(null, "El paciente debe estar registrado en el sistema antes de realizar la consulta.", "Atención", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (diagnosticosActuales.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe agregar al menos un diagnóstico.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (txtTratamiento.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe ingresar un tratamiento.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		citaElegida.setPaciente(pacienteActual);

		Consulta consulta = controller.registrarConsulta(
				citaElegida,
				diagnosticosActuales,
				txtTratamiento.getText(),
				txtObservaciones.getText(),
				chckEsImportante.isSelected()
		);

		if (consulta != null) {
			JOptionPane.showMessageDialog(null, "Consulta realizada con éxito.\nCódigo: " + consulta.getId(), "Consulta Exitosa", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			JOptionPane.showMessageDialog(null, "Ocurrió un error al procesar la consulta.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void limpiarCampos() {
		if (txtPaciente != null) txtPaciente.setText("");
		if (txtDoctor != null) txtDoctor.setText("");
		if (txtFechaCita != null) txtFechaCita.setText("");
		if (txtDiagnostico != null) txtDiagnostico.setText("");
		if (txtTratamiento != null) txtTratamiento.setText("");
		if (txtObservaciones != null) txtObservaciones.setText("");
		if (chckEsImportante != null) chckEsImportante.setSelected(false);

		diagnosticosActuales.clear();
		pacienteActual = null;
	}
}