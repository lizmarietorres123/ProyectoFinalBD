package visual.consultorio;

import bd.ConexionBD;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class ReporteRendimientoGeneral {

    public static void generarReporteGeneral(String rutaArchivo) {


        String sql = "{call str_reporte_general_rendimiento}";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             Workbook workbook = new XSSFWorkbook()) {

            try (ResultSet rs = stmt.executeQuery()) {
                Sheet sheet = workbook.createSheet("Rendimiento Anual");

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

                String[] columnas = {"Doctor", "Total de Consultas", "Pacientes Únicos"};


                Row filaTitulo = sheet.createRow(0);
                for (int i = 0; i < columnas.length; i++) {
                    Cell celda = filaTitulo.createCell(i);
                    celda.setCellStyle(estiloEncabezado);
                    if (i == 0) celda.setCellValue("Rendimiento Anual por Doctor");
                }
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnas.length - 1));

                Row filaEncabezado = sheet.createRow(1);
                for (int i = 0; i < columnas.length; i++) {
                    Cell celda = filaEncabezado.createCell(i);
                    celda.setCellValue(columnas[i]);
                    celda.setCellStyle(estiloEncabezado);
                }


                int numeroFila = 2;
                boolean hayDatos = false;

                while (rs.next()) {
                    hayDatos = true;
                    Row filaDatos = sheet.createRow(numeroFila++);

                    Cell celdaDoctor = filaDatos.createCell(0);
                    celdaDoctor.setCellValue(rs.getString("Nombre_Doctor"));
                    celdaDoctor.setCellStyle(estiloCelda);

                    Cell celdaConsultas = filaDatos.createCell(1);
                    celdaConsultas.setCellValue(rs.getInt("Total_Consultas"));
                    celdaConsultas.setCellStyle(estiloCelda);

                    Cell celdaPacientes = filaDatos.createCell(2);
                    celdaPacientes.setCellValue(rs.getInt("Pacientes_Unicos"));
                    celdaPacientes.setCellStyle(estiloCelda);
                }

                // Si la consulta viene vacía
                if (!hayDatos) {
                    Row filaVacia = sheet.createRow(numeroFila);
                    for (int i = 0; i < columnas.length; i++) {
                        Cell celdaVacia = filaVacia.createCell(i);
                        celdaVacia.setCellStyle(estiloCelda);
                        if (i == 0) celdaVacia.setCellValue("No hay registros de consultas este año.");
                    }
                    sheet.addMergedRegion(new CellRangeAddress(numeroFila, numeroFila, 0, columnas.length - 1));
                }


                for (int i = 0; i < columnas.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                    workbook.write(fileOut);
                    System.out.println("ÉXITO: Reporte general generado en -> " + rutaArchivo);
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Fallo al generar el reporte general.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        generarReporteGeneral("Reporte_Rendimiento_General.xlsx");
    }
}