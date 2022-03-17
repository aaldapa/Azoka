package es.eroski.azoka.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import es.eroski.azoka.dto.Persona;
import es.eroski.azoka.model.PersonaEntity;

@Mapper(componentModel = "spring")
public interface PersonaMapper {
	
	PersonaMapper INSTANCE = Mappers.getMapper(PersonaMapper.class);
	
	   @Mappings({
	        @Mapping(target="id", source="id"),
	        @Mapping(target="nombre", source="nombre"),
	        @Mapping(target="apellido", source="apellido")
	    })
	    PersonaEntity toEntity(Persona source);
	    
	   @Mappings({
	        @Mapping(target="id", source="id"),
	        @Mapping(target="nombre", source="nombre"),
	        @Mapping(target="apellido", source="apellido")
	      })
	    Persona toDto(PersonaEntity target);
	    
	   @Mappings({
	        @Mapping(target="id", source="id"),
	        @Mapping(target="nombre", source="nombre"),
	        @Mapping(target="apellido", source="apellido")
	      })
	    List<Persona> modelsToDtos(List<PersonaEntity> personas);

}
