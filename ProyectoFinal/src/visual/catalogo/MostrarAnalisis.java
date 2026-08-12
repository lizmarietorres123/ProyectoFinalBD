package visual.catalogo;

import logico.catalogo.Analisis;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MostrarAnalisis extends JDialog {

    private static final long serialVersionUID = 1L;

    public MostrarAnalisis(Window parent, Analisis analisis) {
        super(parent, "Información Detallada del Análisis", ModalityType.APPLICATION_MODAL);
        setBounds(100, 100, 440, 410);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout());

        // Se amplía a 7 filas para acomodar todos los atributos del modelo Analisis
        JPanel panelDatos = new JPanel(new GridLayout(7, 2, 8, 8));
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setBorder(new CompoundBorder(
                new TitledBorder(
                        new LineBorder(new Color(135, 206, 235), 2),
                        "Detalles del Análisis",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        new Font("Bahnschrift", Font.BOLD, 14),
                        new Color(70, 130, 180)
                ),
                new EmptyBorder(12, 15, 12, 15)
        ));

        String unidad = (analisis != null && analisis.getUnidadMedida() != null) ? " " + analisis.getUnidadMedida() : "";

        // Campos sincronizados con logico.catalogo.Analisis
        agregarFila(panelDatos, "Código (ID):", analisis != null ? analisis.getId() : "N/A");
        agregarFila(panelDatos, "Nombre:", analisis != null ? analisis.getNombre() : "N/A");
        agregarFila(panelDatos, "Tipo:", analisis != null ? analisis.getTipo() : "N/A");
        agregarFila(panelDatos, "Unidad de Medida:", analisis != null && analisis.getUnidadMedida() != null ? analisis.getUnidadMedida() : "N/A");
        agregarFila(panelDatos, "Valor Promedio:", (analisis != null && analisis.getValorProm() != null) ? analisis.getValorProm() + unidad : "N/A");
        agregarFila(panelDatos, "Valor Mínimo:", (analisis != null && analisis.getValorMin() != null) ? analisis.getValorMin() + unidad : "N/A");
        agregarFila(panelDatos, "Valor Máximo:", (analisis != null && analisis.getValorMax() != null) ? analisis.getValorMax() + unidad : "N/A");

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