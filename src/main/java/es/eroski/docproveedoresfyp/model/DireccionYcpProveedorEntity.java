/**
 * 
 */
package es.eroski.docproveedoresfyp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representacion de objeto en el que volcar la consulta de provvedor de la BD
 * @author BICUGUAL
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionYcpProveedorEntity {

	private String direccion;
	private String codigoPostal;
	private String poblacion;
	private String descripcion;
	
}
