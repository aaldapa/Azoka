/**
 * 
 */
package es.eroski.docproveedoresfyp.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mostrar la informacion de los parametros en la primera tabla de la factura
 * @author BICUGUAL
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParametrosCabeceraDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String numDocDot;
	private String fecFactura;
	private String fecDesdeHasta;
	
}
