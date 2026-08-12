package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Medicamento;
import logico.catalogo.Sintoma;

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
                        rs.getDouble("concentracion"),
                        rs.getString("presentacion"),
                        rs.getString("via_administracion"),
                        rs.getString("fabricante")
                ));
            }

        }catch(SQLException e) {
            System.err.println("Error al guardar el analisis: " + e.getMessage());
        }

        return medicamentos;
    }
}
