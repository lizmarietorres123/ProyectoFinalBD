package logico.catalogo;

import logico.Clinica;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Enfermedad implements Serializable{
	
	private static final long serialVersionUID = -1798792587237642015L;
	
	private String id;
	private String nombre;
	private String descripcion;
    private boolean esContagiosa;
	private Especialidad especialidad;
	private ArrayList<Sintoma> sintomas;
    private int casosReportados;
    private LocalDate fechaUltimoCaso;
    
	
	public Enfermedad(int idNumber, String nombre, String descripcion, boolean esContagiosa, Especialidad especialidad, ArrayList<Sintoma> sintomas) {
		super();
		setId(idNumber);
        this.nombre = nombre;
        this.esContagiosa = esContagiosa;
        this.fechaUltimoCaso = null;
        this.descripcion = descripcion;
		this.especialidad = especialidad;
		this.sintomas = sintomas;
	}
	
	public String getId() {
		return id;
	}
	public void setId(int idNumber) {
		this.id = Clinica.getInstancia().genId(idNumber, Enfermedad.class);
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isEsContagiosa() {
		return esContagiosa;
	}

	public void setEsContagiosa(boolean esContagiosa) {
		this.esContagiosa = esContagiosa;
	}

	public ArrayList<Sintoma> getSintomas() {
		return sintomas;
	}

	public void setSintomas(ArrayList<Sintoma> sintomas) {
		this.sintomas = sintomas;
	}

	public int getCasosReportados() {
		return casosReportados;
	}

	public void setCasosReportados(int casosReportados) {
		this.casosReportados = casosReportados;
	}

	public LocalDate getFechaUltimoCaso() {
		return fechaUltimoCaso;
	}

	public void setFechaUltimoCaso(LocalDate fechaUltimoCaso) {
		this.fechaUltimoCaso = fechaUltimoCaso;
	}

	public void reportarCaso() {
        casosReportados++;
        fechaUltimoCaso = LocalDate.now();
   }
	
	@Override
	public String toString() {
	    return nombre;
	}
}
