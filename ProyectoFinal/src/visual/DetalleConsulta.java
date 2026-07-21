package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import logico.Consulta;

public class DetalleConsulta extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Consulta consulta;
	private JTextField txtCodigo;
	private JTextField txtPaciente;
	private JTextField txtCedula;
	private JTextField txtDoctor;
	private JTextField txtFecha;
	private JTextField txtCodigoDiagnostico;
	private JTextField txtEnfermedad;
	private JTextField txtImportante;
	private JTextArea txtSintomas;
	private JTextArea txtDescripcionDiagnostico;
	private JTextArea txtTratamiento;
	private JTextArea txtObservaciones;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			/*
			DetalleConsulta dialog = new DetalleConsulta(consulta);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DetalleConsulta(Consulta consulta) {
		this.consulta = consulta;
		
		setTitle("Detalle de Consulta - " + consulta.getId());
		setBounds(100, 100, 750, 682);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panelInfoGeneral = new JPanel();
		panelInfoGeneral.setBackground(Color.WHITE);
		panelInfoGeneral.setBorder(new TitledBorder(
			new LineBorder(new Color(70, 130, 180), 2), 
			"Información General", 
			TitledBorder.LEFT, 
			TitledBorder.TOP, 
			new Font("Bahnschrift", Font.BOLD, 14),
			new Color(70, 130, 180)
		));
		panelInfoGeneral.setBounds(10, 10, 714, 130);
		contentPanel.add(panelInfoGeneral);
		panelInfoGeneral.setLayout(null);
		
		JLabel lblCodigo = new JLabel("Código:");
		lblCodigo.setFont(new Font("Verdana", Font.BOLD, 11));
		lblCodigo.setForeground(new Color(70, 130, 180));
		lblCodigo.setBounds(15, 25, 80, 20);
		panelInfoGeneral.add(lblCodigo);
		
		txtCodigo = new JTextField();
		txtCodigo.setEditable(false);
		txtCodigo.setBackground(new Color(224, 247, 250));
		txtCodigo.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtCodigo.setBounds(105, 25, 150, 20);
		txtCodigo.setText(consulta.getId());
		panelInfoGeneral.add(txtCodigo);
		
		JLabel lblFecha = new JLabel("Fecha/Hora:");
		lblFecha.setFont(new Font("Verdana", Font.BOLD, 11));
		lblFecha.setForeground(new Color(70, 130, 180));
		lblFecha.setBounds(364, 25, 90, 20);
		panelInfoGeneral.add(lblFecha);
		
		txtFecha = new JTextField();
		txtFecha.setEditable(false);
		txtFecha.setBackground(new Color(224, 247, 250));
		txtFecha.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtFecha.setBounds(450, 25, 244, 20);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		txtFecha.setText(sdf.format(consulta.getFecha()));
		panelInfoGeneral.add(txtFecha);
		
		JLabel lblPaciente = new JLabel("Paciente:");
		lblPaciente.setFont(new Font("Verdana", Font.BOLD, 11));
		lblPaciente.setForeground(new Color(70, 130, 180));
		lblPaciente.setBounds(15, 60, 80, 20);
		panelInfoGeneral.add(lblPaciente);
		
		txtPaciente = new JTextField();
		txtPaciente.setEditable(false);
		txtPaciente.setBackground(new Color(224, 247, 250));
		txtPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtPaciente.setBounds(105, 60, 250, 20);
		txtPaciente.setText(consulta.getPaciente().getNombre());
		panelInfoGeneral.add(txtPaciente);
		
		JLabel lblCedula = new JLabel("Cédula:");
		lblCedula.setFont(new Font("Verdana", Font.BOLD, 11));
		lblCedula.setForeground(new Color(70, 130, 180));
		lblCedula.setBounds(364, 60, 70, 20);
		panelInfoGeneral.add(lblCedula);
		
		txtCedula = new JTextField();
		txtCedula.setEditable(false);
		txtCedula.setBackground(new Color(224, 247, 250));
		txtCedula.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtCedula.setBounds(450, 60, 244, 20);
		txtCedula.setText(consulta.getPaciente().getCedula());
		panelInfoGeneral.add(txtCedula);
		
		JLabel lblDoctor = new JLabel("Doctor:");
		lblDoctor.setFont(new Font("Verdana", Font.BOLD, 11));
		lblDoctor.setForeground(new Color(70, 130, 180));
		lblDoctor.setBounds(15, 95, 80, 20);
		panelInfoGeneral.add(lblDoctor);
		
		txtDoctor = new JTextField();
		txtDoctor.setEditable(false);
		txtDoctor.setBackground(new Color(224, 247, 250));
		txtDoctor.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtDoctor.setBounds(105, 95, 250, 20);
		txtDoctor.setText(consulta.getDoctor().getNombre());
		panelInfoGeneral.add(txtDoctor);
		
		JLabel lblImportante = new JLabel("Importante:");
		lblImportante.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		lblImportante.setForeground(new Color(70, 130, 180));
		lblImportante.setBounds(364, 95, 80, 20);
		panelInfoGeneral.add(lblImportante);
		
		txtImportante = new JTextField();
		txtImportante.setEditable(false);
		txtImportante.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		txtImportante.setBackground(new Color(224, 247, 250));
		txtImportante.setForeground(new Color(70, 130, 180));
		if(consulta.getEsImportante()) {
			txtImportante.setText("SÍ");
			txtImportante.setForeground(new Color(220, 20, 60));
		} else {
			txtImportante.setText("NO");
			txtImportante.setForeground(new Color(100, 149, 237));
		}
		txtImportante.setBounds(450, 95, 244, 20);
		panelInfoGeneral.add(txtImportante);
		
		JPanel panelSintomas = new JPanel();
		panelSintomas.setBackground(Color.WHITE);
		panelSintomas.setBorder(new TitledBorder(
			new LineBorder(new Color(100, 149, 237), 2), 
			"Síntomas Presentados", 
			TitledBorder.LEFT, 
			TitledBorder.TOP, 
			new Font("Bahnschrift", Font.BOLD, 14),
			new Color(100, 149, 237)
		));
		panelSintomas.setBounds(10, 150, 714, 88);
		contentPanel.add(panelSintomas);
		panelSintomas.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollSintomas = new JScrollPane();
		panelSintomas.add(scrollSintomas, BorderLayout.CENTER);
		
		txtSintomas = new JTextArea();
		txtSintomas.setEditable(false);
		txtSintomas.setLineWrap(true);
		txtSintomas.setWrapStyleWord(true);
		txtSintomas.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtSintomas.setBackground(new Color(240, 248, 255));
		txtSintomas.setText(consulta.getSintomas() != null ? consulta.getSintomas() : "No se registraron síntomas.");
		scrollSintomas.setViewportView(txtSintomas);
		
		JPanel panelDiagnostico = new JPanel();
		panelDiagnostico.setBackground(Color.WHITE);
		panelDiagnostico.setBorder(new TitledBorder(
			new LineBorder(new Color(135, 206, 235), 2), 
			"Diagnóstico", 
			TitledBorder.LEFT, 
			TitledBorder.TOP, 
			new Font("Bahnschrift", Font.BOLD, 14),
			new Color(70, 130, 180)
		));
		panelDiagnostico.setBounds(10, 250, 714, 150);
		contentPanel.add(panelDiagnostico);
		panelDiagnostico.setLayout(null);
		
		JLabel lblCodigoDiag = new JLabel("Código:");
		lblCodigoDiag.setFont(new Font("Verdana", Font.BOLD, 11));
		lblCodigoDiag.setForeground(new Color(70, 130, 180));
		lblCodigoDiag.setBounds(15, 25, 80, 20);
		panelDiagnostico.add(lblCodigoDiag);
		
		txtCodigoDiagnostico = new JTextField();
		txtCodigoDiagnostico.setEditable(false);
		txtCodigoDiagnostico.setBackground(new Color(224, 247, 250));
		txtCodigoDiagnostico.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtCodigoDiagnostico.setBounds(105, 25, 150, 20);
		if(consulta.getDiagnostico() != null) {
			txtCodigoDiagnostico.setText(consulta.getDiagnostico().getCodigoDiagnostico());
		} else {
			txtCodigoDiagnostico.setText("Sin diagnóstico");
		}
		panelDiagnostico.add(txtCodigoDiagnostico);
		
		JLabel lblEnfermedad = new JLabel("Enfermedad:");
		lblEnfermedad.setFont(new Font("Verdana", Font.BOLD, 11));
		lblEnfermedad.setForeground(new Color(70, 130, 180));
		lblEnfermedad.setBounds(300, 25, 90, 20);
		panelDiagnostico.add(lblEnfermedad);
		
		txtEnfermedad = new JTextField();
		txtEnfermedad.setEditable(false);
		txtEnfermedad.setBackground(new Color(224, 247, 250));
		txtEnfermedad.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtEnfermedad.setBounds(400, 25, 294, 20);
		if(consulta.getDiagnostico() != null && 
		   consulta.getDiagnostico().getEnfermedadDiagnosticada() != null) {
			txtEnfermedad.setText(consulta.getDiagnostico().getEnfermedadDiagnosticada().getNombre());
		} else {
			txtEnfermedad.setText("N/A");
		}
		panelDiagnostico.add(txtEnfermedad);
		
		JLabel lblDescripcion = new JLabel("Descripción:");
		lblDescripcion.setFont(new Font("Verdana", Font.BOLD, 11));
		lblDescripcion.setForeground(new Color(70, 130, 180));
		lblDescripcion.setBounds(15, 55, 90, 20);
		panelDiagnostico.add(lblDescripcion);
		
		JScrollPane scrollDiagnostico = new JScrollPane();
		scrollDiagnostico.setBounds(15, 80, 679, 55);
		panelDiagnostico.add(scrollDiagnostico);
		
		txtDescripcionDiagnostico = new JTextArea();
		txtDescripcionDiagnostico.setEditable(false);
		txtDescripcionDiagnostico.setLineWrap(true);
		txtDescripcionDiagnostico.setWrapStyleWord(true);
		txtDescripcionDiagnostico.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtDescripcionDiagnostico.setBackground(new Color(240, 248, 255));
		if(consulta.getDiagnostico() != null) {
			txtDescripcionDiagnostico.setText(consulta.getDiagnostico().getDescripcion());
		} else {
			txtDescripcionDiagnostico.setText("No se realizó diagnóstico.");
		}
		scrollDiagnostico.setViewportView(txtDescripcionDiagnostico);
		
		JPanel panelTratamiento = new JPanel();
		panelTratamiento.setBackground(Color.WHITE);
		panelTratamiento.setBorder(new TitledBorder(
			new LineBorder(new Color(100, 149, 237), 2), 
			"Tratamiento", 
			TitledBorder.LEFT, 
			TitledBorder.TOP, 
			new Font("Bahnschrift", Font.BOLD, 14),
			new Color(100, 149, 237)
		));
		panelTratamiento.setBounds(10, 410, 714, 100);
		contentPanel.add(panelTratamiento);
		panelTratamiento.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollTratamiento = new JScrollPane();
		panelTratamiento.add(scrollTratamiento, BorderLayout.CENTER);
		
		txtTratamiento = new JTextArea();
		txtTratamiento.setEditable(false);
		txtTratamiento.setLineWrap(true);
		txtTratamiento.setWrapStyleWord(true);
		txtTratamiento.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtTratamiento.setBackground(new Color(240, 248, 255));
		txtTratamiento.setText(consulta.getTratamiento() != null ? consulta.getTratamiento() : "No se prescribió tratamiento.");
		scrollTratamiento.setViewportView(txtTratamiento);
		
		JPanel panelObservaciones = new JPanel();
		panelObservaciones.setBackground(Color.WHITE);
		panelObservaciones.setBorder(new TitledBorder(
			new LineBorder(new Color(135, 206, 235), 2), 
			"Observaciones Adicionales", 
			TitledBorder.LEFT, 
			TitledBorder.TOP, 
			new Font("Bahnschrift", Font.BOLD, 14),
			new Color(70, 130, 180)
		));
		panelObservaciones.setBounds(10, 520, 714, 80);
		contentPanel.add(panelObservaciones);
		panelObservaciones.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollObservaciones = new JScrollPane();
		panelObservaciones.add(scrollObservaciones, BorderLayout.CENTER);
		
		txtObservaciones = new JTextArea();
		txtObservaciones.setEditable(false);
		txtObservaciones.setLineWrap(true);
		txtObservaciones.setWrapStyleWord(true);
		txtObservaciones.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtObservaciones.setBackground(new Color(240, 248, 255));
		txtObservaciones.setText(consulta.getObservaciones() != null ? consulta.getObservaciones() : "Sin observaciones adicionales.");
		scrollObservaciones.setViewportView(txtObservaciones);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(240, 248, 255));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
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
			btnCerrar.setActionCommand("Cancel");
			buttonPane.add(btnCerrar);
		}
	}
}