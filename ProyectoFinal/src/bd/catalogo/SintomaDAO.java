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

    // Inicialización temprana: garantiza que sea thread-safe sin usar bloques 'synchronized'
    private static final SintomaDAO instance = new SintomaDAO();

    private SintomaDAO() {}

    public static SintomaDAO getInstance() {
        return instance;
    }

    // Se propaga la excepción para que la interfaz gráfica (ej. JavaFX) pueda mostrar un mensaje de error
    public void guardarSintoma(Sintoma sintoma) throws SQLException {
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
        }
    }

    public void actualizarSintoma(Sintoma sintoma) throws SQLException {
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
        }
    }

    public ArrayList<Sintoma> obtenerSintomas() throws SQLException {
        ArrayList<Sintoma> sintomas = new ArrayList<>();
        // Definición explícita de las columnas para mayor robustez
        final String sql = "SELECT id_sintoma, nombre, descripcion FROM sintoma";

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
        }

        return sintomas;
    }
}