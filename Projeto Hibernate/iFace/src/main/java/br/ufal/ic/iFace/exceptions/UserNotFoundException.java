package br.ufal.ic.iFace.exceptions;

public class UserNotFoundException extends Exception{

	private static final long serialVersionUID = 6070133428689379853L;

	private String message;
	
	public UserNotFoundException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}
}
