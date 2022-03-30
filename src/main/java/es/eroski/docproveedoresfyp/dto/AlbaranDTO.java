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
 * DTO para mostrar la informacion de los albaranes en el reporte
 * @author BICUGUAL
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbaranDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String albProvr;
	private String fecEntrSal;
	private String denomInforme;
	private String baseImp;
	private String tipoIva;
	private String cuotaIva;
	private String importeTotal; 
}
