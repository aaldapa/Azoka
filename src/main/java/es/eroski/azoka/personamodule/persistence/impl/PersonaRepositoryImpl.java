package es.eroski.azoka.personamodule.persistence.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import es.eroski.azoka.model.PersonaEntity;
import es.eroski.azoka.personamodule.persistence.PersonaRepository;

@Repository
public class PersonaRepositoryImpl implements PersonaRepository {
	
	

	private static Logger logger = LogManager.getLogger(PersonaRepositoryImpl.class);
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<PersonaEntity> selectAllPersonas() throws DataAccessException {
		
		
		logger.info("--- Select All Personas ---");
		String sql = "SELECT * FROM T_PERSONA";
		try {
			return jdbcTemplate.query(sql, new PersonaBdMapper());
		}catch(DataAccessException e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e; 
		}
	}

	@Override
	public PersonaEntity selectPersonaById(int id) throws DataAccessException {
		logger.info("--- Select Persona By Id: "+ id +" ---");
		String sql = "SELECT * FROM T_PERSONA WHERE ID = " + id;
		try {
			return jdbcTemplate.queryForObject(sql, new PersonaBdMapper());
		}catch(DataAccessException e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e; 
		}	
	}

	@Override
	public List<PersonaEntity> selectPersonaByName(String name) throws DataAccessException {
		logger.info("--- Select Persona By Name: "+ name +" ---");	
		String sql = "SELECT * FROM T_PERSONA WHERE NOMBRE = '" + name +"'";
		try {
			return jdbcTemplate.query(sql, new PersonaBdMapper());
		}catch(DataAccessException e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e; 
		}	
	}

	@Override
	public PersonaEntity insertPersona(PersonaEntity persona) throws DataAccessException {
		logger.info("--- Insert Persona: "+ persona.toString() +" ---");
		String sql = "INSERT INTO T_PERSONA (id,nombre,apellido) values (?, ?, ?)";
		try {
			jdbcTemplate.update(sql,persona.getId(),persona.getNombre(),persona.getApellido() );
			return persona;
		}catch(DataAccessException e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e; 
		}	
	}

	@Override
	public int deletePersonaById(int id) throws DataAccessException {
		
		logger.info("--- Delete Persona By Id: "+ id +" ---");
		String sql = "DELETE T_PERSONA  WHERE ID = ?";
		try {
			int num_rows_affected = jdbcTemplate.update(sql,id);
			return num_rows_affected;
		}catch(DataAccessException e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e; 
		}	
	
	}

	@Override
	public int updatePersona(PersonaEntity persona) throws DataAccessException {
		logger.info("--- Update Persona : "+ persona.toString() +" ---");
		String sql = "UPDATE T_PERSONA SET nombre=?,apellido=?  WHERE ID = ?";
		try {
			int num_rows_affected = jdbcTemplate.update(sql,persona.getNombre(),persona.getApellido(),persona.getId());
			return num_rows_affected;
		}catch(DataAccessException e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw e; 
			 
		}	
	}
	
	private class PersonaBdMapper implements RowMapper<PersonaEntity>  {

		public PersonaEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			PersonaEntity res  = new PersonaEntity(rs.getInt("id"),rs.getString("nombre"),rs.getString("apellido"));
			  return res;
		}

	}

}
