package controllers;

import bd.ConsultaDAO;
import logico.consultorio.Consulta;

public class ConsultaController {

    public void registrarConsulta(Consulta consulta){
        ConsultaDAO.getInstance().guardarConsulta(consulta);
    }
}