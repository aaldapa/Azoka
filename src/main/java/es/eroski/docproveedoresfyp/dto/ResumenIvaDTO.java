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
 * DTO para mnostrar la informacion de tipos de iva en la factura
 * @author BICUGUAL
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumenIvaDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String descripcion;
	private String porcentaje;
	private String baseImp;
	private String cuotaIva;
	private String totalAPagar;
	
}
