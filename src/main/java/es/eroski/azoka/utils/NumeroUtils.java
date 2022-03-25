/**
 * 
 */
package es.eroski.azoka.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author BICUGUAL
 *
 */
public class NumeroUtils {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(FechaUtils.class);
	
	public static final String pattern_es_ES = "MM/dd/yyyy";
	public static final String pattern_eu_ES = "yyyy/MM/dd";
	

	public static String formatNumberToStringByLocale(Float number, Locale locale) {
		NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
		
		DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
		decimalFormatSymbols.setCurrencySymbol("");
		((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
		
//		nf.setRoundingMode(RoundingMode.UP);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
		
		return nf.format(number);
	}
}
