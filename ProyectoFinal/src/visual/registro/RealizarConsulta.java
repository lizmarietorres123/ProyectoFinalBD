package visual.registro;

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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.DefaultComboBoxModel;
import logico.Cita;
import logico.Clinica;
import logico.Consulta;
import logico.Diagnostico;
import logico.Enfermedad;
import logico.EstadoCita;
import logico.Paciente;
import visual.CrearDiagnostico;
import visual.ListarVacuna;

public class RealizarConsulta extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JComboBox cbxCita;
	private JTextField txtPaciente;
	private JTextField txtDoctor;
	private JTextField txtFechaCita;
	private JTextArea txtSintomas;
	private JTextField txtDiagnostico;
	private JButton btnCrearDiagnostico;
	private JButton btnGestionarPaciente;
	private JTextArea txtTratamiento;
	private JTextArea txtObservaciones;
	private JCheckBox chckEsImportante;
	private Diagnostico diagnosticoActual;
	private Paciente pacienteActual = null;
	private Cita citaElegida = null;

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
		setTitle("Realizar Consulta");
		setBounds(100, 100, 685, 655);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panelCita = new JPanel();
		panelCita.setBackground(Color.WHITE);
		panelCita.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Informaci�n de la Cita", TitledBorder.CENTER, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(70, 130, 180)));
		panelCita.setBounds(20, 11, 614, 150);
		contentPanel.add(panelCita);
		panelCita.setLayout(null);

		JLabel lblCita = new JLabel("Cita:");
		lblCita.setForeground(new Color(70, 130, 180));
		lblCita.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblCita.setBounds(10, 30, 80, 14);
		panelCita.add(lblCita);

		cbxCita = new JComboBox();
		cbxCita.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		cbxCita.setBackground(new Color(224, 247, 250));
		cbxCita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarDatosCita();
			}
		});
		cbxCita.setModel(new DefaultComboBoxModel(new String[] {"<<Seleccione>>"}));
		cbxCita.setBounds(100, 27, 494, 20);
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
		txtPaciente.setBounds(100, 62, 344, 20);
		panelCita.add(txtPaciente);
		txtPaciente.setColumns(10);

		btnGestionarPaciente = new JButton("Crear Paciente");
		btnGestionarPaciente.setFont(new Font("Bahnschrift", Font.BOLD, 11));
		btnGestionarPaciente.setBackground(new Color(176, 224, 230));
		btnGestionarPaciente.setForeground(new Color(70, 130, 180));
		btnGestionarPaciente.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnGestionarPaciente.setFocusPainted(false);
		btnGestionarPaciente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestionarPaciente();
			}
		});
		btnGestionarPaciente.setBounds(454, 62, 140, 20);
		btnGestionarPaciente.setEnabled(false);
		panelCita.add(btnGestionarPaciente);

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
		panelConsulta.setBounds(20, 174, 614, 395);
		contentPanel.add(panelConsulta);
		panelConsulta.setLayout(null);

		JLabel lblSintomas = new JLabel("S�ntomas:");
		lblSintomas.setForeground(new Color(70, 130, 180));
		lblSintomas.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblSintomas.setBounds(10, 25, 80, 14);
		panelConsulta.add(lblSintomas);

		JScrollPane scrollSintomas = new JScrollPane();
		scrollSintomas.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollSintomas.setBounds(100, 25, 494, 70);
		panelConsulta.add(scrollSintomas);

		txtSintomas = new JTextArea();
		txtSintomas.setLineWrap(true);
		txtSintomas.setWrapStyleWord(true);
		txtSintomas.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtSintomas.setBackground(new Color(224, 247, 250));
		scrollSintomas.setViewportView(txtSintomas);

		JLabel lblDiagnostico = new JLabel("Diagn�stico:");
		lblDiagnostico.setForeground(new Color(70, 130, 180));
		lblDiagnostico.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblDiagnostico.setBounds(10, 110, 80, 14);
		panelConsulta.add(lblDiagnostico);

		txtDiagnostico = new JTextField();
		txtDiagnostico.setEditable(false);
		txtDiagnostico.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtDiagnostico.setBackground(new Color(224, 247, 250));
		txtDiagnostico.setBounds(100, 107, 344, 20);
		panelConsulta.add(txtDiagnostico);
		txtDiagnostico.setColumns(10);

		btnCrearDiagnostico = new JButton("Crear Diagn�stico");
		btnCrearDiagnostico.setFont(new Font("Bahnschrift", Font.BOLD, 11));
		btnCrearDiagnostico.setBackground(new Color(176, 224, 230));
		btnCrearDiagnostico.setForeground(new Color(70, 130, 180));
		btnCrearDiagnostico.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCrearDiagnostico.setFocusPainted(false);
		btnCrearDiagnostico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCrearDiagnostico();
				if(diagnosticoActual != null && diagnosticoActual.getEnfermedadDiagnosticada() != null && diagnosticoActual.getEnfermedadDiagnosticada().isVigilancia()){
					chckEsImportante.setEnabled(false);
					chckEsImportante.setSelected(true);
				}else {
					chckEsImportante.setEnabled(true);
				}
			}
		});
		btnCrearDiagnostico.setBounds(454, 106, 140, 23);
		panelConsulta.add(btnCrearDiagnostico);

		JLabel lblTratamiento = new JLabel("Tratamiento:");
		lblTratamiento.setForeground(new Color(70, 130, 180));
		lblTratamiento.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblTratamiento.setBounds(10, 145, 80, 14);
		panelConsulta.add(lblTratamiento);

		JScrollPane scrollTratamiento = new JScrollPane();
		scrollTratamiento.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollTratamiento.setBounds(100, 145, 494, 70);
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
		lblObservaciones.setBounds(10, 230, 90, 14);
		panelConsulta.add(lblObservaciones);

		JScrollPane scrollObservaciones = new JScrollPane();
		scrollObservaciones.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollObservaciones.setBounds(100, 230, 494, 70);
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
		chckEsImportante.setBounds(100, 360, 494, 23);
		panelConsulta.add(chckEsImportante);
		
		JButton btnAplicarVacunas = new JButton("Aplicar Vacunas");
		btnAplicarVacunas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListarVacuna listarVac = new ListarVacuna(pacienteActual, "Agregar");
                listarVac.setModal(true);
                listarVac.setVisible(true);
			}
		});
		btnAplicarVacunas.setForeground(new Color(70, 130, 180));
		btnAplicarVacunas.setFont(new Font("Bahnschrift", Font.BOLD, 11));
		btnAplicarVacunas.setFocusPainted(false);
		btnAplicarVacunas.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnAplicarVacunas.setBackground(new Color(176, 224, 230));
		btnAplicarVacunas.setBounds(100, 316, 494, 23);
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

		cargarCitas();
	}

	private void cargarCitas() {
		ActionListener[] listeners = cbxCita.getActionListeners();
		for(ActionListener listener : listeners) {
			cbxCita.removeActionListener(listener);
		}

		cbxCita.removeAllItems();
		cbxCita.addItem("<<Seleccione>>");
		for(Cita cita : Clinica.getInstancia().getCitas()) {
			if(cita.getEstado() == EstadoCita.PROGRAMADA) {
				String item = cita.getIdCita()+ " - " + cita.getNombrePersona() + " (" + cita.getIdPersona() + ")" + " - (" + utilidad.Formato.getDateString(cita.getFechaHora()) + ")";
				cbxCita.addItem(item);
			}
		}

		for(ActionListener listener : listeners) {
			cbxCita.addActionListener(listener);
		}
	}

	private void cargarDatosCita() {
		if(cbxCita.getSelectedIndex() > 0) {
			String codigo = cbxCita.getSelectedItem().toString().split(" ")[0];
			citaElegida = Clinica.getInstancia().buscarCitaXId(codigo);

			if(citaElegida != null) {
				pacienteActual = Clinica.getInstancia().buscarPacienteXIdentificacion(citaElegida.getIdPersona());
				
				if(pacienteActual != null) {
					btnGestionarPaciente.setText("Modificar Paciente");
					btnGestionarPaciente.setEnabled(true);
					txtPaciente.setText(pacienteActual.getNombre() + " - " + pacienteActual.getCedula());
				} else {
					btnGestionarPaciente.setText("Crear Paciente");
					btnGestionarPaciente.setEnabled(true);
					txtPaciente.setText(citaElegida.getNombrePersona() + " - " + citaElegida.getIdPersona());
				}
				
				txtDoctor.setText(citaElegida.getDoctor().getNombre());
				txtFechaCita.setText(utilidad.Formato.getDateString(citaElegida.getFechaHora()));
			}
		} else {
			limpiarCampos();
			btnGestionarPaciente.setEnabled(false);
			pacienteActual = null;
		}
	}

	private void gestionarPaciente() {
		if(pacienteActual != null) {
			modificarPaciente();
		} else {
			registroPaciente();
		}
	}

	private void modificarPaciente() {
		RegistrarPaciente dialogo = new RegistrarPaciente(pacienteActual);
		dialogo.setModal(true);
		dialogo.setLocationRelativeTo(this);
		dialogo.setVisible(true);

		txtPaciente.setText(pacienteActual.getNombre() + " - " + pacienteActual.getCedula());
	}

	private void registroPaciente() {
		RegistrarPaciente dialogo = new RegistrarPaciente(null);
		
		if(citaElegida != null) {
			dialogo.setDatosIniciales(
				citaElegida.getIdPersona(),
				citaElegida.getNombrePersona(),
				citaElegida.getTelefonoPersona()
			);
		}
		
		dialogo.setModal(true);
		dialogo.setLocationRelativeTo(this);
		dialogo.setVisible(true);

		Paciente nuevoPaciente = dialogo.getPacienteCreado();

		if(nuevoPaciente != null) {
			pacienteActual = nuevoPaciente;
			btnGestionarPaciente.setText("Modificar Paciente");
			txtPaciente.setText(pacienteActual.getNombre() + " - " + pacienteActual.getCedula());

			JOptionPane.showMessageDialog(this,
					"Paciente registrado exitosamente.\nAhora puede continuar con la consulta.",
					"Paciente Creado",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void abrirCrearDiagnostico() {
		CrearDiagnostico dialogo = new CrearDiagnostico();
		dialogo.setModal(true);
		dialogo.setLocationRelativeTo(this);
		dialogo.setVisible(true);
		Diagnostico diag = dialogo.getDiagnosticoCreado();
		if(diag != null) {
			diagnosticoActual = diag;
			txtDiagnostico.setText(diag.getCodigoDiagnostico() + " - " + diag.getDescripcion().substring(0, Math.min(30, diag.getDescripcion().length())) + "...");
		}
	}

	private void realizarConsulta() {
		if(cbxCita.getSelectedIndex() <= 0) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar una cita.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if(pacienteActual == null) {
			JOptionPane.showMessageDialog(null, "Debe crear el paciente antes de realizar la consulta.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if(txtSintomas.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe ingresar los s�ntomas.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if(diagnosticoActual == null) {
			JOptionPane.showMessageDialog(null, "Debe crear un diagn�stico.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if(txtTratamiento.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe ingresar un tratamiento.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}
			
		Consulta consulta = new Consulta(
				"CONS-" + Clinica.genCodigoConsultas,
				pacienteActual, 
				citaElegida.getDoctor(), 
				citaElegida.getFechaHora());
		
		setCamposConsulta(consulta);
		Clinica.getInstancia().realizarConsulta(consulta, citaElegida);
		
		JOptionPane.showMessageDialog(null, "Consulta realizada con �xito.\nC�digo: " + consulta.getId(), "Consulta Exitosa", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}

	private void setCamposConsulta(Consulta consulta) {
		consulta.setSintomas(txtSintomas.getText());
		consulta.setDiagnostico(diagnosticoActual);
		consulta.setTratamiento(txtTratamiento.getText());
		consulta.setObservaciones(txtObservaciones.getText());
		consulta.setEsImportante(chckEsImportante.isSelected());
		Enfermedad enfermedadDiag = diagnosticoActual.getEnfermedadDiagnosticada();
		if(enfermedadDiag != null) {
			pacienteActual.agregarEnfermedad(enfermedadDiag);
		}

		if(chckEsImportante.isSelected()) {
			pacienteActual.getResumen().add(consulta);
		}
	}

	private void limpiarCampos() {
		if(txtPaciente != null) txtPaciente.setText("");
		if(txtDoctor != null) txtDoctor.setText("");
		if(txtFechaCita != null) txtFechaCita.setText("");
		if(txtSintomas != null) txtSintomas.setText("");
		if(txtDiagnostico != null) txtDiagnostico.setText("");
		if(txtTratamiento != null) txtTratamiento.setText("");
		if(txtObservaciones != null) txtObservaciones.setText("");
		if(chckEsImportante != null) chckEsImportante.setSelected(false);

		diagnosticoActual = null;
		pacienteActual = null;

		if(btnGestionarPaciente != null) {
			btnGestionarPaciente.setText("Crear Paciente");
			btnGestionarPaciente.setEnabled(false);
		}
	}
}