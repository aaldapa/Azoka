/**
 * 
 */
package es.eroski.azoka.model;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representacion de objeto en el que volcar la consulta de Albaranes de la BD
 * @author BICUGUAL
 *
 */
@Data
@NoArgsConstructor
public class AlbaranEntity {

	private String albProvr;
	private Date fecEntrSal;
	private String denomInforme;
	private Float baseImp;
	private Float tipoIva;
	private Float cuotaIva;
	private Float importeTotal; 
	
}
