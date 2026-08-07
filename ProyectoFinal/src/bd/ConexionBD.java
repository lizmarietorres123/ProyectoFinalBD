package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String SERVIDOR = "100.107.122.125";
    private static final String PUERTO = "55488";
    private static final String BASE_DATOS = "Clinica";

    private static final String USUARIO = "lmtp";
    private static final String PASSWORD = "20260806";


    public static Connection getConnection() {

        Connection con = null;

        try {

            String url = "jdbc:sqlserver://" + SERVIDOR + ":" + PUERTO + ";"
                    + "databaseName=" + BASE_DATOS + ";"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;";


            con = DriverManager.getConnection(
                    url,
                    USUARIO,
                    PASSWORD
            );


            System.out.println("Conexión exitosa a la base de datos");


        } catch (SQLException e) {

            System.out.println("Error al conectar con la base de datos");
            e.printStackTrace();

        }

        return con;
    }
}