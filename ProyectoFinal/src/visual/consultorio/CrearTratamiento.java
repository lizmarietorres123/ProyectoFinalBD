package visual.consultorio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import bd.catalogo.TratamientoDAO;
import logico.catalogo.Medicamento;
import logico.Clinica;
import logico.consultorio.Diagnostico;
import logico.consultorio.Tratamiento;

public class CrearTratamiento extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JComboBox<String> cbxMedicamento;
    private JLabel lblDosis;
    private JSpinner spnDosis;
    private JTextField txtFrecuencia;
    private JSpinner spnFechaInicio;
    private JSpinner spnFechaFin;
    private JComboBox<String> cbxEstado;
    private JTextArea txtDescripcion;

    private Tratamiento tratamientoCreado = null;
    private Tratamiento tratamientoEdicion = null;
    private Diagnostico diagnosticoActual;
    private boolean eliminado = false; // Indicador para saber si el tratamiento fue eliminado

    public CrearTratamiento(Diagnostico diagnostico) {
        this.diagnosticoActual = diagnostico;
        initUI();
    }

    public CrearTratamiento(Diagnostico diagnostico, Tratamiento tratamiento) {
        this.diagnosticoActual = diagnostico;
        this.tratamientoEdicion = tratamiento;
        initUI();
        cargarDatosEdicion();
    }

    private void initUI() {
        setTitle(tratamientoEdicion == null ? "Agregar Tratamiento Médico" : "Ver / Editar Tratamiento Médico");
        setBounds(100, 100, 560, 460);
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
                "Datos del Tratamiento",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        panelDatos.setBounds(15, 15, 514, 350);
        contentPanel.add(panelDatos);
        panelDatos.setLayout(null);

        JLabel lblMedicamento = new JLabel("Medicamento:");
        lblMedicamento.setForeground(new Color(70, 130, 180));
        lblMedicamento.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblMedicamento.setBounds(15, 30, 100, 14);
        panelDatos.add(lblMedicamento);

        cbxMedicamento = new JComboBox<>();
        cbxMedicamento.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxMedicamento.setBackground(new Color(224, 247, 250));
        cbxMedicamento.setBounds(120, 25, 375, 22);
        cbxMedicamento.addActionListener(e -> actualizarEstadoDosis());
        panelDatos.add(cbxMedicamento);

        lblDosis = new JLabel("Dosis (unid/mg):");
        lblDosis.setForeground(new Color(70, 130, 180));
        lblDosis.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblDosis.setBounds(15, 65, 100, 14);
        panelDatos.add(lblDosis);

        spnDosis = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
        spnDosis.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        spnDosis.setBounds(120, 60, 120, 22);
        panelDatos.add(spnDosis);

        JLabel lblFrecuencia = new JLabel("Frecuencia:");
        lblFrecuencia.setForeground(new Color(70, 130, 180));
        lblFrecuencia.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblFrecuencia.setBounds(255, 65, 80, 14);
        panelDatos.add(lblFrecuencia);

        txtFrecuencia = new JTextField();
        txtFrecuencia.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtFrecuencia.setBackground(new Color(224, 247, 250));
        txtFrecuencia.setToolTipText("Ejemplo: Cada 8 horas, Una vez al día");
        txtFrecuencia.setBounds(335, 60, 160, 22);
        panelDatos.add(txtFrecuencia);

        JLabel lblFechaInicio = new JLabel("Fecha Inicio:");
        lblFechaInicio.setForeground(new Color(70, 130, 180));
        lblFechaInicio.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblFechaInicio.setBounds(15, 105, 100, 14);
        panelDatos.add(lblFechaInicio);

        spnFechaInicio = new JSpinner(new SpinnerDateModel());
        spnFechaInicio.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        JSpinner.DateEditor editorInicio = new JSpinner.DateEditor(spnFechaInicio, "dd/MM/yyyy");
        spnFechaInicio.setEditor(editorInicio);
        spnFechaInicio.setBounds(120, 100, 120, 22);
        panelDatos.add(spnFechaInicio);

        JLabel lblFechaFin = new JLabel("Fecha Fin:");
        lblFechaFin.setForeground(new Color(70, 130, 180));
        lblFechaFin.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblFechaFin.setBounds(255, 105, 80, 14);
        panelDatos.add(lblFechaFin);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        spnFechaFin = new JSpinner(new SpinnerDateModel(cal.getTime(), null, null, Calendar.DAY_OF_MONTH));
        spnFechaFin.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        JSpinner.DateEditor editorFin = new JSpinner.DateEditor(spnFechaFin, "dd/MM/yyyy");
        spnFechaFin.setEditor(editorFin);
        spnFechaFin.setBounds(335, 100, 160, 22);
        panelDatos.add(spnFechaFin);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(new Color(70, 130, 180));
        lblEstado.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEstado.setBounds(15, 145, 100, 14);
        panelDatos.add(lblEstado);

        cbxEstado = new JComboBox<>();
        cbxEstado.setModel(new DefaultComboBoxModel<>(new String[] {"Activo", "Completado", "Suspendido"}));
        cbxEstado.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxEstado.setBackground(new Color(224, 247, 250));
        cbxEstado.setBounds(120, 140, 375, 22);
        panelDatos.add(cbxEstado);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setForeground(new Color(70, 130, 180));
        lblDescripcion.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblDescripcion.setBounds(15, 185, 100, 14);
        panelDatos.add(lblDescripcion);

        JScrollPane scrollDescripcion = new JScrollPane();
        scrollDescripcion.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        scrollDescripcion.setBounds(120, 180, 375, 145);
        panelDatos.add(scrollDescripcion);

        txtDescripcion = new JTextArea();
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtDescripcion.setBackground(new Color(224, 247, 250));
        scrollDescripcion.setViewportView(txtDescripcion);

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(240, 248, 255));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        // Botón Eliminar (Solo se muestra si se está editando un tratamiento existente)
        if (tratamientoEdicion != null) {
            JButton btnEliminar = new JButton("Eliminar");
            btnEliminar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
            btnEliminar.setBackground(new Color(255, 204, 204));
            btnEliminar.setForeground(new Color(178, 34, 34));
            btnEliminar.setBorder(new LineBorder(new Color(205, 92, 92), 2));
            btnEliminar.setFocusPainted(false);
            btnEliminar.addActionListener(e -> eliminarTratamiento());
            buttonPane.add(btnEliminar);
        }

        JButton btnGuardar = new JButton(tratamientoEdicion == null ? "Guardar" : "Guardar Cambios");
        btnGuardar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        btnGuardar.setBackground(new Color(176, 224, 230));
        btnGuardar.setForeground(new Color(70, 130, 180));
        btnGuardar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardarTratamiento());
        buttonPane.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        btnCancelar.setBackground(new Color(176, 224, 230));
        btnCancelar.setForeground(new Color(70, 130, 180));
        btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        buttonPane.add(btnCancelar);

        cargarMedicamentos();
        actualizarEstadoDosis();
    }

    private void guardarTratamiento() {
        if (txtFrecuencia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe indicar la frecuencia del tratamiento.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Medicamento medicamentoElegido;
        int dosis;

        if (cbxMedicamento.getSelectedIndex() <= 0) {
            medicamentoElegido = null;
            dosis = 0;
        } else {
            int indexSeleccionado = cbxMedicamento.getSelectedIndex() - 1;
            String idMedicamento = Clinica.getInstancia().getMedicamentos().get(indexSeleccionado).getId();
            medicamentoElegido = Clinica.getInstancia().buscarMedicamentoXId(idMedicamento);
            dosis = (int) spnDosis.getValue();
        }

        String frecuencia = txtFrecuencia.getText().trim();
        Date fInicio = (Date) spnFechaInicio.getValue();
        Date fFin = (Date) spnFechaFin.getValue();
        String estado = (String) cbxEstado.getSelectedItem();
        String indicaciones = txtDescripcion.getText().trim();

        if (tratamientoEdicion == null) {
            tratamientoCreado = new Tratamiento(
                    0,
                    diagnosticoActual,
                    medicamentoElegido,
                    dosis,
                    frecuencia,
                    fInicio,
                    fFin,
                    indicaciones,
                    estado
            );

        } else {
            tratamientoEdicion.setMedicamento(medicamentoElegido);
            tratamientoEdicion.setDosis(dosis);
            tratamientoEdicion.setFrecuencia(frecuencia);
            tratamientoEdicion.setFechaInicio(fInicio);
            tratamientoEdicion.setFechaFin(fFin);
            tratamientoEdicion.setEstado(estado);
            tratamientoEdicion.setDescripcion(indicaciones);
            tratamientoCreado = tratamientoEdicion;

            TratamientoDAO.getInstance().actualizarTratamiento(tratamientoEdicion);
        }

        dispose();
    }

    public Tratamiento getTratamientoCreado() {
        return tratamientoCreado;
    }

    private void eliminarTratamiento() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar este tratamiento?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (tratamientoEdicion != null) {
                TratamientoDAO.getInstance().eliminarTratamiento(tratamientoEdicion.getIdNumber());
            }
            eliminado = true;
            tratamientoCreado = null;
            dispose();
        }
    }

    public boolean isEliminado() {
        return eliminado;
    }

    private void actualizarEstadoDosis() {
        boolean tieneMedicamento = cbxMedicamento.getSelectedIndex() > 0;

        lblDosis.setEnabled(tieneMedicamento);
        spnDosis.setEnabled(tieneMedicamento);
        lblDosis.setVisible(tieneMedicamento);
        spnDosis.setVisible(tieneMedicamento);
    }

    private void cargarMedicamentos() {
        cbxMedicamento.removeAllItems();
        cbxMedicamento.addItem("Sin Medicamento");
        if (Clinica.getInstancia().getMedicamentos() != null) {
            for (Medicamento m : Clinica.getInstancia().getMedicamentos()) {
                cbxMedicamento.addItem(m.getNombre());
            }
        }
        actualizarEstadoDosis();
    }

    private void cargarDatosEdicion() {
        if (tratamientoEdicion != null) {
            if (tratamientoEdicion.getMedicamento() != null) {
                cbxMedicamento.setSelectedItem(tratamientoEdicion.getMedicamento().getNombre());
            } else {
                cbxMedicamento.setSelectedIndex(0);
            }
            spnDosis.setValue(tratamientoEdicion.getDosis() > 0 ? tratamientoEdicion.getDosis() : 1);
            txtFrecuencia.setText(tratamientoEdicion.getFrecuencia());
            if (tratamientoEdicion.getFechaInicio() != null) {
                spnFechaInicio.setValue(tratamientoEdicion.getFechaInicio());
            }
            if (tratamientoEdicion.getFechaFin() != null) {
                spnFechaFin.setValue(tratamientoEdicion.getFechaFin());
            }
            if (tratamientoEdicion.getEstado() != null) {
                cbxEstado.setSelectedItem(tratamientoEdicion.getEstado());
            }
            txtDescripcion.setText(tratamientoEdicion.getDescripcion());

            actualizarEstadoDosis();
        }
    }

}