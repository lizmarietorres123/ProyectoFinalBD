package visual.standby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import logico.consultorio.Clinica;
import logico.catalogo.Enfermedad;

public class RegEnfermedad extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextArea txtSintomas;
	private JTextArea txtDescripcion;
	private JCheckBox cbVigilancia;
	private Enfermedad miEnfermedad = null;

	public static void main(String[] args) {
		try {
			RegEnfermedad dialog = new RegEnfermedad(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RegEnfermedad(Enfermedad enf) {
		miEnfermedad = enf;
		
		if(miEnfermedad == null) {
			setTitle("Registrar Enfermedad");
		} else {
			setTitle("Modificar Enfermedad");
		}
		
		setBounds(100, 100, 600, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Datos de la Enfermedad", TitledBorder.CENTER, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(70, 130, 180)));
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		panel.setBounds(12, 13, 560, 330);
		contentPanel.add(panel);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setForeground(new Color(70, 130, 180));
		lblNombre.setFont(new Font("Verdana", Font.BOLD, 12));
		lblNombre.setBounds(20, 40, 120, 20);
		panel.add(lblNombre);
		
		txtNombre = new JTextField();
		txtNombre.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtNombre.setBackground(new Color(224, 247, 250));
		txtNombre.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtNombre.setBounds(140, 40, 380, 22);
		panel.add(txtNombre);
		
		cbVigilancia = new JCheckBox("Bajo vigilancia");
		cbVigilancia.setFont(new Font("Verdana", Font.BOLD, 12));
		cbVigilancia.setForeground(new Color(70, 130, 180));
		cbVigilancia.setBackground(Color.WHITE);
		cbVigilancia.setBounds(20, 75, 250, 25);
		panel.add(cbVigilancia);
		
		JLabel lblSintomas = new JLabel("Sintomas:");
		lblSintomas.setForeground(new Color(70, 130, 180));
		lblSintomas.setFont(new Font("Verdana", Font.BOLD, 12));
		lblSintomas.setBounds(20, 115, 120, 20);
		panel.add(lblSintomas);
		
		txtSintomas = new JTextArea();
		txtSintomas.setLineWrap(true);
		txtSintomas.setWrapStyleWord(true);
		txtSintomas.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtSintomas.setBackground(new Color(224, 247, 250));
		JScrollPane scrollSintomas = new JScrollPane(txtSintomas);
		scrollSintomas.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollSintomas.setBounds(140, 115, 380, 70);
		panel.add(scrollSintomas);
		
		JLabel lblDescripcion = new JLabel("Descripcion:");
		lblDescripcion.setForeground(new Color(70, 130, 180));
		lblDescripcion.setFont(new Font("Verdana", Font.BOLD, 12));
		lblDescripcion.setBounds(20, 205, 120, 20);
		panel.add(lblDescripcion);
		
		txtDescripcion = new JTextArea();
		txtDescripcion.setLineWrap(true);
		txtDescripcion.setWrapStyleWord(true);
		txtDescripcion.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtDescripcion.setBackground(new Color(224, 247, 250));
		JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
		scrollDescripcion.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollDescripcion.setBounds(140, 205, 380, 100);
		panel.add(scrollDescripcion);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setBackground(new Color(240, 248, 255));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnRegistrar = new JButton("Registrar");
		if(miEnfermedad != null) {
			btnRegistrar.setText("Modificar");
		}
		btnRegistrar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnRegistrar.setBackground(new Color(176, 224, 230));
		btnRegistrar.setForeground(new Color(70, 130, 180));
		btnRegistrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnRegistrar.setFocusPainted(false);
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(miEnfermedad != null) {
					modificarEnfermedad();
				} else {
					registrarEnfermedad();
				}
			}
		});
		buttonPane.add(btnRegistrar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnCancelar.setBackground(new Color(176, 224, 230));
		btnCancelar.setForeground(new Color(70, 130, 180));
		btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCancelar.setFocusPainted(false);
		btnCancelar.addActionListener(e -> dispose());
		buttonPane.add(btnCancelar);
		
		cargarDatos();
	}
	
	private void cargarDatos() {
		if(miEnfermedad != null) {
			txtNombre.setText(miEnfermedad.getNombre());
			cbVigilancia.setSelected(miEnfermedad.isVigilancia());
			txtSintomas.setText(miEnfermedad.getSintomas());
			txtDescripcion.setText(miEnfermedad.getDescripcion());
		}
	}
	
	private void modificarEnfermedad() {
		if (txtNombre.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Debe ingresar el nombre.", "Campo requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		miEnfermedad.setNombre(txtNombre.getText().trim());
		miEnfermedad.setVigilancia(cbVigilancia.isSelected());
		miEnfermedad.setSintomas(txtSintomas.getText().trim());
		miEnfermedad.setDescripcion(txtDescripcion.getText().trim());
		
		ListarEnfermedad.loadEnfermedades();
		JOptionPane.showMessageDialog(this, "Enfermedad modificada con exito.", "Modificacion Exitosa", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}

	private void registrarEnfermedad() {
		if (txtNombre.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Debe ingresar el nombre.", "Campo requerido", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String id = "ENF-" + Clinica.genCodigoEnfermedad;
		String nombre = txtNombre.getText().trim();
		boolean vigilancia = cbVigilancia.isSelected();
		String sintomas = txtSintomas.getText().trim();
		String descripcion = txtDescripcion.getText().trim();
		
		Enfermedad nueva = new Enfermedad(id, nombre, vigilancia, false, sintomas, descripcion);
		Clinica.getInstancia().registrarEnfermedad(nueva);
		
		JOptionPane.showMessageDialog(this, "Enfermedad registrada con exito.\nCodigo: " + id, "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
		limpiarCampos();
	}

	private void limpiarCampos() {
		txtNombre.setText("");
		cbVigilancia.setSelected(false);
		txtSintomas.setText("");
		txtDescripcion.setText("");
	}
}