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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import logico.Clinica;
import logico.catalogo.Enfermera;
import logico.consultorio.Consulta;
import logico.consultorio.Paciente;
import logico.enfermeria.DetalleVacuna;

public class ListarDetalleVacuna extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JSpinner spinFechaFiltro;
    private JCheckBox chkFiltrarFecha;
    private JComboBox<String> cbxFiltroEstado;
    private JTextField txtBuscarVacuna;

    private JTable tblDetalles;
    private DefaultTableModel tableModel;
    private List<DetalleVacuna> listaDetallesVisibles = new ArrayList<>();

    private boolean soloLectura = false;
    private Consulta consultaEspecifica = null;
    private JButton btnDetalles;

    public ListarDetalleVacuna() {
        this(false);
    }

    public ListarDetalleVacuna(boolean soloLectura) {
        this.soloLectura = soloLectura;
        setTitle(soloLectura ? "Listado General de Detalles de Vacuna (Solo Lectura)" : "Listado General de Detalles de Vacuna");
        inicializarVentana();
    }

    public ListarDetalleVacuna(Consulta consulta) {
        this(true);
        this.consultaEspecifica = consulta;
        setTitle("Detalles de Vacuna de la Consulta");
        cargarTablaDetalles();
    }

    private void inicializarVentana() {
        setBounds(100, 100, 950, 520);
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
        panelFiltros.setBounds(15, 10, 904, 70);
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
        txtBuscarVacuna.setBounds(515, 26, 370, 22);
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
                "Registros de Vacunas",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Bahnschrift", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        panelTabla.setBounds(15, 85, 904, 335);
        contentPanel.add(panelTabla);
        panelTabla.setLayout(new BorderLayout(0, 0));

        String[] columnas = {"ID Consulta", "Vacuna", "Paciente", "Enfermera", "Fecha", "Dosis", "Lote", "Estado", "Observaciones"};
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

        JScrollPane scrollTabla = new JScrollPane(tblDetalles);
        scrollTabla.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(new Color(240, 248, 255));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnDetalles = new JButton(soloLectura ? "Ver Detalles" : "Ver Detalles / Modificar");
        btnDetalles.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnDetalles.setBackground(new Color(176, 224, 230));
        btnDetalles.setForeground(new Color(70, 130, 180));
        btnDetalles.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnDetalles.setFocusPainted(false);
        btnDetalles.setPreferredSize(new Dimension(180, 30));
        btnDetalles.addActionListener(e -> abrirDetalle());
        buttonPane.add(btnDetalles);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnCerrar.setBackground(new Color(176, 224, 230));
        btnCerrar.setForeground(new Color(70, 130, 180));
        btnCerrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setPreferredSize(new Dimension(110, 30));
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);

        cargarTablaDetalles();
    }

    private void abrirDetalle() {
        int selectedRow = tblDetalles.getSelectedRow();
        if (selectedRow < 0 || selectedRow >= listaDetallesVisibles.size()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un registro de la tabla para ver sus detalles.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DetalleVacuna detalle = listaDetallesVisibles.get(selectedRow);
        if (soloLectura || consultaEspecifica != null) {
            MostrarDetalleVacuna ventanaDetalle = new MostrarDetalleVacuna(detalle);
            ventanaDetalle.setModal(true);
            ventanaDetalle.setLocationRelativeTo(this);
            ventanaDetalle.setVisible(true);
        } else {
            CrearDetalleVacuna ventanaDetalle = new CrearDetalleVacuna(detalle);
            ventanaDetalle.setModal(true);
            ventanaDetalle.setLocationRelativeTo(this);
            ventanaDetalle.setVisible(true);
        }

        cargarTablaDetalles();
    }

    private void cargarTablaDetalles() {
        tableModel.setRowCount(0);
        listaDetallesVisibles.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFiltroStr = (chkFiltrarFecha.isSelected() && spinFechaFiltro.getValue() != null)
                ? sdf.format((Date) spinFechaFiltro.getValue()) : "";
        String estadoFiltro = (String) cbxFiltroEstado.getSelectedItem();
        String textoFiltro = txtBuscarVacuna.getText().trim().toLowerCase();

        List<Consulta> consultas = new ArrayList<>();
        if (consultaEspecifica != null) {
            consultas.add(consultaEspecifica);
        } else {
            consultas = Clinica.getInstancia().getConsultas();
        }

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

                        Enfermera enf = detalle.getEnfermera();
                        String nombreEnfermeraStr = (enf != null)
                                ? enf.getNombre() + " " + (enf.getApellido() != null ? enf.getApellido() : "")
                                : "No asignada";

                        boolean coincideTexto = textoFiltro.isEmpty()
                                || (detalle.getId() != null && detalle.getId().toLowerCase().contains(textoFiltro))
                                || (detalle.getLote() != null && detalle.getLote().toLowerCase().contains(textoFiltro))
                                || nombreVacunaStr.toLowerCase().contains(textoFiltro)
                                || nombrePacienteStr.toLowerCase().contains(textoFiltro)
                                || nombreEnfermeraStr.toLowerCase().contains(textoFiltro);

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
                                    nombreEnfermeraStr,
                                    fechaStr,
                                    detalle.getDosis(),
                                    detalle.getLote() != null ? detalle.getLote() : "",
                                    detalle.getEstado(),
                                    detalle.getObservaciones() != null ? detalle.getObservaciones() : ""
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
}