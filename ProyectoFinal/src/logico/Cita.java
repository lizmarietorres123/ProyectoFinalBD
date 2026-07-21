package logico;

import java.io.Serializable;
import java.util.Date;

public class Cita implements Serializable{
   
	private static final long serialVersionUID = -4304362059522165583L;
	
	private String idCita;
	private String idPersona;
	private String nombrePersona;
	private String telefonoPersona;
    private Doctor doctor;
    private Date fechaHora;
    private String motivo;
    private EstadoCita estado;
    private Consulta consultaGenerada;
    
	public Cita(String idCita, String idPersona, String nombrePersona, String telefonoPersona, Doctor doctor,
			Date fechaHora, String motivo) {
		super();
		this.idCita = idCita;
		this.idPersona = idPersona;
		this.nombrePersona = nombrePersona;
		this.telefonoPersona = telefonoPersona;
		this.doctor = doctor;
		this.fechaHora = fechaHora;
		this.motivo = motivo;
		this.estado = EstadoCita.PROGRAMADA;
		this.consultaGenerada = null;
	}
	
	
	public String getIdCita() {
		return idCita;
	}



	public void setIdCita(String idCita) {
		this.idCita = idCita;
	}



	public String getIdPersona() {
		return idPersona;
	}



	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}



	public String getNombrePersona() {
		return nombrePersona;
	}



	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}



	public String getTelefonoPersona() {
		return telefonoPersona;
	}



	public void setTelefonoPersona(String telefonoPersona) {
		this.telefonoPersona = telefonoPersona;
	}



	public Doctor getDoctor() {
		return doctor;
	}



	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}



	public Date getFechaHora() {
		return fechaHora;
	}



	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}



	public String getMotivo() {
		return motivo;
	}



	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}



	public EstadoCita getEstado() {
		return estado;
	}



	public void setEstado(EstadoCita estado) {
		this.estado = estado;
	}



	public Consulta getConsultaGenerada() {
		return consultaGenerada;
	}



	public void setConsultaGenerada(Consulta consultaGenerada) {
		this.consultaGenerada = consultaGenerada;
	}
    
    public void cancelar() {
        if(estado == EstadoCita.PROGRAMADA) {
            estado = EstadoCita.CANCELADA;
        }
    }
    
    public void marcarNoAsistio() {
        if(estado == EstadoCita.PROGRAMADA) {
            estado = EstadoCita.NO_ASISTIO;
        }
    }
    
    public void completar() {
        if(consultaGenerada != null) {
            estado = EstadoCita.COMPLETADA;
        }
    }
    
}