/**
 * 
 */
package es.eroski.azoka.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
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
