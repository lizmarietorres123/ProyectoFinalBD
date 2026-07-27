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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
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

public class ListarConsulta extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private DefaultTableModel model;
	private Object[] row;
	private Doctor doctor;
	private Consulta auxConsulta = null;

	private JSpinner spnFecIni;
	private JSpinner spnFecFin;
	private JTextField txtBuscarPaciente;
	private JCheckBox chkSoloImportantes;
	private JTable table;
	private JPanel panelBarra;
	private JPanel panelTable;

	private JButton btnVerDetalle;
	private JButton btnCancelar;

	public static void main(String[] args) {
		try {
			ListarConsulta dialog = new ListarConsulta(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListarConsulta(Doctor selectDoctor) {
		if (selectDoctor == null) {
			ArrayList<String> especialidades = new ArrayList<>();
			especialidades.add("Pediatría");
			especialidades.add("Dermatología");
			selectDoctor = new Doctor("DOC-1", "Liz Marie Torres", 20, especialidades);
		}
		this.doctor = selectDoctor;
		String tituloDoctor = (doctor != null) ? "Dr. " + doctor.getNombre() : "General";
		setTitle("Listado de Consultas - " + tituloDoctor);

		setBounds(100, 100, 880, 560);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		panelBarra = new JPanel();
		panelBarra.setBackground(Color.WHITE);
		panelBarra.setBorder(new LineBorder(new Color(70, 130, 180)));
		panelBarra.setBounds(28, 15, 808, 95);
		panelBarra.setLayout(null);
		contentPanel.add(panelBarra);

		JLabel lblPaciente = new JLabel("Paciente:");
		lblPaciente.setForeground(new Color(70, 130, 180));
		lblPaciente.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblPaciente.setBounds(15, 15, 70, 25);
		panelBarra.add(lblPaciente);

		txtBuscarPaciente = new JTextField();
		txtBuscarPaciente.setToolTipText("Filtrar por nombre o apellido del paciente");
		txtBuscarPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
		txtBuscarPaciente.setBounds(85, 14, 300, 26);
		txtBuscarPaciente.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtrarTabla();
			}
		});
		panelBarra.add(txtBuscarPaciente);

		JLabel lblFechaInicio = new JLabel("Desde:");
		lblFechaInicio.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblFechaInicio.setForeground(new Color(70, 130, 180));
		lblFechaInicio.setBounds(405, 15, 50, 25);
		panelBarra.add(lblFechaInicio);

		Calendar calInicio = Calendar.getInstance();
		calInicio.set(2000, Calendar.JANUARY, 1);

		spnFecIni = new JSpinner();
		spnFecIni.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
		spnFecIni.setModel(new SpinnerDateModel(calInicio.getTime(), null, null, Calendar.DAY_OF_YEAR));
		spnFecIni.setEditor(new JSpinner.DateEditor(spnFecIni, "dd/MM/yyyy"));
		spnFecIni.setBounds(460, 14, 120, 26);
		spnFecIni.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				filtrarTabla();
			}
		});
		panelBarra.add(spnFecIni);

		JLabel lblFechaFin = new JLabel("Hasta:");
		lblFechaFin.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblFechaFin.setForeground(new Color(70, 130, 180));
		lblFechaFin.setBounds(595, 15, 50, 25);
		panelBarra.add(lblFechaFin);

		spnFecFin = new JSpinner();
		spnFecFin.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
		spnFecFin.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
		spnFecFin.setEditor(new JSpinner.DateEditor(spnFecFin, "dd/MM/yyyy"));
		spnFecFin.setBounds(650, 14, 120, 26);
		spnFecFin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				filtrarTabla();
			}
		});
		panelBarra.add(spnFecFin);

		chkSoloImportantes = new JCheckBox("Solo consultas importantes");
		chkSoloImportantes.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		chkSoloImportantes.setForeground(new Color(70, 130, 180));
		chkSoloImportantes.setBackground(Color.WHITE);
		chkSoloImportantes.setBounds(15, 55, 250, 23);
		chkSoloImportantes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filtrarTabla();
			}
		});
		panelBarra.add(chkSoloImportantes);

		panelTable = new JPanel();
		panelTable.setBounds(28, 120, 808, 320);
		panelTable.setBorder(new LineBorder(new Color(70, 130, 180)));
		contentPanel.add(panelTable);
		panelTable.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panelTable.add(scrollPane, BorderLayout.CENTER);

		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		String[] headers = {"Código", "Paciente", "Fecha", "Diagnóstico", "Tipo", "Importante"};
		model.setColumnIdentifiers(headers);

		table = new JTable();
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		table.setBackground(Color.WHITE);
		table.setSelectionBackground(new Color(176, 224, 230));
		table.setSelectionForeground(new Color(70, 130, 180));
		table.setGridColor(new Color(173, 216, 230));
		table.getTableHeader().setFont(new Font("Bahnschrift", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(135, 206, 235));
		table.getTableHeader().setForeground(new Color(70, 130, 180));

		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setPreferredWidth(90);
		table.getColumnModel().getColumn(3).setPreferredWidth(250);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(80);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index > -1) {
					btnVerDetalle.setEnabled(true);
					String codigo = table.getValueAt(index, 0).toString();
					auxConsulta = Clinica.getInstancia().buscarConsultaXId(codigo);
				}
				if (e.getClickCount() == 2 && auxConsulta != null) {
					mostrarDetalleConsulta(auxConsulta);
				}
			}
		});
		scrollPane.setViewportView(table);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(Color.WHITE);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnVerDetalle = new JButton("Ver Detalle");
		btnVerDetalle.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnVerDetalle.setForeground(new Color(70, 130, 180));
		btnVerDetalle.setBackground(new Color(255, 245, 238));
		btnVerDetalle.setFocusPainted(false);
		btnVerDetalle.setEnabled(false);
		btnVerDetalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (auxConsulta != null) {
					mostrarDetalleConsulta(auxConsulta);
				}
			}
		});
		buttonPane.add(btnVerDetalle);

		btnCancelar = new JButton("Volver");
		btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnCancelar.setForeground(new Color(70, 130, 180));
		btnCancelar.setBackground(new Color(255, 245, 238));
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(btnCancelar);

		filtrarTabla();
	}

	private void filtrarTabla() {
		model.setRowCount(0);

		Date fechaInicio = (Date) spnFecIni.getValue();
		Date fechaFin = (Date) spnFecFin.getValue();
		String filtroPaciente = txtBuscarPaciente.getText().trim().toLowerCase();
		boolean soloImportantes = chkSoloImportantes.isSelected();

		ArrayList<Consulta> consultas = (doctor != null)
				? Clinica.getInstancia().getConsultasVisiblesXDoctor(doctor)
				: Clinica.getInstancia().getConsultas();

		if (consultas != null) {
			for (Consulta c : consultas) {
				if (c != null) {
					boolean coincideFecha = dentroDelRango(c.getFecha(), fechaInicio, fechaFin);

					String nombreComp = "";
					if (c.getPaciente() != null) {
						String nombre = c.getPaciente().getNombre() != null ? c.getPaciente().getNombre() : "";
						String apellido = c.getPaciente().getApellido() != null ? c.getPaciente().getApellido() : "";
						nombreComp = (nombre + " " + apellido).trim().toLowerCase();
					}
					boolean coincidePaciente = filtroPaciente.isEmpty() || nombreComp.contains(filtroPaciente);

					boolean coincideImportante = !soloImportantes || c.getEsImportante();

					if (coincideFecha && coincidePaciente && coincideImportante) {
						row = new Object[6];
						row[0] = c.getId();
						row[1] = c.getPaciente() != null
								? (c.getPaciente().getNombre() + (c.getPaciente().getApellido() != null ? " " + c.getPaciente().getApellido() : "")).trim()
								: "Sin Paciente";
						row[2] = formatearFecha(c.getFecha());
						row[3] = obtenerResumenDiagnostico(c);

						if (doctor != null && c.getDoctor() != null && c.getDoctor().getIdDoctor() != null) {
							row[4] = c.getDoctor().getIdDoctor().equals(doctor.getIdDoctor()) ? "Propia" : "Pública";
						} else {
							row[4] = (c.getDoctor() != null) ? "Dr. " + c.getDoctor().getNombre() : "General";
						}

						row[5] = c.getEsImportante() ? "Sí" : "No";
						model.addRow(row);
					}
				}
			}
		}

		auxConsulta = null;
		if (btnVerDetalle != null) {
			btnVerDetalle.setEnabled(false);
		}
	}

	private boolean dentroDelRango(Date fecha, Date inicio, Date fin) {
		if (fecha == null) return false;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		int fechaInt = Integer.parseInt(sdf.format(fecha));

		if (inicio != null) {
			int inicioInt = Integer.parseInt(sdf.format(inicio));
			if (fechaInt < inicioInt) return false;
		}
		if (fin != null) {
			int finInt = Integer.parseInt(sdf.format(fin));
			if (fechaInt > finInt) return false;
		}

		return true;
	}

	private String formatearFecha(Date fecha) {
		if (fecha == null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(fecha);
	}

	private String obtenerResumenDiagnostico(Consulta c) {
		if (c.getDiagnosticos() != null && !c.getDiagnosticos().isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < c.getDiagnosticos().size(); i++) {
				Diagnostico d = c.getDiagnosticos().get(i);
				if (d != null) {
					String desc = d.getDescripcion();
					sb.append((desc != null && !desc.isEmpty()) ? desc : d.getCodigoDiagnostico());
					if (i < c.getDiagnosticos().size() - 1) sb.append(", ");
				}
			}
			String res = sb.toString();
			return res.length() > 40 ? res.substring(0, 37) + "..." : res;
		}
		return "Sin diagnóstico";
	}

	private void mostrarDetalleConsulta(Consulta c) {
		DetalleConsulta dialogo = new DetalleConsulta(c);
		dialogo.setModal(true);
		dialogo.setVisible(true);
	}
}