import java.util.ArrayList;
import java.util.HashMap;

import excecoes.PontuacaoInvalidaException;

public class Placar {
	
	private Armazenamento armazenamento;
	
	public Placar(Armazenamento armazenamento) {
		this.armazenamento = armazenamento;
	}

	public void adicionarPontuacao(String usuario, long pontos, String tipo) 
			throws PontuacaoInvalidaException {
		armazenamento.guardarPontuacao(usuario, pontos, tipo);
	}

	public HashMap<String, Integer> pontuacoes(String usuario) {
		HashMap<String, Integer> pontuacoesDoUsuario = new HashMap<String, Integer>();
		ArrayList<String> tiposDePontosDoUsuario = armazenamento.recuperarTiposPontuacao(usuario);
		for(int i = 0; i < tiposDePontosDoUsuario.size(); i++) {
			String tipo = tiposDePontosDoUsuario.get(i);
			int pontos = (int) armazenamento.recuperarPontos(usuario, tipo);
			pontuacoesDoUsuario.put(tipo, pontos);
		}
		
		return pontuacoesDoUsuario;
	}

}
