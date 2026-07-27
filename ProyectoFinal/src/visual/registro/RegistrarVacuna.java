package visual.registro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import logico.Clinica;
import logico.Enfermedad;
import logico.Vacuna;
import java.awt.*;
import java.util.ArrayList;

public class RegistrarVacuna extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextField txtFabricante;
	private JList<Enfermedad> listEnfermedades;
	private DefaultListModel<Enfermedad> listModel;
	private Vacuna miVacuna = null;

	public RegistrarVacuna(Vacuna vac) {
		miVacuna = vac;

		if (miVacuna == null) {
			setTitle("Registrar Vacuna");
		} else {
			setTitle("Modificar Vacuna");
		}

		setBounds(100, 100, 500, 360);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panelRegistro = new JPanel();
		panelRegistro.setBackground(Color.WHITE);
		panelRegistro.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Datos de la Vacuna", TitledBorder.CENTER, TitledBorder.TOP, new Font("Bahnschrift", Font.BOLD, 14), new Color(70, 130, 180)));
		panelRegistro.setBounds(12, 13, 460, 250);
		contentPanel.add(panelRegistro);
		panelRegistro.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setForeground(new Color(70, 130, 180));
		lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblNombre.setBounds(20, 30, 100, 22);
		panelRegistro.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(130, 30, 300, 24);
		panelRegistro.add(txtNombre);

		JLabel lblEnfermedad = new JLabel("Enfermedades:");
		lblEnfermedad.setForeground(new Color(70, 130, 180));
		lblEnfermedad.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblEnfermedad.setBounds(20, 68, 100, 22);
		panelRegistro.add(lblEnfermedad);

		listModel = new DefaultListModel<>();
		for (Enfermedad e : Clinica.getInstancia().getEnfermedades()) {
			listModel.addElement(e);
		}
		listEnfermedades = new JList<>(listModel);
		listEnfermedades.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scrollPane = new JScrollPane(listEnfermedades);
		scrollPane.setBounds(130, 68, 300, 100);
		panelRegistro.add(scrollPane);

		JLabel lblFabricante = new JLabel("Fabricante:");
		lblFabricante.setForeground(new Color(70, 130, 180));
		lblFabricante.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		lblFabricante.setBounds(20, 180, 100, 22);
		panelRegistro.add(lblFabricante);

		txtFabricante = new JTextField();
		txtFabricante.setBounds(130, 180, 300, 24);
		panelRegistro.add(txtFabricante);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 248, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnRegistrar = new JButton(miVacuna != null ? "Modificar" : "Registrar");
		btnRegistrar.addActionListener(e -> {
			if (miVacuna != null) modificarVacuna();
			else registrarVacuna();
		});
		buttonPane.add(btnRegistrar);

		JButton btnCerrar = new JButton("Cancelar");
		btnCerrar.addActionListener(e -> dispose());
		buttonPane.add(btnCerrar);

		cargarDatos();
	}

	private void cargarDatos() {
		if (miVacuna != null) {
			txtNombre.setText(miVacuna.getNombre());
			txtFabricante.setText(miVacuna.getFabricante());

			if (miVacuna.getEnfermedades() != null) {
				for (int i = 0; i < listModel.size(); i++) {
					if (miVacuna.getEnfermedades().contains(listModel.get(i))) {
						listEnfermedades.addSelectionInterval(i, i);
					}
				}
			}
		}
	}

	private void modificarVacuna() {
		if (validarCampos()) {
			miVacuna.setNombre(txtNombre.getText().trim());
			miVacuna.setFabricante(txtFabricante.getText().trim());
			miVacuna.setEnfermedades(new ArrayList<>(listEnfermedades.getSelectedValuesList()));

			JOptionPane.showMessageDialog(null, "Vacuna modificada con éxito.");
			dispose();
		}
	}

	private void registrarVacuna() {
		if (validarCampos()) {
			String id = "VAC-" + Clinica.genCodigoVacuna;
			String nombre = txtNombre.getText().trim();
			String fabricante = txtFabricante.getText().trim();
			ArrayList<Enfermedad> seleccionadas = new ArrayList<>(listEnfermedades.getSelectedValuesList());

			Vacuna vacuna = new Vacuna(id, nombre, fabricante, seleccionadas);
			Clinica.getInstancia().getVacunas().add(vacuna);
			Clinica.genCodigoVacuna++;

			limpiarCampos();
			JOptionPane.showMessageDialog(null, "Vacuna registrada con éxito.");
		}
	}

	private boolean validarCampos() {
		if (txtNombre.getText().trim().isEmpty() || txtFabricante.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe completar todos los campos.");
			return false;
		}
		if (listEnfermedades.getSelectedValuesList().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar al menos una enfermedad.");
			return false;
		}
		return true;
	}

	private void limpiarCampos() {
		txtNombre.setText("");
		txtFabricante.setText("");
		listEnfermedades.clearSelection();
	}
}