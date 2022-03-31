/**
 * 
 */
package es.eroski.docproveedoresfyp.autofactura.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import es.eroski.docproveedoresfyp.autofactura.persistence.AutofacturaRepository;
import es.eroski.docproveedoresfyp.autofactura.service.AutofacturaService;
import es.eroski.docproveedoresfyp.dto.AlbaranDTO;
import es.eroski.docproveedoresfyp.dto.CodigoQrDTO;
import es.eroski.docproveedoresfyp.dto.ParametrosCabeceraDTO;
import es.eroski.docproveedoresfyp.dto.ProveedorDTO;
import es.eroski.docproveedoresfyp.dto.ResumenIvaDTO;
import es.eroski.docproveedoresfyp.dto.RetencionDTO;
import es.eroski.docproveedoresfyp.dto.SociedadDTO;
import es.eroski.docproveedoresfyp.exceptions.AutofacturaNotFoundException;
import es.eroski.docproveedoresfyp.mapper.AlbaranMapper;
import es.eroski.docproveedoresfyp.mapper.CodigoQrMapper;
import es.eroski.docproveedoresfyp.mapper.ParametrosCabeceraMapper;
import es.eroski.docproveedoresfyp.mapper.ProveedorMapper;
import es.eroski.docproveedoresfyp.mapper.ResumenIvaMapper;
import es.eroski.docproveedoresfyp.mapper.RetencionMapper;
import es.eroski.docproveedoresfyp.mapper.SociedadMapper;
import es.eroski.docproveedoresfyp.model.CodigoQrEntity;
import es.eroski.docproveedoresfyp.model.DireccionYcpProveedorEntity;
import es.eroski.docproveedoresfyp.model.NombreYNifProveedorEntity;
import es.eroski.docproveedoresfyp.utils.JasperUtils;
import es.eroski.docproveedoresfyp.utils.NumeroUtils;
import es.eroski.docproveedoresfyp.utils.Utils;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
 * Implementacion del servicio
 * @author BICUGUAL
 *
 */
@Service
public class AutofacturaServiceImpl implements AutofacturaService {

	private static Logger logger = LogManager.getLogger(AutofacturaServiceImpl.class);

	@Autowired
	Environment env;

	@Autowired
	ProveedorMapper proveedorMapper;

	@Autowired
	ParametrosCabeceraMapper parametrosCabeceraMapper;

	@Autowired
	SociedadMapper sociedadMapper;

	@Autowired
	AlbaranMapper albaranMapper;

	@Autowired
	ResumenIvaMapper resumenIvaMapper;

	@Autowired
	RetencionMapper retencionMapper;

	@Value("${path.autofactura.template}")
	private String jrxmlTemplatePath;

	@Value("${path.logo.eroski}")
	private String logoEroskiPath;

	@Value("${path.autofactura.icc}")
	private String iccPath;

	@Autowired
	AutofacturaRepository repository;

	@Override
	public byte[] generateJasperReportPDF(Locale locale, Long codProveedor, String numDocumento, Integer year,
			Integer codSociedad) {

		// Obtener los datos a cargar en el reporte
		Map<String, Object> parameters = this.getReportParameters(locale, codProveedor, numDocumento, year,
				codSociedad);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Poner en contexto las fuentes embebidas
		JasperUtils.setJasperReportsContext();

		try {
			// Obtener reporte compilado
			JasperReport reportMain = JasperUtils.reportCompile(jrxmlTemplatePath);

			// Generar el reporte
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportMain, parameters, new JREmptyDataSource());

			// Obtener configuracion para la exportacion del reporte
			SimplePdfExporterConfiguration exportConfig = JasperUtils.getPdfExporterConfiguration(locale, iccPath,
					env.getProperty("report.pdf.metadata.autor"),
					Utils.fileNameContructor(codProveedor, numDocumento, year));

			// Exportar el reporte
			JasperUtils.exportarPDF(jasperPrint, outputStream, exportConfig);

		} catch (FileNotFoundException | JRException e) {
			logger.error(e);
			e.printStackTrace();
		}

