/**
 * 
 */
package es.eroski.azoka.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author BICUGUAL
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParametrosCabeceraEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String numDocDot;
	private Date fecFactura;
	private Date fecDesde;
	private Date fecHasta;
	private Long codProvGen;
	private Integer anoDocDot;
	
}
