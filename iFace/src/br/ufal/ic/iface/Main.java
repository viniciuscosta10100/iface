package br.ufal.ic.iface;

import br.ufal.ic.iface.util.Util;
import br.ufal.ic.iface.view.TelaInicial;
import br.ufal.ic.iface.view.TelaUsuario;
import br.ufal.ic.iface.vo.Sessao;
import br.ufal.ic.iface.vo.Usuario;

public class Main {
	
	public final static Sessao sessao = new Sessao();
	
	public static void main(String []args){
		TelaInicial ti = new TelaInicial();
		boolean emExecucao = true;
		System.out.println("-------------IFACE-------------");
		while(emExecucao){
			System.out.println("1- Login");
			System.out.println("2- Cadastre-se");
			int opc = new Util().getInteger();
			switch(opc){
				case 1:
					Usuario user = ti.login();
					if(user == null)
						System.out.println("Email ou senha inválidos! Tente novamente");
					else{
						sessao.setUsuario(user);
						TelaUsuario tu = new TelaUsuario();
						tu.menuUsuario();
					}
				break;
				case 2:
					ti.cadastrarUsuario();
				break;
				default:
					System.out.println("Opção inválida! Digite novamente:\n");
					break;
			}
		}
	}

}
