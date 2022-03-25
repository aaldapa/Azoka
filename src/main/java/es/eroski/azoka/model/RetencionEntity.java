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
public class RetencionEntity {

	private Float porcRetencion;
	private Float baseIrpf;
	private Float impIrpf;
	private Float importe;
	
}
