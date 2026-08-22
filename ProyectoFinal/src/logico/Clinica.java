package logico;

import bd.catalogo.*;
import logico.catalogo.*;
import logico.consultorio.*;
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
    }

    public <T> String genId(int idNumber, Class<T> clase){
        return ids.get(clase)+idNumber;
    }

    public <T> int getIdNumber(String id, Class<T> clase){
        return Integer.parseInt(id.replace(ids.get(clase), ""));
    }

    // --- PERSISTENCIA ---

    public void cargarBD(){
        /*
        NOTA: Para que no arroje errores, debes asegurarte de que ConsultaDAO,
        SintomaDAO y VacunaDAO ya estén creados con sus métodos obtener().
        */
        // consultas = ConsultaDAO.getInstance().obtenerConsultas();
        // sintomas = SintomaDAO.getInstance().obtenerSintomas();
        enfermedades = EnfermedadDAO.getInstance().obtenerEnfermedades();
        medicamentos = MedicamentoDAO.getInstance().obtenerMedicamentos();
        analisis = AnalisisDAO.getInstance().obtenerAnalisis();
        // vacunas = VacunaDAO.getInstance().obtenerVacunas();
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

    // --- BÚSQUEDAS POR ID / ATRIBUTO ---

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

    public Cita buscarCitaXId(String id) {
        if (id == null) return null;
        for (Cita c : citas) {
            if (c != null && c.getId() != null && c.getId().equalsIgnoreCase(id)) return c;
        }
        return null;
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

    public Usuario buscarUsuarioXId(String id) {
        if (id == null) return null;
        for (Usuario u : usuarios) {
            if (u != null && u.getId() != null && u.getId().equalsIgnoreCase(id)) return u;
        }
        return null;
    }

    public Doctor buscarDoctorXId(String id) {
        if (id == null) return null;
        for (Doctor d : doctores) {
            if (d != null && d.getId() != null && d.getId().equalsIgnoreCase(id)) return d;
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

    public Sintoma buscarSintomaXId(String id) {
        if (id == null) return null;
        for (Sintoma s : sintomas) {
            if (s != null && s.getId() != null && s.getId().equalsIgnoreCase(id)) return s;
        }
        return null;
    }

    public Medicamento buscarMedicamentoXId(String id) {
        if (id == null) return null;
        for (Medicamento m : medicamentos) {
            if (m != null && m.getId() != null && m.getId().equalsIgnoreCase(id)) return m;
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
        if (this.consultas != null) {
            return this.consultas;
        }
        return new ArrayList<>();
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

    // --- DATOS DE PRUEBA / INICIALIZACIÓN ---

    public void initInfo() {
        Usuario doc = new Usuario(1, "doc", "doc", "Doctor");
        regUsuario(doc);

        Usuario efm = new Usuario(2, "efm", "efm", "Enfermera");
        regUsuario(efm);

        Enfermera enfermera1 = new Enfermera(1, "Ana", "Rodríguez", "001-0000000-0", "809-555-0000", efm);
        enfermeras.add(enfermera1);

        // Doctor corregido usando el constructor completo mapeado a base de datos
        Doctor doctor1 = new Doctor("Dr. Juan", "Pérez", 20,"Internista");
        doctor1.setUsuario(doc);
        regDoctor(doctor1);

        // Pacientes corregidos usando el constructor de ID numérico
        regPaciente(new Paciente(1, "Carlos", "Martínez", "001-0000001-1", "809-555-0101", new Date(92, 2, 10), "Masculino", 75.0f, 1.75f, "O+", "Calle Principal #12","activo"));
        regPaciente(new Paciente(2, "Ana", "Gómez", "001-0000002-2", "809-555-0202", new Date(95, 6, 20), "Femenino", 60.0f, 1.65f, "A+", "Av. Central #45","activo"));
        regPaciente(new Paciente(3, "Luis", "Hernández", "001-0000003-3", "809-555-0303", new Date(88, 11, 5), "Masculino", 82.0f, 1.80f, "B+", "Calle Sol #8","activo"));
        regPaciente(new Paciente(4, "Laura", "Díaz", "001-0000004-4", "809-555-0404", new Date(99, 1, 14), "Femenino", 55.0f, 1.60f, "AB+", "Calle Luna #23","activo"));
        regPaciente(new Paciente(5, "Pedro", "Sánchez", "001-0000005-5", "809-555-0505", new Date(91, 8, 30), "Masculino", 90.0f, 1.78f, "O-", "Av. Las Flores #10","activo"));

        long now = System.currentTimeMillis();
        long day = 24 * 60 * 60 * 1000L;

        // Citas corregidas usando el constructor de ID numérico
        Date f1 = new Date(now + day);
        regCita(new Cita(1, LocalDateTime.now(), f1, new Time(f1.getTime()), EstadoCita.PROGRAMADA, pacientes.get(0), doctores.get(0)));

        Date f2 = new Date(now + 2 * day);
        regCita(new Cita(2, LocalDateTime.now(), f2, new Time(f2.getTime()), EstadoCita.PROGRAMADA, pacientes.get(0), doctores.get(0)));

        Date f3 = new Date(now - day);
        Cita c3 = new Cita(3, LocalDateTime.now(), f3, new Time(f3.getTime()), EstadoCita.PROGRAMADA, pacientes.get(2), doctores.get(0));
        c3.marcarNoAsistio();
        regCita(c3);

        Date f4 = new Date(now + 3 * day);
        regCita(new Cita(4, LocalDateTime.now(), f4, new Time(f4.getTime()), EstadoCita.PROGRAMADA, pacientes.get(3), doctores.get(0)));

        Date f5 = new Date(now - 2 * day);
        Cita c5 = new Cita(5, LocalDateTime.now(), f5, new Time(f5.getTime()), EstadoCita.PROGRAMADA, pacientes.get(4), doctores.get(0));
        c5.cancelar();
        regCita(c5);

        Date f6 = new Date(now + 4 * day);
        regCita(new Cita(6, LocalDateTime.now(), f6, new Time(f6.getTime()), EstadoCita.PROGRAMADA, pacientes.get(0), doctores.get(0)));

        Date f7 = new Date(now + 5 * day);
        regCita(new Cita(7, LocalDateTime.now(), f7, new Time(f7.getTime()), EstadoCita.PROGRAMADA, pacientes.get(1), doctores.get(0)));

        Date f8 = new Date(now + 6 * day);
        regCita(new Cita(8, LocalDateTime.now(), f8, new Time(f8.getTime()), EstadoCita.PROGRAMADA, pacientes.get(2), doctores.get(0)));

        Date f9 = new Date(now - 3 * day);
        Cita c9 = new Cita(9, LocalDateTime.now(), f9, new Time(f9.getTime()), EstadoCita.PROGRAMADA, pacientes.get(3), doctores.get(0));
        c9.marcarNoAsistio();
        regCita(c9);

        Date f10 = new Date(now + 7 * day);
        regCita(new Cita(10, LocalDateTime.now(), f10, new Time(f10.getTime()), EstadoCita.PROGRAMADA, pacientes.get(4), doctores.get(0)));
    }
}