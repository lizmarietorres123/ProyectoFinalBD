package logico.catalogo;

import logico.Clinica;

public class Medicamento {

    private String id;
    private String nombre;
    private Double concentracion;
    private String presentacion;
    private String viaAdministracion;
    private String fabricante;

    public Medicamento(int idNumber, String nombre, Double concentracion, String presentacion, String viaAdministracion, String fabricante) {
        setId(idNumber);
        this.nombre = nombre;
        this.concentracion = concentracion;
        this.presentacion = presentacion;
        this.viaAdministracion = viaAdministracion;
        this.fabricante = fabricante;
    }


    public String getId() {
        return id;
    }

    public void setId(int idNumber) {
        this.id = Clinica.getInstancia().genId(idNumber, Medicamento.class);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(Double concentracion) {
        this.concentracion = concentracion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getViaAdministracion() {
        return viaAdministracion;
    }

    public void setViaAdministracion(String viaAdministracion) {
        this.viaAdministracion = viaAdministracion;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }
}
