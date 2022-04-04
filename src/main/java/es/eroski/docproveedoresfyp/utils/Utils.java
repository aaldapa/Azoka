/**
 * 
 */
package es.eroski.docproveedoresfyp.utils;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author BICUGUAL
 *
 */
public class Utils {

	final static String AUTOFACTURA = "autofactura";
	
	/**
	 * Construye el nombre y el titulo del fichero a mostar
	 * @param codProveedor
	 * @param numDocumento
	 * @param year
	 * @return
	 */
	public static String fileNameContructor(Long codProveedor, String numDocumento, int year) {
		
		Locale locale = LocaleContextHolder.getLocale();
		
		StringBuilder filename = new StringBuilder();
		
		filename.append(AUTOFACTURA);
		filename.append("_");
		filename.append(codProveedor);
		filename.append("_");
		filename.append(numDocumento);
		filename.append("_");
		filename.append(year);
		filename.append("_");
		filename.append(locale.getLanguage());
		
		return filename.toString();
	}
}
