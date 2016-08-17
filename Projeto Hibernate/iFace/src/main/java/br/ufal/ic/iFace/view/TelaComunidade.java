package br.ufal.ic.iFace.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.ufal.ic.iFace.Main;
import br.ufal.ic.iFace.interfaces.Serializable;
import br.ufal.ic.iFace.model.Comunidade;
import br.ufal.ic.iFace.model.ComunidadeMensagem;
import br.ufal.ic.iFace.model.Usuario;
import br.ufal.ic.iFace.util.Util;

public class TelaComunidade {

	private Scanner s = new Scanner(System.in);
	private Util u = new Util();
	
	public void menuComunidades(){
		System.out.println("----------COMUNIDADES-----------");
		boolean menuAtivo = true;
		while(menuAtivo){
			System.out.println("1- Cadastrar comunidade");
			System.out.println("2- Gerenciar suas comunidades");
			System.out.println("3- Procurar comunidades");
			System.out.println("4- Comunidades que você participa");
			System.out.println("5- Voltar");
			int opc = u.getInteger();
			switch(opc){
				case 1:
					if(cadastrarComunidade())
						System.out.println("Comunidade cadastrada com sucesso!");
					else
						System.out.println("Erro ao cadastrar comunidade");
					break;
				case 2:
					menuMinhasComunidades();
					break;
				case 3:
					procurarComunidades();
					break;
				case 4:
					comunidadesParticipantes();
					break;
				case 5:
					menuAtivo = false;
					break;
				default:
					System.out.println("Opção inválida! Digite novamente.");
					break;
			}
		}
	}
	
	public boolean cadastrarComunidade(){
		System.out.println("Digite o nome da comunidade: ");
		String nome = s.nextLine();
		System.out.println("Digite a descrição da comunidade: ");
		String descricao = s.nextLine();
		Comunidade comunidade = new Comunidade(nome, descricao);
		if(Main.comunidadeRepository.save(comunidade) == null){
			System.out.println("Erro ao cadastrar comunidade");
			return false;
		}
		return true;
			
	}
	
	public void menuMinhasComunidades(){
		List<Comunidade> comunidades = Main.comunidadeRepository.listarMinhasComunidades();
		if(comunidades.isEmpty())
			System.out.println("Você não possui comunidades!");
		else{
			for(Comunidade c:comunidades){
				System.out.println(c.toString());
			}
			List<Serializable> listaComunidades = new ArrayList<Serializable>();
			listaComunidades.addAll(comunidades);
			System.out.println("Id da comunidade: ");
			gerenciarComunidade(u.getItemFrom(listaComunidades));
		}
	}
	
	public void procurarComunidades(){
		List<Comunidade> comunidades = Main.comunidadeRepository.procurarComunidades();
		if(comunidades.isEmpty())
			System.out.println("Não existem comunidades que você não participa cadastradas no sistema!");
		else{
			for(Comunidade c:comunidades){
				System.out.println(c.toString());
			}
			List<Serializable> listaComunidades = new ArrayList<Serializable>();
			listaComunidades.addAll(comunidades);
			System.out.println("Id da comunidade: ");
			detalheComunidade(u.getItemFrom(listaComunidades));
		}
	}
	
	public void comunidadesParticipantes(){
		List<Comunidade> comunidades = Main.sessao.getUsuario().getComunidades();
		if(comunidades.isEmpty())
			System.out.println("Você não participa de nenhuma comunidade!");
		else{
			for(Comunidade c:comunidades){
				System.out.println(c.toString());
			}
			List<Serializable> listaComunidades = new ArrayList<Serializable>();
			listaComunidades.addAll(comunidades);
			System.out.println("Id da comunidade: ");
			opcoesComunidade(u.getItemFrom(listaComunidades));
		}
	}
	
	public void gerenciarComunidade(int idComunidade){
		Comunidade comunidade = Main.comunidadeRepository.findById(idComunidade);
		boolean comunidadeSelecionada = true;
		System.out.println("Comunidade selecionada: "+comunidade.getId()+" - "+comunidade.getNome());
		while(comunidadeSelecionada){
			System.out.println("1- Adicionar participante");
			System.out.println("2- Remover participante");
			System.out.println("3- Voltar");
			int opc = new Util().getInteger();
			switch(opc){
				case 1:
					adicionarParticipante(comunidade);
					break;
				case 2:
					removerParticipante(comunidade);
					break;
				case 3:
					comunidadeSelecionada = false;
					break;
				default:
					System.out.println("Opção inválida. Digite novamente.");
					break;
			}
		}
	}
	
