package br.ufal.ic.iface.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import br.ufal.ic.iface.interfaces.Serializable;

public class Util {
	
	private Scanner s = new Scanner(System.in);
	DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	
	public int getInteger(){
		try{
			return Integer.parseInt(s.nextLine());
		}catch(NumberFormatException e){
			System.out.println("Entrada inválida! Informe um valor inteiro:");
			return getInteger();
		}
	}
	
	public String getEmail(){
		String email;
		EmailValidator ev = new EmailValidator();
		while(!ev.validate(email = s.nextLine())){
			System.out.println("Email digitado inválido! Informe novamente:");
		}
		return email;
	}
	
	public Date receberData(){
		format.setLenient(false);
	    try {
	        return format.parse(s.nextLine());
	    } catch (ParseException e) {
	        System.out.println("Entrada inválida! Informe um valor no formato (DD/MM/AAAA):");
	        return receberData();
	    }
	}
	
	public int getItemFrom(List<Serializable> lista){
		int id = getInteger();
		for(Serializable item:lista){
			if(item.getId() == id)
				return id;
		}
		System.out.println("Entrada inválida! Item informado não existe.\n");
		return getItemFrom(lista);
	}

}
