package br.ufal.ic.iFace.exceptions;

public class FriendshipException extends Exception{

	private static final long serialVersionUID = -3746650986060376117L;
	
	private String message;
	
	public FriendshipException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}


}
