package visual.consultorio;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

import bd.catalogo.PacienteDAO;
import logico.Clinica;
import logico.consultorio.Paciente;

public class ListarPaciente extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private DefaultTableModel model;
	private Object[] row;
	private Paciente auxPaciente = null;

	private JTextField txtBuscar;
	private JTable table;
	private JPanel panelBarra;
	private JPanel panelTable;

	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnCancelar;

	public static void main(String[] args) {
		try {
			ListarPaciente dialog = new ListarPaciente();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListarPaciente() {
		setTitle("Listado de Pacientes");
		setBounds(100, 100, 850, 540);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		panelBarra = new JPanel();
		panelBarra.setBackground(Color.WHITE);
		panelBarra.setBorder(new LineBorder(new Color(70, 130, 180)));
		panelBarra.setBounds(28, 20, 775, 60);
		panelBarra.setLayout(null);
		contentPanel.add(panelBarra);

		JLabel lblBuscar = new JLabel("Buscar:");
		lblBuscar.setForeground(new Color(70, 130, 180));
		lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblBuscar.setBounds(16, 15, 70, 30);
		panelBarra.add(lblBuscar);

		txtBuscar = new JTextField();
		txtBuscar.setToolTipText("Filtrar por código, nombre, apellido o cédula");
		txtBuscar.setBounds(90, 16, 660, 28);
		txtBuscar.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtrarTabla(txtBuscar.getText());
			}
		});
		panelBarra.add(txtBuscar);

		panelTable = new JPanel();
		panelTable.setBounds(28, 95, 775, 330);
		panelTable.setBorder(new LineBorder(new Color(70, 130, 180)));
		contentPanel.add(panelTable);
		panelTable.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollTabla = new JScrollPane();
		panelTable.add(scrollTabla, BorderLayout.CENTER);

		table = new JTable();
		scrollTabla.setViewportView(table);

		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		String[] headers = {"Código", "Nombre", "Cédula", "Edad", "Teléfono"};
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

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index > -1) {
					String id = table.getValueAt(index, 0).toString();
					auxPaciente = Clinica.getInstancia().buscarPacienteXId(id);
					if (auxPaciente != null) {
						btnModificar.setEnabled(true);
						btnEliminar.setEnabled(true);
					}
				}
			}
		});

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(Color.WHITE);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnEliminar.setForeground(new Color(70, 130, 180));
		btnEliminar.setBackground(new Color(255, 245, 238));
		btnEliminar.setFocusPainted(false);
		btnEliminar.setEnabled(false);
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (auxPaciente != null) {
					int option = JOptionPane.showConfirmDialog(
							null,
							"¿Está seguro que desea eliminar al paciente: " + auxPaciente.getNombre() + " " + auxPaciente.getApellido() + "?",
							"Confirmación",
							JOptionPane.WARNING_MESSAGE
					);

					if (option == JOptionPane.OK_OPTION) {
						PacienteDAO.getInstance().eliminarPaciente(auxPaciente.getIdNumber());
						Clinica.getInstancia().getPacientes().remove(auxPaciente);

						auxPaciente = null;
						btnEliminar.setEnabled(false);
						btnModificar.setEnabled(false);
						filtrarTabla(txtBuscar.getText());
						JOptionPane.showMessageDialog(null,
								"Paciente eliminado exitosamente.",
								"Éxito",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		buttonPane.add(btnEliminar);

		btnModificar = new JButton("Ver Detalles");
		btnModificar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnModificar.setForeground(new Color(70, 130, 180));
		btnModificar.setBackground(new Color(255, 245, 238));
		btnModificar.setEnabled(false);
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (auxPaciente != null) {
					CrearPaciente modPaciente = new CrearPaciente(auxPaciente);
					modPaciente.setModal(true);
					modPaciente.setVisible(true);
					filtrarTabla(txtBuscar.getText());
				}
			}
		});
		buttonPane.add(btnModificar);

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

		filtrarTabla("");
	}

	private int calcularEdad(Date fecNac) {
		if (fecNac == null) return 0;
		Calendar nacimiento = Calendar.getInstance();
		nacimiento.setTime(fecNac);
		Calendar hoy = Calendar.getInstance();
		int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);
		if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
			edad--;
		}
		return edad;
	}

	private void filtrarTabla(String filtro) {
		model.setRowCount(0);
		String f = filtro != null ? filtro.toLowerCase().trim() : "";

		ArrayList<Paciente> pacientes = Clinica.getInstancia().getPacientes();
		if (pacientes != null) {
			for (Paciente p : pacientes) {
				if (p != null) {
					String id = p.getId() != null ? p.getId().toLowerCase() : "";
					String nombreComp = ((p.getNombre() != null ? p.getNombre() : "") + " " + (p.getApellido() != null ? p.getApellido() : "")).toLowerCase();
					String cedula = p.getCedula() != null ? p.getCedula().toLowerCase() : "";

					if (f.isEmpty() || id.contains(f) || nombreComp.contains(f) || cedula.contains(f)) {
						row = new Object[5];
						row[0] = p.getId();
						row[1] = p.getNombre() + (p.getApellido() != null ? " " + p.getApellido() : "");
						row[2] = p.getCedula();
						row[3] = calcularEdad(p.getFecNacim());
						row[4] = p.getTelefono();
						model.addRow(row);
					}
				}
			}
		}
		auxPaciente = null;
		if (btnModificar != null) btnModificar.setEnabled(false);
		if (btnEliminar != null) btnEliminar.setEnabled(false);
	}
}