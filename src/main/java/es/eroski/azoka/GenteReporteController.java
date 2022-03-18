/**
 * 
 */
package es.eroski.azoka;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.eroski.azoka.dto.Alumno;
import es.eroski.azoka.dto.GenteReporte;
import es.eroski.azoka.dto.Persona;
import es.eroski.azoka.exceptions.CustomResponseStatusException;
import es.eroski.azoka.report.service.ReportService;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

/**
 * @author BICUGUAL
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/gente")
public class GenteReporteController {

	@Autowired
	ReportService reportService;

	@GetMapping("/report")
	public ResponseEntity<byte[]> report(HttpServletResponse response) {
		try {

			byte[] bytes = this.generatePDFReport();
			return ResponseEntity.ok()
					// Specify content type as PDF
					.header("Content-Type", "application/pdf; charset=UTF-8")
					// Tell browser to display PDF if it can
					.header("Content-Disposition", "inline; filename=\"gente.pdf\"").body(bytes);

		} catch (Exception e) {
//	 		log.error(e.getClass() + " " + e.getMessage());
			if (e instanceof DuplicateKeyException) {
				throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, 40001, "Clave primaria duplica", e);
			} else {
				if (e instanceof DataIntegrityViolationException) {
					throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, 40002, "Error integridad", e);
				} else {
					throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 50004,
							"Error creando persona", e);
				}
			}
		}
	}

	public byte[] generatePDFReport() {
		byte[] bytes = null;

		List<Persona> lstPersonas = Arrays.asList(new Persona(1, "Elena", "Nunez"), new Persona(2, "Jose", "Lema"));
		List<Alumno> lstAlumnos = Arrays.asList(new Alumno(1, "Alberto", "Cuesta"), new Alumno(2, "Roberto", "Agirre"));

		try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {

			String reportHomePath = "src/main/resources/reports/";
			File reportMainTemplate = ResourceUtils.getFile("classpath:reports/autofactura.jrxml");
			JasperReport reportMain = JasperCompileManager.compileReport(reportMainTemplate.getAbsolutePath());
			JRSaver.saveObject(reportMain,
					reportHomePath.concat(reportMainTemplate.getName().replace("jrxml", "jasper")));
			

			byte[] imagenQrBA = reportService.getImagenQR();
			Clob imagenQrClob = reportService.getImagenQRClob();
			String imagenQrStr  = null;
			InputStream imagenQrStream = null;
			try {
				
				imagenQrStr= imagenQrClob.getSubString(1, (int) imagenQrClob.length());
				imagenQrStream = new ByteArrayInputStream(org.apache.tomcat.util.codec.binary.Base64.decodeBase64(imagenQrStr.getBytes()));
				

				System.out.println("Stream: " + imagenQrStream);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    File targetFile = new File("src/main/resources/imagen.png");
		    OutputStream outStream = new FileOutputStream(targetFile);
		    outStream.write(imagenQrBA);
		    
		    imagenQrBA = Files.readAllBytes(targetFile.toPath());
		    
		    IOUtils.closeQuietly(outStream);

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("pDatasourceAlumno", lstAlumnos);
			parameters.put("pDatasourcePersona", lstPersonas);

			parameters.put("imagenQr", imagenQrStream);

			JasperPrint jasperPrint = JasperFillManager.fillReport(reportMain, parameters, new JREmptyDataSource());
			

			// return the PDF in bytes
			bytes = JasperExportManager.exportReportToPdf(jasperPrint);



		} catch (JRException | IOException e) {
			e.printStackTrace();
		}

		return bytes;

	}

	public byte[] generatePDFConSubReport() {
		byte[] bytes = null;
		JasperReport jasperReport = null;

		var reportDataSource = new GenteReporte();
		reportDataSource
				.setLstPersonas(Arrays.asList(new Persona(1, "Elena", "Nunez"), new Persona(2, "Jose", "Lema")));
		reportDataSource
				.setLstAlumnos(Arrays.asList(new Alumno(1, "Alberto", "Cuesta"), new Alumno(2, "Roberto", "Agirre")));

		List<GenteReporte> jrBeanCollectionDataSource = Arrays.asList(reportDataSource);

		try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {

			String reportHomePath = "src/main/resources/reports/";
			File reportMainTemplate = ResourceUtils.getFile("classpath:reports/report_main.jrxml");
			JasperReport reportMain = JasperCompileManager.compileReport(reportMainTemplate.getAbsolutePath());
			JRSaver.saveObject(reportMain,
					reportHomePath.concat(reportMainTemplate.getName().replace("jrxml", "jasper")));

			File subreport1Template = ResourceUtils.getFile("classpath:reports/report_sub1.jrxml");
			JasperReport subreport1 = JasperCompileManager.compileReport(subreport1Template.getAbsolutePath());
			String subreport1Path = reportHomePath.concat(subreport1Template.getName().replace("jrxml", "jasper"));
			JRSaver.saveObject(subreport1, subreport1Path);

			File subreport2Template = ResourceUtils.getFile("classpath:reports/report_sub2.jrxml");
			JasperReport subreport2 = JasperCompileManager.compileReport(subreport2Template.getAbsolutePath());
			String subreport2Path = reportHomePath.concat(subreport2Template.getName().replace("jrxml", "jasper"));
			JRSaver.saveObject(subreport2, subreport2Path);

			File subreport3Template = ResourceUtils.getFile("classpath:reports/subreport_tabla.jrxml");
			JasperReport subreport3 = JasperCompileManager.compileReport(subreport3Template.getAbsolutePath());
			String subreport3Path = reportHomePath.concat(subreport3Template.getName().replace("jrxml", "jasper"));
			JRSaver.saveObject(subreport3, subreport3Path);

			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(jrBeanCollectionDataSource);
			byte[] imagenQrBA = reportService.getImagenQR();
			byte[] imagenQrBAStatic = this.getImagenQr();
			Clob imagenQrClob = reportService.getImagenQRClob();
			String imagenQrStr = null;
			InputStream imagenQrStream = null;
			try {

				imagenQrStr = imagenQrClob.getSubString(1, (int) imagenQrClob.length());
				imagenQrStream = new ByteArrayInputStream(
						org.apache.tomcat.util.codec.binary.Base64.decodeBase64(imagenQrStr.getBytes()));

				System.out.println("Stream: " + imagenQrStream);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			File targetFile = new File("src/main/resources/imagen.png");
			OutputStream outStream = new FileOutputStream(targetFile);
			outStream.write(imagenQrBA);

			imagenQrBA = Files.readAllBytes(targetFile.toPath());

			IOUtils.closeQuietly(outStream);

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("report_sub1", subreport1Path);
			parameters.put("report_sub2", subreport2Path);
			parameters.put("subreport_tabla", subreport3Path);
//			parameters.put("imagenQrBAStatic", imagenQrBAStatic);
			parameters.put("imagenQr", imagenQrStream);

			JasperPrint jasperPrint = JasperFillManager.fillReport(reportMain, parameters, dataSource);

			// return the PDF in bytes
			bytes = JasperExportManager.exportReportToPdf(jasperPrint);

		} catch (JRException | IOException e) {
			e.printStackTrace();
		}

		return bytes;

	}

	private byte[] getImagenQr() {
		String imagesHomePath = "src/main/resources/images/";

		File fi = new File(imagesHomePath.concat("cbimage.png"));
		byte[] fileContent = null;

		try {
			System.out.println("Static en bytes[]: " + new String(Files.readAllBytes(fi.toPath())));

			fileContent = Files.readAllBytes(fi.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileContent;

	}

	public File exportToPdf(JasperReport jasperReport, Map<String, Object> parameters,
			JRBeanCollectionDataSource dataSource) throws SQLException, IOException, JRException {

		File tmpFile = Files.createTempFile("userReport", ".pdf").toFile();

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(tmpFile));

		SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
		reportConfig.setSizePageToContent(true);
		reportConfig.setForceLineBreakPolicy(false);

		SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
		exportConfig.setMetadataAuthor("chris");
		exportConfig.setEncrypted(true);
		exportConfig.setAllowedPermissionsHint("PRINTING");

		exporter.setConfiguration(reportConfig);
		exporter.setConfiguration(exportConfig);

		exporter.exportReport();

		return tmpFile;
	}

}
