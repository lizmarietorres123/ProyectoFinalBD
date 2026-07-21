package utilidad;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.text.DefaultFormatter;

public class Formato {

	public static void setSpinner(JSpinner spinner) {

		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
		spinner.setEditor(editor);

		JFormattedTextField txt = editor.getTextField();
		DefaultFormatter formatter = (DefaultFormatter)txt.getFormatter();

		formatter.setAllowsInvalid(false); 
		formatter.setOverwriteMode(true);
	}

	public static void colorSpinner(JSpinner spinner, Color color) {

		JComponent editor = spinner.getEditor();
		JFormattedTextField txt = ((JSpinner.DefaultEditor)(editor)).getTextField();

		txt.setBackground(color);
	}

	public static boolean verificarEntradaRegex(String entrada, String regex, String mensaje) {
		boolean error = false;

		if(!entrada.trim().matches(regex)) {
			JOptionPane.showMessageDialog(null, mensaje, "Campo Requerido", JOptionPane.WARNING_MESSAGE);
			error = true;
		}

		return error;
	}


	public static boolean entradaVacia(String entrada, String mensaje) {
		boolean error = false;

		if(entrada.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, mensaje, "Campo Requerido", JOptionPane.WARNING_MESSAGE);

			error = true;

		}

		return error;
	}

	public static int getBound(JComponent component, String pos) {
		int numPos = 0;

		if(pos.equals("x")) {
			numPos = (int)component.getBounds().getX();
		}else if(pos.equals("y")){
			numPos = (int)component.getBounds().getY();
		}
		return numPos;
	}

	public static String getDateString(Date fecha) {
		Calendar calen = Calendar.getInstance();
		calen.setTime(fecha);
		return calen.get(Calendar.DAY_OF_MONTH)+"/"+(calen.get(Calendar.MONTH)+1)+"/"+calen.get(Calendar.YEAR);
	}

}
