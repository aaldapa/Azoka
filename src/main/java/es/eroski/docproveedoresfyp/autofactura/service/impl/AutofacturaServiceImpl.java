/**
 * 
 */
package es.eroski.docproveedoresfyp.autofactura.service.impl;

import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.eroski.docproveedoresfyp.autofactura.persistence.AutofacturaRepository;
import es.eroski.docproveedoresfyp.autofactura.service.AutofacturaService;
import es.eroski.docproveedoresfyp.dto.AlbaranDTO;
import es.eroski.docproveedoresfyp.dto.CodigoQrDTO;
import es.eroski.docproveedoresfyp.dto.ParametrosCabeceraDTO;
import es.eroski.docproveedoresfyp.dto.ProveedorDTO;
import es.eroski.docproveedoresfyp.dto.ResumenIvaDTO;
import es.eroski.docproveedoresfyp.dto.RetencionDTO;
import es.eroski.docproveedoresfyp.dto.SociedadDTO;
import es.eroski.docproveedoresfyp.mapper.AlbaranMapper;
import es.eroski.docproveedoresfyp.mapper.CodigoQrMapper;
import es.eroski.docproveedoresfyp.mapper.ParametrosCabeceraMapper;
import es.eroski.docproveedoresfyp.mapper.ProveedorMapper;
import es.eroski.docproveedoresfyp.mapper.ResumenIvaMapper;
import es.eroski.docproveedoresfyp.mapper.RetencionMapper;
import es.eroski.docproveedoresfyp.mapper.SociedadMapper;
import es.eroski.docproveedoresfyp.model.DireccionYcpProveedorEntity;
import es.eroski.docproveedoresfyp.model.NombreYNifProveedorEntity;
import es.eroski.docproveedoresfyp.utils.NumeroUtils;

/**
 * Implementacion del servicio
 * 
 * @author BICUGUAL
 *
 */
@Service
public class AutofacturaServiceImpl implements AutofacturaService {

	private static Logger logger = LogManager.getLogger(AutofacturaServiceImpl.class);

	@Autowired
	AutofacturaRepository repository;

	/**
	 * Obtiene los datos del proveedor en base al codProveedor
	 * 
	 * @param codProveedor
	 * @return
	 */
	@Override
	public ProveedorDTO getProveedorDTO(Long codProveedor) {

		NombreYNifProveedorEntity nomYnifEntity = repository.getNombreByCodProveedor(codProveedor);

		if (null != nomYnifEntity) {
			DireccionYcpProveedorEntity dirYcpEntity = repository.getDireccionByCodProveedor(codProveedor);

			if (null != dirYcpEntity) {
				ProveedorDTO proveedorDTO = ProveedorMapper.INSTANCE.map(nomYnifEntity, dirYcpEntity);

				logger.info("Datos de Proveedor : {}", proveedorDTO);

				return proveedorDTO;
			}

		}

		return null;

	}

	/**
	 * Obtiene los datos de la sociedad en base al codSociedad
	 * 
	 * @param codSociedad
	 * @return
	 */
	@Override
	public SociedadDTO getSociedadDTO(Integer codSociedad) {

		SociedadDTO sociedadDto = SociedadMapper.INSTANCE.map(repository.getSociedadByCodSociedad(codSociedad));

		logger.info("Datos de Sociedad: {}", sociedadDto);

		return sociedadDto;
	}

	/**
	 * Obtiene el codigo del documento con los parametros de entrada al end-point
	 * 
	 * @param codProveedor
	 * @param numDocumento
	 * @param year
	 * @param codSociedad
	 * @return
	 */
	@Override
	public Long getCodDocumento(Long codProveedor, String numDocumento, int year, Integer codSociedad) {

		Long codDocumento = repository.getCodDocumento(codProveedor, numDocumento, year, codSociedad);

		logger.info("Codigo de documento: {}", codDocumento);

		return codDocumento;
	}

	/**
	 * Obtiene los datos con los que cargar la tabla inicial de cabecera del reporte
	 * 
	 * @param codDocumento
	 * @return
	 */
	@Override
	public ParametrosCabeceraDTO getParametrosCabeceraDTO(Long codDocumento) {

		ParametrosCabeceraDTO parametrosCabeceraDTO = ParametrosCabeceraMapper.INSTANCE
				.map(repository.getHeaderParameters(codDocumento));

		logger.info("Parametros de cabecera: {}", parametrosCabeceraDTO, toString());

		return parametrosCabeceraDTO;
	}

	/**
	 * Obtiene la lista de registros de albaran
	 * 
	 * @param codDocumento
	 * @return
	 */
	@Override
	public List<AlbaranDTO> getLstAlbaranesDTO(Long codDocumento) {

		List<AlbaranDTO> lstDTO = AlbaranMapper.INSTANCE.map(repository.getAlbaranByCodDocumento(codDocumento));
		lstDTO.stream().forEach((dto) -> logger.info(dto.toString()));
		lstDTO.get(0).setAlbProvr("099897687125");
		return lstDTO;
	}

	/**
	 * Obtiene la lista de resumentes de tipo de iva
	 * 
	 * @param codDocumento
	 * @return
	 */
	@Override
	public List<ResumenIvaDTO> getLstResumenesIvaDTO(Long codDocumento) {

		List<ResumenIvaDTO> lstDTO = ResumenIvaMapper.INSTANCE
				.map(repository.getResumenTiposIvaByCodDocumento(codDocumento));

		lstDTO.stream().forEach((dto) -> logger.info(dto.toString()));

		return lstDTO;
	}

	/**
	 * En base a una lista de resumenes, obtiene el sumatorio total de la factura.
	 * 
	 * @param lstResumenesIvaDTO
	 * @param locale
	 * @return
	 */
	@Override
	public String getSumTotalFactura(List<ResumenIvaDTO> lstResumenesIvaDTO, Locale locale) {

		Double sumTotalFactura = lstResumenesIvaDTO.stream().mapToDouble(
				(entity) -> Double.valueOf(NumeroUtils.formatStringToDoubleByLocale(entity.getTotalAPagar(), locale)))
				.sum();

		String sumTotalFacturaStr = NumeroUtils.formatNumberToStringByLocale(Float.valueOf(sumTotalFactura.toString()),
				locale);

		logger.info("Sumatorio factura: {}", sumTotalFacturaStr);

		return sumTotalFacturaStr;

	}

	/**
	 * Obtiene los datos a mostrar en la tabla de retencion del reporte
	 * 
	 * @param codDocumento
	 * @return
	 */
	@Override
	public RetencionDTO getRetencionDTO(Long codDocumento) {

		RetencionDTO retencionDTO = RetencionMapper.INSTANCE.map(repository.getRetencionByCodDocumento(codDocumento));

		logger.info("Datos de retencion: {}", retencionDTO);

		return retencionDTO;
	}

	/**
	 * Obtiene el codigo Qr y el tbai para mostrarlo en el reporte
	 * 
	 * @param codDocumento
	 * @return
	 */
	@Override
	public CodigoQrDTO getCodigoQr(Long codDocumento) {

		CodigoQrDTO dto = CodigoQrMapper.INSTANCE.map(repository.getCodigoQr(codDocumento));

		logger.info("Datos de codigoQr: {}", dto);

		return dto;
	}

}
