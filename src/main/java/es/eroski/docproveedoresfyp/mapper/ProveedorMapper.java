/**
 * 
 */
package es.eroski.docproveedoresfyp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import es.eroski.docproveedoresfyp.dto.ProveedorDTO;
import es.eroski.docproveedoresfyp.model.DireccionYcpProveedorEntity;
import es.eroski.docproveedoresfyp.model.NombreYNifProveedorEntity;

/**
 * Mappeo de Entidad a DTO de Proveedor
 * @author BICUGUAL
 *
 */
@Mapper(componentModel = "spring")
public interface ProveedorMapper {

	ProveedorMapper INSTANCE = Mappers.getMapper(ProveedorMapper.class);
	
	@Mappings({
		  @Mapping(target="nombre", source = "nomYnifEntity.nombre"),
		  @Mapping(target="nif", source = "nomYnifEntity.nif"),
		  @Mapping(target="direccion", source = "dirYcpEntity.direccion"),
		  @Mapping(target ="cpPoblacionProvincia", expression = "java(getCpPoblacionProvincia(dirYcpEntity))")
		  })
	ProveedorDTO map (NombreYNifProveedorEntity nomYnifEntity, DireccionYcpProveedorEntity dirYcpEntity);

	default String getCpPoblacionProvincia(DireccionYcpProveedorEntity dirYcpEntity) {
		StringBuilder str = new StringBuilder();
		
		str.append(dirYcpEntity.getCodigoPostal());
		str.append(" ");
		str.append(dirYcpEntity.getPoblacion());
		str.append(" ");
		str.append(dirYcpEntity.getDescripcion());
		
		return str.toString();
	}
}
