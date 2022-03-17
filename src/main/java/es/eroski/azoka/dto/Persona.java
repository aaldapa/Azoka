package es.eroski.azoka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Persona {
	
	private int id;
	private String nombre;
	private String apellido;

}
