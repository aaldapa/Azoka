/**
 * 
 */
package es.eroski.azoka.autofactura.persistence;

import java.sql.Clob;
import java.util.List;

import org.springframework.dao.DataAccessException;

import es.eroski.azoka.model.AlbaranEntity;
import es.eroski.azoka.model.ParametrosCabeceraEntity;
import es.eroski.azoka.model.ResumenIvaEntity;
import es.eroski.azoka.model.RetencionEntity;

/**
 * @author BICUGUAL
 *
 */
public interface AutofacturaRepository {

	public Clob getImagenQr() throws DataAccessException;
	
	public Long getCodDocumento(Long codProveedor, String numDocumento, Integer year, Integer codSociedad ) throws DataAccessException;
	
	public ParametrosCabeceraEntity getHeaderParameters(Long codDocumento) throws DataAccessException;

	public List<AlbaranEntity> getAlbaranByCodDocumento(Long codDocumento);
	
	public List<ResumenIvaEntity> getResumenTiposIvaByCodDocumento(Long codDocumento);
	
	public RetencionEntity getRetencionByCodDocumento(Long codDocumento);

}
