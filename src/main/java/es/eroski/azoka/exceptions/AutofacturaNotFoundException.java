/**
 * 
 */
package es.eroski.azoka.exceptions;

/**
 * Excepcion a lanzar cuando no encontramos recurso 
 * @author BICUGUAL
 *
 */

public class AutofacturaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AutofacturaNotFoundException(String message) {
	    super(message);
	  }
}
