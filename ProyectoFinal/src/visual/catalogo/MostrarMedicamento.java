package visual.catalogo;

import logico.catalogo.Medicamento;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MostrarMedicamento extends JDialog {

    private static final long serialVersionUID = 1L;

    public MostrarMedicamento(Window parent, Medicamento medicamento) {
        super(parent, "Información Detallada del Medicamento", ModalityType.APPLICATION_MODAL);
        setBounds(100, 100, 440, 380);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout());

        JPanel panelDatos = new JPanel(new GridLayout(5, 2, 8, 8));
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setBorder(new CompoundBorder(
                new TitledBorder(
                        new LineBorder(new Color(135, 206, 235), 2),
                        "Detalles del Medicamento",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        new Font("Bahnschrift", Font.BOLD, 14),
                        new Color(70, 130, 180)
                ),
                new EmptyBorder(12, 15, 12, 15)
        ));

        agregarFila(panelDatos, "Código (ID):", medicamento != null && medicamento.getId() != null ? medicamento.getId() : "N/A");
        agregarFila(panelDatos, "Nombre:", medicamento != null && medicamento.getNombre() != null ? medicamento.getNombre() : "N/A");
        agregarFila(panelDatos, "Fabricante:", medicamento != null && medicamento.getFabricante() != null ? medicamento.getFabricante() : "N/A");
        agregarFila(panelDatos, "Presentación:", medicamento != null && medicamento.getPresentacion() != null ? medicamento.getPresentacion() : "N/A");

        contentPanel.add(panelDatos, BorderLayout.CENTER);

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

    private void agregarFila(JPanel panel, String titulo, String valor) {
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblTitulo.setForeground(new Color(70, 130, 180));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        lblValor.setForeground(Color.DARK_GRAY);

        panel.add(lblTitulo);
        panel.add(lblValor);
    }
}