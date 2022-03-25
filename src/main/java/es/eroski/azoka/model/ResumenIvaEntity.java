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
public class ResumenIvaEntity {

	private String descripcion;
	private Float porcentaje;
	private Float baseImp;
	private Float cuotaIva;
	private Float totalAPagar;
	
}
