package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Enfermedad;
import logico.catalogo.Sintoma;
import logico.Clinica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EnfermedadDAO {

    private static EnfermedadDAO instance = null;

    private EnfermedadDAO() {}

    public static EnfermedadDAO getInstance() {
        if (instance == null) {
            instance = new EnfermedadDAO();
        }
        return instance;
    }

    public ArrayList<Enfermedad> obtenerEnfermedades(){
        ArrayList<Enfermedad> enfermedades = new ArrayList<>();
        final String sql = "SELECT * FROM enfermedad";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)){

            while(rs.next()){
                enfermedades.add(new Enfermedad(
                        rs.getInt("id_enfermedad"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBoolean("es_contagiosa"),
                        null,
                        //rs.getInt("id_especialidad"),
                        obtenerSintomas(rs.getInt("id_enfermedad"))
                ));
            }

        }catch(SQLException e) {
            System.err.println("Error al obtener las enfermedades: " + e.getMessage());
        }

        return enfermedades;
    }

    public ArrayList<Sintoma> obtenerSintomas(int idEnfermedad){
        ArrayList<Sintoma> sintomas = new ArrayList<>();

        final String sql = "select e_s.id_sintoma " +
                "from enfermedad_sintoma e_s " +
                "inner join enfermedad e on e.id_enfermedad = e_s.id_enfermedad "+
                "where e_s.id_enfermedad = " + idEnfermedad;

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)){

            while(rs.next()){
                int idSintomaBD = rs.getInt("id_sintoma");

                Sintoma sintomaEncontrado = Clinica.getInstancia().buscarSintomaXId(
                        Clinica.getInstancia().genId(idSintomaBD, Sintoma.class)
                );

                if (sintomaEncontrado != null) {
                    sintomas.add(sintomaEncontrado);
                }
            }

        }catch(SQLException e) {
            System.err.println("Error al obtener los síntomas de la enfermedad: " + e.getMessage());
        }

        return sintomas;
    }
}