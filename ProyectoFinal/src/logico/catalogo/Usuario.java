package logico.catalogo;

import logico.Clinica;

import java.io.Serializable;

public class Usuario implements Serializable {

	private static final long serialVersionUID = -3609313352946430885L;

	private String id;
	private String nombre;
	private String password;
	private String rol;

	public Usuario(int idNumber, String nombre, String password, String rol) {
		super();
		setId(idNumber);
		this.nombre = nombre;
		this.password = password;
		this.rol = rol;
	}

	public String getId() {
		return id;
	}

	public int getIdNumber() {
		return Clinica.getInstancia().getIdNumber(this.id, Usuario.class);
	}

	public void setId(int idNumber) {
		// CORRECCIÓN: Se cambió Sintoma.class por Usuario.class
		this.id = Clinica.getInstancia().genId(idNumber, Usuario.class);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String tipo) {
		this.rol = tipo;
	}

	public boolean match(String nombre, String password) {
		return (this.nombre.equals(nombre) && this.password.equals(password));
	}
}