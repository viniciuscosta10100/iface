package br.ufal.ic.iface.vo;

import br.ufal.ic.iface.interfaces.Serializable;

public class Mensagem implements Serializable {

	private int id;
	private Usuario remetente;
	private Usuario destinatario;
	private String texto;
	private int status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Usuario getRemetente() {
		return remetente;
	}
	public void setRemetente(Usuario remetente) {
		this.remetente = remetente;
	}
	public Usuario getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(Usuario destinatario) {
		this.destinatario = destinatario;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Override
	public String toString(){
		String s = remetente.getNome()+": "+texto;
		if(status == 0)
			s+=" (NOVA)";
		return s;
	}
	
}
