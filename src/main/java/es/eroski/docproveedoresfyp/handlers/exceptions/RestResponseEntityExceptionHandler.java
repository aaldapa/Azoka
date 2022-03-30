package es.eroski.docproveedoresfyp.handlers.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.eroski.docproveedoresfyp.exceptions.CustomResponseStatusException;

/*
 * Capturamos todos los errores de tipo CustomResponseStatusException.
 * Esta es una clase desarrollada por nosotros que extiende de ResponseStatusException.
 * 
 * Tiene un atributo mas, customcode que nos permite pasar codigos personalizados al frondEnd.
 * 
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger logger = LogManager.getLogger(RestResponseEntityExceptionHandler.class);
	
    @ExceptionHandler(value = { CustomResponseStatusException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "";
        APIError apierror = new APIError(((CustomResponseStatusException)ex).getStatus().value(),
        								 ((CustomResponseStatusException)ex).getCustomcode(),
        								 ((CustomResponseStatusException)ex).getStatus().getReasonPhrase(),
        								 ((CustomResponseStatusException)ex).getReason(),
        								 ((CustomResponseStatusException)ex).getRootCause().getMessage());						   
        ObjectMapper mapper = new ObjectMapper();
        try {
        	bodyOfResponse =  mapper.writeValueAsString(apierror);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		}
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), ((CustomResponseStatusException)ex).getStatus(), request);
    }
    
   
   
}
