package br.ufal.ic.iFace;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.ufal.ic.iFace.exceptions.UserNotFoundException;
import br.ufal.ic.iFace.model.Sessao;
import br.ufal.ic.iFace.model.Usuario;
import br.ufal.ic.iFace.repository.AmizadeRepository;
import br.ufal.ic.iFace.repository.ComunidadeMensagemRepository;
import br.ufal.ic.iFace.repository.ComunidadeParticipantesRepository;
import br.ufal.ic.iFace.repository.ComunidadeRepository;
import br.ufal.ic.iFace.repository.EstadoCivilRepository;
import br.ufal.ic.iFace.repository.MensagemRepository;
import br.ufal.ic.iFace.repository.SessaoRepository;
import br.ufal.ic.iFace.repository.SexoRepository;
import br.ufal.ic.iFace.repository.UsuarioRepository;
import br.ufal.ic.iFace.util.Util;
import br.ufal.ic.iFace.view.TelaInicial;
import br.ufal.ic.iFace.view.TelaUsuario;

public class Main {
	
	public final static SessaoRepository sessaoRepository = new SessaoRepository();
	public final static UsuarioRepository usuarioRepository = new UsuarioRepository();
	public final static AmizadeRepository amizadeRepository = new AmizadeRepository();
	public final static SexoRepository sexoRepository = new SexoRepository();
	public final static EstadoCivilRepository estadoCivilRepository = new EstadoCivilRepository();
	public final static MensagemRepository mensagemRepository = new MensagemRepository();
	public final static ComunidadeRepository comunidadeRepository = new ComunidadeRepository();
	public final static ComunidadeParticipantesRepository comunidadeParticipantesRepository = new ComunidadeParticipantesRepository();
	public final static ComunidadeMensagemRepository comunidadeMensagemRepository = new ComunidadeMensagemRepository();
	
	public final static Sessao sessao = new Sessao();
	
	@SuppressWarnings("deprecation")
	private static SessionFactory sessionFactory = new Configuration().configure("./META-INF/hibernate.cfg.xml").buildSessionFactory();
	public static Session dbsession = sessionFactory.openSession();
	
	public static void main(String []args){
		TelaInicial ti = new TelaInicial();
		boolean emExecucao = true;
		System.out.println("-------------IFACE-------------");
		while(emExecucao){
			System.out.println("1- Login");
			System.out.println("2- Cadastre-se");
			System.out.println("3- Encerrar");
			int opc = new Util().getInteger();
			switch(opc){
				case 1:
					try{
						Usuario user = ti.login();
						sessao.setUsuario(user);
						TelaUsuario tu = new TelaUsuario();
						tu.menuUsuario();
					}catch (UserNotFoundException e) {
						System.out.println(e.getMessage());
					}
				break;
				case 2:
					ti.cadastrarUsuario();
				break;
				case 3:
					emExecucao = false;
					dbsession.close();
					break;
				default:
					System.out.println("Opção inválida! Digite novamente:\n");
					break;
			}
		}
	}

}
