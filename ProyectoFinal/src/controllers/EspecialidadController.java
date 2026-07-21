package controllers;

import java.util.ArrayList;
import java.util.List;

import logico.Clinica;
import logico.Enfermedad;
import logico.Especialidad;
import utilidad.Formato;

public class EspecialidadController {
	
	public List<Enfermedad> listEnfermedades() {
	    List<Enfermedad> enfermedades = new ArrayList<>();
	    
	    for (Enfermedad enf : Clinica.getInstancia().getEnfermedades()) {
	    	enfermedades.add(enf);
	    }
	    return enfermedades;
	}
	
	
	public boolean guardarEspecialidad(String nombre, String areaMedica, String descripcion, List<Enfermedad> enfermedadesSeleccionadas) {
        
        if (Formato.entradaVacia(nombre, "El nombre de la especialidad es obligatorio.")|| 
            Formato.entradaVacia(areaMedica, "El área médica de la especialidad es obligatoria.")) {
            return false;
        }

        Especialidad nuevaEspecialidad = new Especialidad();
        nuevaEspecialidad.setNombre(nombre);
        nuevaEspecialidad.setAreaMedica(areaMedica);
        nuevaEspecialidad.setDescripcion(descripcion);

        for (Enfermedad enf : enfermedadesSeleccionadas) {
            nuevaEspecialidad.agregarEnfermedad(enf);
        }

        return true;
    }
	
	

}
