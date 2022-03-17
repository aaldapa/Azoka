/**
 * 
 */
package es.eroski.azoka.report.service;

import java.sql.Clob;

/**
 * @author BICUGUAL
 *
 */
public interface ReportService {

	public byte[] getImagenQR();
	
	public Clob getImagenQRClob();
}
