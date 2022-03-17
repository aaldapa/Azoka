/**
 * 
 */
package es.eroski.azoka.report.service.impl;

import java.sql.Clob;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.eroski.azoka.report.persistence.ReportRepository;
import es.eroski.azoka.report.service.ReportService;

/**
 * @author BICUGUAL
 *
 */
@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	ReportRepository reporRepository;

	@Override
	public byte[] getImagenQR() {

		Clob clob = null;
		clob = reporRepository.getImagenQr();

		try {
			return clob.getSubString(1, (int) clob.length()).getBytes();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		try{
//            InputStream ins = clob.getAsciiStream();
//            byte[] byteArray = ins.readAllBytes();
//            System.out.println(new String(byteArray));
//            ins.close();
//            return byteArray;
//        }catch(IOException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		try {
//			return IOUtils.toByteArray(clob.getCharacterStream(), "UTF-8");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return null;

//		byte[] blobAsBytes = null;
//
//		int blobLength;
//		try {
//			blobLength = (int) blob.length();
//			blobAsBytes = blob.getBytes(1, blobLength);
//			//release the blob and free up memory. (since JDBC 4.0)
//			blob.free();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//
//		return blobAsBytes;

	}

	@Override
	public Clob getImagenQRClob() {
		Clob clob = reporRepository.getImagenQr();

		try {
			System.out.println("Hello ! " + clob + " length=" + clob.length());
			System.out.println("Hello ! " + clob.getSubString(1, (int) clob.length()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return clob;
	}

}
