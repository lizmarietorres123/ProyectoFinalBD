package logico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Vacuna implements Serializable{
	
	private static final long serialVersionUID = -2869118725192181107L;
	
	private String id;
	private String nombre;
	private Enfermedad enfermedad;
	private int edadMinima;
	private boolean aplicada;           
	private Date fechaAplicacion;
    private String fabricante;
	
	public Vacuna(String id, String nombre, Enfermedad enfermedad, int edadMinima) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.enfermedad = enfermedad;
		this.edadMinima = edadMinima;
		this.aplicada = false;          
        this.fechaAplicacion = null;
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

	public boolean isAplicada() {
		return aplicada;
	}

	public void setAplicada(boolean aplicada) {
		this.aplicada = aplicada;
	}

	public Date getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(Date fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}
	
	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}
	
	public void aplicarVacuna(String doctor) {
	    this.aplicada = true;
	    this.fechaAplicacion = new Date();
    }
	
	public boolean estaPendiente() {
        return !aplicada;
        
	}
	
	public String infoHistorial() {
	    if (!aplicada) {
	        return nombre + " (Pendiente)";
	    }
	    return nombre + " - Aplicada el " + fechaAplicacion;
	}
	
	
}
