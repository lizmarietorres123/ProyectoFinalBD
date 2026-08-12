package visual.catalogo;

import logico.catalogo.Analisis;
import logico.Clinica;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class ListarAnalisis extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel panelContenedorCards = new JPanel();
    private JTextField txtBuscar;

    public ListarAnalisis(Window parent) {
        super(parent, "Listado de Análisis Clínicos", ModalityType.APPLICATION_MODAL);
        setBounds(100, 100, 650, 520);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(contentPanel, BorderLayout.CENTER);

        // Panel de búsqueda superior
        JPanel panelFiltro = new JPanel(new BorderLayout(8, 8));
        panelFiltro.setBackground(new Color(240, 248, 255));

        JLabel lblBuscar = new JLabel("Buscar por Nombre:");
        lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblBuscar.setForeground(new Color(70, 130, 180));

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtBuscar.setBorder(new CompoundBorder(
                new LineBorder(new Color(135, 206, 235), 1),
                new EmptyBorder(5, 8, 5, 8)
        ));

        // Evento para filtrar automáticamente al escribir
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });

        panelFiltro.add(lblBuscar, BorderLayout.WEST);
        panelFiltro.add(txtBuscar, BorderLayout.CENTER);
        contentPanel.add(panelFiltro, BorderLayout.NORTH);

        // Contenedor de Tarjetas
        panelContenedorCards.setLayout(new GridLayout(0, 2, 12, 12));
        panelContenedorCards.setBackground(new Color(240, 248, 255));

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(240, 248, 255));
        panelNorte.add(panelContenedorCards, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(panelNorte);
        scrollPane.setBorder(new TitledBorder(
                new LineBorder(new Color(135, 206, 235), 2),
                "Análisis Registrados",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Cargar análisis (inicialmente sin filtro)
        cargarAnalisis("");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(new Color(240, 248, 255));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        btnCerrar.setBackground(new Color(176, 224, 230));
        btnCerrar.setForeground(new Color(70, 130, 180));
        btnCerrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setPreferredSize(new Dimension(100, 30));
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void filtrar() {
        cargarAnalisis(txtBuscar.getText().trim());
    }

    private void cargarAnalisis(String filtro) {
        panelContenedorCards.removeAll();
        List<Analisis> listaAnalisis = Clinica.getInstancia().getAnalisis();

        boolean encontrado = false;

        if (listaAnalisis != null && !listaAnalisis.isEmpty()) {
            for (Analisis a : listaAnalisis) {
                if (filtro.isEmpty() || (a.getNombre() != null && a.getNombre().toLowerCase().contains(filtro.toLowerCase()))) {
                    panelContenedorCards.add(crearTarjetaAnalisis(a));
                    encontrado = true;
                }
            }
        }

        if (!encontrado) {
            JLabel lblVacio = new JLabel(
                    filtro.isEmpty() ? "No hay análisis registrados en el sistema." : "No se encontraron coincidencia(s).",
                    SwingConstants.CENTER
            );
            lblVacio.setFont(new Font("Bahnschrift", Font.ITALIC, 13));
            lblVacio.setForeground(Color.GRAY);
            panelContenedorCards.add(lblVacio);
        }

        panelContenedorCards.revalidate();
        panelContenedorCards.repaint();
    }

    private JPanel crearTarjetaAnalisis(Analisis analisis) {
        JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(new CompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JPanel panelTextos = new JPanel();
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        panelTextos.setBackground(Color.WHITE);

        JLabel lblNombre = new JLabel(analisis.getNombre());
        lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblNombre.setForeground(new Color(70, 130, 180));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTipo = new JLabel("Tipo: " + (analisis.getTipo() != null ? analisis.getTipo() : "N/A"));
        lblTipo.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        lblTipo.setForeground(new Color(100, 100, 100));
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelTextos.add(lblNombre);
        panelTextos.add(Box.createVerticalStrut(4));
        panelTextos.add(lblTipo);

        JButton btnVerDetalle = new JButton("Ver Info");
        btnVerDetalle.setFont(new Font("Bahnschrift", Font.BOLD, 11));
        btnVerDetalle.setBackground(new Color(176, 224, 230));
        btnVerDetalle.setForeground(new Color(70, 130, 180));
        btnVerDetalle.setBorder(new LineBorder(new Color(135, 206, 235), 1));
        btnVerDetalle.setFocusPainted(false);
        btnVerDetalle.setPreferredSize(new Dimension(80, 25));
        btnVerDetalle.addActionListener(e -> {
            MostrarAnalisis mostrarAnalisis = new MostrarAnalisis(this, analisis);
            mostrarAnalisis.setVisible(true);
        });

        tarjeta.add(panelTextos, BorderLayout.CENTER);
        tarjeta.add(btnVerDetalle, BorderLayout.EAST);

        return tarjeta;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ListarAnalisis dialog = new ListarAnalisis(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        });
    }
}