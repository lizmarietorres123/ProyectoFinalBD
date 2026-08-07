package visual.standby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controllers.EspecialidadController;
import logico.catalogo.Enfermedad;

public class RegistrarEspecialidad extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextField txtAreaMedica;
	private JTextArea txtDescripcion;
	
	private EspecialidadController controller;
	private JList<Enfermedad> listEnfermedades;
	private DefaultListModel<Enfermedad> listModel;

	public static void main(String[] args) {
		try {
			RegistrarEspecialidad dialog = new RegistrarEspecialidad();
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public RegistrarEspecialidad() {
		setTitle("Registrar Especialidad");
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 328, 302);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setForeground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Especialidad", TitledBorder.CENTER, TitledBorder.TOP, new Font("Verdana", Font.BOLD, 11), new Color(70, 130, 180)));
		panel.setForeground(Color.BLACK);
		panel.setBackground(Color.WHITE);
		contentPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(20, 28, 80, 20);
		lblNombre.setForeground(new Color(70, 130, 180));
		lblNombre.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setFont(new Font("Verdana", Font.PLAIN, 10));
		txtNombre.setBackground(new Color(224, 247, 250));
		txtNombre.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtNombre.setBounds(107, 30, 173, 15);
		panel.add(txtNombre);
		txtNombre.setColumns(10);
		
		JLabel lblNombre_1 = new JLabel("Area Medica:");
		lblNombre_1.setForeground(new Color(70, 130, 180));
		lblNombre_1.setFont(new Font("Verdana", Font.BOLD, 10));
		lblNombre_1.setBounds(20, 58, 80, 20);
		panel.add(lblNombre_1);
		
		txtAreaMedica = new JTextField();
		txtAreaMedica.setFont(new Font("Verdana", Font.PLAIN, 10));
		txtAreaMedica.setColumns(10);
		txtAreaMedica.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtAreaMedica.setBackground(new Color(224, 247, 250));
		txtAreaMedica.setBounds(107, 61, 173, 15);
		panel.add(txtAreaMedica);
		
		JLabel lblNombre_1_1 = new JLabel("Descripción:");
		lblNombre_1_1.setForeground(new Color(70, 130, 180));
		lblNombre_1_1.setFont(new Font("Verdana", Font.BOLD, 10));
		lblNombre_1_1.setBounds(20, 90, 80, 20);
		panel.add(lblNombre_1_1);
		
		JScrollPane scrollDescripcion = new JScrollPane();
		scrollDescripcion.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		scrollDescripcion.setBounds(107, 90, 173, 45);
		panel.add(scrollDescripcion);
		
		txtDescripcion = new JTextArea();
		txtDescripcion.setLineWrap(true);
		txtDescripcion.setWrapStyleWord(true);
		txtDescripcion.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtDescripcion.setBackground(new Color(224, 247, 250));
		scrollDescripcion.setViewportView(txtDescripcion);
		
		JLabel lblNombre_1_1_1 = new JLabel("Enfermedades:");
		lblNombre_1_1_1.setForeground(new Color(70, 130, 180));
		lblNombre_1_1_1.setFont(new Font("Verdana", Font.BOLD, 10));
		lblNombre_1_1_1.setBounds(20, 162, 92, 20);
		panel.add(lblNombre_1_1_1);
		
		controller = new EspecialidadController();
		listModel = new DefaultListModel<>();
	
		List<Enfermedad> enfermedades = controller.listEnfermedades();
		for (Enfermedad enf : enfermedades) {
		    listModel.addElement(enf);
		}

		listEnfermedades = new JList<>(listModel);
		listEnfermedades.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scrollPaneEnfermedades = new JScrollPane(listEnfermedades);
		scrollPaneEnfermedades.setBounds(107, 163, 173, 45); 
		panel.add(scrollPaneEnfermedades);
		
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
				String nombre = txtNombre.getText();
				String areaMedica = txtAreaMedica.getText();
				String descripcion = txtDescripcion.getText();
				List<Enfermedad> seleccionadas = listEnfermedades.getSelectedValuesList();

				boolean exito = controller.guardarEspecialidad(nombre, areaMedica, descripcion, seleccionadas);

				if (exito) {
					JOptionPane.showMessageDialog(null, "¡Especialidad registrada con éxito!", "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
					dispose(); 
				}
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
}