package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Doctor;
import java.sql.*;
import java.util.ArrayList;

public class DoctorDAO {
    private static DoctorDAO instance = null;
    private DoctorDAO() {}

    public static DoctorDAO getInstance() {
        if (instance == null) instance = new DoctorDAO();
        return instance;
    }

    public void guardarDoctor(Doctor doctor) {
        final String sql = "{call str_insert_doctor(?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setString(1, doctor.getNombre());
            cs.setString(2, doctor.getApellido());
            cs.setInt(3, doctor.getCupoDiario());

            if (doctor.getUsuario() != null) cs.setInt(4, doctor.getUsuario().getIdNumber());
            else cs.setNull(4, Types.INTEGER);

            cs.setString(5, doctor.getEspecialidad());
            cs.setString(6, doctor.getEstado());

            cs.execute();
        } catch (SQLException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    public void actualizarDoctor(Doctor doctor) {
        final String sql = "{call str_update_doctor(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setInt(1, doctor.getIdNumber());
            cs.setString(2, doctor.getNombre());
            cs.setString(3, doctor.getApellido());
            cs.setInt(4, doctor.getCupoDiario());

            if (doctor.getUsuario() != null) cs.setInt(5, doctor.getUsuario().getIdNumber());
            else cs.setNull(5, Types.INTEGER);

            cs.setString(6, doctor.getEspecialidad());
            cs.setString(7, doctor.getEstado());

            cs.execute();
        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
        }
    }

    public ArrayList<Doctor> obtenerDoctores() {
        ArrayList<Doctor> doctores = new ArrayList<>();
        final String sql = "SELECT * FROM doctor";

        try (Connection connection = ConexionBD.getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                doctores.add(new Doctor(
                        rs.getInt("id_doctor"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("cupo_diario"),
                        rs.getString("especialidad"),
                        rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener: " + e.getMessage());
        }
        return doctores;
    }
}