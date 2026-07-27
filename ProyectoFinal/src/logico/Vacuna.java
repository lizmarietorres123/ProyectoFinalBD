package logico;
import java.io.Serializable;
import java.util.ArrayList;

public class Vacuna implements Serializable {
	private static final long serialVersionUID = -2869118725192181107L;
	private String id;
	private String nombre;
	private String fabricante;
	private ArrayList<Enfermedad> enfermedades;

	public Vacuna(String id, String nombre, String fabricante, ArrayList<Enfermedad> enfermedades) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fabricante = fabricante;
		this.enfermedades = enfermedades != null ? enfermedades : new ArrayList<>();
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

	public ArrayList<Enfermedad> getEnfermedades() {
		return enfermedades;
	}

	public void setEnfermedades(ArrayList<Enfermedad> enfermedades) {
		this.enfermedades = enfermedades;
	}

	@Override
	public String toString() {
		return nombre;
	}
}