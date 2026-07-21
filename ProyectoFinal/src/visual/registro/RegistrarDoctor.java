package visual.registro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controllers.DoctorController;
import logico.Especialidad;
import utilidad.Formato;

public class RegistrarDoctor extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JComboBox<String> cbxSexo;
	private JTextField txtTelefono;
	private JSpinner spnCupoDiario;
	
	private DoctorController controller;
	private JList<Especialidad> listEspecialidades;
	private DefaultListModel<Especialidad> listModel;

	public RegistrarDoctor() {
		setTitle("Registrar Doctor");
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 340, 440);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Datos del Doctor", TitledBorder.CENTER, TitledBorder.TOP, new Font("Verdana", Font.BOLD, 12), new Color(70, 130, 180)));
		panel.setBackground(Color.WHITE);
		contentPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(23, 27, 80, 20);
		lblNombre.setForeground(new Color(70, 130, 180));
		lblNombre.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setFont(new Font("Verdana", Font.PLAIN, 10));
		txtNombre.setBackground(new Color(224, 247, 250));
		txtNombre.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtNombre.setBounds(23, 49, 250, 15);
		panel.add(txtNombre);
		txtNombre.setColumns(10);
		
		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setBounds(23, 67, 43, 20);
		lblSexo.setForeground(new Color(70, 130, 180));
		lblSexo.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblSexo);

		cbxSexo = new JComboBox<>();
		cbxSexo.setBounds(23, 86, 40, 15);
		cbxSexo.setFont(new Font("Verdana", Font.PLAIN, 10));
		cbxSexo.setBackground(new Color(224, 247, 250));
		cbxSexo.setModel(new DefaultComboBoxModel<>(new String[] {"F", "M"}));
		panel.add(cbxSexo);
		
		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setBounds(85, 68, 66, 20);
		lblTelefono.setForeground(new Color(70, 130, 180));
		lblTelefono.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblTelefono);
		
		txtTelefono = new JTextField();
		txtTelefono.setFont(new Font("Verdana", Font.PLAIN, 10));
		txtTelefono.setBackground(new Color(224, 247, 250));
		txtTelefono.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtTelefono.setBounds(85, 86, 91, 15);
		panel.add(txtTelefono);
		txtTelefono.setColumns(10);
		
		JLabel lblCupoDiario = new JLabel("Cupo Diario:");
		lblCupoDiario.setForeground(new Color(70, 130, 180));
		lblCupoDiario.setFont(new Font("Verdana", Font.BOLD, 10));
		lblCupoDiario.setBounds(193, 64, 80, 28);
		panel.add(lblCupoDiario);
		
		spnCupoDiario = new JSpinner();
		spnCupoDiario.setModel(new SpinnerNumberModel(1, 1, 30, 1));
		spnCupoDiario.setFont(new Font("Verdana", Font.PLAIN, 10));
		spnCupoDiario.setBackground(new Color(224, 247, 250));
		Formato.setSpinner(spnCupoDiario);
		spnCupoDiario.setBounds(193, 86, 37, 15);
		panel.add(spnCupoDiario);
		
		JLabel lblEspecialidades = new JLabel("Especialidades:");
		lblEspecialidades.setForeground(new Color(70, 130, 180));
		lblEspecialidades.setFont(new Font("Verdana", Font.BOLD, 10));
		lblEspecialidades.setBounds(23, 115, 120, 20);
		panel.add(lblEspecialidades);
		
		controller = new DoctorController();
		listModel = new DefaultListModel<>();
		
		List<Especialidad> especialidades = controller.obtenerTodasLasEspecialidades();
		for (Especialidad esp : especialidades) {
			listModel.addElement(esp);
		}
		
		listEspecialidades = new JList<>(listModel);
		listEspecialidades.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		JScrollPane scrollPaneEspecialidades = new JScrollPane(listEspecialidades);
		scrollPaneEspecialidades.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollPaneEspecialidades.setBounds(23, 138, 250, 150);
		panel.add(scrollPaneEspecialidades);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 248, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPanel.add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.setFont(new Font("Verdana", Font.BOLD, 10));
		btnRegistrar.setBackground(new Color(176, 224, 230));
		btnRegistrar.setForeground(new Color(70, 130, 180));
		btnRegistrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnRegistrar.setFocusPainted(false);
		btnRegistrar.setActionCommand("OK");
		buttonPane.add(btnRegistrar);
		getRootPane().setDefaultButton(btnRegistrar);
		
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarDatos();
			}
		});
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Verdana", Font.BOLD, 10));
		btnCancelar.setBackground(new Color(176, 224, 230));
		btnCancelar.setForeground(new Color(70, 130, 180));
		btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCancelar.setFocusPainted(false);
		btnCancelar.setActionCommand("Cancel");
		buttonPane.add(btnCancelar);
		
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	private void guardarDatos() {
		String nombre = txtNombre.getText();
		String sexo = cbxSexo.getSelectedItem().toString();
		String telefono = txtTelefono.getText();
		int cupo = ((Number) spnCupoDiario.getValue()).intValue();
		List<Especialidad> seleccionadas = listEspecialidades.getSelectedValuesList();
		
		boolean exito = controller.guardarDoctor(nombre, sexo, telefono, cupo, seleccionadas);
		
		if (exito) {
			JOptionPane.showMessageDialog(null, "¡Doctor registrado con éxito!", "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	}
}