package br.ufal.ic.iface.vo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufal.ic.iface.exceptions.ValidationException;
import br.ufal.ic.iface.interfaces.Serializable;

public class Usuario implements Serializable{
	
	public Usuario(){
		init();
	}
	
	public Usuario(String nome, String email, String senha, int idSexo, 
			int idEstadoCivil, Date dataNascimento, String mensagemPerfil) throws ValidationException{
		if(nome.isEmpty() || email.isEmpty() || senha.isEmpty())
			throw new ValidationException("O usuário deve ter nome, email e senha!");
		else{
			init();
			this.nome = nome;
			this.email = email;
			this.senha = senha;
			this.dataNascimento = dataNascimento;
			this.mensagemPerfil = mensagemPerfil;
			this.sexo.setId(idSexo);
			this.estadoCivil.setId(idEstadoCivil);
		}
	}
	
	private int id;
	private String nome;
	private String email;
	private String senha;
	private Sexo sexo;
	private EstadoCivil estadoCivil;
	private Date dataNascimento;
	private String mensagemPerfil;
	private List<Usuario> amigos;
	private List<Mensagem> mensagens;
	private List<Comunidade> comunidades;
	
	private void init(){
		this.sexo = new Sexo();
		this.estadoCivil = new EstadoCivil();
		this.amigos = new ArrayList<Usuario>();
		this.mensagens = new ArrayList<Mensagem>();
		this.comunidades = new ArrayList<Comunidade>();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getMensagemPerfil() {
		return mensagemPerfil;
	}

	public void setMensagemPerfil(String mensagemPerfil) {
		this.mensagemPerfil = mensagemPerfil;
	}
	
	public List<Usuario> getAmigos() {
		return amigos;
	}

	public void setAmigos(List<Usuario> amigos) {
		this.amigos = amigos;
	}
	
	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}

	public List<Comunidade> getComunidades() {
		return comunidades;
	}

	public void setComunidades(List<Comunidade> comunidades) {
		this.comunidades = comunidades;
	}

	public boolean equals(Usuario u){
		return this.email == u.getEmail();
	}
	
	@Override
	public String toString(){
		String s = "Id: "+this.id+"\n"
				+ "Nome: "+this.nome+"\n"
				+ "Email: "+this.email+"\n"
				+ "Sexo: "+this.sexo.getNome()+"\n"
				+ "Data de nascimento: "+new SimpleDateFormat("dd/MM/yyyy").format(this.dataNascimento)+"\n"
				+ "Estado Civil: "+this.estadoCivil.getNome()+"\n"
				+ "Mensagem do perfil: "+this.mensagemPerfil+"\n\n";
		return s;
	}
	
	public String visaoGeral(){
		String s = toString();
		s += "\nAMIGOS\n";
		if(getAmigos() == null || getAmigos().isEmpty()){
			s += "Sem vincúlos com outros usuários\n";
		}else{
			for(Usuario amigo:getAmigos()){
				s += amigo.getId()+" - "+amigo.getNome()+"\n";
			}
		}
		s += "\nMENSAGENS\n";
		if(getMensagens()==null || getMensagens().isEmpty()){
			s += "Nenhuma mensagem recebida\n";
		}else{
			for(Mensagem m:getMensagens()){
				s += m.toString()+"\n";
			}
		}
		s += "\nCOMUNIDADES\n";
		if(getComunidades()==null || getComunidades().isEmpty()){
			s += "Não tem vinculo com nenhuma comunidade\n";
		}else{
			for(Comunidade c:getComunidades()){
				s += c.getId()+" - "+c.getNome()+"\n";
			}
		}
		s += "\n";
		return s;
	}

}
