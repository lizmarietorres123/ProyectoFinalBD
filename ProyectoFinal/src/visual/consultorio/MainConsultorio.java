package visual.consultorio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import logico.Clinica;
import logico.catalogo.Doctor;
import logico.catalogo.Usuario;

public class MainConsultorio extends JFrame {

    private static final long serialVersionUID = 1L;

    // Paleta de colores consistente con la interfaz
    private static final Color AZUL_TITULO = new Color(70, 130, 180);
    private static final Color AZUL_SUBTITULO = new Color(100, 149, 237);
    private static final Color AZUL_FONDO_LATERAL = new Color(176, 224, 230);
    private static final Color AZUL_TARJETA = new Color(224, 247, 250);
    private static final Color AZUL_BORDE = new Color(173, 216, 230);

    private static final int ALTO_SUBOPCION = 38;

    private JPanel panelLateral;
    private JPanel panelContenidoMenu;

    private JLabel lblValorUsuario;
    private JLabel lblValorNombre;

    private static Socket sfd = null;
    private static DataInputStream EntradaSocket;
    private static DataOutputStream SalidaSocket;

    public MainConsultorio(Usuario usuario) {

        Clinica.getInstancia().cargarBD();

        setTitle("Sistema de Gestión Clínica - Panel Consultorio");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setSize(850, 650);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // Guardado de datos y cierre ordenado al presionar la 'X'
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarDatos();
                dispose();
            }
        });

        panelLateral = new JPanel();
        panelLateral.setBackground(AZUL_FONDO_LATERAL);
        panelLateral.setPreferredSize(new Dimension(260, getHeight()));
        panelLateral.setLayout(new BorderLayout());
        getContentPane().add(panelLateral, BorderLayout.WEST);

        panelLateral.add(crearPanelSuperior(), BorderLayout.NORTH);
        panelContenidoMenu = new JPanel();
        panelContenidoMenu.setOpaque(false);
        panelContenidoMenu.setLayout(new BoxLayout(panelContenidoMenu, BoxLayout.Y_AXIS));
        panelContenidoMenu.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panelLateral.add(panelContenidoMenu, BorderLayout.CENTER);

        // --- MÓDULO 1: PACIENTE ---
        crearModulo(
                "Paciente",
                "recursos/registro.png",
                new String[]{"Registrar", "Listar"},
                new ActionListener[]{
                        e -> abrirRegistrarPaciente(),
                        e -> abrirListarPaciente()
                }
        );

        // --- MÓDULO 2: CITA ---
        crearModulo(
                "Cita",
                "recursos/consulta.png",
                new String[]{"Registrar", "Listar"},
                new ActionListener[]{
                        e -> abrirRegistrarCita(),
                        e -> abrirListarCita()
                }
        );

        // --- MÓDULO 3: CONSULTA ---
        crearModulo(
                "Consulta",
                "recursos/listado.png",
                new String[]{"Registrar", "Listar"},
                new ActionListener[]{
                        e -> abrirRealizarConsulta(),
                        e -> abrirListarConsulta()
                }
        );

        // --- MÓDULO 4: REPORTE ---
        crearModulo(
                "Reporte",
                "recursos/listado.png",
                new String[]{"PacienteXMes", "Rendimiento General"},
                new ActionListener[]{
                        e -> generarReportePacienteXMes(),
                        e -> generarReporteRendimientoGeneral()
                }
        );

        panelContenidoMenu.add(Box.createVerticalGlue());
        panelLateral.add(crearPiePanelLateral(), BorderLayout.SOUTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(Color.WHITE);
        getContentPane().add(panelCentral, BorderLayout.CENTER);

        cargarDatosUsuario(usuario);
    }

    public MainConsultorio() {
        this(Clinica.getInstancia().getUsuarioActual());
    }

    public void cargarDatosUsuario(Usuario usuario) {
        if (usuario != null) {
            String username = usuario.getNombre();
            if (lblValorUsuario != null) {
                lblValorUsuario.setText(username != null ? username : "");
            }

            Doctor doctor = Clinica.getInstancia().buscarDoctorXUsuario(usuario);
            if (lblValorNombre != null) {
                if (doctor != null) {
                    lblValorNombre.setText(doctor.getNombre() + " " + doctor.getApellido());
                } else {
                    lblValorNombre.setText("Sin Nombre");
                }
            }
        } else {
            if (lblValorUsuario != null) lblValorUsuario.setText("invitado");
            if (lblValorNombre != null) lblValorNombre.setText("Sin Nombre");
        }
    }

    private JPanel crearPanelSuperior() {
        JPanel contenedor = new JPanel();
        contenedor.setOpaque(false);
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));

        JPanel panelHeader = new JPanel();
        panelHeader.setOpaque(false);
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));

        JLabel lblTitulo = new JLabel("CLÍNICA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 22));
        lblTitulo.setForeground(AZUL_TITULO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestión", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        lblSubtitulo.setForeground(AZUL_SUBTITULO);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelHeader.add(lblTitulo);
        panelHeader.add(Box.createVerticalStrut(2));
        panelHeader.add(lblSubtitulo);

        contenedor.add(panelHeader);
        contenedor.add(crearDivisor());

        JPanel panelDatos = new JPanel();
        panelDatos.setOpaque(false);
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        lblValorUsuario = new JLabel(" ");
        lblValorNombre = new JLabel(" ");

        panelDatos.add(crearFilaDato("Usuario:", lblValorUsuario));
        panelDatos.add(Box.createVerticalStrut(4));
        panelDatos.add(crearFilaDato("Nombre:", lblValorNombre));
        panelDatos.add(Box.createVerticalStrut(8));
        panelDatos.add(crearInsigniaRol("Panel Consultorio"));

        contenedor.add(panelDatos);
        contenedor.add(crearDivisor());

        return contenedor;
    }

    private JPanel crearFilaDato(String etiqueta, JLabel lblValor) {
        JPanel fila = new JPanel(new BorderLayout(6, 0));
        fila.setOpaque(false);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(220, 22));

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEtiqueta.setForeground(AZUL_TITULO);

        lblValor.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        lblValor.setForeground(AZUL_TITULO);
        lblValor.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AZUL_BORDE));

        fila.add(lblEtiqueta, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.CENTER);
        return fila;
    }

    private JLabel crearInsigniaRol(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lbl.setForeground(AZUL_TITULO);
        lbl.setOpaque(true);
        lbl.setBackground(AZUL_TARJETA);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(220, 26));
        lbl.setPreferredSize(new Dimension(220, 26));
        lbl.setBorder(BorderFactory.createLineBorder(AZUL_BORDE, 1));
        return lbl;
    }

    private JPanel crearDivisor() {
        JPanel divisor = new JPanel();
        divisor.setBackground(AZUL_BORDE);
        divisor.setPreferredSize(new Dimension(260, 2));
        divisor.setMaximumSize(new Dimension(260, 2));
        return divisor;
    }

    private JPanel crearPiePanelLateral() {
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setOpaque(false);
        panelFooter.setPreferredSize(new Dimension(260, 40));
        panelFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AZUL_BORDE));

        JLabel lblPie = new JLabel("\u2695  Consultorio Médico", SwingConstants.CENTER);
        lblPie.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblPie.setForeground(AZUL_TITULO);

        panelFooter.add(lblPie, BorderLayout.CENTER);
        return panelFooter;
    }

    private void crearModulo(String titulo, String rutaIcono, String[] subOpciones, ActionListener[] acciones) {
        JPanel tarjeta = new JPanel();
        tarjeta.setOpaque(false);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.setMaximumSize(new Dimension(260, 180));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new SombraInferior(6),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));

        JLabel lblCategoria = new JLabel(titulo, SwingConstants.CENTER);
        lblCategoria.setFont(new Font("Bahnschrift", Font.BOLD, 15));
        lblCategoria.setForeground(AZUL_TITULO);
        lblCategoria.setOpaque(true);
        lblCategoria.setBackground(Color.WHITE);
        lblCategoria.setIcon(cargarIcono(rutaIcono, 20, 20));
        lblCategoria.setIconTextGap(8);
        lblCategoria.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblCategoria.setMaximumSize(new Dimension(240, 38));
        lblCategoria.setPreferredSize(new Dimension(240, 38));
        lblCategoria.setBorder(BorderFactory.createLineBorder(AZUL_TITULO, 2));

        tarjeta.add(lblCategoria);

        JPanel filaContenido = new JPanel(new BorderLayout());
        filaContenido.setOpaque(false);
        filaContenido.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaContenido.setMaximumSize(new Dimension(240, subOpciones.length * ALTO_SUBOPCION));

        ConectorArbol conector = new ConectorArbol(subOpciones.length, ALTO_SUBOPCION);
        filaContenido.add(conector, BorderLayout.WEST);

        JPanel columnaBotones = new JPanel();
        columnaBotones.setOpaque(false);
        columnaBotones.setLayout(new BoxLayout(columnaBotones, BoxLayout.Y_AXIS));

        for (int i = 0; i < subOpciones.length; i++) {
            BotonRedondeado btnSub = new BotonRedondeado(subOpciones[i]);
            btnSub.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
            btnSub.setForeground(AZUL_TITULO);
            btnSub.setBackground(Color.WHITE);
            btnSub.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnSub.setHorizontalAlignment(SwingConstants.CENTER);
            btnSub.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnSub.setMaximumSize(new Dimension(216, ALTO_SUBOPCION));
            btnSub.setPreferredSize(new Dimension(216, ALTO_SUBOPCION));
            btnSub.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

            btnSub.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btnSub.setBackground(new Color(240, 250, 252));
                    btnSub.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btnSub.setBackground(Color.WHITE);
                    btnSub.repaint();
                }
            });

            if (i < acciones.length && acciones[i] != null) {
                btnSub.addActionListener(acciones[i]);
            }

            columnaBotones.add(btnSub);
        }

        filaContenido.add(columnaBotones, BorderLayout.CENTER);
        tarjeta.add(filaContenido);

        panelContenidoMenu.add(tarjeta);
        panelContenidoMenu.add(Box.createVerticalStrut(8));
    }

    // --- ACCIONES DE NAVEGACIÓN Y VENTANAS ---

    private void abrirRegistrarPaciente() {
        try {
            CrearPaciente reg = new CrearPaciente(null);
            reg.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir registro de paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirListarPaciente() {
        try {
            ListarPaciente list = new ListarPaciente();
            list.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir listado de pacientes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirRegistrarCita() {
        try {
            CrearCita reg = new CrearCita(null);
            reg.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir registro de cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirListarCita() {
        try {
            ListarCita list = new ListarCita();
            list.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir listado de citas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirRealizarConsulta() {
        try {
            CrearConsulta reg = new CrearConsulta();
            reg.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir registro de consulta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirListarConsulta() {
        try {
            Doctor doctorActual = Clinica.getDoctorActual();
            ListarConsulta list = new ListarConsulta(doctorActual);
            list.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir listado de consultas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- EJECUCIÓN DE CLASES DE REPORTES ---

    private void generarReportePacienteXMes() {
        Doctor doctorActual = Clinica.getDoctorActual();
        if (doctorActual == null) {
            Usuario usuarioActual = Clinica.getInstancia().getUsuarioActual();
            if (usuarioActual != null) {
                doctorActual = Clinica.getInstancia().buscarDoctorXUsuario(usuarioActual);
            }
        }

        if (doctorActual == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la información del doctor logueado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte Pacientes por Mes");
        fileChooser.setSelectedFile(new File("Reporte_Mes_Pico_Doctor.xlsx"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Excel (*.xlsx)", "xlsx"));

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            String rutaArchivo = archivo.getAbsolutePath();
            if (!rutaArchivo.endsWith(".xlsx")) {
                rutaArchivo += ".xlsx";
            }

            try {
                ReporteDoctor.generarReporteMesPico(doctorActual.getIdNumber(), rutaArchivo);
                JOptionPane.showMessageDialog(this, "Reporte generado exitosamente en:\n" + rutaArchivo, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar el reporte de doctor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generarReporteRendimientoGeneral() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte de Rendimiento General");
        fileChooser.setSelectedFile(new File("Reporte_Rendimiento_General.xlsx"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Excel (*.xlsx)", "xlsx"));

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            String rutaArchivo = archivo.getAbsolutePath();
            if (!rutaArchivo.endsWith(".xlsx")) {
                rutaArchivo += ".xlsx";
            }

            try {
                ReporteRendimientoGeneral.generarReporteGeneral(rutaArchivo);
                JOptionPane.showMessageDialog(this, "Reporte general generado exitosamente en:\n" + rutaArchivo, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar el reporte de rendimiento general: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- PERSISTENCIA Y RESPALDOS ---

    private void guardarDatos() {
        try (FileOutputStream writeFile = new FileOutputStream("clinica.dat");
             ObjectOutputStream writeObjeto = new ObjectOutputStream(writeFile)) {
            Clinica.getInstancia().guardarContadores();
            writeObjeto.writeObject(Clinica.getInstancia());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void crearRespaldo() {
        try (FileOutputStream clinicaOut = new FileOutputStream("clinica.dat");
             ObjectOutputStream clinicaWrite = new ObjectOutputStream(clinicaOut)) {
            Clinica.getInstancia().guardarContadores();
            clinicaWrite.writeObject(Clinica.getInstancia());

            enviarArchivo("clinica", "clinica.dat");
            JOptionPane.showMessageDialog(null, "Respaldo enviado exitosamente al servidor", "Respaldo Completado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al crear respaldo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarRespaldo() {
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

    private ImageIcon cargarIcono(String ruta, int ancho, int alto) {
        try {
            ImageIcon icon = new ImageIcon(ruta);
            Image img = icon.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    // --- COMPONENTES VISUALES ---

    private static class SombraInferior implements Border {
        private final int grosor;

        SombraInferior(int grosor) {
            this.grosor = grosor;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int startY = y + height - grosor - 4;
            for (int i = 0; i < grosor; i++) {
                int alpha = Math.max(0, 60 - (i * (60 / grosor)));
                g2.setColor(new Color(70, 130, 180, alpha));
                g2.fillRect(x + 4, startY + i, width - 8, 1);
            }
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, grosor + 4, 0);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    private static class ConectorArbol extends JComponent {
        private final int cantidadItems;
        private final int alturaItem;

        ConectorArbol(int cantidadItems, int alturaItem) {
            this.cantidadItems = cantidadItems;
            this.alturaItem = alturaItem;
            setOpaque(false);
            setPreferredSize(new Dimension(18, cantidadItems * alturaItem));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(AZUL_TITULO);

            int xTronco = getWidth() - 6;
            int yInicio = 0;
            int yFin = (cantidadItems - 1) * alturaItem + (alturaItem / 2);
            g2.drawLine(xTronco, yInicio, xTronco, yFin);

            for (int i = 0; i < cantidadItems; i++) {
                int yCentro = i * alturaItem + (alturaItem / 2);
                g2.drawLine(xTronco, yCentro, getWidth(), yCentro);
            }
            g2.dispose();
        }
    }

    private static class BotonRedondeado extends JButton {
        BotonRedondeado(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            g2.setColor(AZUL_BORDE);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Usuario userPrueba = new Usuario(1, "admin", "admin", "Doctor", "activo");
                MainConsultorio frame = new MainConsultorio(userPrueba);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}