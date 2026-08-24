//package visual.consultorio;
//
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.swing.*;
//import javax.swing.border.CompoundBorder;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.LineBorder;
//import javax.swing.border.TitledBorder;
//
//import bd.ConsultaDAO;
//import bd.catalogo.DiagnosticoDAO;
//import controllers.ConsultaController;
//import logico.Clinica;
//import logico.catalogo.Doctor;
//import logico.catalogo.*;
//import logico.consultorio.*;
//import logico.enfermeria.DetalleAnalisis;
//import logico.enfermeria.DetalleVacuna;
//import visual.enfermeria.ListarDetalleAnalisis;
//import visual.enfermeria.ListarDetalleVacuna;
//
//public class CrearConsulta extends JDialog {
//	private static final long serialVersionUID = 1L;
//	private final JPanel contentPanel = new JPanel();
//	private JTextField txtBuscarCita;
//	private JSpinner spinFechaFiltro;
//	private JComboBox<String> cbxCita;
//	private JTextField txtPaciente;
//	private JTextField txtDoctor;
//
//	private JTextArea txtDiagnostico;
//	private JButton btnCrearDiagnostico;
//	private JButton btnVerDiagnosticos;
//
//	private JTextArea txtVacunas;
//	private JButton btnAplicarVacunas;
//	private JButton btnListaVacunas;
//
//	private JTextArea txtAnalisis;
//	private JButton btnIndicarAnalisis;
//	private JButton btnListaAnalisis;
//
//	private JTextArea txtTratamientos;
//	private JButton btnIndicarTratamientos;
//	private JButton btnListaTratamientos;
//
//	private JTextArea txtObservaciones;
//
//	private ArrayList<Diagnostico> diagnosticosActuales = new ArrayList<>();
//	private ArrayList<Vacuna> vacunasIndicadas = new ArrayList<>();
//	private ArrayList<Analisis> analisisIndicados = new ArrayList<>();
//
//	// Listas para control de cambios en modo edición (Sin mapas)
//	private ArrayList<Diagnostico> diagnosticosNuevos = new ArrayList<>();
//	private ArrayList<Diagnostico> diagnosticosModificados = new ArrayList<>();
//	private ArrayList<Diagnostico> diagnosticosEliminados = new ArrayList<>();
//
//	private ArrayList<DetalleVacuna> vacunasNuevas = new ArrayList<>();
//	private ArrayList<DetalleVacuna> vacunasEliminadas = new ArrayList<>();
//	private ArrayList<DetalleVacuna> vacunasOriginalesEdicion = new ArrayList<>();
//
//	private ArrayList<DetalleAnalisis> analisisNuevos = new ArrayList<>();
//	private ArrayList<DetalleAnalisis> analisisEliminadas = new ArrayList<>();
//	private ArrayList<DetalleAnalisis> analisisOriginalesEdicion = new ArrayList<>();
//
//	private Paciente pacienteActual = null;
//	private Cita citaElegida = null;
//	private ConsultaController controller = null;
//
//	private Consulta consultaEdicion = null;
//
//	public static void main(String[] args) {
//		try {
//			CrearConsulta dialog = new CrearConsulta();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public CrearConsulta() {
//		this(null);
//	}
//
//	public CrearConsulta(Consulta consulta) {
//		this.consultaEdicion = consulta;
//		controller = new ConsultaController();
//
//		setTitle(consultaEdicion == null ? "Realizar Consulta" : "Detalle / Modificación de Consulta");
//		setBounds(100, 100, 685, 575);
//		setLocationRelativeTo(null);
//		getContentPane().setLayout(new BorderLayout());
//		contentPanel.setBackground(new Color(240, 248, 255));
//		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
//		getContentPane().add(contentPanel, BorderLayout.CENTER);
//		contentPanel.setLayout(null);
//
//		// --- PANEL DE INFORMACIÓN DE LA CITA ---
//		JPanel panelCita = new JPanel();
//		panelCita.setBackground(Color.WHITE);
//		panelCita.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Información de la Cita", TitledBorder.CENTER, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(70, 130, 180)));
//		panelCita.setBounds(20, 11, 614, 130);
//		contentPanel.add(panelCita);
//		panelCita.setLayout(null);
//
//		JLabel lblBuscarCita = new JLabel("Buscar:");
//		lblBuscarCita.setForeground(new Color(70, 130, 180));
//		lblBuscarCita.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		lblBuscarCita.setBounds(10, 28, 45, 14);
//		panelCita.add(lblBuscarCita);
//
//		txtBuscarCita = new JTextField();
//		txtBuscarCita.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
//		txtBuscarCita.setBackground(Color.WHITE);
//		txtBuscarCita.setToolTipText("Filtrar por código, nombre o cédula");
//		txtBuscarCita.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				cargarCitasFiltro(txtBuscarCita.getText().trim());
//				if (cbxCita.getItemCount() > 1 && !txtBuscarCita.getText().trim().isEmpty()) {
//					cbxCita.showPopup();
//				}
//			}
//		});
//		panelCita.add(txtBuscarCita);
//		txtBuscarCita.setColumns(10);
//
//		JLabel lblFechaFiltro = new JLabel("Fecha:");
//		lblFechaFiltro.setForeground(new Color(70, 130, 180));
//		lblFechaFiltro.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//
//		Date fechaInicial = obtenerFechaCitaExistente();
//		SpinnerDateModel dateModel = new SpinnerDateModel(fechaInicial, null, null, java.util.Calendar.DAY_OF_MONTH);
//		spinFechaFiltro = new JSpinner(dateModel);
//		JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinFechaFiltro, "dd/MM/yyyy");
//		spinFechaFiltro.setEditor(dateEditor);
//		spinFechaFiltro.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
//		spinFechaFiltro.addChangeListener(e -> cargarCitasFiltro(txtBuscarCita.getText().trim()));
//
//		JLabel lblCita = new JLabel("Cita:");
//		lblCita.setForeground(new Color(70, 130, 180));
//		lblCita.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//
//		cbxCita = new JComboBox<>();
//		cbxCita.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
//		cbxCita.setBackground(new Color(224, 247, 250));
//		cbxCita.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				cargarDatosCita();
//			}
//		});
//		cbxCita.setModel(new DefaultComboBoxModel<>(new String[] {"<<Seleccione>>"}));
//
//		if (consultaEdicion == null) {
//			txtBuscarCita.setBounds(55, 25, 205, 22);
//			lblFechaFiltro.setVisible(false);
//			spinFechaFiltro.setVisible(false);
//			lblCita.setBounds(270, 28, 32, 14);
//			cbxCita.setBounds(305, 25, 290, 22);
//		} else {
//			txtBuscarCita.setBounds(55, 25, 105, 22);
//			lblFechaFiltro.setBounds(170, 28, 42, 14);
//			spinFechaFiltro.setBounds(212, 25, 110, 22);
//			spinFechaFiltro.setEnabled(false);
//			lblCita.setBounds(332, 28, 32, 14);
//			cbxCita.setBounds(365, 25, 230, 22);
//			panelCita.add(lblFechaFiltro);
//			panelCita.add(spinFechaFiltro);
//		}
//		panelCita.add(lblCita);
//		panelCita.add(cbxCita);
//
//		JLabel lblPaciente = new JLabel("Paciente:");
//		lblPaciente.setForeground(new Color(70, 130, 180));
//		lblPaciente.setFont(new Font("Bahnschrift", Font.BOLD, 13));
//		lblPaciente.setBounds(10, 60, 80, 14);
//		panelCita.add(lblPaciente);
//
//		txtPaciente = new JTextField();
//		txtPaciente.setEditable(false);
//		txtPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
//		txtPaciente.setBackground(new Color(224, 247, 250));
//		txtPaciente.setBounds(100, 57, 494, 22);
//		panelCita.add(txtPaciente);
//		txtPaciente.setColumns(10);
//
//		JLabel lblDoctor = new JLabel("Doctor:");
//		lblDoctor.setForeground(new Color(70, 130, 180));
//		lblDoctor.setFont(new Font("Bahnschrift", Font.BOLD, 13));
//		lblDoctor.setBounds(10, 92, 80, 14);
//		panelCita.add(lblDoctor);
//
//		txtDoctor = new JTextField();
//		txtDoctor.setEditable(false);
//		txtDoctor.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
//		txtDoctor.setBackground(new Color(224, 247, 250));
//		txtDoctor.setBounds(100, 89, 494, 22);
//		panelCita.add(txtDoctor);
//		txtDoctor.setColumns(10);
//
//		// --- PANEL DE DATOS DE LA CONSULTA ---
//		JPanel panelConsulta = new JPanel();
//		panelConsulta.setBackground(Color.WHITE);
//		panelConsulta.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Datos de la Consulta", TitledBorder.CENTER, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(70, 130, 180)));
//		panelConsulta.setBounds(20, 150, 614, 340);
//		contentPanel.add(panelConsulta);
//		panelConsulta.setLayout(null);
//
//		// --- DIAGNÓSTICOS ---
//		JLabel lblDiagnostico = new JLabel("Diagnósticos:");
//		lblDiagnostico.setForeground(new Color(70, 130, 180));
//		lblDiagnostico.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		lblDiagnostico.setBounds(10, 25, 85, 14);
//		panelConsulta.add(lblDiagnostico);
//
//		JScrollPane scrollDiagVis = new JScrollPane();
//		scrollDiagVis.setBorder(new LineBorder(new Color(173, 216, 230), 1));
//		scrollDiagVis.setBounds(100, 20, 205, 45);
//		panelConsulta.add(scrollDiagVis);
//
//		txtDiagnostico = new JTextArea();
//		txtDiagnostico.setEditable(false);
//		txtDiagnostico.setLineWrap(true);
//		txtDiagnostico.setWrapStyleWord(true);
//		txtDiagnostico.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
//		txtDiagnostico.setBackground(new Color(224, 247, 250));
//		scrollDiagVis.setViewportView(txtDiagnostico);
//
//		btnCrearDiagnostico = new JButton("Agregar Diagnóstico");
//		btnCrearDiagnostico.setFont(new Font("Bahnschrift", Font.BOLD, 11));
//		btnCrearDiagnostico.setBackground(new Color(176, 224, 230));
//		btnCrearDiagnostico.setForeground(new Color(70, 130, 180));
//		btnCrearDiagnostico.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//		btnCrearDiagnostico.setFocusPainted(false);
//		btnCrearDiagnostico.addActionListener(e -> abrirCrearDiagnostico());
//		btnCrearDiagnostico.setBounds(315, 20, 135, 25);
//		panelConsulta.add(btnCrearDiagnostico);
//
//		btnVerDiagnosticos = new JButton("Ver Diagnósticos");
//		btnVerDiagnosticos.setFont(new Font("Bahnschrift", Font.BOLD, 11));
//		btnVerDiagnosticos.setBackground(new Color(176, 224, 230));
//		btnVerDiagnosticos.setForeground(new Color(70, 130, 180));
//		btnVerDiagnosticos.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//		btnVerDiagnosticos.setFocusPainted(false);
//		btnVerDiagnosticos.addActionListener(e -> mostrarDiagnosticos());
//		btnVerDiagnosticos.setBounds(455, 20, 139, 25);
//		panelConsulta.add(btnVerDiagnosticos);
//
//		// --- VACUNAS ---
//		JLabel lblVacunas = new JLabel("Vacunas:");
//		lblVacunas.setForeground(new Color(70, 130, 180));
//		lblVacunas.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		lblVacunas.setBounds(10, 75, 85, 14);
//		panelConsulta.add(lblVacunas);
//
//		JScrollPane scrollVacunasVis = new JScrollPane();
//		scrollVacunasVis.setBorder(new LineBorder(new Color(173, 216, 230), 1));
//		scrollVacunasVis.setBounds(100, 70, 205, 45);
//		panelConsulta.add(scrollVacunasVis);
//
//		txtVacunas = new JTextArea();
//		txtVacunas.setEditable(false);
//		txtVacunas.setLineWrap(true);
//		txtVacunas.setWrapStyleWord(true);
//		txtVacunas.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
//		txtVacunas.setBackground(new Color(224, 247, 250));
//		scrollVacunasVis.setViewportView(txtVacunas);
//
//		btnAplicarVacunas = new JButton("Aplicar Vacunas");
//		btnAplicarVacunas.setFont(new Font("Bahnschrift", Font.BOLD, 11));
//		btnAplicarVacunas.setBackground(new Color(176, 224, 230));
//		btnAplicarVacunas.setForeground(new Color(70, 130, 180));
//		btnAplicarVacunas.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//		btnAplicarVacunas.setFocusPainted(false);
//		btnAplicarVacunas.addActionListener(e -> abrirAplicarVacunas());
//
//		if (consultaEdicion == null) {
//			btnAplicarVacunas.setBounds(315, 70, 279, 25);
//			panelConsulta.add(btnAplicarVacunas);
//		} else {
//			btnAplicarVacunas.setBounds(315, 70, 135, 25);
//			panelConsulta.add(btnAplicarVacunas);
//
//			btnListaVacunas = new JButton("Lista Vacuna");
//			btnListaVacunas.setFont(new Font("Bahnschrift", Font.BOLD, 11));
//			btnListaVacunas.setBackground(new Color(176, 224, 230));
//			btnListaVacunas.setForeground(new Color(70, 130, 180));
//			btnListaVacunas.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//			btnListaVacunas.setFocusPainted(false);
//			btnListaVacunas.addActionListener(e -> abrirListaVacunas());
//			btnListaVacunas.setBounds(455, 70, 139, 25);
//			panelConsulta.add(btnListaVacunas);
//		}
//
//		// --- ANÁLISIS ---
//		JLabel lblAnalisis = new JLabel("Análisis:");
//		lblAnalisis.setForeground(new Color(70, 130, 180));
//		lblAnalisis.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		lblAnalisis.setBounds(10, 125, 85, 14);
//		panelConsulta.add(lblAnalisis);
//
//		JScrollPane scrollAnalisisVis = new JScrollPane();
//		scrollAnalisisVis.setBorder(new LineBorder(new Color(173, 216, 230), 1));
//		scrollAnalisisVis.setBounds(100, 120, 205, 45);
//		panelConsulta.add(scrollAnalisisVis);
//
//		txtAnalisis = new JTextArea();
//		txtAnalisis.setEditable(false);
//		txtAnalisis.setLineWrap(true);
//		txtAnalisis.setWrapStyleWord(true);
//		txtAnalisis.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
//		txtAnalisis.setBackground(new Color(224, 247, 250));
//		scrollAnalisisVis.setViewportView(txtAnalisis);
//
//		btnIndicarAnalisis = new JButton("Indicar Análisis");
//		btnIndicarAnalisis.setFont(new Font("Bahnschrift", Font.BOLD, 11));
//		btnIndicarAnalisis.setBackground(new Color(176, 224, 230));
//		btnIndicarAnalisis.setForeground(new Color(70, 130, 180));
//		btnIndicarAnalisis.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//		btnIndicarAnalisis.setFocusPainted(false);
//		btnIndicarAnalisis.addActionListener(e -> abrirIndicarAnalisis());
//
//		if (consultaEdicion == null) {
//			btnIndicarAnalisis.setBounds(315, 120, 279, 25);
//			panelConsulta.add(btnIndicarAnalisis);
//		} else {
//			btnIndicarAnalisis.setBounds(315, 120, 135, 25);
//			panelConsulta.add(btnIndicarAnalisis);
//
//			btnListaAnalisis = new JButton("Lista Análisis");
//			btnListaAnalisis.setFont(new Font("Bahnschrift", Font.BOLD, 11));
//			btnListaAnalisis.setBackground(new Color(176, 224, 230));
//			btnListaAnalisis.setForeground(new Color(70, 130, 180));
//			btnListaAnalisis.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//			btnListaAnalisis.setFocusPainted(false);
//			btnListaAnalisis.addActionListener(e -> abrirListaAnalisis());
//			btnListaAnalisis.setBounds(455, 120, 139, 25);
//			panelConsulta.add(btnListaAnalisis);
//		}
//
//		// --- TRATAMIENTOS ---
//		JLabel lblTratamientos = new JLabel("Tratamientos:");
//		lblTratamientos.setForeground(new Color(70, 130, 180));
//		lblTratamientos.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		lblTratamientos.setBounds(10, 175, 85, 14);
//		panelConsulta.add(lblTratamientos);
//
//		JScrollPane scrollTratamientosVis = new JScrollPane();
//		scrollTratamientosVis.setBorder(new LineBorder(new Color(173, 216, 230), 1));
//		scrollTratamientosVis.setBounds(100, 170, 205, 45);
//		panelConsulta.add(scrollTratamientosVis);
//
//		txtTratamientos = new JTextArea();
//		txtTratamientos.setEditable(false);
//		txtTratamientos.setLineWrap(true);
//		txtTratamientos.setWrapStyleWord(true);
//		txtTratamientos.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
//		txtTratamientos.setBackground(new Color(224, 247, 250));
//		scrollTratamientosVis.setViewportView(txtTratamientos);
//
//		btnIndicarTratamientos = new JButton("Indicar Tratamiento");
//		btnIndicarTratamientos.setFont(new Font("Bahnschrift", Font.BOLD, 11));
//		btnIndicarTratamientos.setBackground(new Color(176, 224, 230));
//		btnIndicarTratamientos.setForeground(new Color(70, 130, 180));
//		btnIndicarTratamientos.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//		btnIndicarTratamientos.setFocusPainted(false);
//		btnIndicarTratamientos.addActionListener(e -> abrirIndicarTratamientos());
//
//		if (consultaEdicion == null) {
//			btnIndicarTratamientos.setBounds(315, 170, 279, 25);
//			panelConsulta.add(btnIndicarTratamientos);
//		} else {
//			btnIndicarTratamientos.setBounds(315, 170, 135, 25);
//			panelConsulta.add(btnIndicarTratamientos);
//
//			btnListaTratamientos = new JButton("Lista Tratamiento");
//			btnListaTratamientos.setFont(new Font("Bahnschrift", Font.BOLD, 11));
//			btnListaTratamientos.setBackground(new Color(176, 224, 230));
//			btnListaTratamientos.setForeground(new Color(70, 130, 180));
//			btnListaTratamientos.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//			btnListaTratamientos.setFocusPainted(false);
//			btnListaTratamientos.addActionListener(e -> abrirListaTratamientos());
//			btnListaTratamientos.setBounds(455, 170, 139, 25);
//			panelConsulta.add(btnListaTratamientos);
//		}
//
//		// --- OBSERVACIONES ---
//		JLabel lblObservaciones = new JLabel("Observaciones:");
//		lblObservaciones.setForeground(new Color(70, 130, 180));
//		lblObservaciones.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		lblObservaciones.setBounds(10, 230, 90, 14);
//		panelConsulta.add(lblObservaciones);
//
//		JScrollPane scrollObservaciones = new JScrollPane();
//		scrollObservaciones.setBorder(new LineBorder(new Color(173, 216, 230), 1));
//		scrollObservaciones.setBounds(100, 225, 494, 95);
//		panelConsulta.add(scrollObservaciones);
//
//		txtObservaciones = new JTextArea();
//		txtObservaciones.setLineWrap(true);
//		txtObservaciones.setWrapStyleWord(true);
//		txtObservaciones.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
//		txtObservaciones.setBackground(new Color(224, 247, 250));
//		scrollObservaciones.setViewportView(txtObservaciones);
//
//		// --- PANEL INFERIOR DE ACCIONES ---
//		JPanel buttonPane = new JPanel();
//		buttonPane.setBackground(new Color(240, 248, 255));
//		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
//		getContentPane().add(buttonPane, BorderLayout.SOUTH);
//
//		if (consultaEdicion == null) {
//			JButton btnRealizar = new JButton("Realizar Consulta");
//			btnRealizar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
//			btnRealizar.setBackground(new Color(176, 224, 230));
//			btnRealizar.setForeground(new Color(70, 130, 180));
//			btnRealizar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//			btnRealizar.setFocusPainted(false);
//			btnRealizar.addActionListener(e -> registrarConsulta());
//			btnRealizar.setActionCommand("OK");
//			buttonPane.add(btnRealizar);
//			getRootPane().setDefaultButton(btnRealizar);
//		} else {
//			JButton btnModificar = new JButton("Modificar");
//			btnModificar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
//			btnModificar.setBackground(new Color(176, 224, 230));
//			btnModificar.setForeground(new Color(70, 130, 180));
//			btnModificar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//			btnModificar.setFocusPainted(false);
//			btnModificar.addActionListener(e -> modificarConsulta());
//			buttonPane.add(btnModificar);
//
//			JButton btnEliminar = new JButton("Eliminar");
//			btnEliminar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
//			btnEliminar.setBackground(new Color(255, 182, 193));
//			btnEliminar.setForeground(new Color(178, 34, 34));
//			btnEliminar.setBorder(new LineBorder(new Color(240, 128, 128), 2));
//			btnEliminar.setFocusPainted(false);
//			btnEliminar.addActionListener(e -> eliminarConsulta());
//			buttonPane.add(btnEliminar);
//		}
//
//		JButton btnCancelar = new JButton("Cancelar");
//		btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
//		btnCancelar.setBackground(new Color(176, 224, 230));
//		btnCancelar.setForeground(new Color(70, 130, 180));
//		btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//		btnCancelar.setFocusPainted(false);
//		btnCancelar.addActionListener(e -> dispose());
//		btnCancelar.setActionCommand("Cancel");
//		buttonPane.add(btnCancelar);
//
//		if (consultaEdicion != null) {
//			cargarDatosConsultaEdicion();
//		} else {
//			cargarCitasFiltro("");
//		}
//	}
//
//	/// REGISTRAR
//	private void registrarConsulta() {
//		if (!verificarDatos()) {
//			return;
//		}
//
//		Consulta consulta = new Consulta(
//				txtObservaciones.getText(),
//				citaElegida
//		);
//
//		pacienteActual.getHistorialClinico().add(consulta);
//		Clinica.getInstancia().getConsultas().add(consulta);
//
//		registrarDiagnosticos(consulta, diagnosticosActuales);
//		registrarDA(consulta);
//		registrarDV(consulta);
//
//		controller.guardarConsulta(consulta);
//
//		JOptionPane.showMessageDialog(null, "Consulta realizada con éxito.", "Consulta Exitosa", JOptionPane.INFORMATION_MESSAGE);
//		dispose();
//	}
//
//	private void registrarDiagnosticos(Consulta consulta, ArrayList<Diagnostico> diagnosticos) {
//		if (diagnosticos != null) {
//			for (Diagnostico d : diagnosticos) {
//				consulta.addDiagnostico(d);
//				registrarEnfermedadPaciente(d);
//			}
//		}
//	}
//
//	private void registrarEnfermedadPaciente(Diagnostico d) {
//		Enfermedad enfermedadDiag = d.getEnfermedad();
//		if (enfermedadDiag != null && pacienteActual != null) {
//			pacienteActual.agregarEnfermedad(enfermedadDiag);
//		}
//	}
//
//	public void registrarDA(Consulta consulta) {
//		ArrayList<DetalleAnalisis> analisisDetalle = new ArrayList<>();
//		for (Analisis analisis : analisisIndicados) {
//			DetalleAnalisis da = new DetalleAnalisis(analisis, consulta);
//			analisisDetalle.add(da);
//		}
//		consulta.setAnalisis(analisisDetalle);
//	}
//
//	public void registrarDV(Consulta consulta) {
//		ArrayList<DetalleVacuna> vacunasDetalle = new ArrayList<>();
//		for (Vacuna vacuna : vacunasIndicadas) {
//			DetalleVacuna dv = new DetalleVacuna(consulta, vacuna);
//			vacunasDetalle.add(dv);
//		}
//		consulta.setVacunas(vacunasDetalle);
//	}
//
//	/// MODIFICAR (Utilizando ArrayLists puros en lugar de mapas)
//	private void modificarConsulta() {
//		consultaEdicion.setObservaciones(txtObservaciones.getText());
//
//		procesarElementosEliminados();
//		procesarNuevosElementos();
//		procesarElementosModificados();
//
//		controller.actualizarConsulta(consultaEdicion);
//		registrarDA(consultaEdicion);
//		registrarDV(consultaEdicion);
//
//		// Limpiar listas de control tras aplicar los cambios con éxito
//		diagnosticosNuevos.clear();
//		diagnosticosModificados.clear();
//		diagnosticosEliminados.clear();
//		vacunasNuevas.clear();
//		vacunasEliminadas.clear();
//		analisisNuevos.clear();
//		analisisEliminadas.clear();
//
//		JOptionPane.showMessageDialog(this, "Consulta modificada con éxito.", "Modificación Exitosa", JOptionPane.INFORMATION_MESSAGE);
//		dispose();
//	}
//
//	private void procesarElementosEliminados() {
//		for (Diagnostico d : diagnosticosEliminados) {
//			if (d.getIdNumber() > 0) {
//				DiagnosticoDAO.getInstance().eliminarDiagnostico(d.getIdNumber());
//			}
//			if (consultaEdicion.getDiagnosticos() != null) {
//				consultaEdicion.getDiagnosticos().remove(d);
//			}
//		}
//
//		for (DetalleVacuna dv : vacunasEliminadas) {
//			if (consultaEdicion.getVacunas() != null) {
//				consultaEdicion.getVacunas().remove(dv);
//			}
//		}
//
//		for (DetalleAnalisis da : analisisEliminadas) {
//			if (consultaEdicion.getAnalisis() != null) {
//				consultaEdicion.getAnalisis().remove(da);
//			}
//		}
//	}
//
//	private void procesarNuevosElementos() {
//		for (Diagnostico d : diagnosticosNuevos) {
//			d.setConsulta(consultaEdicion);
//			DiagnosticoDAO.getInstance().guardarDiagnostico(d);
//			registrarEnfermedadPaciente(d);
//			consultaEdicion.addDiagnostico(d);
//		}
//
//		for (DetalleVacuna dv : vacunasNuevas) {
//			consultaEdicion.getVacunas().add(dv);
//		}
//
//		for (DetalleAnalisis da : analisisNuevos) {
//			consultaEdicion.getAnalisis().add(da);
//		}
//	}
//
//	private void procesarElementosModificados() {
//		for (Diagnostico d : diagnosticosModificados) {
//			d.setConsulta(consultaEdicion);
//			DiagnosticoDAO.getInstance().actualizarDiagnostico(d);
//			registrarEnfermedadPaciente(d);
//		}
//	}
//
//	/// ELIMINAR
//	private void eliminarConsulta() {
//		int confirm = JOptionPane.showConfirmDialog(
//				this,
//				"¿Está seguro de que desea eliminar esta consulta?",
//				"Confirmar Eliminación",
//				JOptionPane.YES_NO_OPTION,
//				JOptionPane.WARNING_MESSAGE
//		);
//
//		if (confirm == JOptionPane.YES_OPTION) {
//			ConsultaDAO.getInstance().eliminarConsulta(consultaEdicion.getIdNumber());
//			Clinica.getInstancia().getConsultas().remove(consultaEdicion);
//
//			if (pacienteActual != null && pacienteActual.getHistorialClinico() != null) {
//				pacienteActual.getHistorialClinico().remove(consultaEdicion);
//			}
//
//			JOptionPane.showMessageDialog(this, "Consulta eliminada con éxito.", "Eliminación Exitosa", JOptionPane.INFORMATION_MESSAGE);
//			dispose();
//		}
//	}
//
//	private void abrirListaVacunas() {
//		if (consultaEdicion == null || consultaEdicion.getVacunas() == null || consultaEdicion.getVacunas().isEmpty()) {
//			JOptionPane.showMessageDialog(this, "No hay detalles de vacunas registrados en esta consulta.", "Vacunas de la Consulta", JOptionPane.INFORMATION_MESSAGE);
//			return;
//		}
//
//		ListarDetalleVacuna listarVacunas = new ListarDetalleVacuna(consultaEdicion);
//		listarVacunas.setModal(true);
//		listarVacunas.setLocationRelativeTo(this);
//		listarVacunas.setVisible(true);
//	}
//
//	private void abrirListaAnalisis() {
//		if (consultaEdicion == null || consultaEdicion.getAnalisis() == null || consultaEdicion.getAnalisis().isEmpty()) {
//			JOptionPane.showMessageDialog(this, "No hay detalles de análisis registrados en esta consulta.", "Análisis de la Consulta", JOptionPane.INFORMATION_MESSAGE);
//			return;
//		}
//
//		ListarDetalleAnalisis listarAnalisis = new ListarDetalleAnalisis(consultaEdicion);
//		listarAnalisis.setModal(true);
//		listarAnalisis.setLocationRelativeTo(this);
//		listarAnalisis.setVisible(true);
//	}
//
//	private void abrirListaTratamientos() {
//		List<Tratamiento> tratamientosTotales = obtenerTratamientosActuales();
//		if (tratamientosTotales.isEmpty()) {
//			JOptionPane.showMessageDialog(this, "No hay tratamientos registrados en los diagnósticos de esta consulta.", "Tratamientos de la Consulta", JOptionPane.INFORMATION_MESSAGE);
//			return;
//		}
//
//		StringBuilder sb = new StringBuilder();
//		for (Tratamiento t : tratamientosTotales) {
//			if (t != null) {
//				sb.append("• ").append(t.getNombre()).append("\n");
//			}
//		}
//		JOptionPane.showMessageDialog(this, sb.toString(), "Tratamientos de la Consulta", JOptionPane.INFORMATION_MESSAGE);
//	}
//
//	private List<Tratamiento> obtenerTratamientosActuales() {
//		List<Tratamiento> lista = new ArrayList<>();
//		for (Diagnostico d : diagnosticosActuales) {
//			if (d.getTratamientos() != null) {
//				for (Tratamiento t : d.getTratamientos()) {
//					if (t != null && !lista.contains(t)) {
//						lista.add(t);
//					}
//				}
//			}
//		}
//		return lista;
//	}
//
//	private void cargarDatosConsultaEdicion() {
//		if (consultaEdicion == null) return;
//
//		this.citaElegida = consultaEdicion.getCita();
//		if (citaElegida != null) {
//			this.pacienteActual = citaElegida.getPaciente();
//			if (this.pacienteActual == null) {
//				this.pacienteActual = Clinica.getInstancia().buscarPacienteXIdentificacion(citaElegida.getIdPersona());
//			}
//
//			if (this.pacienteActual != null) {
//				String idPaciente = pacienteActual.getCedula() != null ? pacienteActual.getCedula() : pacienteActual.getId();
//				txtPaciente.setText(pacienteActual.getNombre() + " " + (pacienteActual.getApellido() != null ? pacienteActual.getApellido() : "") + " - " + idPaciente);
//			} else {
//				txtPaciente.setText((citaElegida.getNombrePersona() != null ? citaElegida.getNombrePersona() : "") + " - " + (citaElegida.getIdPersona() != null ? citaElegida.getIdPersona() : ""));
//			}
//
//			Doctor doc = citaElegida.getDoctor() != null ? citaElegida.getDoctor() : consultaEdicion.getDoctor();
//			if (doc != null) {
//				txtDoctor.setText(doc.getNombre() + (doc.getApellido() != null ? " " + doc.getApellido() : ""));
//			} else {
//				txtDoctor.setText("N/A");
//			}
//
//			if (spinFechaFiltro != null && citaElegida.getFechaConsulta() != null) {
//				spinFechaFiltro.setValue(citaElegida.getFechaConsulta());
//			}
//
//			ActionListener[] listeners = cbxCita.getActionListeners();
//			for (ActionListener listener : listeners) {
//				cbxCita.removeActionListener(listener);
//			}
//
//			cbxCita.removeAllItems();
//			cbxCita.addItem(citaElegida.getId() + " - " + citaElegida.getNombrePersona());
//			cbxCita.setSelectedIndex(0);
//
//			for (ActionListener listener : listeners) {
//				cbxCita.addActionListener(listener);
//			}
//
//		} else if (consultaEdicion.getDoctor() != null) {
//			Doctor doc = consultaEdicion.getDoctor();
//			txtDoctor.setText(doc.getNombre() + (doc.getApellido() != null ? " " + doc.getApellido() : ""));
//		}
//
//		if (consultaEdicion.getDiagnosticos() != null) {
//			diagnosticosActuales = new ArrayList<>(consultaEdicion.getDiagnosticos());
//		}
//
//		if (consultaEdicion.getVacunas() != null) {
//			vacunasIndicadas.clear();
//			vacunasOriginalesEdicion = new ArrayList<>(consultaEdicion.getVacunas());
//			for (DetalleVacuna dv : consultaEdicion.getVacunas()) {
//				if (dv != null && dv.getVacuna() != null) {
//					vacunasIndicadas.add(dv.getVacuna());
//				}
//			}
//		}
//
//		if (consultaEdicion.getAnalisis() != null) {
//			analisisIndicados.clear();
//			analisisOriginalesEdicion = new ArrayList<>(consultaEdicion.getAnalisis());
//			for (DetalleAnalisis da : consultaEdicion.getAnalisis()) {
//				if (da != null && da.getAnalisis() != null) {
//					analisisIndicados.add(da.getAnalisis());
//				}
//			}
//		}
//
//		txtObservaciones.setText(consultaEdicion.getObservaciones() != null ? consultaEdicion.getObservaciones() : "");
//
//		txtBuscarCita.setEnabled(false);
//		if (spinFechaFiltro != null) {
//			spinFechaFiltro.setEnabled(false);
//		}
//		cbxCita.setEnabled(false);
//
//		actualizarTextoDiagnosticos();
//		actualizarTextoVacunas();
//		actualizarTextoAnalisis();
//		actualizarTextoTratamientos();
//	}
//
//	private Date obtenerFechaCitaExistente() {
//		if (Clinica.getInstancia().getCitas() != null) {
//			for (Cita c : Clinica.getInstancia().getCitas()) {
//				if (c.getEstado() == EstadoCita.PROGRAMADA && c.getFechaConsulta() != null) {
//					return c.getFechaConsulta();
//				}
//			}
//		}
//		return new Date();
//	}
//
//	public boolean verificarDatos() {
//		if (cbxCita.getSelectedIndex() <= 0) {
//			JOptionPane.showMessageDialog(null, "Debe seleccionar una cita.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
//			return false;
//		}
//
//		if (pacienteActual == null) {
//			JOptionPane.showMessageDialog(null, "El paciente debe estar registrado en el sistema antes de realizar la consulta.", "Atención", JOptionPane.WARNING_MESSAGE);
//			return false;
//		}
//
//		if (diagnosticosActuales.isEmpty()) {
//			JOptionPane.showMessageDialog(null, "Debe agregar al menos un diagnóstico.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
//			return false;
//		}
//
//		return true;
//	}
//
//	private void cargarCitasFiltro(String filtro) {
//		ActionListener[] listeners = cbxCita.getActionListeners();
//		for (ActionListener listener : listeners) {
//			cbxCita.removeActionListener(listener);
//		}
//
//		cbxCita.removeAllItems();
//		cbxCita.addItem("<<Seleccione>>");
//
//		String f = filtro.toLowerCase();
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		String fechaFiltroStr = (spinFechaFiltro != null && spinFechaFiltro.isVisible() && spinFechaFiltro.getValue() != null) ?
//				sdf.format((Date) spinFechaFiltro.getValue()) : "";
//
//		if (Clinica.getInstancia().getCitas() != null) {
//			for (Cita cita : Clinica.getInstancia().getCitas()) {
//				if (cita.getEstado() == EstadoCita.PROGRAMADA && cita.getFechaConsulta() != null) {
//					String fechaCitaStr = sdf.format(cita.getFechaConsulta());
//
//					if (!fechaFiltroStr.isEmpty() && !fechaCitaStr.equals(fechaFiltroStr)) {
//						continue;
//					}
//
//					String idCita = cita.getId() != null ? cita.getId().toLowerCase() : "";
//					String nombrePers = cita.getNombrePersona() != null ? cita.getNombrePersona().toLowerCase() : "";
//					String idPers = cita.getIdPersona() != null ? cita.getIdPersona().toLowerCase() : "";
//
//					if (f.isEmpty() || idCita.contains(f) || nombrePers.contains(f) || idPers.contains(f)) {
//						String fechaStr = utilidad.Formato.getDateString(cita.getFechaConsulta());
//						String item = cita.getId() + " - " + cita.getNombrePersona() + " (" + cita.getIdPersona() + ")" + " - (" + fechaStr + ")";
//						cbxCita.addItem(item);
//					}
//				}
//			}
//		}
//
//		for (ActionListener listener : listeners) {
//			cbxCita.addActionListener(listener);
//		}
//
//		cargarDatosCita();
//	}
//
//	private void cargarDatosCita() {
//		if (cbxCita.getSelectedIndex() > 0) {
//			String codigo = cbxCita.getSelectedItem().toString().split(" ")[0];
//			citaElegida = Clinica.getInstancia().buscarCitaXId(codigo);
//
//			if (citaElegida != null) {
//				pacienteActual = citaElegida.getPaciente();
//				if (pacienteActual == null) {
//					pacienteActual = Clinica.getInstancia().buscarPacienteXIdentificacion(citaElegida.getIdPersona());
//				}
//
//				if (pacienteActual != null) {
//					String idPaciente = pacienteActual.getCedula() != null ? pacienteActual.getCedula() : pacienteActual.getId();
//					txtPaciente.setText(pacienteActual.getNombre() + " " + (pacienteActual.getApellido() != null ? pacienteActual.getApellido() : "") + " - " + idPaciente);
//				} else {
//					txtPaciente.setText(citaElegida.getNombrePersona() + " - " + citaElegida.getIdPersona());
//				}
//
//				Doctor doc = citaElegida.getDoctor();
//				if (doc != null) {
//					txtDoctor.setText(doc.getNombre() + (doc.getApellido() != null ? " " + doc.getApellido() : ""));
//				} else {
//					txtDoctor.setText("N/A");
//				}
//			}
//		} else {
//			limpiarCampos();
//			pacienteActual = null;
//		}
//	}
//
//	private void abrirCrearDiagnostico() {
//		if (pacienteActual == null) {
//			JOptionPane.showMessageDialog(this, "Debe seleccionar una cita con un paciente asignado primero.", "Atención", JOptionPane.WARNING_MESSAGE);
//			return;
//		}
//		CrearDiagnostico dialogo = new CrearDiagnostico(diagnosticosActuales, pacienteActual);
//		dialogo.setModal(true);
//		dialogo.setLocationRelativeTo(this);
//		dialogo.setVisible(true);
//
//		registrarDiagnostico(dialogo);
//	}
//
//	private void registrarDiagnostico(CrearDiagnostico dialogo){
//		Diagnostico diag = dialogo.getDiagnosticoCreado();
//
//		if (diag != null) {
//			diagnosticosActuales.add(diag);
//			if (consultaEdicion != null) {
//				diagnosticosNuevos.add(diag);
//			}
//			actualizarTextoDiagnosticos();
//			actualizarTextoTratamientos();
//		}
//	}
//
//	private void manejarEliminacionDiagnostico(Diagnostico dEliminado) {
//		if (consultaEdicion != null) {
//			if (diagnosticosNuevos.contains(dEliminado)) {
//				diagnosticosNuevos.remove(dEliminado);
//			} else {
//				diagnosticosModificados.remove(dEliminado);
//				if (dEliminado.getIdNumber() > 0) {
//					if (!diagnosticosEliminados.contains(dEliminado)) {
//						diagnosticosEliminados.add(dEliminado);
//					}
//					if (consultaEdicion.getDiagnosticos() != null) {
//						consultaEdicion.getDiagnosticos().remove(dEliminado);
//					}
//				}
//			}
//		}
//	}
//
//	private void actualizarTextoDiagnosticos() {
//		if (diagnosticosActuales.isEmpty()) {
//			txtDiagnostico.setText("");
//		} else {
//			StringBuilder sb = new StringBuilder();
//			for (Diagnostico d : diagnosticosActuales) {
//				String enf = (d.getEnfermedad() != null) ? d.getEnfermedad().getNombre() : "Sin Enfermedad";
//				sb.append("• ").append(enf).append("\n");
//			}
//			txtDiagnostico.setText(sb.toString().trim());
//			txtDiagnostico.setCaretPosition(0);
//		}
//	}
//
//	private void mostrarDiagnosticos() {
//		if (diagnosticosActuales.isEmpty()) {
//			JOptionPane.showMessageDialog(this, "No hay diagnósticos creados en esta consulta.", "Diagnósticos de la Consulta", JOptionPane.INFORMATION_MESSAGE);
//			return;
//		}
//
//		if (diagnosticosActuales.size() == 1) {
//			Diagnostico diagAntes = diagnosticosActuales.get(0);
//			CrearDiagnostico dialogo = new CrearDiagnostico(diagAntes, diagnosticosActuales);
//			dialogo.setModal(true);
//			dialogo.setLocationRelativeTo(this);
//			dialogo.setVisible(true);
//
//			if (dialogo.eliminadoIndex() > -1) {
//				Diagnostico dEliminado = diagnosticosActuales.remove(0);
//				manejarEliminacionDiagnostico(dEliminado);
//			} else if (consultaEdicion != null) {
//				Diagnostico dModificado = diagnosticosActuales.get(0);
//				if (!diagnosticosNuevos.contains(dModificado) && !diagnosticosModificados.contains(dModificado)) {
//					diagnosticosModificados.add(dModificado);
//				}
//			}
//		} else {
//			String[] opciones = new String[diagnosticosActuales.size()];
//			for (int i = 0; i < diagnosticosActuales.size(); i++) {
//				Diagnostico d = diagnosticosActuales.get(i);
//				String enf = (d.getEnfermedad() != null) ? d.getEnfermedad().getNombre() : "Sin Enfermedad";
//				opciones[i] = (i + 1) + ". " + enf;
//			}
//
//			String seleccion = (String) JOptionPane.showInputDialog(
//					this,
//					"Seleccione el diagnóstico que desea ver o editar:",
//					"Ver Diagnósticos",
//					JOptionPane.QUESTION_MESSAGE,
//					null,
//					opciones,
//					opciones[0]
//			);
//
//			if (seleccion != null) {
//				for (int i = 0; i < diagnosticosActuales.size(); i++) {
//					if (seleccion.startsWith((i + 1) + ".")) {
//						Diagnostico diagAntes = diagnosticosActuales.get(i);
//						CrearDiagnostico dialogo = new CrearDiagnostico(diagAntes, diagnosticosActuales);
//						dialogo.setModal(true);
//						dialogo.setLocationRelativeTo(this);
//						dialogo.setVisible(true);
//
//						if (dialogo.eliminadoIndex() > -1) {
//							Diagnostico dEliminado = diagnosticosActuales.remove(i);
//							manejarEliminacionDiagnostico(dEliminado);
//						} else if (consultaEdicion != null) {
//							Diagnostico dModificado = diagnosticosActuales.get(i);
//							if (!diagnosticosNuevos.contains(dModificado) && !diagnosticosModificados.contains(dModificado)) {
//								diagnosticosModificados.add(dModificado);
//							}
//						}
//						break;
//					}
//				}
//			}
//		}
//
//		actualizarTextoDiagnosticos();
//		actualizarTextoTratamientos();
//	}
//
//	private void abrirAplicarVacunas() {
//		if (pacienteActual == null) {
//			JOptionPane.showMessageDialog(this, "Debe seleccionar una cita con un paciente asignado primero.", "Atención", JOptionPane.WARNING_MESSAGE);
//			return;
//		}
//
//		List<Vacuna> disponibles = Clinica.getInstancia().getVacunas();
//		if (disponibles == null || disponibles.isEmpty()) {
//			JOptionPane.showMessageDialog(this, "No hay vacunas registradas en la clínica.", "Información", JOptionPane.INFORMATION_MESSAGE);
//			return;
//		}
//
//		JDialog dialogVacunas = new JDialog(this, "Seleccionar Vacunas a Aplicar", true);
//		dialogVacunas.setSize(640, 520);
//		dialogVacunas.setLocationRelativeTo(this);
//		dialogVacunas.setLayout(new BorderLayout());
//
//		JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
//		panelContenido.setBackground(new Color(240, 248, 255));
//		panelContenido.setBorder(new EmptyBorder(12, 12, 12, 12));
//
//		JPanel panelFiltro = new JPanel(new BorderLayout(8, 8));
//		panelFiltro.setBackground(new Color(240, 248, 255));
//
//		JLabel lblBuscar = new JLabel("Buscar por Nombre:");
//		lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		lblBuscar.setForeground(new Color(70, 130, 180));
//
//		JTextField txtBuscar = new JTextField();
//		txtBuscar.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
//		txtBuscar.setPreferredSize(new Dimension(0, 30));
//		txtBuscar.setBorder(new CompoundBorder(
//				new LineBorder(new Color(135, 206, 235), 1),
//				new EmptyBorder(4, 8, 4, 8)
//		));
//
//		panelFiltro.add(lblBuscar, BorderLayout.WEST);
//		panelFiltro.add(txtBuscar, BorderLayout.CENTER);
//		panelContenido.add(panelFiltro, BorderLayout.NORTH);
//
//		JPanel panelGrid = new JPanel(new GridLayout(0, 2, 10, 10));
//		panelGrid.setBackground(new Color(240, 248, 255));
//
//		JPanel panelNorte = new JPanel(new BorderLayout());
//		panelNorte.setBackground(new Color(240, 248, 255));
//		panelNorte.add(panelGrid, BorderLayout.NORTH);
//
//		JScrollPane scroll = new JScrollPane(panelNorte);
//		scroll.setBorder(null);
//		scroll.getVerticalScrollBar().setUnitIncrement(16);
//		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		panelContenido.add(scroll, BorderLayout.CENTER);
//
//		dialogVacunas.add(panelContenido, BorderLayout.CENTER);
//
//		List<Vacuna> seleccionadasTemp = new ArrayList<>(vacunasIndicadas);
//
//		Runnable cargarTarjetas = () -> {
//			panelGrid.removeAll();
//			String filtro = txtBuscar.getText().trim().toLowerCase();
//			boolean algunResultado = false;
//
//			for (Vacuna v : disponibles) {
//				if (filtro.isEmpty() || (v.getNombre() != null && v.getNombre().toLowerCase().contains(filtro))) {
//					algunResultado = true;
//
//					JPanel tarjeta = new JPanel(new BorderLayout(8, 0));
//					tarjeta.setBackground(Color.WHITE);
//					tarjeta.setBorder(new CompoundBorder(
//							new LineBorder(new Color(173, 216, 230), 1, true),
//							new EmptyBorder(8, 8, 8, 8)
//					));
//
//					JCheckBox chk = new JCheckBox();
//					chk.setBackground(Color.WHITE);
//					chk.setSelected(seleccionadasTemp.contains(v));
//					chk.addActionListener(ev -> {
//						if (chk.isSelected()) {
//							if (!seleccionadasTemp.contains(v)) {
//								seleccionadasTemp.add(v);
//							}
//						} else {
//							seleccionadasTemp.remove(v);
//						}
//					});
//					tarjeta.add(chk, BorderLayout.WEST);
//
//					JPanel panelInfo = new JPanel();
//					panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
//					panelInfo.setBackground(Color.WHITE);
//
//					JLabel lblNombre = new JLabel(v.getNombre());
//					lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 13));
//					lblNombre.setForeground(new Color(70, 130, 180));
//
//					JLabel lblFabricante = new JLabel("Fabricante: " + (v.getFabricante() != null ? v.getFabricante() : "N/A"));
//					lblFabricante.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
//					lblFabricante.setForeground(Color.GRAY);
//
//					JLabel lblDosis = new JLabel("Dosis: " + v.getCantDosis());
//					lblDosis.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
//					lblDosis.setForeground(Color.GRAY);
//
//					panelInfo.add(lblNombre);
//					panelInfo.add(Box.createVerticalStrut(2));
//					panelInfo.add(lblFabricante);
//					panelInfo.add(Box.createVerticalStrut(2));
//					panelInfo.add(lblDosis);
//
//					tarjeta.add(panelInfo, BorderLayout.CENTER);
//					panelGrid.add(tarjeta);
//				}
//			}
//
//			if (!algunResultado) {
//				JLabel lblVacio = new JLabel("No se encontraron coincidencias.", SwingConstants.CENTER);
//				lblVacio.setFont(new Font("Bahnschrift", Font.ITALIC, 12));
//				lblVacio.setForeground(Color.GRAY);
//				panelGrid.add(lblVacio);
//			}
//
//			panelGrid.revalidate();
//			panelGrid.repaint();
//			scroll.revalidate();
//			scroll.repaint();
//		};
//
//		txtBuscar.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				cargarTarjetas.run();
//			}
//		});
//
//		cargarTarjetas.run();
//
//		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		panelBotones.setBackground(new Color(240, 248, 255));
//
//		JButton btnAceptar = new JButton("Guardar Selección");
//		btnAceptar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		btnAceptar.setBackground(new Color(176, 224, 230));
//		btnAceptar.setForeground(new Color(70, 130, 180));
//		btnAceptar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//		btnAceptar.setFocusPainted(false);
//		btnAceptar.setPreferredSize(new Dimension(140, 30));
//		btnAceptar.addActionListener(e -> {
//			if (consultaEdicion != null) {
//				for (DetalleVacuna dvOriginal : vacunasOriginalesEdicion) {
//					if (!seleccionadasTemp.contains(dvOriginal.getVacuna())) {
//						if (!vacunasEliminadas.contains(dvOriginal)) {
//							vacunasEliminadas.add(dvOriginal);
//						}
//					}
//				}
//				for (Vacuna v : seleccionadasTemp) {
//					boolean encontrada = false;
//					for (DetalleVacuna dvOriginal : vacunasOriginalesEdicion) {
//						if (dvOriginal.getVacuna().equals(v)) {
//							encontrada = true;
//							break;
//						}
//					}
//					if (!encontrada) {
//						boolean yaNueva = false;
//						for (DetalleVacuna dvNueva : vacunasNuevas) {
//							if (dvNueva.getVacuna().equals(v)) {
//								yaNueva = true;
//								break;
//							}
//						}
//						if (!yaNueva) {
//							DetalleVacuna nuevoDv = new DetalleVacuna(consultaEdicion, v);
//							vacunasNuevas.add(nuevoDv);
//						}
//					}
//				}
//				for (Vacuna v : seleccionadasTemp) {
//					vacunasEliminadas.removeIf(dv -> dv.getVacuna().equals(v));
//				}
//			}
//
//			vacunasIndicadas.clear();
//			vacunasIndicadas.addAll(seleccionadasTemp);
//			actualizarTextoVacunas();
//			dialogVacunas.dispose();
//		});
//
//		JButton btnCancelar = new JButton("Cancelar");
//		btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		btnCancelar.setBackground(new Color(176, 224, 230));
//		btnCancelar.setForeground(new Color(70, 130, 180));
//		btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//		btnCancelar.setFocusPainted(false);
//		btnCancelar.setPreferredSize(new Dimension(90, 30));
//		btnCancelar.addActionListener(e -> dialogVacunas.dispose());
//
//		panelBotones.add(btnAceptar);
//		panelBotones.add(btnCancelar);
//		dialogVacunas.add(panelBotones, BorderLayout.SOUTH);
//
//		dialogVacunas.setVisible(true);
//	}
//
//	private void abrirIndicarAnalisis() {
//		if (pacienteActual == null) {
//			JOptionPane.showMessageDialog(this, "Debe seleccionar una cita con un paciente asignado primero.", "Atención", JOptionPane.WARNING_MESSAGE);
//			return;
//		}
//
//		List<Analisis> disponibles = Clinica.getInstancia().getAnalisis();
//		if (disponibles == null || disponibles.isEmpty()) {
//			JOptionPane.showMessageDialog(this, "No hay análisis clínicos registrados en la clínica.", "Información", JOptionPane.INFORMATION_MESSAGE);
//			return;
//		}
//
//		JDialog dialogAnalisis = new JDialog(this, "Seleccionar Análisis a Indicar", true);
//		dialogAnalisis.setSize(640, 520);
//		dialogAnalisis.setLocationRelativeTo(this);
//		dialogAnalisis.setLayout(new BorderLayout());
//
//		JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
//		panelContenido.setBackground(new Color(240, 248, 255));
//		panelContenido.setBorder(new EmptyBorder(12, 12, 12, 12));
//
//		JPanel panelFiltro = new JPanel(new BorderLayout(8, 8));
//		panelFiltro.setBackground(new Color(240, 248, 255));
//
//		JLabel lblBuscar = new JLabel("Buscar por Nombre:");
//		lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		lblBuscar.setForeground(new Color(70, 130, 180));
//
//		JTextField txtBuscar = new JTextField();
//		txtBuscar.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
//		txtBuscar.setPreferredSize(new Dimension(0, 30));
//		txtBuscar.setBorder(new CompoundBorder(
//				new LineBorder(new Color(135, 206, 235), 1),
//				new EmptyBorder(4, 8, 4, 8)
//		));
//
//		panelFiltro.add(lblBuscar, BorderLayout.WEST);
//		panelFiltro.add(txtBuscar, BorderLayout.CENTER);
//		panelContenido.add(panelFiltro, BorderLayout.NORTH);
//
//		JPanel panelGrid = new JPanel(new GridLayout(0, 2, 10, 10));
//		panelGrid.setBackground(new Color(240, 248, 255));
//
//		JPanel panelNorte = new JPanel(new BorderLayout());
//		panelNorte.setBackground(new Color(240, 248, 255));
//		panelNorte.add(panelGrid, BorderLayout.NORTH);
//
//		JScrollPane scroll = new JScrollPane(panelNorte);
//		scroll.setBorder(null);
//		scroll.getVerticalScrollBar().setUnitIncrement(16);
//		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		panelContenido.add(scroll, BorderLayout.CENTER);
//
//		dialogAnalisis.add(panelContenido, BorderLayout.CENTER);
//
//		List<Analisis> seleccionadosTemp = new ArrayList<>(analisisIndicados);
//
//		Runnable cargarTarjetas = () -> {
//			panelGrid.removeAll();
//			String filtro = txtBuscar.getText().trim().toLowerCase();
//			boolean algunResultado = false;
//
//			for (Analisis a : disponibles) {
//				if (filtro.isEmpty() || (a.getNombre() != null && a.getNombre().toLowerCase().contains(filtro))) {
//					algunResultado = true;
//
//					JPanel tarjeta = new JPanel(new BorderLayout(8, 0));
//					tarjeta.setBackground(Color.WHITE);
//					tarjeta.setBorder(new CompoundBorder(
//							new LineBorder(new Color(173, 216, 230), 1, true),
//							new EmptyBorder(8, 8, 8, 8)
//					));
//
//					JCheckBox chk = new JCheckBox();
//					chk.setBackground(Color.WHITE);
//					chk.setSelected(seleccionadosTemp.contains(a));
//					chk.addActionListener(ev -> {
//						if (chk.isSelected()) {
//							if (!seleccionadosTemp.contains(a)) {
//								seleccionadosTemp.add(a);
//							}
//						} else {
//							seleccionadosTemp.remove(a);
//						}
//					});
//					tarjeta.add(chk, BorderLayout.WEST);
//
//					JPanel panelInfo = new JPanel();
//					panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
//					panelInfo.setBackground(Color.WHITE);
//
//					JLabel lblNombre = new JLabel(a.getNombre());
//					lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 13));
//					lblNombre.setForeground(new Color(70, 130, 180));
//
//					JLabel lblTipo = new JLabel("Tipo: " + (a.getTipo() != null ? a.getTipo() : "N/A"));
//					lblTipo.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
//					lblTipo.setForeground(Color.GRAY);
//
//					JLabel lblUnidad = new JLabel("Rango: " + a.getValorMin() + " - " + a.getValorMax() + " " + (a.getUnidadMedida() != null ? a.getUnidadMedida() : ""));
//					lblUnidad.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
//					lblUnidad.setForeground(Color.GRAY);
//
//					panelInfo.add(lblNombre);
//					panelInfo.add(Box.createVerticalStrut(2));
//					panelInfo.add(lblTipo);
//					panelInfo.add(Box.createVerticalStrut(2));
//					panelInfo.add(lblUnidad);
//
//					JButton btnDetalles = new JButton("Ver Detalles");
//					btnDetalles.setFont(new Font("Bahnschrift", Font.BOLD, 10));
//					btnDetalles.setBackground(new Color(224, 247, 250));
//					btnDetalles.setForeground(new Color(70, 130, 180));
//					btnDetalles.setBorder(new LineBorder(new Color(135, 206, 235), 1));
//					btnDetalles.setFocusPainted(false);
//					btnDetalles.addActionListener(ev -> {
//						String detalle = "Análisis: " + a.getNombre() + "\n"
//								+ "Tipo: " + a.getTipo() + "\n"
//								+ "Unidad: " + a.getUnidadMedida() + "\n\n"
//								+ "Valores de Referencia:\n"
//								+ "• Mínimo: " + a.getValorMin() + " " + a.getUnidadMedida() + "\n"
//								+ "• Promedio: " + a.getValorProm() + " " + a.getUnidadMedida() + "\n"
//								+ "• Máximo: " + a.getValorMax() + " " + a.getUnidadMedida();
//						JOptionPane.showMessageDialog(dialogAnalisis, detalle, "Detalle del Análisis", JOptionPane.INFORMATION_MESSAGE);
//					});
//
//					tarjeta.add(panelInfo, BorderLayout.CENTER);
//					tarjeta.add(btnDetalles, BorderLayout.EAST);
//					panelGrid.add(tarjeta);
//				}
//			}
//
//			if (!algunResultado) {
//				JLabel lblVacio = new JLabel("No se encontraron coincidencias.", SwingConstants.CENTER);
//				lblVacio.setFont(new Font("Bahnschrift", Font.ITALIC, 12));
//				lblVacio.setForeground(Color.GRAY);
//				panelGrid.add(lblVacio);
//			}
//
//			panelGrid.revalidate();
//			panelGrid.repaint();
//			scroll.revalidate();
//			scroll.repaint();
//		};
//
//		txtBuscar.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				cargarTarjetas.run();
//			}
//		});
//
//		cargarTarjetas.run();
//
//		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		panelBotones.setBackground(new Color(240, 248, 255));
//
//		JButton btnAceptar = new JButton("Guardar Selección");
//		btnAceptar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		btnAceptar.setBackground(new Color(176, 224, 230));
//		btnAceptar.setForeground(new Color(70, 130, 180));
//		btnAceptar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//		btnAceptar.setFocusPainted(false);
//		btnAceptar.setPreferredSize(new Dimension(140, 30));
//		btnAceptar.addActionListener(e -> {
//			if (consultaEdicion != null) {
//				for (DetalleAnalisis daOriginal : analisisOriginalesEdicion) {
//					if (!seleccionadosTemp.contains(daOriginal.getAnalisis())) {
//						if (!analisisEliminadas.contains(daOriginal)) {
//							analisisEliminadas.add(daOriginal);
//						}
//					}
//				}
//				for (Analisis a : seleccionadosTemp) {
//					boolean encontrada = false;
//					for (DetalleAnalisis daOriginal : analisisOriginalesEdicion) {
//						if (daOriginal.getAnalisis().equals(a)) {
//							encontrada = true;
//							break;
//						}
//					}
//					if (!encontrada) {
//						boolean yaNueva = false;
//						for (DetalleAnalisis daNueva : analisisNuevos) {
//							if (daNueva.getAnalisis().equals(a)) {
//								yaNueva = true;
//								break;
//							}
//						}
//						if (!yaNueva) {
//							DetalleAnalisis nuevoDa = new DetalleAnalisis(a, consultaEdicion);
//							analisisNuevos.add(nuevoDa);
//						}
//					}
//				}
//				for (Analisis a : seleccionadosTemp) {
//					analisisEliminadas.removeIf(da -> da.getAnalisis().equals(a));
//				}
//			}
//
//			analisisIndicados.clear();
//			analisisIndicados.addAll(seleccionadosTemp);
//			actualizarTextoAnalisis();
//			dialogAnalisis.dispose();
//		});
//
//		JButton btnCancelar = new JButton("Cancelar");
//		btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		btnCancelar.setBackground(new Color(176, 224, 230));
//		btnCancelar.setForeground(new Color(70, 130, 180));
//		btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//		btnCancelar.setFocusPainted(false);
//		btnCancelar.setPreferredSize(new Dimension(90, 30));
//		btnCancelar.addActionListener(e -> dialogAnalisis.dispose());
//
//		panelBotones.add(btnAceptar);
//		panelBotones.add(btnCancelar);
//		dialogAnalisis.add(panelBotones, BorderLayout.SOUTH);
//
//		dialogAnalisis.setVisible(true);
//	}
//
//	private void abrirIndicarTratamientos() {
//		if (diagnosticosActuales.isEmpty()) {
//			JOptionPane.showMessageDialog(this, "Debe agregar al menos un diagnóstico antes de indicar tratamientos.", "Atención", JOptionPane.WARNING_MESSAGE);
//			return;
//		}
//
//		List<Tratamiento> disponibles = Clinica.getInstancia().getTratamientos();
//		if (disponibles == null || disponibles.isEmpty()) {
//			JOptionPane.showMessageDialog(this, "No hay tratamientos registrados en la clínica.", "Información", JOptionPane.INFORMATION_MESSAGE);
//			return;
//		}
//
//		Diagnostico diagnosticoSeleccionado = seleccionarDiagnosticoParaTratamiento();
//		if (diagnosticoSeleccionado == null) {
//			return;
//		}
//
//		JDialog dialogTratamientos = new JDialog(this, "Seleccionar Tratamientos para el Diagnóstico", true);
//		dialogTratamientos.setSize(640, 520);
//		dialogTratamientos.setLocationRelativeTo(this);
//		dialogTratamientos.setLayout(new BorderLayout());
//
//		JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
//		panelContenido.setBackground(new Color(240, 248, 255));
//		panelContenido.setBorder(new EmptyBorder(12, 12, 12, 12));
//
//		JPanel panelFiltro = new JPanel(new BorderLayout(8, 8));
//		panelFiltro.setBackground(new Color(240, 248, 255));
//
//		JLabel lblBuscar = new JLabel("Buscar por Nombre:");
//		lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		lblBuscar.setForeground(new Color(70, 130, 180));
//
//		JTextField txtBuscar = new JTextField();
//		txtBuscar.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
//		txtBuscar.setPreferredSize(new Dimension(0, 30));
//		txtBuscar.setBorder(new CompoundBorder(
//				new LineBorder(new Color(135, 206, 235), 1),
//				new EmptyBorder(4, 8, 4, 8)
//		));
//
//		panelFiltro.add(lblBuscar, BorderLayout.WEST);
//		panelFiltro.add(txtBuscar, BorderLayout.CENTER);
//		panelContenido.add(panelFiltro, BorderLayout.NORTH);
//
//		JPanel panelGrid = new JPanel(new GridLayout(0, 2, 10, 10));
//		panelGrid.setBackground(new Color(240, 248, 255));
//
//		JPanel panelNorte = new JPanel(new BorderLayout());
//		panelNorte.setBackground(new Color(240, 248, 255));
//		panelNorte.add(panelGrid, BorderLayout.NORTH);
//
//		JScrollPane scroll = new JScrollPane(panelNorte);
//		scroll.setBorder(null);
//		scroll.getVerticalScrollBar().setUnitIncrement(16);
//		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		panelContenido.add(scroll, BorderLayout.CENTER);
//
//		dialogTratamientos.add(panelContenido, BorderLayout.CENTER);
//
//		if (diagnosticoSeleccionado.getTratamientos() == null) {
//			diagnosticoSeleccionado.setTratamientos(new ArrayList<>());
//		}
//		List<Tratamiento> seleccionadosTemp = new ArrayList<>(diagnosticoSeleccionado.getTratamientos());
//
//		Runnable cargarTarjetas = () -> {
//			panelGrid.removeAll();
//			String filtro = txtBuscar.getText().trim().toLowerCase();
//			boolean algunResultado = false;
//
//			for (Tratamiento t : disponibles) {
//				if (filtro.isEmpty() || (t.getNombre() != null && t.getNombre().toLowerCase().contains(filtro))) {
//					algunResultado = true;
//
//					JPanel tarjeta = new JPanel(new BorderLayout(8, 0));
//					tarjeta.setBackground(Color.WHITE);
//					tarjeta.setBorder(new CompoundBorder(
//							new LineBorder(new Color(173, 216, 230), 1, true),
//							new EmptyBorder(8, 8, 8, 8)
//					));
//
//					JCheckBox chk = new JCheckBox();
//					chk.setBackground(Color.WHITE);
//					chk.setSelected(seleccionadosTemp.contains(t));
//					chk.addActionListener(ev -> {
//						if (chk.isSelected()) {
//							if (!seleccionadosTemp.contains(t)) {
//								seleccionadosTemp.add(t);
//							}
//						} else {
//							seleccionadosTemp.remove(t);
//						}
//					});
//					tarjeta.add(chk, BorderLayout.WEST);
//
//					JPanel panelInfo = new JPanel();
//					panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
//					panelInfo.setBackground(Color.WHITE);
//
//					JLabel lblNombre = new JLabel(t.getNombre());
//					lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 13));
//					lblNombre.setForeground(new Color(70, 130, 180));
//
//					panelInfo.add(lblNombre);
//
//					tarjeta.add(panelInfo, BorderLayout.CENTER);
//					panelGrid.add(tarjeta);
//				}
//			}
//
//			if (!algunResultado) {
//				JLabel lblVacio = new JLabel("No se encontraron coincidencias.", SwingConstants.CENTER);
//				lblVacio.setFont(new Font("Bahnschrift", Font.ITALIC, 12));
//				lblVacio.setForeground(Color.GRAY);
//				panelGrid.add(lblVacio);
//			}
//
//			panelGrid.revalidate();
//			panelGrid.repaint();
//			scroll.revalidate();
//			scroll.repaint();
//		};
//
//		txtBuscar.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				cargarTarjetas.run();
//			}
//		});
//
//		cargarTarjetas.run();
//
//		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		panelBotones.setBackground(new Color(240, 248, 255));
//
//		JButton btnAceptar = new JButton("Guardar Selección");
//		btnAceptar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		btnAceptar.setBackground(new Color(176, 224, 230));
//		btnAceptar.setForeground(new Color(70, 130, 180));
//		btnAceptar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//		btnAceptar.setFocusPainted(false);
//		btnAceptar.setPreferredSize(new Dimension(140, 30));
//		btnAceptar.addActionListener(e -> {
//			diagnosticoSeleccionado.setTratamientos(new ArrayList<>(seleccionadosTemp));
//			if (consultaEdicion != null && !diagnosticosNuevos.contains(diagnosticoSeleccionado) && !diagnosticosModificados.contains(diagnosticoSeleccionado)) {
//				diagnosticosModificados.add(diagnosticoSeleccionado);
//			}
//			actualizarTextoTratamientos();
//			dialogTratamientos.dispose();
//		});
//
//		JButton btnCancelar = new JButton("Cancelar");
//		btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
//		btnCancelar.setBackground(new Color(176, 224, 230));
//		btnCancelar.setForeground(new Color(70, 130, 180));
//		btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
//		btnCancelar.setFocusPainted(false);
//		btnCancelar.setPreferredSize(new Dimension(90, 30));
//		btnCancelar.addActionListener(e -> dialogTratamientos.dispose());
//
//		panelBotones.add(btnAceptar);
//		panelBotones.add(btnCancelar);
//		dialogTratamientos.add(panelBotones, BorderLayout.SOUTH);
//
//		dialogTratamientos.setVisible(true);
//	}
//
//	private Diagnostico seleccionarDiagnosticoParaTratamiento() {
//		if (diagnosticosActuales.size() == 1) {
//			return diagnosticosActuales.get(0);
//		}
//
//		String[] opciones = new String[diagnosticosActuales.size()];
//		for (int i = 0; i < diagnosticosActuales.size(); i++) {
//			Diagnostico d = diagnosticosActuales.get(i);
//			String enf = (d.getEnfermedad() != null) ? d.getEnfermedad().getNombre() : "Sin Enfermedad";
//			opciones[i] = (i + 1) + ". " + enf;
//		}
//
//		String seleccion = (String) JOptionPane.showInputDialog(
//				this,
//				"Seleccione el diagnóstico al que desea asociar tratamientos:",
//				"Seleccionar Diagnóstico",
//				JOptionPane.QUESTION_MESSAGE,
//				null,
//				opciones,
//				opciones[0]
//		);
//
//		if (seleccion != null) {
//			for (int i = 0; i < diagnosticosActuales.size(); i++) {
//				if (seleccion.startsWith((i + 1) + ".")) {
//					return diagnosticosActuales.get(i);
//				}
//			}
//		}
//		return null;
//	}
//
//	private void actualizarTextoVacunas() {
//		if (vacunasIndicadas.isEmpty()) {
//			txtVacunas.setText("");
//		} else {
//			StringBuilder sb = new StringBuilder();
//			for (Vacuna v : vacunasIndicadas) {
//				sb.append("• ").append(v.getNombre()).append("\n");
//			}
//			txtVacunas.setText(sb.toString().trim());
//			txtVacunas.setCaretPosition(0);
//		}
//	}
//
//	private void actualizarTextoAnalisis() {
//		if (analisisIndicados.isEmpty()) {
//			txtAnalisis.setText("");
//		} else {
//			StringBuilder sb = new StringBuilder();
//			for (Analisis a : analisisIndicados) {
//				sb.append("• ").append(a.getNombre()).append("\n");
//			}
//			txtAnalisis.setText(sb.toString().trim());
//			txtAnalisis.setCaretPosition(0);
//		}
//	}
//
//	private void actualizarTextoTratamientos() {
//		List<Tratamiento> tratamientosTotales = obtenerTratamientosActuales();
//		if (tratamientosTotales.isEmpty()) {
//			txtTratamientos.setText("");
//		} else {
//			StringBuilder sb = new StringBuilder();
//			for (Tratamiento t : tratamientosTotales) {
//				sb.append("• ").append(t.getNombre()).append("\n");
//			}
//			txtTratamientos.setText(sb.toString().trim());
//			txtTratamientos.setCaretPosition(0);
//		}
//	}
//
//	private void limpiarCampos() {
//		if (txtPaciente != null) txtPaciente.setText("");
//		if (txtDoctor != null) txtDoctor.setText("");
//		if (txtDiagnostico != null) txtDiagnostico.setText("");
//		if (txtVacunas != null) txtVacunas.setText("");
//		if (txtAnalisis != null) txtAnalisis.setText("");
//		if (txtTratamientos != null) txtTratamientos.setText("");
//		if (txtObservaciones != null) txtObservaciones.setText("");
//
//		diagnosticosActuales.clear();
//		vacunasIndicadas.clear();
//		analisisIndicados.clear();
//		diagnosticosNuevos.clear();
//		diagnosticosModificados.clear();
//		diagnosticosEliminados.clear();
//		vacunasNuevas.clear();
//		vacunasEliminadas.clear();
//		vacunasOriginalesEdicion.clear();
//		analisisNuevos.clear();
//		analisisEliminadas.clear();
//		analisisOriginalesEdicion.clear();
//		pacienteActual = null;
//	}
//}