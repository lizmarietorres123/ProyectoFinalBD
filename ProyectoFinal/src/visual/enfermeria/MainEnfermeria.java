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
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
//import visual.ListarAnalisis;
import visual.enfermeria.ListarVacuna;
//import visual.registro.RegistrarAnalisis;
import visual.enfermeria.RegistrarVacuna;

public class MainEnfermeria extends JFrame {

    // --- Paleta de azules del diseño original (se reutiliza en todo el panel) ---
    private static final Color AZUL_TITULO = new Color(70, 130, 180);
    private static final Color AZUL_SUBTITULO = new Color(100, 149, 237);
    private static final Color AZUL_FONDO_LATERAL = new Color(176, 224, 230);
    private static final Color AZUL_TARJETA = new Color(224, 247, 250);
    private static final Color AZUL_BORDE = new Color(173, 216, 230);

    private static final int ALTO_SUBOPCION = 38;

    private JPanel panelLateral;
    private JPanel panelContenidoMenu;

    public MainEnfermeria() {
        setTitle("Sistema de Gestión Clínica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
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
        panelContenidoMenu.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        panelLateral.add(panelContenidoMenu, BorderLayout.CENTER);

        // --- MÓDULO 1: ANÁLISIS (siempre desplegado, nunca se pliega) ---
        crearModulo(
                "Análisis",
                "recursos/consulta.png",
                new String[]{"Registrar", "Listar"},
                new ActionListener[]{
                        //e -> abrirRegistrarAnalisis(),
                        //e -> abrirListarAnalisis()
                }
        );

        // --- MÓDULO 2: VACUNA (siempre desplegado, nunca se pliega) ---
        crearModulo(
                "Vacuna",
                "recursos/vacuna.png",
                new String[]{"Registrar", "Listar"},
                new ActionListener[]{
                        e -> abrirRegistrarVacuna(),
                        e -> abrirListarVacunas()
                }
        );

        panelContenidoMenu.add(Box.createVerticalGlue());

        panelLateral.add(crearPiePanelLateral(), BorderLayout.SOUTH);
    }

    /**
     * Bloque superior de la barra: título, datos de sesión (Usuario / Nombre)
     * y el rótulo del rol activo, siguiendo el boceto de referencia.
     */
    private JPanel crearPanelSuperior() {
        JPanel contenedor = new JPanel();
        contenedor.setOpaque(false);
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));

        // Título y subtítulo
        JPanel panelHeader = new JPanel();
        panelHeader.setOpaque(false);
        panelHeader.setPreferredSize(new Dimension(260, 90));
        panelHeader.setMaximumSize(new Dimension(260, 90));
        panelHeader.setLayout(null);

