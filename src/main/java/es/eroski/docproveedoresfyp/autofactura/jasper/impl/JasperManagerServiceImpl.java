/**
 * 
 */
package es.eroski.docproveedoresfyp.autofactura.jasper.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import es.eroski.docproveedoresfyp.autofactura.jasper.JasperManagerService;
import es.eroski.docproveedoresfyp.autofactura.service.AutofacturaService;
import es.eroski.docproveedoresfyp.dto.ResumenIvaDTO;
import es.eroski.docproveedoresfyp.exceptions.AutofacturaNotFoundException;
import es.eroski.docproveedoresfyp.utils.JasperUtils;
import es.eroski.docproveedoresfyp.utils.Utils;
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
@Component
public class JasperManagerServiceImpl implements JasperManagerService{

	private static Logger logger = LogManager.getLogger(JasperManagerServiceImpl.class);
	
	@Value("${path.autofactura.template}")
	private String jrxmlTemplatePath;

	@Value("${path.logo.eroski}")
	private String logoEroskiPath;
	
	@Value("${path.autofactura.icc}")
	private String iccPath;
	
	@Autowired
	Environment env;
	
	@Autowired
	AutofacturaService autoFacturaService;
	
	
	
	@Override
	public byte[] getAutofacturaReport(Long codProveedor, String numDocumento, Integer year,
			Integer codSociedad) {
		
		//Obtener la locale
		Locale locale = LocaleContextHolder.getLocale();
		
		// Obtener los datos a cargar en el reporte
		Map<String, Object> parameters = this.getAutofacturaParameters(locale, codProveedor, numDocumento, year,
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
		} catch (Exception e) {
			logger.error(e);
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
	private Map<String, Object> getAutofacturaParameters(Locale locale, Long codProveedor, String numDocumento, Integer year,
			Integer codSociedad) {

		ResourceBundle bundle = ResourceBundle.getBundle("i18n/messages", locale);

		Long codDocumento = autoFacturaService.getCodDocumento(codProveedor, numDocumento, year, codSociedad);

		// Si no se localiza un numero de documento, se retorna null
		if (null != codDocumento) {

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("REPORT_RESOURCE_BUNDLE", bundle);
			parameters.put(JRParameter.REPORT_LOCALE, locale);

			parameters.put("logoEroskiPath", this.getLogoEroski(locale));
			parameters.put("proveedor", autoFacturaService.getProveedorDTO(codProveedor));
			parameters.put("sociedad", autoFacturaService.getSociedadDTO(codSociedad));

			// Tabla1
			parameters.put("parametrosCabecera", autoFacturaService.getParametrosCabeceraDTO(codDocumento));

			// Tabla2
			parameters.put("lstAlbaranes", autoFacturaService.getLstAlbaranesDTO(codDocumento));
			

			// Tabla3
			List<ResumenIvaDTO> lstResumenesIvaDTO = autoFacturaService.getLstResumenesIvaDTO(codDocumento);
			parameters.put("lstResumenesIva", lstResumenesIvaDTO);

			// Tabla 4
			parameters.put("sumaTotalFactura", autoFacturaService.getSumTotalFactura(lstResumenesIvaDTO, locale));

			// Tabla 5
			parameters.put("retencion", autoFacturaService.getRetencionDTO(codDocumento));

			// qr
			parameters.put("qr", autoFacturaService.getCodigoQr(codDocumento)); 
			
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
		}

		return fileContent;

	}
	
}
