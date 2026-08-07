package controlador;

import java.util.ArrayList;
import logico.consultorio.Clinica;
import logico.catalogo.Usuario;

public class UsuarioController {

	public UsuarioController() {
	}

	public ArrayList<Usuario> obtenerTodos() {
		return Clinica.getInstancia().getUsuarios();
	}

	public Usuario buscarPorUsername(String username) {
		if (username == null) return null;
		for (Usuario u : obtenerTodos()) {
			if (u.getNombre().equalsIgnoreCase(username)) {
				return u;
			}
		}
		return null;
	}

	public boolean registrar(Usuario usuario) {
		if (usuario != null) {
			Clinica.getInstancia().getUsuarios().add(usuario);
			return true;
		}
		return false;
	}

	public boolean actualizar(Usuario usuarioOriginal, String nuevoNombre, String nuevaPassword, String nuevoTipo) {
		if (usuarioOriginal != null) {
			usuarioOriginal.setNombre(nuevoNombre);
			usuarioOriginal.setPassword(nuevaPassword); //[cite: 34]
			usuarioOriginal.setTipo(nuevoTipo);
			return true;
		}
		return false;
	}

	public boolean eliminar(Usuario usuario) {
		if (usuario != null) {
			return Clinica.getInstancia().getUsuarios().remove(usuario);
		}
		return false;
	}
}