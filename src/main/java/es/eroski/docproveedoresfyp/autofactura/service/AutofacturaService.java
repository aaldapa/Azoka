/**
 * 
 */
package es.eroski.docproveedoresfyp.autofactura.service;

import java.util.List;
import java.util.Locale;

import es.eroski.docproveedoresfyp.dto.AlbaranDTO;
import es.eroski.docproveedoresfyp.dto.CodigoQrDTO;
import es.eroski.docproveedoresfyp.dto.ParametrosCabeceraDTO;
import es.eroski.docproveedoresfyp.dto.ProveedorDTO;
import es.eroski.docproveedoresfyp.dto.ResumenIvaDTO;
import es.eroski.docproveedoresfyp.dto.RetencionDTO;
import es.eroski.docproveedoresfyp.dto.SociedadDTO;

/**
 * Interfaz de servicio de la autofactura
 * @author BICUGUAL
 *
 */
public interface AutofacturaService {

	/**
	 * Obtiene los datos del proveedor en base al codProveedor
	 * 
	 * @param codProveedor
	 * @return
	 */
	ProveedorDTO getProveedorDTO(Long codProveedor);

	/**
	 * Obtiene los datos de la sociedad en base al codSociedad
	 * 
	 * @param codSociedad
	 * @return
	 */
	SociedadDTO getSociedadDTO(Integer codSociedad);
	
	/**
	 * Obtiene el codigo del documento con los parametros de entrada al end-point
	 * 
	 * @param codProveedor
	 * @param numDocumento
	 * @param year
	 * @param codSociedad
	 * @return
	 */
	Long getCodDocumento(Long codProveedor, String numDocumento, int year, Integer codSociedad);

	/**
	 * Obtiene los datos con los que cargar la tabla inicial de cabecera del reporte
	 * 
	 * @param codDocumento
	 * @return
	 */
	ParametrosCabeceraDTO getParametrosCabeceraDTO(Long codDocumento);

	/**
	 * Obtiene la lista de registros de albaran
	 * 
	 * @param codDocumento
	 * @return
	 */
	List<AlbaranDTO> getLstAlbaranesDTO(Long codDocumento);

	/**
	 * Obtiene la lista de resumentes de tipo de iva
	 * 
	 * @param codDocumento
	 * @return
	 */
	List<ResumenIvaDTO> getLstResumenesIvaDTO(Long codDocumento);

	/**
	 * En base a una lista de resumenes, obtiene el sumatorio total de la factura.
	 * 
	 * @param lstResumenesIvaDTO
	 * @param locale
	 * @return
	 */
	String getSumTotalFactura(List<ResumenIvaDTO> lstResumenesIvaDTO, Locale locale);

	/**
	 * Obtiene los datos a mostrar en la tabla de retencion del reporte
	 * 
	 * @param codDocumento
	 * @return
	 */
	RetencionDTO getRetencionDTO(Long codDocumento);

	/**
	 * Obtiene el codigo Qr y el tbai para mostrarlo en el reporte
	 * 
	 * @param codDocumento
	 * @return
	 */
	CodigoQrDTO getCodigoQr(Long codDocumento);
	
}
