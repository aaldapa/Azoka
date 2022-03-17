/**
 * 
 */
package es.eroski.azoka.report.persistence.impl;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.eroski.azoka.report.persistence.ReportRepository;

/**
 * @author BICUGUAL
 *
 */
@Repository
public class ReporRepositoryImpl implements ReportRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private class Mapper implements RowMapper<Clob> {

		public Clob mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getClob(1);
		}

	}

	@Override
	public Clob getImagenQr() throws DataAccessException {

		String sql = "SELECT imagen_qr " + " from cab_fact_rfact where cod_cab_fact_rfact = 8756753 ";
		try {
			Clob clob = jdbcTemplate.queryForObject(sql, new Mapper());
			return clob;
		} catch (DataAccessException e) {

			throw e;
		}

	}

}
