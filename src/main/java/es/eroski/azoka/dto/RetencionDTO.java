/**
 * 
 */
package es.eroski.azoka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author BICUGUAL
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetencionDTO {

	private String porcRetencion;
	private String baseIrpf;
	private String impIrpf;
	private String importe;
	
}
