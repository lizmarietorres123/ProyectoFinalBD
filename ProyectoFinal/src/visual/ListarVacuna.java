package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.omg.CORBA.PUBLIC_MEMBER;

import logico.Clinica;
import logico.Enfermedad;
import logico.Paciente;
import logico.Vacuna;
import utilidad.Formato;
import visual.registro.RegistrarVacuna;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;

import javax.swing.DefaultListSelectionModel;
import java.util.List;
import java.util.PrimitiveIterator.OfDouble;
import java.util.Arrays;

public class ListarVacuna extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNomPac;
	private JComboBox cbxMostrar;
	private static DefaultTableModel model;
	private static Object[] row;
	private Paciente auxPaciente;
	private Vacuna vacSelect;
	private String auxOpcion;
	private JButton btnGuardar;
	private JButton btnCancelar;
	private Vacuna auxVacuna = null;
	private JButton btnEliminar;
	private JPanel panelBarra;
	private JPanel panelTable;
	private JButton btnModificar;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ListarVacuna dialog = new ListarVacuna(null,null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ListarVacuna(Paciente paciente, String opcion) {
		auxPaciente = paciente;
		auxOpcion = opcion;

		/*
		if (paciente == null) {
			auxOpcion = "Guardar";
			auxPaciente = Clinica.getInstancia().crearPacientePrueba("Amanda", "1016");
			auxPaciente.agregarVacuna(Clinica.getInstancia().vacunaPrueba("Cybac"));

			Clinica.getInstancia().crearVacsClinicaPrueba();
		}else {
			auxPaciente = paciente;
			auxOpcion = opcion;
		}*/

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
		panelBarra.setBounds(28, 26, 738, 63);
		panelBarra.setVisible(false);
		contentPanel.add(panelBarra);
		panelBarra.setLayout(null);

		JLabel lblPaciente = new JLabel("Paciente:");
		lblPaciente.setForeground(new Color(70, 130, 180));
		lblPaciente.setFont(new Font("Verdana", Font.BOLD, 14));
		lblPaciente.setBounds(16, 16, 78, 30);
		panelBarra.add(lblPaciente);

		txtNomPac = new JTextField();
		txtNomPac.setEditable(false);
		txtNomPac.setBounds(100, 20, 453, 22);
		txtNomPac.setBackground(new Color(224, 247, 250));
		txtNomPac.setFont(new Font("Verdana", Font.PLAIN, 14));
		txtNomPac.setBorder(new LineBorder(new Color(70, 130, 180)));
		panelBarra.add(txtNomPac);
		txtNomPac.setColumns(10);

		cbxMostrar = new JComboBox();
		cbxMostrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadVacunas(auxPaciente);
			}
		});
		cbxMostrar.setModel(new DefaultComboBoxModel(new String[] {"<Mostrar>", "Aplicadas", "No Aplicadas"}));
		cbxMostrar.setBounds(579, 18, 134, 26);
		cbxMostrar.setFont(new Font("Verdana", Font.BOLD, 14));
		cbxMostrar.setBackground(new Color(176, 224, 230));
		cbxMostrar.setForeground(new Color(70, 130, 180));
		panelBarra.add(cbxMostrar);

		panelTable = new JPanel();
		panelTable.setBounds(28, 127, 738, 285);
		panelTable.setBorder(new LineBorder(new Color(70, 130, 180)));
		contentPanel.add(panelTable);
		panelTable.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollTabla = new JScrollPane();
		panelTable.add(scrollTabla, BorderLayout.CENTER);

		table = new JTable();
		scrollTabla.setViewportView(table);

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; 
			}
		};

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		table.setBackground(Color.WHITE);
		table.setSelectionBackground(new Color(176, 224, 230));
		table.setSelectionForeground(new Color(70, 130, 180));
		table.setGridColor(new Color(173, 216, 230));
		table.getTableHeader().setFont(new Font("Bahnschrift", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(135, 206, 235));
		table.getTableHeader().setForeground(new Color(70, 130, 180));

		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(false);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if(index > -1) {
					if(auxPaciente != null) {
						btnCancelar.setEnabled(true);
						btnGuardar.setEnabled(true);

					}else {
						btnEliminar.setEnabled(true);
						btnModificar.setEnabled(true);
						
						String id = table.getValueAt(index, 0).toString();

						for(Vacuna v : Clinica.getInstancia().getVacunas()) {
							if(v.getId().equals(id)) {
								auxVacuna = v;
								break;
							}
						}
					}

				}
			}
		});
		table.setModel(model);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{

				btnEliminar = new JButton("Eliminar");
				btnEliminar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
				btnEliminar.setForeground(new Color(70, 130, 180));
				btnEliminar.setBackground(new Color(255, 245, 238));
				btnEliminar.setFocusPainted(false);
				btnEliminar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(auxVacuna != null) {
							int option = JOptionPane.showConfirmDialog(
									null, 
									"�Est� seguro que desea eliminar la vacuna: " + auxVacuna.getNombre() + "?",
									"Confirmaci�n", 
									JOptionPane.WARNING_MESSAGE
									);
							if(option == JOptionPane.OK_OPTION) {
								Clinica.getInstancia().getVacunas().remove(auxVacuna);
								btnEliminar.setEnabled(false);
								btnGuardar.setEnabled(false);
								loadVacunas(auxPaciente);
								JOptionPane.showMessageDialog(null, 
										"Vacuna eliminada exitosamente.", 
										"�xito", 
										JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}
				});
				btnEliminar.setEnabled(false);
				btnEliminar.setVisible(false);
				buttonPane.add(btnEliminar);


				btnModificar = new JButton("Modificar");
				btnModificar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
				btnModificar.setForeground(new Color(70, 130, 180));
				btnModificar.setBackground(new Color(255, 245, 238));
				btnModificar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(auxVacuna != null) {
							RegistrarVacuna modVacuna = new RegistrarVacuna(auxVacuna);
							modVacuna.setModal(true);
							modVacuna.setVisible(true);
							loadVacunas(auxPaciente);
						}
					}
				});
				btnModificar.setEnabled(false);
				btnModificar.setVisible(false);
				btnModificar.setActionCommand("OK");
				buttonPane.add(btnModificar);
				getRootPane().setDefaultButton(btnModificar);

				btnGuardar = new JButton(auxOpcion);
				btnGuardar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
				btnGuardar.setForeground(new Color(70, 130, 180));
				btnGuardar.setBackground(new Color(255, 245, 238));
				btnGuardar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						if(auxOpcion.equals("Agregar")) {
							//guardarVacs();
							guardarVacuna();

						}else if(auxOpcion.equals("Ver Detalles")) {
							RegistrarVacuna manejoVacuna = new RegistrarVacuna(null);
			                manejoVacuna.setModal(true);
			                manejoVacuna.setVisible(true);
						}

					}
				});
				btnGuardar.setActionCommand("OK");
				btnGuardar.setEnabled(false);
				buttonPane.add(btnGuardar);
				getRootPane().setDefaultButton(btnGuardar);
			}
			{
				btnCancelar = new JButton("Volver");
				btnCancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
				btnCancelar.setForeground(new Color(70, 130, 180));
				btnCancelar.setBackground(new Color(255, 245, 238));
				btnCancelar.setActionCommand("Cancel");
				buttonPane.add(btnCancelar);
			}
		}

		setFormatoTabla(auxPaciente);
		loadVacunas(auxPaciente);

	}

	private void setFormatoTabla(Paciente paciente) {

		if(auxPaciente != null) {
			System.out.println("Vacunas del Paciente");
			setBounds(100, 100, 818, 541);
			panelBarra.setVisible(true);
			panelTable.setBounds(28, 127, 738, 285);

			txtNomPac.setText(auxPaciente.getNombre());

			String[] headers = {"C�digo", "Nombre", "Enfermedad", "Edad M�nima", "Fabricante","Aplicada"};
			model.setColumnIdentifiers(headers);

			if(auxOpcion.equals("Guardar")) {
				table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			}else if(auxOpcion.equals("Ver Detalles")) {
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}

			btnEliminar.setVisible(false);
			btnModificar.setVisible(false);
			btnGuardar.setVisible(true);

		}else {
			setTitle("Inventario de Vacunas");
			setBounds(100, 100, 818, 418);
			panelTable.setBounds(28, 26, 738, 285);

			String[] headers = {"C�digo", "Nombre", "Enfermedad", "Edad M�nima", "Fabricante"};
			model.setColumnIdentifiers(headers);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			btnEliminar.setVisible(true);
			btnModificar.setVisible(true);
			btnGuardar.setVisible(false);

		}

	}

	private void loadVacunas(Paciente paciente) {
		model.setRowCount(0);
		
		if(paciente != null)
			loadVacsPaciente(paciente);
		else
			loadVacsClinica();
	}

	private void loadVacsClinica() {
		ArrayList<Vacuna> vacsClinica = Clinica.getInstancia().getVacunas();

		model.setRowCount(0);
		row = new Object[model.getColumnCount()];

		for (Vacuna vacuna : vacsClinica) {
			if(vacuna != null) {
				row[0] = vacuna.getId();
				row[1] = vacuna.getNombre();
				row[2] = vacuna.getEnfermedad() != null ? vacuna.getEnfermedad().getNombre() : "N/A";
				row[3] = vacuna.getEdadMinima();
				row[4] = vacuna.getFabricante() != null ? vacuna.getFabricante() : "N/A";
				model.addRow(row);
			}

		}

		table.setRowSelectionAllowed(true);
	}

	private void loadVacsPaciente(Paciente paciente) {
		model.setRowCount(0);
		row = new Object[model.getColumnCount()];

		ArrayList<Vacuna> vacsPaciente = auxPaciente.getVacunas();
		ArrayList<Vacuna> vacsClinica = Clinica.getInstancia().getVacunas();

		if(cbxMostrar.getSelectedItem().equals("Aplicadas")) {
			for (Vacuna vacuna : vacsPaciente) {

				row[0] = vacuna.getId();
				row[1] = vacuna.getNombre();
				row[2] = vacuna.getEnfermedad() != null ? vacuna.getEnfermedad().getNombre() : "N/A";
				row[3] = vacuna.getEdadMinima();
				row[4] = vacuna.getFabricante() != null ? vacuna.getFabricante() : "N/A";
				row[5] = "Si";
				model.addRow(row);
			}

			table.setRowSelectionAllowed(false);
			table.clearSelection();

		}else if(cbxMostrar.getSelectedItem().equals("No Aplicadas")) {
			for (Vacuna vacuna : vacsClinica) {

				if(!vacsPaciente.contains(vacuna)) {

					row[0] = vacuna.getId();
					row[1] = vacuna.getNombre();
					row[2] = vacuna.getEnfermedad() != null ? vacuna.getEnfermedad().getNombre() : "N/A";
					row[3] = vacuna.getEdadMinima();
					row[4] = vacuna.getFabricante() != null ? vacuna.getFabricante() : "N/A";
					row[5] = "No";
					model.addRow(row);
				}

			}

			table.setRowSelectionAllowed(true);

		}


	}
	
	private void guardarVacuna() {
		int filaSelect = table.getSelectedRow();
		int contador = 0;


			String codigo = table.getValueAt(filaSelect, 0).toString();
			vacSelect = Clinica.getInstancia().buscarVacunaXId(codigo);

			if(vacSelect != null) {
				auxPaciente.agregarVacuna(vacSelect);
				contador++;
			}else {
				JOptionPane.showMessageDialog(this, 
						"No fue posible aplicar la vacuna",
						"Vacunas Aplicadas", 
						JOptionPane.INFORMATION_MESSAGE);
			}


		loadVacunas(auxPaciente);

		if(contador > 0) {
			JOptionPane.showMessageDialog(this, 
					"Se aplicaron " + contador + " vacunas al paciente " + auxPaciente.getNombre(),
					"Vacunas Aplicadas", 
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	private void guardarVacs() {
		int[] vacsSelect = table.getSelectedRows();
		int contador = 0;

		for (int filaSelect : vacsSelect) {
			String codigo = table.getValueAt(filaSelect, 0).toString();
			vacSelect = Clinica.getInstancia().buscarVacunaXId(codigo);

			if(vacSelect != null) {
				auxPaciente.agregarVacuna(vacSelect);
				contador++;
			}else {
				JOptionPane.showMessageDialog(this, 
						"No fue posible aplicar la vacuna",
						"Vacunas Aplicadas", 
						JOptionPane.INFORMATION_MESSAGE);
			}

		}

		loadVacunas(auxPaciente);

		if(contador > 0) {
			JOptionPane.showMessageDialog(this, 
					"Se aplicaron " + contador + " vacunas al paciente " + auxPaciente.getNombre(),
					"Vacunas Aplicadas", 
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	private void mostrarDetalles() {
		int index = table.getSelectedRow();

		String codigo = table.getValueAt(index, 0).toString();
		vacSelect = Clinica.getInstancia().buscarVacunaXId(codigo);
	}
}
