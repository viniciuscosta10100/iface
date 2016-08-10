package br.ufal.ic.iface.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.ufal.ic.iface.Main;
import br.ufal.ic.iface.bo.UsuarioBO;
import br.ufal.ic.iface.dao.EstadoCivilDAO;
import br.ufal.ic.iface.dao.MensagemDAO;
import br.ufal.ic.iface.dao.SessaoDAO;
import br.ufal.ic.iface.dao.SexoDAO;
import br.ufal.ic.iface.dao.UsuarioDAO;
import br.ufal.ic.iface.interfaces.Serializable;
import br.ufal.ic.iface.util.Util;
import br.ufal.ic.iface.vo.EstadoCivil;
import br.ufal.ic.iface.vo.Mensagem;
import br.ufal.ic.iface.vo.Sexo;
import br.ufal.ic.iface.vo.Usuario;

public class TelaUsuario {
	
	private Scanner s = new Scanner(System.in);
	private Util u = new Util();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	public void menuUsuario(){
		new SessaoDAO().recarregarSessao();
		
		System.out.println("-------------MENU DO USUÁRIO------------");
		System.out.println("Seja bem vindo "+Main.sessao.getUsuario().getNome()+"\n");
		int qtdMsgNaoLidas = Main.sessao.qtdMensagensNaoLidas();
		int qtdSolicitacoes = usuarioDAO.listarSolicitacoesAmizade().size();
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
			System.out.println("6- Visão geral de usuarios");
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
					usuarioDAO.removerUsuario(Main.sessao.getUsuario().getId());
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
		List<Sexo> sexos = new SexoDAO().listarSexos();
		List<Serializable> listaSexo = new ArrayList<Serializable>();
		listaSexo.addAll(sexos);
		for(Sexo s:sexos){
			System.out.println(s.getId()+" - "+s.getNome());
		}
		Main.sessao.getUsuario().getSexo().setId(u.getItemFrom(listaSexo));
		System.out.println("Estado Civil: (Atual - "+Main.sessao.getUsuario().getEstadoCivil().getNome()+")");
		List<EstadoCivil> estadosCivis = new EstadoCivilDAO().listarEstadosCivis();
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
		return usuarioDAO.alterarUsuario(Main.sessao.getUsuario());
	}
	
	private void procurarUsuarios(){
		List<Usuario> usuariosSistema = usuarioDAO.listarNaoAmigos(Main.sessao.getUsuario());
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
		Usuario usuario = usuarioDAO.listarUsuarioPorId(idUsuario);
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
					if(new MensagemDAO().criarMensagem(m))
						System.out.println("Mensagem enviada com sucesso!");
					else
						System.out.println("Erro ao enviar mensagem!");
					break;
				case 2:
					if(!Main.sessao.amigo(usuario)){
						if(new UsuarioBO().enviarSolicitacaoAmizade(usuario)){
							System.out.println("Solicitação enviada com sucesso!");
							usuarioSelecionado = false;
						}else{
							System.out.println("Erro ao enviar solicitação!");
						}
					}else{
						if(usuarioDAO.removerSolicitacaoAmizade(usuario)){
							System.out.println("Amizade desfeita com sucesso!");
							usuarioSelecionado = false;
						}else{
							System.out.println("Erro ao desfazer solicitação de amizade!");
						}
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
		List<Usuario> amigosPendentes = usuarioDAO.listarSolicitacoesAmizade();
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
		Usuario usuario = new UsuarioDAO().listarUsuarioPorId(idUsuario);
		System.out.println("\nUsuário selecionado: "+usuario.getId()+" - "+usuario.getNome());
		boolean usuarioSelecionado = true;
		while(usuarioSelecionado){
			System.out.println("1- Aprovar solicitação");
			System.out.println("2- Rejeitar solicitação");
			System.out.println("3- Voltar");
			int opc =u.getInteger();
			switch(opc){
				case 1:
					if(usuarioDAO.aprovarSolicitacaoAmizade(usuario)){
						System.out.println(usuario.getNome()+" é o seu novo amigo!");
						usuarioSelecionado = false;
					}
					else
						System.out.println("Erro ao adicionar o usuário como amigo");
					break;
				case 2:
					if(usuarioDAO.removerSolicitacaoAmizade(usuario)){
						System.out.println("Solicitação rejeitada com sucesso.");
						usuarioSelecionado = false;
					}
					else
						System.out.println("Erro ao rejeitar solicitação");
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
		List<Mensagem> mensagens = new MensagemDAO().listarMensagensUsuario(Main.sessao.getUsuario());
		if(mensagens.isEmpty()){
			System.out.println("Não há mensagens para você!");
		}else{
			for(Mensagem m:mensagens){
				System.out.println(m.toString());
			}
		}
		System.out.println("\nPressione enter para retornar ao menu anterior");
		new MensagemDAO().marcarMensagensUsuarioLida();
		s.nextLine();
	}
	
}
