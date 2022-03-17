package es.eroski.azoka.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaEntity {
	
	private int id;
	private String nombre;
	private String apellido;

}
