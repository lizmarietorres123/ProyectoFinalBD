package logico;

import bd.ConsultaDAO;
import bd.catalogo.*;
import logico.catalogo.*;
import logico.consultorio.*;
import logico.enfermeria.*;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;

public class Clinica implements Serializable {
    private static final long serialVersionUID = -2147265011502063886L;

    // --- VARIABLES ESTÁTICAS DE GENERACIÓN DE CÓDIGOS Y SESIÓN ---
    public static Doctor loginDoctor;
    public static int genCodigoPacientes = 1;
    public static int genCodigoCitas = 1;
    public static int genCodigoDiagnosticos = 1;
    public static int genCodigoDoctores = 1;
    public static int genCodigoVacuna = 1;
    public static int genCodigoEnfermedad = 1;
    public static int genCodigoUsuarios = 1;
    public static int genCodigoTratamientos = 1;

    // --- INICIAL ID ---
    public static String codConsulta = "CONS-";

    // --- ATRIBUTOS DE INSTANCIA ---
    private Usuario usuarioActual;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Cita> citas;
    private ArrayList<Paciente> pacientes;
    private ArrayList<Doctor> doctores;
    private ArrayList<Enfermera> enfermeras;

    private ArrayList<Consulta> consultas;
    private ArrayList<Sintoma> sintomas;
    private ArrayList<Enfermedad> enfermedades;
    private ArrayList<Medicamento> medicamentos;
    private ArrayList<Analisis> analisis;
    private ArrayList<Vacuna> vacunas;
    private ArrayList<Integer> contadores;
    private ArrayList<Especialidad> especialidades;
    private ArrayList<Diagnostico> diagnosticos;
    private ArrayList<Tratamiento> tratamientos;
    private ArrayList<DetalleAnalisis> detalleAnalisis;
    private ArrayList<DetalleVacuna> detalleVacunas;

    private Map<Class<?>,String> ids;

    private static Clinica instancia = null;

    private Clinica() {
        citas = new ArrayList<>();
        pacientes = new ArrayList<>();
        doctores = new ArrayList<>();
        enfermeras = new ArrayList<>();
        consultas = new ArrayList<>();
        sintomas = new ArrayList<>();
        enfermedades = new ArrayList<>();
        medicamentos = new ArrayList<>();
        analisis = new ArrayList<>();
        vacunas = new ArrayList<>();
        usuarios = new ArrayList<>();
        especialidades = new ArrayList<>();
        diagnosticos = new ArrayList<>();
        tratamientos = new ArrayList<>();
        detalleAnalisis = new ArrayList<>();
        detalleVacunas = new ArrayList<>();

        ids = new HashMap<>();

        asignarIds();
        iniciarContadores();
    }

    public static Clinica getInstancia() {
        if (instancia == null) {
            instancia = new Clinica();
        }
        return instancia;
    }

    public static void setInstancia(Clinica auxClinica) {
        if (auxClinica != null) {
            instancia = auxClinica;
        }
    }

    // --- GETTERS & SETTERS COMPLETOS Y ESTANDARIZADOS ---

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public static Doctor getLoginDoctor() {
        return loginDoctor;
    }

    public static void setLoginDoctor(Doctor loginDoctor) {
        Clinica.loginDoctor = loginDoctor;
    }

    public static Doctor getDoctorActual() {
        return loginDoctor;
    }

