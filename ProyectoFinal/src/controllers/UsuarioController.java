package controllers;

import logico.Clinica;
import logico.Usuario;
import utilidad.Formato;

public class UsuarioController {
	
	public String getNextCode() {
		return "USR-" + Clinica.getInstancia().genCodigoUsuarios;
	}

	public boolean guardarUsuario(String codigo, String nombre, String contrasenia, String tipo) {
		if (Formato.entradaVacia(codigo, "El código de usuario es obligatorio.") || 
			Formato.entradaVacia(nombre, "El nombre de usuario es obligatorio.") || 
			Formato.entradaVacia(contrasenia, "La contraseña es obligatoria.")) {
			return false;
		}
		
		if (tipo.equals("<<Seleccione>>")) {
			javax.swing.JOptionPane.showMessageDialog(null, "Debe seleccionar un tipo de usuario válido.", "Campo Requerido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return false;
		}

		Usuario nuevoUsuario = new Usuario(codigo, contrasenia, tipo); 

		Clinica.getInstancia().regUsuario(nuevoUsuario);
		return true;
	}
}