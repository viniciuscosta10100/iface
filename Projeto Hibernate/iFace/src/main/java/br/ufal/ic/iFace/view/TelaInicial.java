package br.ufal.ic.iFace.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import br.ufal.ic.iFace.Main;
import br.ufal.ic.iFace.bo.UsuarioBO;
import br.ufal.ic.iFace.exceptions.UserNotFoundException;
import br.ufal.ic.iFace.exceptions.ValidationException;
import br.ufal.ic.iFace.interfaces.Serializable;
import br.ufal.ic.iFace.model.EstadoCivil;
import br.ufal.ic.iFace.model.Sexo;
import br.ufal.ic.iFace.model.Usuario;
import br.ufal.ic.iFace.util.Util;

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
		List<Sexo> sexos = Main.sexoRepository.findAll();
		List<Serializable> listaSexo = new ArrayList<Serializable>();
		listaSexo.addAll(sexos);
		for(Sexo s:sexos){
			System.out.println(s.getId()+" - "+s.getNome());
		}
		int idSexo = u.getItemFrom(listaSexo);
		System.out.println("Estado Civil: ");
		List<EstadoCivil> estadosCivis = Main.estadoCivilRepository.findAll();
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
	
	public Usuario login() throws UserNotFoundException{
		System.out.println("------------LOGIN---------------");
		System.out.println("Email: ");
		String email = s.nextLine();
		System.out.println("Senha: ");
		String senha = s.nextLine();
		return Main.usuarioRepository.login(email, senha);
	}

}
