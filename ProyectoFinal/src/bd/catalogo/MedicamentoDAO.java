package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Medicamento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MedicamentoDAO {

    private static MedicamentoDAO instance = null;

    private MedicamentoDAO() {}

    public static MedicamentoDAO getInstance() {
        if (instance == null) {
            instance = new MedicamentoDAO();
        }
        return instance;
    }

    public void guardarMedicamento(Medicamento medicamento) {
        final String sql = "{call str_insert_medicamento(?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, medicamento.getNombre());

            if (medicamento.getConcentracion() != null) {
                callableStatement.setDouble(2, medicamento.getConcentracion());
            } else {
                callableStatement.setNull(2, java.sql.Types.DECIMAL);
            }

            callableStatement.setString(3, medicamento.getPresentacion());
            callableStatement.setString(4, medicamento.getViaAdministracion());
            callableStatement.setString(5, medicamento.getFabricante());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar el medicamento: " + e.getMessage());
        }
    }

    public void actualizarMedicamento(Medicamento medicamento) {
        final String sql = "{call str_update_medicamento(?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, medicamento.getIdNumber());
            callableStatement.setString(2, medicamento.getNombre());

            if (medicamento.getConcentracion() != null) {
                callableStatement.setDouble(3, medicamento.getConcentracion());
            } else {
                callableStatement.setNull(3, java.sql.Types.DECIMAL);
            }

            callableStatement.setString(4, medicamento.getPresentacion());
            callableStatement.setString(5, medicamento.getViaAdministracion());
            callableStatement.setString(6, medicamento.getFabricante());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el medicamento: " + e.getMessage());
        }
    }

    public ArrayList<Medicamento> obtenerMedicamentos(){
        ArrayList<Medicamento> medicamentos = new ArrayList<>();
        final String sql = "SELECT * FROM medicamento";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)){

            while(rs.next()){
                medicamentos.add(new Medicamento(
                        rs.getInt("id_medicamento"),
                        rs.getString("nombre"),
                        rs.getDouble("concentracion"), // Si es NULL en BD, getDouble() devuelve 0.0
                        rs.getString("presentacion"),
                        rs.getString("via_administracion"),
                        rs.getString("fabricante")
                ));
            }

        } catch(SQLException e) {
            System.err.println("Error al obtener los medicamentos: " + e.getMessage());
        }

        return medicamentos;
    }
}