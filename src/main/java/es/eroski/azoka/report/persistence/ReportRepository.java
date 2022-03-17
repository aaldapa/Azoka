/**
 * 
 */
package es.eroski.azoka.report.persistence;

import java.sql.Clob;

import org.springframework.dao.DataAccessException;

/**
 * @author BICUGUAL
 *
 */
public interface ReportRepository {

	Clob getImagenQr() throws DataAccessException;

}
