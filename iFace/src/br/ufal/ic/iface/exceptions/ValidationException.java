package br.ufal.ic.iface.exceptions;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 55117254900587174L;
	
	private String message;
	
	public ValidationException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}

}
