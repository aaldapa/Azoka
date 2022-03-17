package es.eroski.azoka.personamodule.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.eroski.azoka.model.PersonaEntity;
import es.eroski.azoka.personamodule.persistence.PersonaRepository;
import es.eroski.azoka.personamodule.service.PersonaService;

@Service
public class PersonaServiceImpl implements PersonaService {
	
	private static Logger logger = LogManager.getLogger(PersonaServiceImpl.class);
	
	@Autowired
	PersonaRepository personarepository;
	
	@Override
	public List<PersonaEntity> retrivePersonas() {
		logger.info("--- Retrive Personas ---");
		try {
			return personarepository.selectAllPersonas();
		}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e;
		}
	}

	@Override
	public PersonaEntity retrivePersonaById(int id)  {
		logger.info("--- Retrive Persona By Id: "+ id +" ---");
		try {
			return personarepository.selectPersonaById(id);
		}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e;
		}
	}

	@Override
	public List<PersonaEntity> retrivePersonaByName(String name)  {
		logger.info("--- Retrive Persona By Name: "+ name +" ---");
		try {
			return personarepository.selectPersonaByName(name);
		}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e;
		}
	}

	@Override
	public PersonaEntity createPersona(PersonaEntity persona)  {
		logger.info("--- Create Persona: "+ persona.toString() +" ---");
		try {
			return personarepository.insertPersona(persona);
		}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e;
		}
	}

	@Override
	public int removePersonaById(int id)  {
		logger.info("--- Remove Persona By Id: "+ id +" ---");
		try {
			return personarepository.deletePersonaById(id);
		}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e;
		}
	}

	@Override
	public int modifyPersona(PersonaEntity persona)  {
		logger.info("--- modifyPersona: "+ persona.toString() +" ---");
		try {
			return personarepository.updatePersona(persona);
		}catch(Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e;
		}
	}

}
