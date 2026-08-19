package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Sintoma;

import java.sql.CallableStatement;
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

    public void guardarSintoma(Sintoma sintoma) {
        final String sql = "{call str_insert_sintoma(?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, sintoma.getNombre());

            if (sintoma.getDescripcion() != null && !sintoma.getDescripcion().isEmpty()) {
                callableStatement.setString(2, sintoma.getDescripcion());
            } else {
                callableStatement.setNull(2, java.sql.Types.VARCHAR);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar el síntoma: " + e.getMessage());
        }
    }

    public void actualizarSintoma(Sintoma sintoma) {
        final String sql = "{call str_update_sintoma(?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, sintoma.getIdNumber());
            callableStatement.setString(2, sintoma.getNombre());

            if (sintoma.getDescripcion() != null && !sintoma.getDescripcion().isEmpty()) {
                callableStatement.setString(3, sintoma.getDescripcion());
            } else {
                callableStatement.setNull(3, java.sql.Types.VARCHAR);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el síntoma: " + e.getMessage());
        }
    }

    public ArrayList<Sintoma> obtenerSintomas() {
        ArrayList<Sintoma> sintomas = new ArrayList<>();
        final String sql = "SELECT * FROM sintoma";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                sintomas.add(new Sintoma(
                        rs.getInt("id_sintoma"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los síntomas: " + e.getMessage());
        }

        return sintomas;
    }
}