package visual.enfermeria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import logico.Clinica;
import logico.catalogo.Enfermera;
import logico.consultorio.Consulta;
import logico.consultorio.Paciente;
import logico.enfermeria.DetalleVacuna;

public class CrearDetalleVacuna extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JSpinner spinFechaFiltro;
    private JCheckBox chkFiltrarFecha;
    private JComboBox<String> cbxFiltroEstado;
    private JTextField txtBuscarVacuna;

    private JTable tblDetalles;
    private DefaultTableModel tableModel;
    private List<DetalleVacuna> listaDetallesVisibles = new ArrayList<>();

    private JTextField txtNombrePaciente;
    private JTextField txtApellidoPaciente;
    private JTextField txtSexoPaciente;
    private JTextField txtEdadPaciente;
    private JTextField txtNombreVacuna;

    private JSpinner spinDosis;
    private JTextField txtLote;
    private JComboBox<Enfermera> cbxEnfermera;
    private JComboBox<String> cbxEstadoVacuna;
    private JTextArea txtObservaciones;

    private JButton btnGuardar;
    private JButton btnEliminar;

    private DetalleVacuna detalleSeleccionado = null;
    private boolean esModoEdicionDirecta = false;

    public CrearDetalleVacuna() {
        this(null);
    }

    public CrearDetalleVacuna(DetalleVacuna detalleParaEditar) {
        setTitle(detalleParaEditar == null ? "Gestión y Registro de Aplicación de Vacunas" : "Detalle / Modificar Aplicación de Vacuna");
        setBounds(100, 100, 850, 710);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel panelFiltros = new JPanel();
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(new TitledBorder(
                new LineBorder(new Color(135, 206, 235), 2),
                "Filtros de Búsqueda",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        panelFiltros.setBounds(15, 10, 804, 70);
        contentPanel.add(panelFiltros);
        panelFiltros.setLayout(null);

        chkFiltrarFecha = new JCheckBox("Filtrar Fecha:");
        chkFiltrarFecha.setSelected(false);
        chkFiltrarFecha.setBackground(Color.WHITE);
        chkFiltrarFecha.setForeground(new Color(70, 130, 180));
        chkFiltrarFecha.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        chkFiltrarFecha.setBounds(12, 26, 110, 22);
        chkFiltrarFecha.addActionListener(e -> {
            spinFechaFiltro.setEnabled(chkFiltrarFecha.isSelected());
            cargarTablaDetalles();
        });
        panelFiltros.add(chkFiltrarFecha);

        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        spinFechaFiltro = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinFechaFiltro, "dd/MM/yyyy");
        spinFechaFiltro.setEditor(dateEditor);
        spinFechaFiltro.setEnabled(false);
        spinFechaFiltro.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        spinFechaFiltro.setBounds(125, 26, 110, 22);
        spinFechaFiltro.addChangeListener(e -> cargarTablaDetalles());
        panelFiltros.add(spinFechaFiltro);

        JLabel lblFiltroEstado = new JLabel("Estado:");
        lblFiltroEstado.setForeground(new Color(70, 130, 180));
        lblFiltroEstado.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblFiltroEstado.setBounds(255, 29, 50, 16);
        panelFiltros.add(lblFiltroEstado);

        cbxFiltroEstado = new JComboBox<>();
        cbxFiltroEstado.setModel(new DefaultComboBoxModel<>(new String[] {"Todos", "Pendiente", "Aplicada", "Cancelada"}));
        cbxFiltroEstado.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxFiltroEstado.setBackground(new Color(224, 247, 250));
        cbxFiltroEstado.setBounds(310, 26, 130, 22);
        cbxFiltroEstado.addActionListener(e -> cargarTablaDetalles());
        panelFiltros.add(cbxFiltroEstado);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(new Color(70, 130, 180));
        lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblBuscar.setBounds(460, 29, 50, 16);
        panelFiltros.add(lblBuscar);

        txtBuscarVacuna = new JTextField();
        txtBuscarVacuna.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtBuscarVacuna.setBounds(515, 26, 270, 22);
        txtBuscarVacuna.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cargarTablaDetalles();
            }
        });
        panelFiltros.add(txtBuscarVacuna);

        JPanel panelTabla = new JPanel();
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(new TitledBorder(
                new LineBorder(new Color(135, 206, 235), 2),
                "Seleccionar Detalle de Vacuna",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        panelTabla.setBounds(15, 85, 804, 180);
        contentPanel.add(panelTabla);
        panelTabla.setLayout(new BorderLayout(0, 0));

        String[] columnas = {"ID Consulta", "Vacuna", "Paciente", "Fecha Consulta", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDetalles = new JTable(tableModel);
        tblDetalles.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        tblDetalles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDetalles.getTableHeader().setFont(new Font("Bahnschrift", Font.BOLD, 12));
        tblDetalles.getTableHeader().setBackground(new Color(176, 224, 230));
        tblDetalles.getTableHeader().setForeground(new Color(70, 130, 180));
        tblDetalles.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblDetalles.getSelectedRow();
                if (row >= 0 && row < listaDetallesVisibles.size()) {
                    seleccionarDetalle(listaDetallesVisibles.get(row));
                }
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tblDetalles);
        scrollTabla.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        JPanel panelPaciente = new JPanel();
        panelPaciente.setBackground(Color.WHITE);
        panelPaciente.setBorder(new TitledBorder(
                new LineBorder(new Color(135, 206, 235), 2),
                "Información Principal del Paciente",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        panelPaciente.setBounds(15, 270, 804, 100);
        contentPanel.add(panelPaciente);
        panelPaciente.setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(new Color(70, 130, 180));
        lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblNombre.setBounds(15, 28, 60, 14);
        panelPaciente.add(lblNombre);

        txtNombrePaciente = new JTextField();
        txtNombrePaciente.setEditable(false);
        txtNombrePaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtNombrePaciente.setBackground(new Color(224, 247, 250));
        txtNombrePaciente.setBounds(80, 25, 180, 22);
        panelPaciente.add(txtNombrePaciente);

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setForeground(new Color(70, 130, 180));
        lblApellido.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblApellido.setBounds(280, 28, 60, 14);
        panelPaciente.add(lblApellido);

        txtApellidoPaciente = new JTextField();
        txtApellidoPaciente.setEditable(false);
        txtApellidoPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtApellidoPaciente.setBackground(new Color(224, 247, 250));
        txtApellidoPaciente.setBounds(345, 25, 180, 22);
        panelPaciente.add(txtApellidoPaciente);

        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setForeground(new Color(70, 130, 180));
        lblSexo.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblSexo.setBounds(545, 28, 40, 14);
        panelPaciente.add(lblSexo);

        txtSexoPaciente = new JTextField();
        txtSexoPaciente.setEditable(false);
        txtSexoPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtSexoPaciente.setBackground(new Color(224, 247, 250));
        txtSexoPaciente.setBounds(590, 25, 80, 22);
        panelPaciente.add(txtSexoPaciente);

        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setForeground(new Color(70, 130, 180));
        lblEdad.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEdad.setBounds(685, 28, 40, 14);
        panelPaciente.add(lblEdad);

        txtEdadPaciente = new JTextField();
        txtEdadPaciente.setEditable(false);
        txtEdadPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtEdadPaciente.setBackground(new Color(224, 247, 250));
        txtEdadPaciente.setBounds(730, 25, 55, 22);
        panelPaciente.add(txtEdadPaciente);

        JLabel lblVacunaInfo = new JLabel("Vacuna:");
        lblVacunaInfo.setForeground(new Color(70, 130, 180));
        lblVacunaInfo.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblVacunaInfo.setBounds(15, 62, 60, 14);
        panelPaciente.add(lblVacunaInfo);

        txtNombreVacuna = new JTextField();
        txtNombreVacuna.setEditable(false);
        txtNombreVacuna.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        txtNombreVacuna.setBackground(new Color(224, 247, 250));
        txtNombreVacuna.setBounds(80, 59, 705, 22);
        panelPaciente.add(txtNombreVacuna);

        JPanel panelEnfermera = new JPanel();
        panelEnfermera.setBackground(Color.WHITE);
        panelEnfermera.setBorder(new TitledBorder(
                new LineBorder(new Color(135, 206, 235), 2),
                "Registro de Aplicación",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        panelEnfermera.setBounds(15, 375, 804, 240);
        contentPanel.add(panelEnfermera);
        panelEnfermera.setLayout(null);

        JLabel lblDosis = new JLabel("Dosis Aplicada:");
        lblDosis.setForeground(new Color(70, 130, 180));
        lblDosis.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblDosis.setBounds(15, 30, 100, 14);
        panelEnfermera.add(lblDosis);

        spinDosis = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spinDosis.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        spinDosis.setBounds(115, 26, 70, 22);
        panelEnfermera.add(spinDosis);

        JLabel lblLote = new JLabel("Lote:");
        lblLote.setForeground(new Color(70, 130, 180));
        lblLote.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblLote.setBounds(215, 30, 40, 14);
        panelEnfermera.add(lblLote);

        txtLote = new JTextField();
        txtLote.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtLote.setBounds(255, 26, 160, 22);
        panelEnfermera.add(txtLote);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(new Color(70, 130, 180));
        lblEstado.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEstado.setBounds(440, 30, 60, 14);
        panelEnfermera.add(lblEstado);

        cbxEstadoVacuna = new JComboBox<>();
        cbxEstadoVacuna.setModel(new DefaultComboBoxModel<>(new String[] {"Pendiente", "Aplicada", "Cancelada"}));
        cbxEstadoVacuna.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxEstadoVacuna.setBackground(new Color(224, 247, 250));
        cbxEstadoVacuna.setBounds(500, 26, 160, 22);
        panelEnfermera.add(cbxEstadoVacuna);

        JLabel lblEnfermeraObj = new JLabel("Enfermera:");
        lblEnfermeraObj.setForeground(new Color(70, 130, 180));
        lblEnfermeraObj.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEnfermeraObj.setBounds(15, 68, 80, 14);
        panelEnfermera.add(lblEnfermeraObj);

        cbxEnfermera = new JComboBox<>();
        cbxEnfermera.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxEnfermera.setBackground(new Color(224, 247, 250));
        cbxEnfermera.setBounds(95, 65, 320, 22);
        panelEnfermera.add(cbxEnfermera);

        JLabel lblObservaciones = new JLabel("Observaciones / Notas:");
        lblObservaciones.setForeground(new Color(70, 130, 180));
        lblObservaciones.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblObservaciones.setBounds(15, 105, 160, 14);
        panelEnfermera.add(lblObservaciones);

        JScrollPane scrollObservaciones = new JScrollPane();
        scrollObservaciones.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        scrollObservaciones.setBounds(15, 125, 770, 100);
        panelEnfermera.add(scrollObservaciones);

        txtObservaciones = new JTextArea();
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        scrollObservaciones.setViewportView(txtObservaciones);

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(240, 248, 255));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(176, 224, 230));
        btnGuardar.setForeground(new Color(70, 130, 180));
        btnGuardar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(140, 30));
        btnGuardar.addActionListener(e -> guardarDatosVacuna());
        buttonPane.add(btnGuardar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnEliminar.setBackground(new Color(255, 182, 193));
        btnEliminar.setForeground(new Color(178, 34, 34));
        btnEliminar.setBorder(new LineBorder(new Color(240, 128, 128), 2));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setPreferredSize(new Dimension(110, 30));
        btnEliminar.setVisible(false);
        btnEliminar.addActionListener(e -> eliminarVacuna());
        buttonPane.add(btnEliminar);

        JButton btnCancelar = new JButton("Cancelar / Salir");
        btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnCancelar.setBackground(new Color(176, 224, 230));
        btnCancelar.setForeground(new Color(70, 130, 180));
        btnCancelar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(130, 30));
        btnCancelar.addActionListener(e -> dispose());
        buttonPane.add(btnCancelar);

        cargarComboEnfermeras();
        cargarTablaDetalles();

        if (detalleParaEditar != null) {
            this.esModoEdicionDirecta = true;
            seleccionarDetalle(detalleParaEditar);
            btnEliminar.setVisible(true);
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

    private void cargarTablaDetalles() {
        tableModel.setRowCount(0);
        listaDetallesVisibles.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFiltroStr = (chkFiltrarFecha.isSelected() && spinFechaFiltro.getValue() != null)
                ? sdf.format((Date) spinFechaFiltro.getValue()) : "";
        String estadoFiltro = (String) cbxFiltroEstado.getSelectedItem();
        String textoFiltro = txtBuscarVacuna.getText().trim().toLowerCase();

        List<Consulta> consultas = Clinica.getInstancia().getConsultas();
        if (consultas != null) {
            for (Consulta consulta : consultas) {
                if (consulta != null && consulta.getVacunas() != null) {

                    boolean coincideFecha = true;
                    if (chkFiltrarFecha.isSelected() && !fechaFiltroStr.isEmpty()) {
                        String fechaConsultaStr = "";
                        if (consulta.getFechaHora() != null) {
                            Date fechaDate = Date.from(consulta.getFechaHora().atZone(ZoneId.systemDefault()).toInstant());
                            fechaConsultaStr = sdf.format(fechaDate);
                        } else if (consulta.getCita() != null && consulta.getCita().getFechaRegistro() != null) {
                            fechaConsultaStr = sdf.format(consulta.getCita().getFechaRegistro());
                        }
                        if (!fechaConsultaStr.equals(fechaFiltroStr)) {
                            coincideFecha = false;
                        }
                    }

                    if (!coincideFecha) continue;

                    for (DetalleVacuna detalle : consulta.getVacunas()) {
                        if (detalle == null) continue;

                        boolean coincideEstado = estadoFiltro.equalsIgnoreCase("Todos")
                                || (detalle.getEstado() != null && detalle.getEstado().equalsIgnoreCase(estadoFiltro));

                        if (!coincideEstado) continue;

                        Paciente paciente = obtenerPacienteDeConsulta(consulta);
                        String nombrePacienteStr = (paciente != null)
                                ? paciente.getNombre() + " " + (paciente.getApellido() != null ? paciente.getApellido() : "")
                                : (consulta.getCita() != null ? consulta.getCita().getNombrePersona() : "N/A");

                        String nombreVacunaStr = (detalle.getVacuna() != null && detalle.getVacuna().getNombre() != null)
                                ? detalle.getVacuna().getNombre() : "N/A";

                        boolean coincideTexto = textoFiltro.isEmpty()
                                || (detalle.getId() != null && detalle.getId().toLowerCase().contains(textoFiltro))
                                || (detalle.getLote() != null && detalle.getLote().toLowerCase().contains(textoFiltro))
                                || nombreVacunaStr.toLowerCase().contains(textoFiltro)
                                || nombrePacienteStr.toLowerCase().contains(textoFiltro);

                        if (coincideTexto) {
                            listaDetallesVisibles.add(detalle);
                            String fechaStr = (consulta.getFechaHora() != null)
                                    ? consulta.getFechaHora().toString().replace("T", " ")
                                    : (consulta.getCita() != null && consulta.getCita().getFechaRegistro() != null
                                    ? sdf.format(consulta.getCita().getFechaRegistro()) : "N/A");

                            tableModel.addRow(new Object[] {
                                    consulta.getId(),
                                    nombreVacunaStr,
                                    nombrePacienteStr,
                                    fechaStr,
                                    detalle.getEstado()
                            });
                        }
                    }
                }
            }
        }
    }

    private void seleccionarDetalle(DetalleVacuna detalle) {
        this.detalleSeleccionado = detalle;
        if (detalle == null) {
            limpiarFormulario();
            return;
        }

        btnEliminar.setVisible(true);

        Consulta consulta = detalle.getConsulta();
        Paciente paciente = obtenerPacienteDeConsulta(consulta);

        if (paciente != null) {
            txtNombrePaciente.setText(paciente.getNombre());
            txtApellidoPaciente.setText(paciente.getApellido() != null ? paciente.getApellido() : "");
            txtSexoPaciente.setText(paciente.getSexo() != null ? paciente.getSexo() : "N/A");
            txtEdadPaciente.setText(calcularEdad(paciente.getFecNacim()) + " años");
        } else if (consulta != null && consulta.getCita() != null) {
            txtNombrePaciente.setText(consulta.getCita().getNombrePersona() != null ? consulta.getCita().getNombrePersona() : "N/A");
            txtApellidoPaciente.setText("");
            txtSexoPaciente.setText("N/A");
            txtEdadPaciente.setText("N/A");
        }

        if (detalle.getVacuna() != null) {
            txtNombreVacuna.setText(detalle.getVacuna().getNombre());
        } else {
            txtNombreVacuna.setText("N/A");
        }

        if (detalle.getEnfermera() != null) {
            cbxEnfermera.setSelectedItem(detalle.getEnfermera());
        } else {
            cbxEnfermera.setSelectedIndex(0);
        }

        spinDosis.setValue(detalle.getDosis() > 0 ? detalle.getDosis() : 1);
        txtLote.setText(detalle.getLote() != null ? detalle.getLote() : "");
        cbxEstadoVacuna.setSelectedItem(detalle.getEstado() != null ? detalle.getEstado() : "Pendiente");
        txtObservaciones.setText(detalle.getObservaciones() != null ? detalle.getObservaciones() : "");
    }

    private void guardarDatosVacuna() {
        if (detalleSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un detalle de vacuna de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        detalleSeleccionado.setDosis((int) spinDosis.getValue());
        detalleSeleccionado.setLote(txtLote.getText().trim());
        detalleSeleccionado.setEnfermera((Enfermera) cbxEnfermera.getSelectedItem());
        detalleSeleccionado.setEstado((String) cbxEstadoVacuna.getSelectedItem());
        detalleSeleccionado.setObservaciones(txtObservaciones.getText().trim());
        detalleSeleccionado.setFecha_aplicacion(LocalDateTime.now());

        JOptionPane.showMessageDialog(this, "Registro de vacuna actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        if (esModoEdicionDirecta) {
            dispose();
        } else {
            cargarTablaDetalles();
            limpiarFormulario();
        }
    }

    private void eliminarVacuna() {
        if (detalleSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un detalle para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar este detalle de vacuna?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            Consulta consulta = detalleSeleccionado.getConsulta();
            if (consulta != null && consulta.getVacunas() != null) {
                consulta.getVacunas().remove(detalleSeleccionado);
            }
            JOptionPane.showMessageDialog(this, "Detalle de vacuna eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (esModoEdicionDirecta) {
                dispose();
            } else {
                cargarTablaDetalles();
                limpiarFormulario();
            }
        }
    }

    private Paciente obtenerPacienteDeConsulta(Consulta consulta) {
        if (consulta == null) return null;
        if (consulta.getCita() != null) {
            if (consulta.getCita().getPaciente() != null) {
                return consulta.getCita().getPaciente();
            }
            return Clinica.getInstancia().buscarPacienteXIdentificacion(consulta.getCita().getIdPersona());
        }
        return null;
    }

    private int calcularEdad(Date fecNacim) {
        if (fecNacim == null) return 0;
        Calendar fechaNac = Calendar.getInstance();
        fechaNac.setTime(fecNacim);
        Calendar hoy = Calendar.getInstance();

        int edad = hoy.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
        if (hoy.get(Calendar.DAY_OF_YEAR) < fechaNac.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }
        return edad;
    }

    private void limpiarFormulario() {
        this.detalleSeleccionado = null;
        txtNombrePaciente.setText("");
        txtApellidoPaciente.setText("");
        txtSexoPaciente.setText("");
        txtEdadPaciente.setText("");
        txtNombreVacuna.setText("");
        spinDosis.setValue(1);
        txtLote.setText("");
        cbxEnfermera.setSelectedIndex(0);
        cbxEstadoVacuna.setSelectedIndex(0);
        txtObservaciones.setText("");
        btnEliminar.setVisible(false);
    }
}