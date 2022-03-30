/**
 * 
 */
package es.eroski.azoka.mapper;

import java.util.List;
import java.util.Locale;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

import es.eroski.azoka.dto.ResumenIvaDTO;
import es.eroski.azoka.model.ResumenIvaEntity;
import es.eroski.azoka.utils.NumeroUtils;

/**
 * @author BICUGUAL
 *
 */
@Mapper(componentModel = "spring")
public interface ResumenIvaMapper {

	ResumenIvaMapper INSTANCE = Mappers.getMapper(ResumenIvaMapper.class);

	List<ResumenIvaDTO> map(List<ResumenIvaEntity> entity);

	default ResumenIvaDTO map(ResumenIvaEntity entity) {
		
		Locale locale = LocaleContextHolder.getLocale();

		return ResumenIvaDTO.builder()
				.descripcion((entity.getDescripcion()))
				.porcentaje(NumeroUtils.formatNumberToStringByLocale(entity.getPorcentaje(),locale))
				.baseImp(NumeroUtils.formatNumberToStringByLocale(entity.getBaseImp(),locale))
				.cuotaIva(NumeroUtils.formatNumberToStringByLocale(entity.getCuotaIva(),locale))
				.totalAPagar(NumeroUtils.formatNumberToStringByLocale(entity.getTotalAPagar(),locale))
				.build();
		
	}
	
}