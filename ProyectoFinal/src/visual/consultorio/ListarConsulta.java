package visual.consultorio;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import logico.catalogo.Doctor;
import logico.Clinica;
import logico.consultorio.Consulta;
import logico.consultorio.Paciente;
import utilidad.Formato;

public class ListarConsulta extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable tableConsultas;
    private DefaultTableModel modelTable;
    private JTextField txtBuscar;

    // Doctor para filtrar las consultas (si es null, muestra todas)
    private Doctor doctorFiltro;

    // Lista auxiliar para sincronizar la fila seleccionada con el objeto Consulta
    private List<Consulta> listaConsultasVisibles = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ListarConsulta dialog = new ListarConsulta(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Constructor por defecto (para pruebas o uso general)
    public ListarConsulta() {
        this(null);
    }

    // Constructor que recibe el Doctor desde Main
    public ListarConsulta(Doctor doctorFiltro) {
        this.doctorFiltro = doctorFiltro;

        String tituloVentana = "Listado de Consultas Médicas";
        if (this.doctorFiltro != null) {
            tituloVentana += " - Dr. " + this.doctorFiltro.getNombre();
        }
        setTitle(tituloVentana);

        setBounds(100, 100, 750, 480);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 10));

        // --- PANEL SUPERIOR DE FILTRO ---
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltro.setBackground(new Color(240, 248, 255));

        JLabel lblBuscar = new JLabel("Buscar (Paciente / Doctor / Cita):");
        lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        lblBuscar.setForeground(new Color(70, 130, 180));
        panelFiltro.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        txtBuscar.setPreferredSize(new Dimension(260, 25));
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cargarTabla(txtBuscar.getText().trim());
            }
        });
        panelFiltro.add(txtBuscar);

        contentPanel.add(panelFiltro, BorderLayout.NORTH);

        // --- TABLA DE CONSULTAS ---
        String[] headers = {"Cita / ID", "Paciente", "Doctor", "Fecha / Hora", "Diagnósticos"};
        modelTable = new DefaultTableModel(headers, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar edición directa en celda
            }
        };

        tableConsultas = new JTable(modelTable);
        tableConsultas.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        tableConsultas.setRowHeight(24);
        tableConsultas.getTableHeader().setFont(new Font("Bahnschrift", Font.BOLD, 12));
        tableConsultas.getTableHeader().setBackground(new Color(176, 224, 230));
        tableConsultas.getTableHeader().setForeground(new Color(70, 130, 180));
        tableConsultas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Evento de doble clic para abrir detalle/modificación
        tableConsultas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    verDetallesConsulta();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableConsultas);
        scrollPane.setBorder(new LineBorder(new Color(135, 206, 235), 1));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // --- PANEL DE BOTONES INFERIOR ---
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        buttonPane.setBackground(new Color(240, 248, 255));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnVerDetalles = new JButton("Ver Detalle / Editar");
        btnVerDetalles.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        btnVerDetalles.setBackground(new Color(176, 224, 230));
        btnVerDetalles.setForeground(new Color(70, 130, 180));
        btnVerDetalles.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnVerDetalles.setFocusPainted(false);
        btnVerDetalles.setPreferredSize(new Dimension(160, 30));
        btnVerDetalles.addActionListener(e -> verDetallesConsulta());
        buttonPane.add(btnVerDetalles);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Bahnschrift", Font.BOLD, 12));
        btnCerrar.setBackground(new Color(176, 224, 230));
        btnCerrar.setForeground(new Color(70, 130, 180));
        btnCerrar.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setPreferredSize(new Dimension(90, 30));
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);

        // Carga inicial de datos
        cargarTabla("");
    }

    /**
     * Llena la tabla filtrando por el doctor actual (si aplica) y por el texto del buscador.
     */
    private void cargarTabla(String filtro) {
        modelTable.setRowCount(0);
        listaConsultasVisibles.clear();

        List<Consulta> todas = Clinica.getInstancia().getConsultas();
        if (todas == null) return;

        String f = filtro.toLowerCase();

        for (Consulta c : todas) {
            if (c == null) continue;

            // 1. FILTRADO POR DOCTOR (Si doctorFiltro no es null)
            if (doctorFiltro != null) {
                Doctor docConsulta = c.getDoctor();
                if (docConsulta == null && c.getCita() != null) {
                    docConsulta = c.getCita().getDoctor();
                }

                if (docConsulta == null) {
                    continue;
                }
                boolean esMismoDoctor = docConsulta.equals(doctorFiltro) ||
                        (docConsulta.getId() != null && docConsulta.getId().equalsIgnoreCase(doctorFiltro.getId()));

                if (!esMismoDoctor) {
                    continue;
                }
            }

            // 2. OBTENCIÓN DE DATOS PARA LA TABLA
            String idCita = (c.getCita() != null && c.getCita().getId() != null) ? c.getCita().getId() : "N/A";

            String nombrePaciente = "Desconocido";
            if (c.getCita() != null && c.getCita().getPaciente() != null) {
                Paciente p = c.getCita().getPaciente();
                nombrePaciente = p.getNombre() + " " + (p.getApellido() != null ? p.getApellido() : "");
            } else if (c.getCita() != null && c.getCita().getNombrePersona() != null) {
                nombrePaciente = c.getCita().getNombrePersona();
            }

            String nombreDoctor = "N/A";
            if (c.getDoctor() != null) {
                nombreDoctor = c.getDoctor().getNombre();
            } else if (c.getCita() != null && c.getCita().getDoctor() != null) {
                nombreDoctor = c.getCita().getDoctor().getNombre();
            }

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

            String fechaStr = (c.getCita() != null && c.getCita().getFechaRegistro() != null)
                    ? c.getCita().getFechaRegistro().format(fmt)
                    : "N/A";

            int cantDiag = (c.getDiagnosticos() != null) ? c.getDiagnosticos().size() : 0;
            String diagSummary = cantDiag + (cantDiag == 1 ? " diagnóstico" : " diagnósticos");

            // 3. FILTRADO POR TEXTO DE BÚSQUEDA
            if (f.isEmpty() || idCita.toLowerCase().contains(f)
                    || nombrePaciente.toLowerCase().contains(f)
                    || nombreDoctor.toLowerCase().contains(f)) {

                listaConsultasVisibles.add(c);
                modelTable.addRow(new Object[]{
                        idCita,
                        nombrePaciente,
                        nombreDoctor,
                        fechaStr,
                        diagSummary
                });
            }
        }
    }

    /**
     * Obtiene la consulta seleccionada y abre el diálogo RealizarConsulta en modo edición.
     */
    private void verDetallesConsulta() {
        int row = tableConsultas.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una consulta de la lista.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Consulta consulta = listaConsultasVisibles.get(row);

        // Abrir la ventana RealizarConsulta pasando la Consulta seleccionada
        RealizarConsulta dialog = new RealizarConsulta(consulta);
        dialog.setModal(true);
        dialog.setVisible(true);

        // Al regresar, recargar la tabla para reflejar cambios o eliminaciones
        cargarTabla(txtBuscar.getText().trim());
    }
}