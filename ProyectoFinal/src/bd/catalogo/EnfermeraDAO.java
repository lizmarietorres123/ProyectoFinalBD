package bd.catalogo;

import bd.ConexionBD;
import logico.Clinica;
import logico.catalogo.Enfermera;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EnfermeraDAO {
    private static EnfermeraDAO instance = null;

    private EnfermeraDAO() {}

    public static EnfermeraDAO getInstance() {
        if (instance == null) {
            instance = new EnfermeraDAO();
        }
        return instance;
    }

    public void guardarEnfermera(Enfermera enfermera) {
        // Asumiendo que el SP fija el estado en 'Activo' por defecto.
        final String sql = "{call str_insert_enfermera(?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, enfermera.getNombre());
            callableStatement.setString(2, enfermera.getApellido());
            callableStatement.setString(3, enfermera.getCedula());
            callableStatement.setString(4, enfermera.getTelefono());

            if (enfermera.getUsuario() != null) {
                callableStatement.setInt(5, enfermera.getUsuario().getIdNumber());
            } else {
                callableStatement.setNull(5, java.sql.Types.INTEGER);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar la enfermera: " + e.getMessage());
        }
    }

    public void actualizarEnfermera(Enfermera enfermera) {
        // Se añade el estado como parámetro adicional al SP
        final String sql = "{call str_update_enfermera(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, enfermera.getIdNumber());
            callableStatement.setString(2, enfermera.getNombre());
            callableStatement.setString(3, enfermera.getApellido());
            callableStatement.setString(4, enfermera.getCedula());
            callableStatement.setString(5, enfermera.getTelefono());

            if (enfermera.getUsuario() != null) {
                callableStatement.setInt(6, enfermera.getUsuario().getIdNumber());
            } else {
                callableStatement.setNull(6, java.sql.Types.INTEGER);
            }

            callableStatement.setString(7, enfermera.getEstado());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar la enfermera: " + e.getMessage());
        }
    }

    public void eliminarEnfermera(int idEnfermera) {
        final String sql = "{call str_delete_enfermera(?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, idEnfermera);
            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al eliminar la enfermera: " + e.getMessage());
        }
    }

    public ArrayList<Enfermera> obtenerEnfermeras(){
        ArrayList<Enfermera> enfermeras = new ArrayList<>();
        final String sql = "SELECT * FROM enfermera";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)){

            while(rs.next()){
                int idUsuario = rs.getInt("id_usuario");
                logico.catalogo.Usuario user = (idUsuario > 0) ? Clinica.getInstancia().buscarUsuarioXIdNumber(idUsuario) : null;
                enfermeras.add(new Enfermera(
                        rs.getInt("id_enfermera"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        user,
                        rs.getString("estado")
                ));
            }

        } catch(SQLException e) {
            System.err.println("Error al obtener las enfermeras: " + e.getMessage());
        }

        return enfermeras;
    }
}