        JLabel lblTitulo = new JLabel("CLINICA");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 26));
        lblTitulo.setForeground(AZUL_TITULO);
        lblTitulo.setBounds(0, 15, 260, 35);
        panelHeader.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestión");
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
        lblSubtitulo.setForeground(AZUL_SUBTITULO);
        lblSubtitulo.setBounds(0, 48, 260, 20);
        panelHeader.add(lblSubtitulo);

        contenedor.add(panelHeader);
        contenedor.add(crearDivisor());

        // --- Datos de sesión: Usuario / Nombre / Rol activo ---
        JPanel panelDatos = new JPanel();
        panelDatos.setOpaque(false);
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBorder(BorderFactory.createEmptyBorder(14, 20, 12, 20));

        panelDatos.add(crearFilaDato("Usuario:"));
        panelDatos.add(Box.createVerticalStrut(8));
        panelDatos.add(crearFilaDato("Nombre:"));
        panelDatos.add(Box.createVerticalStrut(12));
        panelDatos.add(crearInsigniaRol("Panel Enfermería"));

        contenedor.add(panelDatos);
        contenedor.add(crearDivisor());

        return contenedor;
    }

    /** Fila tipo "Etiqueta: ______" igual al boceto (línea en blanco para el dato). */
    private JPanel crearFilaDato(String etiqueta) {
        JPanel fila = new JPanel(new BorderLayout(6, 0));
        fila.setOpaque(false);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(220, 22));

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblEtiqueta.setForeground(AZUL_TITULO);

        JLabel lblValor = new JLabel(" ");
        lblValor.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AZUL_BORDE));

        fila.add(lblEtiqueta, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.CENTER);
        return fila;
    }

    /** Insignia destacada con el rol activo (reemplaza la línea "Especialidad" del boceto). */
    private JLabel crearInsigniaRol(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lbl.setForeground(AZUL_TITULO);
        lbl.setOpaque(true);
        lbl.setBackground(AZUL_TARJETA);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(220, 32));
        lbl.setPreferredSize(new Dimension(220, 32));
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

    /**
     * Panel decorativo inferior de la barra lateral. El título se mantiene
     * como cierre visual de la barra, ahora identificando el panel activo.
     */
    private JPanel crearPiePanelLateral() {
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setOpaque(false);
        panelFooter.setPreferredSize(new Dimension(260, 50));
        panelFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AZUL_BORDE));

        JLabel lblPie = new JLabel("\u2695  Enfermería", SwingConstants.CENTER);
        lblPie.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblPie.setForeground(AZUL_TITULO);

        panelFooter.add(lblPie, BorderLayout.CENTER);
        return panelFooter;
    }

    /**
     * Crea una tarjeta de módulo siempre desplegada (nunca se pliega, ocupa
     * espacio fijo en la barra), con un encabezado rectangular y sus
     * opciones enlazadas mediante un conector tipo árbol, igual al boceto.
     */
    private void crearModulo(String titulo, String rutaIcono, String[] subOpciones, ActionListener[] acciones) {
        JPanel tarjeta = new JPanel();
        tarjeta.setOpaque(false);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.setMaximumSize(new Dimension(260, 220));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new SombraInferior(8),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));

        // Encabezado de categoría: caja rectangular estática, sin relleno de color
        JLabel lblCategoria = new JLabel(titulo, SwingConstants.CENTER);
        lblCategoria.setFont(new Font("Bahnschrift", Font.BOLD, 17));
        lblCategoria.setForeground(AZUL_TITULO);
        lblCategoria.setOpaque(true);
        lblCategoria.setBackground(Color.WHITE);
        lblCategoria.setIcon(cargarIcono(rutaIcono, 24, 24));
        lblCategoria.setIconTextGap(10);
        lblCategoria.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblCategoria.setMaximumSize(new Dimension(240, 46));
        lblCategoria.setPreferredSize(new Dimension(240, 46));
        lblCategoria.setBorder(BorderFactory.createLineBorder(AZUL_TITULO, 2));

        tarjeta.add(lblCategoria);

        // Fila inferior: conector tipo árbol + columna de opciones
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
            btnSub.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
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
        panelContenidoMenu.add(Box.createVerticalStrut(16));
    }

    // --- Métodos de Acción ---
    /*private void abrirRegistrarAnalisis() {
        RegistrarAnalisis reg = new RegistrarAnalisis();
        reg.setModal(true);
        reg.setVisible(true);
    }

    private void abrirListarAnalisis() {
        ListarAnalisis list = new ListarAnalisis();
        list.setModal(true);
        list.setVisible(true);
    }*/

    private void abrirRegistrarVacuna() {
        RegistrarVacuna reg = new RegistrarVacuna(null);
        reg.setModal(true);
        reg.setVisible(true);
    }

    private void abrirListarVacunas() {
        ListarVacuna list = new ListarVacuna(null, "Ver Detalles");
        list.setModal(true);
        list.setVisible(true);
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

    /**
     * Borde personalizado que dibuja una sombra suave y degradada bajo el
     * componente, simulando elevación tipo "tarjeta".
     */
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

    /**
     * Dibuja el conector tipo árbol (línea vertical + ramas horizontales)
     * que une la caja de categoría con cada opción, igual al boceto a mano.
     */
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

    /**
     * Botón con esquinas redondeadas (igual a las cajas "Registrar"/"Listar"
     * del boceto), pintado a mano para lograr el borde curvo real.
     */
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
}