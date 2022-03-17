package es.eroski.azoka.alumnomodule.service;

import java.util.List;


import es.eroski.azoka.model.AlumnoEntity;

public interface AlumnoService {
	
	 public AlumnoEntity createAlumno(AlumnoEntity alumno);
	 public AlumnoEntity modifyAlumno(AlumnoEntity alumno);
	 public List<AlumnoEntity> retriveAllAlumnos();
	 public AlumnoEntity retriveAlumnoById(int id);
	 public int removeAlumno(int id);
}
