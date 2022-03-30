/**
 * 
 */
package es.eroski.azoka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import es.eroski.azoka.dto.SociedadDTO;
import es.eroski.azoka.model.SociedadEntity;

/**
 * Mappeo de Entidad a DTO de Sociedad
 * @author BICUGUAL
 *
 */
@Mapper(componentModel = "spring")
public interface SociedadMapper {

	SociedadMapper INSTANCE = Mappers.getMapper(SociedadMapper.class);

	@Mappings({ 
		@Mapping(target = "sociedad", source = "sociedad"), 
		@Mapping(target = "nif", source = "nif"),
		@Mapping(target = "direccion", source = "direccion"),
		@Mapping(target = "cpPoblacionProvincia", expression = "java(getCpPoblacionProvincia(socEntity))")
	})
	SociedadDTO map (SociedadEntity socEntity);

	default String getCpPoblacionProvincia(SociedadEntity socEntity) {
		StringBuilder str = new StringBuilder();

		str.append(socEntity.getCodigoPostal());
		str.append(" ");
		str.append(socEntity.getPoblacion());
		str.append(" ");
		str.append(socEntity.getSociedad());

		return str.toString();
	}
}
