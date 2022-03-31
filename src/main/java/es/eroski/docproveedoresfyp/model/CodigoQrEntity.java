/**
 * 
 */
package es.eroski.docproveedoresfyp.model;

import java.sql.Clob;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author BICUGUAL
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodigoQrEntity {

	private String idTbai;
	private Clob imagenQr;
	
}
