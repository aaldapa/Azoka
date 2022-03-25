/**
 * 
 */
package es.eroski.azoka.autofactura.service;

import java.sql.Clob;
import java.util.Locale;

/**
 * @author BICUGUAL
 *
 */
public interface AutofacturaService {

	
	public byte[] generateJasperReportPDF(Locale locale, Long codProveedor, String numDocumento, Integer year, Integer codSociedad);
	
	public byte[] getImagenQR();
	
	public Clob getImagenQRClob();
}
