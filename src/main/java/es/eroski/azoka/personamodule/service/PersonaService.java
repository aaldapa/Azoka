package es.eroski.azoka.personamodule.service;

import java.util.List;

import es.eroski.azoka.model.PersonaEntity;

public interface PersonaService {
	
	public List<PersonaEntity> retrivePersonas();
	public PersonaEntity retrivePersonaById(int id);
	public List<PersonaEntity> retrivePersonaByName(String name);
	public PersonaEntity createPersona(PersonaEntity persona);
    public int removePersonaById(int id);
    public int modifyPersona(PersonaEntity persona);


}
