package logico.catalogo;

import java.io.Serializable;
import java.time.LocalDate;

public class Enfermedad implements Serializable{
	
	private static final long serialVersionUID = -1798792587237642015L;
	
	private String id;
	private String nombre;
	private String sintomas;
	private String descripcion;
    private boolean esContagiosa;
	private boolean vigilancia;
    private int casosReportados;
    private LocalDate fechaUltimoCaso;
    private LocalDate fechaInicioVigilancia;
    
	
	public Enfermedad(String id, String nombre, boolean vigilancia, boolean esContagiosa, String sintomas, String descripcion) {
		super();
		
		this.id = id;
        this.nombre = nombre;
        this.esContagiosa = esContagiosa;
        this.vigilancia = vigilancia;
        this.casosReportados = 0;
        this.fechaUltimoCaso = null;
        this.sintomas = sintomas;
        this.descripcion = descripcion;
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
	
	public String getSintomas() {
		return sintomas;
	}

	public void setSintomas(String sintomas) {
		this.sintomas = sintomas;
	}
	
	public boolean isVigilancia() {
		return vigilancia;
	}
	public void setVigilancia(boolean vigilancia) {
		this.vigilancia = vigilancia;
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
	
	public LocalDate getFechaInicioVigilancia() {
		return fechaInicioVigilancia;
	}

	public void setFechaInicioVigilancia(LocalDate fechaInicioVigilancia) {
		this.fechaInicioVigilancia = fechaInicioVigilancia;
	}
	
	public void activarVigilancia() {
	        vigilancia = true;
	        fechaInicioVigilancia = LocalDate.now();
	}

	public void desactivarVigilancia() {
	        vigilancia = false;
	        fechaInicioVigilancia = null;
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
