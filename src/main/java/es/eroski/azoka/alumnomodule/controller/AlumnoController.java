package es.eroski.azoka.alumnomodule.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import es.eroski.azoka.alumnomodule.service.AlumnoService;
import es.eroski.azoka.dto.Alumno;
import es.eroski.azoka.exceptions.CustomResponseStatusException;
import es.eroski.azoka.mapper.AlumnoMapper;
import es.eroski.azoka.model.AlumnoEntity;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.http.HttpStatus;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/alumnos")
//@OpenAPIDefinition(info = @Info(title = "Example API", version = "1.0", description = "Metodos de prueba API Rest Alumnos"))
public class AlumnoController {

    @Autowired
    AlumnoService alumnoservice;

    @Autowired
    AlumnoMapper alumnomapper;
    
    private static Logger logger = LogManager.getLogger(AlumnoController.class);
    
    @PostMapping("/")
	public Alumno newAlumno(@RequestBody Alumno alumno,HttpServletResponse response){
		 logger.info("--- New Alumno  ---");
		 try {
			response.setStatus(201);
			// Aunque venga un id, como es un POST, quitamos el id para que se ejecute alta.
			alumno.setId(0);
			AlumnoEntity alumnoentity = alumnomapper.toEntity(alumno);
			alumnoentity = alumnoservice.createAlumno(alumnoentity);
		 	return  alumnomapper.toDto(alumnoentity);
	 	}catch(Exception e) {
	 		logger.error(e.getClass() + " " + e.getMessage());
	 		if ( e instanceof DuplicateKeyException ) {
	 			throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, 40001, "Clave primaria duplica", e);
	 		}else {
	 			if ( e instanceof DataIntegrityViolationException ) {
	 				throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, 40002, "Error integridad", e);
	 			}else {
	 				throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 50004, "Error creando persona", e);
	 			}	
	 		}	
	 	}
	 }
    
    @PutMapping("/")
	public Alumno changeAlumno(@RequestBody Alumno alumno,HttpServletResponse response){
		 logger.info("--- Change Alumno  ---");
		 try {
			response.setStatus(200);
			AlumnoEntity alumnoentity = alumnomapper.toEntity(alumno);
			alumnoentity = alumnoservice.modifyAlumno(alumnoentity);
		 	return  alumnomapper.toDto(alumnoentity);
	 	}catch(Exception e) {
	 		logger.error(e.getClass() + " " + e.getMessage());
	 		if ( e instanceof DuplicateKeyException ) {
	 			throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, 40001, "Clave primaria duplica", e);
	 		}else {
	 			if ( e instanceof DataIntegrityViolationException ) {
	 				throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, 40002, "Error integridad", e);
	 			}else {
	 				throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 50004, "Error creando persona", e);
	 			}	
	 		}	
	 	}
	 }
    @GetMapping("/")
    public List<Alumno> findAll(HttpServletResponse response) {
    	logger.info("--- FindAll Alumnos ---");
    	try {
	    	response.setStatus(200);
	    	return alumnomapper.modelsToDtos(alumnoservice.retriveAllAlumnos());
    	}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 50003, "Error recuperando personas por nombre", e);
		}
    }
    
    @GetMapping("/{id}")
    public Alumno findOne(@PathVariable(name="id")int id, HttpServletResponse response) {
    	logger.info("--- FindOne Alumno By Id  ---");
    	try {
    		response.setStatus(200);
    		return alumnomapper.toDto(alumnoservice.retriveAlumnoById(id));
    	}catch(Exception e) {
    		logger.error(e.getClass() + " " + e.getMessage());
			throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 50002, "Error recuperando persona por id", e);
    	}
    }
    
    @DeleteMapping("/{id}")
	 public int eraseById(@PathVariable(name="id")int id,HttpServletResponse response) {
		 logger.info("--- Erase Alumno : " + id +" ---");
		try {
			response.setStatus(200);
		 	return alumnoservice.removeAlumno(id);
	 	}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 50005, "Error borrando persona", e);
		}
	 }
	

}
