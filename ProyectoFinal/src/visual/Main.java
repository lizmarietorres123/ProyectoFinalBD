package visual;

import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import logico.Clinica;
import logico.Doctor;
import servidor.Servidor;
import visual.registro.RealizarConsulta;
import visual.registro.RegEnfermedad;
import visual.registro.RegistrarCita;
import visual.registro.RegistrarDoctor;
import visual.registro.RegistrarEspecialidad;
import visual.registro.RegistrarPaciente;
import visual.registro.RegistrarUsuario;
import visual.registro.RegistrarVacuna;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.SwingConstants;
import javax.swing.JFileChooser;

public class Main extends JFrame {
    private JPanel contentPane;
    private Doctor auxDoctor = null;
    private JLabel lblImagen;
    private JPanel panelLateral;
    private Dimension dim;
    private static Socket sfd = null;
    private static DataInputStream EntradaSocket;
    private static DataOutputStream SalidaSocket;
    private JMenu mnAdmin;
    private JMenu mnRegistro;
    private JMenu mnListado;
    
    private JMenuItem mntmRespaldo;
    private JMenuItem mntmCargarRespaldo;
    private JMenuItem mntmEnfermedad;
    private JMenuItem mntmVacuna;
    private JMenuItem mntmReporte;
    private JMenuItem mntmDoctor;
    private JMenuItem mntmEspecialidad;
    private JMenuItem mntmUsuario;
    
    private JMenuItem mntmPaciente;
    private JMenuItem mntmCita;
    private JMenuItem mntmConsulta;

