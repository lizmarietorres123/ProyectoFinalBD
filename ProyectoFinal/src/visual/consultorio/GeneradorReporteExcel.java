package visual.consultorio;


import bd.ConexionBD;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GeneradorReporteExcel {

    public static void main(String[] args) {
        // Nombre del archivo que se generará en la carpeta raíz del proyecto
        String rutaArchivo = "Reporte_Prueba.xlsx";
        generarReporte(rutaArchivo);
    }

    public static void generarReporte(String rutaArchivo) {

        // ==============================================================================
        // ZONA PARA TU COMPAÑERA: AQUÍ VA EL SELECT DEL REPORTE FINAL
        // ==============================================================================
        String sql = "SELECT cedula, nombre, apellido, peso, estado FROM paciente";

        // Usamos try-with-resources para cerrar automáticamente conexiones y archivos
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery();
             Workbook workbook = new XSSFWorkbook()) { // Crea el libro de Excel en blanco

            // Crea la hoja de cálculo
            Sheet sheet = workbook.createSheet("Reporte Clínico");

            Row encabezado = sheet.createRow(0);

            CellStyle estiloEncabezado = workbook.createCellStyle();
            Font fuenteBold = workbook.createFont();
            fuenteBold.setBold(true);
            estiloEncabezado.setFont(fuenteBold);


            String[] columnas = {"Cédula", "Nombre", "Apellido", "Peso (lb)", "Estado"};

            for (int i = 0; i < columnas.length; i++) {
                Cell celda = encabezado.createCell(i);
                celda.setCellValue(columnas[i]);
                celda.setCellStyle(estiloEncabezado);
            }

            // --- 2. LLENAR LOS DATOS DESDE LA BASE DE DATOS ---
            int numeroFila = 1;
            while (rs.next()) {
                Row fila = sheet.createRow(numeroFila++);

                // ==============================================================================
                // ZONA PARA TU COMPAÑERA: ASIGNAR LOS VALORES DEL RESULTSET A LAS CELDAS
                // ==============================================================================
                fila.createCell(0).setCellValue(rs.getString("cedula"));
                fila.createCell(1).setCellValue(rs.getString("nombre"));
                fila.createCell(2).setCellValue(rs.getString("apellido"));
                fila.createCell(3).setCellValue(rs.getDouble("peso"));
                fila.createCell(4).setCellValue(rs.getString("estado"));
            }

            // Ajustar el ancho de las columnas automáticamente
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // --- 3. GUARDAR EL ARCHIVO FÍSICO ---
            try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                workbook.write(fileOut);
                System.out.println("ÉXITO: Reporte de Excel generado correctamente en -> " + rutaArchivo);
            }

        } catch (Exception e) {
            System.err.println("ERROR: Fallo al generar el reporte de Excel.");
            e.printStackTrace();
        }
    }
}