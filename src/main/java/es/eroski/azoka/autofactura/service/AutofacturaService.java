/**
 * 
 */
package es.eroski.azoka.autofactura.service;

import java.util.Locale;

/**
 * Interfaz de servicio para la generacion del reporte de autofacturas
 * @author BICUGUAL
 *
 */
public interface AutofacturaService {

	/**
	 * Generacion de reporte
	 * @param locale
	 * @param codProveedor
	 * @param numDocumento
	 * @param year
	 * @param codSociedad
	 * @return
	 */
	public byte[] generateJasperReportPDF(Locale locale, Long codProveedor, String numDocumento, Integer year, Integer codSociedad);
	
}
