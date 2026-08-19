package logico.catalogo;

import logico.Clinica;

import java.io.Serializable;

public class Vacuna implements Serializable {
	private static final long serialVersionUID = -2869118725192181107L;
	private String id;
	private String nombre;
	private String fabricante;
	private int cantDosis;

	public Vacuna(int idNumber, String nombre, String fabricante, int cantDosis) {
		super();
		setId(idNumber); // CORREGIDO: Antes decía this.id = id;
		this.nombre = nombre;
		this.fabricante = fabricante;
		this.cantDosis = cantDosis;
	}

	public String getId() {
		return id;
	}

	public int getIdNumber() {
		return Clinica.getInstancia().getIdNumber(this.id, Vacuna.class);
	}

	public void setId(int idNumber) {
		this.id = Clinica.getInstancia().genId(idNumber, Vacuna.class);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public int getCantDosis() {
		return cantDosis;
	}

	public void setCantDosis(int cantDosis) {
		this.cantDosis = cantDosis;
	}

	@Override
	public String toString() {
		return nombre;
	}
}