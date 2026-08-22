package bd.catalogo;

import bd.ConexionBD;
import logico.consultorio.Tratamiento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TratamientoDAO {

    private static TratamientoDAO instance = null;

    private TratamientoDAO() {}

    public static TratamientoDAO getInstance() {
        if (instance == null) {
            instance = new TratamientoDAO();
        }
        return instance;
    }

    public void guardarTratamiento(Tratamiento tratamiento) {
        final String sql = "{call str_insert_tratamiento(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            if (tratamiento.getDescripcion() != null && !tratamiento.getDescripcion().isEmpty()) {
                callableStatement.setString(1, tratamiento.getDescripcion());
            } else {
                callableStatement.setNull(1, java.sql.Types.VARCHAR);
            }

            // Según el constraint chk_tratamiento_dosis check (dosis > 0)
            if (tratamiento.getDosis() > 0) {
                callableStatement.setInt(2, tratamiento.getDosis());
            } else {
                callableStatement.setNull(2, java.sql.Types.INTEGER);
            }

            callableStatement.setString(3, tratamiento.getEstado());
            callableStatement.setDate(4, new java.sql.Date(tratamiento.getFechaInicio().getTime()));
            callableStatement.setDate(5, new java.sql.Date(tratamiento.getFechaFin().getTime()));

            if (tratamiento.getFrecuencia() != null && !tratamiento.getFrecuencia().isEmpty()) {
                callableStatement.setString(6, tratamiento.getFrecuencia());
            } else {
                callableStatement.setNull(6, java.sql.Types.VARCHAR);
            }

            // id_diagnostico es NOT NULL en la base de datos
            callableStatement.setInt(7, tratamiento.getDiagnostico().getIdNumber());

            // id_medicamento puede ser NULL
            if (tratamiento.getMedicamento() != null) {
                callableStatement.setInt(8, tratamiento.getMedicamento().getIdNumber());
            } else {
                callableStatement.setNull(8, java.sql.Types.INTEGER);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar el tratamiento: " + e.getMessage());
        }
    }

    public void actualizarTratamiento(Tratamiento tratamiento) {
        final String sql = "{call str_update_tratamiento(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, tratamiento.getIdNumber());

            if (tratamiento.getDescripcion() != null && !tratamiento.getDescripcion().isEmpty()) {
                callableStatement.setString(2, tratamiento.getDescripcion());
            } else {
                callableStatement.setNull(2, java.sql.Types.VARCHAR);
            }

            if (tratamiento.getDosis() > 0) {
                callableStatement.setInt(3, tratamiento.getDosis());
            } else {
                callableStatement.setNull(3, java.sql.Types.INTEGER);
            }

            callableStatement.setString(4, tratamiento.getEstado());
            callableStatement.setDate(5, new java.sql.Date(tratamiento.getFechaInicio().getTime()));
            callableStatement.setDate(6, new java.sql.Date(tratamiento.getFechaFin().getTime()));

            if (tratamiento.getFrecuencia() != null && !tratamiento.getFrecuencia().isEmpty()) {
                callableStatement.setString(7, tratamiento.getFrecuencia());
            } else {
                callableStatement.setNull(7, java.sql.Types.VARCHAR);
            }

            callableStatement.setInt(8, tratamiento.getDiagnostico().getIdNumber());

            if (tratamiento.getMedicamento() != null) {
                callableStatement.setInt(9, tratamiento.getMedicamento().getIdNumber());
            } else {
                callableStatement.setNull(9, java.sql.Types.INTEGER);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el tratamiento: " + e.getMessage());
        }
    }

    // --- NUEVO MÉTODO DE ELIMINACIÓN ---
    public void eliminarTratamiento(int idTratamiento) {
        final String sql = "{call str_delete_tratamiento(?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, idTratamiento);
            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al eliminar el tratamiento: " + e.getMessage());
        }
    }

    public ArrayList<Tratamiento> obtenerTratamientos() {
        ArrayList<Tratamiento> tratamientos = new ArrayList<>();
        final String sql = "SELECT * FROM tratamiento";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                tratamientos.add(new Tratamiento(
                        rs.getInt("id_tratamiento"),
                        null, // Reemplazar con el método para buscar/cargar el Diagnostico real
                        null, // Reemplazar con el método para buscar/cargar el Medicamento real
                        rs.getInt("dosis"),
                        rs.getString("frecuencia"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"),
                        rs.getString("descripcion"),
                        rs.getString("estado")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los tratamientos: " + e.getMessage());
        }

        return tratamientos;
    }
}