/**
 * 
 */
package es.eroski.docproveedoresfyp.mapper;

import java.util.List;
import java.util.Locale;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

import es.eroski.docproveedoresfyp.dto.AlbaranDTO;
import es.eroski.docproveedoresfyp.model.AlbaranEntity;
import es.eroski.docproveedoresfyp.utils.FechaUtils;
import es.eroski.docproveedoresfyp.utils.NumeroUtils;

/**
 * Mappeo de Entidad a DTO de Albaran
 * @author BICUGUAL
 *
 */
@Mapper(componentModel = "spring")
public interface AlbaranMapper {

	AlbaranMapper INSTANCE = Mappers.getMapper(AlbaranMapper.class);

	List<AlbaranDTO> map(List<AlbaranEntity> entity);

	default AlbaranDTO map(AlbaranEntity entity) {
		
		Locale locale = LocaleContextHolder.getLocale();
		
		return AlbaranDTO.builder().albProvr(entity.getAlbProvr())
				.fecEntrSal(
						FechaUtils.formatDateToStringByLocale(entity.getFecEntrSal(), locale))
				.denomInforme(entity.getDenomInforme())
				.baseImp(NumeroUtils.formatNumberToStringByLocale(entity.getBaseImp(),locale))
				.tipoIva(NumeroUtils.formatNumberToStringByLocale(entity.getTipoIva(),locale))
				.cuotaIva(NumeroUtils.formatNumberToStringByLocale(entity.getCuotaIva(),locale))
				.importeTotal(NumeroUtils.formatNumberToStringByLocale(entity.getImporteTotal(), locale))
				.build();
	}

}
