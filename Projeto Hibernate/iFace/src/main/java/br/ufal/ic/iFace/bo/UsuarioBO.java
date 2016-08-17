package br.ufal.ic.iFace.bo;

import br.ufal.ic.iFace.Main;
import br.ufal.ic.iFace.model.Usuario;

public class UsuarioBO {
	
	public boolean cadastrarUsuario(Usuario u){
		if(Main.usuarioRepository.listarUsuarioPorEmail(u.getEmail())==null){
			Main.usuarioRepository.save(u);
			return true;
		}
		System.out.println("O email informado já existe");
		return false;
	}

}
