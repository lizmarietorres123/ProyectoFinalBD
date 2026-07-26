package logico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Clinica implements Serializable {
    private static final long serialVersionUID = -2147265011502063886L;
    
    private Usuario usuarioActual;
    public static Doctor loginDoctor; 
    public static int genCodigoPacientes = 1;
    public static int genCodigoCitas = 1;
    public static int genCodigoConsultas = 1;
    public static int genCodigoDiagnosticos = 1;
    public static int genCodigoDoctores = 1;
    public static int genCodigoVacuna = 1;
    public static int genCodigoEnfermedad = 1;
    public static int genCodigoUsuarios = 1;

    private ArrayList<Usuario> usuarios;
    private ArrayList<Cita> citas;
    private ArrayList<Paciente> pacientes;
    private ArrayList<Doctor> doctores;
    private ArrayList<Consulta> consultas;
    private ArrayList<Enfermedad> enfermedades;
    private ArrayList<Vacuna> vacunas;
    private ArrayList<Integer> contadores;
    private ArrayList<Especialidad> especialidades;
    
    private static Clinica instancia = null;

    private Clinica() {
        citas = new ArrayList<Cita>();
        pacientes = new ArrayList<Paciente>();
        doctores = new ArrayList<Doctor>();
        consultas = new ArrayList<Consulta>();
        enfermedades = new ArrayList<Enfermedad>();
        vacunas = new ArrayList<Vacuna>();
        usuarios = new ArrayList<Usuario>();
        especialidades = new ArrayList<Especialidad>();
        
        iniciarContadores();
    }

    public static Clinica getInstancia() {
        if(instancia == null) {
            instancia = new Clinica();
        }
        return instancia;
    }

    public ArrayList<Cita> getCitas() {
        return citas;
    }

    public ArrayList<Paciente> getPacientes() {
        return pacientes;
    }

    public ArrayList<Doctor> getDoctores() {
        return doctores;
    }

    public ArrayList<Vacuna> getVacunas() {
        return vacunas;
    }

    public void setVacunas(ArrayList<Vacuna> vacunas) {
        this.vacunas = vacunas;
    }
    
    public ArrayList<Enfermedad> getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(ArrayList<Enfermedad> enfermedades) {
        this.enfermedades = enfermedades;
    }
    
    public ArrayList<Especialidad> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(ArrayList<Especialidad> especialidades) {
        this.especialidades = especialidades;
    }

    public void setClinica(Clinica auxClinica) {
        if(auxClinica != null)
            instancia = auxClinica;
    }
    
    public void initInfo() {
        Usuario admin = new Usuario("admin", "admin", "Admin");
        regUsuario(admin);
        
        Usuario usuarioDoctor1 = new Usuario("Doctor1", "Doctor1", "Doctor");
        regUsuario(usuarioDoctor1);
        
        Usuario usuarioDoctor2 = new Usuario("Doctor2", "Doctor2", "Doctor");
        regUsuario(usuarioDoctor2);
        
        Usuario staff = new Usuario("Staff", "Staff", "Staff");
        regUsuario(staff);
        
        ArrayList<String> especialidades1 = new ArrayList<>();
        especialidades1.add("Pediatría");
        especialidades1.add("Dermatología");
        Doctor doctor1 = new Doctor("DOC-" + genCodigoDoctores, "Dr. Juan Pérez", 20, especialidades1);
        doctor1.setUsuario(usuarioDoctor1);
        regDoctor(doctor1);
        
        ArrayList<String> especialidades2 = new ArrayList<>();
        especialidades2.add("Cardiología");
        especialidades2.add("Medicina General");
        Doctor doctor2 = new Doctor("DOC-" + genCodigoDoctores, "Dra. María González", 20, especialidades2);
        doctor2.setUsuario(usuarioDoctor2);
        regDoctor(doctor2);
        
        doctor1.setPacientes(getPacientes());
        
        crearEnfermDatos();
        if (!enfermedades.isEmpty()) {
            crearVacsClinicaPrueba(enfermedades.get(0));
        }
    }

    private void iniciarContadores() {
        contadores = new ArrayList<Integer>();
        contadores.add(genCodigoPacientes);
        contadores.add(genCodigoDoctores);
        contadores.add(genCodigoCitas);
        contadores.add(genCodigoConsultas);
        contadores.add(genCodigoDiagnosticos);
        contadores.add(genCodigoEnfermedad);
        contadores.add(genCodigoVacuna);
        contadores.add(genCodigoUsuarios);
    }

    public void asignarContadores() {
        if (contadores != null && contadores.size() >= 8) {
            genCodigoPacientes = contadores.get(0);
            genCodigoDoctores = contadores.get(1);
            genCodigoCitas = contadores.get(2);
            genCodigoConsultas = contadores.get(3);
            genCodigoDiagnosticos = contadores.get(4);
            genCodigoEnfermedad = contadores.get(5);
            genCodigoVacuna = contadores.get(6);
            genCodigoUsuarios = contadores.get(7);
        }
    }

    public void guardarContadores() {
        if (contadores == null) {
            iniciarContadores();
        }
        contadores.set(0, genCodigoPacientes);
        contadores.set(1, genCodigoDoctores);
        contadores.set(2, genCodigoCitas);
        contadores.set(3, genCodigoConsultas);
        contadores.set(4, genCodigoDiagnosticos);
        contadores.set(5, genCodigoEnfermedad);
        contadores.set(6, genCodigoVacuna);
        contadores.set(7, genCodigoUsuarios);
    }

    public Paciente buscarPacienteXId(String id) {
        if (id == null) return null;
        for (Paciente p : pacientes) {
            if (p.getIdPaciente().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    public Cita buscarCitaXId(String id) {
        if (id == null) return null;
        for (Cita c : citas) {
            if (c.getIdCita().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public Doctor buscarDoctorXId(String id) {
        if (id == null) return null;
        for (Doctor d : doctores) {
            if (d.getIdDoctor().equals(id)) {
                return d;
            }
        }
        return null;
    }

    public Doctor buscarDoctorXUsuario(Usuario usuario) {
        if (usuario == null) return null;
        for (Doctor doc : doctores) {
            if (doc.getUsuario() != null && doc.getUsuario().getNombre().equals(usuario.getNombre())) {
                return doc;
            }
        }
        return null;
    }

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
  

    public void realizarConsulta(Consulta consulta, Cita cita) {
        if (consulta != null && cita != null) {
            if (consulta.getPaciente() != null) {
                if (consulta.getPaciente().getHistorialClinico() != null) {
                    consulta.getPaciente().getHistorialClinico().add(consulta);
                }
                consulta.getPaciente().addConsultaToResumen(consulta);
            }
            cita.setConsultaGenerada(consulta);
            cita.completar();
            
            genCodigoConsultas++;
            consultas.add(consulta);
        }
    }

    public int contarCitasXDia(Doctor doctor, Date fecha) {
        if (doctor == null || fecha == null) return 0;
        
        Calendar calendFecha = Calendar.getInstance();
        calendFecha.setTime(fecha);
        int contador = 0;
        
        for (Cita cita : citas) {
            if (cita.getDoctor() != null && 
                cita.getDoctor().getIdDoctor().equals(doctor.getIdDoctor()) && 
                cita.getEstado() == EstadoCita.PROGRAMADA && 
                cita.getFechaHora() != null) {
                
                Calendar calendCita = Calendar.getInstance();
                calendCita.setTime(cita.getFechaHora());
                
                if (calendFecha.get(Calendar.YEAR) == calendCita.get(Calendar.YEAR) && 
                    calendFecha.get(Calendar.DAY_OF_YEAR) == calendCita.get(Calendar.DAY_OF_YEAR)) {
                    contador++;
                }
            }
        }
        return contador;
    }
    
    public Paciente buscarPacienteXIdentificacion(String cedula) {
        if (cedula == null) return null;
        for (Paciente p : pacientes) {
            if (p.getCedula().equalsIgnoreCase(cedula)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Consulta> getConsultasXDoctor(Doctor doctor) {
        ArrayList<Consulta> consultasDoctor = new ArrayList<>();
        if (doctor == null) return consultasDoctor;

        for (Consulta consulta : consultas) {
            if (consulta.getDoctor() != null && 
                consulta.getDoctor().getIdDoctor().equals(doctor.getIdDoctor())) {
                consultasDoctor.add(consulta);
            }
        }
        return consultasDoctor;
    }

    public void crearDoctorPrueba() {
        ArrayList<String> especialidadesPrueba = new ArrayList<>();
        especialidadesPrueba.add("Pediatría");
        especialidadesPrueba.add("Dermatología");
        Doctor doctorPrueba = new Doctor(
            "DOC-" + genCodigoDoctores,
            "El tejas",
            20,
            especialidadesPrueba
        );
        regDoctor(doctorPrueba);
    }
    
    public Vacuna vacunaPrueba(String nombre, String cod, Enfermedad e) {
        return new Vacuna(cod, nombre, e, 10);
    }
    
    public void crearVacsClinicaPrueba(Enfermedad e) {
        ArrayList<Vacuna> vacs = new ArrayList<>();
        vacs.add(vacunaPrueba("1930", "Cybac", e));
        vacs.add(vacunaPrueba("2030", "Brinx", e));
        vacs.add(vacunaPrueba("4030", "Fancil", e));
        vacs.add(vacunaPrueba("5030", "Trouse", e));
        vacs.add(vacunaPrueba("1830", "Cyrmac", e));
        
        vacunas = vacs;
        genCodigoVacuna += 5;
    }
    
    public Enfermedad enfermPrueba(String cod, String nombre) {
        Random random = new Random();
        Enfermedad e = new Enfermedad(cod, nombre, false, false, "Dolor de cabeza", "");
        e.setCasosReportados(random.nextInt(350) + 1);
        return e;
    }
    
    public void crearEnfermDatos() {
        Random random = new Random();
        
        ArrayList<String> enfermedadesComunes = new ArrayList<>(Arrays.asList(
                "Gripe",
                "Hipertensión",
                "Diabetes",
                "Asma",
                "Gastritis"
            ));
        
        for (String enferm : enfermedadesComunes) {
            enfermedades.add(enfermPrueba(
                    String.valueOf(random.nextInt(1000) + 999), 
                    enferm));
        }
    }

    public Consulta buscarConsultaXId(String id) {
        if (id == null) return null;
        for (Consulta c : consultas) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }
    
    public Vacuna buscarVacunaXId(String id) {
        if (id == null) return null;
        for (Vacuna v : vacunas) {
            if (v.getId().equals(id)) {
                return v;
            }
        }
        return null;
    }

    public ArrayList<Consulta> getConsultasVisiblesXDoctor(Doctor doctor) {
        ArrayList<Consulta> consultasVisibles = new ArrayList<>();
        if (doctor == null) return consultasVisibles;
        
        for (Consulta consulta : consultas) {
            if (consulta.getDoctor() != null && 
                consulta.getDoctor().getIdDoctor().equals(doctor.getIdDoctor())) {
                consultasVisibles.add(consulta);
            }
        }
        
        for (Paciente paciente : pacientes) {
            if (paciente.getResumen() != null) {
                for (Consulta consultaImportante : paciente.getResumen()) {
                    boolean yaExiste = false;
                    int i = 0;
                    while (i < consultasVisibles.size() && !yaExiste) {
                        if (consultasVisibles.get(i).getId().equals(consultaImportante.getId())) {
                            yaExiste = true;
                        }
                        i++;
                    }
                    if (!yaExiste) {
                        consultasVisibles.add(consultaImportante);
                    }
                }
            }
        }
        
        return consultasVisibles;
    }

    public void registrarEnfermedad(Enfermedad enfermedad) {
        if (enfermedad != null) {
            enfermedades.add(enfermedad);
            genCodigoEnfermedad++;
        }
    }

    public void registrarEnfermedadBajoVigilancia(Enfermedad enfermedad) {
        if (enfermedad != null) {
            enfermedad.activarVigilancia();
            enfermedades.add(enfermedad);
            genCodigoEnfermedad++;
        }
    }

    public Enfermedad buscarEnfermedadXId(String id) {
        if (id == null) return null;
        for (Enfermedad e : enfermedades) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }
    
    public void regUsuario(Usuario usuario) {
        if (usuario != null) {
            usuarios.add(usuario);
            genCodigoUsuarios++;
        }
    }

    public void reportarCasoEnfermedad(String id) {
        Enfermedad enf = buscarEnfermedadXId(id);
        if (enf != null) {
            enf.reportarCaso();
        }
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static Doctor getDoctorActual() {
        return loginDoctor;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }
}