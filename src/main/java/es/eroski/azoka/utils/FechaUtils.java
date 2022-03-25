/**
 * 
 */
package es.eroski.azoka.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase de utilidad para el manejo de fechas
 * @author BICUGUAL
 *
 */
public class FechaUtils {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(FechaUtils.class);

	public static final String PATTERN_ES_ES = "dd/MM/yyyy";
	public static final String PATTERN_EU_ES = "yyyy/MM/dd";

	/**
	 * Dada una fecha y una locale, devuelve la fecha formateada a String utilizando el pattern_es_ES
	 * en euskera si la locale es eu_ES y el pattern_eu_ES sino.
	 * 
	 * Si la locale informada es null, se utiliza la locale por defecto.
	 * Si la fecha informada es null, se devuelve null
	 *  
	 * @param date
	 * @param locale
	 * @return
	 */
	public static String formatDateToStringByLocale(Date date, Locale locale) {

		if (null != date) {

			if (null == locale) {
				locale = Locale.getDefault();
			}

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"es_EU".equals(locale.toString()) ? PATTERN_EU_ES : PATTERN_ES_ES);

			return simpleDateFormat.format(date);

		}

		return null;
	}

}
