package logico.catalogo;

import java.io.Serializable;

public class Usuario implements Serializable{

	private static final long serialVersionUID = -3609313352946430885L;
	
	private String nombre;
	private String password;
	private String tipo;
	
	public Usuario(String nombre, String password, String tipo) {
		super();
		this.nombre = nombre;
		this.password = password;
		this.tipo = tipo;
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
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public boolean match(String nombre, String password) {
		return (this.nombre.equals(nombre) && this.password.equals(password));
	}
}