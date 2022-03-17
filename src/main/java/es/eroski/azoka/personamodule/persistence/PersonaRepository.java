package es.eroski.azoka.personamodule.persistence;

import java.util.List;
import org.springframework.dao.DataAccessException;

import es.eroski.azoka.model.PersonaEntity;

public interface PersonaRepository {
	
	public List<PersonaEntity> selectAllPersonas() throws DataAccessException;
	public PersonaEntity selectPersonaById(int id) throws DataAccessException;
	public List<PersonaEntity> selectPersonaByName(String name) throws DataAccessException;
	public PersonaEntity insertPersona(PersonaEntity persona) throws DataAccessException;
    public int deletePersonaById(int id) throws DataAccessException;
    public int updatePersona(PersonaEntity persona) throws DataAccessException;
	
    
}
	