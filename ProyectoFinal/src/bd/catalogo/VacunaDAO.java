package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Analisis;
import logico.catalogo.Vacuna;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VacunaDAO {

    private static VacunaDAO instance = null;

    private VacunaDAO() {}

    public static VacunaDAO getInstance() {
        if (instance == null) {
            instance = new VacunaDAO();
        }
        return instance;
    }

    public ArrayList<Vacuna> obtenerVacunas(){
        ArrayList<Vacuna> vacunas = new ArrayList<>();
        final String sql = "SELECT * FROM vacuna";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)){

            while(rs.next()){
                vacunas.add(new Vacuna(
                        rs.getInt("id_vacuna"),
                        rs.getString("nombre"),
                        rs.getString("fabricante"),
                        rs.getInt("cant_dosis")
                ));
            }

        }catch(SQLException e) {
            System.err.println("Error al guardar el vacuna: " + e.getMessage());
        }

        return vacunas;
    }
}
