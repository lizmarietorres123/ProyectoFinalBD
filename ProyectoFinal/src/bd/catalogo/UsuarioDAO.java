package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UsuarioDAO {

    private static UsuarioDAO instance = null;

    private UsuarioDAO() {}

    public static UsuarioDAO getInstance() {
        if (instance == null) {
            instance = new UsuarioDAO();
        }
        return instance;
    }

    public void guardarUsuario(Usuario usuario) {
        // El SP str_insert_user solo recibe 3 parámetros y pone 'Activo' por defecto en SQL
        final String sql = "{call str_insert_user(?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, usuario.getNombre());
            callableStatement.setString(2, usuario.getPassword());
            callableStatement.setString(3, usuario.getRol());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar el usuario: " + e.getMessage());
        }
    }

    public void actualizarUsuario(Usuario usuario) {

        final String sql = "{call str_update_usuario(?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, usuario.getIdNumber());
            callableStatement.setString(2, usuario.getNombre());
            callableStatement.setString(3, usuario.getPassword());
            callableStatement.setString(4, usuario.getRol());
            callableStatement.setString(5, usuario.getEstado()); // Usamos getEstado() como String

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    public void eliminarUsuario(int idUsuario) {

        final String sql = "{call str_delete_usuario(?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, idUsuario);
            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    public ArrayList<Usuario> obtenerUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        final String sql = "SELECT * FROM usuario";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("password"),
                        rs.getString("rol"),
                        rs.getString("estado")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los usuarios: " + e.getMessage());
        }

        return usuarios;
    }
}