	public void adicionarParticipante(Comunidade comunidade){
		List<Usuario> users = Main.comunidadeParticipantesRepository.listarUsuariosNaoParticipanteComunidade(comunidade);
		if(users.isEmpty())
			System.out.println("Nenhum usuario disponivel!");
		else{
			System.out.println("Selecione o usuario: ");
			
			for(Usuario u:users){
				System.out.println(u.toString());
			}
			List<Serializable> listaUsers = new ArrayList<Serializable>();
			listaUsers.addAll(users);
			System.out.println("Id do usuário: ");
			Usuario u = new Usuario();
			u.setId(this.u.getItemFrom(listaUsers));
			if(Main.comunidadeParticipantesRepository.adicionarParticipanteComunidade(u, comunidade)){
				System.out.println("Usuário adicionado com sucesso");
			}else{
				System.out.println("Erro ao adicionar usuario");
			}
		}
	}
	
	public void removerParticipante(Comunidade comunidade){
		List<Usuario> users = Main.comunidadeParticipantesRepository.listarUsuariosParticipanteComunidade(comunidade);
		if(users.isEmpty())
			System.out.println("Nenhum usuario na comunidade!");
		else{
			System.out.println("Selecione o usuario: ");
			
			for(Usuario u:users){
				System.out.println(u.toString());
			}
			List<Serializable> listaUsers = new ArrayList<Serializable>();
			listaUsers.addAll(users);
			System.out.println("Id do usuário: ");
			Usuario u = new Usuario();
			u.setId(this.u.getItemFrom(listaUsers));
			Main.comunidadeParticipantesRepository.removerParticipanteComunidade(u, comunidade);
			System.out.println("Usuário removido com sucesso");
		}
	}
	
	public void detalheComunidade(int idComunidade){
		Comunidade comunidade = Main.comunidadeRepository.findById(idComunidade);
		boolean comunidadeSelecionada = true;
		System.out.println("Comunidade selecionada: "+comunidade.getId()+" - "+comunidade.getNome());
		while(comunidadeSelecionada){
			System.out.println("1- Entrar na comunidade");
			System.out.println("2- Voltar");
			int opc = new Util().getInteger();
			switch(opc){
				case 1:
					if(Main.comunidadeParticipantesRepository.adicionarParticipanteComunidade(Main.sessao.getUsuario(), comunidade)){
						System.out.println("Você agora é um membro da comunidade "+comunidade.getNome());
						comunidadeSelecionada = false;
					}else{
						System.out.println("Erro ao entrar na comunidade");
					}
					break;
				case 2:
					comunidadeSelecionada = false;
					break;
				default:
					System.out.println("Opção inválida. Digite novamente.");
					break;
			}
		}
	}
	
	public void opcoesComunidade(int idComunidade){
		Comunidade comunidade = Main.comunidadeRepository.findById(idComunidade);
		boolean comunidadeSelecionada = true;
		System.out.println("Comunidade selecionada: "+comunidade.getId()+" - "+comunidade.getNome());
		while(comunidadeSelecionada){
			System.out.println("1- Enviar mensagem");
			System.out.println("2- Ver mensagens");
			System.out.println("3- Sair da comunidade");
			System.out.println("4- Voltar");
			int opc = new Util().getInteger();
			switch(opc){
				case 1:
					System.out.println("Digite a mensagem a ser enviada: ");
					String txt = s.nextLine();
					ComunidadeMensagem msg = new ComunidadeMensagem();
					msg.setComunidade(comunidade);
					msg.setRemetente(Main.sessao.getUsuario());
					msg.setTexto(txt);
					if(Main.comunidadeMensagemRepository.save(msg) != null){
						System.out.println("Mensagem enviada com sucesso");
					}else{
						System.out.println("Erro ao enviar mensagem");
					}
					break;
				case 2:
					System.out.println(comunidade.getNome()+" - Mensagens");
					for(ComunidadeMensagem mensagem:Main.comunidadeMensagemRepository.listarMensagensComunidade(comunidade)){
						System.out.println(mensagem.toString());
					}
					break;
				case 3:
					Main.comunidadeParticipantesRepository.removerParticipanteComunidade(Main.sessao.getUsuario(), comunidade);
					System.out.println("Você foi removido da comunidade "+comunidade.getNome());
					break;
				case 4:
					comunidadeSelecionada = false;
					break;
				default:
					System.out.println("Opcao inválida. Digite novamente.");
					break;
			}
		}
	}
}
