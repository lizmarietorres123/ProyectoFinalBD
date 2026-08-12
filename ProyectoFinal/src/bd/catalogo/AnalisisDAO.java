package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Analisis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AnalisisDAO {

    private static AnalisisDAO instance = null;

    private AnalisisDAO() {}

    public static AnalisisDAO getInstance() {
        if (instance == null) {
            instance = new AnalisisDAO();
        }
        return instance;
    }

    public ArrayList<Analisis> obtenerAnalisis(){
        ArrayList<Analisis> analisis = new ArrayList<>();
        final String sql = "SELECT * FROM analisis";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)){

            while(rs.next()){
                analisis.add(new Analisis(
                        rs.getInt("id_analisis"),
                        rs.getString("nombre"),
                        rs.getString("tipo_analisis"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("valor_prom"),
                        rs.getDouble("valor_max"),
                        rs.getDouble("valor_min")
                ));
            }

        }catch(SQLException e) {
            System.err.println("Error al guardar el analisis: " + e.getMessage());
        }

        return analisis;
    }
}
