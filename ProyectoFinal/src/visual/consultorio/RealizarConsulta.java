package visual.consultorio;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controllers.ConsultaController;
import logico.Clinica;
import logico.catalogo.Doctor;
import logico.catalogo.*;
import logico.consultorio.*;
import logico.enfermeria.DetalleAnalisis;
import logico.enfermeria.DetalleVacuna;
import visual.enfermeria.ListarDetalleAnalisis;
import visual.enfermeria.ListarDetalleVacuna;

public class RealizarConsulta extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtBuscarCita;
	private JSpinner spinFechaFiltro;
	private JComboBox<String> cbxCita;
	private JTextField txtPaciente;
	private JTextField txtDoctor;

	private JTextArea txtDiagnostico;
	private JButton btnCrearDiagnostico;
	private JButton btnVerDiagnosticos;

	private JTextArea txtVacunas;
	private JButton btnAplicarVacunas;
	private JButton btnListaVacunas;

	private JTextArea txtAnalisis;
	private JButton btnIndicarAnalisis;
	private JButton btnListaAnalisis;

	private JTextArea txtObservaciones;

	private ArrayList<Diagnostico> diagnosticosActuales = new ArrayList<>();
	private ArrayList<Vacuna> vacunasIndicadas = new ArrayList<>();
	private ArrayList<Analisis> analisisIndicados = new ArrayList<>();

	private Paciente pacienteActual = null;
	private Cita citaElegida = null;
	private ConsultaController controller = null;

	// Objeto consulta para modo edición/detalle
	private Consulta consultaEdicion = null;

	public static void main(String[] args) {
		try {
			RealizarConsulta dialog = new RealizarConsulta();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Constructor por defecto para registro
	public RealizarConsulta() {
		this(null);
	}

	// Constructor con parámetro para Ver Detalle / Modificar / Eliminar
	public RealizarConsulta(Consulta consulta) {
		this.consultaEdicion = consulta;
		controller = new ConsultaController();

		setTitle(consultaEdicion == null ? "Realizar Consulta" : "Detalle / Modificación de Consulta");
		setBounds(100, 100, 685, 490);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		// --- PANEL DE INFORMACIÓN DE LA CITA ---
		JPanel panelCita = new JPanel();
		panelCita.setBackground(Color.WHITE);
		panelCita.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Información de la Cita", TitledBorder.CENTER, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(70, 130, 180)));
		panelCita.setBounds(20, 11, 614, 130);
		contentPanel.add(panelCita);
		panelCita.setLayout(null);

		JLabel lblBuscarCita = new JLabel("Buscar:");
		lblBuscarCita.setForeground(new Color(70, 130, 180));
		lblBuscarCita.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblBuscarCita.setBounds(10, 28, 45, 14);
		panelCita.add(lblBuscarCita);

		txtBuscarCita = new JTextField();
		txtBuscarCita.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtBuscarCita.setBackground(Color.WHITE);
		txtBuscarCita.setBounds(55, 25, 105, 22);
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

		JLabel lblFechaFiltro = new JLabel("Fecha:");
		lblFechaFiltro.setForeground(new Color(70, 130, 180));
		lblFechaFiltro.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblFechaFiltro.setBounds(170, 28, 42, 14);
		panelCita.add(lblFechaFiltro);

		Date fechaInicial = obtenerFechaCitaExistente();
		SpinnerDateModel dateModel = new SpinnerDateModel(fechaInicial, null, null, java.util.Calendar.DAY_OF_MONTH);
		spinFechaFiltro = new JSpinner(dateModel);
		JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinFechaFiltro, "dd/MM/yyyy");
		spinFechaFiltro.setEditor(dateEditor);
		spinFechaFiltro.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		spinFechaFiltro.setBounds(212, 25, 110, 22);
		spinFechaFiltro.addChangeListener(e -> cargarCitasFiltro(txtBuscarCita.getText().trim()));
		panelCita.add(spinFechaFiltro);

		JLabel lblCita = new JLabel("Cita:");
		lblCita.setForeground(new Color(70, 130, 180));
		lblCita.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblCita.setBounds(332, 28, 32, 14);
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
		cbxCita.setBounds(365, 25, 230, 22);
		panelCita.add(cbxCita);

		JLabel lblPaciente = new JLabel("Paciente:");
		lblPaciente.setForeground(new Color(70, 130, 180));
		lblPaciente.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblPaciente.setBounds(10, 60, 80, 14);
		panelCita.add(lblPaciente);

		txtPaciente = new JTextField();
		txtPaciente.setEditable(false);
		txtPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtPaciente.setBackground(new Color(224, 247, 250));
		txtPaciente.setBounds(100, 57, 494, 22);
		panelCita.add(txtPaciente);
		txtPaciente.setColumns(10);

		JLabel lblDoctor = new JLabel("Doctor:");
		lblDoctor.setForeground(new Color(70, 130, 180));
		lblDoctor.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblDoctor.setBounds(10, 92, 80, 14);
		panelCita.add(lblDoctor);

		txtDoctor = new JTextField();
		txtDoctor.setEditable(false);
		txtDoctor.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtDoctor.setBackground(new Color(224, 247, 250));
		txtDoctor.setBounds(100, 89, 494, 22);
		panelCita.add(txtDoctor);
		txtDoctor.setColumns(10);

		// --- PANEL DE DATOS DE LA CONSULTA ---
		JPanel panelConsulta = new JPanel();
		panelConsulta.setBackground(Color.WHITE);
		panelConsulta.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Datos de la Consulta", TitledBorder.CENTER, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(70, 130, 180)));
		panelConsulta.setBounds(20, 150, 614, 255);
		contentPanel.add(panelConsulta);
		panelConsulta.setLayout(null);

		// --- DIAGNÓSTICOS ---
		JLabel lblDiagnostico = new JLabel("Diagnósticos:");
		lblDiagnostico.setForeground(new Color(70, 130, 180));
		lblDiagnostico.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblDiagnostico.setBounds(10, 25, 85, 14);
		panelConsulta.add(lblDiagnostico);

		JScrollPane scrollDiagVis = new JScrollPane();
		scrollDiagVis.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollDiagVis.setBounds(100, 20, 205, 45);
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
		btnCrearDiagnostico.addActionListener(e -> abrirCrearDiagnostico());
		btnCrearDiagnostico.setBounds(315, 20, 135, 25);
		panelConsulta.add(btnCrearDiagnostico);

		btnVerDiagnosticos = new JButton("Ver Diagnósticos");
		btnVerDiagnosticos.setFont(new Font("Bahnschrift", Font.BOLD, 11));
		btnVerDiagnosticos.setBackground(new Color(176, 224, 230));
		btnVerDiagnosticos.setForeground(new Color(70, 130, 180));
		btnVerDiagnosticos.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnVerDiagnosticos.setFocusPainted(false);
		btnVerDiagnosticos.addActionListener(e -> mostrarDiagnosticos());
		btnVerDiagnosticos.setBounds(455, 20, 139, 25);
		panelConsulta.add(btnVerDiagnosticos);

		// --- VACUNAS ---
		JLabel lblVacunas = new JLabel("Vacunas:");
		lblVacunas.setForeground(new Color(70, 130, 180));
		lblVacunas.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblVacunas.setBounds(10, 75, 85, 14);
		panelConsulta.add(lblVacunas);

		JScrollPane scrollVacunasVis = new JScrollPane();
		scrollVacunasVis.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollVacunasVis.setBounds(100, 70, 205, 45);
		panelConsulta.add(scrollVacunasVis);

		txtVacunas = new JTextArea();
		txtVacunas.setEditable(false);
		txtVacunas.setLineWrap(true);
		txtVacunas.setWrapStyleWord(true);
		txtVacunas.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
		txtVacunas.setBackground(new Color(224, 247, 250));
		scrollVacunasVis.setViewportView(txtVacunas);

		btnAplicarVacunas = new JButton("Aplicar Vacunas");
		btnAplicarVacunas.setFont(new Font("Bahnschrift", Font.BOLD, 11));
		btnAplicarVacunas.setBackground(new Color(176, 224, 230));
		btnAplicarVacunas.setForeground(new Color(70, 130, 180));
		btnAplicarVacunas.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnAplicarVacunas.setFocusPainted(false);
		btnAplicarVacunas.addActionListener(e -> abrirAplicarVacunas());

		if (consultaEdicion == null) {
			// Diseño original para modo registro
			btnAplicarVacunas.setBounds(315, 70, 279, 25);
			panelConsulta.add(btnAplicarVacunas);
		} else {
			// Diseño con botón de lista para modo listar/modificar
			btnAplicarVacunas.setBounds(315, 70, 135, 25);
			panelConsulta.add(btnAplicarVacunas);

			btnListaVacunas = new JButton("Lista Vacuna");
			btnListaVacunas.setFont(new Font("Bahnschrift", Font.BOLD, 11));
			btnListaVacunas.setBackground(new Color(176, 224, 230));
			btnListaVacunas.setForeground(new Color(70, 130, 180));
			btnListaVacunas.setBorder(new LineBorder(new Color(135, 206, 235), 2));
			btnListaVacunas.setFocusPainted(false);
			btnListaVacunas.addActionListener(e -> abrirListaVacunas());
			btnListaVacunas.setBounds(455, 70, 139, 25);
			panelConsulta.add(btnListaVacunas);
		}

		// --- ANÁLISIS ---
		JLabel lblAnalisis = new JLabel("Análisis:");
		lblAnalisis.setForeground(new Color(70, 130, 180));
		lblAnalisis.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblAnalisis.setBounds(10, 125, 85, 14);
		panelConsulta.add(lblAnalisis);

		JScrollPane scrollAnalisisVis = new JScrollPane();
		scrollAnalisisVis.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollAnalisisVis.setBounds(100, 120, 205, 45);
		panelConsulta.add(scrollAnalisisVis);

		txtAnalisis = new JTextArea();
		txtAnalisis.setEditable(false);
		txtAnalisis.setLineWrap(true);
		txtAnalisis.setWrapStyleWord(true);
		txtAnalisis.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
		txtAnalisis.setBackground(new Color(224, 247, 250));
		scrollAnalisisVis.setViewportView(txtAnalisis);

		btnIndicarAnalisis = new JButton("Indicar Análisis");
		btnIndicarAnalisis.setFont(new Font("Bahnschrift", Font.BOLD, 11));
		btnIndicarAnalisis.setBackground(new Color(176, 224, 230));
		btnIndicarAnalisis.setForeground(new Color(70, 130, 180));
		btnIndicarAnalisis.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnIndicarAnalisis.setFocusPainted(false);
		btnIndicarAnalisis.addActionListener(e -> abrirIndicarAnalisis());

		if (consultaEdicion == null) {
			// Diseño original para modo registro
			btnIndicarAnalisis.setBounds(315, 120, 279, 25);
			panelConsulta.add(btnIndicarAnalisis);
		} else {
			// Diseño con botón de lista para modo listar/modificar
			btnIndicarAnalisis.setBounds(315, 120, 135, 25);
			panelConsulta.add(btnIndicarAnalisis);

			btnListaAnalisis = new JButton("Lista Análisis");
			btnListaAnalisis.setFont(new Font("Bahnschrift", Font.BOLD, 11));
			btnListaAnalisis.setBackground(new Color(176, 224, 230));
			btnListaAnalisis.setForeground(new Color(70, 130, 180));
			btnListaAnalisis.setBorder(new LineBorder(new Color(135, 206, 235), 2));
			btnListaAnalisis.setFocusPainted(false);
			btnListaAnalisis.addActionListener(e -> abrirListaAnalisis());
			btnListaAnalisis.setBounds(455, 120, 139, 25);
			panelConsulta.add(btnListaAnalisis);
		}

		// --- OBSERVACIONES ---
		JLabel lblObservaciones = new JLabel("Observaciones:");
		lblObservaciones.setForeground(new Color(70, 130, 180));
		lblObservaciones.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblObservaciones.setBounds(10, 180, 90, 14);
		panelConsulta.add(lblObservaciones);

		JScrollPane scrollObservaciones = new JScrollPane();
		scrollObservaciones.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollObservaciones.setBounds(100, 175, 494, 65);
		panelConsulta.add(scrollObservaciones);

		txtObservaciones = new JTextArea();
		txtObservaciones.setLineWrap(true);
		txtObservaciones.setWrapStyleWord(true);
		txtObservaciones.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtObservaciones.setBackground(new Color(224, 247, 250));
		scrollObservaciones.setViewportView(txtObservaciones);

		// --- PANEL INFERIOR DE ACCIONES ---
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 248, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		if (consultaEdicion == null) {
			// MODO REGISTRO
			JButton btnRealizar = new JButton("Realizar Consulta");
			btnRealizar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
			btnRealizar.setBackground(new Color(176, 224, 230));
			btnRealizar.setForeground(new Color(70, 130, 180));
			btnRealizar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
			btnRealizar.setFocusPainted(false);
			btnRealizar.addActionListener(e -> realizarConsulta());
			btnRealizar.setActionCommand("OK");
			buttonPane.add(btnRealizar);
			getRootPane().setDefaultButton(btnRealizar);
		} else {
			// MODO EDICIÓN / ELIMINACIÓN
			JButton btnModificar = new JButton("Modificar");
			btnModificar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
			btnModificar.setBackground(new Color(176, 224, 230));
			btnModificar.setForeground(new Color(70, 130, 180));
			btnModificar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
			btnModificar.setFocusPainted(false);
			btnModificar.addActionListener(e -> modificarConsulta());
			buttonPane.add(btnModificar);

			JButton btnEliminar = new JButton("Eliminar");
			btnEliminar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
			btnEliminar.setBackground(new Color(255, 182, 193));
			btnEliminar.setForeground(new Color(178, 34, 34));
			btnEliminar.setBorder(new LineBorder(new Color(240, 128, 128), 2));
			btnEliminar.setFocusPainted(false);
			btnEliminar.addActionListener(e -> eliminarConsulta());
			buttonPane.add(btnEliminar);
		}

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnCancelar.setBackground(new Color(176, 224, 230));
		btnCancelar.setForeground(new Color(70, 130, 180));
		btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCancelar.setFocusPainted(false);
		btnCancelar.addActionListener(e -> dispose());
		btnCancelar.setActionCommand("Cancel");
		buttonPane.add(btnCancelar);

		if (consultaEdicion != null) {
			cargarDatosConsultaEdicion();
		} else {
			cargarCitasFiltro("");
		}
	}

	private void abrirListaVacunas() {
		if (consultaEdicion == null || consultaEdicion.getVacunas() == null || consultaEdicion.getVacunas().isEmpty()) {
			JOptionPane.showMessageDialog(this, "No hay detalles de vacunas registrados en esta consulta.", "Vacunas de la Consulta", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		ListarDetalleVacuna listarVacunas = new ListarDetalleVacuna(consultaEdicion);
		listarVacunas.setModal(true);
		listarVacunas.setLocationRelativeTo(this);
		listarVacunas.setVisible(true);
	}

	private void abrirListaAnalisis() {
		if (consultaEdicion == null || consultaEdicion.getAnalisis() == null || consultaEdicion.getAnalisis().isEmpty()) {
			JOptionPane.showMessageDialog(this, "No hay detalles de análisis registrados en esta consulta.", "Análisis de la Consulta", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		ListarDetalleAnalisis listarAnalisis = new ListarDetalleAnalisis(consultaEdicion);
		listarAnalisis.setModal(true);
		listarAnalisis.setLocationRelativeTo(this);
		listarAnalisis.setVisible(true);
	}

	private void cargarDatosConsultaEdicion() {
		if (consultaEdicion == null) return;

		this.citaElegida = consultaEdicion.getCita();
		if (citaElegida != null) {
			this.pacienteActual = citaElegida.getPaciente();
			if (this.pacienteActual == null) {
				this.pacienteActual = Clinica.getInstancia().buscarPacienteXIdentificacion(citaElegida.getIdPersona());
			}

			if (this.pacienteActual != null) {
				String idPaciente = pacienteActual.getCedula() != null ? pacienteActual.getCedula() : pacienteActual.getId();
				txtPaciente.setText(pacienteActual.getNombre() + " " + (pacienteActual.getApellido() != null ? pacienteActual.getApellido() : "") + " - " + idPaciente);
			} else {
				txtPaciente.setText((citaElegida.getNombrePersona() != null ? citaElegida.getNombrePersona() : "") + " - " + (citaElegida.getIdPersona() != null ? citaElegida.getIdPersona() : ""));
			}

			Doctor doc = citaElegida.getDoctor() != null ? citaElegida.getDoctor() : consultaEdicion.getDoctor();
			if (doc != null) {
				txtDoctor.setText(doc.getNombre() + (doc.getApellido() != null ? " " + doc.getApellido() : ""));
			} else {
				txtDoctor.setText("N/A");
			}

			cbxCita.removeAllItems();
			cbxCita.addItem(citaElegida.getId() + " - " + citaElegida.getNombrePersona());
			cbxCita.setSelectedIndex(0);
		} else if (consultaEdicion.getDoctor() != null) {
			Doctor doc = consultaEdicion.getDoctor();
			txtDoctor.setText(doc.getNombre() + (doc.getApellido() != null ? " " + doc.getApellido() : ""));
		}

		if (consultaEdicion.getDiagnosticos() != null) {
			diagnosticosActuales = new ArrayList<>(consultaEdicion.getDiagnosticos());
		}

		if (consultaEdicion.getVacunas() != null) {
			vacunasIndicadas.clear();
			for (DetalleVacuna dv : consultaEdicion.getVacunas()) {
				if (dv != null && dv.getVacuna() != null) {
					vacunasIndicadas.add(dv.getVacuna());
				}
			}
		}

		if (consultaEdicion.getAnalisis() != null) {
			analisisIndicados.clear();
			for (DetalleAnalisis da : consultaEdicion.getAnalisis()) {
				if (da != null && da.getAnalisis() != null) {
					analisisIndicados.add(da.getAnalisis());
				}
			}
		}

		txtObservaciones.setText(consultaEdicion.getObservaciones() != null ? consultaEdicion.getObservaciones() : "");

		// Deshabilitar controles de filtro de cita al editar una consulta existente
		txtBuscarCita.setEnabled(false);
		spinFechaFiltro.setEnabled(false);
		cbxCita.setEnabled(false);

		actualizarTextoDiagnosticos();
		actualizarTextoVacunas();
		actualizarTextoAnalisis();
	}

	private void modificarConsulta() {
		consultaEdicion.setObservaciones(txtObservaciones.getText());

		if (consultaEdicion.getDiagnosticos() != null) {
			consultaEdicion.getDiagnosticos().clear();
		} else {
			consultaEdicion.setDiagnosticos(new ArrayList<>());
		}

		for (Diagnostico d : diagnosticosActuales) {
			consultaEdicion.addDiagnostico(d);
			Enfermedad enfermedadDiag = d.getEnfermedad();
			if (enfermedadDiag != null && pacienteActual != null) {
				pacienteActual.agregarEnfermedad(enfermedadDiag);
			}
		}

		crearDetalles(consultaEdicion);

		JOptionPane.showMessageDialog(this, "Consulta modificada con éxito.", "Modificación Exitosa", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}

	private void eliminarConsulta() {
		int confirm = JOptionPane.showConfirmDialog(
				this,
				"¿Está seguro de que desea eliminar esta consulta?",
				"Confirmar Eliminación",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE
		);

		if (confirm == JOptionPane.YES_OPTION) {
			if (Clinica.getInstancia().getConsultas() != null) {
				Clinica.getInstancia().getConsultas().remove(consultaEdicion);
			}

			if (pacienteActual != null && pacienteActual.getHistorialClinico() != null) {
				pacienteActual.getHistorialClinico().remove(consultaEdicion);
			}

			JOptionPane.showMessageDialog(this, "Consulta eliminada con éxito.", "Eliminación Exitosa", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	}

	private Date obtenerFechaCitaExistente() {
		if (Clinica.getInstancia().getCitas() != null) {
			for (Cita c : Clinica.getInstancia().getCitas()) {
				if (c.getEstado() == EstadoCita.PROGRAMADA && c.getFechaConsulta() != null) {
					return c.getFechaConsulta();
				}
			}
		}
		return new Date();
	}

	private void realizarConsulta() {
		if (!verificarDatos()) {
			return;
		}

		Consulta consulta = new Consulta(
				txtObservaciones.getText(),
				citaElegida
		);

		if (diagnosticosActuales != null) {
			for (Diagnostico d : diagnosticosActuales) {
				consulta.addDiagnostico(d);

				Enfermedad enfermedadDiag = d.getEnfermedad();
				if (enfermedadDiag != null && pacienteActual != null) {
					pacienteActual.agregarEnfermedad(enfermedadDiag);
				}
			}
		}

		crearDetalles(consulta);

		if (pacienteActual != null && pacienteActual.getHistorialClinico() != null) {
			pacienteActual.getHistorialClinico().add(consulta);
		}

		if (Clinica.getInstancia().getConsultas() != null) {
			Clinica.getInstancia().getConsultas().add(consulta);
		}

		controller.registrarConsulta(consulta);

		JOptionPane.showMessageDialog(null, "Consulta realizada con éxito.", "Consulta Exitosa", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}

	public void crearDetalles(Consulta consulta){
		ArrayList<DetalleVacuna> vacunasDetalle = new ArrayList<>();
		ArrayList<DetalleAnalisis> analisisDetalle = new ArrayList<>();

		for (Analisis analisis : analisisIndicados){
			analisisDetalle.add(new DetalleAnalisis(analisis, consulta));
		}

		consulta.setAnalisis(analisisDetalle);

		for (Vacuna vacuna : vacunasIndicadas){
			vacunasDetalle.add(new DetalleVacuna(consulta, vacuna));
		}

		consulta.setVacunas(vacunasDetalle);
	}

	public boolean verificarDatos(){
		if (cbxCita.getSelectedIndex() <= 0) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar una cita.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return false;
		}

		if (pacienteActual == null) {
			JOptionPane.showMessageDialog(null, "El paciente debe estar registrado en el sistema antes de realizar la consulta.", "Atención", JOptionPane.WARNING_MESSAGE);
			return false;
		}

		if (diagnosticosActuales.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe agregar al menos un diagnóstico.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return false;
		}

		return true;
	}

	private void cargarCitasFiltro(String filtro) {
		ActionListener[] listeners = cbxCita.getActionListeners();
		for (ActionListener listener : listeners) {
			cbxCita.removeActionListener(listener);
		}

		cbxCita.removeAllItems();
		cbxCita.addItem("<<Seleccione>>");

		String f = filtro.toLowerCase();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String fechaFiltroStr = spinFechaFiltro != null && spinFechaFiltro.getValue() != null ?
				sdf.format((Date) spinFechaFiltro.getValue()) : "";

		if (Clinica.getInstancia().getCitas() != null) {
			for (Cita cita : Clinica.getInstancia().getCitas()) {
				if (cita.getEstado() == EstadoCita.PROGRAMADA && cita.getFechaConsulta() != null) {
					String fechaCitaStr = sdf.format(cita.getFechaConsulta());

					if (!fechaFiltroStr.isEmpty() && !fechaCitaStr.equals(fechaFiltroStr)) {
						continue;
					}

					String idCita = cita.getId() != null ? cita.getId().toLowerCase() : "";
					String nombrePers = cita.getNombrePersona() != null ? cita.getNombrePersona().toLowerCase() : "";
					String idPers = cita.getIdPersona() != null ? cita.getIdPersona().toLowerCase() : "";

					if (f.isEmpty() || idCita.contains(f) || nombrePers.contains(f) || idPers.contains(f)) {
						String fechaStr = utilidad.Formato.getDateString(cita.getFechaConsulta());
						String item = cita.getId() + " - " + cita.getNombrePersona() + " (" + cita.getIdPersona() + ")" + " - (" + fechaStr + ")";
						cbxCita.addItem(item);
					}
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
			citaElegida = Clinica.getInstancia().buscarCitaXId(codigo);

			if (citaElegida != null) {
				pacienteActual = citaElegida.getPaciente();
				if (pacienteActual == null) {
					pacienteActual = Clinica.getInstancia().buscarPacienteXIdentificacion(citaElegida.getIdPersona());
				}

				if (pacienteActual != null) {
					String idPaciente = pacienteActual.getCedula() != null ? pacienteActual.getCedula() : pacienteActual.getId();
					txtPaciente.setText(pacienteActual.getNombre() + " " + (pacienteActual.getApellido() != null ? pacienteActual.getApellido() : "") + " - " + idPaciente);
				} else {
					txtPaciente.setText(citaElegida.getNombrePersona() + " - " + citaElegida.getIdPersona());
				}

				Doctor doc = citaElegida.getDoctor();
				if (doc != null) {
					txtDoctor.setText(doc.getNombre() + (doc.getApellido() != null ? " " + doc.getApellido() : ""));
				} else {
					txtDoctor.setText("N/A");
				}
			}
		} else {
			limpiarCampos();
			pacienteActual = null;
		}
	}

	private void abrirCrearDiagnostico() {
		if (pacienteActual == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar una cita con un paciente asignado primero.", "Atención", JOptionPane.WARNING_MESSAGE);
			return;
		}
		CrearDiagnostico dialogo = new CrearDiagnostico(diagnosticosActuales, pacienteActual);
		dialogo.setModal(true);
		dialogo.setLocationRelativeTo(this);
		dialogo.setVisible(true);
		Diagnostico diag = dialogo.getDiagnosticoCreado();
		if (diag != null) {
			diagnosticosActuales.add(diag);
			actualizarTextoDiagnosticos();
		}
	}

	private void actualizarTextoDiagnosticos() {
		if (diagnosticosActuales.isEmpty()) {
			txtDiagnostico.setText("");
		} else {
			StringBuilder sb = new StringBuilder();
			for (Diagnostico d : diagnosticosActuales) {
				String enf = (d.getEnfermedad() != null) ? d.getEnfermedad().getNombre() : "Sin Enfermedad";
				sb.append("• ").append(enf).append("\n");
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
				String enf = (d.getEnfermedad() != null) ? d.getEnfermedad().getNombre() : "Sin Enfermedad";
				opciones[i] = (i + 1) + ". " + enf;
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
				for (int i = 0; i < diagnosticosActuales.size(); i++) {
					if (seleccion.startsWith((i + 1) + ".")) {
						CrearDiagnostico dialogo = new CrearDiagnostico(diagnosticosActuales.get(i), diagnosticosActuales);
						dialogo.setModal(true);
						dialogo.setLocationRelativeTo(this);
						dialogo.setVisible(true);
						break;
					}
				}
			}
		}

		actualizarTextoDiagnosticos();
	}

	private void abrirAplicarVacunas() {
		if (pacienteActual == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar una cita con un paciente asignado primero.", "Atención", JOptionPane.WARNING_MESSAGE);
			return;
		}

		List<Vacuna> disponibles = Clinica.getInstancia().getVacunas();
		if (disponibles == null || disponibles.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No hay vacunas registradas en la clínica.", "Información", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		JDialog dialogVacunas = new JDialog(this, "Seleccionar Vacunas a Aplicar", true);
		dialogVacunas.setSize(640, 520);
		dialogVacunas.setLocationRelativeTo(this);
		dialogVacunas.setLayout(new BorderLayout());

		JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
		panelContenido.setBackground(new Color(240, 248, 255));
		panelContenido.setBorder(new EmptyBorder(12, 12, 12, 12));

		JPanel panelFiltro = new JPanel(new BorderLayout(8, 8));
		panelFiltro.setBackground(new Color(240, 248, 255));

		JLabel lblBuscar = new JLabel("Buscar por Nombre:");
		lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblBuscar.setForeground(new Color(70, 130, 180));

		JTextField txtBuscar = new JTextField();
		txtBuscar.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtBuscar.setPreferredSize(new Dimension(0, 30));
		txtBuscar.setBorder(new CompoundBorder(
				new LineBorder(new Color(135, 206, 235), 1),
				new EmptyBorder(4, 8, 4, 8)
		));

		panelFiltro.add(lblBuscar, BorderLayout.WEST);
		panelFiltro.add(txtBuscar, BorderLayout.CENTER);
		panelContenido.add(panelFiltro, BorderLayout.NORTH);

		JPanel panelGrid = new JPanel(new GridLayout(0, 2, 10, 10));
		panelGrid.setBackground(new Color(240, 248, 255));

		JPanel panelNorte = new JPanel(new BorderLayout());
		panelNorte.setBackground(new Color(240, 248, 255));
		panelNorte.add(panelGrid, BorderLayout.NORTH);

		JScrollPane scroll = new JScrollPane(panelNorte);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelContenido.add(scroll, BorderLayout.CENTER);

		dialogVacunas.add(panelContenido, BorderLayout.CENTER);

		List<Vacuna> seleccionadasTemp = new ArrayList<>(vacunasIndicadas);

		Runnable cargarTarjetas = () -> {
			panelGrid.removeAll();
			String filtro = txtBuscar.getText().trim().toLowerCase();
			boolean algunResultado = false;

			for (Vacuna v : disponibles) {
				if (filtro.isEmpty() || (v.getNombre() != null && v.getNombre().toLowerCase().contains(filtro))) {
					algunResultado = true;

					JPanel tarjeta = new JPanel(new BorderLayout(8, 0));
					tarjeta.setBackground(Color.WHITE);
					tarjeta.setBorder(new CompoundBorder(
							new LineBorder(new Color(173, 216, 230), 1, true),
							new EmptyBorder(8, 8, 8, 8)
					));

					JCheckBox chk = new JCheckBox();
					chk.setBackground(Color.WHITE);
					chk.setSelected(seleccionadasTemp.contains(v));
					chk.addActionListener(ev -> {
						if (chk.isSelected()) {
							if (!seleccionadasTemp.contains(v)) {
								seleccionadasTemp.add(v);
							}
						} else {
							seleccionadasTemp.remove(v);
						}
					});
					tarjeta.add(chk, BorderLayout.WEST);

					JPanel panelInfo = new JPanel();
					panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
					panelInfo.setBackground(Color.WHITE);

					JLabel lblNombre = new JLabel(v.getNombre());
					lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 13));
					lblNombre.setForeground(new Color(70, 130, 180));

					JLabel lblFabricante = new JLabel("Fabricante: " + (v.getFabricante() != null ? v.getFabricante() : "N/A"));
					lblFabricante.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
					lblFabricante.setForeground(Color.GRAY);

					JLabel lblDosis = new JLabel("Dosis: " + v.getCantDosis());
					lblDosis.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
					lblDosis.setForeground(Color.GRAY);

					panelInfo.add(lblNombre);
					panelInfo.add(Box.createVerticalStrut(2));
					panelInfo.add(lblFabricante);
					panelInfo.add(Box.createVerticalStrut(2));
					panelInfo.add(lblDosis);

					tarjeta.add(panelInfo, BorderLayout.CENTER);
					panelGrid.add(tarjeta);
				}
			}

			if (!algunResultado) {
				JLabel lblVacio = new JLabel("No se encontraron coincidencias.", SwingConstants.CENTER);
				lblVacio.setFont(new Font("Bahnschrift", Font.ITALIC, 12));
				lblVacio.setForeground(Color.GRAY);
				panelGrid.add(lblVacio);
			}

			panelGrid.revalidate();
			panelGrid.repaint();
			scroll.revalidate();
			scroll.repaint();
		};

		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				cargarTarjetas.run();
			}
		});

		cargarTarjetas.run();

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelBotones.setBackground(new Color(240, 248, 255));

		JButton btnAceptar = new JButton("Guardar Selección");
		btnAceptar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		btnAceptar.setBackground(new Color(176, 224, 230));
		btnAceptar.setForeground(new Color(70, 130, 180));
		btnAceptar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnAceptar.setFocusPainted(false);
		btnAceptar.setPreferredSize(new Dimension(140, 30));
		btnAceptar.addActionListener(e -> {
			vacunasIndicadas.clear();
			vacunasIndicadas.addAll(seleccionadasTemp);
			actualizarTextoVacunas();
			dialogVacunas.dispose();
		});

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		btnCancelar.setBackground(new Color(176, 224, 230));
		btnCancelar.setForeground(new Color(70, 130, 180));
		btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCancelar.setFocusPainted(false);
		btnCancelar.setPreferredSize(new Dimension(90, 30));
		btnCancelar.addActionListener(e -> dialogVacunas.dispose());

		panelBotones.add(btnAceptar);
		panelBotones.add(btnCancelar);
		dialogVacunas.add(panelBotones, BorderLayout.SOUTH);

		dialogVacunas.setVisible(true);
	}

	private void abrirIndicarAnalisis() {
		if (pacienteActual == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar una cita con un paciente asignado primero.", "Atención", JOptionPane.WARNING_MESSAGE);
			return;
		}

		List<Analisis> disponibles = Clinica.getInstancia().getAnalisis();
		if (disponibles == null || disponibles.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No hay análisis clínicos registrados en la clínica.", "Información", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		JDialog dialogAnalisis = new JDialog(this, "Seleccionar Análisis a Indicar", true);
		dialogAnalisis.setSize(640, 520);
		dialogAnalisis.setLocationRelativeTo(this);
		dialogAnalisis.setLayout(new BorderLayout());

		JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
		panelContenido.setBackground(new Color(240, 248, 255));
		panelContenido.setBorder(new EmptyBorder(12, 12, 12, 12));

		JPanel panelFiltro = new JPanel(new BorderLayout(8, 8));
		panelFiltro.setBackground(new Color(240, 248, 255));

		JLabel lblBuscar = new JLabel("Buscar por Nombre:");
		lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblBuscar.setForeground(new Color(70, 130, 180));

		JTextField txtBuscar = new JTextField();
		txtBuscar.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtBuscar.setPreferredSize(new Dimension(0, 30));
		txtBuscar.setBorder(new CompoundBorder(
				new LineBorder(new Color(135, 206, 235), 1),
				new EmptyBorder(4, 8, 4, 8)
		));

		panelFiltro.add(lblBuscar, BorderLayout.WEST);
		panelFiltro.add(txtBuscar, BorderLayout.CENTER);
		panelContenido.add(panelFiltro, BorderLayout.NORTH);

		JPanel panelGrid = new JPanel(new GridLayout(0, 2, 10, 10));
		panelGrid.setBackground(new Color(240, 248, 255));

		JPanel panelNorte = new JPanel(new BorderLayout());
		panelNorte.setBackground(new Color(240, 248, 255));
		panelNorte.add(panelGrid, BorderLayout.NORTH);

		JScrollPane scroll = new JScrollPane(panelNorte);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelContenido.add(scroll, BorderLayout.CENTER);

		dialogAnalisis.add(panelContenido, BorderLayout.CENTER);

		List<Analisis> seleccionadosTemp = new ArrayList<>(analisisIndicados);

		Runnable cargarTarjetas = () -> {
			panelGrid.removeAll();
			String filtro = txtBuscar.getText().trim().toLowerCase();
			boolean algunResultado = false;

			for (Analisis a : disponibles) {
				if (filtro.isEmpty() || (a.getNombre() != null && a.getNombre().toLowerCase().contains(filtro))) {
					algunResultado = true;

					JPanel tarjeta = new JPanel(new BorderLayout(8, 0));
					tarjeta.setBackground(Color.WHITE);
					tarjeta.setBorder(new CompoundBorder(
							new LineBorder(new Color(173, 216, 230), 1, true),
							new EmptyBorder(8, 8, 8, 8)
					));

					JCheckBox chk = new JCheckBox();
					chk.setBackground(Color.WHITE);
					chk.setSelected(seleccionadosTemp.contains(a));
					chk.addActionListener(ev -> {
						if (chk.isSelected()) {
							if (!seleccionadosTemp.contains(a)) {
								seleccionadosTemp.add(a);
							}
						} else {
							seleccionadosTemp.remove(a);
						}
					});
					tarjeta.add(chk, BorderLayout.WEST);

					JPanel panelInfo = new JPanel();
					panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
					panelInfo.setBackground(Color.WHITE);

					JLabel lblNombre = new JLabel(a.getNombre());
					lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 13));
					lblNombre.setForeground(new Color(70, 130, 180));

					JLabel lblTipo = new JLabel("Tipo: " + (a.getTipo() != null ? a.getTipo() : "N/A"));
					lblTipo.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
					lblTipo.setForeground(Color.GRAY);

					JLabel lblUnidad = new JLabel("Rango: " + a.getValorMin() + " - " + a.getValorMax() + " " + (a.getUnidadMedida() != null ? a.getUnidadMedida() : ""));
					lblUnidad.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
					lblUnidad.setForeground(Color.GRAY);

					panelInfo.add(lblNombre);
					panelInfo.add(Box.createVerticalStrut(2));
					panelInfo.add(lblTipo);
					panelInfo.add(Box.createVerticalStrut(2));
					panelInfo.add(lblUnidad);

					JButton btnDetalles = new JButton("Ver Detalles");
					btnDetalles.setFont(new Font("Bahnschrift", Font.BOLD, 10));
					btnDetalles.setBackground(new Color(224, 247, 250));
					btnDetalles.setForeground(new Color(70, 130, 180));
					btnDetalles.setBorder(new LineBorder(new Color(135, 206, 235), 1));
					btnDetalles.setFocusPainted(false);
					btnDetalles.addActionListener(ev -> {
						String detalle = "Análisis: " + a.getNombre() + "\n"
								+ "Tipo: " + a.getTipo() + "\n"
								+ "Unidad: " + a.getUnidadMedida() + "\n\n"
								+ "Valores de Referencia:\n"
								+ "• Mínimo: " + a.getValorMin() + " " + a.getUnidadMedida() + "\n"
								+ "• Promedio: " + a.getValorProm() + " " + a.getUnidadMedida() + "\n"
								+ "• Máximo: " + a.getValorMax() + " " + a.getUnidadMedida();
						JOptionPane.showMessageDialog(dialogAnalisis, detalle, "Detalle del Análisis", JOptionPane.INFORMATION_MESSAGE);
					});

					tarjeta.add(panelInfo, BorderLayout.CENTER);
					tarjeta.add(btnDetalles, BorderLayout.EAST);
					panelGrid.add(tarjeta);
				}
			}

			if (!algunResultado) {
				JLabel lblVacio = new JLabel("No se encontraron coincidencias.", SwingConstants.CENTER);
				lblVacio.setFont(new Font("Bahnschrift", Font.ITALIC, 12));
				lblVacio.setForeground(Color.GRAY);
				panelGrid.add(lblVacio);
			}

			panelGrid.revalidate();
			panelGrid.repaint();
			scroll.revalidate();
			scroll.repaint();
		};

		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				cargarTarjetas.run();
			}
		});

		cargarTarjetas.run();

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelBotones.setBackground(new Color(240, 248, 255));

		JButton btnAceptar = new JButton("Guardar Selección");
		btnAceptar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		btnAceptar.setBackground(new Color(176, 224, 230));
		btnAceptar.setForeground(new Color(70, 130, 180));
		btnAceptar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnAceptar.setFocusPainted(false);
		btnAceptar.setPreferredSize(new Dimension(140, 30));
		btnAceptar.addActionListener(e -> {
			analisisIndicados.clear();
			analisisIndicados.addAll(seleccionadosTemp);
			actualizarTextoAnalisis();
			dialogAnalisis.dispose();
		});

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		btnCancelar.setBackground(new Color(176, 224, 230));
		btnCancelar.setForeground(new Color(70, 130, 180));
		btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCancelar.setFocusPainted(false);
		btnCancelar.setPreferredSize(new Dimension(90, 30));
		btnCancelar.addActionListener(e -> dialogAnalisis.dispose());

		panelBotones.add(btnAceptar);
		panelBotones.add(btnCancelar);
		dialogAnalisis.add(panelBotones, BorderLayout.SOUTH);

		dialogAnalisis.setVisible(true);
	}

	private void actualizarTextoVacunas() {
		if (vacunasIndicadas.isEmpty()) {
			txtVacunas.setText("");
		} else {
			StringBuilder sb = new StringBuilder();
			for (Vacuna v : vacunasIndicadas) {
				sb.append("• ").append(v.getNombre()).append("\n");
			}
			txtVacunas.setText(sb.toString().trim());
			txtVacunas.setCaretPosition(0);
		}
	}

	private void actualizarTextoAnalisis() {
		if (analisisIndicados.isEmpty()) {
			txtAnalisis.setText("");
		} else {
			StringBuilder sb = new StringBuilder();
			for (Analisis a : analisisIndicados) {
				sb.append("• ").append(a.getNombre()).append("\n");
			}
			txtAnalisis.setText(sb.toString().trim());
			txtAnalisis.setCaretPosition(0);
		}
	}

	private void limpiarCampos() {
		if (txtPaciente != null) txtPaciente.setText("");
		if (txtDoctor != null) txtDoctor.setText("");
		if (txtDiagnostico != null) txtDiagnostico.setText("");
		if (txtVacunas != null) txtVacunas.setText("");
		if (txtAnalisis != null) txtAnalisis.setText("");
		if (txtObservaciones != null) txtObservaciones.setText("");

		diagnosticosActuales.clear();
		vacunasIndicadas.clear();
		analisisIndicados.clear();
		pacienteActual = null;
	}
}