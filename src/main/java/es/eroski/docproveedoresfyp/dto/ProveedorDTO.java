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
 * DTO para mostrar la informacion del proveedor en la factura
 * @author BICUGUAL
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProveedorDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String nif;
	private String direccion;
	private String cpPoblacionProvincia;
}
