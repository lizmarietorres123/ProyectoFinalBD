package bd.catalogo;

import bd.ConexionBD;
import logico.consultorio.Diagnostico;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DiagnosticoDAO {

    private static DiagnosticoDAO instance = null;

    private DiagnosticoDAO() {}

    public static DiagnosticoDAO getInstance() {
        if (instance == null) {
            instance = new DiagnosticoDAO();
        }
        return instance;
    }

    public void guardarDiagnostico(Diagnostico diagnostico) {
        final String sql = "{call str_insert_diagnostico(?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, diagnostico.getDescripcion());

            if (diagnostico.getTipo() != null && !diagnostico.getTipo().isEmpty()) {
                callableStatement.setString(2, diagnostico.getTipo());
            } else {
                callableStatement.setNull(2, java.sql.Types.VARCHAR);
            }

            if (diagnostico.getEstado() != null && !diagnostico.getEstado().isEmpty()) {
                callableStatement.setString(3, diagnostico.getEstado());
            } else {
                callableStatement.setNull(3, java.sql.Types.VARCHAR);
            }
            callableStatement.setInt(4, diagnostico.getConsulta().getIdNumber());
            callableStatement.setInt(5, diagnostico.getEnfermedad().getIdNumber());

            boolean exito = callableStatement.execute();
            if (exito) {
                try (ResultSet rs = callableStatement.getResultSet()) {
                    if (rs != null && rs.next()) {
                        diagnostico.setId(rs.getInt(1));

                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar el diagnóstico: " + e.getMessage());
        }
    }

    public void guardarDiagnostico(Connection connection, Diagnostico diagnostico) {
        final String sql = "{call str_insert_diagnostico(?, ?, ?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, diagnostico.getDescripcion());

            if (diagnostico.getTipo() != null && !diagnostico.getTipo().isEmpty()) {
                callableStatement.setString(2, diagnostico.getTipo());
            } else {
                callableStatement.setNull(2, java.sql.Types.VARCHAR);
            }

            if (diagnostico.getEstado() != null && !diagnostico.getEstado().isEmpty()) {
                callableStatement.setString(3, diagnostico.getEstado());
            } else {
                callableStatement.setNull(3, java.sql.Types.VARCHAR);
            }
            callableStatement.setInt(4, diagnostico.getConsulta().getIdNumber());
            callableStatement.setInt(5, diagnostico.getEnfermedad().getIdNumber());

            boolean exito = callableStatement.execute();
            if (exito) {
                try (ResultSet rs = callableStatement.getResultSet()) {
                    if (rs != null && rs.next()) {
                        diagnostico.setId(rs.getInt(1));

                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar el diagnóstico: " + e.getMessage());
        }
    }

    public void actualizarDiagnostico(Diagnostico diagnostico) {
        final String sql = "{call str_update_diagnostico(?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, diagnostico.getIdNumber());
            callableStatement.setString(2, diagnostico.getDescripcion());

            if (diagnostico.getTipo() != null && !diagnostico.getTipo().isEmpty()) {
                callableStatement.setString(3, diagnostico.getTipo());
            } else {
                callableStatement.setNull(3, java.sql.Types.VARCHAR);
            }

            if (diagnostico.getEstado() != null && !diagnostico.getEstado().isEmpty()) {
                callableStatement.setString(4, diagnostico.getEstado());
            } else {
                callableStatement.setNull(4, java.sql.Types.VARCHAR);
            }

            callableStatement.setInt(5, diagnostico.getConsulta().getIdNumber());
            callableStatement.setInt(6, diagnostico.getEnfermedad().getIdNumber());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el diagnóstico: " + e.getMessage());
        }
    }


    public void eliminarDiagnostico(int idDiagnostico) {
        final String sql = "{call str_delete_diagnostico(?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, idDiagnostico);
            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al eliminar el diagnóstico: " + e.getMessage());
        }
    }

    public ArrayList<Diagnostico> obtenerDiagnosticos() {
        ArrayList<Diagnostico> diagnosticos = new ArrayList<>();
        final String sql = "SELECT * FROM diagnostico";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                int idConsulta = rs.getInt("id_consulta");
                int idEnfermedad = rs.getInt("id_enfermedad");
                logico.consultorio.Consulta consulta = (idConsulta > 0) ? logico.Clinica.getInstancia().buscarConsultaXIdNumber(idConsulta) : null;
                logico.catalogo.Enfermedad enfermedad = (idEnfermedad > 0) ? logico.Clinica.getInstancia().buscarEnfermedadXIdNumber(idEnfermedad) : null;
                diagnosticos.add(new Diagnostico(
                        rs.getInt("id_diagnostico"),
                        rs.getString("descripcion"),
                        rs.getString("tipo"),
                        rs.getString("estado"),
                        consulta,
                        enfermedad
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los diagnósticos: " + e.getMessage());
        }

        return diagnosticos;
    }
}