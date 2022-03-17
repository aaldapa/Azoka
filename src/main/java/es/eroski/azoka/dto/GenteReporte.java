/**
 * 
 */
package es.eroski.azoka.dto;

import java.util.List;

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
public class GenteReporte {

	private List<Alumno> lstAlumnos;
	private List<Persona> lstPersonas;
}
