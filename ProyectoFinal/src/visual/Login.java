package visual;

import java.awt.EventQueue;

import logico.Clinica;
import logico.catalogo.Doctor;
import logico.catalogo.Usuario;
import visual.consultorio.MainConsultorio;

public class Login {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // 1. Conectar y cargar los datos desde SQL Server
                    Clinica.getInstancia().cargarBD();

                    // 2. Bypass temporal: Seleccionar el primer usuario disponible por defecto
                    if (Clinica.getInstancia().getUsuarios() != null && !Clinica.getInstancia().getUsuarios().isEmpty()) {
                        Usuario userDefault = Clinica.getInstancia().getUsuarios().get(0);
                        Clinica.getInstancia().setUsuarioActual(userDefault);

                        if (userDefault.getRol().equalsIgnoreCase("Doctor")) {
                            Doctor doctorEncontrado = Clinica.getInstancia().buscarDoctorXUsuario(userDefault);
                            Clinica.loginDoctor = doctorEncontrado;
                        } else {
                            Clinica.loginDoctor = null;
                        }
                    }

                    // 3. Abrir directamente la ventana principal del consultorio
                    MainConsultorio menuPrincipal = new MainConsultorio();
                    menuPrincipal.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}