package logico.catalogo;

import logico.Clinica;

public class Analisis {

    private String id;
    private String nombre;
    private String tipo;
    private String unidadMedida;
    private Double valorProm;
    private Double valorMax;
    private Double valorMin;

    public Analisis(int idNumber, String nombre, String tipo, String unidadMedida, Double valorProm, Double valorMax, Double valorMin) {
        setId(idNumber);
        this.nombre = nombre;
        this.tipo = tipo;
        this.unidadMedida = unidadMedida;
        this.valorProm = valorProm;
        this.valorMax = valorMax;
        this.valorMin = valorMin;
    }


    public String getId() {
        return id;
    }

    public void setId(int idNumber) {
        this.id = Clinica.getInstancia().genId(idNumber, Analisis.class);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Double getValorProm() {
        return valorProm;
    }

    public void setValorProm(Double valorProm) {
        this.valorProm = valorProm;
    }

    public Double getValorMax() {
        return valorMax;
    }

    public void setValorMax(Double valorMax) {
        this.valorMax = valorMax;
    }

    public Double getValorMin() {
        return valorMin;
    }

    public void setValorMin(Double valorMin) {
        this.valorMin = valorMin;
    }

}
