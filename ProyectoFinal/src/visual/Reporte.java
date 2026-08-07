package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import logico.consultorio.Clinica;
import logico.catalogo.Enfermedad;
import logico.consultorio.Paciente;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Reporte extends JDialog {
    private final JPanel contentPanel = new JPanel();

    public static void main(String[] args) {
        try {
            Reporte dialog = new Reporte();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Reporte() {
        setTitle("Reportes");
        setBounds(100, 100, 1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        
        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("REPORTES ESTAD�STICOS");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Bahnschrift", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(70, 130, 180));
        contentPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelGraficos = new JPanel();
        panelGraficos.setBackground(new Color(240, 248, 255));
        panelGraficos.setLayout(new GridLayout(2, 2, 10, 10));
        contentPanel.add(panelGraficos, BorderLayout.CENTER);

        panelGraficos.add(crearGraficoPacientesPorGenero());
        panelGraficos.add(crearGraficoPacientesEnVigilancia());
        panelGraficos.add(crearGraficoEnfermedadesVigilancia());
        panelGraficos.add(crearPanelEstadisticas());
    }

    private JPanel crearGraficoPacientesPorGenero() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(
            new LineBorder(new Color(70, 130, 180), 2),
            "Distribuci�n de Pacientes por G�nero",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Bahnschrift", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));

        int masculino = 0;
        int femenino = 0;

        for (Paciente p : Clinica.getInstancia().getPacientes()) {
            if (p.getSexo().equalsIgnoreCase("M")) {
                masculino++;
            } else if (p.getSexo().equalsIgnoreCase("F")) {
                femenino++;
            }
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        if (masculino > 0) {
            dataset.setValue("Masculino (" + masculino + ")", masculino);
        }
        if (femenino > 0) {
            dataset.setValue("Femenino (" + femenino + ")", femenino);
        }

        if (masculino == 0 && femenino == 0) {
            dataset.setValue("Sin datos", 1);
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "",
            dataset,
            true,
            true,
            false
        );

        personalizarGraficoPie(chart, new Color(100, 149, 237), new Color(220, 20, 60));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearGraficoPacientesEnVigilancia() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(
            new LineBorder(new Color(70, 130, 180), 2),
            "Pacientes con Enfermedades en Vigilancia",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Bahnschrift", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));

        int conVigilancia = 0;
        int sinVigilancia = 0;

        for (Paciente p : Clinica.getInstancia().getPacientes()) {
            if (tieneEnfermedadEnVigilancia(p)) {
                conVigilancia++;
            } else {
                sinVigilancia++;
            }
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        
        if (conVigilancia > 0) {
            dataset.setValue("Con Vigilancia (" + conVigilancia + ")", conVigilancia);
        }
        if (sinVigilancia > 0) {
            dataset.setValue("Sin Vigilancia (" + sinVigilancia + ")", sinVigilancia);
        }

        if (conVigilancia == 0 && sinVigilancia == 0) {
            dataset.setValue("Sin datos", 1);
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "",
            dataset,
            true,
            true,
            false
        );

        personalizarGraficoPie(chart, new Color(220, 20, 60), new Color(50, 205, 50));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearGraficoEnfermedadesVigilancia() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(
            new LineBorder(new Color(70, 130, 180), 2),
            "Pacientes por Enfermedad en Vigilancia",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Bahnschrift", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));

        Map<String, Integer> contadorEnfermedades = new HashMap<>();
        
        for (Paciente p : Clinica.getInstancia().getPacientes()) {
            for (Enfermedad e : p.getEnfermedades()) {
                if (e.isVigilancia()) {
                    String nombreEnf = e.getNombre();
                    contadorEnfermedades.put(nombreEnf, contadorEnfermedades.getOrDefault(nombreEnf, 0) + 1);
                }
            }
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        
        if (contadorEnfermedades.isEmpty()) {
            dataset.setValue("Sin enfermedades en vigilancia", 1);
        } else {
            for (Map.Entry<String, Integer> entry : contadorEnfermedades.entrySet()) {
                dataset.setValue(entry.getKey() + " (" + entry.getValue() + ")", 
                                entry.getValue());
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "",
            dataset,
            true,
            true,
            false
        );

        chart.setBackgroundPaint(Color.WHITE);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(240, 248, 255));
        plot.setOutlinePaint(new Color(135, 206, 235));
        plot.setLabelFont(new Font("Bahnschrift", Font.PLAIN, 11));
        plot.setLabelBackgroundPaint(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(
            new LineBorder(new Color(70, 130, 180), 2),
            "Resumen Estad�stico",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Bahnschrift", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));

        JLabel lblTotalPacientes = new JLabel("Total de Pacientes:");
        lblTotalPacientes.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblTotalPacientes.setForeground(new Color(70, 130, 180));
        lblTotalPacientes.setBounds(20, 42, 200, 25);
        panel.add(lblTotalPacientes);

        JLabel lblNumPacientes = new JLabel(String.valueOf(Clinica.getInstancia().getPacientes().size()));
        lblNumPacientes.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        lblNumPacientes.setForeground(new Color(100, 149, 237));
        lblNumPacientes.setBounds(250, 42, 100, 25);
        panel.add(lblNumPacientes);

        int totalEnfVigilancia = contarEnfermedadesEnVigilancia();
        
        JLabel lblEnfVigilancia = new JLabel("Enfermedades en Vigilancia:");
        lblEnfVigilancia.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblEnfVigilancia.setForeground(new Color(70, 130, 180));
        lblEnfVigilancia.setBounds(20, 85, 220, 25);
        panel.add(lblEnfVigilancia);

        JLabel lblNumEnfVigilancia = new JLabel(String.valueOf(totalEnfVigilancia));
        lblNumEnfVigilancia.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        lblNumEnfVigilancia.setForeground(new Color(220, 20, 60));
        lblNumEnfVigilancia.setBounds(250, 85, 100, 25);
        panel.add(lblNumEnfVigilancia);

        JLabel lblTotalConsultas = new JLabel("Total de Consultas:");
        lblTotalConsultas.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblTotalConsultas.setForeground(new Color(70, 130, 180));
        lblTotalConsultas.setBounds(20, 130, 200, 25);
        panel.add(lblTotalConsultas);

        JLabel lblNumConsultas = new JLabel(String.valueOf(Clinica.genCodigoConsultas - 1));
        lblNumConsultas.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        lblNumConsultas.setForeground(new Color(100, 149, 237));
        lblNumConsultas.setBounds(250, 130, 100, 25);
        panel.add(lblNumConsultas);

        float porcentajeVigilancia = calcularPorcentajePacientesEnVigilancia();
        
        JLabel lblPorcentaje = new JLabel("% Pacientes en Vigilancia:");
        lblPorcentaje.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblPorcentaje.setForeground(new Color(70, 130, 180));
        lblPorcentaje.setBounds(20, 175, 220, 25);
        panel.add(lblPorcentaje);

        JLabel lblNumPorcentaje = new JLabel(String.format("%.1f%%", porcentajeVigilancia));
        lblNumPorcentaje.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        lblNumPorcentaje.setForeground(new Color(220, 20, 60));
        lblNumPorcentaje.setBounds(250, 175, 100, 25);
        panel.add(lblNumPorcentaje);

        return panel;
    }

    private boolean tieneEnfermedadEnVigilancia(Paciente p) {
        for (Enfermedad e : p.getEnfermedades()) {
            if (e.isVigilancia()) {
                return true;
            }
        }
        return false;
    }

    private int contarEnfermedadesEnVigilancia() {
        int contador = 0;
        for (Enfermedad e : Clinica.getInstancia().getEnfermedades()) {
            if (e.isVigilancia()) {
                contador++;
            }
        }
        return contador;
    }

    private float calcularPorcentajePacientesEnVigilancia() {
        int totalPacientes = Clinica.getInstancia().getPacientes().size();
        if (totalPacientes == 0) {
            return 0.0f;
        }
        
        int pacientesConVigilancia = 0;
        for (Paciente p : Clinica.getInstancia().getPacientes()) {
            if (tieneEnfermedadEnVigilancia(p)) {
                pacientesConVigilancia++;
            }
        }
        
        return((pacientesConVigilancia * 100.0f) / totalPacientes);
    }

    private void personalizarGraficoPie(JFreeChart chart, Color color1, Color color2) {
        chart.setBackgroundPaint(Color.WHITE);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(240, 248, 255));
        plot.setOutlinePaint(new Color(135, 206, 235));
        plot.setLabelFont(new Font("Bahnschrift", Font.BOLD, 12));
        plot.setLabelBackgroundPaint(Color.WHITE);
        
        if (plot.getDataset().getItemCount() >= 1) {
            plot.setSectionPaint(0, color1);
        }
        if (plot.getDataset().getItemCount() >= 2) {
            plot.setSectionPaint(1, color2);
        }
    }
}