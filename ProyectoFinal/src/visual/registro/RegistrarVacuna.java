package visual.registro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import logico.Clinica;
import logico.Enfermedad;
import logico.Vacuna;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrarVacuna extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextField txtFabricante;
	private JSpinner spinEdadMin;
	private JComboBox<Enfermedad> cbEnfermedad;
	private Vacuna miVacuna = null;

	public static void main(String[] args) {
		try {
			RegistrarVacuna dialog = new RegistrarVacuna(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RegistrarVacuna(Vacuna vac) {
		miVacuna = vac;
		
		if(miVacuna == null) {
			setTitle("Registrar Vacuna");
		} else {
			setTitle("Modificar Vacuna");
		}
		
		setBounds(100, 100, 700, 338);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panelRegistro = new JPanel();
		panelRegistro.setBackground(Color.WHITE);
		panelRegistro.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Datos de la Vacuna", TitledBorder.CENTER, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(70, 130, 180)));
		panelRegistro.setBounds(12, 13, 664, 225);
		contentPanel.add(panelRegistro);
		panelRegistro.setLayout(null);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setForeground(new Color(70, 130, 180));
		lblNombre.setFont(new Font("Verdana", Font.BOLD, 12));
		lblNombre.setBounds(10, 30, 120, 20);
		panelRegistro.add(lblNombre);
		
		txtNombre = new JTextField();
		txtNombre.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtNombre.setBackground(new Color(224, 247, 250));
		txtNombre.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtNombre.setBounds(140, 30, 200, 25);
		panelRegistro.add(txtNombre);
		
		JLabel lblEnfermedad = new JLabel("Enfermedad:");
		lblEnfermedad.setForeground(new Color(70, 130, 180));
		lblEnfermedad.setFont(new Font("Verdana", Font.BOLD, 12));
		lblEnfermedad.setBounds(10, 73, 120, 20);
		panelRegistro.add(lblEnfermedad);
		
		cbEnfermedad = new JComboBox<>();
		cbEnfermedad.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		cbEnfermedad.setBackground(new Color(224, 247, 250));
		cbEnfermedad.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		cbEnfermedad.setBounds(140, 68, 200, 25);
		panelRegistro.add(cbEnfermedad);
		
		if (Clinica.getInstancia().getEnfermedades() != null) {
			for (Enfermedad e : Clinica.getInstancia().getEnfermedades()) {
				cbEnfermedad.addItem(e);
			}
		}
		cbEnfermedad.setSelectedIndex(-1);
		
		JLabel lblEdadMin = new JLabel("Edad minima:");
		lblEdadMin.setForeground(new Color(70, 130, 180));
		lblEdadMin.setFont(new Font("Verdana", Font.BOLD, 12));
		lblEdadMin.setBounds(10, 117, 120, 20);
		panelRegistro.add(lblEdadMin);
		
		spinEdadMin = new JSpinner();
		spinEdadMin.setModel(new SpinnerNumberModel(0, 0, 120, 1));
		spinEdadMin.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		spinEdadMin.setBounds(140, 115, 77, 25);
		panelRegistro.add(spinEdadMin);
		
		JLabel lblFabricante = new JLabel("Fabricante:");
		lblFabricante.setForeground(new Color(70, 130, 180));
		lblFabricante.setFont(new Font("Verdana", Font.BOLD, 12));
		lblFabricante.setBounds(10, 164, 120, 20);
		panelRegistro.add(lblFabricante);
		
		txtFabricante = new JTextField();
		txtFabricante.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtFabricante.setBackground(new Color(224, 247, 250));
		txtFabricante.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtFabricante.setBounds(140, 162, 200, 25);
		panelRegistro.add(txtFabricante);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 248, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnRegistrar = new JButton("Registrar");
		if(miVacuna != null) {
			btnRegistrar.setText("Modificar");
		}
		btnRegistrar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnRegistrar.setBackground(new Color(176, 224, 230));
		btnRegistrar.setForeground(new Color(70, 130, 180));
		btnRegistrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnRegistrar.setFocusPainted(false);
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(miVacuna != null) {
					modificarVacuna();
				} else {
					registrarVacuna();
				}
			}
		});
		buttonPane.add(btnRegistrar);
		
		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnCerrar.setBackground(new Color(176, 224, 230));
		btnCerrar.setForeground(new Color(70, 130, 180));
		btnCerrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCerrar.setFocusPainted(false);
		btnCerrar.addActionListener(e -> dispose());
		buttonPane.add(btnCerrar);
		
		cargarDatos();
	}
	
	private void cargarDatos() {
		if(miVacuna != null) {
			txtNombre.setText(miVacuna.getNombre());
			spinEdadMin.setValue(miVacuna.getEdadMinima());
			txtFabricante.setText(miVacuna.getFabricante());
			
			if(miVacuna.getEnfermedad() != null) {
				cbEnfermedad.setSelectedItem(miVacuna.getEnfermedad());
			}
		}
	}
	
	private void modificarVacuna() {
		if(txtNombre.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe ingresar el nombre de la vacuna.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(txtFabricante.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe ingresar el fabricante.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		int edadMin = (int) spinEdadMin.getValue();
		
		miVacuna.setNombre(txtNombre.getText().trim());
		miVacuna.setEdadMinima(edadMin);
		miVacuna.setFabricante(txtFabricante.getText().trim());
		miVacuna.setEnfermedad((Enfermedad) cbEnfermedad.getSelectedItem());
		
		//ListarVacuna.loadVacunas();
		JOptionPane.showMessageDialog(null, "Vacuna modificada con �xito.", "Modificaci�n Exitosa", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}
	
	private void registrarVacuna() {
		if(txtNombre.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe ingresar el nombre de la vacuna.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(txtFabricante.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe ingresar el fabricante.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		int edadMin = (int) spinEdadMin.getValue();
		
		Enfermedad enfermedad = (Enfermedad) cbEnfermedad.getSelectedItem();
		String id = "VAC-" + Clinica.genCodigoVacuna;
		String nombre = txtNombre.getText().trim();
		String fabricante = txtFabricante.getText().trim();
		
		Vacuna vacuna = new Vacuna(id, nombre, enfermedad, edadMin);
		vacuna.setFabricante(fabricante);
		
		Clinica.getInstancia().getVacunas().add(vacuna);
		Clinica.genCodigoVacuna++;
		
		limpiarCampos();
		JOptionPane.showMessageDialog(null, "Vacuna registrada con �xito.", "�xito", JOptionPane.INFORMATION_MESSAGE);
	}

	private void limpiarCampos() {
		txtNombre.setText("");
		spinEdadMin.setValue(0);
		txtFabricante.setText("");
		cbEnfermedad.setSelectedIndex(-1);
	}
}