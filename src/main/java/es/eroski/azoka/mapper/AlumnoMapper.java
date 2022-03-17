package es.eroski.azoka.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import es.eroski.azoka.dto.Alumno;
import es.eroski.azoka.model.AlumnoEntity;


@Mapper(componentModel = "spring")
public interface AlumnoMapper {
		
	   AlumnoMapper INSTANCE = Mappers.getMapper(AlumnoMapper.class);
	
	   @Mappings({
	        @Mapping(target="identifier", source="id"),
	        @Mapping(target="name", source="nombre"),
	        @Mapping(target="surname", source="apellido")
	    })
	    AlumnoEntity toEntity(Alumno source);
	    
	   @Mappings({
	        @Mapping(target="id", source="identifier"),
	        @Mapping(target="nombre", source="name"),
	        @Mapping(target="apellido", source="surname")
	      })
	    Alumno toDto(AlumnoEntity target);
	    
	   @Mappings({
	        @Mapping(target="id", source="identifier"),
	        @Mapping(target="nombre", source="name"),
	        @Mapping(target="apellido", source="surname")
	      })
	    List<Alumno> modelsToDtos(List<AlumnoEntity> alumnos);

}
