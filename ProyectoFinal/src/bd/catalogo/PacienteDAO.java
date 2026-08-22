package bd.catalogo;

import bd.ConexionBD;
import logico.consultorio.Paciente;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PacienteDAO {

    private static PacienteDAO instance = null;

    private PacienteDAO() {}

    public static PacienteDAO getInstance() {
        if (instance == null) {
            instance = new PacienteDAO();
        }
        return instance;
    }

    public void guardarPaciente(Paciente paciente) {
        final String sql = "{call str_insert_paciente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, paciente.getCedula());
            callableStatement.setString(2, paciente.getNombre());
            callableStatement.setString(3, paciente.getApellido());
            callableStatement.setDate(4, new java.sql.Date(paciente.getFecNacim().getTime()));
            callableStatement.setString(5, paciente.getSexo());
            callableStatement.setString(6, paciente.getTelefono());

            if (paciente.getDireccion() != null && !paciente.getDireccion().isEmpty()) {
                callableStatement.setString(7, paciente.getDireccion());
            } else {
                callableStatement.setNull(7, java.sql.Types.VARCHAR);
            }

            callableStatement.setFloat(8, paciente.getPeso());
            callableStatement.setFloat(9, paciente.getEstatura());

            if (paciente.getTipoSangre() != null && !paciente.getTipoSangre().isEmpty()) {
                callableStatement.setString(10, paciente.getTipoSangre());
            } else {
                callableStatement.setNull(10, java.sql.Types.VARCHAR);
            }

            if (paciente.getEstado() != null && !paciente.getEstado().isEmpty()) {
                callableStatement.setString(11, paciente.getEstado());
            } else {
                callableStatement.setNull(11, java.sql.Types.VARCHAR);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar el paciente: " + e.getMessage());
        }
    }

    public void actualizarPaciente(Paciente paciente) {
        final String sql = "{call str_update_paciente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, paciente.getIdNumber());
            callableStatement.setString(2, paciente.getCedula());
            callableStatement.setString(3, paciente.getNombre());
            callableStatement.setString(4, paciente.getApellido());
            callableStatement.setDate(5, new java.sql.Date(paciente.getFecNacim().getTime()));
            callableStatement.setString(6, paciente.getSexo());
            callableStatement.setString(7, paciente.getTelefono());

            if (paciente.getDireccion() != null && !paciente.getDireccion().isEmpty()) {
                callableStatement.setString(8, paciente.getDireccion());
            } else {
                callableStatement.setNull(8, java.sql.Types.VARCHAR);
            }

            callableStatement.setFloat(9, paciente.getPeso());
            callableStatement.setFloat(10, paciente.getEstatura());

            if (paciente.getTipoSangre() != null && !paciente.getTipoSangre().isEmpty()) {
                callableStatement.setString(11, paciente.getTipoSangre());
            } else {
                callableStatement.setNull(11, java.sql.Types.VARCHAR);
            }

            if (paciente.getEstado() != null && !paciente.getEstado().isEmpty()) {
                callableStatement.setString(12, paciente.getEstado());
            } else {
                callableStatement.setNull(12, java.sql.Types.VARCHAR);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el paciente: " + e.getMessage());
        }
    }

    public ArrayList<Paciente> obtenerPacientes() {
        ArrayList<Paciente> pacientes = new ArrayList<>();
        final String sql = "SELECT * FROM paciente";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                pacientes.add(new Paciente(
                        rs.getInt("id_paciente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        rs.getDate("fec_nacim"),
                        rs.getString("sexo"),
                        rs.getFloat("peso"),
                        rs.getFloat("estatura"),
                        rs.getString("tipo_sangre"),
                        rs.getString("direccion"),
                        rs.getString("estado")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los pacientes: " + e.getMessage());
        }

        return pacientes;
    }
}