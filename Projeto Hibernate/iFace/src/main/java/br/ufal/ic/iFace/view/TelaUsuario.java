package br.ufal.ic.iFace.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.ufal.ic.iFace.Main;
import br.ufal.ic.iFace.bo.AmizadeBO;
import br.ufal.ic.iFace.exceptions.FriendshipException;
import br.ufal.ic.iFace.interfaces.Serializable;
import br.ufal.ic.iFace.model.EstadoCivil;
import br.ufal.ic.iFace.model.Mensagem;
import br.ufal.ic.iFace.model.Sexo;
import br.ufal.ic.iFace.model.Usuario;
import br.ufal.ic.iFace.util.Util;

public class TelaUsuario {
	
	private Scanner s = new Scanner(System.in);
	private Util u = new Util();

	public void menuUsuario(){
		Main.sessaoRepository.recarregarSessao();
		
		System.out.println("-------------MENU DO USUÁRIO------------");
		System.out.println("Seja bem vindo "+Main.sessao.getUsuario().getNome()+"\n");
		int qtdMsgNaoLidas = Main.sessao.qtdMensagensNaoLidas();
		int qtdSolicitacoes = Main.amizadeRepository.listarSolicitacoesAmizade().size();
		if(qtdMsgNaoLidas > 0)
			System.out.println("Você tem ["+qtdMsgNaoLidas+"] mensagens não lidas");
		if(qtdSolicitacoes > 0)
			System.out.println("Você tem ["+qtdSolicitacoes+"] solicitações de amizade pendentes");
		
		boolean usuarioLogado = true;
		while(usuarioLogado){
			System.out.println("1- Editar perfil");
			System.out.println("2- Procurar usuários");
			System.out.println("3- Verificar solicitações");
			System.out.println("4- Mensagens");
			System.out.println("5- Comunidades");
			System.out.println("6- Visão geral da conta");
			System.out.println("7- Excluir conta");
			System.out.println("8- Sair");
			int opc = u.getInteger();
			switch(opc){
				case 1:
					if(editarPerfil())
						System.out.println("Perfil atualizado com sucesso!");
					else
						System.out.println("Erro ao atualizar perfil");
					break;
				case 2:
					procurarUsuarios();
					break;
				case 3:
					mostrarSolicitacoesPendentes();
					break;
				case 4:
					menuVerMensagens();
					break;
				case 5:
					new TelaComunidade().menuComunidades();
					break;
				case 6:
					System.out.println(Main.sessao.getUsuario().visaoGeral());
					System.out.println("Pressione enter para retornar ao menu anterior");
					s.nextLine();
					break;
				case 7:
					Main.usuarioRepository.removerUsuario(Main.sessao.getUsuario());
					System.out.println("Usuário removido do sistema!");
					usuarioLogado = false;
					break;
				case 8:
					usuarioLogado = false;
					break;
				default:
					System.out.println("Opção inválida! Informe novamente: ");
					break;
			}
		}
		
	}
	
	private boolean editarPerfil(){
		System.out.println("----------EDITAR PERFIL-----------");
		System.out.println("Nome: (Atual - "+Main.sessao.getUsuario().getNome()+")");
		Main.sessao.getUsuario().setNome(s.nextLine());
		System.out.println("Senha: ");
		Main.sessao.getUsuario().setSenha(s.nextLine());
		System.out.println("Sexo: (Atual - "+Main.sessao.getUsuario().getSexo().getNome()+")");
		List<Sexo> sexos = Main.sexoRepository.findAll();
		List<Serializable> listaSexo = new ArrayList<Serializable>();
		listaSexo.addAll(sexos);
		for(Sexo s:sexos){
			System.out.println(s.getId()+" - "+s.getNome());
		}
		Main.sessao.getUsuario().getSexo().setId(u.getItemFrom(listaSexo));
		System.out.println("Estado Civil: (Atual - "+Main.sessao.getUsuario().getEstadoCivil().getNome()+")");
		List<EstadoCivil> estadosCivis = Main.estadoCivilRepository.findAll();
		List<Serializable> listaEstadoCivil = new ArrayList<Serializable>();
		listaEstadoCivil.addAll(estadosCivis);
		for(EstadoCivil s:estadosCivis){
			System.out.println(s.getId()+" - "+s.getNome());
		}
		Main.sessao.getUsuario().getEstadoCivil().setId(u.getItemFrom(listaEstadoCivil));
		System.out.println("Data de nascimento: (Atual - "+new SimpleDateFormat("dd/MM/yyyy").format(
				Main.sessao.getUsuario().getDataNascimento())+")");
		Main.sessao.getUsuario().setDataNascimento(u.receberData());
		System.out.println("Mensagem de perfil: (Atual - "+Main.sessao.getUsuario().getMensagemPerfil()+")");
		Main.sessao.getUsuario().setMensagemPerfil(s.nextLine());
		return Main.usuarioRepository.update(Main.sessao.getUsuario())!=null;
	}
	
