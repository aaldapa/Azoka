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
 * DTO para mostrar la informacion de la sociedad en la factura 
 * @author BICUGUAL
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SociedadDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sociedad;
	private String nif;
	private String direccion;
	private String cpPoblacionProvincia;
	
}
