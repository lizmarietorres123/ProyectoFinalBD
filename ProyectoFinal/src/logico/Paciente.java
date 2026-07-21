package logico;

import java.util.Date;
import java.io.Serializable;
import java.util.ArrayList;

public class Paciente implements Serializable{

	private static final long serialVersionUID = 2532314229066693215L;
	
	private String idPaciente;
	private String nombre;
	private String cedula;
	private String telefono;
	private Date fecNacim;
	private String sexo;
	private float peso;
	private float estatura;
	private String tipoSangre;
	private String direccion;

	private ArrayList<Vacuna> vacunas;
	private ArrayList<Consulta> resumen; 
	// Son las consultas que cada doctor elige como importantes. Todos los doctores las pueden ver.
	private ArrayList<Consulta> historialClinico; 
	// Guarda todas las consultas que el paciente a tenido. Al mostrar las de cada doctor, se puede hacer revisando a qué doctor pertenece cada consulta.
    private ArrayList<Enfermedad> enfermedades;
    

	public Paciente(String idPaciente, String nombre, String cedula, String telefono, Date fecNacim,  
			String sexo, float peso, float estatura, String tipoSangre, String direccion) {
		super();
		this.idPaciente = idPaciente;
		this.nombre = nombre;
		this.cedula = cedula;
		this.telefono = telefono;
		this.fecNacim = fecNacim;
		this.sexo = sexo;
		this.peso = peso;
		this.estatura = estatura;
		this.tipoSangre = tipoSangre;
		this.direccion = direccion;

		vacunas = new ArrayList<>();
		resumen = new ArrayList<>();
		historialClinico = new ArrayList<>();
		enfermedades = new ArrayList<>();
	}

	
	public String getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Date getFecNacim() {
		return fecNacim;
	}

	public void setFecNacim(Date fecNacim) {
		this.fecNacim = fecNacim;
	}

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}

	public float getEstatura() {
		return estatura;
	}

	public void setEstatura(float estatura) {
		this.estatura = estatura;
	}

	public String getTipoSangre() {
		return tipoSangre;
	}

	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public ArrayList<Vacuna> getVacunas() {
		return vacunas;
	}

	public void setVacunas(ArrayList<Vacuna> vacunas) {
		this.vacunas = vacunas;
	}

	public ArrayList<Consulta> getResumen() {
		return resumen;
	}

	public void setResumen(ArrayList<Consulta> resumen) {
		this.resumen = resumen;
	}

	public ArrayList<Consulta> getHistorialClinico() {
		return historialClinico;
	}

	public void setHistorialClinico(ArrayList<Consulta> historialClinico) {
		this.historialClinico = historialClinico;
	}

	public void addConsultaToResumen(Consulta consulta) {
		if(consulta.getEsImportante())
			resumen.add(consulta);
	}
	
	public ArrayList<String> getDoctores(){
		ArrayList<String> doctores = new ArrayList<>();
		
		for (Consulta consulta : historialClinico) {
			String nomDoc = consulta.getDoctor().getNombre();
			
			if(!doctores.contains(nomDoc))
				doctores.add(nomDoc);
		}
		
		if(doctores.size() == 0)
			doctores = null;
		
		return doctores;
	}

	public void mostrarHistorialXDoctor(Doctor doctor) {
		
		
		
	}

	public ArrayList<Enfermedad> getEnfermedades() {
	    return enfermedades;
	}

	public void setEnfermedades(ArrayList<Enfermedad> enfermedades) {
	    this.enfermedades = enfermedades;
	}
	
	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public void agregarEnfermedad(Enfermedad e) {
        if(!enfermedades.contains(e)) {
            enfermedades.add(e);
        }
    }

	
    public Enfermedad buscarEnfermedadPorId(String id) {
        for (Enfermedad e : enfermedades) {
            if (e.getId().equalsIgnoreCase(id)) {
                return e;
            }
        }
        return null;
    }
    

    public ArrayList<Enfermedad> getEnfermedadesBajoVigilancia() {
        ArrayList<Enfermedad> lista = new ArrayList<>();
        for (Enfermedad e : enfermedades) {
            if (e.isVigilancia()) {
                lista.add(e);
            }
        }
        return lista;
    }
    /*
    public void agregarVacuna(Vacuna v) {
        if (v != null && !vacunas.contains(v)) {
        	System.out.println(Vacuna Agregada);
        	v.setAplicada(true);
            vacunas.add(v);
        }
    }
    */
    
    public void agregarVacuna(Vacuna v) {
        	System.out.println("Vacuna Agregada");
        	v.setAplicada(true);
            vacunas.add(v);
        
    }
    
    public Vacuna buscarVacunaPorId(String id) {
        for (Vacuna v : vacunas) {
            if (v.getId().equalsIgnoreCase(id)) {
                return v;
            }
        }
        return null;
    }
    
    public ArrayList<Vacuna> getVacunasAplicadas() {
        ArrayList<Vacuna> lista = new ArrayList<>();
        for (Vacuna v : vacunas) {
            if (v.isAplicada()) {
                lista.add(v);
            }
        }
        return lista;
    }
    
    public ArrayList<Vacuna> getVacunasPendientes() {
        ArrayList<Vacuna> lista = new ArrayList<>();
        for (Vacuna v : vacunas) {
            if (!v.isAplicada()) {
                lista.add(v);
            }
        }
        return lista;
    }
    
    public boolean aplicarVacuna(String idVacuna, String doctor) {
        Vacuna v = buscarVacunaPorId(idVacuna);
        if (v != null && !v.isAplicada()) {
            v.aplicarVacuna(doctor);
            return true;
        }
        return false;
    }
    
    public ArrayList<Boolean> getEstadosVacunas() {
        ArrayList<Boolean> estados = new ArrayList<>();
        for (Vacuna v : vacunas) {
            estados.add(v.isAplicada());
        }
        return estados;
    }

}
