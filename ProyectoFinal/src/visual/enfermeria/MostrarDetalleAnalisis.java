package visual.enfermeria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import logico.Clinica;
import logico.catalogo.Enfermera;
import logico.enfermeria.DetalleAnalisis;

public class MostrarDetalleAnalisis extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JComboBox<Enfermera> cbxEnfermera;
    private JTextField txtResultado;
    private JComboBox<String> cbxEstadoAnalisis;
    private JTextArea txtObservaciones;

    private DetalleAnalisis detalleSeleccionado = null;

    public MostrarDetalleAnalisis(DetalleAnalisis detalleParaVer) {
        this.detalleSeleccionado = detalleParaVer;
        setTitle("Detalle de Análisis Clínico (Solo Lectura)");

        setBounds(100, 100, 520, 340);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // --- PANEL DE INFORMACIÓN DE ANÁLISIS ---
        JPanel panelResultado = new JPanel();
        panelResultado.setBackground(Color.WHITE);
        panelResultado.setBorder(new TitledBorder(
                new LineBorder(new Color(135, 206, 235), 2),
                "Información de Análisis (Vista)",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        panelResultado.setBounds(15, 10, 475, 235);
        contentPanel.add(panelResultado);
        panelResultado.setLayout(null);

        JLabel lblEnfermera = new JLabel("Enfermera:");
        lblEnfermera.setForeground(new Color(70, 130, 180));
        lblEnfermera.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEnfermera.setBounds(15, 30, 80, 14);
        panelResultado.add(lblEnfermera);

        cbxEnfermera = new JComboBox<>();
        cbxEnfermera.setEnabled(false);
        cbxEnfermera.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxEnfermera.setBackground(new Color(224, 247, 250));
        cbxEnfermera.setBounds(105, 26, 355, 22);
        panelResultado.add(cbxEnfermera);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(new Color(70, 130, 180));
        lblEstado.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEstado.setBounds(15, 68, 60, 14);
        panelResultado.add(lblEstado);

        cbxEstadoAnalisis = new JComboBox<>();
        cbxEstadoAnalisis.setEnabled(false);
        cbxEstadoAnalisis.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxEstadoAnalisis.setBackground(new Color(224, 247, 250));
        cbxEstadoAnalisis.setBounds(105, 64, 150, 22);
        panelResultado.add(cbxEstadoAnalisis);

        JLabel lblResultado = new JLabel("Resultado:");
        lblResultado.setForeground(new Color(70, 130, 180));
        lblResultado.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblResultado.setBounds(275, 68, 70, 14);
        panelResultado.add(lblResultado);

        txtResultado = new JTextField();
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtResultado.setBounds(345, 64, 115, 22);
        panelResultado.add(txtResultado);

        JLabel lblObservaciones = new JLabel("Observaciones / Notas:");
        lblObservaciones.setForeground(new Color(70, 130, 180));
        lblObservaciones.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblObservaciones.setBounds(15, 105, 160, 14);
        panelResultado.add(lblObservaciones);

        JScrollPane scrollObs = new JScrollPane();
        scrollObs.setBounds(15, 125, 445, 90);
        panelResultado.add(scrollObs);

        txtObservaciones = new JTextArea();
        txtObservaciones.setEditable(false);
        txtObservaciones.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        scrollObs.setViewportView(txtObservaciones);

        // --- PANEL DE BOTONES (Solo Cerrar) ---
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(240, 248, 255));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnCerrar.setBackground(new Color(176, 224, 230));
        btnCerrar.setForeground(new Color(70, 130, 180));
        btnCerrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setPreferredSize(new Dimension(100, 30));
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);

        cargarComboEnfermeras();
        if (detalleParaVer != null) {
            seleccionarDetalle(detalleParaVer);
        }
    }

    private void cargarComboEnfermeras() {
        DefaultComboBoxModel<Enfermera> model = new DefaultComboBoxModel<>();
        model.addElement(null);
        List<Enfermera> enfermeras = Clinica.getInstancia().getEnfermeras();
        if (enfermeras != null) {
            for (Enfermera e : enfermeras) {
                model.addElement(e);
            }
        }
        cbxEnfermera.setModel(model);
    }

    private void seleccionarDetalle(DetalleAnalisis detalle) {
        if (detalle == null) return;
        if (detalle.getEnfermera() != null) {
            cbxEnfermera.setSelectedItem(detalle.getEnfermera());
        }
        txtResultado.setText(detalle.getResultado() != null ? detalle.getResultado().toString() : "");
        cbxEstadoAnalisis.addItem(detalle.getEstado() != null ? detalle.getEstado() : "Pendiente");
        cbxEstadoAnalisis.setSelectedItem(detalle.getEstado() != null ? detalle.getEstado() : "Pendiente");
        txtObservaciones.setText(detalle.getObservaciones() != null ? detalle.getObservaciones() : "");
    }
}