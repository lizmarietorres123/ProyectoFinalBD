package visual.registro;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import controllers.DoctorController;
import logico.Doctor;
import logico.Especialidad;

public class RegistrarDoctor extends JDialog {

	private static final long serialVersionUID = 1L;

	private DoctorController controller;

	// Componentes gráficos del formulario
	private JTextField txtIdDoctor;
	private JTextField txtNombre;
	private JSpinner spnCupoDiario;
	private JList<Especialidad> listEspecialidades;
	private JButton btnGuardar;
	private JButton btnCancelar;

	public RegistrarDoctor() {
		controller = new DoctorController();

		setTitle("Registrar Doctor");
		setBounds(100, 100, 450, 420);
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout(10, 10));

		JPanel panelForm = new JPanel(new GridLayout(3, 2, 5, 5));

		panelForm.add(new JLabel(" ID Doctor:"));
		txtIdDoctor = new JTextField();
		panelForm.add(txtIdDoctor);

		panelForm.add(new JLabel(" Nombre Completo:"));
		txtNombre = new JTextField();
		panelForm.add(txtNombre);

		panelForm.add(new JLabel(" Cupo Diario:"));
		spnCupoDiario = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		panelForm.add(spnCupoDiario);

		getContentPane().add(panelForm, BorderLayout.NORTH);

		JPanel panelEspecialidades = new JPanel(new BorderLayout(5, 5));
		panelEspecialidades.add(new JLabel(" Seleccione Especialidad(es):"), BorderLayout.NORTH);

		List<Especialidad> especialidadesBD = controller.obtenerTodasLasEspecialidades();
		listEspecialidades = new JList<>(especialidadesBD.toArray(new Especialidad[0]));
		panelEspecialidades.add(new JScrollPane(listEspecialidades), BorderLayout.CENTER);

		getContentPane().add(panelEspecialidades, BorderLayout.CENTER);

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(e -> guardarDoctor());

		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(e -> dispose());

		panelBotones.add(btnGuardar);
		panelBotones.add(btnCancelar);
		getContentPane().add(panelBotones, BorderLayout.SOUTH);
	}

	private void guardarDoctor() {
		String id = txtIdDoctor.getText().trim();
		String nombre = txtNombre.getText().trim();
		int cupoDiario = (Integer) spnCupoDiario.getValue();
		List<Especialidad> seleccionadas = listEspecialidades.getSelectedValuesList();


		if (id.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Debe ingresar el ID del doctor.", "Validación", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (nombre.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del doctor.", "Validación", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (seleccionadas.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar al menos una especialidad.", "Validación", JOptionPane.WARNING_MESSAGE);
			return;
		}

		ArrayList<String> especialidadesNombres = new ArrayList<>();
		for (Especialidad esp : seleccionadas) {
			especialidadesNombres.add(esp.getNombre());
		}

		Doctor doctor = new Doctor(id, nombre, cupoDiario, especialidadesNombres);

		if (controller.guardarDoctor(doctor)) {
			JOptionPane.showMessageDialog(this, "Doctor registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "No se pudo registrar al doctor.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}