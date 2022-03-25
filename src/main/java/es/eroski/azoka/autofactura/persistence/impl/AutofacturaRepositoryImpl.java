/**
 * 
 */
package es.eroski.azoka.autofactura.persistence.impl;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.eroski.azoka.autofactura.persistence.AutofacturaRepository;
import es.eroski.azoka.model.AlbaranEntity;
import es.eroski.azoka.model.ParametrosCabeceraEntity;
import es.eroski.azoka.model.ResumenIvaEntity;
import es.eroski.azoka.model.RetencionEntity;

/**
 * @author BICUGUAL
 *
 */
@Repository
public class AutofacturaRepositoryImpl implements AutofacturaRepository {

	private static Logger logger = LogManager.getLogger(AutofacturaRepositoryImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Long getCodDocumento(Long codProveedor, String numDocumento, Integer year, Integer codSociedad)
			throws DataAccessException {

		List<Object> params = Arrays.asList(numDocumento, codProveedor, year, codSociedad);

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT cod_doc_cot ");
		sql.append(" FROM doc_cot ");
		sql.append(" WHERE num_doc_cot = ? ");
		sql.append(" AND cod_provr_gen = ? ");
		sql.append(" AND ano_doc_cot = ? ");
		sql.append(" AND cod_soc= ?");

		logger.info("getCodDocumento [SQL] [{}] Params: [{}] : ", sql.toString(), params);

		try {

			return jdbcTemplate.queryForObject(sql.toString(), Long.class, params.toArray());

		} catch (EmptyResultDataAccessException e) {
			logger.error(e);

			return null;
		}
	}

	@Override
	public ParametrosCabeceraEntity getHeaderParameters(Long codDocumento) throws DataAccessException {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT dc.num_doc_cot ");
		sql.append(", f.fec_factura ");
		sql.append(", f.fec_desde ");
		sql.append(", f.fec_hasta ");
		sql.append(", dc.cod_provr_gen ");
		sql.append(", dc.ano_doc_cot ");
		sql.append(" FROM fichas_Fact f , doc_cot dc  ");
		sql.append(" WHERE dc.num_doc_cot = f.num_fact_provr ");
		sql.append(" AND dc.cod_provr_gen = f.cod_provr_gen ");
		sql.append(" AND dc.ano_doc_cot = f.ano_doc ");
		sql.append(" AND dc.cod_doc_cot =  ?");

		logger.info("getHeaderParameters [SQL] [{}] Params: [{}] : ", sql.toString(), codDocumento);

		try {

			return jdbcTemplate.queryForObject(sql.toString(), new ParametrosCabeceraBdMapper(), codDocumento);

		} catch (EmptyResultDataAccessException e) {
			logger.error(e);

			return null;
		}

	}

	@Override
	public List<AlbaranEntity> getAlbaranByCodDocumento(Long codDocumento) {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ca.alb_provr ");
		sql.append(", ca.fec_entr_sal ");
		sql.append(", a.denom_informe ");
		sql.append(", cln.valor_neto_linea base_imp ");
		sql.append(", vi.porcentaje  tipo_iva ");
		sql.append(", cln.valor_neto_linea*vi.porcentaje/100 cuota_iva ");
		sql.append(", cln.valor_neto_linea+ (cln.valor_neto_linea*vi.porcentaje/100) importe_total ");
		sql.append(" FROM doc_cot dc ");
		sql.append(", cab_albaranes ca ");
		sql.append(", lin_albaranes la ");
		sql.append(", concept_lin_niv cln ");
		sql.append(", rel_imp_apagar ria ");
		sql.append(", vigencias_impuestos vi ");
		sql.append(", articulos a ");
		sql.append(" WHERE ca.cod_doc_cot = dc.cod_doc_cot ");
		sql.append(" AND ca.cod_cab_alb = la.cod_cab_alb ");
		sql.append(" AND ca.version_cab_alb = la.version_cab_alb ");
		sql.append(" AND a.cod_articulo = la.cod_articulo ");
		sql.append(" AND la.version_cab_alb = (SELECT max(k.version_cab_alb) FROM lin_albaranes k WHERE k.cod_cab_alb = ca.cod_cab_alb AND  k.version_cab_alb <= ca.version_cab_alb	AND k.cod_lin_alb=la.cod_lin_alb) ");
		sql.append(" AND cln.cod_cab_alb= la.cod_cab_alb ");
		sql.append(" AND cln.cod_lin_alb = la.cod_lin_alb ");
		sql.append(" AND cln.version_cab_alb = la.version_cab_alb ");
		sql.append(" AND ria.cod_rel_imp_apagar(+) = cln.cod_rel_imp_apagar ");
		sql.append(" AND vi.cod_tax(+) = ria.cod_tax_enfac_ori ");
		sql.append(" AND vi.cod_tp_impositivo(+)= ria.cod_tp_impos_enfac_ori ");
		sql.append(" AND vi.fec_desde <= ca.fec_entr_sal ");
		sql.append(" AND vi.fec_hasta >=  ca.fec_entr_sal ");
		sql.append(" AND dc.cod_doc_cot = ? ");
		sql.append(" ORDER BY alb_provr, a.cod_articulo");
		
		logger.info("getAlbaranByCodDocumento [SQL] [{}] Params: [{}] : ", sql.toString(), codDocumento);

		return jdbcTemplate.query(sql.toString(), new AlbaranBdMapper(), codDocumento);

	}

	@Override
	public List<ResumenIvaEntity> getResumenTiposIvaByCodDocumento(Long codDocumento) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ti.descripcion ");
		sql.append(", bd.porcentaje ");
		sql.append(", sum(bd.imp_base) base_imp ");
		sql.append(", sum(bd.imp_base)*bd.porcentaje/100 cuota_iva ");
		sql.append(", sum(bd.imp_base) + (sum(bd.imp_base)*bd.porcentaje/100) total_a_pagar ");
		sql.append(" FROM bases_doc_cot_provr bd ");
		sql.append(", doc_cot dc ");
		sql.append(", sicmauxadm.tipos_impositivos ti ");
		sql.append(" WHERE dc.cod_doc_cot = ? ");
		sql.append(" AND dc.cod_doc_cot = bd.cod_doc_cot ");
		sql.append(" AND bd.cod_tp_impositivo = ti.cod_tp_impositivo ");
		sql.append(" GROUP BY bd.cod_doc_cot, bd.cod_tp_impositivo,ti.descripcion, bd.porcentaje");
		
		logger.info("getResumenTiposIvaByCodDocumento [SQL] [{}] Params: [{}] : ", sql.toString(), codDocumento);
		
		return jdbcTemplate.query(sql.toString(), new ResumenIvaBdMapper(), codDocumento);
	}
	
	
	@Override
	public RetencionEntity getRetencionByCodDocumento(Long codDocumento) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT f.porc_retencion ");
		sql.append(", f.base_irpf " );
		sql.append(", cdcp.imp_irpf ");
		sql.append(", f.importe ");
		sql.append(" FROM cab_fact_provr f ");
		sql.append(", doc_cot dc" );
		sql.append(", cab_doc_cot_provr cdcp ");
		sql.append(" WHERE  dc.num_doc_cot = f.num_fact_provr " );
		sql.append(" AND dc.cod_provr_gen = f.cod_provr_gen ");
		sql.append(" AND dc.ano_doc_cot = f.ano_doc ");
		sql.append(" AND dc.cod_doc_cot = cdcp.cod_doc_cot ");
		sql.append(" AND dc.cod_doc_cot =  ?");

