/**
 * 
 */
package es.eroski.docproveedoresfyp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representacion de objeto en el que volcar la consulta de sociedades de la BD
 * @author BICUGUAL
 *
 */
@Data
@NoArgsConstructor
public class SociedadEntity {

	private String sociedad;
	private String nif;
	private String direccion;
	private String codigoPostal;
	private String poblacion;
	private String provincia;	
	
}
