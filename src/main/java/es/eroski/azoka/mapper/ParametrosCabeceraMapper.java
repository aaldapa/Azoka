/**
 * 
 */
package es.eroski.azoka.mapper;

import java.util.Locale;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

import es.eroski.azoka.dto.ParametrosCabeceraDTO;
import es.eroski.azoka.model.ParametrosCabeceraEntity;
import es.eroski.azoka.utils.FechaUtils;

/**
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
