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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import es.eroski.azoka.autofactura.persistence.AutofacturaRepository;
import es.eroski.azoka.autofactura.service.AutofacturaService;
import es.eroski.azoka.dto.AlbaranDTO;
import es.eroski.azoka.dto.Alumno;
import es.eroski.azoka.dto.Persona;
import es.eroski.azoka.dto.ResumenIvaDTO;
import es.eroski.azoka.dto.RetencionDTO;
import es.eroski.azoka.exceptions.AutofacturaException;
import es.eroski.azoka.mapper.AlbaranMapper;
import es.eroski.azoka.mapper.ResumenIvaMapper;
import es.eroski.azoka.mapper.RetencionMapper;
import es.eroski.azoka.model.ParametrosCabeceraEntity;
import es.eroski.azoka.model.ResumenIvaEntity;
import es.eroski.azoka.utils.FechaUtils;
import es.eroski.azoka.utils.NumeroUtils;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.type.PdfVersionEnum;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;

/**
 * @author BICUGUAL
 *
 */
@Service
public class AutofacturaServiceImpl implements AutofacturaService {

	private static Logger logger = LogManager.getLogger(AutofacturaServiceImpl.class);

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	private Environment env;
	
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
	public byte[] getImagenQR() {

		Clob clob = null;
		clob = repository.getImagenQr();

		try {
			return clob.getSubString(1, (int) clob.length()).getBytes();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		try{
//            InputStream ins = clob.getAsciiStream();
//            byte[] byteArray = ins.readAllBytes();
//            System.out.println(new String(byteArray));
//            ins.close();
//            return byteArray;
//        }catch(IOException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		try {
//			return IOUtils.toByteArray(clob.getCharacterStream(), "UTF-8");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return null;

//		byte[] blobAsBytes = null;
//
//		int blobLength;
//		try {
//			blobLength = (int) blob.length();
//			blobAsBytes = blob.getBytes(1, blobLength);
//			//release the blob and free up memory. (since JDBC 4.0)
//			blob.free();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//
//		return blobAsBytes;

	}

	@Override
	public Clob getImagenQRClob() {
		Clob clob = repository.getImagenQr();

//		try {
//			System.out.println("Hello ! " + clob + " length=" + clob.length());
//			System.out.println("Hello ! " + clob.getSubString(1, (int) clob.length()));
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return clob;
	}

	@Override
	public byte[] generateJasperReportPDF(Locale locale, Long codProveedor, String numDocumento, Integer year,
			Integer codSociedad) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Poner en contexto las fuentes embebidas
		this.setJasperReportsContext();

		try {
			// Obtener reporte compilado
			JasperReport reportMain = this.reportCompile();

			// Obtener los datos a cargar en el reporte
			Map<String, Object> parameters = this.getReportParameters(locale, codProveedor, numDocumento, year,
					codSociedad);

			// Generar el reporte
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportMain, parameters, new JREmptyDataSource());

			// Obtener configuracion para la exportacion del reporte
			SimplePdfExporterConfiguration exportConfig = this.getPdfExporterConfiguration(locale);

			// Exportar el reporte
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			exporter.setConfiguration(exportConfig);
			exporter.exportReport();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}

	private void setJasperReportsContext() {
		JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();

		jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.font.name",
				"net/sf/jasperreports/fonts/dejavu/DejaVuSans.ttf");
		jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.embedded", "true");
	}

	private JasperReport reportCompile() throws FileNotFoundException, JRException {

		String reportHomePath = "src/main/resources/reports/";
		File reportMainTemplate = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX.concat(jrxmlTemplatePath));

		// Compilar
		JasperReport reportMain = JasperCompileManager.compileReport(reportMainTemplate.getAbsolutePath());

		// Guardar el compilado
		JRSaver.saveObject(reportMain, reportHomePath.concat(reportMainTemplate.getName().replace("jrxml", "jasper")));

