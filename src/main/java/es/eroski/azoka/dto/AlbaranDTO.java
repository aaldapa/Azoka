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
