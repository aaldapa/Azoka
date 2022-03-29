/**
 * 
 */
package es.eroski.azoka.autofactura.service.impl;

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

import es.eroski.azoka.autofactura.persistence.AutofacturaRepository;
import es.eroski.azoka.autofactura.service.AutofacturaService;
import es.eroski.azoka.dto.AlbaranDTO;
import es.eroski.azoka.dto.ParametrosCabeceraDTO;
import es.eroski.azoka.dto.ProveedorDTO;
import es.eroski.azoka.dto.ResumenIvaDTO;
import es.eroski.azoka.dto.RetencionDTO;
import es.eroski.azoka.dto.SociedadDTO;
import es.eroski.azoka.exceptions.AutofacturaException;
import es.eroski.azoka.mapper.AlbaranMapper;
import es.eroski.azoka.mapper.ParametrosCabeceraMapper;
import es.eroski.azoka.mapper.ProveedorMapper;
import es.eroski.azoka.mapper.ResumenIvaMapper;
import es.eroski.azoka.mapper.RetencionMapper;
import es.eroski.azoka.mapper.SociedadMapper;
import es.eroski.azoka.model.DireccionYcpProveedorEntity;
import es.eroski.azoka.model.NombreYNifProveedorEntity;
import es.eroski.azoka.utils.JasperUtils;
import es.eroski.azoka.utils.NumeroUtils;
import es.eroski.azoka.utils.Utils;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
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
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Poner en contexto las fuentes embebidas
		JasperUtils.setJasperReportsContext();

		try {
			// Obtener reporte compilado
			JasperReport reportMain = JasperUtils.reportCompile(jrxmlTemplatePath);

			// Obtener los datos a cargar en el reporte
			Map<String, Object> parameters = this.getReportParameters(locale, codProveedor, numDocumento, year,
					codSociedad);

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

	private Map<String, Object> getReportParameters(Locale locale, Long codProveedor, String numDocumento, Integer year,
			Integer codSociedad) {

		ResourceBundle bundle = ResourceBundle.getBundle("i18n/messages", locale);

		Long codDocumento = this.getCodDocumento(codProveedor, numDocumento, year, codSociedad);

		if (null == codDocumento) {
			throw new AutofacturaException("No se ha encontrado codigo de documento para los parametros facilitados");
		}

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

		parameters.put("imagenQr", this.getImagenQrStream(codDocumento));

		return parameters;
	}

	private byte[] getLogoEroski(Locale locale) {

		File logoEroski;
		byte[] fileContent = null;

		try {
			logoEroski = ResourceUtils.getFile(
					ResourceUtils.CLASSPATH_URL_PREFIX.concat(logoEroskiPath.replace("xx_XX", locale.toString())));
			fileContent = Files.readAllBytes(logoEroski.toPath());

		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}

		return fileContent;

	}

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

	private SociedadDTO getSociedadDTO(Integer codSociedad) {

		SociedadDTO sociedadDto = sociedadMapper.map(repository.getSociedadByCodSociedad(codSociedad));

		logger.info("Datos de Sociedad: {}", sociedadDto);

		return sociedadDto;
	}

	private Long getCodDocumento(Long codProveedor, String numDocumento, int year, Integer codSociedad) {

		Long codDocumento = repository.getCodDocumento(codProveedor, numDocumento, year, codSociedad);

		logger.info("Codigo de documento: {}", codDocumento);

		return codDocumento;
	}

	private ParametrosCabeceraDTO getParametrosCabeceraDTO(Long codDocumento) {

		ParametrosCabeceraDTO parametrosCabeceraDTO = parametrosCabeceraMapper
				.map(repository.getHeaderParameters(codDocumento));

		logger.info("Parametros de cabecera: {}", parametrosCabeceraDTO, toString());

		return parametrosCabeceraDTO;
	}

	private List<AlbaranDTO> getLstAlbaranesDTO(Long codDocumento) {

		List<AlbaranDTO> lstDTO = albaranMapper.map(repository.getAlbaranByCodDocumento(codDocumento));
//			AlbaranMapper.INSTANCE.map(repository.getAlbaranByCodDocumento(codDocumento));
		lstDTO.stream().forEach((dto) -> logger.info(dto.toString()));

		return lstDTO;
	}

	private List<ResumenIvaDTO> getLstResumenesIvaDTO(Long codDocumento) {

		List<ResumenIvaDTO> lstDTO = resumenIvaMapper.map(repository.getResumenTiposIvaByCodDocumento(codDocumento));

		lstDTO.stream().forEach((dto) -> logger.info(dto.toString()));

		return lstDTO;
	}

	private String getSumTotalFactura(List<ResumenIvaDTO> lstResumenesIvaDTO, Locale locale) {

		Double sumTotalFactura = lstResumenesIvaDTO.stream().mapToDouble(
				(entity) -> Double.valueOf(NumeroUtils.formatStringToDoubleByLocale(entity.getTotalAPagar(), locale)))
				.sum();

		String sumTotalFacturaStr = NumeroUtils.formatNumberToStringByLocale(Float.valueOf(sumTotalFactura.toString()),
				locale);

		logger.info("Sumatorio factura: {}", sumTotalFacturaStr);

		return sumTotalFacturaStr;

	}

	private RetencionDTO getRetencionDTO(Long codDocumento) {

		RetencionDTO retencionDTO = retencionMapper.map(repository.getRetencionByCodDocumento(codDocumento));

		logger.info("Datos de retencion: {}", retencionDTO);

		return retencionDTO;
	}

	private InputStream getImagenQrStream(Long codDocumento) {

		Clob imagenQrClob = repository.getImagenQr();
		String imagenQrStr = null;
		InputStream imagenQrStream = null;
		try {

			imagenQrStr = imagenQrClob.getSubString(1, (int) imagenQrClob.length());
			imagenQrStream = new ByteArrayInputStream(
					org.apache.tomcat.util.codec.binary.Base64.decodeBase64(imagenQrStr.getBytes()));

			return imagenQrStream;

		} catch (SQLException e) {

			e.printStackTrace();
			logger.error(e);

			return null;
		}

	}

}
