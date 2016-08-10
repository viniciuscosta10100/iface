package br.ufal.ic.iface.vo;

public class Sessao {
	
	private Usuario usuario;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public boolean amigo(Usuario user){
		for(Usuario amigo:usuario.getAmigos()){
			if(amigo.getId() == user.getId())
				return true;
		}
		return false; 
	}
	
	public int qtdMensagensNaoLidas(){
		int qtd = 0;
		for(Mensagem mensagem:usuario.getMensagens()){
			if(mensagem.getStatus() == 0)
				qtd++;
		}
		return qtd;
	}
	
}
