package br.ufal.ic.iFace.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.ufal.ic.iFace.exceptions.ValidationException;
import br.ufal.ic.iFace.interfaces.Serializable;

@Entity
@Table(name="usuario")
public class Usuario implements Serializable{
	
	private static final long serialVersionUID = 7718511923263719338L;

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
	
	@Id
	@GeneratedValue
	private int id;
	
	private String nome;
	
	private String email;
	
	private String senha;
	
	@ManyToOne
	@JoinColumn(name = "sexo_id")
	private Sexo sexo;
	
	@ManyToOne
	@JoinColumn(name = "estado_civil_id")
	private EstadoCivil estadoCivil;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento")
	private Date dataNascimento;
	
	@Column(name = "mensagem_perfil")
	private String mensagemPerfil;
	
	@Transient
	private List<Usuario> amigos;
	
	@Transient
	private List<Mensagem> mensagens;
	
	@Transient
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
		if(dataNascimento!=null)
			return dataNascimento;
		return new Date();
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
				+ "Data de nascimento: "+new SimpleDateFormat("dd/MM/yyyy").format(getDataNascimento())+"\n"
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
