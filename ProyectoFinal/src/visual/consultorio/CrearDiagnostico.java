package visual.consultorio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import logico.consultorio.Clinica;
import logico.consultorio.Consulta;
import logico.consultorio.Diagnostico;
import logico.catalogo.Enfermedad;
import logico.consultorio.Paciente;

public class CrearDiagnostico extends JDialog {
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JLabel lblPrevios;
    private JComboBox<String> cbxDiagnosticosPrevios;
    private JTextField txtCodigoDiagnostico;
    private JTextArea txtDescripcion;
    private JComboBox<String> cbxEnfermedad;
    private JTextArea txtDetallesEnfermedad;
    private JButton okButton;
    private JButton cancelButton;
    private JButton btnEditar;
    private Diagnostico diagnosticoCreado;
    private Diagnostico diagnosticoEdicion;

    private ArrayList<Diagnostico> diagnosticosExistentes;
    private ArrayList<Diagnostico> listaDiagnosticosPrevios = new ArrayList<>();
    private Paciente pacienteActual;
    private boolean enModoEdicion = false;

    public static void main(String[] args) {
        try {
            CrearDiagnostico dialog = new CrearDiagnostico();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CrearDiagnostico() {
        this((ArrayList<Diagnostico>) null, (Paciente) null);
    }

    public CrearDiagnostico(Diagnostico diagnosticoVer) {
        this(diagnosticoVer, null);
    }

    public CrearDiagnostico(Diagnostico diagnosticoVer, ArrayList<Diagnostico> diagnosticosExistentes) {
        this(diagnosticosExistentes, null);
        if (diagnosticoVer != null) {
            this.diagnosticoEdicion = diagnosticoVer;
            configurarModoVisualizacion(diagnosticoVer);
        }
    }

    public CrearDiagnostico(ArrayList<Diagnostico> diagnosticosExistentes) {
        this(diagnosticosExistentes, null);
    }

    public CrearDiagnostico(ArrayList<Diagnostico> diagnosticosExistentes, Paciente pacienteActual) {
        this.diagnosticosExistentes = diagnosticosExistentes;
        this.pacienteActual = pacienteActual;

        setTitle("Crear / Seleccionar Diagnóstico");
        setModal(true);
        setBounds(100, 100, 600, 540);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(176, 224, 230));
        panel.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Informacion del Diagnostico", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(70, 130, 180)));
        panel.setBounds(10, 11, 564, 440);
        contentPanel.add(panel);
        panel.setLayout(null);

        lblPrevios = new JLabel("Diag. Previos Paciente:");
        lblPrevios.setForeground(new Color(70, 130, 180));
        lblPrevios.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblPrevios.setBounds(10, 25, 150, 14);
        panel.add(lblPrevios);

        cbxDiagnosticosPrevios = new JComboBox<>();
        cbxDiagnosticosPrevios.setBackground(Color.WHITE);
        cbxDiagnosticosPrevios.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxDiagnosticosPrevios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alSeleccionarDiagnosticoPrevio();
            }
        });
        cbxDiagnosticosPrevios.setBounds(170, 22, 374, 22);
        panel.add(cbxDiagnosticosPrevios);

        JLabel lblCodigo = new JLabel("Codigo Diagnostico:");
        lblCodigo.setForeground(new Color(70, 130, 180));
        lblCodigo.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblCodigo.setBounds(10, 60, 150, 14);
        panel.add(lblCodigo);

        txtCodigoDiagnostico = new JTextField();
        txtCodigoDiagnostico.setEditable(false);
        txtCodigoDiagnostico.setBackground(Color.WHITE);
        txtCodigoDiagnostico.setBounds(170, 57, 374, 20);
        panel.add(txtCodigoDiagnostico);
        txtCodigoDiagnostico.setColumns(10);
        txtCodigoDiagnostico.setText("DIAG-" + Clinica.genCodigoDiagnosticos);

        JLabel lblEnfermedad = new JLabel("Enfermedad:");
        lblEnfermedad.setForeground(new Color(70, 130, 180));
        lblEnfermedad.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblEnfermedad.setBounds(10, 95, 150, 14);
        panel.add(lblEnfermedad);

        cbxEnfermedad = new JComboBox<>();
        cbxEnfermedad.setBackground(Color.WHITE);
        cbxEnfermedad.setModel(new DefaultComboBoxModel<>(new String[] {"<<Seleccione>>"}));
        cbxEnfermedad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarDetallesEnfermedad();
            }
        });
        cbxEnfermedad.setBounds(170, 92, 374, 20);
        panel.add(cbxEnfermedad);

        JLabel lblDetalles = new JLabel("Detalles Enfermedad:");
        lblDetalles.setForeground(new Color(70, 130, 180));
        lblDetalles.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblDetalles.setBounds(10, 130, 150, 14);
        panel.add(lblDetalles);

        JScrollPane scrollDetalles = new JScrollPane();
        scrollDetalles.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        scrollDetalles.setBounds(170, 130, 374, 65);
        panel.add(scrollDetalles);

        txtDetallesEnfermedad = new JTextArea();
        txtDetallesEnfermedad.setEditable(false);
        txtDetallesEnfermedad.setLineWrap(true);
        txtDetallesEnfermedad.setWrapStyleWord(true);
        txtDetallesEnfermedad.setBackground(new Color(224, 247, 250));
        txtDetallesEnfermedad.setForeground(new Color(70, 130, 180));
        txtDetallesEnfermedad.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        scrollDetalles.setViewportView(txtDetallesEnfermedad);

        JLabel lblDescripcion = new JLabel("Descripcion:");
        lblDescripcion.setForeground(new Color(70, 130, 180));
        lblDescripcion.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblDescripcion.setBounds(10, 210, 150, 14);
        panel.add(lblDescripcion);

        JScrollPane scrollDescripcion = new JScrollPane();
        scrollDescripcion.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        scrollDescripcion.setBounds(170, 210, 374, 215);
        panel.add(scrollDescripcion);

        txtDescripcion = new JTextArea();
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBackground(Color.WHITE);
        txtDescripcion.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        scrollDescripcion.setViewportView(txtDescripcion);

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(240, 248, 255));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(176, 224, 230));
        btnEditar.setForeground(new Color(70, 130, 180));
        btnEditar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnEditar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnEditar.setVisible(false);
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activarModoEdicion();
            }
        });
        buttonPane.add(btnEditar);

        okButton = new JButton("Crear / Asignar");
        okButton.setBackground(new Color(176, 224, 230));
        okButton.setForeground(new Color(70, 130, 180));
        okButton.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        okButton.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (enModoEdicion) {
                    guardarEdicionDiagnostico();
                } else {
                    crearDiagnostico();
                }
            }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        cancelButton = new JButton("Cancelar");
        cancelButton.setBackground(new Color(176, 224, 230));
        cancelButton.setForeground(new Color(70, 130, 180));
        cancelButton.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        cancelButton.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

        cargarEnfermedades();
        cargarDiagnosticosPrevios();
    }

    private void configurarModoVisualizacion(Diagnostico d) {
        setTitle("Visualizar / Editar Diagnóstico - " + d.getCodigoDiagnostico());

        if (lblPrevios != null) lblPrevios.setVisible(false);
        if (cbxDiagnosticosPrevios != null) cbxDiagnosticosPrevios.setVisible(false);

        txtCodigoDiagnostico.setText(d.getCodigoDiagnostico());
        txtCodigoDiagnostico.setEditable(false);

        cargarEnfermedades();
        if (d.getEnfermedadDiagnosticada() != null) {
            String idEnf = d.getEnfermedadDiagnosticada().getId();
            for (int i = 0; i < cbxEnfermedad.getItemCount(); i++) {
                if (cbxEnfermedad.getItemAt(i).startsWith(idEnf + " - ")) {
                    cbxEnfermedad.setSelectedIndex(i);
                    break;
                }
            }
            mostrarDetallesEnfermedad();
        }
        cbxEnfermedad.setEnabled(false);

        txtDescripcion.setText(d.getDescripcion());
        txtDescripcion.setEditable(false);

        btnEditar.setVisible(true);

        okButton.setText("Cerrar");
        for (ActionListener al : okButton.getActionListeners()) {
            okButton.removeActionListener(al);
        }
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        cancelButton.setVisible(false);
    }

    private void activarModoEdicion() {
        enModoEdicion = true;
        setTitle("Editar Diagnóstico - " + diagnosticoEdicion.getCodigoDiagnostico());

        // Recargar las enfermedades filtrando las asignadas a OTROS diagnósticos
        cargarEnfermedades();

        // Reseleccionar la enfermedad actual del diagnóstico
        if (diagnosticoEdicion.getEnfermedadDiagnosticada() != null) {
            String idEnf = diagnosticoEdicion.getEnfermedadDiagnosticada().getId();
            for (int i = 0; i < cbxEnfermedad.getItemCount(); i++) {
                if (cbxEnfermedad.getItemAt(i).startsWith(idEnf + " - ")) {
                    cbxEnfermedad.setSelectedIndex(i);
                    break;
                }
            }
        }

        cbxEnfermedad.setEnabled(true);
        txtDescripcion.setEditable(true);
        btnEditar.setVisible(false);
        cancelButton.setVisible(true);

        okButton.setText("Guardar Cambios");
        for (ActionListener al : okButton.getActionListeners()) {
            okButton.removeActionListener(al);
        }
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarEdicionDiagnostico();
            }
        });
    }

    private void guardarEdicionDiagnostico() {
        if (txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una descripción.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cbxEnfermedad.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una enfermedad.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String seleccion = cbxEnfermedad.getSelectedItem().toString();
        String idEnfermedad = seleccion.split(" - ")[0];
        Enfermedad enfermedadSeleccionada = Clinica.getInstancia().buscarEnfermedadXId(idEnfermedad);

        // Validar que no se seleccione una enfermedad asignada a OTRO diagnóstico en la consulta actual
        if (diagnosticosExistentes != null) {
            for (Diagnostico d : diagnosticosExistentes) {
                if (d != diagnosticoEdicion && d.getEnfermedadDiagnosticada() != null &&
                        d.getEnfermedadDiagnosticada().getId().equalsIgnoreCase(idEnfermedad)) {
                    JOptionPane.showMessageDialog(null,
                            "La enfermedad '" + enfermedadSeleccionada.getNombre() + "' ya fue asignada a otro diagnóstico en esta consulta.",
                            "Diagnóstico Duplicado",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

        diagnosticoEdicion.setDescripcion(txtDescripcion.getText().trim());
        diagnosticoEdicion.setEnfermedadDiagnosticada(enfermedadSeleccionada);

        JOptionPane.showMessageDialog(null, "Diagnóstico actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void cargarEnfermedades() {
        cbxEnfermedad.removeAllItems();
        cbxEnfermedad.addItem("<<Seleccione>>");

        for (Enfermedad enfermedad : Clinica.getInstancia().getEnfermedades()) {
            boolean yaExisteEnConsultaActual = false;

            if (diagnosticosExistentes != null) {
                for (Diagnostico diag : diagnosticosExistentes) {
                    if (diag != diagnosticoEdicion && diag.getEnfermedadDiagnosticada() != null &&
                            diag.getEnfermedadDiagnosticada().getId().equalsIgnoreCase(enfermedad.getId())) {
                        yaExisteEnConsultaActual = true;
                        break;
                    }
                }
            }

            if (!yaExisteEnConsultaActual) {
                cbxEnfermedad.addItem(enfermedad.getId() + " - " + enfermedad.getNombre());
            }
        }
    }

    private void cargarDiagnosticosPrevios() {
        ActionListener[] listeners = cbxDiagnosticosPrevios.getActionListeners();
        for (ActionListener l : listeners) {
            cbxDiagnosticosPrevios.removeActionListener(l);
        }

        cbxDiagnosticosPrevios.removeAllItems();
        cbxDiagnosticosPrevios.addItem("<<Nuevo / Seleccione Histórico>>");
        listaDiagnosticosPrevios.clear();

        if (pacienteActual != null && pacienteActual.getHistorialClinico() != null) {
            for (Consulta c : pacienteActual.getHistorialClinico()) {
                if (c.getDiagnosticos() != null) {
                    for (Diagnostico d : c.getDiagnosticos()) {
                        if (!listaDiagnosticosPrevios.contains(d)) {
                            listaDiagnosticosPrevios.add(d);
                            String enfNombre = (d.getEnfermedadDiagnosticada() != null)
                                    ? d.getEnfermedadDiagnosticada().getNombre()
                                    : "Sin Enfermedad";
                            cbxDiagnosticosPrevios.addItem(d.getCodigoDiagnostico() + " - " + enfNombre);
                        }
                    }
                }
            }
        }

        cbxDiagnosticosPrevios.setEnabled(!listaDiagnosticosPrevios.isEmpty());

        for (ActionListener l : listeners) {
            cbxDiagnosticosPrevios.addActionListener(l);
        }
    }

    private void alSeleccionarDiagnosticoPrevio() {
        int idx = cbxDiagnosticosPrevios.getSelectedIndex();
        if (idx > 0 && idx <= listaDiagnosticosPrevios.size()) {
            Diagnostico diagPrevio = listaDiagnosticosPrevios.get(idx - 1);
            if (diagPrevio != null) {
                txtDescripcion.setText(diagPrevio.getDescripcion());
                if (diagPrevio.getEnfermedadDiagnosticada() != null) {
                    String idEnf = diagPrevio.getEnfermedadDiagnosticada().getId();
                    boolean encontrada = false;
                    for (int i = 0; i < cbxEnfermedad.getItemCount(); i++) {
                        String item = cbxEnfermedad.getItemAt(i);
                        if (item.startsWith(idEnf + " - ")) {
                            cbxEnfermedad.setSelectedIndex(i);
                            encontrada = true;
                            break;
                        }
                    }

                    if (!encontrada) {
                        JOptionPane.showMessageDialog(this,
                                "La enfermedad '" + diagPrevio.getEnfermedadDiagnosticada().getNombre() +
                                        "' ya fue agregada a la consulta actual.",
                                "Diagnóstico Duplicado",
                                JOptionPane.WARNING_MESSAGE);
                        cbxDiagnosticosPrevios.setSelectedIndex(0);
                        txtDescripcion.setText("");
                        cbxEnfermedad.setSelectedIndex(0);
                    }
                }
            }
        }
    }

    private void mostrarDetallesEnfermedad() {
        if (cbxEnfermedad.getSelectedIndex() >= 0) {
            String seleccion = cbxEnfermedad.getSelectedItem().toString();
            if (seleccion.contains(" - ")) {
                String idEnfermedad = seleccion.split(" - ")[0];
                Enfermedad enfermedad = Clinica.getInstancia().buscarEnfermedadXId(idEnfermedad);

                if (enfermedad != null) {
                    StringBuilder detalles = new StringBuilder();
                    detalles.append("Nombre: ").append(enfermedad.getNombre()).append("\n");
                    detalles.append("En Vigilancia: ").append(enfermedad.isVigilancia() ? "Sí" : "No").append("\n");
                    detalles.append("Síntomas: ").append(enfermedad.getSintomas()).append("\n");

                    txtDetallesEnfermedad.setText(detalles.toString());
                    return;
                }
            }
        }
        txtDetallesEnfermedad.setText("");
    }

    private void crearDiagnostico() {
        if (txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una descripción.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cbxEnfermedad.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una enfermedad.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String seleccion = cbxEnfermedad.getSelectedItem().toString();
        String idEnfermedad = seleccion.split(" - ")[0];
        Enfermedad enfermedadSeleccionada = Clinica.getInstancia().buscarEnfermedadXId(idEnfermedad);

        if (diagnosticosExistentes != null) {
            for (Diagnostico d : diagnosticosExistentes) {
                if (d.getEnfermedadDiagnosticada() != null &&
                        d.getEnfermedadDiagnosticada().getId().equalsIgnoreCase(idEnfermedad)) {
                    JOptionPane.showMessageDialog(null,
                            "La enfermedad '" + enfermedadSeleccionada.getNombre() + "' ya fue agregada a esta consulta.",
                            "Diagnóstico Duplicado",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

        String id = "DIAG-" + Clinica.genCodigoDiagnosticos;

        diagnosticoCreado = new Diagnostico(id, txtDescripcion.getText().trim(), new Date());
        diagnosticoCreado.setCodigoDiagnostico(txtCodigoDiagnostico.getText().trim());

        if (enfermedadSeleccionada != null) {
            diagnosticoCreado.setEnfermedadDiagnosticada(enfermedadSeleccionada);
            Clinica.getInstancia().reportarCasoEnfermedad(idEnfermedad);
        }

        Clinica.genCodigoDiagnosticos++;

        JOptionPane.showMessageDialog(null,
                "Diagnóstico registrado exitosamente.\nEnfermedad: " + (enfermedadSeleccionada != null ? enfermedadSeleccionada.getNombre() : ""),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    public Diagnostico getDiagnosticoCreado() {
        return diagnosticoCreado;
    }
}