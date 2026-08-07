package logico.consultorio;

import logico.Doctor;
import logico.catalogo.*;
import logico.enfermeria.Vacuna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Clinica implements Serializable {
    private static final long serialVersionUID = -2147265011502063886L;

    // --- VARIABLES ESTÁTICAS DE GENERACIÓN DE CÓDIGOS Y SESIÓN ---
    public static Doctor loginDoctor;
    public static int genCodigoPacientes = 1;
    public static int genCodigoCitas = 1;
    public static int genCodigoConsultas = 1;
    public static int genCodigoDiagnosticos = 1;
    public static int genCodigoDoctores = 1;
    public static int genCodigoVacuna = 1;
    public static int genCodigoEnfermedad = 1;
    public static int genCodigoUsuarios = 1;

    // --- ATRIBUTOS DE INSTANCIA ---
    private Usuario usuarioActual;
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

    public ArrayList<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(ArrayList<Consulta> consultas) {
        this.consultas = consultas;
    }

    public ArrayList<Enfermedad> getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(ArrayList<Enfermedad> enfermedades) {
        this.enfermedades = enfermedades;
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

    // --- CONTADORES DE PERSISTENCIA ---

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

    public void registrarEnfermedadBajoVigilancia(Enfermedad enfermedad) {
        if (enfermedad != null) {
            enfermedad.activarVigilancia();
            registrarEnfermedad(enfermedad);
        }
    }

    // --- BÚSQUEDAS POR ID / ATRIBUTO ---

    public Paciente buscarPacienteXId(String id) {
        if (id == null) return null;
        for (Paciente p : pacientes) {
            if (p != null && p.getIdPaciente() != null && p.getIdPaciente().equalsIgnoreCase(id)) return p;
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

    public Cita buscarCitaXId(String id) {
        if (id == null) return null;
        for (Cita c : citas) {
            if (c != null && c.getIdCita() != null && c.getIdCita().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    public Doctor buscarDoctorXId(String id) {
        if (id == null) return null;
        for (Doctor d : doctores) {
            if (d != null && d.getIdDoctor() != null && d.getIdDoctor().equalsIgnoreCase(id)) return d;
        }
        return null;
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

    public Vacuna buscarVacunaXId(String id) {
        if (id == null) return null;
        for (Vacuna v : vacunas) {
            if (v != null && v.getId() != null && v.getId().equalsIgnoreCase(id)) return v;
        }
        return null;
    }

    public Enfermedad buscarEnfermedadXId(String id) {
        if (id == null) return null;
        for (Enfermedad e : enfermedades) {
            if (e != null && e.getId() != null && e.getId().equalsIgnoreCase(id)) return e;
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

    public ArrayList<Consulta> getConsultasXDoctor(Doctor doctor) {
        ArrayList<Consulta> consultasDoctor = new ArrayList<>();
        if (doctor == null) return consultasDoctor;

        for (Consulta consulta : consultas) {
            if (consulta != null && consulta.getDoctor() != null &&
                    consulta.getDoctor().getIdDoctor().equals(doctor.getIdDoctor())) {
                consultasDoctor.add(consulta);
            }
        }
        return consultasDoctor;
    }

    public ArrayList<Consulta> getConsultasVisiblesXDoctor(Doctor doctor) {
        if (doctor == null) return new ArrayList<>();

        ArrayList<Consulta> consultasVisibles = getConsultasXDoctor(doctor);

        for (Paciente paciente : pacientes) {
            if (paciente != null && paciente.getResumen() != null) {
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

    public void reportarCasoEnfermedad(String id) {
        Enfermedad enf = buscarEnfermedadXId(id);
        if (enf != null) {
            enf.reportarCaso();
        }
    }

    // --- DATOS DE PRUEBA / INICIALIZACIÓN ---

    public void initInfo() {
        Usuario admin = new Usuario("admin", "admin", "Admin");
        regUsuario(admin);

        Usuario usuarioDoctor1 = new Usuario("doc", "doc", "Doctor");
        regUsuario(usuarioDoctor1);

        Usuario usuarioDoctor2 = new Usuario("Doctor2", "Doctor2", "Doctor");
        regUsuario(usuarioDoctor2);

        Usuario staff = new Usuario("Staff", "Staff", "Staff");
        regUsuario(staff);

        ArrayList<String> especialidades1 = new ArrayList<>(Arrays.asList("Pediatría", "Dermatología"));
        Doctor doctor1 = new Doctor("DOC-" + genCodigoDoctores, "Dr. Juan Pérez", 20, especialidades1);
        doctor1.setUsuario(usuarioDoctor1);
        regDoctor(doctor1);

        ArrayList<String> especialidades2 = new ArrayList<>(Arrays.asList("Cardiología", "Medicina General"));
        Doctor doctor2 = new Doctor("DOC-" + genCodigoDoctores, "Dra. María González", 20, especialidades2);
        doctor2.setUsuario(usuarioDoctor2);
        regDoctor(doctor2);

        doctor1.setPacientes(getPacientes());

        registrarEnfermedad(new Enfermedad("ENF-1", "Gripe A", false, true, "Fiebre, tos, dolor muscular", "Infección viral respiratoria"));
        registrarEnfermedad(new Enfermedad("ENF-2", "Dengue", true, false, "Fiebre alta, dolor retroocular, artralgias", "Enfermedad transmitida por mosquito Aedes"));
        registrarEnfermedad(new Enfermedad("ENF-3", "COVID-19", true, true, "Fiebre, tos, fatiga, pérdida de olfato", "Infección respiratoria por coronavirus"));
        registrarEnfermedad(new Enfermedad("ENF-4", "Diabetes Tipo 2", false, false, "Sed excesiva, fatiga, visión borrosa", "Trastorno metabólico de glucosa alta"));
        registrarEnfermedad(new Enfermedad("ENF-5", "Hipertensión", false, false, "Cefalea, mareos, palpitaciones", "Presión arterial crónicamente elevada"));
        registrarEnfermedad(new Enfermedad("ENF-6", "Hepatitis B", true, true, "Ictericia, fatiga, dolor abdominal", "Infección viral del hígado"));

        regVacuna(new Vacuna("VAC-1", "Flublok", "Sanofi", new ArrayList<>(Arrays.asList(enfermedades.get(0), enfermedades.get(2)))));
        regVacuna(new Vacuna("VAC-2", "Qdenga", "Takeda", new ArrayList<>(Arrays.asList(enfermedades.get(1), enfermedades.get(0)))));
        regVacuna(new Vacuna("VAC-3", "Comirnaty", "Pfizer", new ArrayList<>(Arrays.asList(enfermedades.get(2), enfermedades.get(0), enfermedades.get(1)))));
        regVacuna(new Vacuna("VAC-4", "Spikevax", "Moderna", new ArrayList<>(Arrays.asList(enfermedades.get(2), enfermedades.get(0)))));
        regVacuna(new Vacuna("VAC-5", "Engerix-B", "GSK", new ArrayList<>(Arrays.asList(enfermedades.get(5), enfermedades.get(1)))));
        regVacuna(new Vacuna("VAC-6", "Recombivax HB", "Merck", new ArrayList<>(Arrays.asList(enfermedades.get(5), enfermedades.get(0), enfermedades.get(2)))));

        regPaciente(new Paciente("PAC-1", "Carlos", "Martínez", "001-0000001-1", "809-555-0101", new Date(92, 2, 10), "Masculino", 75.0f, 1.75f, "O+", "Calle Principal #12"));
        regPaciente(new Paciente("PAC-2", "Ana", "Gómez", "001-0000002-2", "809-555-0202", new Date(95, 6, 20), "Femenino", 60.0f, 1.65f, "A+", "Av. Central #45"));
        regPaciente(new Paciente("PAC-3", "Luis", "Hernández", "001-0000003-3", "809-555-0303", new Date(88, 11, 5), "Masculino", 82.0f, 1.80f, "B+", "Calle Sol #8"));
        regPaciente(new Paciente("PAC-4", "Laura", "Díaz", "001-0000004-4", "809-555-0404", new Date(99, 1, 14), "Femenino", 55.0f, 1.60f, "AB+", "Calle Luna #23"));
        regPaciente(new Paciente("PAC-5", "Pedro", "Sánchez", "001-0000005-5", "809-555-0505", new Date(91, 8, 30), "Masculino", 90.0f, 1.78f, "O-", "Av. Las Flores #10"));

        long now = System.currentTimeMillis();
        long day = 24 * 60 * 60 * 1000L;

        regCita(new Cita("CIT-1", pacientes.get(0), doctores.get(0), new Date(now + day)));
        regCita(new Cita("CIT-2", pacientes.get(1), doctores.get(1), new Date(now + 2 * day)));

        regCita(new Cita("CIT-3", pacientes.get(2), doctores.get(0), new Date(now - day)));
        citas.get(citas.size() - 1).marcarNoAsistio();

        regCita(new Cita("CIT-4", pacientes.get(3), doctores.get(1), new Date(now + 3 * day)));

        regCita(new Cita("CIT-5", pacientes.get(4), doctores.get(0), new Date(now - 2 * day)));
        citas.get(citas.size() - 1).cancelar();

        regCita(new Cita("CIT-6", pacientes.get(0), doctores.get(1), new Date(now + 4 * day)));
        regCita(new Cita("CIT-7", pacientes.get(1), doctores.get(0), new Date(now + 5 * day)));
        regCita(new Cita("CIT-8", pacientes.get(2), doctores.get(1), new Date(now + 6 * day)));

        regCita(new Cita("CIT-9", pacientes.get(3), doctores.get(0), new Date(now - 3 * day)));
        citas.get(citas.size() - 1).marcarNoAsistio();

        regCita(new Cita("CIT-10", pacientes.get(4), doctores.get(1), new Date(now + 7 * day)));
    }
}