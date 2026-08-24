package visual.consultorio;

import bd.ConexionBD;
import logico.catalogo.Doctor;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class ListarConsulta extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable tableConsultas;
    private DefaultTableModel modelTable;
    private JTextField txtBuscar;

    // Doctor para filtrar las consultas
    private Doctor doctorFiltro;

    public static void main(String[] args) {
        try {
            ListarConsulta dialog = new ListarConsulta(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ListarConsulta() {
        this(null);
    }

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

        JLabel lblBuscar = new JLabel("Buscar (Paciente / Doctor):");
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
        // Se ajustan los encabezados para coincidir con el Procedimiento Almacenado
        String[] headers = {"ID", "Fecha / Hora", "Paciente", "Doctor", "Observaciones"};
        modelTable = new DefaultTableModel(headers, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableConsultas = new JTable(modelTable);
        tableConsultas.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        tableConsultas.setRowHeight(24);
        tableConsultas.getTableHeader().setFont(new Font("Bahnschrift", Font.BOLD, 12));
        tableConsultas.getTableHeader().setBackground(new Color(176, 224, 230));
        tableConsultas.getTableHeader().setForeground(new Color(70, 130, 180));
        tableConsultas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar ancho de columnas
        tableConsultas.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tableConsultas.getColumnModel().getColumn(1).setPreferredWidth(130); // Fecha
        tableConsultas.getColumnModel().getColumn(2).setPreferredWidth(150); // Paciente
        tableConsultas.getColumnModel().getColumn(3).setPreferredWidth(150); // Doctor
        tableConsultas.getColumnModel().getColumn(4).setPreferredWidth(200); // Observaciones

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

        // Carga inicial de datos desde SQL Server
        cargarTabla("");
    }

    /**
     * Llena la tabla ejecutando el procedimiento almacenado en SQL Server.
     */
    private void cargarTabla(String filtro) {
        modelTable.setRowCount(0);
        String sql = "{call str_listar_buscar_consulta(?)}";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, filtro);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idConsulta = rs.getInt("id_consulta");
                    String fecha = rs.getString("Fecha de Consulta");
                    String paciente = rs.getString("Paciente");
                    String doctor = rs.getString("Doctor");
                    String observaciones = rs.getString("observaciones");

                    // Si la ventana fue abierta por un doctor en específico, filtramos visualmente
                    if (doctorFiltro != null) {
                        String nombreDoctorLogueado = doctorFiltro.getNombre() + " " + doctorFiltro.getApellido();
                        // Si el doctor de la consulta no coincide con el logueado, saltamos la fila
                        if (!doctor.equalsIgnoreCase(nombreDoctorLogueado.trim())) {
                            continue;
                        }
                    }

                    modelTable.addRow(new Object[]{
                            idConsulta,
                            fecha,
                            paciente,
                            doctor,
                            (observaciones != null) ? observaciones : "Sin observaciones"
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Fallo al cargar las consultas desde la base de datos.");
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el ID de la consulta seleccionada para abrir su detalle.
     */
    private void verDetallesConsulta() {
        int row = tableConsultas.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una consulta de la lista.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ya no dependemos de una lista en memoria, tomamos el ID directo de la columna 0 del JTable
        int idConsultaSeleccionada = (int) tableConsultas.getValueAt(row, 0);

        // TODO: Abre tu ventana de detalle pasándole el idConsultaSeleccionada
        // RealizarConsulta dialog = new RealizarConsulta(idConsultaSeleccionada);
        // dialog.setModal(true);
        // dialog.setVisible(true);

        // Recargar la tabla al cerrar la ventana de detalle para ver actualizaciones
        cargarTabla(txtBuscar.getText().trim());
    }
}