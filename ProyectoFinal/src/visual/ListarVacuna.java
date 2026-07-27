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
import java.util.ArrayList;

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

import logico.Clinica;
import logico.Enfermedad;
import logico.Paciente;
import logico.Vacuna;
import visual.registro.RegistrarVacuna;

public class ListarVacuna extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private DefaultTableModel model;
    private Object[] row;
    private Paciente auxPaciente;
    private String auxOpcion;
    private boolean esModoAplicar;
    private Vacuna auxVacuna = null;

    private JTextField txtBuscar;
    private JTable table;
    private JPanel panelBarra;
    private JPanel panelTable;

    private JButton btnAplicar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnCancelar;

    public static void main(String[] args) {
        try {
            ListarVacuna dialog = new ListarVacuna(null, "Mantenimiento");
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ListarVacuna(Paciente paciente, String opcion) {
        auxPaciente = paciente;
        auxOpcion = opcion != null ? opcion : "Mantenimiento";
        esModoAplicar = (auxPaciente != null) && (auxOpcion.equalsIgnoreCase("Aplicar") || auxOpcion.equalsIgnoreCase("Agregar"));

        setBounds(100, 100, 818, 541);
        setLocationRelativeTo(null);
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
        txtBuscar.setToolTipText("Filtrar por código, nombre, fabricante o enfermedad");
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
            public Class<?> getColumnClass(int columnIndex) {
                if (esModoAplicar && columnIndex == 0) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return esModoAplicar && column == 0;
            }
        };

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
                    int colCodigo = esModoAplicar ? 1 : 0;
                    String id = table.getValueAt(index, colCodigo).toString();
                    auxVacuna = Clinica.getInstancia().buscarVacunaXId(id);
                    if (!esModoAplicar && auxVacuna != null) {
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
                if (auxVacuna != null) {
                    int option = JOptionPane.showConfirmDialog(
                            null,
                            "¿Está seguro que desea eliminar la vacuna: " + auxVacuna.getNombre() + "?",
                            "Confirmación",
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (option == JOptionPane.OK_OPTION) {
                        Clinica.getInstancia().getVacunas().remove(auxVacuna);
                        auxVacuna = null;
                        btnEliminar.setEnabled(false);
                        btnModificar.setEnabled(false);
                        filtrarTabla(txtBuscar.getText());
                        JOptionPane.showMessageDialog(null,
                                "Vacuna eliminada exitosamente.",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        buttonPane.add(btnEliminar);

        btnModificar = new JButton("Modificar");
        btnModificar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnModificar.setForeground(new Color(70, 130, 180));
        btnModificar.setBackground(new Color(255, 245, 238));
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (auxVacuna != null) {
                    RegistrarVacuna modVacuna = new RegistrarVacuna(auxVacuna);
                    modVacuna.setModal(true);
                    modVacuna.setVisible(true);
                    filtrarTabla(txtBuscar.getText());
                }
            }
        });
        buttonPane.add(btnModificar);

        btnAplicar = new JButton("Aplicar Vacunas");
        btnAplicar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnAplicar.setForeground(new Color(70, 130, 180));
        btnAplicar.setBackground(new Color(255, 245, 238));
        btnAplicar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aplicarVacunasSeleccionadas();
            }
        });
        buttonPane.add(btnAplicar);

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

        configurarVistaSegunModo();
        filtrarTabla("");
    }

    private void configurarVistaSegunModo() {
        if (esModoAplicar) {
            setTitle("Aplicar Vacunas a Paciente: " + (auxPaciente != null ? auxPaciente.getNombre() : ""));
            String[] headers = {"Aplicar", "Código", "Nombre", "Enfermedades", "Fabricante"};
            model.setColumnIdentifiers(headers);

            btnAplicar.setVisible(true);
            btnModificar.setVisible(false);
            btnEliminar.setVisible(false);
        } else {
            setTitle("Listado de Vacunas");
            String[] headers = {"Código", "Nombre", "Enfermedades", "Fabricante"};
            model.setColumnIdentifiers(headers);

            btnAplicar.setVisible(false);
            btnModificar.setVisible(true);
            btnEliminar.setVisible(true);
        }
    }

    private String formatEnfermedades(Vacuna v) {
        if (v == null || v.getEnfermedades() == null || v.getEnfermedades().isEmpty()) {
            return "N/A";
        }
        StringBuilder sb = new StringBuilder();
        for (Enfermedad e : v.getEnfermedades()) {
            if (e != null && e.getNombre() != null) {
                sb.append(e.getNombre()).append(", ");
            }
        }
        return sb.toString().replaceAll(", $", "");
    }

    private void aplicarVacunasSeleccionadas() {
        if (auxPaciente == null) {
            JOptionPane.showMessageDialog(this, "No hay paciente seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int aplicadas = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            Boolean seleccionado = (Boolean) table.getValueAt(i, 0);
            if (seleccionado != null && seleccionado) {
                String codigo = table.getValueAt(i, 1).toString();
                Vacuna v = Clinica.getInstancia().buscarVacunaXId(codigo);
                if (v != null) {
                    if (auxPaciente.getVacunas() == null || !auxPaciente.getVacunas().contains(v)) {
                        auxPaciente.agregarVacuna(v);
                        aplicadas++;
                    }
                }
            }
        }

        if (aplicadas > 0) {
            JOptionPane.showMessageDialog(this,
                    "Se aplicaron exitosamente " + aplicadas + " vacuna(s) al paciente " + auxPaciente.getNombre(),
                    "Vacunas Aplicadas",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se seleccionaron nuevas vacunas para aplicar.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void filtrarTabla(String filtro) {
        model.setRowCount(0);
        String f = filtro != null ? filtro.toLowerCase().trim() : "";

        ArrayList<Vacuna> vacunas = Clinica.getInstancia().getVacunas();
        if (vacunas != null) {
            for (Vacuna v : vacunas) {
                if (v != null) {
                    String id = v.getId() != null ? v.getId().toLowerCase() : "";
                    String nombre = v.getNombre() != null ? v.getNombre().toLowerCase() : "";
                    String fabricante = v.getFabricante() != null ? v.getFabricante().toLowerCase() : "";
                    String enfermedades = formatEnfermedades(v).toLowerCase();

                    if (f.isEmpty() || id.contains(f) || nombre.contains(f) || fabricante.contains(f) || enfermedades.contains(f)) {
                        if (esModoAplicar) {
                            boolean yaAplicada = (auxPaciente != null && auxPaciente.getVacunas() != null && auxPaciente.getVacunas().contains(v));
                            row = new Object[5];
                            row[0] = Boolean.valueOf(yaAplicada);
                            row[1] = v.getId();
                            row[2] = v.getNombre();
                            row[3] = formatEnfermedades(v);
                            row[4] = v.getFabricante() != null ? v.getFabricante() : "N/A";
                        } else {
                            row = new Object[4];
                            row[0] = v.getId();
                            row[1] = v.getNombre();
                            row[2] = formatEnfermedades(v);
                            row[3] = v.getFabricante() != null ? v.getFabricante() : "N/A";
                        }
                        model.addRow(row);
                    }
                }
            }
        }
    }
}