		return outputStream.toByteArray();

	}

	/**
	 * Obtiene los datos necesarios para cargar el reporte
	 * 
	 * @param locale
	 * @param codProveedor
	 * @param numDocumento
	 * @param year
	 * @param codSociedad
	 * @return
	 */
	private Map<String, Object> getReportParameters(Locale locale, Long codProveedor, String numDocumento, Integer year,
			Integer codSociedad) {

		ResourceBundle bundle = ResourceBundle.getBundle("i18n/messages", locale);

		Long codDocumento = this.getCodDocumento(codProveedor, numDocumento, year, codSociedad);

		// Si no se localiza un numero de documento, se retorna null
		if (null != codDocumento) {

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("REPORT_RESOURCE_BUNDLE", bundle);
			parameters.put(JRParameter.REPORT_LOCALE, locale);

			parameters.put("logoEroskiPath", this.getLogoEroski(locale));
			parameters.put("proveedor", this.getProveedorDTO(codProveedor));
			parameters.put("sociedad", this.getSociedadDTO(codSociedad));

			// Tabla1
			parameters.put("parametrosCabecera", this.getParametrosCabeceraDTO(codDocumento));

			// Tabla2
			parameters.put("lstAlbaranes", this.getLstAlbaranesDTO(codDocumento));

			// Tabla3
			List<ResumenIvaDTO> lstResumenesIvaDTO = this.getLstResumenesIvaDTO(codDocumento);
			parameters.put("lstResumenesIva", lstResumenesIvaDTO);

			// Tabla 4
			parameters.put("sumaTotalFactura", this.getSumTotalFactura(lstResumenesIvaDTO, locale));

			// Tabla 5
			parameters.put("retencion", this.getRetencionDTO(codDocumento));

			// qr
			parameters.put("qr", this.getCodigoQr(codDocumento)); 
			
			return parameters;
			
		} else {
			throw new AutofacturaNotFoundException("Los parametros introducidos no devuelven resultatos");
		}

	}

	/**
	 * Obtiene la imagen del logotipo de Eroski almacenada en los recursos del
	 * proyecto en funcion de la locale.
	 * 
	 * La propiedad path.logo.eroski del properties debe ser configurada con el
	 * formato images/nombre_de_la_imagen_xx.ext donde xx se corresponde con el
	 * idioma.
	 * 
	 * En el proceso de obtencion del logo, se sustituyen la xx por los valores
	 * de la locale.toString()
	 * 
	 * @param locale
	 * @return
	 */
	private byte[] getLogoEroski(Locale locale) {

		File logoEroski;
		byte[] fileContent = null;

		try {
			String idiomaLogo = "es";
			
			if (null != locale && "eu".equals(locale.getLanguage())){
				idiomaLogo = "eu";
			}
			
			logoEroski = ResourceUtils.getFile(
					ResourceUtils.CLASSPATH_URL_PREFIX.concat(logoEroskiPath.replace("xx", idiomaLogo)));
			fileContent = Files.readAllBytes(logoEroski.toPath());

		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}

		return fileContent;

	}

	/**
	 * Obtiene los datos del proveedor en base al codProveedor
	 * 
	 * @param codProveedor
	 * @return
	 */
	private ProveedorDTO getProveedorDTO(Long codProveedor) {

		NombreYNifProveedorEntity nomYnifEntity = repository.getNombreByCodProveedor(codProveedor);

		if (null != nomYnifEntity) {
			DireccionYcpProveedorEntity dirYcpEntity = repository.getDireccionByCodProveedor(codProveedor);

			if (null != dirYcpEntity) {
				ProveedorDTO proveedorDTO = proveedorMapper.map(nomYnifEntity, dirYcpEntity);

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
	private SociedadDTO getSociedadDTO(Integer codSociedad) {

		SociedadDTO sociedadDto = sociedadMapper.map(repository.getSociedadByCodSociedad(codSociedad));

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
	private Long getCodDocumento(Long codProveedor, String numDocumento, int year, Integer codSociedad) {

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
	private ParametrosCabeceraDTO getParametrosCabeceraDTO(Long codDocumento) {

		ParametrosCabeceraDTO parametrosCabeceraDTO = parametrosCabeceraMapper
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
	private List<AlbaranDTO> getLstAlbaranesDTO(Long codDocumento) {

		List<AlbaranDTO> lstDTO = albaranMapper.map(repository.getAlbaranByCodDocumento(codDocumento));
//			AlbaranMapper.INSTANCE.map(repository.getAlbaranByCodDocumento(codDocumento));
		lstDTO.stream().forEach((dto) -> logger.info(dto.toString()));

		return lstDTO;
	}

	/**
	 * Obtiene la lista de resumentes de tipo de iva
	 * 
	 * @param codDocumento
	 * @return
	 */
	private List<ResumenIvaDTO> getLstResumenesIvaDTO(Long codDocumento) {

		List<ResumenIvaDTO> lstDTO = resumenIvaMapper.map(repository.getResumenTiposIvaByCodDocumento(codDocumento));

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
	private String getSumTotalFactura(List<ResumenIvaDTO> lstResumenesIvaDTO, Locale locale) {

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
	private RetencionDTO getRetencionDTO(Long codDocumento) {

		RetencionDTO retencionDTO = retencionMapper.map(repository.getRetencionByCodDocumento(codDocumento));

		logger.info("Datos de retencion: {}", retencionDTO);

		return retencionDTO;
	}


	/**
	 * Obtiene el codigo Qr y el tbai para mostrarlo en el reporte
	 * @param codDocumento
	 * @return
	 */
	private CodigoQrDTO getCodigoQr(Long codDocumento) {
		
		CodigoQrDTO dto = CodigoQrMapper.INSTANCE.map(repository.getCodigoQr(codDocumento));
		
		logger.info("Datos de codigoQr: {}", dto);
		
		return dto; 
	}

}
