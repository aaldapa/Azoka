package es.eroski.azoka.alumnomodule.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.eroski.azoka.model.AlumnoEntity;

@Repository
public interface AlumnoRepository extends JpaRepository<AlumnoEntity,Integer>{

}
