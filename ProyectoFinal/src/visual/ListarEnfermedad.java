package visual;

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
import logico.Enfermedad;
import visual.registro.RegEnfermedad;

public class ListarEnfermedad extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private static DefaultTableModel model;
	private static Object[] row;
	private Enfermedad auxEnfermedad = null;

	private static JTextField txtBuscar;
	private JTable table;
	private JPanel panelBarra;
	private JPanel panelTable;

	private static JButton btnModificar;
	private static JButton btnEliminar;
	private JButton btnCancelar;

	public static void main(String[] args) {
		try {
			ListarEnfermedad dialog = new ListarEnfermedad();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListarEnfermedad() {
		setTitle("Listado de Enfermedades");
		setBounds(100, 100, 818, 541);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		panelBarra = new JPanel();
		panelBarra.setBackground(Color.WHITE);
		panelBarra.setBorder(new LineBorder(new Color(70, 130, 180)));
		panelBarra.setBounds(28, 20, 738, 60);
		panelBarra.setLayout(null);
		contentPanel.add(panelBarra);

		JLabel lblBuscar = new JLabel("Buscar:");
		lblBuscar.setForeground(new Color(70, 130, 180));
		lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 14));
		lblBuscar.setBounds(16, 15, 70, 30);
		panelBarra.add(lblBuscar);

		txtBuscar = new JTextField();
		txtBuscar.setToolTipText("Filtrar por código o nombre de la enfermedad");
		txtBuscar.setBounds(90, 16, 625, 28);
		txtBuscar.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtrarTabla(txtBuscar.getText());
			}
		});
		panelBarra.add(txtBuscar);

		panelTable = new JPanel();
		panelTable.setBounds(28, 95, 738, 330);
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

		String[] headers = {"Código", "Nombre", "En Vigilancia", "Casos Reportados"};
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
					auxEnfermedad = Clinica.getInstancia().buscarEnfermedadXId(id);
					if (auxEnfermedad != null) {
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
				if (auxEnfermedad != null) {
					int option = JOptionPane.showConfirmDialog(
							null,
							"¿Está seguro que desea eliminar la enfermedad: " + auxEnfermedad.getNombre() + "?",
							"Confirmación",
							JOptionPane.WARNING_MESSAGE
					);
					if (option == JOptionPane.OK_OPTION) {
						Clinica.getInstancia().getEnfermedades().remove(auxEnfermedad);
						auxEnfermedad = null;
						loadEnfermedades();
						JOptionPane.showMessageDialog(null,
								"Enfermedad eliminada exitosamente.",
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
				if (auxEnfermedad != null) {
					RegEnfermedad modEnfermedad = new RegEnfermedad(auxEnfermedad);
					modEnfermedad.setModal(true);
					modEnfermedad.setVisible(true);
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

		loadEnfermedades();
	}

	public static void loadEnfermedades() {
		filtrarTabla(txtBuscar != null ? txtBuscar.getText() : "");
	}

	private static void filtrarTabla(String filtro) {
		if (model == null) return;
		model.setRowCount(0);
		String f = filtro != null ? filtro.toLowerCase().trim() : "";

		ArrayList<Enfermedad> enfermedades = Clinica.getInstancia().getEnfermedades();
		if (enfermedades != null) {
			for (Enfermedad enf : enfermedades) {
				if (enf != null) {
					String id = enf.getId() != null ? enf.getId().toLowerCase() : "";
					String nombre = enf.getNombre() != null ? enf.getNombre().toLowerCase() : "";

					if (f.isEmpty() || id.contains(f) || nombre.contains(f)) {
						row = new Object[4];
						row[0] = enf.getId();
						row[1] = enf.getNombre();
						row[2] = enf.isVigilancia() ? "Sí" : "No";
						row[3] = enf.getCasosReportados();
						model.addRow(row);
					}
				}
			}
		}
		if (btnModificar != null) btnModificar.setEnabled(false);
		if (btnEliminar != null) btnEliminar.setEnabled(false);
	}
}