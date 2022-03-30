/**
 * 
 */
package es.eroski.docproveedoresfyp.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;

import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
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
public class JasperUtils {

	
	public static void setJasperReportsContext() {
		JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();

		jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.font.name",
				"net/sf/jasperreports/fonts/dejavu/DejaVuSans.ttf");
		jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.embedded", "true");
	}

	/**
	 * Compila el template y guarda el objeto compilado y lo retorna
	 * 
	 * @param jrxmlTemplatePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws JRException
	 */
	public static JasperReport reportCompile(String jrxmlTemplatePath) throws FileNotFoundException, JRException {

		File reportMainTemplate = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX.concat(jrxmlTemplatePath));

		// Compilar
		JasperReport reportMain = JasperCompileManager.compileReport(reportMainTemplate.getAbsolutePath());

		// Guardar el compilado
		JRSaver.saveObject(reportMain, reportMainTemplate.getAbsolutePath().replace("jrxml", "jasper"));

		return reportMain;
	}

	/**
	 * 
	 * @param locale
	 * @param iccPath
	 * @return
	 * @throws FileNotFoundException
	 */
	public static SimplePdfExporterConfiguration getPdfExporterConfiguration(Locale locale, String iccPath, String autor, String titulo)
			throws FileNotFoundException {

		SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();

		exportConfig.setTagged(true);
		exportConfig.setTagLanguage(locale.getLanguage());
		exportConfig.setMetadataAuthor(autor);
		exportConfig.setDisplayMetadataTitle(Boolean.TRUE);
		exportConfig.setMetadataTitle(titulo);
		exportConfig.setMetadataCreator("EL CREADOR");
		exportConfig.setMetadataSubject("EL SUBJETC");

		File iccFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX.concat(iccPath));

		exportConfig.setIccProfilePath(iccFile.getAbsolutePath());
		exportConfig.setPdfaConformance(PdfaConformanceEnum.PDFA_1A);

		exportConfig.setPdfVersion(PdfVersionEnum.VERSION_1_2);
		exportConfig.setMetadataKeywords("keyword, metadata");

		return exportConfig;
	}

	/**
	 * Vuelca el pdf con los datos cargados y la configuracion recibida en el outputStream
	 * 
	 * @param jasperPrint
	 * @param outputStream
	 * @param exportConfig
	 * @throws JRException
	 */
	public static void exportarPDF(JasperPrint jasperPrint, ByteArrayOutputStream outputStream,
			SimplePdfExporterConfiguration exportConfig) throws JRException {

		// Exportar el reporte
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		exporter.setConfiguration(exportConfig);
		exporter.exportReport();
	}

}
