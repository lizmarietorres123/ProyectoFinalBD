package visual.consultorio;

import bd.ConexionBD;
import bd.catalogo.CitaDAO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class ListarCita extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private DefaultTableModel model;


    private int idCitaSeleccionada = -1;
    private String nombrePacienteSeleccionado = "";

    private JTextField txtBuscar;
    private JTable table;
    private JPanel panelBarra;
    private JPanel panelTable;

    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnCancelar;

    public ListarCita() {
        setTitle("Listado de Citas");
        setBounds(100, 100, 818, 541);
        setLocationRelativeTo(null);
        setModal(true);

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        panelBarra = new JPanel();
        panelBarra.setBackground(Color.WHITE);
        panelBarra.setBorder(new LineBorder(new Color(70, 130, 180)));
        panelBarra.setBounds(28, 20, 738, 60);
        panelBarra.setLayout(null);
        contentPanel.add(panelBarra);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(new Color(70, 130, 180));
        lblBuscar.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        lblBuscar.setBounds(16, 15, 70, 30);
        panelBarra.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setToolTipText("Filtrar por paciente, doctor o estado");
        txtBuscar.setBounds(90, 16, 625, 28);
        txtBuscar.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarTabla(txtBuscar.getText());
            }
        });
        panelBarra.add(txtBuscar);

        panelTable = new JPanel();
        panelTable.setBounds(28, 95, 738, 330);
        panelTable.setBorder(new LineBorder(new Color(70, 130, 180)));
        contentPanel.add(panelTable);
        panelTable.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollTabla = new JScrollPane();
        panelTable.add(scrollTabla, BorderLayout.CENTER);

        model = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] headers = {"Código", "Paciente", "Doctor", "Fecha y Hora", "Estado"};
        model.setColumnIdentifiers(headers);

        table = new JTable(model);
        scrollTabla.setViewportView(table);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(176, 224, 230));
        table.setSelectionForeground(new Color(70, 130, 180));
        table.setGridColor(new Color(173, 216, 230));
        table.getTableHeader().setFont(new Font("Bahnschrift", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(135, 206, 235));
        table.getTableHeader().setForeground(new Color(70, 130, 180));

        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        // Ajustar anchos de columna
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // Código
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Paciente
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Doctor
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Fecha y Hora
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Estado

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index > -1) {
                    // Capturamos el ID y el nombre directamente de la tabla
                    idCitaSeleccionada = (int) table.getValueAt(index, 0);
                    nombrePacienteSeleccionado = table.getValueAt(index, 1).toString();

                    btnModificar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                }
            }
        });

        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(Color.WHITE);
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnEliminar.setForeground(new Color(70, 130, 180));
        btnEliminar.setBackground(new Color(255, 245, 238));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (idCitaSeleccionada != -1) {
                    int option = JOptionPane.showConfirmDialog(
                            null,
                            "¿Está seguro que desea eliminar la cita " + idCitaSeleccionada + " de " + nombrePacienteSeleccionado + "?",
                            "Confirmación",
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (option == JOptionPane.OK_OPTION) {

                        // TODO: Asegúrate de que este método en tu DAO reciba un 'int'
                        CitaDAO.getInstance().eliminarCita(idCitaSeleccionada);

                        idCitaSeleccionada = -1;
                        nombrePacienteSeleccionado = "";
                        btnEliminar.setEnabled(false);
                        btnModificar.setEnabled(false);
                        filtrarTabla(txtBuscar.getText());
                        JOptionPane.showMessageDialog(null,
                                "Cita eliminada exitosamente.",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        buttonPane.add(btnEliminar);

        btnModificar = new JButton("Ver Detalles");
        btnModificar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnModificar.setForeground(new Color(70, 130, 180));
        btnModificar.setBackground(new Color(255, 245, 238));
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (idCitaSeleccionada != -1) {
                    // TODO: Ajusta CrearCita para que reciba el ID (int) en lugar del objeto
                    // CrearCita modCita = new CrearCita(idCitaSeleccionada);
                    // modCita.setModal(true);
                    // modCita.setVisible(true);

                    filtrarTabla(txtBuscar.getText());
                    btnModificar.setEnabled(false);
                    btnEliminar.setEnabled(false);
                    idCitaSeleccionada = -1;
                }
            }
        });
        buttonPane.add(btnModificar);

        btnCancelar = new JButton("Volver");
        btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnCancelar.setForeground(new Color(70, 130, 180));
        btnCancelar.setBackground(new Color(255, 245, 238));
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(btnCancelar);

        filtrarTabla("");
    }

    private void filtrarTabla(String filtro) {
        model.setRowCount(0);
        String sql = "{call str_listar_buscar_cita(?)}";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, filtro);

            try (ResultSet rs = stmt.executeQuery()) {
                SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdfHora = new SimpleDateFormat("hh:mm a");

                while (rs.next()) {
                    int idCita = rs.getInt("id_cita");
                    String paciente = rs.getString("Paciente");
                    String doctor = "Dr. " + rs.getString("Doctor");
                    String estado = rs.getString("estado");

                    // Recuperar y formatear fecha y hora desde SQL Server
                    java.sql.Date sqlFecha = rs.getDate("fecha_consulta");
                    java.sql.Time sqlHora = rs.getTime("hora_consulta");

                    String fechaHoraStr = "N/A";
                    if (sqlFecha != null) {
                        fechaHoraStr = sdfFecha.format(sqlFecha);
                        if (sqlHora != null) {
                            fechaHoraStr += " " + sdfHora.format(sqlHora);
                        }
                    }

                    model.addRow(new Object[]{
                            idCita,
                            paciente,
                            doctor,
                            fechaHoraStr,
                            estado
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Fallo al cargar las citas desde la base de datos.");
            e.printStackTrace();
        }
    }
}