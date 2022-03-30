/**
 * 
 */
package es.eroski.docproveedoresfyp.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase de utilidad para trabajar con numeros
 * 
 * @author BICUGUAL
 *
 */
public class NumeroUtils {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(FechaUtils.class);

	public static final int DEFAULT_MAX_FRACTION_DIGITS = 2;
	public static final int DEFAULT_MIN_FRACTION_DIGITS = 2;

	/**
	 * Devuelve String con el numero en formato moneda (sin el simbolo) en funcion
	 * de la locale con el numero de digitos decimales minimos y maximos
	 * configurados por defecto.
	 * 
	 * Si la locale informada es null, se utiliza la locale por defecto. Si el
	 * numero informado es null, se devuelve null
	 * 
	 * @param number
	 * @param locale
	 * @return
	 */
	public static String formatNumberToStringByLocale(Float number, Locale locale) {
		return formatNumberToStringByLocale(number, locale, DEFAULT_MAX_FRACTION_DIGITS, DEFAULT_MIN_FRACTION_DIGITS);
	}

	/**
	 * Devuelve String con el numero en formato moneda (sin el simbolo) en funcion
	 * de la locale con el numero de digitos decimales minimos y maximos pasados por
	 * parametro.
	 * 
	 * Si la locale informada es null, se utiliza la locale por defecto. Si el
	 * numero informado es null, se devuelve null
	 * 
	 * @param number
	 * @param locale
	 * @param maxFractionDigits
	 * @param minFractionDigits
	 * @return
	 */
	public static String formatNumberToStringByLocale(Float number, Locale locale, int maxFractionDigits,
			int minFractionDigits) {

		if (null != number) {

			//Si la locale es nula o no es ni español o euskera me quedo con la locale por defecto
			if (null == locale
					|| (null != locale && (!"eu".equals(locale.getLanguage()) && !"es".equals(locale.getLanguage())) ) ) {

				locale = Locale.getDefault();
			}

			DecimalFormat nf = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
			// nf.setRoundingMode(RoundingMode.UP);
			nf.setMinimumFractionDigits(minFractionDigits);
			nf.setMaximumFractionDigits(maxFractionDigits);

			DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
			decimalFormatSymbols.setCurrencySymbol("");
			((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

			return nf.format(number);
		}

		return null;
	}

	/**
	 * Formatea String formato teniendo en cuanta la locale.
	 * 
	 * Si la locale informada es null, se utiliza la locale por defecto. Si el
	 * numero informado es null, se devuelve null
	 * 
	 * @param numberStr
	 * @param locale
	 * @return
	 */
	public static Double formatStringToDoubleByLocale(String numberStr, Locale locale) {

		if (null != numberStr) {

			//Si la locale es nula o no es ni español o euskera me quedo con la locale por defecto 
			if (null == locale
					|| (null != locale && (!"eu".equals(locale.getLanguage()) && !"es".equals(locale.getLanguage())) ) ) {

				locale = Locale.getDefault();
			}

			NumberFormat nf = NumberFormat.getInstance(locale);

			try {

				return nf.parse(numberStr).doubleValue();

			} catch (ParseException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}

		return null;

	}
}
