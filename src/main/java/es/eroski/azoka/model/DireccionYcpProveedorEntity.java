/**
 * 
 */
package es.eroski.azoka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
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
