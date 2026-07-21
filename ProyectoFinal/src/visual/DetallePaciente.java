package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import logico.Paciente;

public class DetallePaciente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Paciente paciente;
	private JTextField txtId;
	private JTextField txtNombre;
	private JTextField txtCedula;
	private JTextField txtEdad;
	private JTextField txtSexo;
	private JTextField txtTelefono;
	private JTextField txtDireccion;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*
		try {
			
			DetallePaciente dialog = new DetallePaciente(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

	/**
	 * Create the dialog.
	 */
	public DetallePaciente(Paciente paciente) {
		this.paciente = paciente;

		setTitle("Detalle del Paciente - " + paciente.getIdPaciente());
		setBounds(100, 100, 600, 421);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panelInfo = new JPanel();
		panelInfo.setBackground(Color.WHITE);
		panelInfo.setBorder(new TitledBorder(
				new LineBorder(new Color(70, 130, 180), 2),
				"Información del Paciente",
				TitledBorder.LEFT,
				TitledBorder.TOP,
				new Font("Bahnschrift", Font.BOLD, 14),
				new Color(70, 130, 180)
				));
		panelInfo.setBounds(10, 10, 560, 333);
		contentPanel.add(panelInfo);
		panelInfo.setLayout(null);


		JLabel lblId = new JLabel("ID:");
		lblId.setFont(new Font("Verdana", Font.BOLD, 11));
		lblId.setForeground(new Color(70, 130, 180));
		lblId.setBounds(20, 30, 100, 20);
		panelInfo.add(lblId);

		txtId = new JTextField();
		txtId.setEditable(false);
		txtId.setBackground(new Color(224, 247, 250));
		txtId.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtId.setBounds(152, 30, 180, 22);
		txtId.setText(paciente.getIdPaciente());
		panelInfo.add(txtId);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Verdana", Font.BOLD, 11));
		lblNombre.setForeground(new Color(70, 130, 180));
		lblNombre.setBounds(20, 70, 100, 20);
		panelInfo.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setEditable(false);
		txtNombre.setBackground(new Color(224, 247, 250));
		txtNombre.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtNombre.setBounds(150, 70, 300, 22);
		txtNombre.setText(paciente.getNombre());
		panelInfo.add(txtNombre);

		JLabel lblCedula = new JLabel("Cédula:");
		lblCedula.setFont(new Font("Verdana", Font.BOLD, 11));
		lblCedula.setForeground(new Color(70, 130, 180));
		lblCedula.setBounds(20, 110, 100, 20);
		panelInfo.add(lblCedula);

		txtCedula = new JTextField();
		txtCedula.setEditable(false);
		txtCedula.setBackground(new Color(224, 247, 250));
		txtCedula.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtCedula.setBounds(152, 110, 180, 22);
		txtCedula.setText(paciente.getCedula());
		panelInfo.add(txtCedula);

		JLabel lblEdad = new JLabel("Fecha nacimiento:");
		lblEdad.setFont(new Font("Verdana", Font.BOLD, 11));
		lblEdad.setForeground(new Color(70, 130, 180));
		lblEdad.setBounds(20, 150, 121, 20);
		panelInfo.add(lblEdad);

		txtEdad = new JTextField();
		txtEdad.setEditable(false);
		txtEdad.setBackground(new Color(224, 247, 250));
		txtEdad.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtEdad.setBounds(153, 145, 157, 22);
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		txtEdad.setText(formato.format(paciente.getFecNacim()));

		panelInfo.add(txtEdad);

		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setFont(new Font("Verdana", Font.BOLD, 11));
		lblSexo.setForeground(new Color(70, 130, 180));
		lblSexo.setBounds(20, 190, 100, 20);
		panelInfo.add(lblSexo);

		txtSexo = new JTextField();
		txtSexo.setEditable(false);
		txtSexo.setBackground(new Color(224, 247, 250));
		txtSexo.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtSexo.setBounds(153, 190, 90, 22);
		txtSexo.setText(paciente.getSexo());
		panelInfo.add(txtSexo);

		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setFont(new Font("Verdana", Font.BOLD, 11));
		lblTelefono.setForeground(new Color(70, 130, 180));
		lblTelefono.setBounds(20, 230, 100, 20);
		panelInfo.add(lblTelefono);

		txtTelefono = new JTextField();
		txtTelefono.setEditable(false);
		txtTelefono.setBackground(new Color(224, 247, 250));
		txtTelefono.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtTelefono.setBounds(152, 230, 180, 22);
		txtTelefono.setText(paciente.getTelefono());
		panelInfo.add(txtTelefono);

		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setFont(new Font("Verdana", Font.BOLD, 11));
		lblDireccion.setForeground(new Color(70, 130, 180));
		lblDireccion.setBounds(20, 270, 100, 20);
		panelInfo.add(lblDireccion);

		txtDireccion = new JTextField();
		txtDireccion.setEditable(false);
		txtDireccion.setBackground(new Color(224, 247, 250));
		txtDireccion.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		txtDireccion.setBounds(152, 270, 380, 22);
		txtDireccion.setText(paciente.getDireccion());
		panelInfo.add(txtDireccion);
	}
}