package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JCalendar;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.SystemColor;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class PruebaCalendar extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtFecha;
	private JSpinner spnDate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PruebaCalendar dialog = new PruebaCalendar();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PruebaCalendar() {
		setBounds(100, 100, 730, 395);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JCalendar calendar = new JCalendar();
		calendar.setBounds(295, 68, 240, 159);
		calendar.setVisible(false);
		contentPanel.add(calendar);
		
		JButton btnFecha = new JButton("Buscar");
		btnFecha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!calendar.isVisible()) {
					calendar.setVisible(true);
					btnFecha.setText("Seleccionar");
				}else {
					calendar.setVisible(false);
					txtFecha.setText(getDateString(calendar.getDate()));
					btnFecha.setText("Buscar");
				}
			}
		});
		btnFecha.setBounds(420, 37, 115, 29);
		contentPanel.add(btnFecha);
		
		JLabel lblNewLabel = new JLabel("Fecha:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(295, 16, 69, 20);
		contentPanel.add(lblNewLabel);
		
		txtFecha = new JTextField();
		txtFecha.setEnabled(false);
		txtFecha.setText(getDateString(calendar.getDate()));
		txtFecha.setBounds(295, 38, 122, 26);
		contentPanel.add(txtFecha);
		txtFecha.setColumns(10);
		
		spnDate = new JSpinner();
		spnDate.setModel(new SpinnerDateModel(new Date(1763870400000L), null, null, Calendar.DAY_OF_YEAR));
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spnDate, "dd/MM/yyyy");
		spnDate.setEditor(editor);
		spnDate.setBounds(32, 68, 115, 26);
		contentPanel.add(spnDate);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Date fecha = (Date)spnDate.getValue();
						System.out.println(fecha);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private String getDateString(Date date) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(date);
		
		return calendario.get(Calendar.DAY_OF_MONTH)+"/"+(calendario.get(Calendar.MONTH)+1)+"/"+calendario.get(Calendar.YEAR);
		
	}
}
