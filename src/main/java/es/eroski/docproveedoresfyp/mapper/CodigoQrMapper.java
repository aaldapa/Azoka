/**
 * 
 */
package es.eroski.docproveedoresfyp.mapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import es.eroski.docproveedoresfyp.dto.CodigoQrDTO;
import es.eroski.docproveedoresfyp.model.CodigoQrEntity;

/**
 * @author BICUGUAL
 *
 */
@Mapper(componentModel = "spring")
public interface CodigoQrMapper {

	static Logger logger = LogManager.getLogger(CodigoQrMapper.class);
	
	CodigoQrMapper INSTANCE = Mappers.getMapper(CodigoQrMapper.class);

	@Mappings({ @Mapping(target = "idTbai", source = "idTbai"),
			@Mapping(target = "imagenQr", expression = "java(parseImagenQr(entity))") })

	CodigoQrDTO map(CodigoQrEntity entity);

	default InputStream parseImagenQr(CodigoQrEntity entity) {

		InputStream imagenQrStream = null;
		
		try {
			if (null != entity.getImagenQr()) {
				String imagenQrStr = entity.getImagenQr().getSubString(1, (int) entity.getImagenQr().length());
				imagenQrStream = new ByteArrayInputStream(
						org.apache.tomcat.util.codec.binary.Base64.decodeBase64(imagenQrStr.getBytes()));
			}

		} catch (SQLException e) {

			e.printStackTrace();
			logger.error(e);
		}
		
		return imagenQrStream;

	}

}
