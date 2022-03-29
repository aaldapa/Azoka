/**
 * 
 */
package es.eroski.azoka.utils;

/**
 * @author BICUGUAL
 *
 */
public class Utils {

	final static String AUTOFACTURA = "autofactura";
	
	public static String fileNameContructor(Long codProveedor, String numDocumento, int year) {
		StringBuilder filename = new StringBuilder();
		
		filename.append(AUTOFACTURA);
		filename.append("_");
		filename.append(codProveedor);
		filename.append("_");
		filename.append(numDocumento);
		filename.append("_");
		filename.append(year);
		
		return filename.toString();
	}
}
