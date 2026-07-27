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

import controlador.UsuarioController;
import logico.Usuario;
import visual.registro.RegistrarUsuario;

public class ListarUsuario extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private DefaultTableModel model;
    private Object[] row;
    private Usuario auxUsuario = null;
    private UsuarioController usuarioController;

    private JTextField txtBuscar;
    private JTable table;
    private JPanel panelBarra;
    private JPanel panelTable;

    private JButton btnVerDetalles;
    private JButton btnEliminar;
    private JButton btnCancelar;

    public ListarUsuario() {
        this.usuarioController = new UsuarioController();
        setTitle("Listado de Usuarios");
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
        txtBuscar.setToolTipText("Filtrar por nombre de usuario o rol");
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

        String[] headers = {"Nombre de Usuario", "Tipo / Rol"};
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

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index > -1) {
                    String username = table.getValueAt(index, 0).toString();
                    auxUsuario = usuarioController.buscarPorUsername(username);
                    if (auxUsuario != null) {
                        btnVerDetalles.setEnabled(true);
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
                if (auxUsuario != null) {
                    int option = JOptionPane.showConfirmDialog(
                            null,
                            "¿Está seguro de eliminar al usuario: " + auxUsuario.getNombre() + "?",
                            "Confirmación",
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (option == JOptionPane.OK_OPTION) {
                        usuarioController.eliminar(auxUsuario);
                        auxUsuario = null;
                        btnEliminar.setEnabled(false);
                        btnVerDetalles.setEnabled(false);
                        filtrarTabla(txtBuscar.getText());
                    }
                }
            }
        });
        buttonPane.add(btnEliminar);

        // Botón Ver Detalles
        btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnVerDetalles.setForeground(new Color(70, 130, 180));
        btnVerDetalles.setBackground(new Color(255, 245, 238));
        btnVerDetalles.setEnabled(false);
        btnVerDetalles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (auxUsuario != null) {
                    RegistrarUsuario verUsuario = new RegistrarUsuario(auxUsuario, true);
                    verUsuario.setVisible(true);
                    filtrarTabla(txtBuscar.getText());
                }
            }
        });
        buttonPane.add(btnVerDetalles);

        btnCancelar = new JButton("Volver");
        btnCancelar.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        btnCancelar.setForeground(new Color(70, 130, 180));
        btnCancelar.setBackground(new Color(255, 245, 238));
        btnCancelar.addActionListener(e -> dispose());
        buttonPane.add(btnCancelar);

        filtrarTabla("");
    }

    private void filtrarTabla(String filtro) {
        model.setRowCount(0);
        String f = filtro != null ? filtro.toLowerCase().trim() : "";

        ArrayList<Usuario> usuarios = usuarioController.obtenerTodos();
        if (usuarios != null) {
            for (Usuario u : usuarios) {
                if (u != null) {
                    String username = u.getNombre() != null ? u.getNombre().toLowerCase() : "";
                    String tipo = u.getTipo() != null ? u.getTipo().toLowerCase() : "";

                    if (f.isEmpty() || username.contains(f) || tipo.contains(f)) {
                        row = new Object[2];
                        row[0] = u.getNombre();
                        row[1] = u.getTipo();
                        model.addRow(row);
                    }
                }
            }
        }
    }
}