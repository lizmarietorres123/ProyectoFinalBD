package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import logico.consultorio.Clinica;
import logico.Doctor;
import servidor.Servidor;
import visual.consultorio.*;
import visual.enfermeria.ListarVacuna;
import visual.standby.*;
import visual.enfermeria.RegistrarVacuna;

public class Main extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblImagen;
    private JPanel panelLateral;
    private Dimension dim;
    private static Socket sfd = null;
    private static DataInputStream EntradaSocket;
    private static DataOutputStream SalidaSocket;

    private JMenu mnAdmin;
    private JMenu mnRegistro;
    private JMenu mnListado;

    private JMenu mnRegAdminSub;
    private JMenu mnRegGeneralSub;
    private JMenu mnListAdminSub;
    private JMenu mnListGeneralSub;

    private JMenuItem mntmRespaldo;
    private JMenuItem mntmCargarRespaldo;
    private JMenuItem mntmReporte;

    private JMenuItem mntmEnfermedad;
    private JMenuItem mntmVacuna;
    private JMenuItem mntmDoctor;
    private JMenuItem mntmEspecialidad;
    private JMenuItem mntmUsuario;
    private JMenuItem mntmPaciente;
    private JMenuItem mntmCita;
    private JMenuItem mntmConsulta;

    private JMenuItem mntmListarConsultas;
    private JMenuItem mntmListarCitas;
    private JMenuItem mntmListarPacientes;
    private JMenuItem mntmListarEnfermedades;
    private JMenuItem mntmListarVacunas;
    private JMenuItem mntmListarDoctores;
    private JMenuItem mntmListarUsuarios;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main frame = new Main();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
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

        setTitle("Sistema de Gestión Clínica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dim = getToolkit().getScreenSize();
        setSize(dim.width - 40, dim.height - 60);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(235, 242, 250));
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        // Panel Lateral con paleta azul clínico
        panelLateral = new JPanel();
        panelLateral.setBackground(new Color(18, 53, 91));
        panelLateral.setPreferredSize(new Dimension(280, dim.height));
        panelLateral.setLayout(null);
        contentPane.add(panelLateral, BorderLayout.WEST);

        JLabel lblLogo = new JLabel("🏥");
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        lblLogo.setBounds(0, 20, 280, 50);
        panelLateral.add(lblLogo);

        JLabel lblTitulo = new JLabel("CLÍNICA MÉDICA");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(0, 70, 280, 30);
        panelLateral.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Panel de Control");
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(175, 205, 235));
        lblSubtitulo.setBounds(0, 98, 280, 20);
        panelLateral.add(lblSubtitulo);

        // Estilizado de Barras de Menú
        JMenuBar menuBarAdmin = crearMenuBarEstilizado(20, 150);
        mnAdmin = crearMenuEstilizado("  Respaldo y Administración", "recursos/admin.png");
        estilarPopupMenu(mnAdmin);
        menuBarAdmin.add(mnAdmin);
        panelLateral.add(menuBarAdmin);

        mntmRespaldo = crearMenuItem(" Crear Respaldo", "recursos/respaldo.png");
        mntmRespaldo.addActionListener(e -> crearRespaldo());
        mnAdmin.add(mntmRespaldo);

        mntmCargarRespaldo = crearMenuItem(" Cargar Respaldo", "recursos/descargar.png");
        mntmCargarRespaldo.addActionListener(e -> cargarRespaldo());
        mnAdmin.add(mntmCargarRespaldo);

        mntmReporte = crearMenuItem(" Reporte General", "recursos/reporte.png");
        mntmReporte.addActionListener(e -> {
            Reporte reporte = new Reporte();
            reporte.setModal(true);
            reporte.setVisible(true);
        });
        mnAdmin.add(mntmReporte);

        JMenuBar menuBarReg = crearMenuBarEstilizado(20, 215);
        mnRegistro = crearMenuEstilizado("  Registros", "recursos/registro.png");
        estilarPopupMenu(mnRegistro);
        menuBarReg.add(mnRegistro);
        panelLateral.add(menuBarReg);

        mnRegGeneralSub = crearSubMenu(" Generales");
        mnRegistro.add(mnRegGeneralSub);

        mntmPaciente = crearMenuItem(" Paciente", null);
        mntmPaciente.addActionListener(e -> new RegistrarPaciente(null).setVisible(true));
        mnRegGeneralSub.add(mntmPaciente);

        mntmCita = crearMenuItem(" Cita Médica", null);
        mntmCita.addActionListener(e -> new RegistrarCita().setVisible(true));
        mnRegGeneralSub.add(mntmCita);

        mntmConsulta = crearMenuItem(" Realizar Consulta", null);
        mntmConsulta.addActionListener(e -> new RealizarConsulta().setVisible(true));
        mnRegGeneralSub.add(mntmConsulta);

        mnRegAdminSub = crearSubMenu(" Administración");
        mnRegistro.add(mnRegAdminSub);

        mntmUsuario = crearMenuItem(" Usuario", null);
        mntmUsuario.addActionListener(e -> new RegistrarUsuario(null, false).setVisible(true));
        mnRegAdminSub.add(mntmUsuario);

        mntmDoctor = crearMenuItem(" Doctor", null);
        mntmDoctor.addActionListener(e -> new RegistrarDoctor(null, false).setVisible(true));
        mnRegAdminSub.add(mntmDoctor);

        mntmVacuna = crearMenuItem(" Vacuna", null);
        mntmVacuna.addActionListener(e -> new RegistrarVacuna(null).setVisible(true));
        mnRegAdminSub.add(mntmVacuna);

        mntmEnfermedad = crearMenuItem(" Enfermedad", null);
        mntmEnfermedad.addActionListener(e -> new RegEnfermedad(null).setVisible(true));
        mnRegAdminSub.add(mntmEnfermedad);

        mntmEspecialidad = crearMenuItem(" Especialidad", null);
        mntmEspecialidad.addActionListener(e -> new RegistrarEspecialidad().setVisible(true));
        mnRegAdminSub.add(mntmEspecialidad);

        JMenuBar menuBarList = crearMenuBarEstilizado(20, 280);
        mnListado = crearMenuEstilizado("  Listados", "recursos/listado.png");
        estilarPopupMenu(mnListado);
        menuBarList.add(mnListado);
        panelLateral.add(menuBarList);

        mnListGeneralSub = crearSubMenu(" Generales");
        mnListado.add(mnListGeneralSub);

        mntmListarCitas = crearMenuItem(" Citas Registradas", null);
        mntmListarCitas.addActionListener(e -> new ListarCita().setVisible(true));
        mnListGeneralSub.add(mntmListarCitas);

        mntmListarConsultas = crearMenuItem(" Consultas Realizadas", null);
        mntmListarConsultas.addActionListener(e -> {
            Doctor doctorActual = Clinica.getDoctorActual();
            //new ListarConsulta(doctorActual).setVisible(true);
        });
        mnListGeneralSub.add(mntmListarConsultas);

        mntmListarPacientes = crearMenuItem(" Pacientes Registrados", null);
        mntmListarPacientes.addActionListener(e -> new ListarPaciente().setVisible(true));
        mnListGeneralSub.add(mntmListarPacientes);

        mnListAdminSub = crearSubMenu(" Administración");
        mnListado.add(mnListAdminSub);

        mntmListarUsuarios = crearMenuItem(" Usuarios del Sistema", null);
        mntmListarUsuarios.addActionListener(e -> new ListarUsuario().setVisible(true));
        mnListAdminSub.add(mntmListarUsuarios);

        mntmListarDoctores = crearMenuItem(" Doctores Registrados", null);
        mntmListarDoctores.addActionListener(e -> new ListarDoctor().setVisible(true));
        mnListAdminSub.add(mntmListarDoctores);

        mntmListarVacunas = crearMenuItem(" Vacunas Registradas", null);
        mntmListarVacunas.addActionListener(e -> new ListarVacuna(null, "Ver Detalles").setVisible(true));
        mnListAdminSub.add(mntmListarVacunas);

        mntmListarEnfermedades = crearMenuItem(" Enfermedades Registradas", null);
        mntmListarEnfermedades.addActionListener(e -> new ListarEnfermedad().setVisible(true));
        mnListAdminSub.add(mntmListarEnfermedades);

        // Panel Central con fondo en degradado azul suave
        JPanel panelCentral = new JPanel(new BorderLayout()) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(215, 232, 245),
                        0, getHeight(), new Color(240, 246, 252)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPane.add(panelCentral, BorderLayout.CENTER);

        lblImagen = new JLabel("");
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        panelCentral.add(lblImagen, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                cargarImagenCentral();
            }
        });

        cargarImagenCentral();
        configurarPermisos();
    }

    private JMenuBar crearMenuBarEstilizado(int x, int y) {
        JMenuBar mb = new JMenuBar() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 80, 130));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        mb.setOpaque(false);
        mb.setBorder(null);
        mb.setBounds(x, y, 240, 48);
        return mb;
    }

    private JMenu crearMenuEstilizado(String texto, String rutaIcono) {
        JMenu menu = new JMenu(texto);
        if (rutaIcono != null) {
            menu.setIcon(cargarIcono(rutaIcono, 22, 22));
        }
        menu.setFont(new Font("Segoe UI", Font.BOLD, 15));
        menu.setForeground(Color.WHITE);
        menu.setPreferredSize(new Dimension(240, 48));
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return menu;
    }

    private void estilarPopupMenu(JMenu menu) {
        menu.getPopupMenu().setBackground(new Color(25, 65, 105));
        menu.getPopupMenu().setBorder(BorderFactory.createLineBorder(new Color(18, 53, 91), 1));
    }

    private JMenu crearSubMenu(String texto) {
        JMenu subMenu = new JMenu(texto);
        subMenu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        subMenu.setForeground(Color.WHITE);
        subMenu.setBackground(new Color(25, 65, 105));
        subMenu.setOpaque(true);
        subMenu.getPopupMenu().setBackground(new Color(25, 65, 105));
        subMenu.getPopupMenu().setBorder(BorderFactory.createLineBorder(new Color(18, 53, 91), 1));
        return subMenu;
    }

    private JMenuItem crearMenuItem(String texto, String rutaIcono) {
        JMenuItem item = new JMenuItem(texto);
        if (rutaIcono != null) {
            item.setIcon(cargarIcono(rutaIcono, 18, 18));
        }
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setBackground(new Color(25, 65, 105));
        item.setForeground(Color.WHITE);
        item.setOpaque(true);
        return item;
    }

    private void crearRespaldo() {
        try (FileOutputStream clinicaOut = new FileOutputStream("clinica.dat");
             ObjectOutputStream clinicaWrite = new ObjectOutputStream(clinicaOut)) {
            Clinica.getInstancia().guardarContadores();
            clinicaWrite.writeObject(Clinica.getInstancia());

            enviarArchivo("clinica", "clinica.dat");
            JOptionPane.showMessageDialog(null, "Respaldo enviado exitosamente al servidor", "Respaldo Completado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al crear respaldo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enviarArchivo(String tipo, String nombreArchivo) {
        try {
            sfd = new Socket("127.0.0.1", 7000);
            File archivo = new File(nombreArchivo);
            if (!archivo.exists()) return;

            EntradaSocket = new DataInputStream(new FileInputStream(archivo));
            SalidaSocket = new DataOutputStream(sfd.getOutputStream());
            SalidaSocket.writeUTF(tipo);

            int unByte;
            while ((unByte = EntradaSocket.read()) != -1) {
                SalidaSocket.write(unByte);
            }
            SalidaSocket.flush();

            EntradaSocket.close();
            SalidaSocket.close();
            sfd.close();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Error de comunicación: " + ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarPermisos() {
        if (Clinica.getInstancia().getUsuarioActual() != null) {
            String tipoUsuario = Clinica.getInstancia().getUsuarioActual().getTipo();

            if (tipoUsuario.equalsIgnoreCase("Admin") || tipoUsuario.equalsIgnoreCase("Administrador")) {
                mnAdmin.setEnabled(true);
                mnRegAdminSub.setVisible(true);
                mnListAdminSub.setVisible(true);
            } else {
                mnAdmin.setEnabled(false);
                mnRegAdminSub.setVisible(false);
                mnListAdminSub.setVisible(false);
            }
        }
    }

    private void cargarRespaldo() {
        JFileChooser fileChooser = new JFileChooser(new File("."));
        fileChooser.setDialogTitle("Seleccionar archivo de respaldo de la clínica");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileNameExtensionFilter filtroDat = new FileNameExtensionFilter("Archivos de respaldo (.dat)", "dat");
        fileChooser.setFileFilter(filtroDat);

        int resultado = fileChooser.showOpenDialog(null);
        if (resultado != JFileChooser.APPROVE_OPTION) return;

        File archivo = fileChooser.getSelectedFile();
        try (ObjectInputStream clinicaIn = new ObjectInputStream(new FileInputStream(archivo))) {
            Clinica instancia = (Clinica) clinicaIn.readObject();
            Clinica.getInstancia().setClinica(instancia);
            Clinica.getInstancia().asignarContadores();

            JOptionPane.showMessageDialog(null, "Respaldo restaurado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ImageIcon cargarIcono(String ruta, int ancho, int alto) {
        try {
            ImageIcon icon = new ImageIcon(ruta);
            Image img = icon.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    private void cargarImagenCentral() {
        try {
            ImageIcon icon = new ImageIcon("recursos/fondo_clinica.png");
            if (icon.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
                int ancho = getWidth() - panelLateral.getWidth() - 40;
                int alto = getHeight() - 40;
                Image img = icon.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(img));
                lblImagen.setText("");
            }
        } catch (Exception e) {
            lblImagen.setText("Bienvenido al Sistema de Gestión Clínica");
            lblImagen.setFont(new Font("Segoe UI", Font.BOLD, 24));
            lblImagen.setForeground(new Color(18, 53, 91));
        }
    }

    private void guardarDatos() {
        try (FileOutputStream writeFile = new FileOutputStream("clinica.dat");
             ObjectOutputStream writeObjeto = new ObjectOutputStream(writeFile)) {
            Clinica.getInstancia().guardarContadores();
            writeObjeto.writeObject(Clinica.getInstancia());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}