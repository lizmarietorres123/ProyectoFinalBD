package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import logico.Clinica;
import logico.Consulta;
import logico.Diagnostico;
import logico.Doctor;
import logico.Paciente;

public class ListarConsulta extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;
	private Object[] row;

	private Doctor doctorActual;

	private JTextField txtBuscar;
	private JSpinner spnFechaInicio;
	private JSpinner spnFechaFin;

	private JButton btnVerDetalle;
	private JButton btnCerrar;

	public static void main(String[] args) {
		try {
			ListarConsulta dialog = new ListarConsulta(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListarConsulta(Doctor doctor) {
		if (doctor != null) {
			this.doctorActual = doctor;
		} else if (Clinica.getDoctorActual() != null) {
			this.doctorActual = Clinica.getDoctorActual();
		} else {
			ArrayList<String> especialidades = new ArrayList<>();
			especialidades.add("Medicina General");
			this.doctorActual = new Doctor("DOC-1", "Doctor General", 10, especialidades);
		}

		setTitle("Listado de Consultas - Dr(a). " + doctorActual.getNombre());
		setBounds(100, 100, 880, 580);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());

		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panelBarra = new JPanel();
		panelBarra.setBackground(Color.WHITE);
		panelBarra.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		panelBarra.setBounds(20, 15, 824, 85);
		panelBarra.setLayout(null);
		contentPanel.add(panelBarra);

		JLabel lblDoctorInfo = new JLabel("Doctor:");
		lblDoctorInfo.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblDoctorInfo.setForeground(new Color(70, 130, 180));
		lblDoctorInfo.setBounds(15, 12, 60, 20);
		panelBarra.add(lblDoctorInfo);

		JLabel lblNombreDoctor = new JLabel(doctorActual.getNombre());
		lblNombreDoctor.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblNombreDoctor.setForeground(new Color(70, 130, 180));
		lblNombreDoctor.setBounds(80, 12, 350, 20);
		panelBarra.add(lblNombreDoctor);

		JLabel lblBuscar = new JLabel("Buscar:");
		lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblBuscar.setForeground(new Color(70, 130, 180));
		lblBuscar.setBounds(15, 45, 60, 25);
		panelBarra.add(lblBuscar);

		txtBuscar = new JTextField();
		txtBuscar.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
		txtBuscar.setBackground(new Color(224, 247, 250));
		txtBuscar.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtBuscar.setBounds(80, 45, 350, 25);
		txtBuscar.setToolTipText("Buscar por código de consulta, paciente, cédula o diagnóstico");
		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				aplicarFiltros();
			}
		});
		panelBarra.add(txtBuscar);

		JLabel lblFechaInicio = new JLabel("Desde:");
		lblFechaInicio.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblFechaInicio.setForeground(new Color(70, 130, 180));
		lblFechaInicio.setBounds(455, 45, 50, 25);
		panelBarra.add(lblFechaInicio);

		Calendar cal = Calendar.getInstance();
		cal.set(2024, Calendar.JANUARY, 1);
		Date fechaDefectoInicio = cal.getTime();

		spnFechaInicio = new JSpinner(new SpinnerDateModel(fechaDefectoInicio, null, null, Calendar.DAY_OF_YEAR));
		spnFechaInicio.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		spnFechaInicio.setEditor(new JSpinner.DateEditor(spnFechaInicio, "dd/MM/yyyy"));
		spnFechaInicio.setBounds(510, 45, 120, 25);
		spnFechaInicio.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				aplicarFiltros();
			}
		});
		panelBarra.add(spnFechaInicio);

		JLabel lblFechaFin = new JLabel("Hasta:");
		lblFechaFin.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblFechaFin.setForeground(new Color(70, 130, 180));
		lblFechaFin.setBounds(645, 45, 50, 25);
		panelBarra.add(lblFechaFin);

		spnFechaFin = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
		spnFechaFin.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		spnFechaFin.setEditor(new JSpinner.DateEditor(spnFechaFin, "dd/MM/yyyy"));
		spnFechaFin.setBounds(695, 45, 114, 25);
		spnFechaFin.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				aplicarFiltros();
			}
		});
		panelBarra.add(spnFechaFin);

		JPanel panelTable = new JPanel();
		panelTable.setBounds(20, 110, 824, 365);
		panelTable.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		panelTable.setLayout(new BorderLayout(0, 0));
		contentPanel.add(panelTable);

		JScrollPane scrollPane = new JScrollPane();
		panelTable.add(scrollPane, BorderLayout.CENTER);

		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		String[] headers = {"Código", "Paciente", "Fecha", "Diagnóstico Principal", "Tipo", "Importante"};
		model.setColumnIdentifiers(headers);

		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		table.setBackground(Color.WHITE);
		table.setSelectionBackground(new Color(176, 224, 230));
		table.setSelectionForeground(new Color(70, 130, 180));
		table.setGridColor(new Color(173, 216, 230));
		table.getTableHeader().setFont(new Font("Bahnschrift", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(135, 206, 235));
		table.getTableHeader().setForeground(new Color(70, 130, 180));

		table.getColumnModel().getColumn(0).setPreferredWidth(90);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setPreferredWidth(90);
		table.getColumnModel().getColumn(3).setPreferredWidth(260);
		table.getColumnModel().getColumn(4).setPreferredWidth(90);
		table.getColumnModel().getColumn(5).setPreferredWidth(80);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					btnVerDetalle.setEnabled(true);
					if (e.getClickCount() == 2) {
						String idConsulta = table.getValueAt(selectedRow, 0).toString();
						mostrarDetalleConsulta(idConsulta);
					}
				}
			}
		});

		scrollPane.setViewportView(table);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(Color.WHITE);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnVerDetalle = new JButton("Ver Detalles");
		btnVerDetalle.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnVerDetalle.setForeground(new Color(70, 130, 180));
		btnVerDetalle.setBackground(new Color(255, 245, 238));
		btnVerDetalle.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnVerDetalle.setFocusPainted(false);
		btnVerDetalle.setEnabled(false);
		btnVerDetalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					String idConsulta = table.getValueAt(selectedRow, 0).toString();
					mostrarDetalleConsulta(idConsulta);
				} else {
					JOptionPane.showMessageDialog(null, "Debe seleccionar una consulta de la lista.", "Atención", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		buttonPane.add(btnVerDetalle);

		btnCerrar = new JButton("Volver");
		btnCerrar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnCerrar.setForeground(new Color(70, 130, 180));
		btnCerrar.setBackground(new Color(255, 245, 238));
		btnCerrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCerrar.setFocusPainted(false);
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(btnCerrar);

		aplicarFiltros();
	}

	private void aplicarFiltros() {
		model.setRowCount(0);
		btnVerDetalle.setEnabled(false);

		String filtroTexto = txtBuscar.getText().trim().toLowerCase();
		Date fechaInicio = (Date) spnFechaInicio.getValue();
		Date fechaFin = (Date) spnFechaFin.getValue();

		List<Consulta> listaConsultas = Clinica.getInstancia().getConsultasXDoctor(doctorActual);

		if (listaConsultas != null) {
			for (Consulta c : listaConsultas) {
				if (c != null) {
					boolean cumpleFiltro = true;

					if (fechaInicio != null && fechaFin != null) {
						if (!enRangoFecha(c.getFecha(), fechaInicio, fechaFin)) {
							cumpleFiltro = false;
						}
					}

					if (cumpleFiltro && !filtroTexto.isEmpty()) {
						String idConsulta = c.getId() != null ? c.getId().toLowerCase() : "";
						String nombrePac = (c.getPaciente() != null && c.getPaciente().getNombre() != null) ? c.getPaciente().getNombre().toLowerCase() : "";
						String apellidoPac = (c.getPaciente() != null && c.getPaciente().getApellido() != null) ? c.getPaciente().getApellido().toLowerCase() : "";
						String cedulaPac = (c.getPaciente() != null && c.getPaciente().getCedula() != null) ? c.getPaciente().getCedula().toLowerCase() : "";
						String resumenDiag = obtenerResumenDiagnostico(c).toLowerCase();

						boolean coincide = idConsulta.contains(filtroTexto)
								|| nombrePac.contains(filtroTexto)
								|| apellidoPac.contains(filtroTexto)
								|| cedulaPac.contains(filtroTexto)
								|| resumenDiag.contains(filtroTexto);

						if (!coincide) {
							cumpleFiltro = false;
						}
					}

					if (cumpleFiltro) {
						row = new Object[6];
						row[0] = c.getId();
						row[1] = formatearNombrePaciente(c.getPaciente());
						row[2] = formatearFecha(c.getFecha());
						row[3] = obtenerResumenDiagnostico(c);
						row[4] = determinarTipoConsulta(c);
						row[5] = c.getEsImportante() ? "Sí" : "No";
						model.addRow(row);
					}
				}
			}
		}
	}

	private boolean enRangoFecha(Date fecha, Date inicio, Date fin) {
		if (fecha == null) return false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		int fInt = Integer.parseInt(sdf.format(fecha));
		int iInt = Integer.parseInt(sdf.format(inicio));
		int finInt = Integer.parseInt(sdf.format(fin));
		return fInt >= iInt && fInt <= finInt;
	}

	private String formatearFecha(Date fecha) {
		if (fecha == null) return "N/A";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(fecha);
	}

	private String formatearNombrePaciente(Paciente p) {
		if (p == null) return "Desconocido";
		String nombre = p.getNombre() != null ? p.getNombre() : "";
		String apellido = p.getApellido() != null ? p.getApellido() : "";
		return (nombre + " " + apellido).trim();
	}

	private String obtenerResumenDiagnostico(Consulta c) {
		if (c != null && c.getDiagnosticos() != null && !c.getDiagnosticos().isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < c.getDiagnosticos().size(); i++) {
				Diagnostico d = c.getDiagnosticos().get(i);
				if (d != null) {
					if (d.getEnfermedadDiagnosticada() != null && d.getEnfermedadDiagnosticada().getNombre() != null) {
						sb.append(d.getEnfermedadDiagnosticada().getNombre());
					} else if (d.getDescripcion() != null) {
						sb.append(d.getDescripcion());
					}

					if (i < c.getDiagnosticos().size() - 1) {
						sb.append(", ");
					}
				}
			}
			String result = sb.toString().trim();
			if (!result.isEmpty()) {
				return result.length() > 45 ? result.substring(0, 42) + "..." : result;
			}
		}
		return "Sin diagnóstico";
	}

	private String determinarTipoConsulta(Consulta c) {
		if (c != null && c.getDoctor() != null && doctorActual != null) {
			if (c.getDoctor().getIdDoctor().equalsIgnoreCase(doctorActual.getIdDoctor())) {
				return "Propia";
			}
		}
		return "Pública";
	}

	private void mostrarDetalleConsulta(String idConsulta) {
		Consulta consultaEncontrada = Clinica.getInstancia().buscarConsultaXId(idConsulta);
		if (consultaEncontrada != null) {
			try {
				DetalleConsulta dialogo = new DetalleConsulta(consultaEncontrada);
				dialogo.setModal(true);
				dialogo.setVisible(true);
			} catch (NoClassDefFoundError | Exception e) {
				StringBuilder sb = new StringBuilder();
				sb.append("Código: ").append(consultaEncontrada.getId()).append("\n");
				sb.append("Paciente: ").append(formatearNombrePaciente(consultaEncontrada.getPaciente())).append("\n");
				sb.append("Doctor: ").append(consultaEncontrada.getDoctor() != null ? consultaEncontrada.getDoctor().getNombre() : "N/A").append("\n");
				sb.append("Fecha: ").append(formatearFecha(consultaEncontrada.getFecha())).append("\n");
				sb.append("Tratamiento: ").append(consultaEncontrada.getTratamiento()).append("\n");
				sb.append("Observaciones: ").append(consultaEncontrada.getObservaciones()).append("\n");

				JOptionPane.showMessageDialog(this, sb.toString(), "Detalle de Consulta " + consultaEncontrada.getId(), JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "No se encontró la consulta seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}