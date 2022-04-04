/**
 * 
 */
package es.eroski.docproveedoresfyp.autofactura.jasper;

/**
 * @author BICUGUAL
 *
 */
public interface JasperManagerService {

	/**
	 * Generacion del PDF de autofactura
	 * @param codProveedor
	 * @param numDocumento
	 * @param year
	 * @param codSociedad
	 * @return
	 */
	public byte[] getAutofacturaReport(Long codProveedor, String numDocumento, Integer year,
			Integer codSociedad);
	
}
