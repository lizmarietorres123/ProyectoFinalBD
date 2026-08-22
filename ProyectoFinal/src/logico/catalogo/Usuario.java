package logico.catalogo;

import logico.Clinica;

import java.io.Serializable;

public class Usuario implements Serializable {

	private static final long serialVersionUID = -3609313352946430885L;

	private String id;
	private String nombre;
	private String password;
	private String rol;
	private String estado; // Nuevo campo para borrado lógico

	public Usuario(int idNumber, String nombre, String password, String rol, String estado) {
		super();
		setId(idNumber);
		this.nombre = nombre;
		this.password = password;
		this.rol = rol;
		this.estado = estado;
	}

	public String getId() {
		return id;
	}

	public int getIdNumber() {
		return Clinica.getInstancia().getIdNumber(this.id, Usuario.class);
	}

	public void setId(int idNumber) {
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public boolean match(String nombre, String password) {
		 return (this.nombre.equals(nombre) && this.password.equals(password) && this.estado.equalsIgnoreCase("Activo"));

	}
}