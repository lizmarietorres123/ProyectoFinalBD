package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Vacuna;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VacunaDAO {


    private static final VacunaDAO instance = new VacunaDAO();

    private VacunaDAO() {}

    public static VacunaDAO getInstance() {
        return instance;
    }

    public void guardarVacuna(Vacuna vacuna) {
        final String sql = "{call str_insert_vacuna(?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, vacuna.getNombre());
            callableStatement.setString(2, vacuna.getFabricante());
            callableStatement.setInt(3, vacuna.getCantDosis());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar la vacuna: " + e.getMessage());
        }
    }

    public void actualizarVacuna(Vacuna vacuna) {
        final String sql = "{call str_update_vacuna(?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, vacuna.getIdNumber());
            callableStatement.setString(2, vacuna.getNombre());
            callableStatement.setString(3, vacuna.getFabricante());
            callableStatement.setInt(4, vacuna.getCantDosis());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar la vacuna: " + e.getMessage());
        }
    }

    // --- NUEVO MÉTODO DE ELIMINACIÓN ---
    public void eliminarVacuna(int idVacuna) {
        final String sql = "{call str_delete_vacuna(?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, idVacuna);
            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al eliminar la vacuna: " + e.getMessage());
        }
    }

    public ArrayList<Vacuna> obtenerVacunas() {
        ArrayList<Vacuna> vacunas = new ArrayList<>();
        final String sql = "SELECT * FROM vacuna";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                vacunas.add(new Vacuna(
                        rs.getInt("id_vacuna"),
                        rs.getString("nombre"),
                        rs.getString("fabricante"),
                        rs.getInt("cant_dosis")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener las vacunas: " + e.getMessage());
        }

        return vacunas;
    }
}