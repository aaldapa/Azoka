/**
 * 
 */
package es.eroski.azoka.dto;

import java.io.Serializable;

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
public class RetencionDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String porcRetencion;
	private String baseIrpf;
	private String impIrpf;
	private String importe;
	
}