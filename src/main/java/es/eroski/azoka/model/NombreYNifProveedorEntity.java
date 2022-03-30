/**
 * 
 */
package es.eroski.azoka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representacion de objeto en el que volcar la consulta de proveedor de la BD
 * @author BICUGUAL
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NombreYNifProveedorEntity{

	private String nombre;
	private String nif;

}
