package br.ufal.ic.iface.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import br.ufal.ic.iface.bo.UsuarioBO;
import br.ufal.ic.iface.dao.EstadoCivilDAO;
import br.ufal.ic.iface.dao.SexoDAO;
import br.ufal.ic.iface.dao.UsuarioDAO;
import br.ufal.ic.iface.exceptions.ValidationException;
import br.ufal.ic.iface.interfaces.Serializable;
import br.ufal.ic.iface.util.Util;
import br.ufal.ic.iface.vo.EstadoCivil;
import br.ufal.ic.iface.vo.Sexo;
import br.ufal.ic.iface.vo.Usuario;

public class TelaInicial {
	
	private Scanner s = new Scanner(System.in);
	private Util u = new Util();
	
	public boolean cadastrarUsuario(){
		System.out.println("Nome: ");
		String nome = s.nextLine();
		System.out.println("Email: ");
		String email = u.getEmail();
		System.out.println("Senha: ");
		String senha = s.nextLine();
		System.out.println("Sexo: ");
		List<Sexo> sexos = new SexoDAO().listarSexos();
		List<Serializable> listaSexo = new ArrayList<Serializable>();
		listaSexo.addAll(sexos);
		for(Sexo s:sexos){
			System.out.println(s.getId()+" - "+s.getNome());
		}
		int idSexo = u.getItemFrom(listaSexo);
		System.out.println("Estado Civil: ");
		List<EstadoCivil> estadosCivis = new EstadoCivilDAO().listarEstadosCivis();
		List<Serializable> listaEstadoCivil = new ArrayList<Serializable>();
		listaEstadoCivil.addAll(estadosCivis);
		for(EstadoCivil s:estadosCivis){
			System.out.println(s.getId()+" - "+s.getNome());
		}
		int idEstadoCivil = u.getItemFrom(listaEstadoCivil);
		System.out.println("Data de nascimento: ");
		Date dataNascimento = u.receberData();
		System.out.println("Mensagem de perfil: ");
		String mensagemPerfil = s.nextLine();
		try{
			Usuario u = new Usuario(nome, email, senha, idSexo, idEstadoCivil, dataNascimento, mensagemPerfil);
			if(new UsuarioBO().cadastrarUsuario(u)){
				System.out.println("Usuário cadastrado com sucesso!");
				return true;
			}
			return false;
		}catch(ValidationException e){
			System.out.println(e.getMessage());
			return cadastrarUsuario();
		}
	}
	
	public Usuario login(){
		System.out.println("------------LOGIN---------------");
		System.out.println("Email: ");
		String email = s.nextLine();
		System.out.println("Senha: ");
		String senha = s.nextLine();
		return new UsuarioDAO().login(email, senha);
	}

}
