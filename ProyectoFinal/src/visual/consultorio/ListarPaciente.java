package visual.consultorio;

import bd.ConexionBD;
import bd.catalogo.PacienteDAO;

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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

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

public class ListarPaciente extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private DefaultTableModel model;

	// Sustituyendo el objeto auxiliar por IDs nativos
	private int idPacienteSeleccionado = -1;
	private String nombrePacienteSeleccionado = "";

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
		txtBuscar.setToolTipText("Filtrar por nombre, apellido o cédula");
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

		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		String[] headers = {"Código", "Nombre", "Cédula", "Teléfono", "Tipo Sangre", "Peso (Kg)"};
		model.setColumnIdentifiers(headers);

		table = new JTable(model);
		scrollTabla.setViewportView(table);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		table.setBackground(Color.WHITE);
		table.setSelectionBackground(new Color(176, 224, 230));
		table.setSelectionForeground(new Color(70, 130, 180));
		table.setGridColor(new Color(173, 216, 230));
		table.getTableHeader().setFont(new Font("Bahnschrift", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(135, 206, 235));
		table.getTableHeader().setForeground(new Color(70, 130, 180));

		// Ajuste visual de columnas
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(80);
		table.getColumnModel().getColumn(5).setPreferredWidth(80);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index > -1) {
					// Capturar variables directo del JTable
					idPacienteSeleccionado = (int) table.getValueAt(index, 0);
					nombrePacienteSeleccionado = table.getValueAt(index, 1).toString();

					btnModificar.setEnabled(true);
					btnEliminar.setEnabled(true);
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
				if (idPacienteSeleccionado != -1) {
					int option = JOptionPane.showConfirmDialog(
							null,
							"¿Está seguro que desea eliminar al paciente: " + nombrePacienteSeleccionado + "?",
							"Confirmación",
							JOptionPane.WARNING_MESSAGE
					);

					if (option == JOptionPane.OK_OPTION) {

						// Recibimos la confirmación del DAO
						boolean exito = PacienteDAO.getInstance().eliminarPaciente(idPacienteSeleccionado);

						if (exito) {
							idPacienteSeleccionado = -1;
							nombrePacienteSeleccionado = "";
							btnEliminar.setEnabled(false);
							btnModificar.setEnabled(false);
							filtrarTabla(txtBuscar.getText());
							JOptionPane.showMessageDialog(null,
									"Paciente eliminado exitosamente.",
									"Éxito",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null,
									"No se pudo eliminar al paciente. Es posible que tenga consultas, citas o historiales asociados en el sistema.",
									"Error de Eliminación",
									JOptionPane.ERROR_MESSAGE);
						}
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
				if (idPacienteSeleccionado != -1) {

					// 1. Buscamos el objeto Paciente completo en la memoria usando el ID
					logico.consultorio.Paciente pacienteEditar = null;
					if (logico.Clinica.getInstancia().getPacientes() != null) {
						for (logico.consultorio.Paciente p : logico.Clinica.getInstancia().getPacientes()) {
							// Verifica si tu método es getId() o getIdNumber() y ajústalo si marca error aquí
							if (p.getIdNumber() == idPacienteSeleccionado) {
								pacienteEditar = p;
								break;
							}
						}
					}


					if (pacienteEditar != null) {
						CrearPaciente modPaciente = new CrearPaciente(pacienteEditar);
						modPaciente.setModal(true);
						modPaciente.setLocationRelativeTo(ListarPaciente.this);
						modPaciente.setVisible(true);

						// Al cerrar la ventana, recargamos la tabla por si cambiaste algo
						filtrarTabla(txtBuscar.getText());
						btnModificar.setEnabled(false);
						btnEliminar.setEnabled(false);
						idPacienteSeleccionado = -1;
					} else {
						JOptionPane.showMessageDialog(null,
								"No se pudo cargar la información completa del paciente.",
								"Error de Carga",
								JOptionPane.ERROR_MESSAGE);
					}
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

	private void filtrarTabla(String filtro) {
		model.setRowCount(0);
		String sql = "{call str_listar_buscar_paciente(?)}";

		try (Connection conn = ConexionBD.getConnection();
			 CallableStatement stmt = conn.prepareCall(sql)) {

			stmt.setString(1, filtro);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("id_paciente");
					String nombre = rs.getString("nombre");
					String apellido = rs.getString("apellido");
					String cedula = rs.getString("cedula");
					String telefono = rs.getString("telefono");
					String tipoSangre = rs.getString("tipo_sangre");
					double peso = rs.getDouble("peso");

					model.addRow(new Object[]{
							id,
							nombre + " " + apellido,
							cedula,
							telefono,
							(tipoSangre != null) ? tipoSangre : "N/A",
							(peso > 0) ? peso : "N/A"
					});
				}
			}
		} catch (Exception e) {
			System.err.println("ERROR: Fallo al cargar los pacientes desde la base de datos.");
			e.printStackTrace();
		}

		idPacienteSeleccionado = -1;
		if (btnModificar != null) btnModificar.setEnabled(false);
		if (btnEliminar != null) btnEliminar.setEnabled(false);
	}
}