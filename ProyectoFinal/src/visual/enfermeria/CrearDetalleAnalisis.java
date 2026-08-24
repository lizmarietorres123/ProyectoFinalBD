package visual.enfermeria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import bd.catalogo.DetalleAnalisisDAO;
import logico.Clinica;
import logico.catalogo.Enfermera;
import logico.consultorio.Consulta;
import logico.consultorio.Paciente;
import logico.enfermeria.DetalleAnalisis;

public class CrearDetalleAnalisis extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JSpinner spinFechaFiltro;
    private JCheckBox chkFiltrarFecha;
    private JComboBox<String> cbxFiltroEstado;
    private JTextField txtBuscarAnalisis;

    private JTable tblDetalles;
    private DefaultTableModel tableModel;
    private List<DetalleAnalisis> listaDetallesVisibles = new ArrayList<>();

    private JTextField txtNombrePaciente;
    private JTextField txtApellidoPaciente;
    private JTextField txtSexoPaciente;
    private JTextField txtEdadPaciente;
    private JTextField txtNombreAnalisis;

    private JComboBox<Enfermera> cbxEnfermera;
    private JTextArea txtResultado;
    private JComboBox<String> cbxEstadoAnalisis;
    private JTextArea txtObservaciones;

    private JButton btnGuardar;
    private JButton btnEliminar;

    private DetalleAnalisis detalleSeleccionado = null;
    private boolean esModoEdicionDirecta = false;

    public CrearDetalleAnalisis() {
        this(null);
    }

    public CrearDetalleAnalisis(DetalleAnalisis detalleParaEditar) {
        setTitle(detalleParaEditar == null ? "Gestión y Registro de Análisis Clínicos" : "Detalle / Modificar Análisis Clínico");
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
        cbxFiltroEstado.setModel(new DefaultComboBoxModel<>(new String[] {"Todos", "Pendiente", "En proceso", "Completado"}));
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

        txtBuscarAnalisis = new JTextField();
        txtBuscarAnalisis.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtBuscarAnalisis.setBounds(515, 26, 270, 22);
        txtBuscarAnalisis.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cargarTablaDetalles();
            }
        });
        panelFiltros.add(txtBuscarAnalisis);

        JPanel panelTabla = new JPanel();
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(new TitledBorder(
                new LineBorder(new Color(135, 206, 235), 2),
                "Seleccionar Análisis",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        panelTabla.setBounds(15, 85, 804, 160);
        contentPanel.add(panelTabla);
        panelTabla.setLayout(new BorderLayout(0, 0));

        String[] columnas = {"ID Consulta", "Análisis", "Paciente", "Fecha", "Estado"};
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
                "Información del Paciente",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        panelPaciente.setBounds(15, 250, 804, 95);
        contentPanel.add(panelPaciente);
        panelPaciente.setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(new Color(70, 130, 180));
        lblNombre.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblNombre.setBounds(15, 25, 60, 14);
        panelPaciente.add(lblNombre);

        txtNombrePaciente = new JTextField();
        txtNombrePaciente.setEditable(false);
        txtNombrePaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtNombrePaciente.setBackground(new Color(224, 247, 250));
        txtNombrePaciente.setBounds(80, 22, 180, 22);
        panelPaciente.add(txtNombrePaciente);

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setForeground(new Color(70, 130, 180));
        lblApellido.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblApellido.setBounds(280, 25, 60, 14);
        panelPaciente.add(lblApellido);

        txtApellidoPaciente = new JTextField();
        txtApellidoPaciente.setEditable(false);
        txtApellidoPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtApellidoPaciente.setBackground(new Color(224, 247, 250));
        txtApellidoPaciente.setBounds(345, 22, 180, 22);
        panelPaciente.add(txtApellidoPaciente);

        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setForeground(new Color(70, 130, 180));
        lblSexo.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblSexo.setBounds(545, 25, 40, 14);
        panelPaciente.add(lblSexo);

        txtSexoPaciente = new JTextField();
        txtSexoPaciente.setEditable(false);
        txtSexoPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtSexoPaciente.setBackground(new Color(224, 247, 250));
        txtSexoPaciente.setBounds(590, 22, 80, 22);
        panelPaciente.add(txtSexoPaciente);

        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setForeground(new Color(70, 130, 180));
        lblEdad.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEdad.setBounds(685, 25, 40, 14);
        panelPaciente.add(lblEdad);

        txtEdadPaciente = new JTextField();
        txtEdadPaciente.setEditable(false);
        txtEdadPaciente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtEdadPaciente.setBackground(new Color(224, 247, 250));
        txtEdadPaciente.setBounds(730, 22, 55, 22);
        panelPaciente.add(txtEdadPaciente);

        JLabel lblAnalisisInfo = new JLabel("Análisis:");
        lblAnalisisInfo.setForeground(new Color(70, 130, 180));
        lblAnalisisInfo.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblAnalisisInfo.setBounds(15, 58, 60, 14);
        panelPaciente.add(lblAnalisisInfo);

        txtNombreAnalisis = new JTextField();
        txtNombreAnalisis.setEditable(false);
        txtNombreAnalisis.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        txtNombreAnalisis.setBackground(new Color(224, 247, 250));
        txtNombreAnalisis.setBounds(80, 55, 705, 22);
        panelPaciente.add(txtNombreAnalisis);

        JPanel panelResultado = new JPanel();
        panelResultado.setBackground(Color.WHITE);
        panelResultado.setBorder(new TitledBorder(
                new LineBorder(new Color(135, 206, 235), 2),
                "Resultados y Observaciones",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        panelResultado.setBounds(15, 350, 804, 260);
        contentPanel.add(panelResultado);
        panelResultado.setLayout(null);

        JLabel lblEnfermera = new JLabel("Enfermera:");
        lblEnfermera.setForeground(new Color(70, 130, 180));
        lblEnfermera.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEnfermera.setBounds(15, 25, 80, 14);
        panelResultado.add(lblEnfermera);

        cbxEnfermera = new JComboBox<>();
        cbxEnfermera.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxEnfermera.setBackground(new Color(224, 247, 250));
        cbxEnfermera.setBounds(15, 45, 500, 22);
        panelResultado.add(cbxEnfermera);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(new Color(70, 130, 180));
        lblEstado.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblEstado.setBounds(540, 25, 60, 14);
        panelResultado.add(lblEstado);

        cbxEstadoAnalisis = new JComboBox<>();
        cbxEstadoAnalisis.setModel(new DefaultComboBoxModel<>(new String[] {"Pendiente", "En proceso", "Completado"}));
        cbxEstadoAnalisis.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        cbxEstadoAnalisis.setBackground(new Color(224, 247, 250));
        cbxEstadoAnalisis.setBounds(540, 45, 245, 22);
        panelResultado.add(cbxEstadoAnalisis);

        JLabel lblResultado = new JLabel("Resultado:");
        lblResultado.setForeground(new Color(70, 130, 180));
        lblResultado.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblResultado.setBounds(15, 75, 80, 14);
        panelResultado.add(lblResultado);

        JScrollPane scrollRes = new JScrollPane();
        scrollRes.setBounds(15, 95, 770, 65);
        panelResultado.add(scrollRes);

        txtResultado = new JTextArea();
        txtResultado.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        scrollRes.setViewportView(txtResultado);

        JLabel lblObservaciones = new JLabel("Observaciones / Notas:");
        lblObservaciones.setForeground(new Color(70, 130, 180));
        lblObservaciones.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblObservaciones.setBounds(15, 168, 160, 14);
        panelResultado.add(lblObservaciones);

        JScrollPane scrollObs = new JScrollPane();
        scrollObs.setBounds(15, 185, 770, 60);
        panelResultado.add(scrollObs);

        txtObservaciones = new JTextArea();
        txtObservaciones.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        scrollObs.setViewportView(txtObservaciones);

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
        btnGuardar.addActionListener(e -> guardarDatosAnalisis());
        buttonPane.add(btnGuardar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnEliminar.setBackground(new Color(255, 182, 193));
        btnEliminar.setForeground(new Color(178, 34, 34));
        btnEliminar.setBorder(new LineBorder(new Color(240, 128, 128), 2));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setPreferredSize(new Dimension(110, 30));
        btnEliminar.setVisible(false);
        btnEliminar.addActionListener(e -> eliminarAnalisis());
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


    private void guardarDatosAnalisis() {
        if (detalleSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un detalle de análisis.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double res = Double.parseDouble(txtResultado.getText().trim());
            detalleSeleccionado.setResultado(res);
        } catch (NumberFormatException ex) {
            detalleSeleccionado.setResultado(0.0);
        }

        detalleSeleccionado.setEnfermera((Enfermera) cbxEnfermera.getSelectedItem());
        detalleSeleccionado.setEstado((String) cbxEstadoAnalisis.getSelectedItem());
        detalleSeleccionado.setObservaciones(txtObservaciones.getText().trim());

        JOptionPane.showMessageDialog(this, "Registro de análisis actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        if (esModoEdicionDirecta) {
            dispose();
        } else {
            cargarTablaDetalles();
            limpiarFormulario();
        }
    }

    private void eliminarAnalisis() {
        if (detalleSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un detalle para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar este detalle de análisis?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            Consulta consulta = detalleSeleccionado.getConsulta();

            DetalleAnalisisDAO.getInstance().eliminarDetalleAnalisis(detalleSeleccionado.getIdNumber());
            consulta.getAnalisis().remove(detalleSeleccionado);

            JOptionPane.showMessageDialog(this, "Detalle de análisis eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (esModoEdicionDirecta) {
                dispose();
            } else {
                cargarTablaDetalles();
                limpiarFormulario();
            }
        }
    }

    private void seleccionarDetalle(DetalleAnalisis detalle) {
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

        if (detalle.getAnalisis() != null) {
            txtNombreAnalisis.setText(detalle.getAnalisis().getNombre());
        } else {
            txtNombreAnalisis.setText("N/A");
        }

        if (detalle.getEnfermera() != null) {
            cbxEnfermera.setSelectedItem(detalle.getEnfermera());
        } else {
            cbxEnfermera.setSelectedIndex(0);
        }

        txtResultado.setText(detalle.getResultado() != null ? detalle.getResultado().toString() : "");
        cbxEstadoAnalisis.setSelectedItem(detalle.getEstado() != null ? detalle.getEstado() : "Pendiente");
        txtObservaciones.setText(detalle.getObservaciones() != null ? detalle.getObservaciones() : "");
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
        String textoFiltro = txtBuscarAnalisis.getText().trim().toLowerCase();

        List<Consulta> consultas = Clinica.getInstancia().getConsultas();
        if (consultas != null) {
            for (Consulta consulta : consultas) {
                if (consulta != null && consulta.getAnalisis() != null) {

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

                    for (DetalleAnalisis detalle : consulta.getAnalisis()) {
                        if (detalle == null) continue;

                        boolean coincideEstado = estadoFiltro.equalsIgnoreCase("Todos")
                                || (detalle.getEstado() != null && detalle.getEstado().equalsIgnoreCase(estadoFiltro));

                        if (!coincideEstado) continue;

                        Paciente paciente = obtenerPacienteDeConsulta(consulta);
                        String nombrePacienteStr = (paciente != null)
                                ? paciente.getNombre() + " " + (paciente.getApellido() != null ? paciente.getApellido() : "")
                                : (consulta.getCita() != null ? consulta.getCita().getNombrePersona() : "N/A");

                        String nombreAnalisisStr = (detalle.getAnalisis() != null && detalle.getAnalisis().getNombre() != null)
                                ? detalle.getAnalisis().getNombre() : "N/A";

                        boolean coincideTexto = textoFiltro.isEmpty()
                                || (detalle.getId() != null && detalle.getId().toLowerCase().contains(textoFiltro))
                                || nombreAnalisisStr.toLowerCase().contains(textoFiltro)
                                || nombrePacienteStr.toLowerCase().contains(textoFiltro);

                        if (coincideTexto) {
                            listaDetallesVisibles.add(detalle);
                            String fechaStr = (consulta.getFechaHora() != null)
                                    ? consulta.getFechaHora().toString().replace("T", " ")
                                    : (consulta.getCita() != null && consulta.getCita().getFechaRegistro() != null
                                    ? sdf.format(consulta.getCita().getFechaRegistro()) : "N/A");

                            tableModel.addRow(new Object[] {
                                    consulta.getId(),
                                    nombreAnalisisStr,
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
        txtNombreAnalisis.setText("");
        cbxEnfermera.setSelectedIndex(0);
        txtResultado.setText("");
        cbxEstadoAnalisis.setSelectedIndex(0);
        txtObservaciones.setText("");
        btnEliminar.setVisible(false);
    }
}