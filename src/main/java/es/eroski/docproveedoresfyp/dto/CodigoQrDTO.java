/**
 * 
 */
package es.eroski.docproveedoresfyp.dto;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author BICUGUAL
 *
 */
@Data
@AllArgsConstructor
@Builder
public class CodigoQrDTO {

	private String idTbai;
	private InputStream imagenQr;
	
}
