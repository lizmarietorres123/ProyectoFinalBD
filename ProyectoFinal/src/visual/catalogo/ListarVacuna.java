package visual.catalogo;

import logico.catalogo.Vacuna;
import logico.Clinica;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class ListarVacuna extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel panelContenedorCards = new JPanel();

    public ListarVacuna(Window parent) {
        super(parent, "Listado de Vacunas", ModalityType.APPLICATION_MODAL);
        setBounds(100, 100, 650, 480);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(contentPanel, BorderLayout.CENTER);

        panelContenedorCards.setLayout(new GridLayout(0, 2, 12, 12));
        panelContenedorCards.setBackground(new Color(240, 248, 255));

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(240, 248, 255));
        panelNorte.add(panelContenedorCards, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(panelNorte);
        scrollPane.setBorder(new TitledBorder(
                new LineBorder(new Color(135, 206, 235), 2),
                "Vacunas Registradas",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        cargarVacunas();

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

    private void cargarVacunas() {
        panelContenedorCards.removeAll();
        List<Vacuna> listaVacunas = Clinica.getInstancia().getVacunas();

        if (listaVacunas != null && !listaVacunas.isEmpty()) {
            for (Vacuna v : listaVacunas) {
                panelContenedorCards.add(crearTarjetaVacuna(v));
            }
        } else {
            JLabel lblVacio = new JLabel("No hay vacunas registradas en el sistema.", SwingConstants.CENTER);
            lblVacio.setFont(new Font("Bahnschrift", Font.ITALIC, 13));
            lblVacio.setForeground(Color.GRAY);
            panelContenedorCards.add(lblVacio);
        }

        panelContenedorCards.revalidate();
        panelContenedorCards.repaint();
    }

    private JPanel crearTarjetaVacuna(Vacuna vacuna) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);

        tarjeta.setBorder(new CompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel lblNombre = new JLabel(vacuna.getNombre());
        lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblNombre.setForeground(new Color(70, 130, 180));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblFabricante = new JLabel("Fabricante: " + (vacuna.getFabricante() != null ? vacuna.getFabricante() : "N/A"));
        lblFabricante.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        lblFabricante.setForeground(new Color(100, 100, 100));
        lblFabricante.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDosis = new JLabel("Cantidad de Dosis: " + vacuna.getCantDosis());
        lblDosis.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        lblDosis.setForeground(new Color(100, 100, 100));
        lblDosis.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(lblNombre);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblFabricante);
        tarjeta.add(Box.createVerticalStrut(2));
        tarjeta.add(lblDosis);

        return tarjeta;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ListarVacuna dialog = new ListarVacuna(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        });
    }
}