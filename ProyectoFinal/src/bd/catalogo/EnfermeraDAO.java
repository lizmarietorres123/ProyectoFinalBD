package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Enfermera;

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

    public ArrayList<Enfermera> obtenerEnfermeras(){
        ArrayList<Enfermera> enfermeras = new ArrayList<>();
        final String sql = "SELECT * FROM enfermera";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)){

            while(rs.next()){
                enfermeras.add(new Enfermera(
                        rs.getInt("id_enfermera"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        null
//                        Clinica.getInstancia().buscarUsuarioXId(
//                               Clinica.getInstancia().genId(rs.getInt("id_usuario"), Usuario.class)
//                        )
                ));
            }

        }catch(SQLException e) {
            System.err.println("Error al guardar el analisis: " + e.getMessage());
        }

        return enfermeras;
    }

}