    private JMenuItem mntmListarConsultas;
    private JMenuItem mntmListarPacientes;
    private JMenuItem mntmListarEnfermedades;
    private JMenuItem mntmListarVacunas;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main frame = new Main();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Main() {
        Servidor servidor = new Servidor(7000);
        servidor.start();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarDatos();
            }
        });
        
        setTitle("Sistema de Gestion Clinica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dim = getToolkit().getScreenSize();
        setSize(dim.width - 40, dim.height - 60);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setForeground(Color.WHITE);
        contentPane.setBackground(new Color(240, 248, 255));
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());
        
        panelLateral = new JPanel();
        panelLateral.setBackground(new Color(176, 224, 230));
        panelLateral.setPreferredSize(new Dimension(280, dim.height));
        panelLateral.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        contentPane.add(panelLateral, BorderLayout.WEST);
        panelLateral.setLayout(null);
        
        JLabel lblTitulo = new JLabel("CLINICA");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(70, 130, 180));
        lblTitulo.setBounds(0, 20, 280, 40);
        panelLateral.add(lblTitulo);
        
        JLabel lblSubtitulo = new JLabel("Sistema de Gestion");
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(100, 149, 237));
        lblSubtitulo.setBounds(0, 60, 280, 20);
        panelLateral.add(lblSubtitulo);
        
        JMenuBar menuBarAdmin = new JMenuBar();
        menuBarAdmin.setBackground(new Color(224, 247, 250));
        menuBarAdmin.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        menuBarAdmin.setBounds(20, 110, 240, 52);
        panelLateral.add(menuBarAdmin);

        mnAdmin = new JMenu(" Administracion");
        mnAdmin.setIcon(cargarIcono("recursos/admin.png", 40, 40));
        mnAdmin.setBackground(Color.WHITE);
        mnAdmin.setForeground(new Color(70, 130, 180));
        mnAdmin.setFont(new Font("Bahnschrift", Font.BOLD, 18));
        mnAdmin.setPreferredSize(new Dimension(240, 52));
        mnAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mnAdmin.setHorizontalTextPosition(SwingConstants.RIGHT);
        mnAdmin.setIconTextGap(10);
        menuBarAdmin.add(mnAdmin);

        mntmRespaldo = new JMenuItem(" Crear Respaldo");
        mntmRespaldo.setIcon(cargarIcono("recursos/respaldo.png", 24, 24));
        mntmRespaldo.setPreferredSize(new Dimension(240, 40));
        mntmRespaldo.setBackground(Color.WHITE);
        mntmRespaldo.setForeground(new Color(70, 130, 180));
        mntmRespaldo.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mntmRespaldo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                crearRespaldo();
            }
        });
        mnAdmin.add(mntmRespaldo);

        mntmCargarRespaldo = new JMenuItem(" Cargar Respaldo");
        mntmCargarRespaldo.setIcon(cargarIcono("recursos/descargar.png", 24, 24));
        mntmCargarRespaldo.setPreferredSize(new Dimension(240, 40));
        mntmCargarRespaldo.setBackground(Color.WHITE);
        mntmCargarRespaldo.setForeground(new Color(70, 130, 180));
        mntmCargarRespaldo.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mntmCargarRespaldo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarRespaldo();
            }
        });
        mnAdmin.add(mntmCargarRespaldo);

        mntmReporte = new JMenuItem(" Reporte");
        mntmReporte.setIcon(cargarIcono("recursos/reporte.png", 24, 24));
        mntmReporte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Reporte reporte = new Reporte();
                reporte.setModal(true);
                reporte.setVisible(true);
            }
        });
        mntmReporte.setPreferredSize(new Dimension(240, 40));
        mntmReporte.setBackground(Color.WHITE);
        mntmReporte.setForeground(new Color(70, 130, 180));
        mntmReporte.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mnAdmin.add(mntmReporte);
        

        JMenuBar menuBarReg = new JMenuBar();
        menuBarReg.setBackground(new Color(224, 247, 250));
        menuBarReg.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        menuBarReg.setBounds(20, 175, 240, 52);
        panelLateral.add(menuBarReg);

        mnRegistro = new JMenu(" Registro");
        mnRegistro.setIcon(cargarIcono("recursos/registro.png", 40, 40));
        mnRegistro.setBackground(Color.WHITE);
        mnRegistro.setForeground(new Color(70, 130, 180));
        mnRegistro.setFont(new Font("Bahnschrift", Font.BOLD, 18));
        mnRegistro.setPreferredSize(new Dimension(240, 52));
        mnRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mnRegistro.setHorizontalTextPosition(SwingConstants.RIGHT);
        mnRegistro.setIconTextGap(10);
        menuBarReg.add(mnRegistro);

        mntmPaciente = new JMenuItem(" Paciente");
        mntmPaciente.setIcon(cargarIcono("recursos/paciente.png", 24, 24));
        mntmPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                RegistrarPaciente regPaciente = new RegistrarPaciente(null);
                regPaciente.setModal(true);
                regPaciente.setVisible(true);
            }
        });
        mntmPaciente.setPreferredSize(new Dimension(240, 40));
        mntmPaciente.setBackground(Color.WHITE);
        mntmPaciente.setForeground(new Color(70, 130, 180));
        mntmPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mnRegistro.add(mntmPaciente);

        mntmCita = new JMenuItem(" Cita");
        mntmCita.setIcon(cargarIcono("recursos/cita.png", 24, 24));
        mntmCita.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                RegistrarCita regCita = new RegistrarCita();
                regCita.setModal(true);
                regCita.setVisible(true);
            }
        });
        mntmCita.setPreferredSize(new Dimension(240, 40));
        mntmCita.setBackground(Color.WHITE);
        mntmCita.setForeground(new Color(70, 130, 180));
        mntmCita.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mnRegistro.add(mntmCita);

        mntmConsulta = new JMenuItem(" Consulta");
        mntmConsulta.setIcon(cargarIcono("recursos/consulta.png", 24, 24));
        mntmConsulta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RealizarConsulta realizarConsulta = new RealizarConsulta();
                realizarConsulta.setModal(true);
                realizarConsulta.setVisible(true);
            }
        });
        mntmConsulta.setPreferredSize(new Dimension(240, 40));
        mntmConsulta.setBackground(Color.WHITE);
        mntmConsulta.setForeground(new Color(70, 130, 180));
        mntmConsulta.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mnRegistro.add(mntmConsulta);

        mntmEnfermedad = new JMenuItem(" Enfermedad");
        mntmEnfermedad.setIcon(cargarIcono("recursos/enfermedad.png", 24, 24));
        mntmEnfermedad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                RegEnfermedad regEnfermedad = new RegEnfermedad(null);
                regEnfermedad.setModal(true);
                regEnfermedad.setVisible(true);
            }
        });
        mntmEnfermedad.setPreferredSize(new Dimension(240, 40));
        mntmEnfermedad.setBackground(Color.WHITE);
        mntmEnfermedad.setForeground(new Color(70, 130, 180));
        mntmEnfermedad.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mnRegistro.add(mntmEnfermedad);

        mntmVacuna = new JMenuItem(" Vacuna");
        mntmVacuna.setIcon(cargarIcono("recursos/vacuna.png", 24, 24));
        mntmVacuna.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegistrarVacuna manejoVacuna = new RegistrarVacuna(null);
                manejoVacuna.setModal(true);
                manejoVacuna.setVisible(true);
            }
        });
        mntmVacuna.setPreferredSize(new Dimension(240, 40));
        mntmVacuna.setBackground(Color.WHITE);
        mntmVacuna.setForeground(new Color(70, 130, 180));
        mntmVacuna.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mnRegistro.add(mntmVacuna);
        
        mntmDoctor = new JMenuItem(" Doctor");
        mntmDoctor.setIcon(cargarIcono("recursos/doctor.png", 24, 24));
        mntmDoctor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Se asume que la clase se llama RegistrarDoctor y acepta el Frame padre
                RegistrarDoctor regDoctor = new RegistrarDoctor();
                regDoctor.setModal(true);
                regDoctor.setVisible(true);
            }
        });
        mntmDoctor.setPreferredSize(new Dimension(240, 40));
        mntmDoctor.setBackground(Color.WHITE);
        mntmDoctor.setForeground(new Color(70, 130, 180));
        mntmDoctor.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mnRegistro.add(mntmDoctor);

        mntmEspecialidad = new JMenuItem(" Especialidad");
        mntmEspecialidad.setIcon(cargarIcono("recursos/especialidad.png", 24, 24));
        mntmEspecialidad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegistrarEspecialidad regEspecialidad = new RegistrarEspecialidad();
                regEspecialidad.setModal(true);
                regEspecialidad.setVisible(true);
            }
        });
        mntmEspecialidad.setPreferredSize(new Dimension(240, 40));
        mntmEspecialidad.setBackground(Color.WHITE);
        mntmEspecialidad.setForeground(new Color(70, 130, 180));
        mntmEspecialidad.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mnRegistro.add(mntmEspecialidad);

        mntmUsuario = new JMenuItem(" Usuario");
        mntmUsuario.setIcon(cargarIcono("recursos/usuario.png", 24, 24));
        mntmUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegistrarUsuario regUsuario = new RegistrarUsuario();
                regUsuario.setModal(true);
                regUsuario.setVisible(true);
            }
        });
        mntmUsuario.setPreferredSize(new Dimension(240, 40));
        mntmUsuario.setBackground(Color.WHITE);
        mntmUsuario.setForeground(new Color(70, 130, 180));
        mntmUsuario.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mnRegistro.add(mntmUsuario);
        
        JMenuBar menuBarList = new JMenuBar();
        menuBarList.setBackground(new Color(224, 247, 250));
        menuBarList.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        menuBarList.setBounds(20, 240, 240, 52);
        panelLateral.add(menuBarList);

        mnListado = new JMenu(" Listado");
        mnListado.setIcon(cargarIcono("recursos/listado.png", 40, 40));
        mnListado.setPreferredSize(new Dimension(240, 52));
        mnListado.setForeground(new Color(70, 130, 180));
        mnListado.setFont(new Font("Bahnschrift", Font.BOLD, 18));
        mnListado.setBackground(Color.WHITE);
        mnListado.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mnListado.setHorizontalTextPosition(SwingConstants.RIGHT);
        mnListado.setIconTextGap(10);
        menuBarList.add(mnListado);

        mntmListarConsultas = new JMenuItem(" Consultas");
        mntmListarConsultas.setIcon(cargarIcono("recursos/consulta.png", 24, 24));
        mntmListarConsultas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Doctor doctorActual = Clinica.getDoctorActual();
                if(doctorActual != null) {
                    MostrarConsulta mostrarconsulta = new MostrarConsulta(doctorActual);
                    mostrarconsulta.setModal(true);
                    mostrarconsulta.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Debe iniciar sesion como Doctor para ver las consultas.", 
                        "Acceso Denegado", 
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        mntmListarConsultas.setPreferredSize(new Dimension(240, 40));
        mntmListarConsultas.setForeground(new Color(70, 130, 180));
        mntmListarConsultas.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mntmListarConsultas.setBackground(Color.WHITE);
        mnListado.add(mntmListarConsultas);

        mntmListarPacientes = new JMenuItem(" Pacientes");
        mntmListarPacientes.setIcon(cargarIcono("recursos/paciente.png", 24, 24));
        mntmListarPacientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                MostrarPaciente mostrarPacientes = new MostrarPaciente();
                mostrarPacientes.setModal(true);
                mostrarPacientes.setVisible(true);
            }
        });
        mntmListarPacientes.setPreferredSize(new Dimension(240, 40));
        mntmListarPacientes.setForeground(new Color(70, 130, 180));
        mntmListarPacientes.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mntmListarPacientes.setBackground(Color.WHITE);
        mnListado.add(mntmListarPacientes);

        mntmListarEnfermedades = new JMenuItem(" Enfermedades");
        mntmListarEnfermedades.setIcon(cargarIcono("recursos/enfermedad.png", 24, 24));
        mntmListarEnfermedades.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ListarEnfermedad listarEnf = new ListarEnfermedad();
                listarEnf.setModal(true);
                listarEnf.setVisible(true);
            }
        });
        mntmListarEnfermedades.setPreferredSize(new Dimension(240, 40));
        mntmListarEnfermedades.setForeground(new Color(70, 130, 180));
        mntmListarEnfermedades.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mntmListarEnfermedades.setBackground(Color.WHITE);
        mnListado.add(mntmListarEnfermedades);

        mntmListarVacunas = new JMenuItem(" Vacunas");
        mntmListarVacunas.setIcon(cargarIcono("recursos/vacuna.png", 24, 24));
        mntmListarVacunas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ListarVacuna listarVac = new ListarVacuna(null, "Ver Detalles");
                listarVac.setModal(true);
                listarVac.setVisible(true);
            }
        });
        
        mntmListarVacunas.setPreferredSize(new Dimension(240, 40));
        mntmListarVacunas.setForeground(new Color(70, 130, 180));
        mntmListarVacunas.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        mntmListarVacunas.setBackground(Color.WHITE);
        mnListado.add(mntmListarVacunas);
        
        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(240, 248, 255));
        panel_1.setLayout(new BorderLayout());
        contentPane.add(panel_1, BorderLayout.CENTER);
        
        lblImagen = new JLabel("");
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        panel_1.add(lblImagen, BorderLayout.CENTER);
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                cargarImagenCentral();
            }
        });
        
        cargarImagenCentral();
        configurarPermisos();
    }
    
    

    private void crearRespaldo() {
        FileOutputStream clinicaOut;
        ObjectOutputStream clinicaWrite;
        try {
            clinicaOut = new FileOutputStream("clinica.dat");
            clinicaWrite = new ObjectOutputStream(clinicaOut);
            Clinica.getInstancia().guardarContadores();
            clinicaWrite.writeObject(Clinica.getInstancia());
            clinicaOut.close();
            clinicaWrite.close();
            
            enviarArchivo("clinica", "clinica.dat");
            JOptionPane.showMessageDialog(null, 
                "Respaldo enviado exitosamente al servidor", 
                "Respaldo Completado", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error al crear el archivo de respaldo", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error al guardar los datos", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enviarArchivo(String tipo, String nombreArchivo) {
        try {
            sfd = new Socket("127.0.0.1", 7000);
            File archivo = new File(nombreArchivo);
            
            if (!archivo.exists()) {
                JOptionPane.showMessageDialog(null, 
                    "Archivo " + nombreArchivo + " no encontrado", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            EntradaSocket = new DataInputStream(new FileInputStream(archivo));
            SalidaSocket = new DataOutputStream(sfd.getOutputStream());
            
            SalidaSocket.writeUTF(tipo);
            
            int unByte;
            while ((unByte = EntradaSocket.read()) != -1) {
                SalidaSocket.write(unByte);
            }
            SalidaSocket.flush();
            
            if (EntradaSocket != null) {
                EntradaSocket.close();
            }
            if (SalidaSocket != null) {
                SalidaSocket.close();
            }
            if (sfd != null) {
                sfd.close();
            }
        } catch (UnknownHostException uhe) {
            JOptionPane.showMessageDialog(null, 
                "No se puede acceder al servidor: " + uhe.getMessage(),
                "Error de Conexi�n", 
                JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, 
                "Error durante la transferencia: " + ioe.getMessage(), 
                "Error de Comunicaci�n", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void configurarPermisos() {
        if(Clinica.getInstancia().getUsuarioActual() != null) {
            String tipoUsuario = Clinica.getInstancia().getUsuarioActual().getTipo();
            
            if(tipoUsuario.equalsIgnoreCase("Admin")) {
                mnAdmin.setEnabled(true);
                mnRegistro.setEnabled(true);
                mnListado.setEnabled(true);
                mntmEnfermedad.setVisible(true);
                mntmVacuna.setVisible(true);
                
                // Nuevas opciones para Admin
                mntmDoctor.setVisible(true);
                mntmEspecialidad.setVisible(true);
                mntmUsuario.setVisible(true);
                
                mntmListarEnfermedades.setEnabled(true);
                mntmListarEnfermedades.setVisible(true);
                mntmListarVacunas.setEnabled(true);
                mntmListarVacunas.setVisible(true);
                
            } else if(tipoUsuario.equalsIgnoreCase("Doctor")) {
                mnAdmin.setEnabled(false);
                mnRegistro.setEnabled(true);
                mnListado.setEnabled(true);
                mntmEnfermedad.setVisible(false);
                mntmVacuna.setVisible(false);
                
                mntmDoctor.setVisible(false);
                mntmEspecialidad.setVisible(false);
                mntmUsuario.setVisible(false);
                
                mntmListarEnfermedades.setEnabled(false);
                mntmListarEnfermedades.setVisible(false);
                mntmListarVacunas.setEnabled(false);
                mntmListarVacunas.setVisible(false);
                
            } else if(tipoUsuario.equalsIgnoreCase("Staff")) {
                mnAdmin.setEnabled(false);
                mnRegistro.setEnabled(true);
                mnListado.setEnabled(false);
                mntmEnfermedad.setVisible(false);
                mntmVacuna.setVisible(false);
                
                mntmDoctor.setVisible(false);
                mntmEspecialidad.setVisible(false);
                mntmUsuario.setVisible(false);
                
                mntmListarEnfermedades.setEnabled(false);
                mntmListarEnfermedades.setVisible(false);
                mntmListarVacunas.setEnabled(false);
                mntmListarVacunas.setVisible(false);
            }
        }
    
    }

    private void cargarRespaldo() {
        JFileChooser fileChooser = new JFileChooser(new File("."));
        fileChooser.setDialogTitle("Seleccionar archivo de respaldo de la cl�nica");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        FileNameExtensionFilter filtroDat = new FileNameExtensionFilter("Archivos de respaldo (.dat)", "dat");
        fileChooser.setFileFilter(filtroDat);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        int resultado = fileChooser.showOpenDialog(null);
        
        if (resultado != JFileChooser.APPROVE_OPTION) return;
        
        File archivo = fileChooser.getSelectedFile();
        String nombre = archivo.getName();
        
        if (!nombre.startsWith("clinica_respaldo_") || !nombre.endsWith(".dat")) {
            JOptionPane.showMessageDialog(null, 
                "Archivo inv�lido. Debe ser un respaldo tipo 'clinica_respaldo_#.dat'", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(null, 
            "�Deseas restaurar la cl�nica desde este respaldo?\nEsto sobrescribir� los datos actuales.", 
            "Confirmar Restauraci�n", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;
        
        try (ObjectInputStream clinicaIn = new ObjectInputStream(new FileInputStream(archivo))) {
            Clinica instancia = (Clinica) clinicaIn.readObject();
            Clinica.getInstancia().setClinica(instancia);
            Clinica.getInstancia().asignarContadores();
            
            JOptionPane.showMessageDialog(null, 
                "Respaldo restaurado exitosamente.", 
                "Restauraci�n completada", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error al cargar archivo: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private ImageIcon cargarIcono(String ruta, int ancho, int alto) {
        try {
            ImageIcon icon = new ImageIcon(ruta);
            Image img = icon.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + ruta);
            return null;
        }
    }

    private void cargarImagenCentral() {
        try {
            ImageIcon icon = new ImageIcon("recursos/fondo_clinica.png");
            if (icon.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
                int ancho = getWidth() - panelLateral.getWidth() - 40;
                int alto = getHeight() - 40;
                Image img = icon.getImage();
                Image imgScale = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(imgScale);
                lblImagen.setIcon(scaledIcon);
                lblImagen.setText("");
            }
        } catch (Exception e) {
            lblImagen.setIcon(null);
            lblImagen.setText("Bienvenido al Sistema de Gesti�n Cl�nica");
            lblImagen.setFont(new Font("Bahnschrift", Font.BOLD, 24));
            lblImagen.setForeground(new Color(70, 130, 180));
        }
    }
    private void guardarDatos() {
        try {
            FileOutputStream writeFile = new FileOutputStream("clinica.dat");
            ObjectOutputStream writeObjeto = new ObjectOutputStream(writeFile);
            Clinica.getInstancia().guardarContadores();
            writeObjeto.writeObject(Clinica.getInstancia());
            
            writeObjeto.close();
            writeFile.close();
            
            System.out.println("Datos guardados correctamente");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error al guardar los datos: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}