package visual;

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
import java.text.SimpleDateFormat;
import java.util.List;

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

import controllers.CitaController;
import logico.Cita;
import visual.registro.RegistrarCita;

public class ListarCita extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private DefaultTableModel model;
    private Object[] row;
    private Cita auxCita = null;
    private final CitaController controller;

    private JTextField txtBuscar;
    private JTable table;
    private JPanel panelBarra;
    private JPanel panelTable;

    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnCancelar;

    public static void main(String[] args) {
        try {
            ListarCita dialog = new ListarCita();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ListarCita() {
        this.controller = new CitaController();

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
        txtBuscar.setToolTipText("Filtrar por código, paciente, cédula, doctor o estado");
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

        table = new JTable();
        scrollTabla.setViewportView(table);

        model = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] headers = {"Código", "Paciente", "Doctor", "Fecha y Hora", "Estado"};
        model.setColumnIdentifiers(headers);

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

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index > -1) {
                    String id = table.getValueAt(index, 0).toString();
                    auxCita = controller.buscarCitaPorId(id);
                    if (auxCita != null) {
                        btnModificar.setEnabled(true);
                        btnEliminar.setEnabled(true);
                    }
                }
            }
        });
        table.setModel(model);

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
                if (auxCita != null) {
                    int option = JOptionPane.showConfirmDialog(
                            null,
                            "¿Está seguro que desea eliminar la cita " + auxCita.getIdCita() + " de " + auxCita.getNombrePersona() + "?",
                            "Confirmación",
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (option == JOptionPane.OK_OPTION) {
                        controller.eliminarCita(auxCita);
                        auxCita = null;
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
                if (auxCita != null) {
                    RegistrarCita modCita = new RegistrarCita(auxCita);
                    modCita.setModal(true);
                    modCita.setVisible(true);
                    filtrarTabla(txtBuscar.getText());
                    btnModificar.setEnabled(false);
                    btnEliminar.setEnabled(false);
                    auxCita = null;
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        List<Cita> citas = controller.filtrarCitas(filtro);

        if (citas != null) {
            for (Cita c : citas) {
                if (c != null) {
                    row = new Object[5];
                    row[0] = c.getIdCita();
                    row[1] = c.getNombrePersona();
                    row[2] = (c.getDoctor() != null) ? "Dr. " + c.getDoctor().getNombre() : "N/A";
                    row[3] = (c.getFechaHora() != null) ? sdf.format(c.getFechaHora()) : "N/A";
                    row[4] = (c.getEstado() != null) ? c.getEstado().toString() : "N/A";
                    model.addRow(row);
                }
            }
        }
    }
}