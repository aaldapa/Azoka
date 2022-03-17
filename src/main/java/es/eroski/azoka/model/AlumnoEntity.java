package es.eroski.azoka.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_alumno")
public class AlumnoEntity{
	
	@Id
	@Column(name = "identifier",unique=true)
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@SequenceGenerator(name = "ALUMNO_PK_SEQ", allocationSize = 1) 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "ALUMNO_PK_SEQ")
    @NotNull
	private int identifier;
	@Column(name = "name")
	private String name;
	@Column(name = "surname")
	private String surname;
	
}
