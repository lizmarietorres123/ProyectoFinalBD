package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Analisis;

import java.sql.CallableStatement;
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

    public void guardarAnalisis(Analisis analisis) {
        final String sql = "{call str_insert_analisis(?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, analisis.getNombre());
            callableStatement.setString(2, analisis.getTipo());
            callableStatement.setString(3, analisis.getUnidadMedida());
            callableStatement.setDouble(4, analisis.getValorProm());
            callableStatement.setDouble(5, analisis.getValorMax());
            callableStatement.setDouble(6, analisis.getValorMin());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar el análisis: " + e.getMessage());
        }
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

        } catch(SQLException e) {
            System.err.println("Error al obtener los análisis: " + e.getMessage());
        }

        return analisis;
    }

    public void actualizarAnalisis(Analisis analisis) {
        final String sql = "{call str_update_analisis(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, analisis.getIdNumber());
            callableStatement.setString(2, analisis.getNombre());
            callableStatement.setString(3, analisis.getTipo());
            callableStatement.setString(4, analisis.getUnidadMedida());
            callableStatement.setDouble(5, analisis.getValorProm());
            callableStatement.setDouble(6, analisis.getValorMax());
            callableStatement.setDouble(7, analisis.getValorMin());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el análisis: " + e.getMessage());
        }
    }


    public void eliminarAnalisis(int idAnalisis) {
        final String sql = "{call str_delete_analisis(?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, idAnalisis);
            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al eliminar el análisis: " + e.getMessage());
        }
    }
}