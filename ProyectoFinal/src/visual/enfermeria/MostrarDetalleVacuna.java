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
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import logico.Clinica;
import logico.catalogo.Enfermera;
import logico.enfermeria.DetalleVacuna;

public class MostrarDetalleVacuna extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JSpinner spinDosis;
    private JTextField txtLote;
    private JComboBox<Enfermera> cbxEnfermera;
    private JComboBox<String> cbxEstadoVacuna;
    private JTextArea txtObservaciones;

    private DetalleVacuna detalleSeleccionado = null;

    public MostrarDetalleVacuna(DetalleVacuna detalleParaVer) {
        this.detalleSeleccionado = detalleParaVer;
        setTitle("Detalle de Aplicación de Vacuna (Solo Lectura)");

        setBounds(100, 100, 520, 340);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // --- PANEL DE INFORMACIÓN DE APLICACIÓN ---
        JPanel panelEnfermera = new JPanel();
        panelEnfermera.setBackground(Color.WHITE);
        panelEnfermera.setBorder(new TitledBorder(
                new LineBorder(new Color(135, 206, 235), 2),
                "Información de Aplicación (Vista)",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        panelEnfermera.setBounds(15, 10, 475, 235);
        contentPanel.add(panelEnfermera);
        panelEnfermera.setLayout(null);

        JLabel lblDosis = new JLabel("Dosis Aplicada:");
        lblDosis.setForeground(new Color(70, 130, 180));
        lblDosis.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblDosis.setBounds(15, 30, 100, 14);
        panelEnfermera.add(lblDosis);

        spinDosis = new JSpinner();
        spinDosis.setEnabled(false);
        spinDosis.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        spinDosis.setBounds(115, 26, 70, 22);
        panelEnfermera.add(spinDosis);

        JLabel lblLote = new JLabel("Lote:");
        lblLote.setForeground(new Color(70, 130, 180));
        lblLote.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblLote.setBounds(205, 30, 40, 14);
        panelEnfermera.add(lblLote);

        txtLote = new JTextField();
        txtLote.setEditable(false);
        txtLote.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtLote.setBounds(245, 26, 205, 22);
        panelEnfermera.add(txtLote);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(new Color(70, 130, 180));
        lblEstado.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEstado.setBounds(15, 68, 60, 14);
        panelEnfermera.add(lblEstado);

        cbxEstadoVacuna = new JComboBox<>();
        cbxEstadoVacuna.setEnabled(false);
        cbxEstadoVacuna.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxEstadoVacuna.setBackground(new Color(224, 247, 250));
        cbxEstadoVacuna.setBounds(115, 64, 160, 22);
        panelEnfermera.add(cbxEstadoVacuna);

        JLabel lblEnfermeraObj = new JLabel("Enfermera:");
        lblEnfermeraObj.setForeground(new Color(70, 130, 180));
        lblEnfermeraObj.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEnfermeraObj.setBounds(15, 106, 80, 14);
        panelEnfermera.add(lblEnfermeraObj);

        cbxEnfermera = new JComboBox<>();
        cbxEnfermera.setEnabled(false);
        cbxEnfermera.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxEnfermera.setBackground(new Color(224, 247, 250));
        cbxEnfermera.setBounds(115, 102, 335, 22);
        panelEnfermera.add(cbxEnfermera);

        JLabel lblObservaciones = new JLabel("Observaciones:");
        lblObservaciones.setForeground(new Color(70, 130, 180));
        lblObservaciones.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblObservaciones.setBounds(15, 140, 160, 14);
        panelEnfermera.add(lblObservaciones);

        JScrollPane scrollObservaciones = new JScrollPane();
        scrollObservaciones.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        scrollObservaciones.setBounds(15, 160, 435, 60);
        panelEnfermera.add(scrollObservaciones);

        txtObservaciones = new JTextArea();
        txtObservaciones.setEditable(false);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        scrollObservaciones.setViewportView(txtObservaciones);

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

    private void seleccionarDetalle(DetalleVacuna detalle) {
        if (detalle == null) return;
        if (detalle.getEnfermera() != null) {
            cbxEnfermera.setSelectedItem(detalle.getEnfermera());
        }
        spinDosis.setValue(detalle.getDosis() > 0 ? detalle.getDosis() : 1);
        txtLote.setText(detalle.getLote() != null ? detalle.getLote() : "");
        cbxEstadoVacuna.addItem(detalle.getEstado() != null ? detalle.getEstado() : "Pendiente");
        cbxEstadoVacuna.setSelectedItem(detalle.getEstado() != null ? detalle.getEstado() : "Pendiente");
        txtObservaciones.setText(detalle.getObservaciones() != null ? detalle.getObservaciones() : "");
    }
}