package logico;

import java.io.Serializable;

public class Vacuna implements Serializable {
	
	private static final long serialVersionUID = -2869118725192181107L;
	
	private String id;
	private String nombre;
	private String fabricante;
	private Enfermedad enfermedad;
	private int edadMinima;
    
	public Vacuna(String id, String nombre, String fabricante, Enfermedad enfermedad, int edadMinima) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fabricante = fabricante;
		this.enfermedad = enfermedad;
		this.edadMinima = edadMinima;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Enfermedad getEnfermedad() {
		return enfermedad;
	}

	public void setEnfermedad(Enfermedad enfermedad) {
		this.enfermedad = enfermedad;
	}

	public int getEdadMinima() {
		return edadMinima;
	}

	public void setEdadMinima(int edadMinima) {
		this.edadMinima = edadMinima;
	}

	@Override
	public String toString() {
		return nombre;
	}
}