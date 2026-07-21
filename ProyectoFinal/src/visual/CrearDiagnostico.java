package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.LineBorder;

import logico.Clinica;
import logico.Diagnostico;
import logico.Enfermedad;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;

public class CrearDiagnostico extends JDialog {
    private final JPanel contentPanel = new JPanel();
    private JTextField txtCodigoDiagnostico;
    private JTextArea txtDescripcion;
    private JComboBox<String> cbxEnfermedad;
    private JTextArea txtDetallesEnfermedad;
    private Diagnostico diagnosticoCreado;
    
    public static void main(String[] args) {
        try {
            CrearDiagnostico dialog = new CrearDiagnostico();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public CrearDiagnostico() {
        setTitle("Crear Diagnóstico");
        setModal(true);
        setBounds(100, 100, 600, 500);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(176, 224, 230));
        panel.setBorder(new TitledBorder(new LineBorder(new Color(135, 206, 235), 2), "Información del Diagnóstico", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(70, 130, 180)));
        panel.setBounds(10, 11, 564, 400);
        contentPanel.add(panel);
        panel.setLayout(null);
        
        JLabel lblCodigo = new JLabel("Código Diagnóstico:");
        lblCodigo.setForeground(new Color(70, 130, 180));
        lblCodigo.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblCodigo.setBounds(10, 30, 150, 14);
        panel.add(lblCodigo);
        
        txtCodigoDiagnostico = new JTextField();
        txtCodigoDiagnostico.setEditable(false);
        txtCodigoDiagnostico.setBackground(Color.WHITE);
        txtCodigoDiagnostico.setBounds(170, 27, 374, 20);
        panel.add(txtCodigoDiagnostico);
        txtCodigoDiagnostico.setColumns(10);
        
        txtCodigoDiagnostico.setText("DIAG-"+Clinica.genCodigoDiagnosticos);
        
        JLabel lblEnfermedad = new JLabel("Enfermedad:");
        lblEnfermedad.setForeground(new Color(70, 130, 180));
        lblEnfermedad.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblEnfermedad.setBounds(10, 70, 150, 14);
        panel.add(lblEnfermedad);
        
        cbxEnfermedad = new JComboBox<String>();
        cbxEnfermedad.setBackground(Color.WHITE);
        cbxEnfermedad.setModel(new DefaultComboBoxModel<String>(new String[] {"<<Seleccione>>"}));
        cbxEnfermedad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarDetallesEnfermedad();
            }
        });
        cbxEnfermedad.setBounds(170, 67, 374, 20);
        panel.add(cbxEnfermedad);
        
        JLabel lblDetalles = new JLabel("Detalles Enfermedad:");
        lblDetalles.setForeground(new Color(70, 130, 180));
        lblDetalles.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblDetalles.setBounds(10, 105, 150, 14);
        panel.add(lblDetalles);
        
        JScrollPane scrollDetalles = new JScrollPane();
        scrollDetalles.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        scrollDetalles.setBounds(170, 105, 374, 70);
        panel.add(scrollDetalles);
        
        txtDetallesEnfermedad = new JTextArea();
        txtDetallesEnfermedad.setEditable(false);
        txtDetallesEnfermedad.setLineWrap(true);
        txtDetallesEnfermedad.setWrapStyleWord(true);
        txtDetallesEnfermedad.setBackground(new Color(224, 247, 250));
        txtDetallesEnfermedad.setForeground(new Color(70, 130, 180));
        txtDetallesEnfermedad.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        scrollDetalles.setViewportView(txtDetallesEnfermedad);
        
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setForeground(new Color(70, 130, 180));
        lblDescripcion.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblDescripcion.setBounds(10, 195, 150, 14);
        panel.add(lblDescripcion);
        
        JScrollPane scrollDescripcion = new JScrollPane();
        scrollDescripcion.setBorder(new LineBorder(new Color(173, 216, 230), 1));
        scrollDescripcion.setBounds(170, 195, 374, 185);
        panel.add(scrollDescripcion);
        
        txtDescripcion = new JTextArea();
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBackground(Color.WHITE);
        txtDescripcion.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        scrollDescripcion.setViewportView(txtDescripcion);
        
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setBackground(new Color(240, 248, 255));
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("Crear");
                okButton.setBackground(new Color(176, 224, 230));
                okButton.setForeground(new Color(70, 130, 180));
                okButton.setFont(new Font("Bahnschrift", Font.BOLD, 13));
                okButton.setBorder(new LineBorder(new Color(135, 206, 235), 2));
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        crearDiagnostico();
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancelar");
                cancelButton.setBackground(new Color(176, 224, 230));
                cancelButton.setForeground(new Color(70, 130, 180));
                cancelButton.setFont(new Font("Bahnschrift", Font.BOLD, 13));
                cancelButton.setBorder(new LineBorder(new Color(135, 206, 235), 2));
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
        
        cargarEnfermedades();
    }
    
    private void cargarEnfermedades() {
        cbxEnfermedad.removeAllItems();
        cbxEnfermedad.addItem("<<Seleccione>>");
        
        for(Enfermedad enfermedad : Clinica.getInstancia().getEnfermedades()) {
            cbxEnfermedad.addItem(enfermedad.getId() + " - " + enfermedad.getNombre());
        }
    }
    
    private void mostrarDetallesEnfermedad() {
        if(cbxEnfermedad.getSelectedIndex() > 0) {
            String seleccion = cbxEnfermedad.getSelectedItem().toString();
            String idEnfermedad = seleccion.split(" - ")[0];
            
            Enfermedad enfermedad = Clinica.getInstancia().buscarEnfermedadXId(idEnfermedad);
            
            if(enfermedad != null) {
                StringBuilder detalles = new StringBuilder();
                detalles.append("Nombre: ").append(enfermedad.getNombre()).append("\n");
                detalles.append("En Vigilancia: ").append(enfermedad.isVigilancia() ? "Sí" : "No").append("\n");
                detalles.append("Síntomas: ").append(enfermedad.getSintomas()).append("\n");
                
                txtDetallesEnfermedad.setText(detalles.toString());
            }
        } else {
            txtDetallesEnfermedad.setText("");
        }
    }
    
    private void crearDiagnostico() {
        if(txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una descripción.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if(cbxEnfermedad.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una enfermedad.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = "DIAG-" + Clinica.genCodigoDiagnosticos;
        
        diagnosticoCreado = new Diagnostico(id, txtDescripcion.getText().trim(), new Date());
        diagnosticoCreado.setCodigoDiagnostico(txtCodigoDiagnostico.getText().trim());
        
        String seleccion = cbxEnfermedad.getSelectedItem().toString();
        String idEnfermedad = seleccion.split(" - ")[0];
        Enfermedad enfermedadSeleccionada = Clinica.getInstancia().buscarEnfermedadXId(idEnfermedad);
        
        if(enfermedadSeleccionada != null) {
            diagnosticoCreado.setEnfermedadDiagnosticada(enfermedadSeleccionada);
            Clinica.getInstancia().reportarCasoEnfermedad(idEnfermedad);
        }
        
        Clinica.genCodigoDiagnosticos++;
        
        JOptionPane.showMessageDialog(null, 
            "Diagnóstico creado exitosamente.\nEnfermedad: " + enfermedadSeleccionada.getNombre(), 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
    }
    
    public Diagnostico getDiagnosticoCreado() {
        return diagnosticoCreado;
    }
}