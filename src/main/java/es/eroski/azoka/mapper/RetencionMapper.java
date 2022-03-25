/**
 * 
 */
package es.eroski.azoka.mapper;

import java.util.Locale;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

import es.eroski.azoka.dto.RetencionDTO;
import es.eroski.azoka.model.RetencionEntity;
import es.eroski.azoka.utils.NumeroUtils;

/**
 * @author BICUGUAL
 *
 */
@Mapper(componentModel = "spring")
public interface RetencionMapper {

	
	RetencionMapper INSTANCE = Mappers.getMapper(RetencionMapper.class);

	default RetencionDTO map(RetencionEntity entity) {
		Locale locale = LocaleContextHolder.getLocale();
		
		return RetencionDTO.builder()
				.porcRetencion(NumeroUtils.formatNumberToStringByLocale(entity.getPorcRetencion(), locale))
				.baseIrpf(NumeroUtils.formatNumberToStringByLocale(entity.getBaseIrpf(), locale))
				.impIrpf(NumeroUtils.formatNumberToStringByLocale(entity.getImpIrpf(), locale))
				.importe(NumeroUtils.formatNumberToStringByLocale(entity.getImporte(), locale))
				.build();
	}
	
}
