/**
 * 
 */
package es.eroski.docproveedoresfyp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representacion de objeto en el que volcar la consulta de resumenes de tipo de iva de la BD
 * @author BICUGUAL
 *
 */
@Data
@NoArgsConstructor
public class ResumenIvaEntity {

	private String descripcion;
	private Float porcentaje;
	private Float baseImp;
	private Float cuotaIva;
	private Float totalAPagar;
	
}
