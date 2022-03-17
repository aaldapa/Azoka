package es.eroski.azoka.alumnomodule.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.eroski.azoka.alumnomodule.persistence.AlumnoRepository;
import es.eroski.azoka.alumnomodule.service.AlumnoService;
import es.eroski.azoka.model.AlumnoEntity;

@Service
public class AlumnoServiceImpl implements AlumnoService {
	
	private static Logger logger = LogManager.getLogger(AlumnoServiceImpl.class);
	
	@Autowired
	private AlumnoRepository alumnorepository;

	@Override
	public AlumnoEntity createAlumno(AlumnoEntity alumno) {
		logger.info("--- Create Alumno ---");
		try {
			return alumnorepository.save(alumno);
		}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e;
		}
	}
	
	@Override
	public AlumnoEntity modifyAlumno(AlumnoEntity alumno) {
		logger.info("--- Modify Alumno ---");
		try {
			return alumnorepository.save(alumno);
		}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e;
		}
	}

	@Override
	public List<AlumnoEntity> retriveAllAlumnos() {
		logger.info("--- Retrive All Alumnos ---");
		return alumnorepository.findAll();
	}

	@Override
	public AlumnoEntity retriveAlumnoById(int id) {
		logger.info("--- Retrive Alumno By Id ---");
		return alumnorepository.findById(id).orElse(null);
	}

	@Override
	public int removeAlumno(int id) {
		logger.info("--- Remove Alumno  ---");
		try {
			 alumnorepository.deleteById(id);
			 return 1;
		}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e;
		}
	}
	
	

}