		return reportMain;

	}

	private Map<String, Object> getReportParameters(Locale locale, Long codProveedor, String numDocumento, Integer year,
			Integer codSociedad) {

		ResourceBundle bundle = ResourceBundle.getBundle("i18n/messages", locale);
		List<Persona> lstPersonas = Arrays.asList(new Persona(1, "Elena", "Nunez"), new Persona(2, "Jose", "Lema"),
				new Persona(3, "Endika", "Campo"), new Persona(4, "Roberto", "Castanedo"));
		List<Alumno> lstAlumnos = Arrays.asList(new Alumno(1, "Alberto", "Cuesta"), new Alumno(2, "Roberto", "Agirre"));

		Long codDocumento = repository.getCodDocumento(codProveedor, numDocumento, year, codSociedad);

		if (null == codDocumento) {
			throw new AutofacturaException("No se ha encontrado codigo de documento para los parametros facilitados");
		}

		ParametrosCabeceraEntity headerParams = repository.getHeaderParameters(codDocumento);
		
		List<AlbaranDTO> lstAlbaranesDTO = albaranMapper.map(repository.getAlbaranByCodDocumento(codDocumento));
//		AlbaranMapper.INSTANCE.map(repository.getAlbaranByCodDocumento(codDocumento));
		
		
		List<ResumenIvaEntity> lstResumenesIvaEntity = repository.getResumenTiposIvaByCodDocumento(codDocumento);
		List<ResumenIvaDTO> lstResumenesIvaDTO = resumenIvaMapper.map(lstResumenesIvaEntity);
		
		lstAlbaranesDTO.stream().forEach((dto) -> logger.info(dto.toString()));
		lstResumenesIvaDTO.stream().forEach((dto) -> logger.info(dto.toString()));
		
		
		Double sumTotalFactura = lstResumenesIvaEntity.stream().mapToDouble((entity) -> entity.getTotalAPagar()).sum();
		logger.info("Sumatorio factura: {}",NumeroUtils.formatNumberToStringByLocale(Float.valueOf(sumTotalFactura.toString()), locale));
		
		
		RetencionDTO retencionDTO = retencionMapper.map(repository.getRetencionByCodDocumento(codDocumento));
		
		
		Map<String, Object> parameters = new HashMap<>();

		parameters.put("REPORT_RESOURCE_BUNDLE", bundle);
		parameters.put(JRParameter.REPORT_LOCALE, locale);
		parameters.put("logoEroskiPath", this.getLogoEroski(locale));
		
		//Tabla1
		parameters.put("detailTable1Column1", headerParams.getNumDocDot());
		parameters.put("detailTable1Column2",
				FechaUtils.formatDateToStringByLocale(headerParams.getFecFactura(), locale));
		parameters.put("detailTable1Column3", FechaUtils.formatDateToStringByLocale(headerParams.getFecDesde(), locale)
				.concat(" - ").concat(FechaUtils.formatDateToStringByLocale(headerParams.getFecHasta(), locale)));

		//Tabla2
		parameters.put("lstAlbaranes", lstAlbaranesDTO);
				
		//Tabla3
		parameters.put("lstResumenesIva", lstResumenesIvaDTO);
		
		//Tabla 4
		parameters.put("detailTable4Column1", NumeroUtils.formatNumberToStringByLocale(Float.valueOf(sumTotalFactura.toString()), locale));
		
		//Tabla 5
		parameters.put("retencion", retencionDTO);
		
		parameters.put("pDatasourceAlumno", lstAlumnos);
		parameters.put("pDatasourcePersona", lstPersonas);

		Clob imagenQrClob = repository.getImagenQr();
		String imagenQrStr = null;
		InputStream imagenQrStream = null;
		try {

			imagenQrStr = imagenQrClob.getSubString(1, (int) imagenQrClob.length());
			imagenQrStream = new ByteArrayInputStream(
					org.apache.tomcat.util.codec.binary.Base64.decodeBase64(imagenQrStr.getBytes()));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		parameters.put("imagenQr", imagenQrStream);

		return parameters;
	}

	private SimplePdfExporterConfiguration getPdfExporterConfiguration(Locale locale) throws FileNotFoundException {

		SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();

		exportConfig.setTagged(true);
		exportConfig.setTagLanguage(locale.getLanguage());
		exportConfig.setMetadataAuthor("ALBERTO CUESTA");
		exportConfig.setMetadataTitle("TITULO DEL PDF");
		exportConfig.setDisplayMetadataTitle(Boolean.TRUE);
		exportConfig.setMetadataCreator("EL CREADOR");
		exportConfig.setMetadataSubject("EL SUBJETC");

		File iccFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX.concat(iccPath));

		exportConfig.setIccProfilePath(iccFile.getAbsolutePath());
		exportConfig.setPdfaConformance(PdfaConformanceEnum.PDFA_1A);

		exportConfig.setPdfVersion(PdfVersionEnum.VERSION_1_2);
		exportConfig.setMetadataKeywords("keyword,metadata");

		return exportConfig;
	}

	private byte[] getLogoEroski(Locale locale) {

		File logoEroski;
		byte[] fileContent = null;

		try {
			logoEroski = ResourceUtils.getFile(
					ResourceUtils.CLASSPATH_URL_PREFIX.concat(logoEroskiPath.replace("xx_XX", locale.toString())));
			fileContent = Files.readAllBytes(logoEroski.toPath());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		String imagesHomePath = "src/main/resources/images/";
//		String localeStr = locale.toString();
//		
//		File fi = new File(imagesHomePath.concat("logoEroski").concat(localeStr).concat(".png"));
//		
//		byte[] fileContent = null;
//
//		try {
////			System.out.println("Static en bytes[]: " + new String(Files.readAllBytes(fi.toPath())));
//
//			fileContent = Files.readAllBytes(fi.toPath());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return fileContent;

	}
}