    public static void setDoctorActual(Doctor doctor) {
        Clinica.loginDoctor = doctor;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public ArrayList<Cita> getCitas() {
        return citas;
    }

    public void setCitas(ArrayList<Cita> citas) {
        this.citas = citas;
    }

    public ArrayList<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(ArrayList<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public ArrayList<Doctor> getDoctores() {
        return doctores;
    }

    public void setDoctores(ArrayList<Doctor> doctores) {
        this.doctores = doctores;
    }

    public ArrayList<Enfermera> getEnfermeras() {
        return enfermeras;
    }

    public void setEnfermeras(ArrayList<Enfermera> enfermeras) {
        this.enfermeras = enfermeras;
    }

    public ArrayList<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(ArrayList<Consulta> consultas) {
        this.consultas = consultas;
    }

    public ArrayList<Sintoma> getSintomas() {
        return sintomas;
    }

    public void setSintomas(ArrayList<Sintoma> sintomas) {
        this.sintomas = sintomas;
    }

    public ArrayList<Enfermedad> getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(ArrayList<Enfermedad> enfermedades) {
        this.enfermedades = enfermedades;
    }

    public ArrayList<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(ArrayList<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public ArrayList<Analisis> getAnalisis() {
        return analisis;
    }

    public void setAnalisis(ArrayList<Analisis> analisis) {
        this.analisis = analisis;
    }

    public ArrayList<Vacuna> getVacunas() {
        return vacunas;
    }

    public void setVacunas(ArrayList<Vacuna> vacunas) {
        this.vacunas = vacunas;
    }

    public ArrayList<Especialidad> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(ArrayList<Especialidad> especialidades) {
        this.especialidades = especialidades;
    }

    public ArrayList<Integer> getContadores() {
        return contadores;
    }

    public void setContadores(ArrayList<Integer> contadores) {
        this.contadores = contadores;
    }

    public void setClinica(Clinica auxClinica) {
        setInstancia(auxClinica);
    }

    public ArrayList<Diagnostico> getDiagnosticos() {
        return diagnosticos;
    }

    public void setDiagnosticos(ArrayList<Diagnostico> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }

    public ArrayList<Tratamiento> getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(ArrayList<Tratamiento> tratamientos) {
        this.tratamientos = tratamientos;
    }

    public ArrayList<DetalleAnalisis> getDetalleAnalisis() {
        return detalleAnalisis;
    }

    public void setDetalleAnalisis(ArrayList<DetalleAnalisis> detalleAnalisis) {
        this.detalleAnalisis = detalleAnalisis;
    }

    public ArrayList<DetalleVacuna> getDetalleVacunas() {
        return detalleVacunas;
    }

    public void setDetalleVacunas(ArrayList<DetalleVacuna> detalleVacunas) {
        this.detalleVacunas = detalleVacunas;
    }

    // --- MANEJO DE CODIGOS ---

    private void asignarIds(){
        ids.put(Cita.class,"CIT-");
        ids.put(Doctor.class,"DOC-");
        ids.put(Enfermera.class,"EFM-");
        ids.put(Usuario.class,"US-");
        ids.put(Consulta.class,"CONS-");
        ids.put(Sintoma.class,"SIN-");
        ids.put(Enfermedad.class,"ENF-");
        ids.put(Medicamento.class,"MED-");
        ids.put(Analisis.class,"AN-");
        ids.put(Vacuna.class,"VAC-");
        ids.put(Paciente.class,"PAC-");
        ids.put(Especialidad.class,"ESP-");
        ids.put(Diagnostico.class,"DIAG-");
        ids.put(Tratamiento.class,"TRAT-");
    }

    public <T> String genId(int idNumber, Class<T> clase){
        return ids.get(clase)+idNumber;
    }

    public <T> int getIdNumber(String id, Class<T> clase){
        return Integer.parseInt(id.replace(ids.get(clase), ""));
    }



    public void cargarBD() {
        try {
            usuarios = UsuarioDAO.getInstance().obtenerUsuarios();
            pacientes = PacienteDAO.getInstance().obtenerPacientes();
            doctores = DoctorDAO.getInstance().obtenerDoctores();
            citas = CitaDAO.getInstance().obtenerCitas();
            consultas = ConsultaDAO.getInstance().obtenerConsultas();
            especialidades = EspecialidadDAO.getInstance().obtenerEspecialidades();
            sintomas = SintomaDAO.getInstance().obtenerSintomas();
            vacunas = VacunaDAO.getInstance().obtenerVacunas();
            medicamentos = MedicamentoDAO.getInstance().obtenerMedicamentos();
            analisis = AnalisisDAO.getInstance().obtenerAnalisis();

            enfermedades = EnfermedadDAO.getInstance().obtenerEnfermedades();
            diagnosticos = DiagnosticoDAO.getInstance().obtenerDiagnosticos();
            tratamientos = TratamientoDAO.getInstance().obtenerTratamientos();
            detalleAnalisis = DetalleAnalisisDAO.getInstance().obtenerDetallesAnalisis();
            detalleVacunas = DetalleVacunaDAO.getInstance().obtenerDetallesVacuna();

        } catch (Exception e) {
            System.err.println("Error crítico al cargar datos desde la base de datos: " + e.getMessage());
        }
    }

    private void iniciarContadores() {
        contadores = new ArrayList<Integer>();
        contadores.add(genCodigoPacientes); // 0
        contadores.add(genCodigoDoctores);  // 1
        contadores.add(genCodigoCitas);     // 2
        contadores.add(genCodigoDiagnosticos); // 3
        contadores.add(genCodigoEnfermedad);   // 4
        contadores.add(genCodigoVacuna);       // 5
        contadores.add(genCodigoUsuarios);     // 6
    }

    public void asignarContadores() {
        if (contadores != null && contadores.size() >= 7) {
            genCodigoPacientes = contadores.get(0);
            genCodigoDoctores = contadores.get(1);
            genCodigoCitas = contadores.get(2);
            genCodigoDiagnosticos = contadores.get(3);
            genCodigoEnfermedad = contadores.get(4);
            genCodigoVacuna = contadores.get(5);
            genCodigoUsuarios = contadores.get(6);
        }
    }

    public void guardarContadores() {
        if (contadores == null || contadores.size() < 7) {
            iniciarContadores();
        } else {
            contadores.set(0, genCodigoPacientes);
            contadores.set(1, genCodigoDoctores);
            contadores.set(2, genCodigoCitas);
            contadores.set(3, genCodigoDiagnosticos);
            contadores.set(4, genCodigoEnfermedad);
            contadores.set(5, genCodigoVacuna);
            contadores.set(6, genCodigoUsuarios);
        }
    }
    // --- REGISTRO DE ENTIDADES ---

    public void regPaciente(Paciente paciente) {
        if (paciente != null) {
            pacientes.add(paciente);
            genCodigoPacientes++;
        }
    }

    public void regDoctor(Doctor doctor) {
        if (doctor != null) {
            doctores.add(doctor);
            genCodigoDoctores++;
        }
    }

    public void regCita(Cita cita) {
        if (cita != null) {
            citas.add(cita);
            genCodigoCitas++;
        }
    }

    public void regVacuna(Vacuna vacuna) {
        if (vacuna != null) {
            vacunas.add(vacuna);
            genCodigoVacuna++;
        }
    }

    public void regUsuario(Usuario usuario) {
        if (usuario != null) {
            usuarios.add(usuario);
            genCodigoUsuarios++;
        }
    }

    public void registrarEnfermedad(Enfermedad enfermedad) {
        if (enfermedad != null) {
            enfermedades.add(enfermedad);
            genCodigoEnfermedad++;
        }
    }

    public void regDiagnostico(Diagnostico diagnostico) {
        if (diagnostico != null) {
            diagnosticos.add(diagnostico);
            genCodigoDiagnosticos++;
        }
    }

    public void regTratamiento(Tratamiento tratamiento) {
        if (tratamiento != null) {
            tratamientos.add(tratamiento);
            genCodigoTratamientos++;
        }
    }

    // DetalleAnalisis y DetalleVacuna identifican su fila con un id de tipo
    // String asignado por la BD (no usan el patrón genId/getIdNumber de las
    // demás entidades), por lo que aquí solo se agregan a la lista.
    public void regDetalleAnalisis(DetalleAnalisis detalle) {
        if (detalle != null) {
            detalleAnalisis.add(detalle);
        }
    }

    public void regDetalleVacuna(DetalleVacuna detalle) {
        if (detalle != null) {
            detalleVacunas.add(detalle);
        }
    }



    public Paciente buscarPacienteXId(String id) {
        if (id == null) return null;
        for (Paciente p : pacientes) {
            if (p != null && p.getId() != null && p.getId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    public Paciente buscarPacienteXIdentificacion(String cedula) {
        if (cedula == null) return null;
        for (Paciente p : pacientes) {
            if (p != null && p.getCedula() != null && p.getCedula().equalsIgnoreCase(cedula)) return p;
        }
        return null;
    }

    public Paciente buscarPacienteXIdNumber(int idNumber) {
        return buscarPacienteXId(genId(idNumber, Paciente.class));
    }

    public Cita buscarCitaXId(String id) {
        if (id == null) return null;
        for (Cita c : citas) {
            if (c != null && c.getId() != null && c.getId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    public Cita buscarCitaXIdNumber(int idNumber) {
        return buscarCitaXId(genId(idNumber, Cita.class));
    }

    public Enfermera buscarEnfermeraXId(String id) {
        if (id == null) return null;
        for (Enfermera e : enfermeras) {
            if (e != null && e.getId() != null && e.getId().equalsIgnoreCase(id)) return e;
        }
        return null;
    }

    public Enfermera buscarEnfermeraXUsuario(Usuario usuario) {
        if (usuario == null) return null;
        for (Enfermera efm : enfermeras) {
            if (efm != null && efm.getUsuario() != null && efm.getUsuario().getNombre().equals(usuario.getNombre())) {
                return efm;
            }
        }
        return null;
    }


    public Enfermera buscarEnfermeraXIdNumber(int idNumber) {
        return buscarEnfermeraXId(genId(idNumber, Enfermera.class));
    }

    public Usuario buscarUsuarioXId(String id) {
        if (id == null) return null;
        for (Usuario u : usuarios) {
            if (u != null && u.getId() != null && u.getId().equalsIgnoreCase(id)) return u;
        }
        return null;
    }

    // Sobrecarga para buscar por ID numérico directamente (Útil para mapear los DAOs)
    public Usuario buscarUsuarioXIdNumber(int idNumber) {
        return buscarUsuarioXId(genId(idNumber, Usuario.class));
    }

    public Doctor buscarDoctorXId(String id) {
        if (id == null) return null;
        for (Doctor d : doctores) {
            if (d != null && d.getId() != null && d.getId().equalsIgnoreCase(id)) return d;
        }
        return null;
    }

    public Doctor buscarDoctorXIdNumber(int idNumber) {
        return buscarDoctorXId(genId(idNumber, Doctor.class));
    }

    public Doctor buscarDoctorXUsuario(Usuario usuario) {
        if (usuario == null) return null;
        for (Doctor doc : doctores) {
            if (doc != null && doc.getUsuario() != null && doc.getUsuario().getNombre().equals(usuario.getNombre())) {
                return doc;
            }
        }
        return null;
    }

    public Consulta buscarConsultaXId(String id) {
        if (id == null) return null;
        for (Consulta c : consultas) {
            if (c != null && c.getId() != null && c.getId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    public Consulta buscarConsultaXIdNumber(int idNumber) {
        return buscarConsultaXId(genId(idNumber, Consulta.class));
    }

    public Vacuna buscarVacunaXId(String id) {
        if (id == null) return null;
        for (Vacuna v : vacunas) {
            if (v != null && v.getId() != null && v.getId().equalsIgnoreCase(id)) return v;
        }
        return null;
    }

    public Vacuna buscarVacunaXIdNumber(int idNumber) {
        return buscarVacunaXId(genId(idNumber, Vacuna.class));
    }

    public Especialidad buscarEspecialidadXId(String id) {
        if (id == null) return null;
        for (Especialidad esp : especialidades) {
            if (esp != null && esp.getId() != null && esp.getId().equalsIgnoreCase(id)) return esp;
        }
        return null;
    }

    public Especialidad buscarEspecialidadXIdNumber(int idNumber) {
        return buscarEspecialidadXId(genId(idNumber, Especialidad.class));
    }

    public Enfermedad buscarEnfermedadXId(String id) {
        if (id == null) return null;
        for (Enfermedad e : enfermedades) {
            if (e != null && e.getId() != null && e.getId().equalsIgnoreCase(id)) return e;
        }
        return null;
    }

    public Enfermedad buscarEnfermedadXIdNumber(int idNumber) {
        return buscarEnfermedadXId(genId(idNumber, Enfermedad.class));
    }

    public Sintoma buscarSintomaXId(String id) {
        if (id == null) return null;
        for (Sintoma s : sintomas) {
            if (s != null && s.getId() != null && s.getId().equalsIgnoreCase(id)) return s;
        }
        return null;
    }

    public Sintoma buscarSintomaXIdNumber(int idNumber) {
        return buscarSintomaXId(genId(idNumber, Sintoma.class));
    }

    public Medicamento buscarMedicamentoXId(String id) {
        if (id == null) return null;
        for (Medicamento m : medicamentos) {
            if (m != null && m.getId() != null && m.getId().equalsIgnoreCase(id)) return m;
        }
        return null;
    }

    public Medicamento buscarMedicamentoXIdNumber(int idNumber) {
        return buscarMedicamentoXId(genId(idNumber, Medicamento.class));
    }

    public Analisis buscarAnalisisXIdNumber(int idNumber) {
        for (Analisis a : analisis) {
            if (a != null && a.getIdNumber() == idNumber) return a;
        }
        return null;
    }

    public Analisis buscarAnalisisXId(String id) {
        if (id == null) return null;
        try {
            int idNumber = getIdNumber(id, Analisis.class);
            return buscarAnalisisXIdNumber(idNumber);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Diagnostico buscarDiagnosticoXId(String id) {
        if (id == null) return null;
        for (Diagnostico d : diagnosticos) {
            if (d != null && d.getId() != null && d.getId().equalsIgnoreCase(id)) return d;
        }
        return null;
    }

    public Diagnostico buscarDiagnosticoXIdNumber(int idNumber) {
        return buscarDiagnosticoXId(genId(idNumber, Diagnostico.class));
    }

    public Tratamiento buscarTratamientoXId(String id) {
        if (id == null) return null;
        for (Tratamiento t : tratamientos) {
            if (t != null && t.getId() != null && t.getId().equalsIgnoreCase(id)) return t;
        }
        return null;
    }

    public Tratamiento buscarTratamientoXIdNumber(int idNumber) {
        return buscarTratamientoXId(genId(idNumber, Tratamiento.class));
    }

    public DetalleAnalisis buscarDetalleAnalisisXId(String id) {
        if (id == null) return null;
        for (DetalleAnalisis da : detalleAnalisis) {
            if (da != null && da.getId() != null && da.getId().equalsIgnoreCase(id)) return da;
        }
        return null;
    }

    public DetalleVacuna buscarDetalleVacunaXId(String id) {
        if (id == null) return null;
        for (DetalleVacuna dv : detalleVacunas) {
            if (dv != null && dv.getId() != null && dv.getId().equalsIgnoreCase(id)) return dv;
        }
        return null;
    }

    // --- LÓGICA DE NEGOCIO ---
    public int contarCitasXDia(Doctor doctor, Date fecha) {
        if (doctor == null || fecha == null) return 0;

        Calendar calendFecha = Calendar.getInstance();
        calendFecha.setTime(fecha);
        int contador = 0;

        for (Cita cita : citas) {
            if (cita != null && cita.getDoctor() != null &&
                    cita.getDoctor().getId().equals(doctor.getId()) &&
                    cita.getEstado() == EstadoCita.PROGRAMADA &&
                    cita.getFechaConsulta() != null) {

                Calendar calendCita = Calendar.getInstance();
                calendCita.setTime(cita.getFechaConsulta());

                if (calendFecha.get(Calendar.YEAR) == calendCita.get(Calendar.YEAR) &&
                        calendFecha.get(Calendar.DAY_OF_YEAR) == calendCita.get(Calendar.DAY_OF_YEAR)) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public ArrayList<Consulta> getConsultasXDoctor(Doctor doctor) {
        ArrayList<Consulta> res = new ArrayList<>();
        if (this.consultas != null) {
            if (doctor == null) {
                return new ArrayList<>(this.consultas);
            }
            for (Consulta c : this.consultas) {
                if (c != null && c.getDoctor() != null && c.getDoctor().getId() != null && c.getDoctor().getId().equals(doctor.getId())) {
                    res.add(c);
                }
            }
        }
        return res;
    }

    public ArrayList<Consulta> getConsultasVisiblesXDoctor(Doctor doctor) {
        return getConsultasXDoctor(doctor);
    }

    public void reportarCasoEnfermedad(String id) {
        Enfermedad enf = buscarEnfermedadXId(id);
        if (enf != null) {
            enf.reportarCaso();
        }
    }
}