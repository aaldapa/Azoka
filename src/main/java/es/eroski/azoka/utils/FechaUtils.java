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
 * @author BICUGUAL
 *
 */
public class FechaUtils {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(FechaUtils.class);
	
	public static final String pattern_es_ES = "MM/dd/yyyy";
	public static final String pattern_eu_ES = "yyyy/MM/dd";
	

	public static String formatDateToStringByLocale(Date date, Locale locale) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("es_ES".equals(locale.toString()) ? pattern_es_ES : pattern_eu_ES);
		String dateStr = simpleDateFormat.format(date);
		
//		logger.info("La fecha: {} ha sifo formateada a {} - locale {}", date, dateStr, locale);
		
		return dateStr;
	}
	
}