		logger.info("getRetencionByCodDocumento [SQL] [{}] Params: [{}] : ", sql.toString(), codDocumento);

		try {

			return jdbcTemplate.queryForObject(sql.toString(), new RetencionBdMapper(), codDocumento);

		} catch (EmptyResultDataAccessException e) {
			logger.error(e);

			return null;
		}
	}

	private class ParametrosCabeceraBdMapper implements RowMapper<ParametrosCabeceraEntity> {

		public ParametrosCabeceraEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

			ParametrosCabeceraEntity item = new ParametrosCabeceraEntity();

			item.setNumDocDot(rs.getString("num_doc_cot"));
			item.setFecFactura(rs.getDate("fec_factura"));
			item.setFecDesde(rs.getDate("fec_desde"));
			item.setFecHasta(rs.getDate("fec_hasta"));
			item.setCodProvGen(rs.getLong("cod_provr_gen"));
			item.setAnoDocDot(rs.getInt("ano_doc_cot"));

			return item;
		}

	}

	private class AlbaranBdMapper implements RowMapper<AlbaranEntity> {
		
		public AlbaranEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			AlbaranEntity item = new AlbaranEntity();
			
			item.setAlbProvr(rs.getString("alb_provr"));
			item.setFecEntrSal(rs.getDate("fec_entr_sal"));
			item.setDenomInforme(rs.getString("denom_informe"));
			item.setBaseImp(rs.getFloat("base_imp"));
			item.setTipoIva(rs.getFloat("tipo_iva"));
			item.setCuotaIva(rs.getFloat("cuota_iva"));
			item.setImporteTotal(rs.getFloat("importe_total"));
			
			return item;
		}
		
	}
	
	
	private class ResumenIvaBdMapper implements RowMapper<ResumenIvaEntity> {
		
		public ResumenIvaEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			ResumenIvaEntity item = new ResumenIvaEntity();
			
			item.setDescripcion(rs.getString("descripcion"));
			item.setPorcentaje(rs.getFloat("porcentaje"));
			item.setBaseImp(rs.getFloat("base_imp"));
			item.setCuotaIva(rs.getFloat("cuota_iva"));
			item.setTotalAPagar(rs.getFloat("total_a_pagar"));
			
			return item;
		}
		
	}
	
	private class RetencionBdMapper implements RowMapper<RetencionEntity> {
		
		public RetencionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			RetencionEntity item = new RetencionEntity();
			
			item.setPorcRetencion(rs.getFloat("porc_retencion"));
			item.setBaseIrpf(rs.getFloat("base_irpf"));
			item.setImpIrpf(rs.getFloat("imp_irpf"));
			item.setImporte(rs.getFloat("importe"));
			
			return item;
		}
		
	}
	
	
	
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
