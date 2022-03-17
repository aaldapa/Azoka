package es.eroski.azoka.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomResponseStatusException extends ResponseStatusException{

	
	private static final long serialVersionUID = 1L;
	private int customcode;
	
	public CustomResponseStatusException(HttpStatus status, String reason, Throwable cause) {
		super(status, reason, cause);
	}
	
	public CustomResponseStatusException(HttpStatus status,int customcode, String reason, Throwable cause) {
		super(status, reason, cause);
		this.customcode = customcode;
	}

	public int getCustomcode() {
		return customcode;
	}

	public void setCustomcode(int customcode) {
		this.customcode = customcode;
	}
	 
 }
