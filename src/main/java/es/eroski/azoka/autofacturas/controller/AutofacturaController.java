/**
 * 
 */
package es.eroski.azoka.autofacturas.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.eroski.azoka.autofactura.service.AutofacturaService;
import es.eroski.azoka.exceptions.CustomResponseStatusException;
import es.eroski.azoka.utils.Utils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * @author BICUGUAL
 *
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/proveedores/{codProveedor}/documentos/{numeroDocumento}")
@OpenAPIDefinition(info = @Info(title = "Example API", version = "1.0", description = "Servicio para la consulta de autofacturas en formato PDF"))
@PreAuthorize("user")
public class AutofacturaController {

	private static final Logger logger = LoggerFactory.getLogger(AutofacturaController.class);
	
	@Autowired
	AutofacturaService service;

	@SuppressWarnings("unused")
	@Operation(summary = "Obtener autofactura", description = "El servicio obtiene una autofactura en formato PDF")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Autofactura recuperadas", content = {
			@Content(mediaType = MediaType.APPLICATION_PDF_VALUE) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(mediaType = "application/json")}) })

	@GetMapping(produces = { MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<byte[]> report(Locale locale,
			@PathVariable(name = "codProveedor", required = true) Long codProveedor,
			@PathVariable(name = "numeroDocumento", required = true) String numDocumento,
			@RequestParam(name = "anno", required = true) Integer year,
			@RequestParam(name = "codigoSociedad", required = true) Integer codSociedad) {

		logger.info("Entra por la peticion");
		
		byte[] autofactura = null;

		String filename  = Utils.fileNameContructor(codProveedor, numDocumento, year).concat(".pdf");

		try {
			autofactura = service.generateJasperReportPDF(locale, codProveedor, numDocumento, year, codSociedad);
			
			if (null == autofactura) {
				return ResponseEntity.notFound().build();
			}

		} catch (Exception e) {
			logger.error(e.getClass() + " " + e.getMessage());
			throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Error al consultar autofactura",
					e);
		}

		return ResponseEntity.ok()
				.header("Content-Type", "application/pdf; charset=UTF-8")
				.header("Content-Disposition", "inline; filename="+filename).body(autofactura);
	}

}
