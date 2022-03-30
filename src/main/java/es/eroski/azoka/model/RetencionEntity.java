/**
 * 
 */
package es.eroski.azoka.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representacion de objeto en el que volcar la consulta de retenciones de la BD
 * @author BICUGUAL
 *
 */

@Data
@NoArgsConstructor
public class RetencionEntity {

	private Float porcRetencion;
	private Float baseIrpf;
	private Float impIrpf;
	private Float importe;
	
}
