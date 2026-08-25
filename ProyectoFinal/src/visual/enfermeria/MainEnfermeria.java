package visual.enfermeria;

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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import logico.Clinica;
import logico.catalogo.Enfermera;
import logico.catalogo.Usuario;

public class MainEnfermeria extends JFrame {

    private static final long serialVersionUID = 1L;

    // --- Paleta de colores del diseño original ---
    private static final Color AZUL_TITULO = new Color(70, 130, 180);
    private static final Color AZUL_SUBTITULO = new Color(100, 149, 237);
    private static final Color AZUL_FONDO_LATERAL = new Color(176, 224, 230);
    private static final Color AZUL_TARJETA = new Color(224, 247, 250);
    private static final Color AZUL_BORDE = new Color(173, 216, 230);

    private static final int ALTO_SUBOPCION = 38;

    private JPanel panelLateral;
    private JPanel panelContenidoMenu;

    // Componentes para los datos de sesión activa
    private JLabel lblValorUsuario;
    private JLabel lblValorNombre;

    /**
     * Constructor principal que recibe el objeto Usuario en sesión.
     */
    public MainEnfermeria(Usuario usuario) {

        // Cargar datos de la BD al iniciar
        Clinica.getInstancia().cargarBD();

        setTitle("Sistema de Gestion Clinica - Panel de Enfermeria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ventana con tamaño reducido
        setSize(850, 550);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // --- Panel Lateral Principal ---
        panelLateral = new JPanel();
        panelLateral.setBackground(AZUL_FONDO_LATERAL);
        panelLateral.setPreferredSize(new Dimension(260, getHeight()));
        panelLateral.setLayout(new BorderLayout());
        getContentPane().add(panelLateral, BorderLayout.WEST);

        panelLateral.add(crearPanelSuperior(), BorderLayout.NORTH);

        // Contenedor vertical para los módulos del menú
        panelContenidoMenu = new JPanel();
        panelContenidoMenu.setOpaque(false);
        panelContenidoMenu.setLayout(new BoxLayout(panelContenidoMenu, BoxLayout.Y_AXIS));
        panelContenidoMenu.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        panelLateral.add(panelContenidoMenu, BorderLayout.CENTER);

        // --- MODULO 1: ANALISIS ---
        crearModulo(
                "Analisis",
                "recursos/consulta.png",
                new String[]{"Registrar", "Listar"},
                new ActionListener[]{
                        e -> abrirRegistrarAnalisis(),
                        e -> abrirListarAnalisis()
                }
        );

        // --- MODULO 2: VACUNA ---
        crearModulo(
                "Vacuna",
                "recursos/vacuna.png",
                new String[]{"Registrar", "Listar"},
                new ActionListener[]{
                        e -> abrirRegistrarVacuna(),
                        e -> abrirListarVacuna()
                }
        );

        panelContenidoMenu.add(Box.createVerticalGlue());
        panelLateral.add(crearPiePanelLateral(), BorderLayout.SOUTH);

        // --- Panel Central ---
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(Color.WHITE);
        getContentPane().add(panelCentral, BorderLayout.CENTER);

        cargarDatosUsuario(usuario);
    }

    public MainEnfermeria() {
        this(Clinica.getInstancia().getUsuarioActual());
    }

    public void cargarDatosUsuario(Usuario usuario) {
        if (usuario != null) {
            String username = usuario.getNombre();
            if (lblValorUsuario != null) {
                lblValorUsuario.setText(username != null ? username : "");
            }

            Enfermera enfermera = Clinica.getInstancia().buscarEnfermeraXUsuario(usuario);
            if (lblValorNombre != null) {
                if (enfermera != null) {
                    lblValorNombre.setText(enfermera.getNombreApellido());
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
        panelHeader.setBorder(BorderFactory.createEmptyBorder(12, 10, 10, 10));

        JLabel lblTitulo = new JLabel("CLINICA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 22));
        lblTitulo.setForeground(AZUL_TITULO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestion", SwingConstants.CENTER);
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
        panelDatos.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblValorUsuario = new JLabel(" ");
        lblValorNombre = new JLabel(" ");

        panelDatos.add(crearFilaDato("Usuario:", lblValorUsuario));
        panelDatos.add(Box.createVerticalStrut(6));
        panelDatos.add(crearFilaDato("Nombre:", lblValorNombre));
        panelDatos.add(Box.createVerticalStrut(10));
        panelDatos.add(crearInsigniaRol("Panel Enfermeria"));

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
        lbl.setMaximumSize(new Dimension(220, 28));
        lbl.setPreferredSize(new Dimension(220, 28));
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
        panelFooter.setPreferredSize(new Dimension(260, 45));
        panelFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AZUL_BORDE));

        JLabel lblPie = new JLabel("Enfermeria", SwingConstants.CENTER);
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
        tarjeta.setMaximumSize(new Dimension(260, 200));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new SombraInferior(6),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));

        JLabel lblCategoria = new JLabel(titulo, SwingConstants.CENTER);
        lblCategoria.setFont(new Font("Bahnschrift", Font.BOLD, 16));
        lblCategoria.setForeground(AZUL_TITULO);
        lblCategoria.setOpaque(true);
        lblCategoria.setBackground(Color.WHITE);
        lblCategoria.setIcon(cargarIcono(rutaIcono, 22, 22));
        lblCategoria.setIconTextGap(8);
        lblCategoria.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblCategoria.setMaximumSize(new Dimension(240, 42));
        lblCategoria.setPreferredSize(new Dimension(240, 42));
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
            btnSub.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
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
        panelContenidoMenu.add(Box.createVerticalStrut(12));
    }

    private void abrirRegistrarAnalisis() {
        try {
            CrearDetalleAnalisis list = new CrearDetalleAnalisis();
            list.setModal(true);
            list.setLocationRelativeTo(this);
            list.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir registro de analisis: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirListarAnalisis() {
        try {
            ListarDetalleAnalisis list = new ListarDetalleAnalisis();
            list.setModal(true);
            list.setLocationRelativeTo(this);
            list.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir listado de analisis: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirRegistrarVacuna() {
        try {
            ListarDetalleVacuna list = new ListarDetalleVacuna();
            list.setModal(true);
            list.setLocationRelativeTo(this);
            list.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir registro de vacuna: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirListarVacuna() {
        try {
            ListarDetalleVacuna list = new ListarDetalleVacuna();
            list.setModal(true);
            list.setLocationRelativeTo(this);
            list.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir listado de vacunas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                Usuario userPrueba = new Usuario(2, "enfermera", "1234", "Enfermera", "activo");
                MainEnfermeria frame = new MainEnfermeria(userPrueba);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}