	private void procurarUsuarios(){
		List<Usuario> usuariosSistema = Main.usuarioRepository.findAll();
		if(usuariosSistema.isEmpty())
			System.out.println("Nenhum outro usuário cadastrado!");
		else{
			for(Usuario u:usuariosSistema){
				System.out.println(u.toString());
			}
			List<Serializable> listaUsuario = new ArrayList<Serializable>();
			listaUsuario.addAll(usuariosSistema);
			System.out.println("Id do usuário: ");
			menuSelecaoUsuario(u.getItemFrom(listaUsuario));
		}
		
	}
	
	private void menuSelecaoUsuario(int idUsuario){
		Usuario usuario = Main.usuarioRepository.findById(idUsuario);
		System.out.println("\nUsuário selecionado: "+usuario.getId()+" - "+usuario.getNome());
		boolean usuarioSelecionado = true;
		while(usuarioSelecionado){
			System.out.println("1- Enviar mensagem");
			if(!Main.sessao.amigo(usuario))
				System.out.println("2- Enviar solicitação de amizade");
			else
				System.out.println("2- Desfazer vinculo de amizade");
			System.out.println("3- Voltar");
			int opc = new Util().getInteger();
			switch(opc){
				case 1:
					System.out.println("Digite a mensagem: ");
					Mensagem m = new Mensagem();
					m.setDestinatario(usuario);
					m.setRemetente(Main.sessao.getUsuario());
					m.setStatus(0);
					m.setTexto(s.nextLine());
					if(Main.mensagemRepository.save(m) != null)
						System.out.println("Mensagem enviada com sucesso!");
					else
						System.out.println("Erro ao enviar mensagem!");
					break;
				case 2:
					try{
						if(!Main.sessao.amigo(usuario)){
							if(new AmizadeBO().enviarSolicitacaoAmizade(usuario)){
								System.out.println("Solicitação enviada com sucesso!");
								usuarioSelecionado = false;
							}else{
								System.out.println("Erro ao enviar solicitação!");
							}
						}else{
							Main.amizadeRepository.removerSolicitacaoAmizade(usuario);
							System.out.println("Amizade desfeita com sucesso!");
							usuarioSelecionado = false;
						}
					}catch(FriendshipException e){
						System.out.println(e.getMessage());
					}
					break;
				case 3:
					usuarioSelecionado = false;
					break;
				default:
					System.out.println("Opção inválida! Informe novamente: ");
					break;
			}
		}
	}
	
	private void mostrarSolicitacoesPendentes(){
		List<Usuario> amigosPendentes = Main.amizadeRepository.listarSolicitacoesAmizade();
		if(amigosPendentes.isEmpty()){
			System.out.println("Nenhuma solicitação pendente!");
		}else{
			for(Usuario amigo:amigosPendentes){
				System.out.println(amigo.toString());
			}
			List<Serializable> listaUsuario = new ArrayList<Serializable>();
			listaUsuario.addAll(amigosPendentes);
			System.out.println("Id do usuário: ");
			menuSolicitacaoPendente(u.getItemFrom(listaUsuario));
		}
	}
	
	private void menuSolicitacaoPendente(int idUsuario){
		Usuario usuario = Main.usuarioRepository.findById(idUsuario);
		System.out.println("\nUsuário selecionado: "+usuario.getId()+" - "+usuario.getNome());
		boolean usuarioSelecionado = true;
		while(usuarioSelecionado){
			System.out.println("1- Aprovar solicitação");
			System.out.println("2- Rejeitar solicitação");
			System.out.println("3- Voltar");
			int opc =u.getInteger();
			switch(opc){
				case 1:
					try{
						Main.amizadeRepository.aprovarSolicitacaoAmizade(usuario);
						System.out.println(usuario.getNome()+" é o seu novo amigo!");
						usuarioSelecionado = false;
					}catch (FriendshipException e) {
						System.out.println(e.getMessage());
						System.out.println("Erro ao adicionar o usuário como amigo");
					}
					break;
				case 2:
					try{
						Main.amizadeRepository.removerSolicitacaoAmizade(usuario);
						System.out.println("Solicitação rejeitada com sucesso.");
						usuarioSelecionado = false;
					}catch(FriendshipException e){
						System.out.println(e.getMessage());
						System.out.println("Erro ao rejeitar solicitação");
					}
					break;
				case 3:
					usuarioSelecionado = false;
					break;
				default:
					System.out.println("Opção inválida. Digite novamente.");
					break;
			}
		}
	}
	
	public void menuVerMensagens(){
		List<Mensagem> mensagens = Main.mensagemRepository.listarMensagensUsuario(Main.sessao.getUsuario());
		if(mensagens.isEmpty()){
			System.out.println("Não há mensagens para você!");
		}else{
			for(Mensagem m:mensagens){
				System.out.println(m.toString());
			}
		}
		System.out.println("\nPressione enter para retornar ao menu anterior");
		Main.mensagemRepository.marcarMensagensUsuarioLida();
		s.nextLine();
	}
	
}
