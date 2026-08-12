package visual.catalogo;

import logico.catalogo.Medicamento;
import logico.Clinica;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ListarMedicamento extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel panelContenedorCards = new JPanel();
    private JScrollPane scrollPane;
    private JTextField txtBuscar;

    private boolean modoSeleccion = false;
    private Medicamento medicamentoSeleccionado = null;

    public ListarMedicamento(Window parent) {
        this(parent, false);
    }

    public ListarMedicamento(Window parent, boolean modoSeleccion) {
        super(parent, modoSeleccion ? "Seleccionar Medicamento" : "Listado de Medicamentos", ModalityType.APPLICATION_MODAL);
        this.modoSeleccion = modoSeleccion;

        setBounds(100, 100, 680, 520);
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
        lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblBuscar.setForeground(new Color(70, 130, 180));

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
        txtBuscar.setPreferredSize(new Dimension(0, 30));
        txtBuscar.setBorder(new CompoundBorder(
                new LineBorder(new Color(135, 206, 235), 1),
                new EmptyBorder(4, 8, 4, 8)
        ));

        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrar();
            }
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

        scrollPane = new JScrollPane(panelNorte);
        scrollPane.setBorder(new TitledBorder(
                new LineBorder(new Color(135, 206, 235), 2),
                "Medicamentos Registrados",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        cargarMedicamentos("");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(new Color(240, 248, 255));

        JButton btnCerrar = new JButton("Cancelar");
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
        cargarMedicamentos(txtBuscar.getText().trim());
    }

    private void cargarMedicamentos(String filtro) {
        panelContenedorCards.removeAll();
        List<Medicamento> listaMedicamentos = Clinica.getInstancia().getMedicamentos();

        boolean encontrado = false;

        if (listaMedicamentos != null && !listaMedicamentos.isEmpty()) {
            for (Medicamento m : listaMedicamentos) {
                if (filtro.isEmpty() || (m.getNombre() != null && m.getNombre().toLowerCase().contains(filtro.toLowerCase()))) {
                    panelContenedorCards.add(crearTarjetaMedicamento(m));
                    encontrado = true;
                }
            }
        }

        if (!encontrado) {
            JLabel lblVacio = new JLabel(
                    filtro.isEmpty() ? "No hay medicamentos registrados en el sistema." : "No se encontraron coincidencias.",
                    SwingConstants.CENTER
            );
            lblVacio.setFont(new Font("Bahnschrift", Font.ITALIC, 13));
            lblVacio.setForeground(Color.GRAY);
            panelContenedorCards.add(lblVacio);
        }

        panelContenedorCards.revalidate();
        panelContenedorCards.repaint();

        if (scrollPane != null) {
            scrollPane.revalidate();
            scrollPane.repaint();
        }
    }

    private JPanel crearTarjetaMedicamento(Medicamento medicamento) {
        JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(new CompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JPanel panelTextos = new JPanel();
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        panelTextos.setBackground(Color.WHITE);

        JLabel lblNombre = new JLabel(medicamento.getNombre());
        lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblNombre.setForeground(new Color(70, 130, 180));

        JLabel lblFabricante = new JLabel("Fabricante: " + (medicamento.getFabricante() != null ? medicamento.getFabricante() : "N/A"));
        lblFabricante.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        lblFabricante.setForeground(new Color(100, 100, 100));

        panelTextos.add(lblNombre);
        panelTextos.add(Box.createVerticalStrut(4));
        panelTextos.add(lblFabricante);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        panelAcciones.setBackground(Color.WHITE);

        JButton btnVerDetalle = new JButton("Info");
        btnVerDetalle.setFont(new Font("Bahnschrift", Font.BOLD, 11));
        btnVerDetalle.setBackground(new Color(224, 247, 250));
        btnVerDetalle.setForeground(new Color(70, 130, 180));
        btnVerDetalle.setBorder(new LineBorder(new Color(135, 206, 235), 1));
        btnVerDetalle.setFocusPainted(false);
        btnVerDetalle.setPreferredSize(new Dimension(55, 25));
        btnVerDetalle.addActionListener(e -> {
            MostrarMedicamento mostrarMedicamento = new MostrarMedicamento(this, medicamento);
            mostrarMedicamento.setVisible(true);
        });
        panelAcciones.add(btnVerDetalle);

        if (modoSeleccion) {
            JButton btnSeleccionar = new JButton("Elegir");
            btnSeleccionar.setFont(new Font("Bahnschrift", Font.BOLD, 11));
            btnSeleccionar.setBackground(new Color(176, 224, 230));
            btnSeleccionar.setForeground(new Color(70, 130, 180));
            btnSeleccionar.setBorder(new LineBorder(new Color(135, 206, 235), 1));
            btnSeleccionar.setFocusPainted(false);
            btnSeleccionar.setPreferredSize(new Dimension(60, 25));
            btnSeleccionar.addActionListener(e -> {
                this.medicamentoSeleccionado = medicamento;
                dispose();
            });
            panelAcciones.add(btnSeleccionar);
        }

        tarjeta.add(panelTextos, BorderLayout.CENTER);
        tarjeta.add(panelAcciones, BorderLayout.EAST);

        return tarjeta;
    }

    public Medicamento getMedicamentoSeleccionado() {
        return medicamentoSeleccionado;
    }
}