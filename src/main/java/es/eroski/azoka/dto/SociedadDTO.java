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
