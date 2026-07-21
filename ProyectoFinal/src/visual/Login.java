package visual;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.Clinica;
import logico.Clinica;
import logico.Usuario;
import logico.Doctor;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

public class Login extends JFrame {
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
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                FileInputStream readClinica;
                FileOutputStream writeClinica;
                ObjectInputStream readClass;
                ObjectOutputStream writeClass;
                
                try {
                    readClinica = new FileInputStream("clinica.dat");
                    readClass = new ObjectInputStream(readClinica);
                    Clinica.getInstancia().setClinica((Clinica)readClass.readObject());
                    Clinica.getInstancia().asignarContadores();
                    readClass.close();
                    readClinica.close();
                } catch (Exception e) {
                    try {
                        writeClinica = new FileOutputStream("clinica.dat");
                        writeClass = new ObjectOutputStream(writeClinica);
                        Clinica.getInstancia().initInfo();
                        writeClass.writeObject(Clinica.getInstancia());
                        writeClass.close();
                        writeClinica.close();
                    } catch (FileNotFoundException e2) {
                        e2.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        
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
                    
                    Main menu = new Main();
                    menu.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Usuario o contrasenia"
                        + " incorrectos.", 
                        "Advertencia", 
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        btnLogin.setBounds(120, 300, 194, 35);
        panelPrincipal.add(btnLogin);
    }
    
    private Usuario verificar() {
        Usuario aux = null;

        for(Usuario user : Clinica.getInstancia().getUsuarios()) {
            if(user.match(txtNombre.getText(), new String(txtPassword.getPassword()))) {
                aux = user;
            }
        }
        
        if(aux != null && aux.getTipo().equalsIgnoreCase("Doctor")) {
            Doctor doctorEncontrado = Clinica.getInstancia().buscarDoctorXUsuario(aux);
            Clinica.loginDoctor = doctorEncontrado;
        } else if(aux != null) {
            Clinica.loginDoctor = null;
        }
        
        return aux;
    }
}