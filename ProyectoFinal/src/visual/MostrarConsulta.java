package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Font;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import logico.Clinica;
import logico.Consulta;
import logico.Doctor;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JCheckBox;

public class MostrarConsulta extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private static DefaultTableModel model;
	private static Object[] row;
	private Doctor doctor;
	private JSpinner spnFecIni;
	private JSpinner spnFecFin;
	private JTextField txtFiltroPaciente;
	private JCheckBox chkSoloImportantes;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MostrarConsulta dialog = new MostrarConsulta(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MostrarConsulta(Doctor selectDoctor) {
		
		if(selectDoctor == null) {
			ArrayList<String> especialidades = new ArrayList<>();
			especialidades.add("Pediatría");
			especialidades.add("Dermatología");
			
			selectDoctor = new Doctor(
				"DOC-"+ 1,
				"Liz Marie Torres",
				20,
				especialidades
			);
		}
		
		setTitle("Listado de Consultas - Dr. " + selectDoctor.getNombre());
		setBounds(100, 100, 900, 600);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(240, 248, 255));
		contentPanel.setBounds(0, 0, 0, 0);
		doctor = selectDoctor;
		
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(new Color(240, 248, 255));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);

		JPanel panelBarra = new JPanel();
		panelBarra.setBackground(Color.WHITE);
		panelBarra.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		panelBarra.setBounds(15, 16, 853, 160);
		getContentPane().add(panelBarra);
		panelBarra.setLayout(null);
		
		JLabel lblDoctor = new JLabel("Doctor:");
		lblDoctor.setBounds(15, 14, 78, 20);
		panelBarra.add(lblDoctor);
		lblDoctor.setFont(new Font("Verdana", Font.BOLD, 15));
		lblDoctor.setForeground(new Color(70, 130, 180));
		
		JLabel lblNomDoc = new JLabel(selectDoctor.getNombre());
		lblNomDoc.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		lblNomDoc.setForeground(new Color(70, 130, 180));
		lblNomDoc.setBounds(84, 14, 388, 20);
		panelBarra.add(lblNomDoc);
		
		JLabel lblPaciente = new JLabel("Paciente:");
		lblPaciente.setFont(new Font("Verdana", Font.BOLD, 13));
		lblPaciente.setForeground(new Color(70, 130, 180));
		lblPaciente.setBounds(15, 50, 96, 20);
		panelBarra.add(lblPaciente);
		
		txtFiltroPaciente = new JTextField();
		txtFiltroPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		txtFiltroPaciente.setBackground(new Color(224, 247, 250));
		txtFiltroPaciente.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtFiltroPaciente.setBounds(117, 48, 403, 26);
		txtFiltroPaciente.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				aplicarFiltros();
			}
		});
		panelBarra.add(txtFiltroPaciente);
		txtFiltroPaciente.setColumns(10);
		
		JLabel lblFechaInicio = new JLabel("Fecha Inicio:");
		lblFechaInicio.setFont(new Font("Verdana", Font.BOLD, 13));
		lblFechaInicio.setForeground(new Color(70, 130, 180));
		lblFechaInicio.setBounds(15, 90, 96, 20);
		panelBarra.add(lblFechaInicio);
		
		JLabel lblFechaFin = new JLabel("Fecha Fin:");
		lblFechaFin.setFont(new Font("Verdana", Font.BOLD, 13));
		lblFechaFin.setForeground(new Color(70, 130, 180));
		lblFechaFin.setBounds(290, 90, 96, 20);
		panelBarra.add(lblFechaFin);
		
		spnFecIni = new JSpinner();
		spnFecIni.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		spnFecIni.setBackground(new Color(224, 247, 250));
		spnFecIni.setModel(new SpinnerDateModel(new Date(1704085200000L), new Date(1704085200000L), null, Calendar.DAY_OF_YEAR));
		spnFecIni.setEditor(new JSpinner.DateEditor(spnFecIni, "dd/MM/yyyy"));
		spnFecIni.setBounds(117, 88, 150, 26);
		panelBarra.add(spnFecIni);
		
		spnFecFin = new JSpinner();
		spnFecFin.setModel(new SpinnerDateModel(new Date(), new Date(1704085200000L), null, Calendar.DAY_OF_YEAR));
		spnFecFin.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		spnFecFin.setBackground(new Color(224, 247, 250));
		spnFecFin.setEditor(new JSpinner.DateEditor(spnFecFin, "dd/MM/yyyy"));
		spnFecFin.setBounds(370, 88, 150, 26);
		panelBarra.add(spnFecFin);
		
		chkSoloImportantes = new JCheckBox("Solo consultas importantes");
		chkSoloImportantes.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		chkSoloImportantes.setForeground(new Color(70, 130, 180));
		chkSoloImportantes.setBackground(Color.WHITE);
		chkSoloImportantes.setBounds(15, 125, 250, 23);
		chkSoloImportantes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aplicarFiltros();
			}
		});
		panelBarra.add(chkSoloImportantes);
		
		JButton btnFiltrar = new JButton("Filtrar");
		btnFiltrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aplicarFiltros();
			}
		});
		btnFiltrar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnFiltrar.setBackground(new Color(176, 224, 230));
		btnFiltrar.setForeground(new Color(70, 130, 180));
		btnFiltrar.setFocusPainted(false);
		btnFiltrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnFiltrar.setBounds(550, 88, 100, 26);
		panelBarra.add(btnFiltrar);
		
		JButton btnMostrarTodas = new JButton("Mostrar Todas");
		btnMostrarTodas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtFiltroPaciente.setText("");
				chkSoloImportantes.setSelected(false);
				listarConsultas(null, null, "", false);
			}
		});
		btnMostrarTodas.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnMostrarTodas.setBackground(new Color(176, 224, 230));
		btnMostrarTodas.setForeground(new Color(70, 130, 180));
		btnMostrarTodas.setFocusPainted(false);
		btnMostrarTodas.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnMostrarTodas.setBounds(670, 88, 150, 26);
		panelBarra.add(btnMostrarTodas);
		
		JPanel panelTable = new JPanel();
		panelTable.setBackground(new Color(240, 248, 255));
		panelTable.setBounds(15, 180, 853, 300);
		getContentPane().add(panelTable);
		panelTable.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		panelTable.add(scrollPane, BorderLayout.CENTER);
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; 
			}
		};
		
		table = new JTable();
		String[] headers = {
			"Código", 
			"Paciente", 
			"Fecha", 
			"Diagnóstico", 
			"Tipo",
			"Importante"
		};
		model.setColumnIdentifiers(headers);
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
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					if(row >= 0) {
						String codigoConsulta = (String) table.getValueAt(row, 0);
						mostrarDetalleConsulta(codigoConsulta);
					}
				}
			}
		});
		
		scrollPane.setViewportView(table);
		
		JPanel panelBotones = new JPanel();
		panelBotones.setBackground(new Color(240, 248, 255));
		panelBotones.setBounds(15, 485, 853, 40);
		getContentPane().add(panelBotones);
		panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton btnVerDetalle = new JButton("Ver Detalle");
		btnVerDetalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if(row >= 0) {
					String codigoConsulta = (String) table.getValueAt(row, 0);
					mostrarDetalleConsulta(codigoConsulta);
				} else {
					javax.swing.JOptionPane.showMessageDialog(null, 
						"Debe seleccionar una consulta.", 
						"Información", 
						javax.swing.JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnVerDetalle.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnVerDetalle.setBackground(new Color(176, 224, 230));
		btnVerDetalle.setForeground(new Color(70, 130, 180));
		btnVerDetalle.setFocusPainted(false);
		btnVerDetalle.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		panelBotones.add(btnVerDetalle);
		
		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCerrar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnCerrar.setBackground(new Color(176, 224, 230));
		btnCerrar.setForeground(new Color(70, 130, 180));
		btnCerrar.setFocusPainted(false);
		btnCerrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		panelBotones.add(btnCerrar);
		
		listarConsultas(null, null, "", false);
	}
	
	private void aplicarFiltros() {
		Date fechaInicio = (Date) spnFecIni.getValue();
		Date fechaFin = (Date) spnFecFin.getValue();
		String nombrePaciente = txtFiltroPaciente.getText().trim();
		boolean soloImportantes = chkSoloImportantes.isSelected();
		listarConsultas(fechaInicio, fechaFin, nombrePaciente, soloImportantes);
	}
	
	private void listarConsultas(Date fechaInicio, Date fechaFin, String nombrePaciente, boolean soloImportantes) {
		model.setRowCount(0);
		row = new Object[model.getColumnCount()];
		
		ArrayList<Consulta> consultas = Clinica.getInstancia().getConsultasVisiblesXDoctor(doctor);
		
		for(Consulta consulta : consultas) {
			boolean cumpleFiltros = true;
			
			if(fechaInicio != null && fechaFin != null) {
				if(!dentroDelRango(consulta.getFecha(), fechaInicio, fechaFin)) {
					cumpleFiltros = false;
				}
			}
			
			if(cumpleFiltros && nombrePaciente != null && !nombrePaciente.isEmpty()) {
				String nombreConsulta = consulta.getPaciente().getNombre().toLowerCase();
				if(!nombreConsulta.contains(nombrePaciente.toLowerCase())) {
					cumpleFiltros = false;
				}
			}
			
			if(cumpleFiltros && soloImportantes) {
				if(!consulta.getEsImportante()) {
					cumpleFiltros = false;
				}
			}
			
			if(cumpleFiltros) {
				row[0] = consulta.getId();
				row[1] = consulta.getPaciente().getNombre();
				row[2] = formatearFecha(consulta.getFecha());
				row[3] = obtenerResumenDiagnostico(consulta);
				row[4] = determinarTipoConsulta(consulta);
				row[5] = consulta.getEsImportante() ? "Sí" : "No";
				model.addRow(row);
			}
		}
	}
	
	private boolean dentroDelRango(Date fecha, Date inicio, Date fin) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		int fechaInt = Integer.parseInt(sdf.format(fecha));
		int inicioInt = Integer.parseInt(sdf.format(inicio));
		int finInt = Integer.parseInt(sdf.format(fin));
		
		return fechaInt >= inicioInt && fechaInt <= finInt;
	}
	
	private String formatearFecha(Date fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(fecha);
	}
	
	private String obtenerResumenDiagnostico(Consulta consulta) {
		if(consulta.getDiagnostico() != null) {
			String desc = consulta.getDiagnostico().getDescripcion();
			if(desc != null && !desc.isEmpty()) {
				return desc.length() > 40 ? 
					desc.substring(0, 40) + "..." : desc;
			}
		}
		return "Sin diagnóstico";
	}
	
	private String determinarTipoConsulta(Consulta consulta) {
		if(consulta.getDoctor().getIdDoctor().equals(doctor.getIdDoctor())) {
			return "Propia";
		}
		return "Pública";
	}
	
	private void mostrarDetalleConsulta(String codigo) {
		Consulta consulta = Clinica.getInstancia().buscarConsultaXId(codigo);
		if(consulta != null) {
			DetalleConsulta dialogo = new DetalleConsulta(consulta);
			dialogo.setModal(true);
			dialogo.setVisible(true);
		}
	}
}