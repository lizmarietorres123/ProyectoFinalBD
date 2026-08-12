package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Analisis;
import logico.catalogo.Sintoma;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SintomaDAO {

    private static SintomaDAO instance = null;

    private SintomaDAO() {}

    public static SintomaDAO getInstance() {
        if (instance == null) {
            instance = new SintomaDAO();
        }
        return instance;
    }

    public ArrayList<Sintoma> obtenerSintomas(){
        ArrayList<Sintoma> sintomas = new ArrayList<>();
        final String sql = "SELECT * FROM sintoma";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)){

            while(rs.next()){
                sintomas.add(new Sintoma(
                        rs.getInt("id_sintoma"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }

        }catch(SQLException e) {
            System.err.println("Error al guardar el analisis: " + e.getMessage());
        }

        return sintomas;
    }
}
