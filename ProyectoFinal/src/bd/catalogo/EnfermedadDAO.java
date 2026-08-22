package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Enfermedad;
import logico.catalogo.Sintoma;
import logico.Clinica;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EnfermedadDAO {

    private static EnfermedadDAO instance = null;

    private EnfermedadDAO() {}

    public static EnfermedadDAO getInstance() {
        if (instance == null) {
            instance = new EnfermedadDAO();
        }
        return instance;
    }

    public void guardarEnfermedad(Enfermedad enfermedad) {
        final String sql = "{call str_insert_enfermedad(?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, enfermedad.getDescripcion());
            callableStatement.setBoolean(2, enfermedad.isEsContagiosa());

            if (enfermedad.getEspecialidad() != null) {
                // Se asume que Especialidad tiene el método getIdNumber()
                callableStatement.setInt(3, enfermedad.getEspecialidad().getIdNumber());
            } else {
                callableStatement.setNull(3, java.sql.Types.INTEGER);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar la enfermedad: " + e.getMessage());
        }
    }

    public void actualizarEnfermedad(Enfermedad enfermedad) {
        final String sql = "{call str_update_enfermedad(?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, enfermedad.getIdNumber());
            callableStatement.setString(2, enfermedad.getDescripcion());
            callableStatement.setBoolean(3, enfermedad.isEsContagiosa());

            if (enfermedad.getEspecialidad() != null) {
                callableStatement.setInt(4, enfermedad.getEspecialidad().getIdNumber());
            } else {
                callableStatement.setNull(4, java.sql.Types.INTEGER);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar la enfermedad: " + e.getMessage());
        }
    }

    // --- NUEVO MÉTODO DE ELIMINACIÓN ---
    public void eliminarEnfermedad(int idEnfermedad) {
        final String sql = "{call str_delete_enfermedad(?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, idEnfermedad);
            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al eliminar la enfermedad: " + e.getMessage());
        }
    }

    public ArrayList<Enfermedad> obtenerEnfermedades(){
        ArrayList<Enfermedad> enfermedades = new ArrayList<>();
        final String sql = "SELECT * FROM enfermedad";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)){

            while(rs.next()){
                enfermedades.add(new Enfermedad(
                        rs.getInt("id_enfermedad"),
                        // rs.getString("nombre") <- Eliminado. Ya no existe en la BD.
                        rs.getString("descripcion"),
                        rs.getBoolean("es_contagiosa"),
                        null, // Aquí deberías cargar el objeto Especialidad si es necesario
                        obtenerSintomas(rs.getInt("id_enfermedad"))
                ));
            }

        } catch(SQLException e) {
            System.err.println("Error al obtener las enfermedades: " + e.getMessage());
        }

        return enfermedades;
    }

    public ArrayList<Sintoma> obtenerSintomas(int idEnfermedad){
        ArrayList<Sintoma> sintomas = new ArrayList<>();

        final String sql = "select e_s.id_sintoma " +
                "from enfermedad_sintoma e_s " +
                "inner join enfermedad e on e.id_enfermedad = e_s.id_enfermedad "+
                "where e_s.id_enfermedad = " + idEnfermedad;

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)){

            while(rs.next()){
                int idSintomaBD = rs.getInt("id_sintoma");

                Sintoma sintomaEncontrado = Clinica.getInstancia().buscarSintomaXId(
                        Clinica.getInstancia().genId(idSintomaBD, Sintoma.class)
                );

                if (sintomaEncontrado != null) {
                    sintomas.add(sintomaEncontrado);
                }
            }

        } catch(SQLException e) {
            System.err.println("Error al obtener los síntomas de la enfermedad: " + e.getMessage());
        }

        return sintomas;
    }
}