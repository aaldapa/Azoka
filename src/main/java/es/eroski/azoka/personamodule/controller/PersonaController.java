package es.eroski.azoka.personamodule.controller;


import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import es.eroski.azoka.dto.Persona;
import es.eroski.azoka.exceptions.CustomResponseStatusException;
import es.eroski.azoka.mapper.PersonaMapper;
import es.eroski.azoka.model.PersonaEntity;
import es.eroski.azoka.personamodule.service.PersonaService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/personas")
//@OpenAPIDefinition(info = @Info(title = "Example API", version = "1.0", description = "Metodos de prueba API Rest Persona"))
public class PersonaController {	
	
	@Autowired
	PersonaService personaservice;
	
	@Autowired
	PersonaMapper personamapper;
	
	private static Logger logger = LogManager.getLogger(PersonaController.class);
	
	@GetMapping("/")
	@Operation(summary = "Obtener todas las personas", description = "Devuelve una lista con todas las personas existentes")
    @ApiResponses(value = { 
    				@ApiResponse(responseCode = "200", 
    						     description = "Personas recuperadas",
    						     content = {@Content(mediaType = "application/json", 
    						                         array = @ArraySchema(schema = @Schema(implementation = Persona.class)))
    						     }),
    				@ApiResponse(responseCode = "500", 
    							 description = "Internal Server Error",
    						     content = {@Content(mediaType = "application/json",
    						     					 schema = @Schema(implementation = CustomResponseStatusException.class))
    						     })
    				})	
    public List<Persona> getPersonas(HttpServletResponse response) {
		logger.info("--- Get Personas ---");
		try {
			response.setStatus(200);
			return personamapper.modelsToDtos(personaservice.retrivePersonas());
		}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "Error recuperando personas", e);
		}
	}
	
	@Operation(summary = "Obtener persona por id", description = "Devuelve la persona con el id introducido")
    @ApiResponses(value = { 
    				@ApiResponse(responseCode = "200", 
    						     description = "Persona recuperada",
    						     content = {@Content(mediaType = "application/json", 
    						    		             schema = @Schema(implementation = Persona.class))
    						     }),
    				@ApiResponse(responseCode = "500", 
    							 description = "Internal Server Error",
    						     content = {@Content(mediaType = "application/json",
    						     					 schema = @Schema(implementation = CustomResponseStatusException.class))
    						     })
    				})	
	@GetMapping("/id/{id}")
	public Persona getPersonaById(@PathVariable(name="id")int id, HttpServletResponse response) {
		logger.info("--- Get Persona By Id: " + id +" ---");
		try {
			response.setStatus(200);
			return personamapper.toDto(personaservice.retrivePersonaById(id));
		}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 50002, "Error recuperando persona por id", e);
		}	
	}
	
	@Operation(summary = "Obtener personas por nombre", description = "Devuelve lista de persona con el Nombre introducido")
    @ApiResponses(value = { 
    				@ApiResponse(responseCode = "200", 
    						     description = "Personas recuperada",
    						     content = {@Content(mediaType = "application/json", 
    						    		    array = @ArraySchema(schema = @Schema(implementation = Persona.class)))
    						     }),
    				@ApiResponse(responseCode = "500", 
    							 description = "Internal Server Error",
    						     content = {@Content(mediaType = "application/json",
    						     					 schema = @Schema(implementation = CustomResponseStatusException.class))
    						     })
    				})	
	@GetMapping("/nombre/{name}")
	public List<Persona> getPersonaByName(@PathVariable(name="name")String name,HttpServletResponse response) {
		logger.info("--- Get Persona By Name: " + name +" ---");
	    try {
	    	response.setStatus(200);
	    	return personamapper.modelsToDtos(personaservice.retrivePersonaByName(name));

		}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 50003, "Error recuperando personas por nombre", e);
		}
	}

	@Operation(summary = "Crear nueva persona", description = "Crear una nueva persona y nos devuelve la persona")
    @ApiResponses(value = { 
    				@ApiResponse(responseCode = "201", 
    						     description = "Persona creada",
    						     content = {@Content(mediaType = "application/json", 
    						                         schema = @Schema(implementation = Persona.class))
    						     }),
    				@ApiResponse(responseCode = "400", 
					 description = "Internal Server Error",
				     content = {@Content(mediaType = "application/json",
				     					 schema = @Schema(implementation = CustomResponseStatusException.class))
				    }),
    				@ApiResponse(responseCode = "500", 
    							 description = "Internal Server Error",
    						     content = {@Content(mediaType = "application/json",
    						     					 schema = @Schema(implementation = CustomResponseStatusException.class))
    						     })
    				})	
	
	 @PostMapping("/")
	 public Persona newPersona(@RequestBody Persona persona,HttpServletResponse response){
		 logger.info("--- New Persona : " + persona.toString() +" ---");
		 try {
			response.setStatus(201);
			PersonaEntity personaentity = personamapper.toEntity(persona);
			personaentity = personaservice.createPersona(personaentity);
			return personamapper.toDto(personaentity);			
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
	
	 @Operation(summary = "Borra datos persona", description = "Borra datos de la persona con el id introducido. Devuelve 1 si se ha borrado registro. 0 si no se ha borrado y -1 si hay error")
	    @ApiResponses(value = { 
	    				@ApiResponse(responseCode = "200", 
	    						     description = "Personas borradas",
	    						     content = {@Content(mediaType = "application/json", 
	    						                         schema = @Schema(implementation = int.class))
	    						     }),
	    				@ApiResponse(responseCode = "500", 
	    							 description = "Internal Server Error",
	    						     content = {@Content(mediaType = "application/json",
	    						     					 schema = @Schema(implementation = CustomResponseStatusException.class))
	    						     })
	    				})	
	
	 @DeleteMapping("/{id}")
	 public int eraseById(@PathVariable(name="id")int id,HttpServletResponse response) {
		 logger.info("--- Erase Persona : " + id +" ---");
		try {
			response.setStatus(200);
		 	return personaservice.removePersonaById(id);
	 	}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 50005, "Error borrando persona", e);
		}
	 }
	 
	 @Operation(summary = "Actualiza persona", description = "Actualiza datos de la persona con el id introducido. Devuelve 1 si se ha actualizado registro. 0 si no y -1 si hay error ")
	    @ApiResponses(value = { 
	    				@ApiResponse(responseCode = "200", 
	    						     description = "Personas actualizadas",
	    						     content = {@Content(mediaType = "application/json", 
	    						                         schema = @Schema(implementation = int.class))
	    						     }),
	    				@ApiResponse(responseCode = "500", 
	    							 description = "Internal Server Error",
	    						     content = {@Content(mediaType = "application/json",
	    						     					 schema = @Schema(implementation = CustomResponseStatusException.class))
	    						     })
	    				})	
			 
	 @PutMapping("/")
	 public int changePersona(@RequestBody Persona persona,HttpServletResponse response) {
		 logger.info("--- Change Persona : " + persona.toString() +" ---");	
		 try {
			 response.setStatus(201);
			 PersonaEntity personaentity = personamapper.toEntity(persona);
			 int res = personaservice.modifyPersona(personaentity);
			 return res;						 
		 }catch(Exception e) {
		 		logger.error(e.getClass() + " " + e.getMessage());
		 		if ( e instanceof DuplicateKeyException ) {
		 			throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, 40003, "Clave primaria duplica", e);
		 		}else {
		 			if ( e instanceof DataIntegrityViolationException ) {
		 				throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, 40004, "Error integridad", e);
		 			}else {
		 				throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 50006, "Error actualizando persona", e);
		 			}	
		 		}	
		 	}
	 }
			 
}
