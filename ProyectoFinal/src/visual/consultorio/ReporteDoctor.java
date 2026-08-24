package visual.consultorio;

import bd.ConexionBD;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class ReporteDoctor {

    public static void generarReporteMesPico(int idDoctor, String rutaArchivo) {

        String sql = "{call str_reporte_mes_doctor(?)}";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             Workbook workbook = new XSSFWorkbook()) {

            stmt.setInt(1, idDoctor);

            try (ResultSet rs = stmt.executeQuery()) {
                Sheet sheet = workbook.createSheet("Me" +
                        "" +
                        "" +
                        "s de Mayor Volumen");
                CellStyle estiloEncabezado = workbook.createCellStyle();
                estiloEncabezado.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
                estiloEncabezado.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
                estiloEncabezado.setBorderTop(BorderStyle.THIN);
                estiloEncabezado.setBorderBottom(BorderStyle.THIN);
                estiloEncabezado.setBorderLeft(BorderStyle.THIN);
                estiloEncabezado.setBorderRight(BorderStyle.THIN);

                Font fuenteBold = workbook.createFont();
                fuenteBold.setBold(true);
                estiloEncabezado.setFont(fuenteBold);

                CellStyle estiloCelda = workbook.createCellStyle();
                estiloCelda.setBorderTop(BorderStyle.THIN);
                estiloCelda.setBorderBottom(BorderStyle.THIN);
                estiloCelda.setBorderLeft(BorderStyle.THIN);
                estiloCelda.setBorderRight(BorderStyle.THIN);

                String[] columnas = {"Mes", "Total de Pacientes Únicos"};

                Row filaTitulo = sheet.createRow(0);

                for (int i = 0; i < columnas.length; i++) {
                    Cell celda = filaTitulo.createCell(i);
                    celda.setCellStyle(estiloEncabezado);
                    if (i == 0) celda.setCellValue("Nombre del reporte");
                }

                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnas.length - 1));

                Row filaEncabezado = sheet.createRow(1);
                for (int i = 0; i < columnas.length; i++) {
                    Cell celda = filaEncabezado.createCell(i);
                    celda.setCellValue(columnas[i]);
                    celda.setCellStyle(estiloEncabezado);
                }

                int numeroFila = 2;
                if (rs.next()) {
                    Row filaDatos = sheet.createRow(numeroFila++);

                    Cell celdaMes = filaDatos.createCell(0);
                    celdaMes.setCellValue(rs.getString("Mes"));
                    celdaMes.setCellStyle(estiloCelda); // Aplica bordes

                    Cell celdaTotal = filaDatos.createCell(1);
                    celdaTotal.setCellValue(rs.getInt("Total_Pacientes"));
                    celdaTotal.setCellStyle(estiloCelda); // Aplica bordes
                } else {
                    Row filaVacia = sheet.createRow(numeroFila);
                    for (int i = 0; i < columnas.length; i++) {
                        Cell celdaVacia = filaVacia.createCell(i);
                        celdaVacia.setCellStyle(estiloCelda);
                        if (i == 0) celdaVacia.setCellValue("Sin registros");
                    }
                    sheet.addMergedRegion(new CellRangeAddress(numeroFila, numeroFila, 0, columnas.length - 1));
                }

                for (int i = 0; i < columnas.length; i++) {
                    sheet.autoSizeColumn(i);
                }
                try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                    workbook.write(fileOut);
                    System.out.println("ÉXITO: Reporte estructurado generado en -> " + rutaArchivo);
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Fallo al generar el reporte.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        generarReporteMesPico(1, "Reporte_Mes_Pico_Doctor.xlsx");
    }
}