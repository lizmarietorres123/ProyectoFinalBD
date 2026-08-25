package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import logico.Clinica;
import logico.catalogo.Doctor;
import logico.catalogo.Enfermera;
import logico.catalogo.Usuario;
import visual.consultorio.MainConsultorio;
import visual.enfermeria.MainEnfermeria;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Color AZUL_TITULO = new Color(70, 130, 180);
    private static final Color AZUL_SUBTITULO = new Color(100, 149, 237);
    private static final Color AZUL_FONDO = new Color(240, 248, 255);
    private static final Color AZUL_CAMPO = new Color(224, 247, 250);
    private static final Color AZUL_BORDE = new Color(173, 216, 230);

    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public Login() {
        setTitle("Sistema de Gestión Clínica - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 360);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(AZUL_FONDO);
        contentPane.setBorder(new EmptyBorder(20, 25, 20, 25));
        setContentPane(contentPane);

        // Header Panel
        JPanel panelHeader = new JPanel();
        panelHeader.setOpaque(false);
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("CLÍNICA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 26));
        lblTitulo.setForeground(AZUL_TITULO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestión Médica", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
        lblSubtitulo.setForeground(AZUL_SUBTITULO);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelHeader.add(lblTitulo);
        panelHeader.add(Box.createVerticalStrut(4));
        panelHeader.add(lblSubtitulo);
        panelHeader.add(Box.createVerticalStrut(15));

        contentPane.add(panelHeader, BorderLayout.NORTH);

        // Form Panel
        JPanel panelForm = new JPanel(null);
        panelForm.setOpaque(false);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblUsuario.setForeground(AZUL_TITULO);
        lblUsuario.setBounds(30, 10, 100, 20);
        panelForm.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
        txtUsuario.setBackground(AZUL_CAMPO);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(AZUL_BORDE, 1),
                new EmptyBorder(4, 8, 4, 8)
        ));
        txtUsuario.setBounds(30, 32, 310, 32);
        panelForm.add(txtUsuario);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblPassword.setForeground(AZUL_TITULO);
        lblPassword.setBounds(30, 75, 100, 20);
        panelForm.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
        txtPassword.setBackground(AZUL_CAMPO);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(AZUL_BORDE, 1),
                new EmptyBorder(4, 8, 4, 8)
        ));
        txtPassword.setBounds(30, 97, 310, 32);
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    autenticar();
                }
            }
        });
        panelForm.add(txtPassword);

        contentPane.add(panelForm, BorderLayout.CENTER);

        // Footer Panel (Buttons)
        JPanel panelFooter = new JPanel();
        panelFooter.setOpaque(false);
        panelFooter.setLayout(new BoxLayout(panelFooter, BoxLayout.Y_AXIS));

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        btnLogin.setBackground(new Color(176, 224, 230));
        btnLogin.setForeground(AZUL_TITULO);
        btnLogin.setBorder(new LineBorder(AZUL_TITULO, 2));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new java.awt.Dimension(310, 38));
        btnLogin.setPreferredSize(new java.awt.Dimension(310, 38));
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticar();
            }
        });

        panelFooter.add(btnLogin);
        panelFooter.add(Box.createVerticalStrut(10));

        contentPane.add(panelFooter, BorderLayout.SOUTH);
    }

    private void autenticar() {
        String username = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese usuario y contraseña.", "Campos Requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioAutenticado = null;
        if (Clinica.getInstancia().getUsuarios() != null) {
            for (Usuario u : Clinica.getInstancia().getUsuarios()) {
                if (u != null && u.getNombre() != null && u.getNombre().equalsIgnoreCase(username) &&
                        u.getPassword() != null && u.getPassword().equals(password)) {
                    usuarioAutenticado = u;
                    break;
                }
            }
        }

        if (usuarioAutenticado == null && (Clinica.getInstancia().getUsuarios() == null || Clinica.getInstancia().getUsuarios().isEmpty())) {
            if (username.equalsIgnoreCase("admin") && password.equals("admin")) {
                usuarioAutenticado = new Usuario(1, "admin", "admin", "Doctor", "activo");
                Clinica.getInstancia().getUsuarios().add(usuarioAutenticado);
            }
        }

        if (usuarioAutenticado == null) {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Clinica.getInstancia().setUsuarioActual(usuarioAutenticado);

        if ("Enfermera".equalsIgnoreCase(usuarioAutenticado.getRol())) {
            Clinica.loginDoctor = null;
            Enfermera enfermeraEncontrada = Clinica.getInstancia().buscarEnfermeraXUsuario(usuarioAutenticado);

            MainEnfermeria mainEnfermeria = new MainEnfermeria(usuarioAutenticado);
            mainEnfermeria.setVisible(true);
        } else {
            Doctor doctorEncontrado = Clinica.getInstancia().buscarDoctorXUsuario(usuarioAutenticado);
            Clinica.loginDoctor = doctorEncontrado;
            MainConsultorio mainConsultorio = new MainConsultorio(usuarioAutenticado);
            mainConsultorio.setVisible(true);
        }

        dispose();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Clinica.getInstancia().cargarBD();
                    Login window = new Login();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}