package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import logico.Clinica;
import logico.Paciente;
import visual.registro.RegistrarPaciente;

public class MostrarPaciente extends JDialog {
	
	public static void main(String[] args) {
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                MostrarPaciente frame = new MostrarPaciente();
	                frame.setVisible(true);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private static DefaultTableModel model;
	private static Object[] row;
	private JTextField txtFiltroNombre;
	private JTextField txtFiltroCedula;
	private static JButton btnModificar;
	private static JButton btnEliminar;
	private Paciente auxPaciente = null;

	public MostrarPaciente() {
		setTitle("Listado de Pacientes");
		setBounds(100, 100, 900, 588);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(240, 248, 255));

		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setLayout(null);

		JPanel panelBarra = new JPanel();
		panelBarra.setBackground(Color.WHITE);
		panelBarra.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		panelBarra.setBounds(15, 16, 853, 72);
		panelBarra.setLayout(null);
		getContentPane().add(panelBarra);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Verdana", Font.BOLD, 13));
		lblNombre.setForeground(new Color(70, 130, 180));
		lblNombre.setBounds(15, 20, 96, 20);
		panelBarra.add(lblNombre);

		txtFiltroNombre = new JTextField();
		txtFiltroNombre.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		txtFiltroNombre.setBackground(new Color(224, 247, 250));
		txtFiltroNombre.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtFiltroNombre.setBounds(84, 17, 250, 26);
		txtFiltroNombre.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				aplicarFiltros();
			}
		});
		panelBarra.add(txtFiltroNombre);

		JLabel lblCedula = new JLabel("C�dula:");
		lblCedula.setFont(new Font("Verdana", Font.BOLD, 13));
		lblCedula.setForeground(new Color(70, 130, 180));
		lblCedula.setBounds(349, 20, 96, 20);
		panelBarra.add(lblCedula);

		txtFiltroCedula = new JTextField();
		txtFiltroCedula.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		txtFiltroCedula.setBackground(new Color(224, 247, 250));
		txtFiltroCedula.setBorder(new LineBorder(new Color(173, 216, 230), 1));
		txtFiltroCedula.setBounds(410, 17, 250, 26);
		txtFiltroCedula.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				aplicarFiltros();
			}
		});
		panelBarra.add(txtFiltroCedula);

		JButton btnMostrarTodos = new JButton("Mostrar Todos");
		btnMostrarTodos.setForeground(new Color(70, 130, 180));
		btnMostrarTodos.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnMostrarTodos.setBackground(new Color(176, 224, 230));
		btnMostrarTodos.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnMostrarTodos.setBounds(688, 17, 150, 26);
		btnMostrarTodos.addActionListener(e -> {
			txtFiltroNombre.setText("");
			txtFiltroCedula.setText("");
			loadPacientes("", "");
		});
		panelBarra.add(btnMostrarTodos);

		JPanel panelTable = new JPanel();
		panelTable.setBackground(new Color(240, 248, 255));
		panelTable.setBounds(15, 104, 853, 350);
		panelTable.setLayout(new BorderLayout(0, 0));
		getContentPane().add(panelTable);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		panelTable.add(scrollPane, BorderLayout.CENTER);

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};

		table = new JTable();
		String[] headers = {
				"C�digo",
				"Nombre",
				"C�dula",
				"Edad",
				"Tel�fono"
		};

		model.setColumnIdentifiers(headers);
		table.setModel(model);
		scrollPane.setViewportView(table);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		table.setBackground(Color.WHITE);
		table.setSelectionBackground(new Color(176, 224, 230));
		table.setSelectionForeground(new Color(70, 130, 180));
		table.setGridColor(new Color(173, 216, 230));
		table.getTableHeader().setFont(new Font("Bahnschrift", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(135, 206, 235));
		table.getTableHeader().setForeground(new Color(70, 130, 180));
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if(index > -1) {
					btnModificar.setEnabled(true);
					btnEliminar.setEnabled(true);
					String idPaciente = table.getValueAt(index, 0).toString();
					auxPaciente = Clinica.getInstancia().buscarPacienteXId(idPaciente);
				}
			}
		});

		JPanel panelBotones = new JPanel();
		panelBotones.setBackground(new Color(240, 248, 255));
		panelBotones.setBounds(15, 500, 853, 40);
		panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(panelBotones);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnEliminar.setBackground(new Color(176, 224, 230));
		btnEliminar.setForeground(new Color(70, 130, 180));
		btnEliminar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnEliminar.setEnabled(false);
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(auxPaciente != null) {
					int option = JOptionPane.showConfirmDialog(
						null, 
						"�Est� seguro que desea eliminar al paciente: " + auxPaciente.getNombre() + "?",
						"Confirmaci�n", 
						JOptionPane.WARNING_MESSAGE
					);
					if(option == JOptionPane.OK_OPTION) {
						Clinica.getInstancia().getPacientes().remove(auxPaciente);
						btnEliminar.setEnabled(false);
						btnModificar.setEnabled(false);
						loadPacientes("", "");
						JOptionPane.showMessageDialog(null, 
							"Paciente eliminado exitosamente.", 
							"�xito", 
							JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		panelBotones.add(btnEliminar);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnModificar.setBackground(new Color(176, 224, 230));
		btnModificar.setForeground(new Color(70, 130, 180));
		btnModificar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnModificar.setEnabled(false);
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(auxPaciente != null) {
					RegistrarPaciente modPaciente = new RegistrarPaciente(auxPaciente);
					modPaciente.setModal(true);
					modPaciente.setVisible(true);
				}
			}
		});
		panelBotones.add(btnModificar);

		JButton btnDetalle = new JButton("Ver Detalle");
		btnDetalle.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnDetalle.setBackground(new Color(176, 224, 230));
		btnDetalle.setForeground(new Color(70, 130, 180));
		btnDetalle.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnDetalle.addActionListener(e -> {
			int fila = table.getSelectedRow();
			if (fila == -1) {
				JOptionPane.showMessageDialog(
					null,
					"Debe seleccionar un paciente de la lista.",
					"Advertencia",
					JOptionPane.WARNING_MESSAGE
				);
				return;
			}

			String idPaciente = table.getValueAt(fila, 0).toString();
			Paciente paciente = Clinica.getInstancia().buscarPacienteXId(idPaciente);

			if (paciente == null) {
				JOptionPane.showMessageDialog(null,
					"No se encontr� el paciente seleccionado.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
				return;
			}

			DetallePaciente detalle = new DetallePaciente(paciente);
			detalle.setModal(true);
			detalle.setLocationRelativeTo(null);
			detalle.setVisible(true);
		});
		panelBotones.add(btnDetalle);
		
		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnCerrar.setBackground(new Color(176, 224, 230));
		btnCerrar.setForeground(new Color(70, 130, 180));
		btnCerrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
		btnCerrar.addActionListener(e -> dispose());
		panelBotones.add(btnCerrar);

		loadPacientes("", "");
	}

	private void aplicarFiltros() {
		loadPacientes(
			txtFiltroNombre.getText().trim(),
			txtFiltroCedula.getText().trim()
		);
	}

	private int calcularEdad(java.util.Date date) {
	    Calendar nacimiento = Calendar.getInstance();
	    nacimiento.setTime(date);

	    Calendar hoy = Calendar.getInstance();

	    int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);

	    if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
	        edad--;
	    }

	    return edad;
	}

	public static void loadPacientes() {
		loadPacientes("", "");
	}
	
	private static void loadPacientes(String nombreFiltro, String cedulaFiltro) {
		model.setRowCount(0);
		row = new Object[model.getColumnCount()];

		ArrayList<Paciente> pacientes = Clinica.getInstancia().getPacientes();

		for (Paciente p : pacientes) {
			boolean coincideNombre = p.getNombre().toLowerCase()
					.contains(nombreFiltro.toLowerCase());

			boolean coincideCedula = p.getCedula().toLowerCase()
					.contains(cedulaFiltro.toLowerCase());

			if (coincideNombre && coincideCedula) {
				row[0] = p.getIdPaciente();
				row[1] = p.getNombre();
				row[2] = p.getCedula();
				
				Calendar nacimiento = Calendar.getInstance();
				nacimiento.setTime(p.getFecNacim());
				Calendar hoy = Calendar.getInstance();
				int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);
				if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
					edad--;
				}
				
				row[3] = edad;
				row[4] = p.getTelefono();

				model.addRow(row);
			}
		}
		
		if(btnModificar != null) btnModificar.setEnabled(false);
		if(btnEliminar != null) btnEliminar.setEnabled(false);
	}
}