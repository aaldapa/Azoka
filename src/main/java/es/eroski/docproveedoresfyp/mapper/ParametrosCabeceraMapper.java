/**
 * 
 */
package es.eroski.docproveedoresfyp.mapper;

import java.util.Locale;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

import es.eroski.docproveedoresfyp.dto.ParametrosCabeceraDTO;
import es.eroski.docproveedoresfyp.model.ParametrosCabeceraEntity;
import es.eroski.docproveedoresfyp.utils.FechaUtils;

/**
 * Mappeo de Entidad a DTO de Parametros de cabecera
 * @author BICUGUAL
 *
 */
@Mapper(componentModel = "spring")
public interface ParametrosCabeceraMapper {

	ParametrosCabeceraMapper INSTANCE = Mappers.getMapper(ParametrosCabeceraMapper.class);

	default ParametrosCabeceraDTO map(ParametrosCabeceraEntity entity) {
		Locale locale = LocaleContextHolder.getLocale();

		return ParametrosCabeceraDTO.builder().numDocDot(entity.getNumDocDot())
				.fecFactura(FechaUtils.formatDateToStringByLocale(entity.getFecFactura(), locale))
				.fecDesdeHasta(FechaUtils.formatDateToStringByLocale(entity.getFecDesde(), locale).concat(" - ")
						.concat(FechaUtils.formatDateToStringByLocale(entity.getFecHasta(), locale)))
				.build();
	}

}
