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
 * Interfaz de la capa de persistencia
 * @author BICUGUAL
 *
 */
public interface AutofacturaRepository {

	/**
	 * Devuelve los datos de direccion y cp de proveedor
	 * @param codProveedor
	 * @return
	 * @throws DataAccessException
	 */
	public DireccionYcpProveedorEntity getDireccionByCodProveedor(Long codProveedor) throws DataAccessException ;
	
	/**
	 * Devuelve el nombre y el nif del proveedor
	 * @param codProveedor
	 * @return
	 * @throws DataAccessException
	 */
	public NombreYNifProveedorEntity getNombreByCodProveedor(Long codProveedor) throws DataAccessException ;
	
	/**
	 * Devuelve los datos de la sociedad
	 * @param codSociedad
	 * @return
	 * @throws DataAccessException
	 */
	public SociedadEntity getSociedadByCodSociedad(Integer codSociedad) throws DataAccessException ;
	
	/**
	 * Devuelve el codigo de documento de la autofactura
	 * @param codProveedor
	 * @param numDocumento
	 * @param year
	 * @param codSociedad
	 * @return
	 * @throws DataAccessException
	 */
	public Long getCodDocumento(Long codProveedor, String numDocumento, Integer year, Integer codSociedad ) throws DataAccessException;
	
	/**
	 * Devuelve los datos para completar los datos de la primera tabla de la factura
	 * @param codDocumento
	 * @return
	 * @throws DataAccessException
	 */
	public ParametrosCabeceraEntity getHeaderParameters(Long codDocumento) throws DataAccessException;

	/**
	 * Devuelve la lista de albaranes existentes en la factura
	 * @param codDocumento
	 * @return
	 */
	public List<AlbaranEntity> getAlbaranByCodDocumento(Long codDocumento);
	
	/**
	 * Devuelve la lista de tipos de iva existentes en la factura
	 * @param codDocumento
	 * @return
	 */
	public List<ResumenIvaEntity> getResumenTiposIvaByCodDocumento(Long codDocumento);
	
	/**
	 * Devuelve los datos de retencion de la factura
	 * @param codDocumento
	 * @return
	 */
	public RetencionEntity getRetencionByCodDocumento(Long codDocumento);

	/**
	 * Devuelve el codigo Qr de la factura
	 * @return
	 * @throws DataAccessException
	 */
	public Clob getImagenQr() throws DataAccessException;
}
