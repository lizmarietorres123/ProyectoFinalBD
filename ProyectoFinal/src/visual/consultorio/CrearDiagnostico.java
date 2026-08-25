package visual.consultorio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import bd.catalogo.DiagnosticoDAO;
// IMPORTANTE: Si tienes tu DAO de Enfermedad, descomenta la siguiente línea:
// import bd.catalogo.EnfermedadDAO;
import logico.catalogo.Enfermedad;
import logico.catalogo.Sintoma;
import logico.Clinica;
import logico.consultorio.Diagnostico;
import logico.consultorio.Paciente;
import logico.consultorio.Tratamiento;

public class CrearDiagnostico extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JComboBox<String> cbxEnfermedad;
    private JComboBox<String> cbxTipo;
    private JComboBox<String> cbxEstado;
    private JComboBox<String> cbxSintoma;
    private JComboBox<String> cbxIntensidad;
    private JButton btnAgregarSintoma;
    private JTextArea txtSintomas;

    private JTextArea txtTratamientos;
    private JTextArea txtObservacion;

    private JButton btnAgregarTratamiento;
    private JButton btnVerTratamientos;

    private Map<Sintoma, String> sintomasSeleccionados = new HashMap<>();
    private ArrayList<Tratamiento> tratamientosActuales = new ArrayList<>();
    private Diagnostico diagnosticoCreado = null;
    private Diagnostico diagnosticoEdicion = null;
    private Paciente pacienteActual = null;
    private int eliminado = -1;

    private Diagnostico diagnosticoTrabajo;

    // NUEVO: Lista local para no depender ciegamente de la memoria
    private ArrayList<Enfermedad> listaEnfermedadesLocal = new ArrayList<>();

    public CrearDiagnostico(ArrayList<Diagnostico> listaExistentes, Paciente paciente) {
        this.pacienteActual = paciente;
        this.diagnosticoTrabajo = new Diagnostico();
        initUI();
    }

    public CrearDiagnostico(Diagnostico diagnostico, ArrayList<Diagnostico> listaExistentes) {
        this.diagnosticoEdicion = diagnostico;
        this.diagnosticoTrabajo = diagnostico;
        if (diagnostico != null) {
            if (diagnostico.getTratamientos() != null) {
                this.tratamientosActuales = new ArrayList<>(diagnostico.getTratamientos());
            }
            if (diagnostico.getSintomas() != null) {
                this.sintomasSeleccionados = new HashMap<>(diagnostico.getSintomas());
            }
        }
        initUI();
        cargarDatosEdicion();
    }

    private void initUI() {
        setTitle(diagnosticoEdicion == null ? "Agregar Diagnóstico" : "Ver / Editar Diagnóstico");
        setBounds(100, 100, 640, 560);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setBorder(new TitledBorder(
                new LineBorder(new Color(135, 206, 235), 2),
                "Información del Diagnóstico",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        panelDatos.setBounds(15, 15, 594, 445);
        contentPanel.add(panelDatos);
        panelDatos.setLayout(null);

        JLabel lblEnfermedad = new JLabel("Enfermedad:");
        lblEnfermedad.setForeground(new Color(70, 130, 180));
        lblEnfermedad.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEnfermedad.setBounds(15, 25, 90, 20);
        panelDatos.add(lblEnfermedad);

        cbxEnfermedad = new JComboBox<>();
        cbxEnfermedad.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxEnfermedad.setBackground(new Color(224, 247, 250));
        cbxEnfermedad.setBounds(95, 25, 170, 22);
        cbxEnfermedad.addActionListener(e -> cargarSintomasPorEnfermedad());
        panelDatos.add(cbxEnfermedad);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setForeground(new Color(70, 130, 180));
        lblTipo.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblTipo.setBounds(275, 25, 35, 20);
        panelDatos.add(lblTipo);

        cbxTipo = new JComboBox<>();
        cbxTipo.setModel(new DefaultComboBoxModel<>(new String[] {"<<Seleccione>>", "presuntivo", "confirmado"}));
        cbxTipo.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxTipo.setBackground(new Color(224, 247, 250));
        cbxTipo.setBounds(315, 25, 105, 22);
        panelDatos.add(cbxTipo);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(new Color(70, 130, 180));
        lblEstado.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEstado.setBounds(430, 25, 45, 20);
        panelDatos.add(lblEstado);

        cbxEstado = new JComboBox<>();
        cbxEstado.setModel(new DefaultComboBoxModel<>(new String[] {"<<Seleccione>>", "activo", "en tratamiento", "resuelto", "cronico"}));
        cbxEstado.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxEstado.setBackground(new Color(224, 247, 250));
        cbxEstado.setBounds(480, 25, 100, 22);
        panelDatos.add(cbxEstado);

        JLabel lblSintoma = new JLabel("Síntoma:");
        lblSintoma.setForeground(new Color(70, 130, 180));
        lblSintoma.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblSintoma.setBounds(15, 60, 90, 20);
        panelDatos.add(lblSintoma);

        cbxSintoma = new JComboBox<>();
        cbxSintoma.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxSintoma.setBackground(new Color(224, 247, 250));
        cbxSintoma.setBounds(110, 60, 190, 22);
        panelDatos.add(cbxSintoma);

        JLabel lblIntensidad = new JLabel("Intensidad:");
        lblIntensidad.setForeground(new Color(70, 130, 180));
        lblIntensidad.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblIntensidad.setBounds(310, 60, 75, 20);
        panelDatos.add(lblIntensidad);

        cbxIntensidad = new JComboBox<>();
        cbxIntensidad.setModel(new DefaultComboBoxModel<>(new String[] {"Leve", "Moderado", "Grave", "Critico"}));
        cbxIntensidad.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxIntensidad.setBackground(new Color(224, 247, 250));
        cbxIntensidad.setBounds(385, 60, 105, 22);
        panelDatos.add(cbxIntensidad);

        btnAgregarSintoma = new JButton("Agregar");
        btnAgregarSintoma.setFont(new Font("Bahnschrift", Font.BOLD, 11));
        btnAgregarSintoma.setBackground(new Color(176, 224, 230));
        btnAgregarSintoma.setForeground(new Color(70, 130, 180));
        btnAgregarSintoma.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnAgregarSintoma.setFocusPainted(false);
        btnAgregarSintoma.setBounds(500, 60, 75, 22);
        btnAgregarSintoma.addActionListener(e -> agregarSintoma());
        panelDatos.add(btnAgregarSintoma);

        JLabel lblListaSintomas = new JLabel("Síntomas:");
        lblListaSintomas.setForeground(new Color(70, 130, 180));
        lblListaSintomas.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblListaSintomas.setBounds(15, 95, 90, 20);
        panelDatos.add(lblListaSintomas);

        JScrollPane scrollSintomas = new JScrollPane();
        scrollSintomas.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        scrollSintomas.setBounds(110, 95, 465, 65);
        panelDatos.add(scrollSintomas);

        txtSintomas = new JTextArea();
        txtSintomas.setEditable(false);
        txtSintomas.setLineWrap(true);
        txtSintomas.setWrapStyleWord(true);
        txtSintomas.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
        txtSintomas.setBackground(new Color(224, 247, 250));
        scrollSintomas.setViewportView(txtSintomas);

        JLabel lblTratamientos = new JLabel("Tratamientos:");
        lblTratamientos.setForeground(new Color(70, 130, 180));
        lblTratamientos.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblTratamientos.setBounds(15, 175, 90, 20);
        panelDatos.add(lblTratamientos);

        JScrollPane scrollTratamientos = new JScrollPane();
        scrollTratamientos.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        scrollTratamientos.setBounds(110, 175, 215, 55);
        panelDatos.add(scrollTratamientos);

        txtTratamientos = new JTextArea();
        txtTratamientos.setEditable(false);
        txtTratamientos.setLineWrap(true);
        txtTratamientos.setWrapStyleWord(true);
        txtTratamientos.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
        txtTratamientos.setBackground(new Color(224, 247, 250));
        scrollTratamientos.setViewportView(txtTratamientos);

        btnAgregarTratamiento = new JButton("Agregar tratamiento");
        btnAgregarTratamiento.setFont(new Font("Bahnschrift", Font.BOLD, 11));
        btnAgregarTratamiento.setBackground(new Color(176, 224, 230));
        btnAgregarTratamiento.setForeground(new Color(70, 130, 180));
        btnAgregarTratamiento.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnAgregarTratamiento.setFocusPainted(false);
        btnAgregarTratamiento.setBounds(335, 175, 115, 25);
        btnAgregarTratamiento.addActionListener(e -> abrirAgregarTratamiento());
        panelDatos.add(btnAgregarTratamiento);

        btnVerTratamientos = new JButton("Ver Tratamientos");
        btnVerTratamientos.setFont(new Font("Bahnschrift", Font.BOLD, 11));
        btnVerTratamientos.setBackground(new Color(176, 224, 230));
        btnVerTratamientos.setForeground(new Color(70, 130, 180));
        btnVerTratamientos.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnVerTratamientos.setFocusPainted(false);
        btnVerTratamientos.setBounds(460, 175, 115, 25);
        btnVerTratamientos.addActionListener(e -> mostrarTratamientos());
        panelDatos.add(btnVerTratamientos);

        JLabel lblObservacion = new JLabel("Observación:");
        lblObservacion.setForeground(new Color(70, 130, 180));
        lblObservacion.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblObservacion.setBounds(15, 245, 90, 20);
        panelDatos.add(lblObservacion);

        JScrollPane scrollObservacion = new JScrollPane();
        scrollObservacion.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        scrollObservacion.setBounds(110, 245, 465, 180);
        panelDatos.add(scrollObservacion);

        txtObservacion = new JTextArea();
        txtObservacion.setLineWrap(true);
        txtObservacion.setWrapStyleWord(true);
        txtObservacion.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtObservacion.setBackground(new Color(224, 247, 250));
        scrollObservacion.setViewportView(txtObservacion);

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(240, 248, 255));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        if (diagnosticoEdicion != null) {
            JButton btnEliminar = new JButton("Eliminar");
            btnEliminar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
            btnEliminar.setBackground(new Color(255, 182, 193));
            btnEliminar.setForeground(new Color(178, 34, 34));
            btnEliminar.setBorder(new LineBorder(new Color(240, 128, 128), 2));
            btnEliminar.setFocusPainted(false);
            btnEliminar.addActionListener(e -> eliminarDiagnostico());
            buttonPane.add(btnEliminar);
        }

        JButton btnGuardar = new JButton(diagnosticoEdicion == null ? "Agregar" : "Guardar Cambios");
        btnGuardar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        btnGuardar.setBackground(new Color(176, 224, 230));
        btnGuardar.setForeground(new Color(70, 130, 180));
        btnGuardar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardarDiagnostico());
        buttonPane.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        btnCancelar.setBackground(new Color(176, 224, 230));
        btnCancelar.setForeground(new Color(70, 130, 180));
        btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        buttonPane.add(btnCancelar);

        cargarEnfermedades();
    }

    private void cargarEnfermedades() {
        cbxEnfermedad.removeAllItems();
        cbxEnfermedad.addItem("<<Seleccione>>");

        // OPCIÓN A: Si tienes tu EnfermedadDAO creado, quítale las barras de comentarios a la línea de abajo
        // para que la ventana vaya directo a la base de datos a buscar las enfermedades:
        // listaEnfermedadesLocal = EnfermedadDAO.getInstance().obtenerEnfermedades();

        // OPCIÓN B: (Si dejas la de arriba comentada) Usamos la memoria del Singleton.
        // Solo asegúrate de que cuando inicies sesión en el sistema, llenes esta lista.
        if (listaEnfermedadesLocal.isEmpty() && Clinica.getInstancia().getEnfermedades() != null) {
            listaEnfermedadesLocal = new ArrayList<>(Clinica.getInstancia().getEnfermedades());
        }

        if (listaEnfermedadesLocal != null && !listaEnfermedadesLocal.isEmpty()) {
            for (Enfermedad e : listaEnfermedadesLocal) {
                cbxEnfermedad.addItem(e.getNombre());
            }
        } else {
            System.err.println("Atención: No se cargaron las enfermedades (ni de la BD ni de la memoria).");
        }
    }

    private void cargarSintomasPorEnfermedad() {
        cbxSintoma.removeAllItems();
        cbxSintoma.addItem("<<Seleccione>>");

        if (cbxEnfermedad.getSelectedIndex() > 0) {
            // CORRECCIÓN: Leemos directo de la lista local
            int indexSeleccionado = cbxEnfermedad.getSelectedIndex() - 1;
            Enfermedad enfermedad = listaEnfermedadesLocal.get(indexSeleccionado);

            if (enfermedad != null && enfermedad.getSintomas() != null) {
                for (Sintoma s : enfermedad.getSintomas()) {
                    cbxSintoma.addItem(s.getNombre());
                }
            }
        }
    }

    private void agregarSintoma() {
        if (cbxEnfermedad.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una enfermedad primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cbxSintoma.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un síntoma.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // CORRECCIÓN: Leemos directo de la lista local
        int indexEnfermedad = cbxEnfermedad.getSelectedIndex() - 1;
        Enfermedad enfermedad = listaEnfermedadesLocal.get(indexEnfermedad);

        int indexSintoma = cbxSintoma.getSelectedIndex() - 1;
        Sintoma sintomaSeleccionado = enfermedad.getSintomas().get(indexSintoma);
        String intensidad = (String) cbxIntensidad.getSelectedItem();

        sintomasSeleccionados.put(sintomaSeleccionado, intensidad);
        actualizarTextoSintomas();
    }

    private void actualizarTextoSintomas() {
        if (sintomasSeleccionados.isEmpty()) {
            txtSintomas.setText("");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Sintoma, String> entry : sintomasSeleccionados.entrySet()) {
                sb.append("• ").append(entry.getKey().getNombre())
                        .append(" - Intensidad: ").append(entry.getValue()).append("\n");
            }
            txtSintomas.setText(sb.toString().trim());
            txtSintomas.setCaretPosition(0);
        }
    }

    private void cargarDatosEdicion() {
        if (diagnosticoEdicion != null) {
            if (diagnosticoEdicion.getEnfermedad() != null) {
                cbxEnfermedad.setSelectedItem(diagnosticoEdicion.getEnfermedad().getNombre());
            }
            if (diagnosticoEdicion.getTipo() != null) {
                cbxTipo.setSelectedItem(diagnosticoEdicion.getTipo());
            }
            if (diagnosticoEdicion.getEstado() != null) {
                cbxEstado.setSelectedItem(diagnosticoEdicion.getEstado());
            }

            txtObservacion.setText(diagnosticoEdicion.getObservacion());
            actualizarTextoSintomas();
            actualizarTextoTratamientos();
        }
    }

    private void abrirAgregarTratamiento() {
        CrearTratamiento dialog = new CrearTratamiento(diagnosticoTrabajo);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        Tratamiento nuevoTratamiento = dialog.getTratamientoCreado();
        if (nuevoTratamiento != null) {
            tratamientosActuales.add(nuevoTratamiento);
            actualizarTextoTratamientos();
        }
    }

    private void mostrarTratamientos() {
        if (tratamientosActuales.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay tratamientos asignados a este diagnóstico.",
                    "Tratamientos",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        if (tratamientosActuales.size() == 1) {
            CrearTratamiento dialog = new CrearTratamiento(diagnosticoTrabajo, tratamientosActuales.get(0));
            dialog.setModal(true);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

            if (dialog.isEliminado()) {
                tratamientosActuales.remove(0);
            }
        } else {
            String[] opciones = new String[tratamientosActuales.size()];
            for (int i = 0; i < tratamientosActuales.size(); i++) {
                Tratamiento t = tratamientosActuales.get(i);
                String nombreMedicamento = (t.getMedicamento() != null) ? t.getMedicamento().getNombre() : "Sin medicamento";
                opciones[i] = (i + 1) + ". " + nombreMedicamento;
            }

            String seleccion = (String) JOptionPane.showInputDialog(
                    this,
                    "Seleccione el tratamiento que desea ver o modificar:",
                    "Ver Tratamientos",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (seleccion != null) {
                for (int i = 0; i < tratamientosActuales.size(); i++) {
                    if (seleccion.startsWith((i + 1) + ".")) {
                        CrearTratamiento dialog = new CrearTratamiento(diagnosticoTrabajo, tratamientosActuales.get(i));
                        dialog.setModal(true);
                        dialog.setLocationRelativeTo(this);
                        dialog.setVisible(true);

                        if (dialog.isEliminado()) {
                            tratamientosActuales.remove(i);
                        }
                        break;
                    }
                }
            }
        }

        actualizarTextoTratamientos();
    }

    private void actualizarTextoTratamientos() {
        if (tratamientosActuales.isEmpty()) {
            txtTratamientos.setText("");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Tratamiento t : tratamientosActuales) {
                String nombreMedicamento = (t.getMedicamento() != null) ? t.getMedicamento().getNombre() : "Sin medicamento";
                sb.append("• ").append(nombreMedicamento).append("\n");
            }
            txtTratamientos.setText(sb.toString().trim());
            txtTratamientos.setCaretPosition(0);
        }
    }

    private void guardarDiagnostico() {
        if (cbxEnfermedad.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una enfermedad.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cbxTipo.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar el tipo de diagnóstico (presuntivo o confirmado).", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cbxEstado.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar el estado del diagnóstico.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }


        int indexSeleccionado = cbxEnfermedad.getSelectedIndex() - 1;
        Enfermedad enfermedadElegida = listaEnfermedadesLocal.get(indexSeleccionado);

        String tipoSeleccionado = cbxTipo.getSelectedItem().toString();
        String estadoSeleccionado = cbxEstado.getSelectedItem().toString();

        if (diagnosticoEdicion == null) {
            diagnosticoTrabajo.setEnfermedad(enfermedadElegida);
            diagnosticoTrabajo.setTipo(tipoSeleccionado);
            diagnosticoTrabajo.setEstado(estadoSeleccionado);
            diagnosticoTrabajo.setObservacion(txtObservacion.getText().trim());
            diagnosticoTrabajo.setTratamientos(new ArrayList<>(tratamientosActuales));
            diagnosticoTrabajo.setSintomas(sintomasSeleccionados);
            diagnosticoCreado = diagnosticoTrabajo;
        } else {
            diagnosticoEdicion.setEnfermedad(enfermedadElegida);
            diagnosticoEdicion.setTipo(tipoSeleccionado);
            diagnosticoEdicion.setEstado(estadoSeleccionado);
            diagnosticoEdicion.setObservacion(txtObservacion.getText().trim());
            diagnosticoEdicion.setTratamientos(new ArrayList<>(tratamientosActuales));
            diagnosticoEdicion.setSintomas(sintomasSeleccionados);
            diagnosticoCreado = diagnosticoEdicion;

            DiagnosticoDAO.getInstance().actualizarDiagnostico(diagnosticoEdicion);
        }

        dispose();
    }

    private void eliminarDiagnostico() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar este diagnóstico?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            eliminado = diagnosticoEdicion.getIdNumber();
            DiagnosticoDAO.getInstance().eliminarDiagnostico(diagnosticoEdicion.getIdNumber());
            dispose();
        }
    }

    public int eliminadoIndex() {
        return eliminado;
    }

    public Diagnostico getDiagnosticoCreado() {
        return diagnosticoCreado;
    }
}