/**
 * 
 */
package es.eroski.azoka.autofactura.persistence;

import java.sql.Clob;
import java.util.List;

import org.springframework.dao.DataAccessException;

import es.eroski.azoka.model.AlbaranEntity;
import es.eroski.azoka.model.DireccionYcpProveedorEntity;
import es.eroski.azoka.model.NombreYNifProveedorEntity;
import es.eroski.azoka.model.ParametrosCabeceraEntity;
import es.eroski.azoka.model.ResumenIvaEntity;
import es.eroski.azoka.model.RetencionEntity;
import es.eroski.azoka.model.SociedadEntity;

/**
 * @author BICUGUAL
 *
 */
public interface AutofacturaRepository {

	
	public DireccionYcpProveedorEntity getDireccionByCodProveedor(Long codProveedor) throws DataAccessException ;
	
	public NombreYNifProveedorEntity getNombreByCodProveedor(Long codProveedor) throws DataAccessException ;
	
	public SociedadEntity getSociedadByCodSociedad(Integer codSociedad) throws DataAccessException ;
	
	public Long getCodDocumento(Long codProveedor, String numDocumento, Integer year, Integer codSociedad ) throws DataAccessException;
	
	public ParametrosCabeceraEntity getHeaderParameters(Long codDocumento) throws DataAccessException;

	public List<AlbaranEntity> getAlbaranByCodDocumento(Long codDocumento);
	
	public List<ResumenIvaEntity> getResumenTiposIvaByCodDocumento(Long codDocumento);
	
	public RetencionEntity getRetencionByCodDocumento(Long codDocumento);

	public Clob getImagenQr() throws DataAccessException;
}
