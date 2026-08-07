package visual;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

import logico.consultorio.Clinica;
import logico.Doctor;
import logico.catalogo.Usuario;
import visual.enfermeria.MainEnfermeria;

public class Login extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtNombre;
    private JPasswordField txtPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
        // 1. Carga sincrónica de datos antes de renderizar la interfaz
        cargarDatosClinica();

        setTitle("Login - Sistema Clinica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 450);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 248, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(new LineBorder(new Color(135, 206, 235), 3));
        panelPrincipal.setBounds(50, 30, 434, 350);
        contentPane.add(panelPrincipal);
        panelPrincipal.setLayout(null);

        JLabel lblTitulo = new JLabel("CLINICA");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(70, 130, 180));
        lblTitulo.setBounds(0, 30, 434, 40);
        panelPrincipal.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestion");
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Verdana", Font.PLAIN, 16));
        lblSubtitulo.setForeground(new Color(100, 149, 237));
        lblSubtitulo.setBounds(0, 75, 434, 25);
        panelPrincipal.add(lblSubtitulo);

        JPanel panelDivisor = new JPanel();
        panelDivisor.setBackground(new Color(176, 224, 230));
        panelDivisor.setBounds(50, 120, 334, 3);
        panelPrincipal.add(panelDivisor);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Verdana", Font.BOLD, 16));
        lblUsuario.setForeground(new Color(70, 130, 180));
        lblUsuario.setBounds(70, 150, 80, 25);
        panelPrincipal.add(lblUsuario);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Verdana", Font.PLAIN, 14));
        txtNombre.setBorder(new LineBorder(new Color(173, 216, 230), 2));
        txtNombre.setBackground(new Color(224, 247, 250));
        txtNombre.setBounds(70, 180, 294, 30);
        panelPrincipal.add(txtNombre);
        txtNombre.setColumns(10);

        JLabel lblContrasena = new JLabel("Contrasenia:");
        lblContrasena.setFont(new Font("Verdana", Font.BOLD, 16));
        lblContrasena.setForeground(new Color(70, 130, 180));
        lblContrasena.setBounds(70, 220, 120, 25);
        panelPrincipal.add(lblContrasena);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Verdana", Font.PLAIN, 14));
        txtPassword.setBorder(new LineBorder(new Color(173, 216, 230), 2));
        txtPassword.setBackground(new Color(224, 247, 250));
        txtPassword.setBounds(70, 250, 294, 30);
        panelPrincipal.add(txtPassword);

        JButton btnLogin = new JButton("Iniciar Sesion");
        btnLogin.setFont(new Font("Verdana", Font.BOLD, 16));
        btnLogin.setBackground(new Color(176, 224, 230));
        btnLogin.setForeground(new Color(70, 130, 180));
        btnLogin.setBorder(new LineBorder(new Color(135, 206, 235), 2));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Usuario user = verificar();

                if(user != null) {
                    Clinica.getInstancia().setUsuarioActual(user);

                    //Main menu = new Main();
                    MainEnfermeria menu = new MainEnfermeria();
                    menu.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Usuario o contrasenia incorrectos.", 
                        "Advertencia", 
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        btnLogin.setBounds(120, 300, 194, 35);
        panelPrincipal.add(btnLogin);

        // Permitir iniciar sesion presionando la tecla Enter
        getRootPane().setDefaultButton(btnLogin);
    }

    private void cargarDatosClinica() {
        try (FileInputStream readClinica = new FileInputStream("clinica.dat");
             ObjectInputStream readClass = new ObjectInputStream(readClinica)) {

            Clinica deserializada = (Clinica) readClass.readObject();
            Clinica.getInstancia().setClinica(deserializada);
            Clinica.getInstancia().asignarContadores();

        } catch (Exception e) {
            // Si el archivo no existe o esta corrupto, inicializar datos por defecto y guardar
            Clinica.getInstancia().initInfo();
            guardarDatosClinica();
        }

        // Garantizar que la lista de usuarios no este vacia incluso si existia clinica.dat preexistente
        if (Clinica.getInstancia().getUsuarios() == null || Clinica.getInstancia().getUsuarios().isEmpty()) {
            Clinica.getInstancia().initInfo();
            guardarDatosClinica();
        }
    }

    private void guardarDatosClinica() {
        try (FileOutputStream writeClinica = new FileOutputStream("clinica.dat");
             ObjectOutputStream writeClass = new ObjectOutputStream(writeClinica)) {

            writeClass.writeObject(Clinica.getInstancia());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Usuario verificar() {
        String nombreInput = txtNombre.getText().trim();
        String passInput = new String(txtPassword.getPassword()).trim();

        if (nombreInput.isEmpty() || passInput.isEmpty()) {
            return null;
        }

        Usuario aux = null;

        if (Clinica.getInstancia().getUsuarios() != null) {
            for (Usuario user : Clinica.getInstancia().getUsuarios()) {
                if (user.match(nombreInput, passInput)) {
                    aux = user;
                    break;
                }
            }
        }

        if (aux != null && aux.getTipo().equalsIgnoreCase("Doctor")) {
            Doctor doctorEncontrado = Clinica.getInstancia().buscarDoctorXUsuario(aux);
            Clinica.loginDoctor = doctorEncontrado;
        } else if (aux != null) {
            Clinica.loginDoctor = null;
        }

        return aux;
    }
}