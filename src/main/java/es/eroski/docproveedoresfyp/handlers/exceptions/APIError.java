package es.eroski.docproveedoresfyp.handlers.exceptions;

import java.sql.Timestamp;
import java.util.Date;

public class APIError{
	
	private  int status;
	private  int customcode;
	private  String error;
	private  String message;
	private  String rootcause;
	private Timestamp timestamp;
	  
	public APIError(int status, int customcode, String error, String message, String rootcause) {
		super();
		this.status = status;
		this.customcode = customcode;
		this.error = error;
		this.message = message;
		this.rootcause = rootcause;
		Date date= new Date();
		long time = date.getTime();
		this.setTimestamp(new Timestamp(time));		
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCustomcode() {
		return customcode;
	}
	public void setCustomcode(int customcode) {
		this.customcode = customcode;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRootcause() {
		return rootcause;
	}
	public void setRootcause(String rootcause) {
		this.rootcause = rootcause;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	} 
}
